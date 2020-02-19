package kr.co.corners.model.mqtt;

public class MqttStartActionModel {
	private int sid;	// 스테이션 일련번호 (DB ID 아님. -1일 경우 해당 코디의 모든 스테이션을 지칭)
	private int diag;	// diagnostic 여부
	private int snd;	// 사운드 시나리오 (0~63)
	private int rpt;	// 사운드 반복 여부 (0 또는 1)
	private int blk;	// 빔 점멸 여부 (0 또는 1)
	private int lo;		// 동그라미 LED 상태 (0꺼짐 1켜짐 3점멸)
	private int lx;		// X자 LED 상태 (0꺼짐 1켜짐)
	private int ba;		// 모든방향 빔 상태 (0꺼짐 1켜짐)
	private int br;		// 우방향 빔 상태(0꺼짐 1켜짐)
	private int bl;		// 좌방향 빔 상태(0꺼짐 1켜짐)
	private int bu;		// 상방향 빔 상태(0꺼짐 1켜짐)
	private int bd;		// 하방향 빔 상태(0꺼짐 1켜짐)
	   
	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
		this.sid = sid;
	}
	public int getDiag() {
		return diag;
	}
	public void setDiag(int diag) {
		this.diag = diag;
	}
	public int getSnd() {
		return snd;
	}
	public void setSnd(int snd) {
		this.snd = snd;
	}
	public int getRpt() {
		return rpt;
	}
	public void setRpt(int rpt) {
		this.rpt = rpt;
	}
	public int getBlk() {
		return blk;
	}
	public void setBlk(int blk) {
		this.blk = blk;
	}
	public int getLo() {
		return lo;
	}
	public void setLo(int lo) {
		this.lo = lo;
	}
	public int getLx() {
		return lx;
	}
	public void setLx(int lx) {
		this.lx = lx;
	}
	public int getBa() {
		return ba;
	}
	public void setBa(int ba) {
		this.ba = ba;
	}
	public int getBr() {
		return br;
	}
	public void setBr(int br) {
		this.br = br;
	}
	public int getBl() {
		return bl;
	}
	public void setBl(int bl) {
		this.bl = bl;
	}
	public int getBu() {
		return bu;
	}
	public void setBu(int bu) {
		this.bu = bu;
	}
	public int getBd() {
		return bd;
	}
	public void setBd(int bd) {
		this.bd = bd;
	}   

}
