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
import com.richitec.bean.ResultBean;
import com.richitec.util.FileUtil;
import com.richitec.util.JSONUtil;
import com.richitec.util.StringUtil;
import com.sego.mvc.model.bean.Galleries;
import com.sego.mvc.model.bean.Gallery;
import com.sego.mvc.model.bean.IdBean;
import com.sego.mvc.model.bean.Photo;
import com.sego.mvc.model.dao.GalleryDao;

@Controller
@RequestMapping("/gallery")
public class GalleryController {
	private static Log log = LogFactory.getLog(GalleryController.class);
	private GalleryDao galleryDao;

	@PostConstruct
	public void init() {
		galleryDao = ContextLoader.getGalleryDao();
	}

	/**
	 * 获取相册列表
	 * @param response
	 * @param userName
	 * @param petId
	 * @throws IOException
	 */
	@RequestMapping(value = "/getgalleries")
	public void getGalleries(HttpServletResponse response,
			@RequestParam(value = "username") String userName,
			@RequestParam(value = "petid", required = false) String petId)
			throws IOException {
		Galleries galleries = new Galleries();
		if (StringUtil.isNullOrEmpty(petId)) {
			galleries = galleryDao.getGalleries(userName);
		} else {
			galleries = galleryDao.getGalleriesByPetId(petId);
		}
		galleries.setResult("0");
		response.getWriter().print(JSONUtil.toString(galleries));
	}

	/**
	 * 获取相册内的所有照片
	 * @param response
	 * @param galleryId
	 * @throws IOException
	 */
	@RequestMapping(value = "/getgallery")
	public void gatGallery(HttpServletResponse response,
			@RequestParam(value = "galleryid") String galleryId)
			throws IOException {
		Gallery gallery = new Gallery();
		if (StringUtil.isNullOrEmpty(galleryId)) {
			gallery.setResult("1"); // galler id is null
		} else {
			gallery = galleryDao.getGallery(galleryId);
			gallery.setResult("0");
		}
		response.getWriter().print(JSONUtil.toString(gallery));
	}

	/**
	 * 创建相册
	 * @param response
	 * @param userName
	 * @param title
	 * @throws IOException
	 */
	@RequestMapping(value = "/creategallery")
	public void createGallery(HttpServletResponse response,
			@RequestParam(value = "username") String userName,
			@RequestParam(value = "title") String title) throws IOException {
		IdBean resultBean = new IdBean();
		long id = galleryDao.createGallery(userName, title);
		if (id > 0) {
			resultBean.setResult("0");
			resultBean.setId(String.valueOf(id));
		} else {
			resultBean.setResult("1");
		}
		response.getWriter().print(JSONUtil.toString(resultBean));
	}

	/**
	 * 删除相册
	 * @param response
	 * @param userName
	 * @param galleryId
	 * @throws IOException
	 */
	@RequestMapping(value = "/delgallery")
	public void delGallery(HttpServletResponse response,
			@RequestParam(value = "username") String userName,
			@RequestParam(value = "galleryid") String galleryId)
			throws IOException {
		ResultBean resultBean = new ResultBean();
		if (StringUtil.isNullOrEmpty(galleryId)) {
			resultBean.setResult("1"); // id null
		} else {
			if (galleryDao.delGallery(galleryId, userName) > 0) {
				resultBean.setResult("0");
			} else {
				resultBean.setResult("2");
			}
		}
		response.getWriter().print(JSONUtil.toString(resultBean));
	}

	/**
	 * 删除照片
	 * @param response
	 * @param userName
	 * @param photoId
	 * @throws IOException
	 */
	@RequestMapping(value = "/delphoto")
	public void delPhoto(HttpServletResponse response,
			@RequestParam(value = "username") String userName,
			@RequestParam(value = "photoid") String photoId) throws IOException {
		ResultBean resultBean = new ResultBean();
		if (StringUtil.isNullOrEmpty(photoId)) {
			resultBean.setResult("1"); // id null
		} else {
			if (galleryDao.delPhoto(photoId, userName) > 0) {
				resultBean.setResult("0");
			} else {
				resultBean.setResult("2");
			}
		}
		response.getWriter().print(JSONUtil.toString(resultBean));
	}

