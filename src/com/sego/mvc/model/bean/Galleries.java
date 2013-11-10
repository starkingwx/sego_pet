package com.sego.mvc.model.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.richitec.bean.ResultBean;

@JsonInclude(Include.NON_NULL) 
public class Galleries extends ResultBean {
	private List<Gallery> list;

	public List<Gallery> getList() {
		return list;
	}

	public void setList(List<Gallery> list) {
		this.list = list;
	}

}
