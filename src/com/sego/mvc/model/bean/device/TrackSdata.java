package com.sego.mvc.model.bean.device;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class TrackSdata {
	private long termid;
	private long x;
	private long y;
	private String termtime;
	private int status;
	private long vitality;
	private String roughaddr;

	public long getTermid() {
		return termid;
	}

	public void setTermid(long termid) {
		this.termid = termid;
	}

	public long getX() {
		return x;
	}

	public void setX(long x) {
		this.x = x;
	}

	public long getY() {
		return y;
	}

	public void setY(long y) {
		this.y = y;
	}

	public String getTermtime() {
		return termtime;
	}

	public void setTermtime(String time) {
		this.termtime = time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getVitality() {
		return vitality;
	}

	public void setVitality(long vitality) {
		this.vitality = vitality;
	}

	public String getRoughaddr() {
		return roughaddr;
	}

	public void setRoughaddr(String address) {
		this.roughaddr = address;
	}

}
