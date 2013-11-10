package com.sego.mvc.model.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.richitec.bean.ResultBean;

@JsonInclude(Include.NON_NULL) 
public class LeaveMsg extends ResultBean {
	private String msgid;
	private String author;
	private String leaver_nickname;
	private String leaver_sex;
	private String leaver_avatar;
	private String content;
	private String petid;
	private String parentid;
	private String leave_timestamp;

	public String getMsgid() {
		return msgid;
	}
	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}
	public String getLeaver_nickname() {
		return leaver_nickname;
	}
	public void setLeaver_nickname(String leaver_nickname) {
		this.leaver_nickname = leaver_nickname;
	}
	public String getLeaver_sex() {
		return leaver_sex;
	}
	public void setLeaver_sex(String leaver_sex) {
		this.leaver_sex = leaver_sex;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPetid() {
		return petid;
	}
	public void setPetid(String petid) {
		this.petid = petid;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public String getLeave_timestamp() {
		return leave_timestamp;
	}
	public void setLeave_timestamp(String leave_timestamp) {
		this.leave_timestamp = leave_timestamp;
	}
	public String getLeaver_avatar() {
		return leaver_avatar;
	}
	public void setLeaver_avatar(String leaver_avatar) {
		this.leaver_avatar = leaver_avatar;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	
}
