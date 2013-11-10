package com.sego.mvc.model.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.richitec.bean.ResultBean;

@JsonInclude(Include.NON_NULL) 
public class LeaveMsgs extends ResultBean {
	List<LeaveMsg> list;

	public List<LeaveMsg> getList() {
		return list;
	}

	public void setList(List<LeaveMsg> list) {
		this.list = list;
	}
	
}
