package com.sego.mvc.model.dao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.transaction.annotation.Transactional;

import com.richitec.dao.BaseDao;
import com.sego.mvc.model.bean.Galleries;
import com.sego.mvc.model.bean.Gallery;
import com.sego.mvc.model.bean.Photo;
import com.sego.table.GalleryColumn;
import com.sego.table.PhotoColumn;

public class GalleryDao extends BaseDao {

	public Galleries getGalleries(String userName) {
		String sql = "SELECT id, title, cover_url, ownerid, UNIX_TIMESTAMP(createtime) AS createtime FROM gallery WHERE ownerid = ?";
		List<Map<String, Object>> list = jdbc.queryForList(sql, userName);
		Galleries galleries = new Galleries();
		List<Gallery> galleryList = new ArrayList<Gallery>();
		galleries.setList(galleryList);
		if (list != null) {
			for (Map<String, Object> map : list) {
				Gallery gallery = new Gallery();
				gallery.setId(String.valueOf(map.get(GalleryColumn.id.name())));
				gallery.setTitle(String.valueOf(map.get(GalleryColumn.title
						.name())));
				gallery.setCover_url(String.valueOf(map
						.get(GalleryColumn.cover_url.name())));
				gallery.setOwnerid(String.valueOf(map.get(GalleryColumn.ownerid
						.name())));
				gallery.setCreatetime(String.valueOf(map
						.get(GalleryColumn.createtime.name())));
				galleryList.add(gallery);
			}
		}
		return galleries;
	}

	public Gallery getGallery(String galleryId) {
		Gallery gallery = new Gallery();
		String sql = "SELECT id, type, galleryid, path, description, name, ownerid, UNIX_TIMESTAMP(createtime) AS createtime "
				+ "FROM photo WHERE galleryid = ?";
		List<Map<String, Object>> list = jdbc.queryForList(sql, galleryId);
		List<Photo> photoList = new ArrayList<Photo>();
		gallery.setPhotos(photoList);
		if (list != null) {
			for (Map<String, Object> map : list) {
				Photo photo = new Photo();
				photo.setId(String.valueOf(map.get(PhotoColumn.id.name())));
				photo.setType(String.valueOf(map.get(PhotoColumn.type.name())));
				photo.setGalleryid(String.valueOf(map.get(PhotoColumn.galleryid
						.name())));
				photo.setPath(String.valueOf(map.get(PhotoColumn.path.name())));
				photo.setDescription(String.valueOf(map
						.get(PhotoColumn.description.name())));
				photo.setName(String.valueOf(map.get(PhotoColumn.name.name())));
				photo.setOwnerid(String.valueOf(map.get(PhotoColumn.ownerid
						.name())));
				photo.setCreatetime(String.valueOf(map
						.get(PhotoColumn.createtime.name())));
			}
		}
		return gallery;
	}

	/**
	 * 
	 * @param userName
	 * @param title
	 * @return id
	 */
	public long createGallery(String userName, String title) {
		String sql = "INSERT INTO gallery (title, ownerid) VALUES(?,?) ";
		SqlParameter[] params = new SqlParameter[] {
				new SqlParameter(Types.VARCHAR),
				new SqlParameter(Types.VARCHAR) };
		String[] keys = new String[] { GalleryColumn.id.name() };
		Object[] values = new Object[] { title, userName };
		long id = insertAndReturnLastId(sql, params, values, keys);
		return id;
	}

	/**
	 * 
	 * @param userName
	 * @param galleryId
	 * @param name
	 * @param type
	 * @param path
	 * @param description
	 * @return id
	 */
	public long createPhoto(String userName, String galleryId, String name,
			String type, String path, String description) {
		String sql = "INSERT INTO photo (type, galleryid, path, description, name, ownerid) "
				+ "VALUES(?,?,?,?,?,?)";
		SqlParameter[] params = new SqlParameter[] {
				new SqlParameter(Types.VARCHAR),
				new SqlParameter(Types.INTEGER),
				new SqlParameter(Types.VARCHAR),
				new SqlParameter(Types.VARCHAR),
				new SqlParameter(Types.VARCHAR),
				new SqlParameter(Types.VARCHAR) };
		String[] keys = new String[] {PhotoColumn.id.name()};
		Object[] values = new Object[] {type, galleryId, path, description,
				 name, userName};
		long id = insertAndReturnLastId(sql, params, values, keys);
		return id;
	}

	public int delGallery(String galleryId, String userName) {
		String sql = "DELETE FROM gallery WHERE id = ? AND ownerid = ? ";
		return jdbc.update(sql, galleryId, userName);
	}

	public int delPhoto(String photoId, String userName) {
		String sql = "DELETE FROM photo WHERE id = ? AND ownerid = ? ";
		return jdbc.update(sql, photoId, userName);
	}
}
