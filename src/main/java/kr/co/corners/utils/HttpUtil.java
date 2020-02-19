package kr.co.corners.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import kr.co.corners.model.TokenInfo;

@SuppressWarnings("deprecation")
/**
 * 사용상 주의사항 *****
 * 멀티 쓰레드에서 비동기 호출 되는 클래스이므로 Bean이나 Component로 지정하지 말것. 
 * 함수도 Bean이나 Resource, static으로 사용하면 안됨. 
 * 반드시 멤버 객체로 할당하여 사용할것.- (안하면 비동기쓰레드 지옥)
 */


public class HttpUtil {

	private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);	
	
	public HttpClient getHttpsClient() {
        try {
        	SSLContext sslContext = SSLContexts.custom()
        		    .useTLS()
        		    .build();

        		SSLConnectionSocketFactory f = new SSLConnectionSocketFactory(
        		    sslContext,
        		    new String[]{"TLSv1", "TLSv1.1","TLSv1.2"},   
        		    null,
        		    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

        		return HttpClients.custom()
        		    .setSSLSocketFactory(f)
        		    .build();
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }
	
	public void sendAsyncPost(final String url, final Map<String,String> headers, final String content) throws Exception {		
		new Thread("AsyncHttpPost") {
			@Override
			public void run() {
				try {
					sendPost(url, headers, content);
				} catch (Exception e) {					
					e.printStackTrace();
				}
			}
		}.start();
	}
		
	public void sendPost(String url,  Map<String, String> headers, String content) throws Exception {
		
		logger.info("HTTP POST : " + url);
		
		DefaultHttpClient webClient = null;
		if( url.contains("https"))
			webClient = (DefaultHttpClient)getHttpsClient();
		else
			webClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
	
		
		// Set Header
		if( headers != null)
		{
			List<String> keyList = new ArrayList<String>(headers.keySet());	
			if( keyList != null)
			{
				for (String key : keyList)
			    {
			    	String value = headers.get(key);
			    	post.setHeader(key, value);
			    	logger.info("Set Header-" + key + ":" + value);
			    }
			}		    
		}
		
		
		
		
				
	    // Set Content
		if( content != null)
		{
			StringEntity entity = new StringEntity(content, "UTF-8");
			post.setEntity(entity);
		}	
		
		// Get Response
		HttpResponse response = webClient.execute(post);
		
		logger.info("Sending 'POST' request to URL : " + url);
		logger.info("Post parameters : " + post.getEntity());
		logger.info("Response Code : " + response.getStatusLine().getStatusCode());

		BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		logger.info("Device Action Response : " + result.toString());
		
		webClient.close();		
	}
	
	
	

}
