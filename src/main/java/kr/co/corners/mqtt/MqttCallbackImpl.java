package kr.co.corners.mqtt;


import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class MqttCallbackImpl implements MqttCallback {

	private static final Logger logger = LoggerFactory.getLogger(MqttCallbackImpl.class);

	@Autowired 
	MqttService mqttService;
	
	@Override
	public void connectionLost(Throwable arg0) {
		mqttService.connectionLost(arg0);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {			
	}

	@Override
	public void messageArrived(String arg0, MqttMessage arg1) throws Exception {		
	}

}
