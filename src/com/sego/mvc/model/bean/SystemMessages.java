package com.sego.mvc.model.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.richitec.bean.ResultBean;

@JsonInclude(Include.NON_NULL) 
public class SystemMessages extends ResultBean {
	private List<SystemMessage> messages;

	public List<SystemMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<SystemMessage> messages) {
		this.messages = messages;
	}
	
}
