package kr.co.corners.mqtt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import kr.co.corners.model.StationConfig;
import kr.co.corners.model.StationStatus;

@Component
public class MqttSender {

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
   
    @Value("${mqtt.broker.tcp}")
    private String broker_tcp;
    
//    @Value("${mqtt.broker.ssl}")
//    private String broker_ssl;
    
    
    @Autowired
    private MqttService service;
    
	//private static final Logger logger = LoggerFactory.getLogger(MqttSender.class);
    
    /**
     * 스테이션 동작 지시 취소 
     * @param coordiSn 코디네이터 시리얼번호
     */
    public synchronized Boolean SendCancelAction(int coordiSn, int stationSn)
    {
		try {

			ObjectMapper mapper = new ObjectMapper();
			 
		    ArrayNode stationArray = mapper.createArrayNode();	    
		    
		    ObjectNode objectSt = mapper.createObjectNode();
		    objectSt.put("sid",stationSn);
		    objectSt.put("diag",0);
		    objectSt.put("snd",0);
		    objectSt.put("rpt",0);
		    objectSt.put("blk",0);
		    objectSt.put("lo",0);
		    objectSt.put("lx",0);
		    objectSt.put("ba",0);
		    objectSt.put("br",0);
		    objectSt.put("bl",0);
		    objectSt.put("bu",0);
		    objectSt.put("bd",0);
		    
		    stationArray.add(objectSt);		    
			
		    ObjectNode objectPrm = mapper.createObjectNode();
		    objectPrm.put("dly", 0);
		    objectPrm.putPOJO("st", stationArray);
			
		    String topic = "sidp/dev/crd/"+coordiSn;
			ObjectNode objectReq = mapper.createObjectNode();		
			objectReq.put("fr",topic_station );
			objectReq.put("to",topic);
			objectReq.put("cmd","T");			
		    
		    objectReq.putPOJO("prm", objectPrm);
		    
	        ObjectNode objectNode1 = mapper.createObjectNode();
	        objectNode1.putPOJO("req", objectReq);
	        
			String jsonString = objectNode1.toString();
			
			service.SendAsyncMqtt(topic, jsonString);
			return true;
			
			
			
		}
		catch(Exception me) {
			return false;
		}
    }
    
    /**
     * 스테이션 설정 정보를 self로 Mqtt로 전송
     * @param coordiSn 코디네이터 시리얼번호
     * @param stationSn 스테이션 시리얼번호
     * @param model 스테이션 설정 정보
     */
    public synchronized boolean SendConfigStation(int coordiSn, Integer stationSn, StationConfig model )
    {
    	try {			
    		
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode stationArray = mapper.createArrayNode();
			
			ObjectNode objectSt = mapper.createObjectNode();
			
			objectSt.put("dev", model.getdeviceEUI());
			objectSt.put("sid", model.getSid());
			objectSt.put("tmp", model.getTempConf());
			objectSt.put("vol", model.getVolumeConf());
			objectSt.put("int", model.getIntervalConf());
			objectSt.put("scf", model.getScaleConf());
			objectSt.put("opt", model.getOption());
			objectSt.put("act", model.getDefaultAction());
		
			stationArray.add(objectSt);
					
			String topic = "sidp/dev/crd/"+coordiSn;
			ObjectNode objectReq = mapper.createObjectNode();		
			objectReq.put("fr", topic );
			objectReq.put("to", topic );
			objectReq.put("cmd","A");
			objectReq.putPOJO("prm", stationArray);
			
			ObjectNode objectNode1 = mapper.createObjectNode();
			objectNode1.putPOJO("req", objectReq);
	        
			String jsonString = objectNode1.toString();			
			service.SendAsyncMqtt(topic, jsonString);
			return true;
		}
		catch(Exception me) {
			 me.printStackTrace();
		}
		return false;
    }
    

