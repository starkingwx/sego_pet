package com.richitec.ucenter.model;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.imeeting.bean.UserBean;
import com.imeeting.constants.UserAccountStatus;
import com.imeeting.framework.ContextLoader;
import com.imeeting.mvc.controller.UserController;
import com.richitec.sms.client.SMSHttpResponse;
import com.richitec.util.CryptoUtil;
import com.richitec.util.RandomString;
import com.richitec.util.ValidatePattern;

public class UserDAO {
	private static Log log = LogFactory.getLog(UserDAO.class);
	public static final String PASSWORD_STR = "huuguanghui";

	private JdbcTemplate jdbc;

	public void setDataSource(DataSource ds) {
		jdbc = new JdbcTemplate(ds);
	}

	/**
	 * 获得手机验证码
	 * 
	 * @param session
	 * @param phone
	 * @param phoneCode
	 * @return
	 */
	public String getPhoneCode(HttpSession session, String phone) {
		String result = "0";
		String phoneCode = RandomString.validateCode();
		log.info("phone code: " + phoneCode);
		try {
			session.setAttribute("phonenumber", phone);
			session.setAttribute("phonecode", phoneCode);
			String content = "验证码：" + phoneCode + " [赛果宠物]";
			SMSHttpResponse response = ContextLoader.getSMSClient()
					.sendTextMessage(phone, content);
			log.info("sms return: " + response.getCode());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 注册
	 * 
	 * @return
	 */
	public String regUser(String userName, String deviceId, String nickname,
			String password, String password1) {
		String result = checkRegisterUser(userName, password, password1);
		if (result.equals("0")) {
			Map<String, Object> user = null;
			if (!"".equals(deviceId)) {
				user = getUserByDeviceId(deviceId);
				if (user != null) {
					String id = (String) user.get("id");
					String sql = "UPDATE im_user SET username = ?, password = ?, nickname = ? WHERE id = ?";
					try {
						jdbc.update(sql, userName, CryptoUtil.md5(password),
								nickname, id);
						result = "0";
					} catch (Exception e) {
						log.info(e.getMessage());
						result = "1001";
					}
				}
			}

			if (user == null) {
				String id = RandomString.genRandomChars(32);
				String userkey = RandomString.genRandomChars(32);
				String sql = "INSERT INTO im_user(id, username, deviceId, password, userkey, nickname) VALUES (?,?,?,?,?,?)";
				Object[] params = new Object[] { id, userName, deviceId,
						CryptoUtil.md5(password), userkey, nickname };
				try {
					jdbc.update(sql, params);
					result = "0";
				} catch (Exception e) {
					log.info(e.getMessage());
					result = "1";
				}
			}
		}
		return result;
	}

	public UserBean getUserBean(String loginName, final String loginPwd)
			throws DataAccessException {
		String sql = "SELECT id, username, password, userkey, nickname FROM im_user WHERE username=? AND password=? AND status = ?";
		Object[] params = new Object[] { loginName, loginPwd,
				UserAccountStatus.success.name() };
		return jdbc.queryForObject(sql, params, new RowMapper<UserBean>() {
			@Override
			public UserBean mapRow(ResultSet rs, int rowCount)
					throws SQLException {
				UserBean user = new UserBean();
				user.setUserId(rs.getString("id"));
				user.setUserName(rs.getString("username"));
				user.setNickName(rs.getString("nickname"));
				user.setPassword(rs.getString("password"));
				user.setUserkey(rs.getString("userkey"));
				return user;
			}
		});
	}

	/**
	 * record the device info of login user
	 * 
	 * @param userId
	 * @param brand
	 * @param model
	 * @param release
	 * @param sdk
	 * @param width
	 * @param height
	 */
	public void recordDeviceInfo(String userId, String brand, String model,
			String release, String sdk, String width, String height) {
		log.info("record device info - user id:  " + userId + " brand: "
				+ brand);

		String sql = "SELECT count(userId) FROM im_device_info WHERE userId = ?";
		int count = jdbc.queryForInt(sql, userId);
		if (count > 0) {
			jdbc.update(
					"UPDATE im_device_info SET brand=?, model=?, "
							+ "release_ver=?, sdk=?, width=?, height=? WHERE userId = ?",
					brand, model, release, sdk, width, height, userId);
		} else {
			jdbc.update("INSERT INTO im_device_info VALUE(?,?,?,?,?,?,?)",
					userId, brand, model, release, sdk, width, height);
		}
	}

	public String checkPhoneCode(HttpSession session, String code) {
		if (code.equals(UserController.BEST_CHECK_CODE)) {
			return "0";
		}

		if (code.equals("")) {
			return "1"; // code is required
		} else if (!code.equals(session.getAttribute("phonecode"))) {
			return "2"; // error code
		} else {
			return "0";
		}
	}

	/**
	 * 判断用户注册信息是否正确
	 * 
	 * @param session
	 * @param phone
	 * @param password
	 * @param password1
	 * @param code
	 * @return
	 */
	public String checkRegisterUser(String phone, String password,
			String password1) {
		try {
			if (phone.equals("")) {
				return "1"; // 手机号码必填
			} else if (!ValidatePattern.isValidMobilePhone(phone)) {
				return "2"; // 手机号码格式错误
			} else if (isExistsLoginName(phone)) {
				return "3"; // 手机号码已存�?
			} else if (password.equals("")) {
				return "4"; // 密码必填
			} else if (!password.equals(password1)) {
				return "5"; // 两次密码输入不一�?
			} else {
				return "0";
			}
		} catch (Exception e) {
			return "1001";
		}

	}

	/**
	 * 判断手机号码是否正确
	 * 
	 * @param phone
	 * @return
	 */
	public String checkRegisterPhone(String phone, String type) {
		try {
			if (phone.equals("")) {
				return "1"; // 手机号码必填
			} else if (!ValidatePattern.isValidMobilePhone(phone)) {
				return "2"; // 手机号码格式错误
			} else if ("register".equals(type) && isExistsLoginName(phone)) {
				return "3"; // 手机号码已存在
			} else if ("resetpwd".equals(type) && !isExistsLoginName(phone)) {
				return "4"; // 手机号不存在
			} else {
				return "0";
			}
		} catch (Exception e) {
			return "1001";
		}
	}

	/**
	 * 判断该用户名是否存在
	 * 
	 * @param loginName
	 * @return
	 * @throws SQLException
	 */
	public boolean isExistsLoginName(String loginName) throws SQLException {
		String sql = "SELECT count(username) FROM im_user WHERE username = ?";
		Object[] params = new Object[] { loginName };
		return jdbc.queryForInt(sql, params) > 0;
	}

	/**
	 * 获得userkey
	 * 
	 * @param userId
	 * @return
	 */
	public String getUserKey(String userId) {
		String sql = "SELECT userkey FROM im_user WHERE id = ? OR username = ?";
		Object[] params = new Object[] { userId, userId };
		return jdbc.queryForObject(sql, params, String.class);
	}

	public String saveToken(String userName, String token) {
		String retCode = "0";
		String sql = "UPDATE im_token SET token = ? WHERE username = ?";
		int affectedRows = jdbc.update(sql, token, userName);
		if (affectedRows == 0) {
			// no user existed, insert new one
			sql = "INSERT INTO im_token(username, token) VALUES(?,?)";
			int rows = jdbc.update(sql, userName, token);
			retCode = rows > 0 ? "0" : "1001";
		}
		return retCode;
	}

	public int changePassword(String userName, String md5Password) {
		String sql = "UPDATE im_user SET password=?, userkey=? WHERE username=?";
		String userkey = CryptoUtil.md5(RandomString.genRandomChars(10));
		return jdbc.update(sql, md5Password, userkey, userName);
	}

	public int updateUserAccountStatus(String userId, UserAccountStatus status) {
		String sql = "UPDATE im_user SET status = ? WHERE id = ?";
		return jdbc.update(sql, status.name(), userId);
	}

	/**
	 * get nickname of user
	 * 
	 * @param userNameList
	 *            - IN Operation parameter eg. "(x, x, x)"
	 * @return
	 */
	public List<Map<String, Object>> getNicknameInfo(String userNameList) {
		String sql = "SELECT username, nickname FROM im_user WHERE username IN "
				+ userNameList;
		log.info(sql);
		return jdbc.queryForList(sql);
	}

	public String getNickname(String userName) {
		String nickname = "";
		String sql = "SELECT nickname FROM im_user WHERE username = ?";
		try {
			nickname = jdbc.queryForObject(sql, new Object[] { userName },
					String.class);
		} catch (Exception e) {
		}
		return nickname;
	}

	public int changeNickname(String userName, String nickname) {
		String sql = "UPDATE im_user SET nickname = ? WHERE username = ?";
		return jdbc.update(sql, nickname, userName);
	}

	public Map<String, Object> getUser(String userName) {
		String sql = "SELECT * FROM im_user WHERE username = ?";
		return jdbc.queryForMap(sql, userName);
	}

	@Deprecated
	public Map<String, Object> getUserByDeviceId(String deviceId) {
		String sql = "SELECT * FROM im_user WHERE deviceId = ?";
		Map<String, Object> user = null;
		try {
			user = jdbc.queryForMap(sql, deviceId);
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return user;
	}

	@Deprecated
	public Map<String, Object> getUserById(String userId) {
		String sql = "SELECT * FROM im_user WHERE id = ?";
		Map<String, Object> user = null;
		try {
			user = jdbc.queryForMap(sql, userId);
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return user;
	}

	public String registerDeviceId(String deviceId) {
		String id = RandomString.genRandomChars(20);
		String userKey = CryptoUtil.md5(id);
		String sql = "INSERT INTO im_user (id, username, deviceId, password, userkey, nickname) VALUES(?,?,?,?,?,?)";
		String result = "0";
		try {
			jdbc.update(sql, id, "", deviceId, "", userKey, "");
		} catch (Exception e) {
			result = "1001";
		}
		return result;
	}

	public UserBean loginWithThirdIdentifier(String identifier) {
		UserBean userBean = new UserBean();
		try {
			Map<String, Object> userInfo = getUser(identifier);
			// user exist, just return
			userBean.setUserId((String) userInfo.get("id"));
			userBean.setUserName((String) userInfo.get("username"));
			userBean.setUserkey((String) userInfo.get("userkey"));
			userBean.setResult("0");
			// TODO: insert user data to device server
		} catch (Exception e) {
			log.info(e.getMessage());
			// user doesn't exist, insert new
			String id = RandomString.genRandomChars(32);
			String userkey = RandomString.genRandomChars(32);
			String sql = "INSERT INTO im_user(id, username, password, userkey) VALUES (?,?,?,?)";
			Object[] params = new Object[] { id, identifier,
					CryptoUtil.md5("123"), userkey };
			try {
				int rows = jdbc.update(sql, params);
				if (rows > 0) {
					userBean.setResult("0");
					userBean.setUserId(id);
					userBean.setUserName(identifier);
					userBean.setUserkey(userkey);
					// TODO: insert user data to device server
				} else {
					userBean.setResult("2");
				}
			} catch (Exception ex) {
				log.info(e.getMessage());
				userBean.setResult("2"); // login failed
			}
		}
		return userBean;
	}
}
