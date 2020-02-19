package kr.co.corners.mqtt;

import java.util.UUID;

import javax.annotation.PostConstruct;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class MqttService implements IMqttMessageListener {
	//기초설정
	@Value("${mqtt.clientid}")
    private String clientId = "DDDDDDD";
	
	@Value("${mqtt.topic.refresh}")
    private String topic_refresh;
	
	@Value("${mqtt.topic.act}")
	private String topic_act;
	
	@Value("${mqtt.topic.alg}")
	private String topic_alg;
	
	@Value("${mqtt.topic.exercise}")
	private String topic_exercise;
	
	@Value("${mqtt.topic.station}")
	private String topic_station;
	
	
	@Value("${mqtt.qos}")
    private int qos;
    
    @Value("${mqtt.broker.tcp}")
    private String broker_tcp;
    
//    @Value("${mqtt.broker.ssl}")
//    private String broker_ssl;
//    
	@Autowired
	MqttCallbackImpl callback;

	private MqttClient msgReciver = null;
	
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MqttService.class);
    
	public synchronized Boolean SendMqtt(String topic, String jsonString)
	{
		try {
			if(msgReciver != null && msgReciver.isConnected() == true)
			{
				//MqttMessage message = new MqttMessage(jsonString.getBytes());				
				//message.setQos(0);	
				//message.setRetained(false);
				msgReciver.publish(topic, jsonString.getBytes(), 0, false);
			}
			return true;
		}
		catch(MqttException me) {
			logger.info("Send Mqtt Error : " + me.getMessage());
			me.printStackTrace();
			
		}
		return false;
	}
	
	public synchronized Boolean SendAsyncMqtt(String topic, String jsonString)
	{
		try {
			if(msgReciver != null && msgReciver.isConnected() == true)
			{
				msgReciver.publish(topic, jsonString.getBytes(), 0, false);
				return true;
			}
			
		}
		catch(MqttException me) {
			logger.info("Send Async Mqtt Error : " + me.getMessage());
			me.printStackTrace();			
		}
		return false;
	}
	
	
	@PostConstruct
	public void subscribeTopics() {
		try {
			//단순 MQTT 전송
			String uuid = UUID.randomUUID().toString().replace("-", "");
			
			String clientName = clientId + "_" + uuid;
		
			//clientId =  "OneM2MBroker_"+uuid;
			msgReciver = new MqttClient(broker_tcp, clientName, null);
//			msgReciver = new MqttClient(broker_ssl, clientId, null);
			msgReciver.setTimeToWait(4000);
	
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setConnectionTimeout(5000);
			connOpts.setKeepAliveInterval(3000);
			connOpts.setCleanSession(true);
			
			// 사용자이름과 암호로 연결하는 경우 예제
			connOpts.setUserName("corners");
			connOpts.setPassword(new char[] {'!','1','2','3','q','w','e'});

			
			//SSL 이용 연결
			//connOpts.setSocketFactory(SslUtil.getSocketFactory("caFilePath", "clientCrtFilePath", "clientKeyFilePath", "password"));
//			connOpts.setSocketFactory(SslUtil.getSocketFactory());
			//connOpts.setAutomaticReconnect(true);
			
			msgReciver.connect(connOpts);		
			msgReciver.setCallback(callback);
			
			
			
			if( msgReciver.isConnected() == true)
			{
				// publish connection message				
				msgReciver.subscribe("sidp/dev/crd/#", 1, this);				
				logger.info("Subscribe complete : " + clientId);
			}
			
		}
		catch(MqttException me) {
			me.printStackTrace();              
		}		
	}
	

	public void connectionLost(Throwable arg0) {
		logger.info("Connection Lost : " + arg0.getMessage());
		arg0.printStackTrace();		
		subscribeTopics();
	}


	@Autowired
	private kr.co.corners.mqtt.StationActionRequestProcessor stationActionRequestProcessor;
	
	@Autowired
    private kr.co.corners.mqtt.StationConfigRequestProcessor stationConfigRequestProcessor;	
	
	@Autowired
	private kr.co.corners.mqtt.StationConfigResponseProcessor stationConfigResponseProcessor;
	

//	
//	private Object lockObj = new Object();
	@Override
	public void messageArrived(String topic, MqttMessage orgMessage) throws Exception 
	{		
//		synchronized(lockObj)
		{
			try
			{
				byte[] data = orgMessage.getPayload();
				String jsonString = new String(data, "utf-8");
				
				ObjectMapper mapper = new ObjectMapper();
				JsonNode node = mapper.readTree(jsonString);

				JsonNode reqNode = node.get("req");						
				if(reqNode == null)
					return;
				
				JsonNode cmdNode = reqNode.get("cmd");
				char cmd = (char)cmdNode.asText().charAt(0);
				
				JsonNode fromNode = reqNode.get("fr");
				 String fromTopic = fromNode.asText();
				
				JsonNode toNode = reqNode.get("to");
				
				
				final String toTopic = toNode.asText();
				final MqttMessage message = orgMessage;
							
				switch(cmd)
				{
				case 'T':
//					
					new Thread("stationActionRequestProcessor"){
						@Override
						public void run(){
							try {
								stationActionRequestProcessor.messageArrived(toTopic, message);
							} catch (Exception e) {					
								e.printStackTrace();
							}
						}
					}.start();
					
					break;
				case 'C':
				case 'S':
					new Thread("stationConfigResponseProcessor") {
						@Override
						public void run() {
							try {
								stationConfigResponseProcessor.messageArrived(toTopic, message);
							} catch (Exception e) {					
								e.printStackTrace();              
							}
						}
					}.start();				
					break;
				case 'A':
					new Thread("stationConfigRequestProcessor") {
						@Override
						public void run() {
							try {
								stationConfigRequestProcessor.messageArrived(toTopic, message);
							} catch (Exception e) {					
								e.printStackTrace();
							}
						}
					}.start();
					
					break;				
					
				};			
				
				logger.info("Recived Mqtt Msg from :" + fromTopic + ", To : " + toTopic + ", Content : " + jsonString);
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}		
	}

}