    /**
     * 스테이션 동작정보를 Mqtt로 전송 ( 테스트용 cron에서 호출됨 )
     * @param coordiSn 코디네이터(gateway) 시리얼번호
     * @param stationSn 스테이션 시리얼번호
     */
    public synchronized Boolean SendActionStation(int coordiSn, Integer stationSn, StationStatus model)
    {
		try {
			
			ObjectMapper mapper = new ObjectMapper();
			
			ArrayNode stationArray = mapper.createArrayNode();			
			ObjectNode objectSt = mapper.createObjectNode();			
			objectSt.put("sid", model.getSid());
			objectSt.put("snd", model.getSnd());
			objectSt.put("rpt", model.getRpt());
			objectSt.put("blk", model.getBlk());													
			objectSt.put("ba", model.getBa());
			objectSt.put("lo", model.getLo());
			objectSt.put("lx", model.getLx());
			objectSt.put("br", model.getBr());
			objectSt.put("bl", model.getBl());
			objectSt.put("bu", model.getBu());
			objectSt.put("bd", model.getBd());
			stationArray.add(objectSt);
			
			String topic = "sidp/dev/crd/"+coordiSn;
			ObjectNode objectReq = mapper.createObjectNode();		
			objectReq.put("fr",topic_station );
			objectReq.put("to","sidp/dev/crd/"+coordiSn);
			objectReq.put("cmd","T");
			objectReq.putPOJO("prm", stationArray);			
			ObjectNode objectNode1 = mapper.createObjectNode();
			objectNode1.putPOJO("req", objectReq);		
	        
			String jsonString = objectNode1.toString();			
			service.SendAsyncMqtt(topic, jsonString);
			return true;
		}
		catch(Exception me) {
			 me.printStackTrace();
		}
		return false;
    }
    
    /**
     * 스테이션 정보를 Mqtt로 전송 
     * @param coordiSn 코디네이터 시리얼번호
     * @param stationSn 스테이션 시리얼번호
     */
    public synchronized Boolean SendStationEvent(int coordiSn, Integer stationSn, StationStatus statusModel)
    {
		try {
			
			ObjectMapper mapper = new ObjectMapper();
			//ArrayNode arrayNode = mapper.createArrayNode();
			ArrayNode stationArray = mapper.createArrayNode();
			
			ObjectNode objectSt = mapper.createObjectNode();
			
			objectSt.put("sid", statusModel.getSid());
			objectSt.put("ts", statusModel.getTs());
			objectSt.put("tv", statusModel.getTv());
			objectSt.put("gs", statusModel.getGs());
			objectSt.put("gv", statusModel.getGv());					
			objectSt.put("ss", statusModel.getSs());
			objectSt.put("blv", statusModel.getBlv());
			objectSt.put("bus", statusModel.getBus());
			objectSt.put("sv", statusModel.getSv());
			objectSt.put("vr", statusModel.getVr());
			objectSt.put("snd", statusModel.getSnd());
			objectSt.put("rpt", statusModel.getRpt());
			objectSt.put("blk", statusModel.getBlk());													
			objectSt.put("ba", statusModel.getBa());
			objectSt.put("lo", statusModel.getLo());
			objectSt.put("lx", statusModel.getLx());
			objectSt.put("br", statusModel.getBr());
			objectSt.put("bl", statusModel.getBl());
			objectSt.put("bu", statusModel.getBu());
			objectSt.put("bd", statusModel.getBd());
			
		
			stationArray.add(objectSt);
					
			ObjectNode objectReq = mapper.createObjectNode();
			objectReq.put("fr","sidp/dev/crd/"+coordiSn);
			objectReq.put("to",topic_station );
			objectReq.put("cmd","s");
			objectReq.putPOJO("prm", stationArray);
			
			ObjectNode objectNode1 = mapper.createObjectNode();
			objectNode1.putPOJO("res", objectReq);
			
			String jsonString = objectNode1.toString();			
			service.SendAsyncMqtt(topic_station, jsonString);
			return true;
		}
		catch(Exception me) {
			 me.printStackTrace();
		}
		return false;
    }
  
    public synchronized Boolean SendConnect(int coordiSn)
    {
    	service.SendAsyncMqtt("sidp/dev/crd/"+coordiSn, "{\"con\":1}");
    	return true;
    }
    
    public synchronized Boolean SendDisconnect(int coordiSn)
    {
    	service.SendAsyncMqtt("sidp/dev/crd/"+coordiSn, "{\"con\":0}");
    	return true;
    }
}
