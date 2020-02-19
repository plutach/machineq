package kr.co.corners.redis.dao;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import kr.co.corners.model.deviceInfo;

@Component
public class deviceInfoDao {

	private final static String KEY = "deviceInfo :";
	
	@Resource(name="deviceInfoTemplate") 
	private RedisTemplate<String, deviceInfo> redisTemplate;
	
	public deviceInfo add(deviceInfo model) {
		try
		{
			redisTemplate.opsForValue().set(KEY + model.getDevEUI(), model);
		}
		catch(Exception ex)
		{			
		}
		return model;		
	}
	
	public deviceInfo get(deviceInfo model){
		try
		{
			return redisTemplate.opsForValue().get(model);
		}
		catch(Exception ex)
		{			
		}
		return null;
	}

		
	}
	