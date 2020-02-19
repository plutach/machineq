package kr.co.corners.mqtt;

import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.simple.JSONObject;
//import org.json.JSONObject;
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
import kr.co.corners.utils.HttpUtil;


@Component
public class StationConfigRequestProcessor implements IMqttMessageListener {

	private static final Logger logger = LoggerFactory.getLogger(StationConfigRequestProcessor.class);
	private String deviceEUI = "70B3D53E6FFFFFD5";

	private JWTTokenSave jwttoken;

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
//						String xmlContent = createOneM2MDeviceConfigXML( nStationSn, objNode);
						String url = "https://api.machineq.net/v1/device/"+deviceEUI+"/message";
					    String content = createMachineQDeviceConfig(nStationSn,objNode);
					    Map<String, String> headers = new LinkedHashMap<String,String>();
					    TokenInfo tokenObj = jwttoken.get(this.clientId);
					    String t = tokenObj.getToken();
					    
					    headers.put("Authorization", t);
					    headers.put("Content-type", "application/json");
				
					    httpUtil.sendPost(url,headers,content);
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
		synchronized(stationKeyValueOper)
		{
			String key = Constants.PREFIX_REDIS_STATION_SERIAL_KEY + nSid;
			
			if( stationKeyValueOper.getOperations().hasKey(key))
			{
				return stationKeyValueOper.get(key);
			}
			else
			{
				throw new Exception("Redis Not found Key. Station : " + nSid);
			}		
		}		
		
	}

	private String createMachineQDeviceConfig(int stationSn, JsonNode data)
	{		
		try {
			byte[] appdata = makeAppData(stationSn, data);
			if( appdata != null )
			{
				byte[] encodedBytes = Base64.getEncoder().encode(appdata);
				String encodedString = new String(encodedBytes);	
				
				JSONObject message = new JSONObject();
				
				//how to convert payload (payloadConverter?)
				String Payload = new String(encodedBytes);		
				String TargetPort = "2";

				message.put("DevEUI :" ,deviceEUI);
				message.put("Payload:", Payload);
				message.put("TargetPort", TargetPort);
				//confirm:true == not to receive a confirmation message
				message.put("Confirm", false);
				//to clear the previously sent message
				message.put("FlushQueue", true);
					
			    String jsonData = message.toString();
			    
			}
		}catch (Exception e){
			e.printStackTrace();
		} 	
		return null;
	}
	
	private byte[] makeAppData(int nStationSn, JsonNode data) {
		
		byte[] datas = new byte[12];
		byte nTmp = (byte)(data.get("tmp").asInt());
		byte nVol = (byte)data.get("vol").asInt();
		byte nInt = (byte)data.get("int").asInt();
		byte nScf = (byte)data.get("scf").asInt();
		byte nOpt = (byte)data.get("opt").asInt();
		int nAction = data.get("act").asInt();
			
		datas[11] = (byte)((nAction & 0x0000FF00)>>8);
		datas[10] = (byte)(nAction  & 0x000000FF);
				
		datas[9] = nOpt; // option
		datas[8] = nInt; // interval
		datas[7] = nScf; // scale
		
		datas[6] = nVol; // volume
		datas[5] = nTmp; // temp		
		datas[4] = 7;
		datas[3] = 'S';	
				
		datas[2]=(byte)((nStationSn & 0x00FF0000)>>16);
		datas[1]=(byte)((nStationSn & 0x0000FF00)>>8);
		datas[0]=(byte)(nStationSn  & 0x000000FF);
		
		return datas;
	}

}


