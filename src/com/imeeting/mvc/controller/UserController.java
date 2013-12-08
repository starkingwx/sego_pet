package com.imeeting.mvc.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.imeeting.bean.UserBean;
import com.imeeting.constants.AccountBindStatus;
import com.imeeting.framework.Configuration;
import com.imeeting.framework.ContextLoader;
import com.richitec.bean.ResultBean;
import com.richitec.sms.client.SMSClient;
import com.richitec.ucenter.model.UserDAO;
import com.richitec.util.JSONUtil;
import com.richitec.util.CryptoUtil;
import com.richitec.util.RandomString;
import com.richitec.util.StringUtil;
import com.sego.mvc.model.DeviceManager;

@Controller
@RequestMapping("/user")
public class UserController extends ExceptionController {

	private static Log log = LogFactory.getLog(UserController.class);

	public static final String BEST_CHECK_CODE = "192837465"; // 万能

	private UserDAO userDao;
	private SMSClient smsClient;
	private Configuration config;

	public static final String ErrorCode = "error_code";
	public static final String PhoneNumberError = "phone_number_error";
	public static final String PhoneCodeError = "phone_code_error";
	public static final String PasswordError = "password_error";
	public static final String ConfirmPasswordError = "confirm_password_error";
	public static final String NicknameError = "nickname_error";

	@PostConstruct
	public void init() {
		userDao = ContextLoader.getUserDAO();
		config = ContextLoader.getConfiguration();
		smsClient = ContextLoader.getSMSClient();
	}

