package com.sego.mvc.model.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.richitec.bean.ResultBean;

@JsonInclude(Include.NON_NULL) 
public class IdBean extends ResultBean {
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
