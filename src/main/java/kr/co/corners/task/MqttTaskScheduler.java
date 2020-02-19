package kr.co.corners.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import kr.co.corners.mqtt.MqttSender;
import kr.co.corners.common.Constants;
import kr.co.corners.model.StationStatus;

import javax.annotation.Resource;


@Component
public class MqttTaskScheduler 
{
   // private static final Logger logger = LoggerFactory.getLogger(MqttTaskScheduler.class);
    
    //private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Autowired
    MqttSender mqtt;
    
    @Resource(name="deviceEntityKeyTemplate") 
    private ListOperations<String, Object> listOperations; 
    
	@Value("${machineq.testdevice}")
	private String testDeviceEUI;

	
	private int nCount = 0;	
	
	@Scheduled(cron="0 0/1 * * * ?")
    public void scheduleTaskStationAction() {        
        try
        {
        	nCount++;			
			
        	if( nCount >= 5)
			{
				nCount = 1;
			}
			        		
    		String deviceID = testDeviceEUI;
    		//if( nCount  % 2 == 0)
    		//{
    		//	deviceID = testDeviceID2;
    		//}
    		
    		int nCoordiID =(Integer)listOperations.index(deviceID, Constants.COORDI_ID);        	
        	int StationSn =(Integer)listOperations.index(deviceID, Constants.STATION_SN);    		
    		
    		StationStatus model = new StationStatus();
			model.setSid(StationSn);
			model.setTs(0);
			model.setSnd(48);
			model.setRpt(1);
			model.setBlk(0);													
			model.setBa(0);
			model.setLo(0);
			model.setLx(0);
			if( nCount == 1)
				model.setBr(1);
			else
				model.setBr(0);
			
			if( nCount == 2)
				model.setBl(1);
			else
				model.setBl(0);
			
			if( nCount == 3)
				model.setBu(1);
			else
				model.setBu(0);
			
			if( nCount == 4)
				model.setBd(1);
			else
				model.setBd(0);
			
			//logger.info("Cron Task Send Device Request :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()) );
			mqtt.SendActionStation(nCoordiID, StationSn, model);
        	        				
			
        }
        catch(Exception ex)
        {
        	ex.printStackTrace();
        	//logger.info("Redis Data Not Exist!");
        }
    }
    
	private int nCount2 = 0;	
	
	@Scheduled(cron="0 0/1 * * * ?")
	public void scheduleTaskStationSetting()
	{
		try
		{
			nCount2++;			
			if( nCount2 >= 5)
			{
				nCount2 = 1;
			}
	    		    		
    		String deviceID = testDeviceEUI;    		
    		
    		int nCoordiID =(Integer)listOperations.index(deviceID, Constants.COORDI_ID);        	
        	int StationSn =(Integer)listOperations.index(deviceID, Constants.STATION_SN);    		
    		
    		StationStatus model = new StationStatus();
			model.setSid(StationSn);
			model.setTs(0);
			model.setSnd(48);
			model.setRpt(1);
			model.setBlk(0);													
			model.setBa(0);
			model.setLo(0);
			model.setLx(0);
			if( nCount2 == 1)
				model.setBr(1);
			else
				model.setBr(0);			
			if( nCount2 == 2)
				model.setBl(1);
			else
				model.setBl(0);			
			if( nCount2 == 3)
				model.setBu(1);
			else
				model.setBu(0);
			
			if( nCount2 == 4)
				model.setBd(1);
			else
				model.setBd(0);
			
			//logger.info("Cron Task Send Device Request :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()) );
			mqtt.SendActionStation(nCoordiID, StationSn, model);			
		}
		catch(Exception ex)
		{
			
		}
		
	}
	
//	private HttpUtil httpUtil = new HttpUtil();	
//	public void getMachineQLoginToken()
//	{
//		try {
//			httpUtil.sendPost("https://api.machineq.net/v1/login", null, "{\"username\": \"seung_yoon\",\"password\": \"NbsHZO3O\"}");
//		} catch (Exception e) {			
//			e.printStackTrace();
//		}
//	}
}