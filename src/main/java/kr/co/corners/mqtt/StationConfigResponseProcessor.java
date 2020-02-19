
package kr.co.corners.mqtt;

import java.util.Base64;

import javax.annotation.Resource;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
//import org.json.JSONObject;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.corners.dao.StationConfigOperator;

@Component
public class StationConfigResponseProcessor implements IMqttMessageListener {

	private static final Logger logger = LoggerFactory.getLogger(StationConfigResponseProcessor.class);

	@Resource(name="stationKeyTemplate") 
    private ValueOperations<String, String> stationKeyValueOper;
	
	@Resource(name="deviceEntityKeyTemplate") 
    private ListOperations<String, Object> devieKeyListOper; 
	
	
	@Autowired
	StationConfigOperator stationConfigOp;
	
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
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
					
				/*	try
					{
						String sid = objNode.get("sid").toString();

						int nStationSn = Integer.parseInt(sid);
						String deviceID = getDeviceID(nStationSn);
												
						ObjectNode objectSt = mapper.createObjectNode();	
						
						int siteId = (Integer)(devieKeyListOper.index(deviceID, Constants.SITE_ID));	
						int stationId = (Integer)(devieKeyListOper.index(deviceID, Constants.STATION_ID));
						
						StationConfig model = stationConfigOp.getStationConfig(nStationSn, stationId, siteId);						
						objectSt.put("dev", model.getDeviceEntityID());
						objectSt.put("sid", model.getSid());
						objectSt.put("tmp", model.getTempConf());
						objectSt.put("vol", model.getVolumeConf());
						objectSt.put("int", model.getIntervalConf());
						objectSt.put("scf", model.getScaleConf());
						objectSt.put("opt", model.getOption());
						objectSt.put("act", model.getDefaultAction());
											
						String xmlContent = createOneM2MDeviceConfigXML( nStationSn, objectSt);
						String url = helper.getRequestDeviceControlURL(deviceID);
												
						Map<String, String> headers = new LinkedHashMap<String,String>();
						String ri = helper.getRequestRI();
						headers.put("X-M2M-RI", ri);
						headers.put("X-M2M-Origin", X_M2M_Origin);
						headers.put("X-MEF-TK", X_MEF_TK);
						headers.put("X-MEF-EKI", X_MEF_EKI);
						headers.put("Content-Type", oneM2MRequestContentType);						
						httpUtil.sendPost(url, headers, xmlContent);
					}
					catch(Exception en)
					{
						en.printStackTrace();
					}
					*/
			    }
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
    
	/*
	
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
	
	*/
	
	private String createOneM2MDeviceConfigXML(int stationSn, JsonNode data){		
		try {
  			
	        byte[] appdata = makeAppData(stationSn, data);
			if( appdata != null)
			{

				byte[] encodedBytes = Base64.getEncoder().encode(appdata);
				JSONObject message = new JSONObject();
				String DevEUI  = "70B3D53E6FFFFFD5";
				//how to convert payload (payloadConverter?)
				String Payload = new String(encodedBytes);		
				String TargetPort = "224";


			    message.put("DevEUI :", DevEUI);
			    message.put("Payload:", Payload);
				message.put("TargetPort", TargetPort);
				//confirm:true == not to receive a confirmation message
				message.put("Confirm", false);
				//to clear the previously sent message
				message.put("FlushQueue", true);
					
				String jsonData = message.toString();
				logger.info("jsonData"+jsonData);
				
		
			}
		}
				catch(Exception e) {
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


