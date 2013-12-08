package com.sego.mvc.controller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.imeeting.framework.ContextLoader;
import com.imeeting.mvc.controller.ExceptionController;
import com.richitec.bean.ResultBean;
import com.richitec.util.FileUtil;
import com.richitec.util.JSONUtil;
import com.richitec.util.StringUtil;
import com.sego.mvc.model.DeviceManager;
import com.sego.mvc.model.bean.IdBean;
import com.sego.mvc.model.bean.PetInfo;
import com.sego.mvc.model.bean.PetInfos;
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
		IdBean petUpdateReturnBean = new IdBean();
		if (StringUtil.isNullOrEmpty(petId)) {
			if (!petInfoDao.hasPetInfo(userName)) {
				// create pet info
				long id = petInfoDao.createPetInfo(userName, nickname, sex,
						breed, age, height, weight, district, placeOftenGo);
				log.info("create pet id: " + id);
				if (id > 0) {
					petUpdateReturnBean.setResult("0");
					petUpdateReturnBean.setId(String.valueOf(id));
				} else {
					petUpdateReturnBean.setResult("4");
				}
			} else {
				petUpdateReturnBean.setResult("3"); // already has pet
			}
		} else {
			// save pet info
			int update = petInfoDao.updatePetInfo(petId, nickname, sex, breed,
					age, height, weight, district, placeOftenGo, null);
			if (update > 0) {
				petUpdateReturnBean.setResult("0");
				petUpdateReturnBean.setId(petId);
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
			petInfo.setResult("1"); // null id
		} else {
			petInfo = petInfoDao.getPetDetail(petId);
			petInfo.setResult("0");
		}
		response.getWriter().print(JSONUtil.toString(petInfo));
	}

	@RequestMapping(value = "/uploadavatar")
	public void uploadPetAvatar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ServletFileUpload upload = new ServletFileUpload(
				new DiskFileItemFactory());

		upload.setHeaderEncoding("UTF-8");

		List<FileItem> items;
		String userName = "";
		String petId = "";
		IdBean petUpdateReturnBean = new IdBean();
		try {
			items = upload.parseRequest(request);
			log.info("items : " + items);
			if (items == null) {
				throw new FileUploadException();
			}

			FileItem file2Upload = null;
			for (FileItem item : items) {
				log.info("field name: " + item.getFieldName());
				if ("avatar_file".equals(item.getFieldName())) {
					file2Upload = item;
				} else if ("petid".equals(item.getFieldName())) {
					petId = item.getString();
				} else if ("username".equals(item.getFieldName())) {
					userName = item.getString();
				}
			}
			if (file2Upload == null) {
				throw new FileUploadException("No avatar file");
			}

			String avatarFileName = UUID.randomUUID().toString();
			// save avatar file
			FileUtil.saveFile(avatarFileName, file2Upload);

			if (StringUtil.isNullOrEmpty(petId)) {
				if (!petInfoDao.hasPetInfo(userName)) {
					// create new avatar
					long id = petInfoDao.createPetAvatarInfo(avatarFileName,
							userName);
					log.info("create pet id: " + id);
					if (id > 0) {
						petUpdateReturnBean.setResult("0");
						petUpdateReturnBean.setId(String.valueOf(id));
					} else {
						petUpdateReturnBean.setResult("4");
					}
				} else {
					petUpdateReturnBean.setResult("3"); // already has pet
				}
			} else {
				// update avatar
				// String avatar = petInfoDao.getPetAvatar(petId);
				// if (!StringUtil.isNullOrEmpty(avatar)) {
				// avatarFileName = avatar;
				// }
				int update = petInfoDao.updatePetAvatar(petId, avatarFileName);
				if (update > 0) {
					petUpdateReturnBean.setResult("0");
					petUpdateReturnBean.setId(petId);
				} else {
					petUpdateReturnBean.setResult("1"); // update failed
				}
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
			petUpdateReturnBean.setResult("2"); // no file
		} catch (Exception e) {
			e.printStackTrace();
			petUpdateReturnBean.setResult("5"); // save file failed
		}
		response.getWriter().print(JSONUtil.toString(petUpdateReturnBean));
	}

	@RequestMapping(value = "/searchpets")
	public void searchPetInfo(HttpServletResponse response,
			@RequestParam(value = "phone") String phone) throws IOException {
		PetInfos petInfos = new PetInfos();
		if (StringUtil.isNullOrEmpty(phone)) {
			petInfos.setResult("1"); // phone is empty
		} else {
			petInfos = petInfoDao.searchPetInfos(phone);
			petInfos.setResult("0");
		}
		String json = JSONUtil.toString(petInfos);
		response.getWriter().print(json);
	}

	@RequestMapping(value = "/binddevice")
	public void bindPetDevice(HttpServletResponse response, 
			@RequestParam(value = "username") String userName,
			@RequestParam(value = "petid") String petId,
			@RequestParam(value = "deviceno") String deviceNo
			) throws IOException {
		ResultBean resultBean = new ResultBean();
		if (StringUtil.isNullOrEmpty(deviceNo)) {
			resultBean.setResult("4"); // device id is empty
		} else {
			petInfoDao.updatePetInfo(petId, null, null, null, null, null, null, null, null, deviceNo);
			
			DeviceManager deviceManager = ContextLoader.getDeviceManager();
			String result = deviceManager.bindDevice(userName, deviceNo);
			resultBean.setResult(result);
		}
		response.getWriter().print(JSONUtil.toString(resultBean));
	}
	
	
}
