package com.sego.mvc.model.dao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.SqlParameter;

import com.richitec.dao.BaseDao;
import com.richitec.util.ArrayUtil;
import com.richitec.util.StringUtil;
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
				gallery.setId((Integer)(map.get(GalleryColumn.id.name())));
				gallery.setTitle(String.valueOf(map.get(GalleryColumn.title
						.name())));
				gallery.setCover_url(String.valueOf(map
						.get(GalleryColumn.cover_url.name())));
				gallery.setOwnerid(String.valueOf(map.get(GalleryColumn.ownerid
						.name())));
				gallery.setCreatetime((Long)(map
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
				Photo photo = convertMapToPhoto(map);
			}
		}
		return gallery;
	}

	public Photo getPhoto(Long photoId) {
		String sql = "SELECT id, type, galleryid, path, description, name, ownerid, UNIX_TIMESTAMP(createtime) AS createtime "
			+ "FROM photo WHERE galleryid = ?";
		Map<String, Object> map = jdbc.queryForMap(sql, photoId);
		return convertMapToPhoto(map);
	}
	
	private Photo convertMapToPhoto(Map<String, Object> map) {
		Photo photo = new Photo();
		photo.setId((Integer)(map.get(PhotoColumn.id.name())));
		photo.setType(String.valueOf(map.get(PhotoColumn.type.name())));
		photo.setGalleryid((Integer)(map.get(PhotoColumn.galleryid
				.name())));
		photo.setPath(String.valueOf(map.get(PhotoColumn.path.name())));
		photo.setDescription(String.valueOf(map
				.get(PhotoColumn.description.name())));
		photo.setName(String.valueOf(map.get(PhotoColumn.name.name())));
		photo.setOwnerid(String.valueOf(map.get(PhotoColumn.ownerid
				.name())));
		photo.setCreatetime((Long)(map
				.get(PhotoColumn.createtime.name())));
		return photo;
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
	
	public int updateGallery(String galleryId, String title, String coverUrl) {
		StringBuffer sqlBuffer = new StringBuffer();
		ArrayList<Object> objList = new ArrayList<Object>();
		ArrayList<Integer> typeList = new ArrayList<Integer>();
		sqlBuffer.append("UPDATE gallery SET");
		if (!StringUtil.isNullOrEmpty(title)) {
			sqlBuffer.append(" title = ?,");
			objList.add(title);
			typeList.add(Types.VARCHAR);
		}
		if (!StringUtil.isNullOrEmpty(coverUrl)) {
			sqlBuffer.append(" cover_url = ?,");
			objList.add(coverUrl);
			typeList.add(Types.VARCHAR);
		}
		sqlBuffer = StringUtil.deleteLastChar(sqlBuffer, ',');
		
		sqlBuffer.append(" WHERE id = ?");
		objList.add(galleryId);
		typeList.add(Types.INTEGER);
		
		int[] types = ArrayUtil.convertIntegerListToIntArray(typeList);
		return jdbc.update(sqlBuffer.toString(), objList.toArray(), types);
	}
	
	public int updatePhoto(String photoId, String type, String description, String name) {
		StringBuffer sqlBuffer = new StringBuffer();
		ArrayList<Object> objList = new ArrayList<Object>();
		ArrayList<Integer> typeList = new ArrayList<Integer>();
		sqlBuffer.append("UPDATE photo SET");
		if (!StringUtil.isNullOrEmpty(type)) {
			sqlBuffer.append(" type = ?,");
			objList.add(type);
			typeList.add(Types.VARCHAR);
		}
		if (!StringUtil.isNullOrEmpty(description)) {
			sqlBuffer.append(" description = ?,");
			objList.add(description);
			typeList.add(Types.VARCHAR);
		}
		if (!StringUtil.isNullOrEmpty(name)) {
			sqlBuffer.append(" name = ?,");
			objList.add(name);
			typeList.add(Types.VARCHAR);
		}
		sqlBuffer = StringUtil.deleteLastChar(sqlBuffer, ',');
		
		sqlBuffer.append(" WHERE id = ?");
		objList.add(photoId);
		typeList.add(Types.INTEGER);
		
		int[] types = ArrayUtil.convertIntegerListToIntArray(typeList);
		return jdbc.update(sqlBuffer.toString(), objList.toArray(), types);
	}
}
