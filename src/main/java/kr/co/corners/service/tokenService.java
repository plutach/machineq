package kr.co.corners.service;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.corners.model.TokenInfo;
import kr.co.corners.redis.dao.JWTTokenSave;


@SuppressWarnings("deprecation")
@Component
public class tokenService {
	
    @Autowired
	JWTTokenSave tokenSave;
    
    private final static Logger LOGGER = LoggerFactory.getLogger(tokenService.class);
    
	//api url
	@Value("${machineQ.api.url}")
	private String apiUrl;
	
	@Value("${machineQ.token.url}")
	private String tokenUrl;

	public ResponseEntity<String> getAccessTokenViaSpringSecurityOAuthClient(String grantType, String clientId, String clientSecret) {
	    try{
	    	
	        ClientCredentialsResourceDetails resourceDetails = new ClientCredentialsResourceDetails();
	        resourceDetails.setClientId(clientId);
	        resourceDetails.setClientSecret(clientSecret);
	        resourceDetails.setAccessTokenUri(tokenUrl);
	        //resourceDetails.setScope(TestOAuthConstants.SCOPES);
	        resourceDetails.setGrantType(grantType);

	        OAuth2RestTemplate oAuthRestTemplate = new OAuth2RestTemplate(resourceDetails);

	        List<MediaType> acceptableList = new ArrayList<MediaType>();
	        acceptableList.add(MediaType.APPLICATION_JSON);
	        
	        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
	        headers.setAccept(acceptableList);
	        headers.setContentType(MediaType.APPLICATION_JSON);

	        OAuth2AccessToken token = oAuthRestTemplate.getAccessToken();
	        System.out.println(token);
	        
	        System.out.println(token.getExpiration());
	
	        
	        //token 에 값이 저장되어 있음
	        //Token을 REDIS DB 에 저장
	        TokenInfo tk = new TokenInfo();
	        tk.setToken(token.getValue());
            tokenSave.save(clientId, tk);
	        
            
            
	        return new ResponseEntity<String>(HttpStatus.OK);
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	       
	        return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
}


