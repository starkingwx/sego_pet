package com.sego.mvc.controller;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.imeeting.framework.ContextLoader;
import com.imeeting.mvc.controller.ExceptionController;
import com.richitec.util.JSONUtil;
import com.richitec.util.StringUtil;
import com.sego.mvc.model.bean.PetInfo;
import com.sego.mvc.model.bean.PetInfos;
import com.sego.mvc.model.bean.PetUpdateReturnBean;
import com.sego.mvc.model.dao.PetInfoDao;

@Controller
@RequestMapping("/petinfo")
public class PetInfoController extends ExceptionController {
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
			@RequestParam(value = "petid", required = false) String petId,
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
			int update = petInfoDao.updatePetInfo(petId, nickname, sex, breed,
					age, height, weight, district, placeOftenGo);
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
	public void getPetInfoList(HttpServletResponse response,
			@RequestParam(value = "username") String userName)
			throws IOException {
		PetInfos petInfos = petInfoDao.getPetInfos(userName);
		petInfos.setResult("0");
		String json = JSONUtil.toString(petInfos);
		response.getWriter().print(json);
	}

	@RequestMapping(value = "/getpetdetail")
	public void getPetDetailInfo(HttpServletResponse response,
			@RequestParam(value = "petid") String petId) throws IOException {
		PetInfo petInfo = new PetInfo();
		if (StringUtil.isNullOrEmpty(petId)) {
			petInfo.setResult("1");
		} else {
			petInfo = petInfoDao.getPetDetail(petId);
			petInfo.setResult("0");
		}
		response.getWriter().print(JSONUtil.toString(petInfo)); 
	}
	
	

}
