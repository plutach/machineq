package kr.co.corners.model;

public class deviceInfo {

	private String DevEUI;
	private String Payload;
	private String TargetPort;
	private boolean Confirm;
	private boolean FlushQueue;
	
	
	public String getDevEUI() {
		return DevEUI;
	}
	public void setDevEUI(String DevEUI) {
		this.DevEUI = DevEUI;
	}
	public String getPayload() {
		return Payload;
	}
	public void setPayload(String Payload) {
		this.Payload = Payload;
	}
	public String getTargetPort() {
		return TargetPort;
	}
	public void setTargetPort(String TargetPort) {
		this.TargetPort = TargetPort;
	}
	public boolean isConfirm() {
		return Confirm;
	}
	public void setConfirm(boolean Confirm) {
		this.Confirm = Confirm;
	}
	public boolean isFlushQueue() {
		return FlushQueue;
	}
	public void setFlushQueue(boolean flushQueue) {
		FlushQueue = flushQueue;
	}

	
}
