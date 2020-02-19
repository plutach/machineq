package kr.co.corners.dao;

import org.apache.ibatis.annotations.Param;

import kr.co.corners.model.StationInfo;

public interface StationMapper {

	StationInfo getStationInfo(@Param("stationSN") int nStationSN);
    
}
