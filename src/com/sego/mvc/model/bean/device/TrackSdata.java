package com.sego.mvc.model.bean.device;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class TrackSdata {
	private Long termid;
	private Long x;
	private Long y;
	private String termtime;
	private Integer status;
	private Long vitality;
	private String roughaddr;

	public Long getTermid() {
		return termid;
	}

	public void setTermid(long termid) {
		this.termid = termid;
	}

	public Long getX() {
		return x;
	}

	public void setX(long x) {
		this.x = x;
	}

	public Long getY() {
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Long getVitality() {
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
