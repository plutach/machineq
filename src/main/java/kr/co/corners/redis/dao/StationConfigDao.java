package kr.co.corners.redis.dao;


import javax.annotation.Resource;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import kr.co.corners.model.StationConfig;
import kr.co.corners.model.StationStatus;
import kr.co.corners.model.TokenInfo;
import kr.co.corners.model.deviceInfo;

@Component

public class StationConfigDao {
	
	private final static String KEY = "Station:Config:";
	
	@Resource(name="stationStatusTemplate") 
	private RedisTemplate<String, StationConfig> redisTemplate;
	
	public synchronized void add(StationConfig confObj) {
		try
		{
			redisTemplate.opsForValue().set(KEY+ confObj.getdeviceEUI(), confObj);
		}
		catch(Exception ex)
		{			
		}		
	}
	
	public synchronized StationConfig get(String deviceEUI) {
		try
		{
			return redisTemplate.opsForValue().get(KEY+ deviceEUI);
		}
		catch(Exception ex)
		{			
		}
		return null;
	}
	
	
	public synchronized void remove(StationConfig confObj) {
		try
		{
			redisTemplate.opsForValue().getOperations().delete(KEY+ confObj.getdeviceEUI());
		}
		catch(Exception ex)
		{
		}
	}


 }



