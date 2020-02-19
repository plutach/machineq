package kr.co.corners.redis;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import kr.co.corners.model.StationConfig;
import kr.co.corners.model.StationStatus;
import kr.co.corners.model.TokenInfo;
import kr.co.corners.model.deviceInfo;


@Configuration
public class RedisConfig {
	
	@Value("${spring.redis.host}")	
	private String redisHost;
	
	@Value("${spring.redis.port}")
	private int redisPort;
	
	@Value("${spring.redis.database}")
	private int dbindex;

	@Bean
	public RedisTemplate<String, Object> deviceEntityKeyTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new GenericToStringSerializer<Integer>(Integer.class));
		redisTemplate.setConnectionFactory(connectionFactory(dbindex));
		return redisTemplate;
	}
	
	@Bean
	public JedisConnectionFactory connectionFactory(int dbindex2) {				
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
		jedisConnectionFactory.setHostName(redisHost);
		jedisConnectionFactory.setPort(redisPort);
		jedisConnectionFactory.setUsePool(true);		
		jedisConnectionFactory.setDatabase(dbindex2);
		return jedisConnectionFactory;
	}
	
	@Bean
	public RedisTemplate<String, String> stationKeyTemplate() {
		RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());
		redisTemplate.setConnectionFactory(connectionFactory(dbindex));
		return redisTemplate;
	}
	
	@Bean
	public RedisTemplate<String,TokenInfo> jwtTokenTemplate() {
	 RedisTemplate<String, TokenInfo> redisTemplate = new RedisTemplate<String, TokenInfo>();
	 redisTemplate.setKeySerializer(new StringRedisSerializer());
	 redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<TokenInfo>(TokenInfo.class));
     redisTemplate.setConnectionFactory(connectionFactory(dbindex)); 
     return redisTemplate;
	}


    @Bean
    public RedisTemplate<String, StationConfig> stationConfigTemplate(){
    	RedisTemplate<String,StationConfig> redisTemplate = new RedisTemplate<String, StationConfig>();
    	redisTemplate.setKeySerializer(new StringRedisSerializer());
    	redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<StationConfig>(StationConfig.class));
    	redisTemplate.setConnectionFactory(connectionFactory(dbindex));
    	return redisTemplate;
    }
    
    @Bean
    public RedisTemplate<String, deviceInfo> deviceInfoTemplate(){
    	RedisTemplate<String,deviceInfo> redisTemplate = new RedisTemplate<String, deviceInfo>();
    	redisTemplate.setKeySerializer(new StringRedisSerializer());
    	redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<deviceInfo>(deviceInfo.class));
    	redisTemplate.setConnectionFactory(connectionFactory(dbindex));
    	return redisTemplate;
    }
    

    @Bean
    public RedisTemplate<String, StationStatus> stationStatusTemplate(){
    	RedisTemplate<String,StationStatus> redisTemplate = new RedisTemplate<String, StationStatus>();
    	redisTemplate.setKeySerializer(new StringRedisSerializer());
    	redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<StationStatus>(StationStatus.class));
    	redisTemplate.setConnectionFactory(connectionFactory(dbindex));
    	return redisTemplate;
    }
    
}