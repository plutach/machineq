package kr.co.corners.service;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.tomcat.util.net.SSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import kr.co.corners.model.StationConfig;
import kr.co.corners.model.StationStatus;
import kr.co.corners.model.TokenInfo;
import kr.co.corners.model.deviceInfo;
import kr.co.corners.mqtt.MqttSender;
import kr.co.corners.mqtt.StationActionRequestProcessor;
import kr.co.corners.redis.dao.JWTTokenSave;
import kr.co.corners.redis.dao.StationConfigDao;
import kr.co.corners.redis.dao.StationStatusDao;
import kr.co.corners.redis.dao.deviceInfoDao;
import kr.co.corners.utils.HttpUtil;


@SuppressWarnings("deprecation")
@Component
public class devService{
	
	private final static Logger LOGGER = LoggerFactory.getLogger(devService.class);

	@Autowired
	private MqttSender mqtt;

	@Autowired
	private StationStatusDao stationStatusDao;

	private String deviceEUI = "70B3D53E6FFFFFD5";

	@Autowired
	private StationConfigDao stationConfig;

	@Autowired
	private JWTTokenSave jwttoken;

	//Application Id (ClientId)
	@Value("${machineQ.clientId}")
	private String clientId;


	public ResponseEntity<String> ReciveMachineQData(String body) throws Exception{


	 ObjectMapper mapper = new ObjectMapper();
     JsonNode node = mapper.readTree(body.toString()); 	
     String payload =  node.get("payload_hex").asText();  
     String deviceEUI =  node.get("DevEUI").asText(); 

     LOGGER.info("Payload Hex: " + payload);
     LOGGER.info("deviceEUI: " + deviceEUI);
    
     
        
        byte[] decodedBytes = Base64.getDecoder().decode(payload);
        if(decodedBytes != null && decodedBytes.length <6)
        {
       	 throw new Exception("Data Byte Length Wrong!!");
        }

        String s = "";
        for(int i=0; i<decodedBytes.length; i++) {
       		s = s + String.format(" %x", decodedBytes[i]);
        }
        

        LOGGER.info("Recived Byte Data : " + s);      

        int stationSn = (int)(((decodedBytes[2] & 0xFF) << 16) + ((decodedBytes[1] & 0xFF) << 8) + (decodedBytes[0] & 0xFF));
        char command = (char)decodedBytes[3]; // decodedString.substring(3,1)
		byte length = decodedBytes[4];

			if(length + 5 != decodedBytes.length) {
			throw new Exception("Data Byte Length Wrong");
		 }


	  int nSiteId = -1;
	  int nCoordiId = -1;
	  int nStationId = -1;
			
	   if(command == 'R') {
		   
				LOGGER.info("Station SN: " + stationSn + " Command : " + command + " Payload Length : " + length );  
			
	   
       //set deviceEUI
       StationStatus statusModel = stationStatusDao.get(deviceEUI);
  
       if(statusModel == null) {
      	 statusModel = new StationStatus();
         statusModel.setdeviceEUI(deviceEUI);
      	
       }
    
       statusModel.setSid(stationSn);

	   StationConfig configModel = stationConfig.get(deviceEUI);
		if( configModel == null)
		{
			configModel = new StationConfig(); 
//		    configModel = stationConfigOp.getStationConfig(stationSn, nStationId, nSiteId);
			configModel.setdeviceEUI(deviceEUI);
			configModel.setPayload(payload);	
		}
	   
	   
	        int payloadStart = 4;

	        int cfgTemp = decodedBytes[payloadStart +1];
	        if(cfgTemp != configModel.getTempConf()){	        	

	        }
	        
	        int cfgVolume = decodedBytes[payloadStart +2];
	        if(cfgVolume != configModel.getVolumeConf()) {
	        	
	        	
	        }
	        
	        char cfgScale = (char) decodedBytes[payloadStart +3];
	        if(cfgScale != configModel.getScaleConf()) {
	        	
	        }	        
	        
	        byte cfgInterval = decodedBytes[payloadStart+4];
	        if(cfgInterval != configModel.getIntervalConf()) {
	        
	        }
	        byte option = (byte)(decodedBytes[payloadStart + 5] & (byte)3);
	        if(option != configModel.getOption()){
	        
	        }
			
	        int action = decodedBytes[payloadStart + 6] & 0xFF + ((decodedBytes[payloadStart +7] & 0xFF) >> 8);
	        if(action != configModel.getDefaultAction()) {
	        	
	        }
	        
	        
	        configModel.setSid(stationSn);
	        configModel.setOption(option); 
	        configModel.setDefaultAction(action);
	        configModel.setScaleConf(cfgScale);
	        configModel.setIntervalConf(cfgInterval);	       
	        configModel.setVolumeConf(cfgVolume);
	        stationConfig.add(configModel);
	        
			// Read Status
			int readIdx = payloadStart + 8;
			
			int nTempSensorStatus =  (decodedBytes[readIdx]>>7) & 0x01;						
			//int nTemp =  decodedBytes[readIdx] & 0x7F - 30;		
			int nTemp =  decodedBytes[readIdx] & 0x7F;	
			readIdx++;
			
			int nSmokeSensorStatus = (decodedBytes[readIdx]>>3) & 0x01;
			int nSmokeLevel =  decodedBytes[readIdx] & 0x07;						
			readIdx++;
								
			int nBatteryStatus = (decodedBytes[readIdx] & 0x0F) * 10;
			readIdx++;
							
			// 전원 상태   0 : 메인전원, 1: 배터리 사용
			int nPowerStatus = (decodedBytes[readIdx]>>6) & 0x01;						
			// 0: Mute, 1:Min, 2:Middle, 3:Max 
			int nVolume = decodedBytes[readIdx] & 0x03;
			readIdx++;
								
			int nFirmwareVersion = decodedBytes[readIdx];
			readIdx++;
			
			//int nStationStatus =  decodedBytes[readIdx] & 0xFF + (( decodedBytes[readIdx+1] & 0xFF ) >> 8);					
			int nBroadcastTable = decodedBytes[readIdx] & 0x3F;
			int nRepeat    = (decodedBytes[readIdx]>>6) & 0x01;
			int nblink     = (decodedBytes[readIdx]>>7) & 0x01;
			readIdx++;
			
			int beamAll    = decodedBytes[readIdx] & 0x01;						
			int beamCircle = (decodedBytes[readIdx]>>1) & 0x01;
			int beamStop   = (decodedBytes[readIdx]>>3) & 0x01;
			int beamRight  = (decodedBytes[readIdx]>>4) & 0x01;
			int beamLeft   = (decodedBytes[readIdx]>>5) & 0x01;
			int beamUp     = (decodedBytes[readIdx]>>6) & 0x01;
			int beamDown   = (decodedBytes[readIdx]>>7) & 0x01;
			readIdx++;
			
			
			statusModel.setTs(nTempSensorStatus);
			statusModel.setTs(nTempSensorStatus);
			statusModel.setTv(nTemp);
			statusModel.setGs(nSmokeSensorStatus);
			statusModel.setGv(nSmokeLevel);					
			statusModel.setSs(1);
			statusModel.setBlv(nBatteryStatus);
			statusModel.setBus(nPowerStatus);
			statusModel.setSv(nVolume);
			statusModel.setVr(nFirmwareVersion);
			statusModel.setSnd(nBroadcastTable);
			statusModel.setRpt(nRepeat);
			statusModel.setBlk(nblink);													
			statusModel.setBa(beamAll);
			statusModel.setLo(beamCircle);
			statusModel.setLx(beamStop);
			statusModel.setBr(beamRight);
			statusModel.setBl(beamLeft);
			statusModel.setBu(beamUp);
			statusModel.setBd(beamDown);
			statusModel.setdeviceEUI(deviceEUI);
			
		/*	deviceInfo model = new deviceInfo();
		    model.setDevEUI("70B3D53E6FFFFFD5");
		    model.setPayload(payload);
		    model.setTargetPort("2");
		    model.setConfirm(true);
		    model.setFlushQueue(false);
			deviceInfoD.add(model);
		  */
			
			LOGGER.info("Sent mqtt Station Event");
			mqtt.SendStationEvent(nCoordiId, stationSn, statusModel);
//			mqtt.SendActionStation(nCoordiId, stationSn, statusModel);
	        
		    
		    try {
		    	stationStatusDao.remove(statusModel);
		    	stationStatusDao.add(statusModel);
		    	
		    }catch(Exception e) {
		    	LOGGER.error(e.getMessage());
		    	e.printStackTrace();
		    }
	   }
			
	        return new ResponseEntity<String>("", HttpStatus.OK);
  }


public ResponseEntity<String> getDeviceData() throws Exception{
	
	deviceInfo model = new deviceInfo();
    String url = "https://api.machineq.net/v1/devices/"+model.getDevEUI()+"/";
    URL obj = new URL(url);
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    con.setRequestMethod("GET");
   
	int responseCode = con.getResponseCode();
	LOGGER.info("Sending 'GET' request to URL: " + url);
	LOGGER.info("Response Code: " + responseCode);
	

	BufferedReader in = new BufferedReader(
	new InputStreamReader(con.getInputStream()));
	StringBuffer response = new StringBuffer();


	in.close();
	LOGGER.info(response.toString());
    
	return null;
}

//public ResponseEntity<deviceInfo> SendMachineQData(String body){
//public ResponseEntity<deviceInfo> SendMachineQData(deviceInfo model) {
public ResponseEntity<deviceInfo> SendMachineQData(String payload, String targetPort, Boolean flushQueue, Boolean confirm) {

	deviceInfo model = new deviceInfo();
	
	sendMQmsg("https://api.machineq.net/v1/devices/"+deviceEUI+"/message", payload,targetPort,flushQueue,confirm);
//		sendMQmsg("https://api.machineq.net/v1/devices/"+deviceEUI+"/message", model);
	
	return new ResponseEntity<deviceInfo>(HttpStatus.OK); 
}


//convert string to hex
public String convertStringToHex(String p){
	  
	  char[] chars = p.toCharArray();
	  
	  StringBuffer hex = new StringBuffer();
	  for(int i = 0; i < chars.length; i++){
	    hex.append(Integer.toHexString((int)chars[i]));
	  }
	  
	  return hex.toString();
}


//payload convert to regex
public static String createRegex(String s) {
    StringBuilder b = new StringBuilder();
    for(int i=0; i<s.length(); ++i) {
        char ch = s.charAt(i);
        if ("\\.^$|?*+[]{}()".indexOf(ch) != -1)
            b.append('\\').append(ch);
        else if (Character.isLetter(ch))
            b.append("[A-Za-z]");
        else if (Character.isDigit(ch))
            b.append("\\d");
        else
            b.append(ch);
    }
    return b.toString();
}


//public Object sendMQmsg(String url, String model) throws IOException{
//public Object sendMQmsg(String url, deviceInfo model) throws IOException{

