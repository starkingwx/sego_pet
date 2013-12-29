package com.sego.mvc.model.dao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.SqlParameter;

import com.imeeting.framework.ContextLoader;
import com.richitec.dao.BaseDao;
import com.sego.mvc.model.bean.Galleries;
import com.sego.mvc.model.bean.PetInfo;
import com.sego.mvc.model.bean.PetInfos;
import com.sego.table.PetInfoColumn;

public class PetInfoDao extends BaseDao {
	private static Log log = LogFactory.getLog(PetInfoDao.class);

	/**
	 * 
	 * @param userName
	 * @param nickname
	 * @param sex
	 * @param breed
	 * @param birthday
	 * @param height
	 * @param weight
	 * @param district
	 * @param placeOftenGo
	 * @return petid - last insert id
	 */
	public long createPetInfo(String userName, String nickname, String sex,
			String breed, String birthday, String height, String weight,
			String district, String placeOftenGo) {
		log.info(String
				.format("createPetInfo - userName: %s, nickname: %s, sex: %s, breed: %s, birthday: %s, height: %s, weight: %s, district: %s, place: %s",
						userName, nickname, sex, breed, birthday, height, weight,
						district, placeOftenGo));
		
		TableField[] values = new TableField[]{new TableField("nickname", nickname, Types.VARCHAR), 
				new TableField("sex", sex, Types.TINYINT), new TableField("breed", breed, Types.TINYINT),
				new TableField("birthday", birthday, Types.BIGINT), new TableField("height", height, Types.FLOAT),
				new TableField("weight", weight, Types.FLOAT), new TableField("district", district, Types.VARCHAR),
				new TableField("placeoftengo", placeOftenGo, Types.VARCHAR), new TableField("ownerid", userName, Types.VARCHAR)};
		String[] keys = new String[] { PetInfoColumn.petid.name() };
		return insert("f_pets", values, keys);
	}

	public boolean hasPetInfo(String userName) {
		String sql = "SELECT count(petid) FROM f_pets WHERE ownerid = ?";
		int count = jdbc.queryForInt(sql, userName);
		return count > 0;
	}

	public long createPetAvatarInfo(String avatarFileName, String userName) {
		String sql = "INSERT INTO f_pets (avatar, ownerid) VALUES(?,?)";
		SqlParameter[] params = new SqlParameter[] {
				new SqlParameter(Types.VARCHAR),
				new SqlParameter(Types.VARCHAR) };
		String[] keys = new String[] { PetInfoColumn.petid.name() };
		Object[] values = new Object[] { avatarFileName, userName };
		long id = insertAndReturnLastId(sql, params, values, keys);
		return id;
	}

	/**
	 * 
	 * @param petId
	 * @param nickname
	 * @param sex
	 * @param breed
	 * @param birthday
	 * @param height
	 * @param weight
	 * @param district
	 * @param placeOftenGo
	 * @return update count
	 */
	public int updatePetInfo(String petId, String nickname, String sex,
			String breed, String birthday, String height, String weight,
			String district, String placeOftenGo, String deviceId) {
		log.info(String
				.format("updatePetInfo - nickname: %s, sex: %s, breed: %s, birthday: %s, height: %s, weight: %s, district: %s, place: %s",
						 nickname, sex, breed, birthday, height, weight,
						district, placeOftenGo));
		TableField[] updateParams = new TableField[]{new TableField("nickname", nickname, Types.VARCHAR), 
				new TableField("sex", sex, Types.TINYINT), new TableField("breed", breed, Types.TINYINT),
				new TableField("birthday", birthday, Types.BIGINT), new TableField("height", height, Types.FLOAT),
				new TableField("weight", weight, Types.FLOAT), new TableField("district", district, Types.VARCHAR),
				new TableField("placeoftengo", placeOftenGo, Types.VARCHAR), new TableField("deviceId", deviceId, Types.VARCHAR)};
		String selection = "WHERE petid = ?";
		TableField[] selectionArgs = new TableField[]{new TableField("petid", petId, Types.INTEGER)};
		return update("f_pets", updateParams, selection, selectionArgs);
	}

