package com.sego.mvc.model.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.richitec.bean.ResultBean;

@JsonInclude(Include.NON_NULL) 
public class LeaveMsg extends ResultBean {
	private long msgid;
	private String author;
	private String leaver_nickname;
	private int leaver_sex;
	private String leaver_avatar;
	private String content;
	private long petid;
	private long parentid;
	private long leave_timestamp;

	public long getMsgid() {
		return msgid;
	}
	public void setMsgid(long msgid) {
		this.msgid = msgid;
	}
	public String getLeaver_nickname() {
		return leaver_nickname;
	}
	public void setLeaver_nickname(String leaver_nickname) {
		this.leaver_nickname = leaver_nickname;
	}
	public int getLeaver_sex() {
		return leaver_sex;
	}
	public void setLeaver_sex(int leaver_sex) {
		this.leaver_sex = leaver_sex;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getPetid() {
		return petid;
	}
	public void setPetid(long petid) {
		this.petid = petid;
	}
	public long getParentid() {
		return parentid;
	}
	public void setParentid(long parentid) {
		this.parentid = parentid;
	}
	public long getLeave_timestamp() {
		return leave_timestamp;
	}
	public void setLeave_timestamp(long leave_timestamp) {
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
