package com.imeeting.mvc.controller;

import java.io.IOException;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.imeeting.framework.ContextLoader;
import com.richitec.ucenter.model.UserDAO;
import com.richitec.util.CryptoUtil;
import com.richitec.util.StringUtil;

@Controller
@RequestMapping("/profile")
public class ProfileApiController {
	private static Log log = LogFactory.getLog(ProfileController.class);
	private UserDAO userDao;

	@PostConstruct
	public void init() {
		userDao = ContextLoader.getUserDAO();
	}

	@RequestMapping(value = "/changePwd")
	public void changePwdAPI(HttpServletResponse response,
			@RequestParam(value = "username") String userName,
			@RequestParam(value = "oldPwd") String oldPwd,
			@RequestParam(value = "newPwd") String newPwd,
			@RequestParam(value = "newPwdConfirm") String newPwdConfirm)
			throws IOException, JSONException {
		log.info(" username: " + userName + " oldPwd: " + oldPwd + " newpwd: "
				+ newPwd + " confirmpwd: " + newPwdConfirm);
		Map<String, Object> user = userDao.getUser(userName);
		String pwd = (String) user.get("password");
		String result = "0";
		if (!oldPwd.equals(pwd)) {
			result = "4"; // old password incorrect
		}

		if ("0".equals(result)) {
			if (StringUtil.isNullOrEmpty(newPwd)
					|| StringUtil.isNullOrEmpty(newPwdConfirm)) {
				result = "1"; // password is empty
			} else if (!newPwd.equals(newPwdConfirm)) {
				result = "2"; // two passwords are different
			}
		}

		if ("0".equals(result)) {
			String md5Password = CryptoUtil.md5(newPwd);
			if (userDao.changePassword(userName, md5Password) <= 0) {
				result = "3"; // change failed
			}
		}

		Map<String, Object> userMap = userDao.getUser(userName);
		String userkey = (String) userMap.get("userkey");
		JSONObject ret = new JSONObject();
		ret.put("result", result);
		ret.put("userkey", userkey);
		response.getWriter().print(ret.toString());
	}
}
