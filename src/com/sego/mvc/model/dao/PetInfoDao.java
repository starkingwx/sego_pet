package com.sego.mvc.model.dao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.SqlParameter;

import com.richitec.dao.BaseDao;
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
	 * @param age
	 * @param height
	 * @param weight
	 * @param district
	 * @param placeOftenGo
	 * @return petid - last insert id
	 */
	public long createPetInfo(String userName, String nickname, String sex,
			String breed, String age, String height, String weight,
			String district, String placeOftenGo) {
		String sql = "INSERT INTO f_pets (nickname, sex, weight, ownerid, breed, age, height, district, placeoftengo) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

		SqlParameter[] params = new SqlParameter[9];
		for (int i = 0; i < 9 ; i++) {
			params[i] = new SqlParameter(Types.VARCHAR);
		}
		String[] keys = new String[] {PetInfoColumn.petid.name()};
		Object[] values = new Object[]{nickname, sex, weight, userName, breed,
				age, height, district, placeOftenGo};
		
		long id = insertAndReturnLastId(sql, params, values, keys);
		return id;
	}

	public boolean hasPetInfo(String userName) {
		String sql = "SELECT count(petid) FROM f_pets WHERE ownerid = ?";
		int count = jdbc.queryForInt(sql, userName);
		return count > 0;
	}

	public long createPetAvatarInfo(String avatarFileName, String userName) {
		String sql = "INSERT INTO f_pets (avatar, ownerid) VALUES(?,?)";
		SqlParameter[] params = new SqlParameter[]{new SqlParameter(Types.VARCHAR), new SqlParameter(Types.VARCHAR)};
		String[] keys = new String[] {PetInfoColumn.petid.name()}; 
		Object[] values = new Object[] {avatarFileName, userName};
		long id = insertAndReturnLastId(sql, params, values, keys);
		return id;
	}
	
	/**
	 * 
	 * @param petId
	 * @param nickname
	 * @param sex
	 * @param breed
	 * @param age
	 * @param height
	 * @param weight
	 * @param district
	 * @param placeOftenGo
	 * @return update count
	 */
	public int updatePetInfo(String petId, String nickname, String sex,
			String breed, String age, String height, String weight,
			String district, String placeOftenGo) {
		String sql = "UPDATE f_pets SET nickname = ?, sex = ?, breed = ?, age = ?, height = ?, weight = ?, district = ?, placeoftengo = ? WHERE petid = ?";
		return jdbc.update(sql, nickname, sex, breed, age, height, weight,
				district, placeOftenGo, petId);
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

	/**
	 * 
	 * @param petId
	 * @return
	 */
	public PetInfo getPetDetail(String petId) {
		String sql = "SELECT * FROM f_pets WHERE petid = ?";
		Map<String, Object> map = jdbc.queryForMap(sql, petId);
		return convertMapToPetInfo(map);
	}

	public static PetInfo convertMapToPetInfo(Map<String, Object> map) {
		PetInfo petInfo = new PetInfo();
		petInfo.setPetid((Integer)(map.get(PetInfoColumn.petid.name())));
		petInfo.setNickname((String) (map.get(PetInfoColumn.nickname
				.name())));
		petInfo.setSex((Integer) (map.get(PetInfoColumn.sex.name())));
		petInfo.setWeight((Float) map.get(PetInfoColumn.weight.name()));
		petInfo.setChuanganid((String) (map.get(PetInfoColumn.chuanganid
				.name())));
		petInfo.setOwnerid((String) (map.get(PetInfoColumn.ownerid.name())));
		petInfo.setAvatar((String) (map.get(PetInfoColumn.avatar.name())));
		petInfo.setBreed((Integer) map.get(PetInfoColumn.breed.name()));
		petInfo.setAge((Integer) (map.get(PetInfoColumn.age.name())));
		petInfo.setHeight((Float) (map.get(PetInfoColumn.height.name())));
		petInfo.setDistrict((String) (map.get(PetInfoColumn.district
				.name())));
		petInfo.setPlaceoftengo((String) (map
				.get(PetInfoColumn.placeoftengo.name())));
		petInfo.setDeviceid((String) (map.get(PetInfoColumn.deviceId
				.name())));
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
