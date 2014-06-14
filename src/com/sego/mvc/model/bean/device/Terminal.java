package com.sego.mvc.model.bean.device;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.richitec.util.JSONUtil;

@JsonInclude(Include.NON_NULL)
public class Terminal {
	private String deviceno;
	private Long userid;
	private String name1;
	private String name2;
	private Integer type;
	private Integer icon;
	private Integer corpid;
	private Integer groupid;
	private String simphno;
	private String imei;
	private String installdate;
	private String expirydate;
	private String password;
	private Long id;
	
	
	public String getDeviceno() {
		return deviceno;
	}

	public void setDeviceno(String deviceno) {
		this.deviceno = deviceno;
	}

	public Long getUserid() {
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

	public Integer getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Integer getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public Integer getCorpid() {
		return corpid;
	}

	public void setCorpid(int corpid) {
		this.corpid = corpid;
	}

	public Integer getGroupid() {
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public static void main(String[] args) {
		Terminal terminal = new Terminal();
		terminal.setUserid(12);
		terminal.setName1("hello");
		
		String json = JSONUtil.toString(terminal);
		System.out.println("Json: " + json);
		
		
	}
}
