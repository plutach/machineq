package kr.co.corners.mqtt;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.simple.JSONObject;
//import json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.corners.common.Constants;
import kr.co.corners.model.TokenInfo;
import kr.co.corners.redis.dao.JWTTokenSave;
import kr.co.corners.service.devService;
import kr.co.corners.utils.HttpUtil;

@Component
public class StationActionRequestProcessor implements IMqttMessageListener {
	
	@Value("${machineq.server.url.common}")
	private String url;

	JWTTokenSave jwttoken;
	devService devservice;

	private static final Logger logger = LoggerFactory.getLogger(StationActionRequestProcessor.class);
	private String deviceEUI = "70B3D53E6FFFFFD5";
	private String payload = "AAA";

	@Resource(name="stationKeyTemplate") 
    private ValueOperations<String, String> stationKeyValueOper;
	
	private HttpUtil httpUtil = new HttpUtil();
	
	//Application Id (ClientId)
	@Value("${machineQ.clientId}")
	private String clientId;
	
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception 
	{

		
		try
		{
			byte[] data = message.getPayload();
			String jsonString = new String(data, "utf-8");
			
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(jsonString);
		
			JsonNode reqNode = node.get("req");						
			if(reqNode == null)
				return;	
			
			// 어느 스테이션을 보낼것인지 확인
			JsonNode jarray = reqNode.findValue("prm");
			
			try
			{
				// 스테이션 어레이인 경우 (테스터)
				JsonNode jarray2 = jarray.get("st");
				if( jarray2 != null && jarray2.isArray() == true)
				{
					jarray = jarray2;
				}
			}
			catch(Exception ex)
			{				
			}
			
			if( jarray.isArray() == true)
			{
				for (final JsonNode objNode : jarray) {
					
					try
					{
						String sid = objNode.get("sid").toString();		
						logger.info("Recived Mqtt Message (Listener). Station: " + sid + " | Content : " + node.toString());
						
						int nStationSn = Integer.parseInt(sid);
			
//						String deviceID = getDeviceID(nStationSn);
						logger.info("Get Device ID");
						String url = "https://api.machineq.net/v1/devices/"+deviceEUI+"/message";
						
						String content = createMachineQDeviceAction(nStationSn, objNode);
						logger.info("Json Content");
						
						Map<String, String> headers = new LinkedHashMap<String,String>();

						TokenInfo tokenObj = jwttoken.get(this.clientId);	
						String t  = tokenObj.getToken();
	                    //use jwttoken in header
						headers.put("Authorization",t);
						headers.put("Content-Type","application/json");
						//headers.put("Accept","application/json");
					
						httpUtil.sendPost(url, headers, content);
						logger.info("Sending Http Post");
					}
					catch(Exception en)
					{
						en.printStackTrace();
					}					
			    }
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}	
    
	
	
	private String getDeviceID(int nSid) throws Exception
	{
		
			String key = Constants.PREFIX_REDIS_STATION_SERIAL_KEY + nSid;
			{
				return stationKeyValueOper.get(key);
			}

		}
		
	
	private String createMachineQDeviceAction(int stationSn, JsonNode data)
	{	
		try {
		byte[] appdata = makeAppData(stationSn, data);	
		if(appdata!=null) {
			
			
			//byte[] encodedBytes = Base64.getEncoder().encode(appdata);
			JSONObject message = new JSONObject();
//			String Payload = new String(encodedBytes);
//			String Payload = "AA";
			// "TargetPort": "FPort values 1..223",
			String TargetPort = "2";
			

			message.put("DevEUI", deviceEUI);
			message.put("Payload", payload);
			message.put("TargetPort", TargetPort);
			//confirm:true == not to receive a confirmation message
			message.put("Confirm", false);
			//to clear the previously sent message
			message.put("FlushQueue", true);
				
		    String jsonData = message.toString();		    
		    logger.info("JsonData" + jsonData);
		    
		}
	}
		 catch(Exception e) {
				e.printStackTrace();
			}

		return null;
	}
	
	private byte[] makeAppData(int nStationSn, JsonNode data) {

		byte[] datas = new byte[7];		
		int snd = data.get("snd").asInt();
		int rpt = data.get("rpt").asInt();
		int blk = data.get("blk").asInt();
		int ba = data.get("ba").asInt();
		int lo = data.get("lo").asInt();
		int lx = data.get("lx").asInt();
		int br = data.get("br").asInt();
		int bl = data.get("bl").asInt();
		int bu = data.get("bu").asInt();
		int bd = data.get("bd").asInt();
		
		datas[6] = (byte)(((blk << 7) & 0x80) + ((rpt << 6) & 0x40) + (snd & 0x3F));
		datas[5] = (byte)(((bd << 7) & 0x80) + (( bu << 6) & 0x40) + ((bl << 5) & 0x20) + 
				((br << 4) & 0x10) + ((lx << 3) & 0x08) + ((lo << 1) & 0x04) + (ba & 0x01));
		datas[4] = 2;
		datas[3] = 'A';	
				
		datas[2]=(byte)((nStationSn & 0x00FF0000)>>16);
		datas[1]=(byte)((nStationSn & 0x0000FF00)>>8);
		datas[0]=(byte)(nStationSn  & 0x000000FF);

		return datas;
	}	
}