	public Object sendMQmsg(String url, String payload, String targetPort, Boolean flushQueue,Boolean confirm) {

		HttpClient httpClient=getHttpClient();		
		HttpPost post  = new HttpPost(url); 
 
//		TokenInfo tokenObj = jwttoken.get(this.clientId);
		TokenInfo tokenObj = jwttoken.get(clientId);		
		String t  = tokenObj.getToken();
		
	   //token expiration 
		

		post.addHeader("Authorization", t);
		post.addHeader("Content-Type","application/json");

		String regexStr = convertStringToHex(payload);
  
		deviceInfo model = new deviceInfo();
		model.setDevEUI(deviceEUI);
		model.setPayload(regexStr);
		model.setTargetPort(targetPort);
		model.setFlushQueue(flushQueue);
		model.setConfirm(confirm);
   
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode deviceArray = mapper.createArrayNode();	
		ObjectNode objectSt = mapper.createObjectNode();	

		objectSt.put("DevEUI", model.getDevEUI());
		objectSt.put("Payload", model.getPayload());
		objectSt.put("TargetPort", model.getTargetPort());
		objectSt.put("Confirm", model.isConfirm());
		objectSt.put("FlushQueue", model.isFlushQueue());
		deviceArray.add(objectSt);

		String a = objectSt.toString();
 
   
		StringEntity entity;
		try {
		
			entity = new StringEntity(a);
			post.setEntity(entity);
			HttpResponse response  = httpClient.execute(post);
		
		
			LOGGER.info("Sending 'POST' request to URL:" + url);
			LOGGER.info("Headers :" + t);
			LOGGER.info("Response Code:" + response.getStatusLine().getStatusCode());
			LOGGER.info("Response Msg:" +  response.getStatusLine().getReasonPhrase());
			LOGGER.info("Request Entity:" + a);
		
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent())); 
		
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			((DefaultHttpClient)httpClient).close();
			LOGGER.info("Response : " + result.toString());	
			return result;
		
		} catch (IOException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	
		return null;

	}



/* SSL to TLS  */
private HttpClient getHttpClient() {
    try {
       SSLContext sslContext = (SSLContext) SSLContexts.custom()
              .useTLS()
              .build();
 SSLConnectionSocketFactory f = new SSLConnectionSocketFactory(
              (javax.net.ssl.SSLContext) sslContext,
              new String[]{"TLSv1", "TLSv1.1","TLSv1.2"},   
              null,
              SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

          return (DefaultHttpClient) HttpClients.custom()
              .setSSLSocketFactory(f)
              .build();
    } catch (Exception e) {
        return new DefaultHttpClient();
    }
}

}
