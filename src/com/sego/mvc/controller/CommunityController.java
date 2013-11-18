package com.sego.mvc.controller;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.imeeting.framework.ContextLoader;
import com.richitec.bean.ResultBean;
import com.richitec.util.JSONUtil;
import com.richitec.util.StringUtil;
import com.sego.mvc.model.bean.LeaveMsg;
import com.sego.mvc.model.bean.LeaveMsgs;
import com.sego.mvc.model.bean.PetInfos;
import com.sego.mvc.model.dao.CommunityDao;
import com.sego.mvc.model.dao.PetInfoDao;

@Controller
@RequestMapping("/community")
public class CommunityController {
	private CommunityDao communityDao;
	private PetInfoDao petInfoDao;

	@PostConstruct
	public void init() {
		communityDao = ContextLoader.getCommunityDao();
		petInfoDao = ContextLoader.getPetInfoDao();
	}

	@RequestMapping(value = "/getrecommendpets")
	public void getRecommendedPets(HttpServletResponse response,
			@RequestParam(value = "username") String userName)
			throws IOException {
		PetInfos petInfos = communityDao.getRecommendedPets(userName);
		petInfos.setResult("0");
		response.getWriter().print(JSONUtil.toString(petInfos));
	}

	@RequestMapping(value = "/getnearbypets")
	public void getNearbyPets(HttpServletResponse response,
			@RequestParam(value = "longitude") String longitude,
			@RequestParam(value = "latitude") String latitude)
			throws IOException {
		PetInfos petInfos = new PetInfos();
		if (StringUtil.isNullOrEmpty(longitude)
				|| StringUtil.isNullOrEmpty(latitude)) {
			petInfos.setResult("1");
		} else {
			try {
				double lng = Double.parseDouble(longitude);
				double lat = Double.parseDouble(latitude);
				petInfos = communityDao.getNearbyPets(lng, lat);
				petInfos.setResult("0");
			} catch (NumberFormatException e) {
				e.printStackTrace();
				petInfos.setResult("2");
			}
		}
		response.getWriter().print(JSONUtil.toString(petInfos));
	}

	@RequestMapping(value = "/getconcernpets")
	public void getConcernedPets(HttpServletResponse response,
			@RequestParam(value = "username") String userName)
			throws IOException {
		PetInfos petInfos = communityDao.getConcernedPets(userName);
		petInfos.setResult("0");
		response.getWriter().print(JSONUtil.toString(petInfos));
	}

	@RequestMapping(value = "/concernpet")
	public void concernPet(HttpServletResponse response,
			@RequestParam(value = "username") String userName,
			@RequestParam(value = "petid") String petId) throws IOException {
		ResultBean resultBean = new ResultBean();
		if (StringUtil.isNullOrEmpty(petId)) {
			resultBean.setResult("2"); // petid is null
		} else {
			if (!petInfoDao.isPetExist(petId)) {
				resultBean.setResult("1"); // pet doesn't exist
			} else {
				int res = communityDao.concernPet(userName, petId);
				if (res == -2) {
					resultBean.setResult("3"); // already concerned
				} else if (res > 0) {
					resultBean.setResult("0");
				} else {
					resultBean.setResult("4"); // concern failed
				}
			}
		}
		response.getWriter().print(JSONUtil.toString(resultBean));
	}

	@RequestMapping(value = "/unconcernpet")
	public void unconcernPet(HttpServletResponse response,
			@RequestParam(value = "username") String userName,
			@RequestParam(value = "petid") String petId) throws IOException {
		ResultBean resultBean = new ResultBean();
		if (StringUtil.isNullOrEmpty(petId)) {
			resultBean.setResult("2"); // petid is null
		} else {
			if (!petInfoDao.isPetExist(petId)) {
				resultBean.setResult("1"); // pet doesn't exist
			} else {
				int row = communityDao.unconcernPet(userName, petId);
				if (row > 0) {
					resultBean.setResult("0");
				} else {
					resultBean.setResult("3"); // haven't concern before
				}
			}
		}
		response.getWriter().print(JSONUtil.toString(resultBean));
	}

	@RequestMapping(value = "/leavemsg")
	public void leaveMsgToPet(HttpServletResponse response,
			@RequestParam(value = "username") String userName,
			@RequestParam(value = "petid") String petId,
			@RequestParam(value = "content") String content) throws IOException {
		ResultBean resultBean = new ResultBean();
		if (StringUtil.isNullOrEmpty(petId)) {
			resultBean.setResult("2"); // petid is null
		} else {
			if (!petInfoDao.isPetExist(petId)) {
				resultBean.setResult("1"); // pet doesn't exist
			} else {
				int row = communityDao.leaveMsg(userName, petId, content, "0");
				if (row > 0) {
					resultBean.setResult("0");
				} else {
					resultBean.setResult("3"); // add msg failed
				}
			}
		}
		response.getWriter().print(JSONUtil.toString(resultBean));
	}

