package kr.co.corners.model.mqtt;

public class MqttRefreshModel {
	private int site_id;
	private String cmd;
	
	public int getSite_id() {
		return site_id;
	}
	public void setSite_id(int site_id) {
		this.site_id = site_id;
	}
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}	
}
