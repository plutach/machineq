package kr.co.corners.model;

public class StationConfig {
	
	private String deviceEUI;
	public String getdeviceEUI() {
		return deviceEUI;
	}
	public void setdeviceEUI(String deviceEUI) {
		this.deviceEUI = deviceEUI;
	}
	private String payload;

	public String getPayload() {
		return payload;
	}
	public void setPayload(String payload) {
		this.payload = payload;
	}
	// 시리얼 번호 
	private int nStationSn;
	public void setSid(int stationSn) {
		nStationSn = stationSn;
	}
	public int getSid() {
		return nStationSn;
	}
	
	//이벤트를 발생시킬 온도 도달 값
	//Range 0~100 (60)
	private int nCfgTemp;
	public void setTempConf(int cfgTemp) {
		nCfgTemp = cfgTemp;
	}
	public int getTempConf(){
		return nCfgTemp;
	}
	
	private int nCfgVolume;
	public void setVolumeConf(int cfgVolume) {
		nCfgVolume = cfgVolume;
	}
	public int getVolumeConf(){
		return nCfgVolume;
	}
	
	private char nCfgScale;
	public void setScaleConf(char cfgScale) {
		nCfgScale = cfgScale;
	}
	public char getScaleConf(){
		return nCfgScale;
	}
	
	private byte nCfgInterval;
	public void setIntervalConf(byte cfgInterval) {
		nCfgInterval = cfgInterval;
	}	
	public byte getIntervalConf(){
		return nCfgInterval;
	}
	
	private byte nOption;
	public void setOption(byte option) {
		nOption = option;
	}
	public byte getOption(){
		return nOption;
	}
	
	
	private int nAction;
	public void setDefaultAction(int action) {
		nAction = action;
	}
	public int getDefaultAction(){
		return nAction;
	}

	

}
