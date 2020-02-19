package kr.co.corners.dao;

import org.apache.ibatis.annotations.Param;

public interface StationStatusMapper {
	
	//void setConnectionStatus(@Param("deviceId") String deviceId, @Param("statCd") String statCd);

	void setConnectionStatus(@Param("stationId") int stationId, @Param("siteId") int siteId, @Param("statCd") String statCd);

}
