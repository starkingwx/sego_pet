package com.sego.mvc.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.imeeting.constants.PetInfoColumn;
import com.sego.mvc.model.bean.PetInfo;
import com.sego.mvc.model.bean.PetInfos;

@Transactional
public class PetInfoDao {
	private static Log log = LogFactory.getLog(PetInfoDao.class);

	private JdbcTemplate jdbc;

	public void setDataSource(DataSource ds) {
		jdbc = new JdbcTemplate(ds);
	}

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
	public int createPetInfo(String userName, String nickname, String sex,
			String breed, String age, String height, String weight,
			String district, String placeOftenGo) {
		String sql = "INSERT INTO f_pets (nickname, sex, weight, ownerid, breed, age, height, district, placeoftengo) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

		int update = jdbc.update(sql, nickname, sex, weight, userName, breed,
				age, height, district, placeOftenGo);
		int id = -1;
		if (update > 0) {
			sql = "SELECT LAST_INSERT_ID()";
			id = jdbc.queryForInt(sql);
		}
		return id;
	}

	public boolean hasPetInfo(String userName) {
		String sql = "SELECT count(petid) FROM f_pets WHERE ownerid = ?";
		int count = jdbc.queryForInt(sql, userName);
		return count > 0;
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

	public PetInfos getPetInfos(String userName) {
		String sql = "SELECT * FROM f_pets WHERE ownerid = ?";
		List<Map<String, Object>> list = jdbc.queryForList(sql, userName);
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

	private PetInfo convertMapToPetInfo(Map<String, Object> map) {
		PetInfo petInfo = new PetInfo();
		petInfo.setPetid(String.valueOf(map.get(PetInfoColumn.petid.name())));
		petInfo.setNickname(String.valueOf(map.get(PetInfoColumn.nickname
				.name())));
		petInfo.setSex(String.valueOf(map.get(PetInfoColumn.sex.name())));
		petInfo.setWeight(String.valueOf(map.get(PetInfoColumn.weight.name())));
		petInfo.setChuanganid(String.valueOf(map.get(PetInfoColumn.chuanganid
				.name())));
		petInfo.setOwnerid(String.valueOf(map.get(PetInfoColumn.ownerid.name())));
		petInfo.setAvatar(String.valueOf(map.get(PetInfoColumn.avatar.name())));
		petInfo.setBreed(String.valueOf(map.get(PetInfoColumn.breed.name())));
		petInfo.setAge(String.valueOf(map.get(PetInfoColumn.age.name())));
		petInfo.setHeight(String.valueOf(map.get(PetInfoColumn.height.name())));
		petInfo.setDistrict(String.valueOf(map.get(PetInfoColumn.district
				.name())));
		petInfo.setPlaceoftengo(String.valueOf(map
				.get(PetInfoColumn.placeoftengo.name())));
		petInfo.setDeviceid(String.valueOf(map.get(PetInfoColumn.deviceId
				.name())));
		petInfo.setScore(String.valueOf(map.get(PetInfoColumn.score.name())));
		return petInfo;
	}
}
