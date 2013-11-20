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
				Gallery gallery = convertMapToGallery(map);
				galleryList.add(gallery);
			}
		}
		return galleries;
	}
	
	public Galleries getGalleriesByPetId(String petId) {
		String sql = "SELECT g.id as id, g.title as title, g.cover_url as cover_url, g.ownerid as ownerid, UNIX_TIMESTAMP(g.createtime) AS createtime "
				+ "FROM gallery AS g JOIN f_pets AS p ON p.ownerid = g.ownerid WHERE p.petid = ?";
		List<Map<String, Object>> list = jdbc.queryForList(sql, petId);
		Galleries galleries = new Galleries();
		List<Gallery> galleryList = new ArrayList<Gallery>();
		galleries.setList(galleryList);
		if (list != null) {
			for (Map<String, Object> map : list) {
				Gallery gallery = convertMapToGallery(map);
				galleryList.add(gallery);
			}
		}
		return galleries;
	}

	private Gallery convertMapToGallery(Map<String, Object> map) {
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
		return gallery;
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
			+ "FROM photo WHERE id = ?";
		Map<String, Object> map = jdbc.queryForMap(sql, String.valueOf(photoId));
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
		TableField[] updateParams = new TableField[]{new TableField("title", title, Types.VARCHAR), new TableField("cover_url", coverUrl, Types.VARCHAR)};
		String selection = "WHERE id = ?";
		TableField[] selectionArgs = new TableField[]{new TableField("id", galleryId, Types.INTEGER)};
		return update("gallery", updateParams, selection, selectionArgs);
	}
	
	public int updatePhoto(String photoId, String type, String description, String name) {
		TableField[] updateParams = new TableField[]{new TableField("type", type, Types.VARCHAR), 
				new TableField("description", description, Types.VARCHAR), new TableField("name", name, Types.VARCHAR)};
		String selection = "WHERE id = ?";
		TableField[] selectionArgs = new TableField[]{new TableField("id", photoId, Types.INTEGER)};
		return update("photo", updateParams, selection, selectionArgs);
	}
}
