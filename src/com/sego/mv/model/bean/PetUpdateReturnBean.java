package com.sego.mv.model.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.richitec.bean.ResultBean;

@JsonInclude(Include.NON_NULL) 
public class PetUpdateReturnBean extends ResultBean {
	private String petid;

	public String getPetid() {
		return petid;
	}

	public void setPetid(String petid) {
		this.petid = petid;
	}
}
