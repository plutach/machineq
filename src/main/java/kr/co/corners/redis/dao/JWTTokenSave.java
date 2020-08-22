package kr.co.corners.redis.dao;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import kr.co.corners.model.TokenInfo;


@Component
public class JWTTokenSave {
	
	
	private final Logger logger = LoggerFactory.getLogger(JWTTokenSave.class);
	
	@Resource(name="jwtTokenTemplate") 
	private RedisTemplate<String,TokenInfo>redisTemplate;

	
	public void save(String clientId, TokenInfo tokenObj) {
		 try {           
		  redisTemplate.opsForValue().set(clientId, tokenObj);
		 }catch(Exception e) {
			 logger.info(e.getMessage());
			 e.printStackTrace();
		 }
	}
	
	public TokenInfo get(String clientId)
	{
		try
		{	
		return redisTemplate.opsForValue().get(clientId);	        
		}
		catch(Exception e)
		{
		e.printStackTrace();
		}
		return null;	
	}
 
}

