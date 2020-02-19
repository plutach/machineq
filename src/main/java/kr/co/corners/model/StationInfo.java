package kr.co.corners.model;

public class StationInfo {

	private int stationId;
    private int siteId;    
    private int coordiId;
    
    
	public int getStationId() {
		return stationId;
	}
	public void setStationId(int stationId) {
		this.stationId = stationId;
	}
	
	public int getSiteId() {
		return siteId;
	}
	
	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}
	
	public int getCoordiId() {
		return coordiId;
	}
	
	public void setCoordiId(int coordiId) {
		this.coordiId = coordiId;
	}
}