	@RequestMapping("/login")
	public void login(
			@RequestParam(value = "loginName") String loginName,
			@RequestParam(value = "loginPwd") String loginPwd,
			@RequestParam(value = "brand", required = false, defaultValue = "") String brand,
			@RequestParam(value = "model", required = false, defaultValue = "") String model,
			@RequestParam(value = "release", required = false, defaultValue = "") String release,
			@RequestParam(value = "sdk", required = false, defaultValue = "") String sdk,
			@RequestParam(value = "width", required = false, defaultValue = "0") String width,
			@RequestParam(value = "height", required = false, defaultValue = "0") String height,
			HttpServletResponse response, HttpSession session) throws Exception {
		UserBean user = null;
		try {
			user = userDao.getUserBean(loginName, loginPwd);
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		JSONObject json = new JSONObject();
		if (null != user) {
			json.put("result", "0");
			json.put("userId", user.getUserId());
			json.put("username", user.getUserName());
			json.put("userkey", user.getUserkey());
			json.put("nickname", user.getNickName());
//			if (user.getUserName() != null && !user.getUserName().equals("")) {
//				json.put("bind_status", AccountBindStatus.bind_phone.name());
//				json.put(AccountBindStatus.bind_phone.name(),
//						user.getUserName());
//			}
			session.setAttribute(UserBean.SESSION_BEAN, user);
			userDao.recordDeviceInfo(user.getUserId(), brand, model, release,
					sdk, width, height);
		} else {
			json.put("result", "1");
		}
		response.getWriter().print(json.toString());
	}

	/**
	 * forgetpwd.jsp 页面获取手机验证码请求。
	 * 
	 * @param session
	 * @param phoneNumber
	 * @return
	 */
	@RequestMapping("/validatePhoneNumber")
	public @ResponseBody
	String validatePhoneNumber(HttpSession session,
			@RequestParam(value = "phone") String phoneNumber,
			@RequestParam(value = "type") String type) {
		String result = userDao.checkRegisterPhone(phoneNumber, type);
		if ("3".equals(result)) {
			userDao.getPhoneCode(session, phoneNumber);
			return "200";
		} else {
			return "404";
		}
	}

	/**
	 * 用户忘记密码后重新设置密码
	 * 
	 * @param session
	 * @param phoneNumber
	 * @param phoneCode
	 * @param newPassword
	 * @param newPasswordConfirm
	 * @return
	 */
	@RequestMapping("/resetPassword")
	public @ResponseBody
	String resetPassword(HttpSession session,
			@RequestParam(value = "phone") String phoneNumber,
			@RequestParam(value = "code") String phoneCode,
			@RequestParam(value = "newPwd") String newPassword,
			@RequestParam(value = "newPwdConfirm") String newPasswordConfirm) {
		if (phoneNumber.isEmpty() || phoneCode.isEmpty()
				|| newPassword.isEmpty() || newPasswordConfirm.isEmpty()) {
			return "400";
		}

		String sessionPhoneNumber = (String) session
				.getAttribute("phonenumber");
		String sessionPhoneCode = (String) session.getAttribute("phonecode");
		if (null == sessionPhoneCode || null == sessionPhoneNumber) {
			return "410";
		}

		if (!phoneNumber.equals(sessionPhoneNumber)
				|| !phoneCode.equals(sessionPhoneCode)) {
			return "401";
		}

		if (!newPassword.equals(newPasswordConfirm)) {
			return "403";
		}

		String md5Password = CryptoUtil.md5(newPassword);
		if (userDao.changePassword(phoneNumber, md5Password) <= 0) {
			return "500";
		}

		session.removeAttribute("phonenumber");
		session.removeAttribute("phonecode");
		return "200";
	}

	@RequestMapping(value = "/resetpwd")
	public @ResponseBody
	String resetPwdForMobile(HttpSession session,
			@RequestParam(value = "newPwd") String newPassword,
			@RequestParam(value = "newPwdConfirm") String newPasswordConfirm) {
		Boolean flag = (Boolean) session.getAttribute("code_validated");
		String result = "0";
		if (flag == null || flag == false) {
			result = "7"; // haven't pass code validation
		}
		if (result.equals("0")) {
			String phone = (String) session.getAttribute("phonenumber");
			if (!StringUtil.isNullOrEmpty(phone)) {
				// reset pwd
				if (StringUtil.isNullOrEmpty(newPassword) || StringUtil.isNullOrEmpty(newPasswordConfirm)) {
					result = "1"; // password is empty
				} else if (!newPassword.equals(newPasswordConfirm)) {
					result = "2"; // two passwords are different
				} else {
					String md5Password = CryptoUtil.md5(newPassword);
					if(userDao.changePassword(phone, md5Password) > 0) {
						result = "0"; // success
					} else {
						result = "3"; // reset failed
					}
				}
			} else {
				result = "6"; // session timeout
			}
		}

		ResultBean resultBean = new ResultBean();
		resultBean.setResult(result);
		return JSONUtil.toString(resultBean);
	}

	@RequestMapping(value = "/websignup", method = RequestMethod.POST)
	public ModelAndView webSignup(HttpSession session,
			@RequestParam(value = "phoneNumber") String phoneNumber,
			@RequestParam(value = "phoneCode") String phoneCode,
			@RequestParam(value = "nickname") String nickname,
			@RequestParam(value = "password") String password,
			@RequestParam(value = "confirmPassword") String confirmPassword)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("signup");

		String sessionPhoneNumber = (String) session
				.getAttribute("phonenumber");
		String sessionPhoneCode = (String) session.getAttribute("phonecode");

		if (phoneNumber.isEmpty() || phoneCode.isEmpty() || password.isEmpty()
				|| confirmPassword.isEmpty() || nickname.isEmpty()) {
			mv.addObject(ErrorCode, HttpServletResponse.SC_BAD_REQUEST);
			if (phoneNumber.isEmpty()) {
				mv.addObject(PhoneNumberError,
						HttpServletResponse.SC_BAD_REQUEST);
			}
			if (phoneCode.isEmpty()) {
				mv.addObject(PhoneCodeError, HttpServletResponse.SC_BAD_REQUEST);
			}
			if (password.isEmpty()) {
				mv.addObject(PasswordError, HttpServletResponse.SC_BAD_REQUEST);
			}
			if (confirmPassword.isEmpty()) {
				mv.addObject(ConfirmPasswordError,
						HttpServletResponse.SC_BAD_REQUEST);
			}
			if (nickname.isEmpty()) {
				mv.addObject(NicknameError, HttpServletResponse.SC_BAD_REQUEST);
			}
			return mv;
		}

		if (!phoneNumber.equals(sessionPhoneNumber)
				|| !phoneCode.equals(sessionPhoneCode)) {
			mv.addObject(ErrorCode, HttpServletResponse.SC_UNAUTHORIZED);
			mv.addObject(PhoneCodeError, HttpServletResponse.SC_UNAUTHORIZED);
			return mv;
		}

		if (!password.equals(confirmPassword)) {
			mv.addObject(ErrorCode, HttpServletResponse.SC_FORBIDDEN);
			mv.addObject(ConfirmPasswordError, HttpServletResponse.SC_FORBIDDEN);
			return mv;
		}

		String result = userDao.regUser(phoneNumber, "", nickname, password,
				confirmPassword);

		if ("0".equals(result)) {
			mv.addObject(ErrorCode, HttpServletResponse.SC_OK);
		} else {
			mv.addObject(ErrorCode,
					HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return mv;
	}

	/**
	 * 用户从手机注册获取验证码请求。
	 * 
	 * @param phone
	 * @param type
	 *            类型：注册-register, 找回密码-resetpwd
	 * @param response
	 * @param session
	 * @throws Exception
	 */
	@RequestMapping("/getPhoneCode")
	public void getPhoneCode(@RequestParam(value = "phone") String phone,
			@RequestParam(value = "type") String type,
			HttpServletResponse response, HttpSession session) throws Exception {
		JSONObject jsonUser = new JSONObject();
		try {
			String result = "0";
			result = userDao.checkRegisterPhone(phone, type);
			log.info("check register phone return: " + result);
			if (result.equals("0")) {
				result = userDao.getPhoneCode(session, phone);
			}
			jsonUser.put("result", result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		response.getWriter().print(jsonUser.toString());
	}

	/**
	 * 验证手机客户端发送来的验证码
	 * 
	 * @param code
	 * @param response
	 * @param session
	 * @throws Exception
	 */
	@RequestMapping("/checkPhoneCode")
	public void checkPhoneCode(@RequestParam(value = "code") String code,
			HttpServletResponse response, HttpSession session) throws Exception {
		JSONObject jsonUser = new JSONObject();
		try {
			String result = "0";
			if (session.getAttribute("phonecode") != null) {
				result = userDao.checkPhoneCode(session, code);
			} else {
				result = "6"; // session timeout
			}
			if ("0".equals(result)) {
				// add phone code validated flag to session
				session.setAttribute("code_validated", true);
			}
			jsonUser.put("result", result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		response.getWriter().print(jsonUser.toString());
	}

	/**
	 * used to bind phone number
	 * 
	 * @param password
	 * @param password1
	 * @param deviceId
	 * @param nickname
	 * @param response
	 * @param session
	 * @throws Exception
	 */
	@RequestMapping("/regUser")
	public void regUser(
			@RequestParam(value = "phonecode", required = false) String phoneCode,
			@RequestParam(value = "password") String password,
			@RequestParam(value = "password1") String password1,
			@RequestParam(value = "deviceId", defaultValue = "") String deviceId,
			@RequestParam(value = "nickname", defaultValue = "") String nickname,
			HttpServletResponse response, HttpSession session) throws Exception {
		log.info("regUser");

		String result = "0";
		String phone = "";

		// if (session.getAttribute("phonecode") != null) {
		// result = userDao.checkPhoneCode(session, phoneCode);
		// } else {
		// result = "6"; // session timeout
		// }
		Boolean flag = (Boolean) session.getAttribute("code_validated");
		if (flag == null || flag == false) {
			result = "7"; // haven't pass code validation
		}
		if (result.equals("0")) {
			if (null == session.getAttribute("phonenumber")) {
				result = "6"; // session过期
			} else {
				phone = (String) session.getAttribute("phonenumber");
				if (!StringUtil.isNullOrEmpty(phone)) {
					if (nickname.length() == 0) {
						nickname = phone;
					}
					result = userDao.regUser(phone, deviceId, nickname,
							password, password1);
				} else {
					result = "6";
				}
			}
		}

		JSONObject jsonUser = new JSONObject();
		try {
			jsonUser.put("result", result);
			if ("0".equals(result)) {
				UserBean user = userDao.getUserBean(phone,
						CryptoUtil.md5(password));
				jsonUser.put("userId", user.getUserId());
				jsonUser.put("username", user.getUserName());
				jsonUser.put("userkey", user.getUserkey());
				jsonUser.put("bind_status", AccountBindStatus.bind_phone.name());
				jsonUser.put(AccountBindStatus.bind_phone.name(),
						user.getUserName());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		response.getWriter().print(jsonUser.toString());
		
		if ("0".equals(result)) {
			DeviceManager deviceManager = ContextLoader.getDeviceManager();
			deviceManager.insertUserData(phone, CryptoUtil.md5(password));
		}
	}

	@RequestMapping("/checkUserExist")
	public void checkUserExist(
			@RequestParam(value = "username", required = true) String userName,
			HttpServletResponse response) throws JSONException, SQLException,
			IOException {
		JSONObject ret = new JSONObject();
		boolean isExist = userDao.isExistsLoginName(userName);
		ret.put("result", isExist);
		response.getWriter().print(ret.toString());
	}

	@RequestMapping("/getUserPwd")
	@Deprecated
	public void getUserPassword(HttpServletResponse response,
			@RequestParam(value = "username") String userName)
			throws IOException {
		try {
			Map<String, Object> user = userDao.getUser(userName);
			if (user == null) {
				log.info("user is null");
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			String newPwd = RandomString.genRandomNum(6);
			int rows = userDao.changePassword(userName, CryptoUtil.md5(newPwd));
			if (rows > 0) {
				String msg = String
						.format("您的新密码是%s，请登录后及时修改您的密码。[智会]", newPwd);
				smsClient.sendTextMessage(userName, msg);
			} else {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
		} catch (DataAccessException e) {
			// e.printStackTrace();
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	@RequestMapping("/regAndLoginWithDeviceID")
	public void regAndLoginWithDeviceID(
			HttpServletResponse response,
			@RequestParam(value = "deviceId") String deviceId,
			@RequestParam(value = "brand", required = false, defaultValue = "") String brand,
			@RequestParam(value = "model", required = false, defaultValue = "") String model,
			@RequestParam(value = "release", required = false, defaultValue = "") String release,
			@RequestParam(value = "sdk", required = false, defaultValue = "") String sdk,
			@RequestParam(value = "width", required = false, defaultValue = "0") String width,
			@RequestParam(value = "height", required = false, defaultValue = "0") String height)
			throws JSONException, IOException {
		JSONObject ret = new JSONObject();
		Map<String, Object> user = userDao.getUserByDeviceId(deviceId);

		if (user != null) {
			String id = (String) user.get("id");
			String userKey = (String) user.get("userkey");
			String userName = (String) user.get("username");
			String result = "0";
			ret.put("userId", id);
			ret.put("userkey", userKey);
			if (userName != null && !userName.equals("")) {
				ret.put("bind_status", AccountBindStatus.bind_phone.name());
				ret.put(AccountBindStatus.bind_phone.name(), userName);
			}
			ret.put("result", result);
		} else {
			// register device id
			String result = userDao.registerDeviceId(deviceId);
			if ("0".equals(result)) {
				Map<String, Object> userBean = userDao
						.getUserByDeviceId(deviceId);
				String id = (String) userBean.get("id");
				String userName = (String) userBean.get("username");

				if (userName != null && !userName.equals("")) {
					ret.put("bind_status", AccountBindStatus.bind_phone.name());
					ret.put(AccountBindStatus.bind_phone.name(), userName);
				}
				ret.put("userId", id);
				ret.put("userkey", userBean.get("userkey"));

				userDao.recordDeviceInfo(id, brand, model, release, sdk, width,
						height);
			}
			ret.put("result", result);
		}
		response.getWriter().print(ret.toString());
	}
	
	@RequestMapping(value = "/thirdlogin")
	public void thirdAccountLogin(HttpServletResponse response,
			@RequestParam(value = "identifier") String identifier) throws IOException {
		ResultBean resultBean = new ResultBean();
		if (StringUtil.isNullOrEmpty(identifier)) {
			resultBean.setResult("1"); // identifier is empty
		} else {
			resultBean = userDao.loginWithThirdIdentifier(identifier);
		}
		response.getWriter().print(JSONUtil.toString(resultBean));
	}
}
