package kr.co.corners.redis.dao;


import javax.annotation.Resource;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import kr.co.corners.model.StationStatus;

@Component

public class StationStatusDao {
	
	private final static String KEY = "Station:Status:";
	
	@Resource(name="stationStatusTemplate") 
	private RedisTemplate<String, StationStatus> redisTemplate;
	
	public synchronized void add(StationStatus statObj) {
		try
		{
			redisTemplate.opsForValue().set(KEY+ statObj.getdeviceEUI(), statObj);
		}
		catch(Exception ex)
		{			
		}		
	}
	
	public synchronized StationStatus get(String deviceEUI) {
		try
		{
			return redisTemplate.opsForValue().get(KEY+deviceEUI);
		}
		catch(Exception ex)
		{			
		}
		return null;
	}
	
	public synchronized void remove(StationStatus statObj) {
		try
		{
			redisTemplate.opsForValue().getOperations().delete(KEY+statObj.getdeviceEUI());
		}
		catch(Exception ex)
		{
		}
	}
}

