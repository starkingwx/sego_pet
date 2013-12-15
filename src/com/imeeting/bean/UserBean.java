package com.imeeting.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.richitec.bean.ResultBean;

@JsonInclude(Include.NON_NULL)
public class UserBean extends ResultBean {
	public static final String SESSION_BEAN = "userbean";
	private String userId;
	private String username;
	private String nickname;
	private String userkey;
	private String password;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String name) {
		this.username = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickName) {
		this.nickname = nickName;
	}

	public String getDisplayName() {
		if (nickname == null || nickname.length() <= 0) {
			return username;
		} else {
			return nickname;
		}
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserkey() {
		return userkey;
	}

	public void setUserkey(String userKey) {
		this.userkey = userKey;
	}
}
