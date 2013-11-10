package com.sego.mvc.model.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.richitec.bean.ResultBean;

@JsonInclude(Include.NON_NULL) 
public class PetInfos extends ResultBean {
	List<PetInfo> list;

	public List<PetInfo> getList() {
		return list;
	}

	public void setList(List<PetInfo> list) {
		this.list = list;
	}

}