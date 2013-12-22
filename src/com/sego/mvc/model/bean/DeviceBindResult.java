package com.sego.mvc.model.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.richitec.bean.ResultBean;

@JsonInclude(Include.NON_NULL)
public class DeviceBindResult extends ResultBean {
	private long device_id;
	private String device_password;

	public long getDevice_id() {
		return device_id;
	}

	public void setDevice_id(long device_id) {
		this.device_id = device_id;
	}

	public String getDevice_password() {
		return device_password;
	}

	public void setDevice_password(String device_password) {
		this.device_password = device_password;
	}

}
