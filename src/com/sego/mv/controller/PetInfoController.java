package com.sego.mv.controller;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.imeeting.framework.ContextLoader;
import com.richitec.util.JSONUtil;
import com.richitec.util.StringUtil;
import com.sego.mv.model.bean.PetInfos;
import com.sego.mv.model.bean.PetUpdateReturnBean;
import com.sego.mv.model.dao.PetInfoDao;

@Controller
@RequestMapping("/petinfo")
public class PetInfoController {
	private static Log log = LogFactory.getLog(PetInfoController.class);
	private PetInfoDao petInfoDao;

	@PostConstruct
	public void init() {
		petInfoDao = ContextLoader.getPetInfoDao();
	}

	@RequestMapping(value = "/modify")
	public void modifyPetInfo(
			HttpServletResponse response,
			@RequestParam(value = "username") String userName,
			@RequestParam(value = "id", required = false) String petId,
			@RequestParam(value = "nickname", defaultValue = "") String nickname,
			@RequestParam(value = "sex", defaultValue = "") String sex,
			@RequestParam(value = "breed", defaultValue = "") String breed,
			@RequestParam(value = "age", defaultValue = "") String age,
			@RequestParam(value = "height", defaultValue = "") String height,
			@RequestParam(value = "weight", defaultValue = "") String weight,
			@RequestParam(value = "district", defaultValue = "") String district,
			@RequestParam(value = "placeoftengo", defaultValue = "") String placeOftenGo)
			throws IOException {
		PetUpdateReturnBean petUpdateReturnBean = new PetUpdateReturnBean();
		if (StringUtil.isNullOrEmpty(petId)) {
			if (!petInfoDao.hasPetInfo(userName)) {
				// create pet info
				int id = petInfoDao.createPetInfo(userName, nickname, sex,
						breed, age, height, weight, district, placeOftenGo);
				log.info("create pet id: " + id);
				if (id > 0) {
					petUpdateReturnBean.setResult("0");
					petUpdateReturnBean.setPetid(String.valueOf(id));
				} else {
					petUpdateReturnBean.setResult("4");
				}
			} else {
				petUpdateReturnBean.setResult("3");
			}
		} else {
			// save pet info
			int update = petInfoDao.updatePetInfo(petId, nickname, sex, breed, age, height, weight, district, placeOftenGo);
			if (update > 0) {
				petUpdateReturnBean.setResult("0");
				petUpdateReturnBean.setPetid(petId);
			} else {
				petUpdateReturnBean.setResult("1");
			}
		}
		response.getWriter().print(JSONUtil.toString(petUpdateReturnBean));
	}

	@RequestMapping(value = "/getpets")
	public void getPetInfoList(	HttpServletResponse response,
			@RequestParam(value = "username") String userName) throws IOException {
		PetInfos petInfos = petInfoDao.getPetInfos(userName);
		String json = JSONUtil.toString(petInfos);
		response.getWriter().print(json);
	}
	
}
