package kr.co.corners.controller;

//import org.hibernate.validator.internal.util.logging.Log_.logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.corners.model.TokenInfo;
import kr.co.corners.model.deviceInfo;
import kr.co.corners.redis.dao.JWTTokenSave;
import kr.co.corners.redis.dao.deviceInfoDao;
import kr.co.corners.service.devService;
import kr.co.corners.service.tokenService;



@Controller
public class mqcontroller {
    
	//login URL
	@Value("${machineq.server.url.login}")
    private String login_url;
	
	//msg URL
	@Value("${machineq.server.url.common}")
    private String url;
	
	
//	@Value("${machineq.testdevice}")
//	private String devEUI;
	
	@Autowired 
	tokenService tokenrequest; 

	@Autowired
	JWTTokenSave tokenSave;
	
	@Autowired
	devService devservice;
	
	@Autowired
	deviceInfoDao deviceInfoD;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(mqcontroller.class);
	
	//Auth 2.0 
	@ResponseBody
   	@RequestMapping(value="/OAuth", method=RequestMethod.POST)
	public ResponseEntity<String> oauth(@RequestParam("grant_type") String grantType,
	                                    @RequestParam("client_id") String clientId,
	    		                        @RequestParam("client_secret") String clientSecret)
	       		                        throws Exception{
	   		
		return tokenrequest.getAccessTokenViaSpringSecurityOAuthClient(grantType, clientId, clientSecret);
		
		//return tokenrequest.oauthReq(grantType, clientId, clientSecret);
	}
	
	
	/*validate
	 * 
	 * 로그인후 생성된 토큰을 사용하여야 함.
	 * 
	 * */
//	@ResponseBody
//	@RequestMapping(value="/v1/validate", method=RequestMethod.POST)
//	public ResponseEntity<String> validate() throws Exception{
//	
//		TokenInfo tokenObj = tokenSave.get();
//		
//		if(tokenObj == null)
//			LOGGER.info("Token NULL");
//		else
//			LOGGER.info("Token \"" +  tokenObj.getToken() +  "\"");
//		return tokenrequest.validateToken(tokenObj);
//	}
	
	/*login
	 * 
	 * 로그인후 생성된 토큰을 사용하여야 함.
	 * */
//	@ResponseBody
//	@RequestMapping(value="/v1/login", method=RequestMethod.POST)
//	public ResponseEntity<String> login(@RequestBody String body) throws Exception{
//		
//		return tokenrequest.tokenRequest(login_url,body);
//	}

	
	/*상태조회
	 * 
	 * 디바이스 정보 중 상태정보 “Payload” 참조
	 */
	@ResponseBody
	@RequestMapping(value="/MQ", method=RequestMethod.POST)
	public ResponseEntity<String> ReciveMachineQData(@RequestBody String body) throws Exception{
		
		return devservice.ReciveMachineQData(body);	
   }


	/*
	 * 메시지 전송
	 *
	
    @ResponseBody
	@RequestMapping(value="/{deviceEUI}", method=RequestMethod.POST)

    public ResponseEntity<deviceInfo> sendMessagetoDevice(@PathVariable String deviceEUI, @RequestBody String model)
    		                                              throws Exception{
    
		
    return devservice.SendMachineQData(model);
    }
   
    */

   
    @ResponseBody
   	@RequestMapping(value="/{deviceEUI}", method=RequestMethod.POST)

       public ResponseEntity<deviceInfo> sendMessagetoDevice(@PathVariable String deviceEUI, 
    		                                                 @RequestParam("Payload") String Payload,
    		                                                 @RequestParam("TargetPort") String TargetPort,
    		                                                 @RequestParam("Confirm") Boolean Confirm,
    		                                                 @RequestParam("FlushQueue") Boolean FlushQueue)
       		                                              throws Exception{
    	   deviceInfo model = new deviceInfo();
    	   model.setDevEUI(deviceEUI);
   		
       return devservice.SendMachineQData(Payload, TargetPort, Confirm, FlushQueue);
       }
    
    /*
    @ResponseBody
	@RequestMapping(value="/{deviceEUI}", method=RequestMethod.POST)

    public ResponseEntity<deviceInfo> sendMessagetoDevice(@PathVariable String deviceEUI, @RequestBody deviceInfo model) throws Exception{
    		                                            
    model.setDevEUI(deviceEUI);
    return devservice.SendMachineQData(model);
    }
	*/
 


/*       
    @ResponseBody
	@RequestMapping(value="v1/devices/{deviceEUI}", method=RequestMethod.GET)
	public ResponseEntity<String> getData(@PathVariable String deviceEUI) throws Exception{ 
    	
        return devservice.getDeviceData();
	}    
 */
    
    

    
}
