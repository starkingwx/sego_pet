package com.sego.mv.model.dao;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class PetInfoDao {
	private static Log log = LogFactory.getLog(PetInfoDao.class);

	private JdbcTemplate jdbc;

	public void setDataSource(DataSource ds) {
		jdbc = new JdbcTemplate(ds);
	}

	public int createPetInfo(String userName, String nickname, String sex,
			String breed, String age, String height, String weight,
			String district, String placeOftenGo) {
		String sql = "INSERT INTO f_pets (nickname, sex, weight, ownerid, breed, age, height, district, placeoftengo) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
		return jdbc.update(sql, nickname, sex, weight, userName, breed, age,
				height, district, placeOftenGo);
	}

	public boolean hasPetInfo(String userName) {
		String sql = "SELECT count(petid) FROM f_pets WHERE ownerid = ?";
		int count = jdbc.queryForInt(sql, userName);
		return count > 0;
	}

	public int updatePetInfo(String petId, String nickname, String sex,
			String breed, String age, String height, String weight,
			String district, String placeOftenGo) {
		String sql = "UPDATE f_pets SET nickname = ?, sex = ?, breed = ?, age = ?, height = ?, weight = ?, district = ?, placeoftengo = ? WHERE petid = ?";
		return jdbc.update(sql, nickname, sex, breed, age, height, weight, district, placeOftenGo, petId);
	}
}