	/**
	 * 上传照片
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/uploadphoto")
	public void uploadPhoto(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ServletFileUpload upload = new ServletFileUpload(
				new DiskFileItemFactory());

		upload.setHeaderEncoding("UTF-8");

		List<FileItem> items;
		String userName = "";
		String galleryId = "";
		String name = "";
		String type = "";
		String description = "";
		Photo photoBean = new Photo();
		try {
			items = upload.parseRequest(request);
			log.info("items : " + items);
			if (items == null) {
				throw new FileUploadException();
			}

			FileItem file2Upload = null;
			for (FileItem item : items) {
				log.info("field name: " + item.getFieldName());
				if ("photo_file".equals(item.getFieldName())) {
					file2Upload = item;
				} else if ("galleryid".equals(item.getFieldName())) {
					galleryId = item.getString();
				} else if ("username".equals(item.getFieldName())) {
					userName = item.getString();
				} else if ("name".equals(item.getFieldName())) {
					name = item.getString();
				} else if ("type".equals(item.getFieldName())) {
					type = item.getString();
				} else if ("descritpion".equals(item.getFieldName())) {
					description = item.getString();
				}
			}
			if (file2Upload == null) {
				throw new FileUploadException("No avatar file");
			}

			if (StringUtil.isNullOrEmpty(galleryId)) {
				photoBean.setResult("1"); // id null
			} else {
				String photoPathName = UUID.randomUUID().toString();
				// save photo file
				FileUtil.saveFile(photoPathName, file2Upload);

				long id = galleryDao.createPhoto(userName, galleryId, name,
						type, photoPathName, description);
				log.info("create photo id: " + id);
				if (id > 0) {
					photoBean = galleryDao.getPhoto(id);
					photoBean.setResult("0");
				} else {
					photoBean.setResult("3"); // create failed
				}
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
			photoBean.setResult("2"); // no file
		} catch (Exception e) {
			e.printStackTrace();
			photoBean.setResult("5"); // save file failed
		}
		response.getWriter().print(JSONUtil.toString(photoBean));
	}

	/**
	 * 设置相册信息
	 * @param response
	 * @param galleryId
	 * @param title
	 * @param photoPath
	 * @throws IOException
	 */
	@RequestMapping(value = "/setgalleryinfo")
	public void setGalleryInfo(HttpServletResponse response,
			@RequestParam(value = "galleryid") String galleryId,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "coverurl", required = false) String photoPath)
			throws IOException {
		ResultBean resultBean = new ResultBean();
		if (StringUtil.isNullOrEmpty(galleryId)) {
			resultBean.setResult("1"); // gallery id null
		} else {
			if (galleryDao.updateGallery(galleryId, title, photoPath) > 0) {
				resultBean.setResult("0");
			} else {
				resultBean.setResult("2"); // update failed
			}
		}
		response.getWriter().print(JSONUtil.toString(resultBean));
	}

	/**
	 * 设置照片信息
	 * @param response
	 * @param photoId
	 * @param type
	 * @param description
	 * @param name
	 * @throws IOException
	 */
	@RequestMapping(value = "/setphotoinfo")
	public void setPhotoInfo(
			HttpServletResponse response,
			@RequestParam(value = "photoid") String photoId,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "name", required = false) String name)
			throws IOException {
		ResultBean resultBean = new ResultBean();
		if (StringUtil.isNullOrEmpty(photoId)) {
			resultBean.setResult("1"); // photo id null
		} else {
			if (galleryDao.updatePhoto(photoId, type, description, name) > 0) {
				resultBean.setResult("0");
			} else {
				resultBean.setResult("2"); // update failed
			}
		}
		response.getWriter().print(JSONUtil.toString(resultBean));
	}
}
