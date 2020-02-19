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
	
   //private final static String apiUrl = "https://api.preview.machineq.net/v1/";
   //private final static String tokenUrl = "https://oauth.preview.machineq.net/oauth2/token";
	
    //application using : TEST1
   //private final static String client_Id = "36971b3d-dc76-46c4-a171-6e88c3f6bc64";
   //private final static String client_Sec = "K5IiyMn9jSmokFs1R7VA7iSWtUCp9cVK";

	/**
	 * OAuth 를 이용하여 서버로부터 토큰을 가져온다.
	 * @param grantType
	 * @param clientId
	 * @param clientSecret
	 * @return
	 */
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
	

//	public ResponseEntity<String> oauthReq(String grantType, String clientId, String clientSecret) {
//		// TODO Auto-generated method stub
//		try {
//			oauthTokenReq(tokenUrl, grantType, clientSecret, clientSecret);
//	      } catch (Exception e) {
//	         e.printStackTrace();
//	      }
//		return new ResponseEntity<String>(HttpStatus.OK);
//	}
	
//   public String oauthTokenReq(String grantType, String clientId, String clientSecret, String tokenUrl) throws ClientProtocolException, IOException {
//
//	   HttpClient httpClient=getHttpClient();		
//	   HttpPost post  = new HttpPost(apiUrl); 
//	  
//	    post.addHeader("Access","application/json");
//	    post.addHeader("Content-Type","application/json");
//	    
//	    HttpResponse response  = httpClient.execute(post);
//	    
//	    LOGGER.info("Sending 'POST' request to URL:" + tokenUrl);
//		LOGGER.info("Response Code:" + response.getStatusLine().getStatusCode());
//		LOGGER.info("Response Msg:" +  response.getStatusLine().getReasonPhrase());
//		 		
//		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent())); 
//		 		 
//		 		
//		 String result = new String();
//		 String line = "";
//		 while ((line = rd.readLine()) != null) {		
//		 		}
//
//		 		((DefaultHttpClient)httpClient).close();
//		 		LOGGER.info("Response : " + result.toString());	
//		 		
//		 		
//		 		
//        try {  
//  		//get access_token
//  		     ObjectMapper mapper = new ObjectMapper();
//  	         JsonNode node = mapper.readTree(result.toString()); 
//  	         String access_token =  node.get("access_token").asText();	         
//  	   
//  	         LOGGER.info("Token: " + access_token);
//  	         
//  		      String token = "Bearer" + access_token; 
//  		 
//  		      
//  		      post.addHeader("Authentication", "Bearer"+access_token);
//  		 	 return token;
//  	         
//           
//  	} catch (Exception e) {
//  		// TODO Auto-generated catch block
//  		e.printStackTrace();
//  	}
//        
//    	return result;
//	 	
//
//
//	 }



/*jwt token 가져오기  */ 
//public ResponseEntity<String> tokenRequest(String url,String content) {
//	
//	
//
//		try {
//			 getToken("https://api.machineq.net/v1/login", content);
//	      } catch (Exception e) {
//	         e.printStackTrace();
//	      }
//		return new ResponseEntity<String>(HttpStatus.OK);
//	}


//public Object getToken(String url,String content) throws IOException {
//
//		HttpClient httpClient=getHttpClient();		
//		HttpPost post  = new HttpPost(url);
//    
//		StringEntity entity  = new StringEntity(content, "UTF-8"); 
//		post.setEntity(entity);
//	     
//		HttpResponse response  = httpClient.execute(post);
//		
//		LOGGER.info("Sending 'POST' request to URL: " + "https://api.machineq.net/v1/login");
//		LOGGER.info("POST user credentials:" + post.getEntity());
//		LOGGER.info("Response Code:" + response.getStatusLine().getStatusCode());
//		
//		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//
//		StringBuffer result = new StringBuffer();
//		String line = "";
//		while ((line = rd.readLine()) != null) {
//			result.append(line);
//		}
//
//		((DefaultHttpClient)httpClient).close();
//		LOGGER.info("Response : " + result.toString());
//
//		
//	   try {  
//		  //save token 
//		     ObjectMapper mapper = new ObjectMapper();
//	         JsonNode node = mapper.readTree(result.toString()); 
//	         String tkn =  node.get("token").asText();	         
//	         TokenInfo tk = new TokenInfo();
//	         tk.setToken(tkn);
//             tokenSave.save(tk);
//             
//	         LOGGER.info("Token Saved: " + tkn);
//         
//	} catch (Exception e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//	return result;
//	  	
//}

/*jwt Token validation*/

//public ResponseEntity<String> validateToken(TokenInfo tokenObj){
//   tokenObj = tokenSave.get();	
//	String cn  = "{" 
//    + "\"token\"" 
//    + ":\""+tokenObj.getToken()+"\"" 
//    + "}";
//
//	try {
//       validatetoken("https://api.machineq.net/v1/validate",cn);
//     } catch (Exception e) {    
//        e.printStackTrace();
//     }
//return new ResponseEntity<String>(HttpStatus.OK);
//}


//public void validatetoken(String url, String content) throws IOException { 
//
//	HttpClient httpClient=getHttpClient();		
//	HttpPost post  = new HttpPost(url); 
//		
//	StringEntity entity  = new StringEntity(content, "UTF-8"); 
//	post.setEntity(entity);   
//	HttpResponse response  = httpClient.execute(post);
//	
//	LOGGER.info("Sending 'POST' request to URL: " + "https://api.machineq.net/v1/validate");
//	LOGGER.info("POST token:" + post.getEntity());	
//	LOGGER.info("Response Code:" + response.getStatusLine().getStatusCode());
//	
//	BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//	StringBuffer result = new StringBuffer();
//	String line = "";
//	while ((line = rd.readLine()) != null) {
//		result.append(line);
//	}
//
//	((DefaultHttpClient)httpClient).close();
//	LOGGER.info("Response : " + result.toString());
//}


/* SSL to TLS  */
//	private HttpClient getHttpClient() {
//        try {
//           SSLContext sslContext = (SSLContext) SSLContexts.custom()
//                  .useTLS()
//                  .build();
//     SSLConnectionSocketFactory f = new SSLConnectionSocketFactory(
//                  (javax.net.ssl.SSLContext) sslContext,
//                  new String[]{"TLSv1", "TLSv1.1","TLSv1.2"},   
//                  null,
//                  SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
//
//              return (DefaultHttpClient) HttpClients.custom()
//                  .setSSLSocketFactory(f)
//                  .build();
//        } catch (Exception e) {
//            return new DefaultHttpClient();
//        }
//    }

}