	public int updatePetAvatar(String petId, String avatarFileName) {
		String sql = "UPDATE f_pets SET avatar = ? WHERE petid = ?";
		return jdbc.update(sql, avatarFileName, petId);
	}

	public String getPetAvatar(String petId) {
		String sql = "SELECT avatar FROM f_pets WHERE petid = ?";
		return jdbc.queryForObject(sql, String.class, petId);
	}

	public PetInfos getPetInfos(String userName) {
		String sql = "SELECT * FROM f_pets WHERE ownerid = ?";
		List<Map<String, Object>> list = jdbc.queryForList(sql, userName);
		PetInfos petInfos = convertListToPetInfos(list);
		return petInfos;
	}

	public PetInfos searchPetInfos(String phone) {
		String sql = "SELECT * FROM f_pets WHERE ownerid LIKE ?";
		List<Map<String, Object>> list = jdbc.queryForList(sql, "%" + phone + "%");
		PetInfos petInfos = new PetInfos();
		List<PetInfo> petInfoList = new ArrayList<PetInfo>();
		petInfos.setList(petInfoList);
		if (list != null) {
			int size = list.size();
			for (Map<String, Object> map : list) {
				PetInfo petInfo = convertMapToPetInfo(map);
				if (size == 1) {
					Galleries galleries = ContextLoader.getGalleryDao().getRecentGalleriesByPetId(String.valueOf(petInfo.getPetid()), 3);
					petInfo.setGalleries(galleries.getList());
				}
				petInfoList.add(petInfo);
			}
		}
		return petInfos;
	}
	
	/**
	 * 
	 * @param petId
	 * @return
	 */
	public PetInfo getPetDetail(String petId) {
		String sql = "SELECT * FROM f_pets WHERE petid = ?";
		Map<String, Object> map = jdbc.queryForMap(sql, petId);
		PetInfo petInfo = convertMapToPetInfo(map);
		Galleries galleries = ContextLoader.getGalleryDao().getRecentGalleriesByPetId(petId, 3);
		petInfo.setGalleries(galleries.getList());
		return petInfo;
	}

	public static PetInfo convertMapToPetInfo(Map<String, Object> map) {
		PetInfo petInfo = new PetInfo();
		petInfo.setPetid((Integer) (map.get(PetInfoColumn.petid.name())));
		petInfo.setNickname((String) (map.get(PetInfoColumn.nickname.name())));
		petInfo.setSex((Integer) (map.get(PetInfoColumn.sex.name())));
		petInfo.setWeight((Float) map.get(PetInfoColumn.weight.name()));
		petInfo.setChuanganid((String) (map.get(PetInfoColumn.chuanganid.name())));
		petInfo.setOwnerid((String) (map.get(PetInfoColumn.ownerid.name())));
		petInfo.setAvatar((String) (map.get(PetInfoColumn.avatar.name())));
		petInfo.setBreed((Integer) map.get(PetInfoColumn.breed.name()));
		petInfo.setBirthday((Long) (map.get(PetInfoColumn.birthday.name())));
		petInfo.setHeight((Float) (map.get(PetInfoColumn.height.name())));
		petInfo.setDistrict((String) (map.get(PetInfoColumn.district.name())));
		petInfo.setPlaceoftengo((String) (map.get(PetInfoColumn.placeoftengo
				.name())));
		petInfo.setDeviceid((String) (map.get(PetInfoColumn.deviceId.name())));
		petInfo.setScore((Integer) (map.get(PetInfoColumn.score.name())));
		return petInfo;
	}

	public static PetInfos convertListToPetInfos(List<Map<String, Object>> list) {
		PetInfos petInfos = new PetInfos();
		List<PetInfo> petInfoList = new ArrayList<PetInfo>();
		petInfos.setList(petInfoList);
		if (list != null) {
			for (Map<String, Object> map : list) {
				PetInfo petInfo = convertMapToPetInfo(map);
				petInfoList.add(petInfo);
			}
		}
		return petInfos;
	}

	public boolean isPetExist(String petId) {
		String sql = "SELECT COUNT(petid) FROM f_pets WHERE petid = ?";
		if (jdbc.queryForInt(sql, petId) > 0) {
			return true;
		} else {
			return false;
		}
	}
}