	@RequestMapping(value = "/replymsg")
	public void replyMsg(HttpServletResponse response,
			@RequestParam(value = "username") String userName,
			@RequestParam(value = "content") String content,
			@RequestParam(value = "msgid") String msgId) throws IOException {
		ResultBean resultBean = new ResultBean();
		if (StringUtil.isNullOrEmpty(msgId)) {
			resultBean.setResult("2"); // msgid is null
		} else {
			if (!communityDao.isMsgExist(msgId)) {
				resultBean.setResult("1"); // msg doesn't exist
			} else {
				int row = communityDao.replyMsg(userName, content, msgId);
				if (row > 0) {
					resultBean.setResult("0");
				} else {
					resultBean.setResult("3"); // add msg failed
				}
			}
		}
		response.getWriter().print(JSONUtil.toString(resultBean));
	}

	@RequestMapping(value = "/delmsg")
	public void delMsg(HttpServletResponse response,
			@RequestParam(value = "username") String userName,
			@RequestParam(value = "msgid") String msgId) throws IOException {
		ResultBean resultBean = new ResultBean();
		if (StringUtil.isNullOrEmpty(msgId)) {
			resultBean.setResult("2"); // msgid is null
		} else {
			if (!communityDao.isMsgExist(msgId)) {
				resultBean.setResult("1"); // msg doesn't exist
			} else {
				int row = communityDao.delMsg(msgId);
				if (row > 0) {
					resultBean.setResult("0");
				} else {
					resultBean.setResult("3"); // del msg failed
				}
			}
		}
		response.getWriter().print(JSONUtil.toString(resultBean));
	}
	
	@RequestMapping(value = "/getleavemsgbox")
	public void getLeaveMsgBox(HttpServletResponse response,
			@RequestParam(value = "username") String userName) {
		
	}
	
	@RequestMapping(value = "/getleavemsgs")
	public void getLeaveMsgs(HttpServletResponse response,
			@RequestParam(value = "username") String userName,
			@RequestParam(value = "petid") String petId) throws IOException {
		LeaveMsgs msgs = new LeaveMsgs();
		if (StringUtil.isNullOrEmpty(petId)) {
			msgs.setResult("2"); // petid is null
		} else {
			msgs = communityDao.getLeaveMsgsByUser(userName, petId);
			msgs.setResult("0");
		}
		response.getWriter().print(JSONUtil.toString(msgs));
	}
	
	@RequestMapping(value = "/getleavemsgdetail")
	public void getLeaveMsgDetail(HttpServletResponse response,
			@RequestParam(value = "msgid") String msgId) throws IOException {
		LeaveMsgs msgs = new LeaveMsgs();
		if (StringUtil.isNullOrEmpty(msgId)) {
			msgs.setResult("2"); // msgid is null
		} else {
			msgs = communityDao.getRelatedMsgs(msgId);
			msgs.setResult("0");
		}
		response.getWriter().print(JSONUtil.toString(msgs));
	}
	
	@RequestMapping(value = "/getblacklist")
	public void getBlackList(HttpServletResponse response,
			@RequestParam(value = "username") String userName) throws IOException {
		PetInfos petInfos = communityDao.getPetBlackList(userName);
		petInfos.setResult("0");
		response.getWriter().print(JSONUtil.toString(petInfos));
	}
	
	@RequestMapping(value = "/addblacklist")
	public void addPetToBlackList(HttpServletResponse response,
			@RequestParam(value = "username") String userName,
			@RequestParam(value = "petid") String petId) throws IOException {
		ResultBean resultBean = new ResultBean();
		if (StringUtil.isNullOrEmpty(petId)) {
			resultBean.setResult("2"); // petid is null
		} else {
			if (!petInfoDao.isPetExist(petId)) {
				resultBean.setResult("1"); // pet doesn't exist
			} else if (communityDao.isPetInBlackList(petId, userName)){
				resultBean.setResult("3"); // already in black list
			} else {
				int row = communityDao.addPetToBlackList(petId, userName);
				if (row > 0) {
					resultBean.setResult("0");
				} else {
					resultBean.setResult("4"); // add failed
				}
			}
		}
		response.getWriter().print(JSONUtil.toString(resultBean));
	}
	
	@RequestMapping(value = "/delblacklist")
	public void delPetFromBlackList(HttpServletResponse response,
			@RequestParam(value = "username") String userName,
			@RequestParam(value = "petid") String petId) throws IOException {
		ResultBean resultBean = new ResultBean();
		if (StringUtil.isNullOrEmpty(petId)) {
			resultBean.setResult("2"); // petid is null
		} else {
			if (!petInfoDao.isPetExist(petId)) {
				resultBean.setResult("1"); // pet doesn't exist
			} else if (!communityDao.isPetInBlackList(petId, userName)){
				resultBean.setResult("3"); // not in black list
			} else {
				int row = communityDao.delPetFromBlackList(petId, userName);
				if (row > 0) {
					resultBean.setResult("0");
				} else {
					resultBean.setResult("4"); // del failed
				}
			}
		}
		response.getWriter().print(JSONUtil.toString(resultBean));
	}
}
