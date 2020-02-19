package kr.co.corners.utils;

import java.security.MessageDigest;

public class Encryption {
	/**
	 * 문자열을 MD-5 방식으로 암호화
	 * @param txt 암호화 하려하는 문자열
	 * @return String
	 * @throws Exception
	 */
	public static String getEncMD5(String txt) throws Exception {
	     
	    StringBuffer sbuf = new StringBuffer();
	     
	     
	    MessageDigest mDigest = MessageDigest.getInstance("MD5");
	    mDigest.update(txt.getBytes());
	     
	    byte[] msgStr = mDigest.digest() ;
	     
	    for(int i=0; i < msgStr.length; i++){
	    	int intVal = msgStr[i] & 0xff;
            if (intVal < 0x10) {
            	sbuf.append("0");
            }
            sbuf.append(Integer.toHexString(intVal));
	    }
	     
	    return sbuf.toString() ;
	}
}
