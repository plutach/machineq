package kr.co.corners.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface StationConfigMapper {
	
	// 설정된 스테이션의 센서 레벨
	public Integer getSensorLevel(@Param("stationId") int stationId, @Param("siteId") int siteId, @Param("sensorCode") String sensorCode);

	// 스테이션의 기본 동작 JsonString
	public String getDefaultAction(@Param("stationId") int stationId, int siteId);
	
	// 스테이션 보고 설정  주기 시간, 시/분/초 , 이벤트별 보고 설정 
	public List<Integer> getReportInfo(@Param("v") int siteId);
 
}

//org.apache.ibatis.binding.BindingException: Parameter 'stationId' not found. Available parameters are [arg1, arg0, param1, param2]