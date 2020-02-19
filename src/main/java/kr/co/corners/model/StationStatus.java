package kr.co.corners.model;

public class StationStatus {

	private int sid = -1;
	private int ts = -1;
	private int tv = 0;
	private int gs = -1;
	private int gv = 0;
	private int ss = 0;
	private int blv = 0;
	private int bus = 0;
	private int sv = 0;
	private int vr = 0;	
	private int snd = 0;
	private int rpt = 0;
	private int blk = 0;
	private int ba = 0;
	private int lo = 0;
	private int lx = 0;
	private int br = 0;
	private int bl = 0;
	private int bu = 0;
	private int bd = 0;
	private String deviceEUI;
	public String getdeviceEUI() {
		return deviceEUI;
	}
	public void setdeviceEUI(String deviceEUI) {
		this.deviceEUI = deviceEUI;
	}
	
	
	public void setSid(int stationId) {
		sid = stationId;		
	}
		
	public void setTs(int nTempSensorStatus) {
		ts = nTempSensorStatus;		
	}
	
	public void setTv(int nTemperture) {
		tv = nTemperture;
	}
		
	public void setGs(int nSmokeSensorStatus) {
		gs = nSmokeSensorStatus;		
	}
		
	public void setGv(int nSmokeLevel) {
		gv = nSmokeLevel;
	}

	public void setSs(int nRadioPw) {
		ss = nRadioPw;		
	}

	public void setBlv(int nBatteryStatus) {
		blv = nBatteryStatus;
	}

	public void setBus(int nPowerStatus) {
		bus = nPowerStatus;
	}

	public void setSv(int nVolume) {
		sv = nVolume;
	}

	public void setVr(int nFirmwareVersion) {
		vr = nFirmwareVersion;
	}

	public void setSnd(int nBroadcastTable) {
		snd = nBroadcastTable;
	}

	public void setRpt(int nRepeat) {
		rpt = nRepeat;
	}
	
	public void setBlk(int nblink) {
		blk = nblink;
	}
	
	public void setBa(int beamAll) {
		ba = beamAll;
	}
	
	public void setLo(int beamCircle) {
		lo = beamCircle;
	}
	
	public void setLx(int beamStop) {
		lx = beamStop;
	}

	public void setBr(int beamRight) {
		br = beamRight;
	}
	
	public void setBl(int beamLeft) {
		bl = beamLeft;
	}
	
	public void setBu(int beamUp) {
		bu = beamUp;
	}
	
	public void setBd(int beamDown) {
		bd = beamDown;
	}

	public int getSid() {
		return sid;
	}

	public int getTs() {
		return ts;		
	}
	public int getTv() {
		return tv;
	}
	public int getGs() {
		return gs;
	}
	public int getGv(){
		return gv;
	}
	public int getSs(){
		return ss;
	}
	public int getBlv(){
		return blv;
	}
	public int getBus(){
		return bus;
	}
	public int getSv(){
		return sv;
	}
	public int getVr(){
		return vr;
	}
	public int getSnd(){
		return snd;
	}
	public int getRpt(){
		return rpt;
	}
	public int getBlk(){
		return blk;
	}
	public int getBa(){
		return ba;
	}
	public int getLo() {
		return lo;
	}
	public int getLx() {
		return lx;
	}
	public int getBr() {
		return br;
	}
	public int getBl() {
		return bl;		
	}
	public int getBu() {
		return bu;
	}
	public int getBd() {
		return bd;
	}

}
