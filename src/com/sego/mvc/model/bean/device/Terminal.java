package com.sego.mvc.model.bean.device;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Terminal {
	private String deviceno;
	private long userid;
	private String name1;
	private String name2;
	private int type;
	private int icon;
	private int corpid;
	private int groupid;
	private String simphno;
	private String imei;
	private String installdate;
	private String expirydate;
	private String password;
	private long id;
	
	
	public String getDeviceno() {
		return deviceno;
	}

	public void setDeviceno(String deviceno) {
		this.deviceno = deviceno;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getName1() {
		return name1;
	}

	public void setName1(String name1) {
		this.name1 = name1;
	}

	public String getName2() {
		return name2;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public int getCorpid() {
		return corpid;
	}

	public void setCorpid(int corpid) {
		this.corpid = corpid;
	}

	public int getGroupid() {
		return groupid;
	}

	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}

	public String getSimphno() {
		return simphno;
	}

	public void setSimphno(String simphno) {
		this.simphno = simphno;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getInstalldate() {
		return installdate;
	}

	public void setInstalldate(String installdate) {
		this.installdate = installdate;
	}

	public String getExpirydate() {
		return expirydate;
	}

	public void setExpirydate(String expirydate) {
		this.expirydate = expirydate;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
