package com.sego.mvc.model.dao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.SqlParameter;

import com.richitec.dao.BaseDao;
import com.richitec.util.StringUtil;
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
		log.info(String
				.format("createPetInfo - userName: %s, nickname: %s, sex: %s, breed: %s, age: %s, height: %s, weight: %s, district: %s, place: %s",
						userName, nickname, sex, breed, age, height, weight,
						district, placeOftenGo));

		String[] keys = new String[] { PetInfoColumn.petid.name() };

		List<Object> valueList = new ArrayList<Object>();
		List<SqlParameter> paramList = new ArrayList<SqlParameter>();
		StringBuffer sqlBuffer = new StringBuffer();
		StringBuffer valueBuffer = new StringBuffer();
		sqlBuffer.append("INSERT INTO f_pets (");
		if (!StringUtil.isNullOrEmpty(nickname)) {
			sqlBuffer.append(" nickname,");
			valueBuffer.append("?,");
			valueList.add(nickname);
			paramList.add(new SqlParameter(Types.VARCHAR));
		}
		if (!StringUtil.isNullOrEmpty(sex)) {
			sqlBuffer.append(" sex,");
			valueBuffer.append("?,");
			valueList.add(sex);
			paramList.add(new SqlParameter(Types.TINYINT));
		}
		if (!StringUtil.isNullOrEmpty(weight)) {
			sqlBuffer.append(" weight,");
			valueBuffer.append("?,");
			valueList.add(weight);
			paramList.add(new SqlParameter(Types.FLOAT));
		}
		if (!StringUtil.isNullOrEmpty(userName)) {
			sqlBuffer.append(" ownerid,");
			valueBuffer.append("?,");
			valueList.add(userName);
			paramList.add(new SqlParameter(Types.VARCHAR));
		}
		if (!StringUtil.isNullOrEmpty(breed)) {
			sqlBuffer.append(" breed,");
			valueBuffer.append("?,");
			paramList.add(new SqlParameter(Types.TINYINT));
		}
		if (!StringUtil.isNullOrEmpty(age)) {
			sqlBuffer.append(" age,");
			valueBuffer.append("?,");
			valueList.add(age);
			paramList.add(new SqlParameter(Types.INTEGER));
		}
		if (!StringUtil.isNullOrEmpty(height)) {
			sqlBuffer.append(" height,");
			valueBuffer.append("?,");
			valueList.add(height);
			paramList.add(new SqlParameter(Types.FLOAT));
		}
		if (!StringUtil.isNullOrEmpty(placeOftenGo)) {
			sqlBuffer.append(" district,");
			valueBuffer.append("?,");
			valueList.add(placeOftenGo);
			paramList.add(new SqlParameter(Types.VARCHAR));
		}
		if (!StringUtil.isNullOrEmpty(district)) {
			sqlBuffer.append(" placeoftengo,");
			valueBuffer.append("?,");
			valueList.add(district);
			paramList.add(new SqlParameter(Types.VARCHAR));
		}
		sqlBuffer = StringUtil.deleteLastChar(sqlBuffer, ',');
		valueBuffer = StringUtil.deleteLastChar(valueBuffer, ',');

		sqlBuffer.append(") VALUES(").append(valueBuffer).append(")");

		SqlParameter[] params = new SqlParameter[paramList.size()];
		params = paramList.toArray(params);

		log.info("sql: " + sqlBuffer.toString());
		for (int i = 0; i < paramList.size(); i++) {
			SqlParameter param = paramList.get(i);
			log.info("param type: " + param.getSqlType() + " value: "
					+ valueList.get(i));
		}
		long id = insertAndReturnLastId(sqlBuffer.toString(), params,
				valueList.toArray(), keys);
		return id;
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
		log.info(String
				.format("updatePetInfo - nickname: %s, sex: %s, breed: %s, age: %s, height: %s, weight: %s, district: %s, place: %s",
						 nickname, sex, breed, age, height, weight,
						district, placeOftenGo));
		
		StringBuffer sqlBuffer = new StringBuffer();
		ArrayList<Object> objList = new ArrayList<Object>();
		ArrayList<Integer> typeList = new ArrayList<Integer>();
		sqlBuffer.append("UPDATE f_pets SET");
		if (!StringUtil.isNullOrEmpty(nickname)) {
			sqlBuffer.append(" nickname = ?,");
			objList.add(nickname);
			typeList.add(Types.VARCHAR);
		}
		if (!StringUtil.isNullOrEmpty(sex)) {
			sqlBuffer.append(" sex = ?,");
			objList.add(sex);
			typeList.add(Types.TINYINT);
		}
		if (!StringUtil.isNullOrEmpty(breed)) {
			sqlBuffer.append(" breed = ?,");
			objList.add(breed);
			typeList.add(Types.TINYINT);
		}
		if (!StringUtil.isNullOrEmpty(age)) {
			sqlBuffer.append(" age = ?,");
			objList.add(age);
			typeList.add(Types.INTEGER);
		}
		if (!StringUtil.isNullOrEmpty(height)) {
			sqlBuffer.append(" height = ?,");
			objList.add(height);
			typeList.add(Types.FLOAT);
		}
		if (!StringUtil.isNullOrEmpty(weight)) {
			sqlBuffer.append(" weight = ?,");
			objList.add(weight);
			typeList.add(Types.FLOAT);
		}
		if (!StringUtil.isNullOrEmpty(district)) {
			sqlBuffer.append(" district = ?,");
			objList.add(district);
			typeList.add(Types.VARCHAR);
		}
		if (!StringUtil.isNullOrEmpty(placeOftenGo)) {
			sqlBuffer.append(" placeoftengo = ?,");
			objList.add(placeOftenGo);
			typeList.add(Types.VARCHAR);
		}
		sqlBuffer = StringUtil.deleteLastChar(sqlBuffer, ',');

		sqlBuffer.append(" WHERE petid = ?");
		objList.add(petId);
		typeList.add(Types.INTEGER);

		Integer[] tmp = new Integer[0];
		tmp = typeList.toArray(tmp);
		int[] types = new int[tmp.length];
		for (int i = 0; i < tmp.length; i++) {
			types[i] = tmp[i];
		}
		log.info("sql: " + sqlBuffer.toString());
		for (int i = 0; i < objList.size(); i++) {
			log.info("param type: " + types[i] + " value: "
					+ objList.get(i));
		}
		
		return jdbc.update(sqlBuffer.toString(), objList.toArray(), types);
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
		petInfo.setPetid((Integer) (map.get(PetInfoColumn.petid.name())));
		petInfo.setNickname((String) (map.get(PetInfoColumn.nickname.name())));
		petInfo.setSex((Integer) (map.get(PetInfoColumn.sex.name())));
		petInfo.setWeight((Float) map.get(PetInfoColumn.weight.name()));
		petInfo.setChuanganid((String) (map.get(PetInfoColumn.chuanganid.name())));
		petInfo.setOwnerid((String) (map.get(PetInfoColumn.ownerid.name())));
		petInfo.setAvatar((String) (map.get(PetInfoColumn.avatar.name())));
		petInfo.setBreed((Integer) map.get(PetInfoColumn.breed.name()));
		petInfo.setAge((Integer) (map.get(PetInfoColumn.age.name())));
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
