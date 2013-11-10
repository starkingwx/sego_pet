package com.sego.mvc.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.richitec.dao.BaseDao;
import com.richitec.util.DistanceUtil;
import com.sego.mvc.model.bean.LeaveMsg;
import com.sego.mvc.model.bean.LeaveMsgs;
import com.sego.mvc.model.bean.PetInfo;
import com.sego.mvc.model.bean.PetInfos;
import com.sego.table.LeaveMsgColumn;
import com.sego.table.LocationColum;

@Transactional
public class CommunityDao extends BaseDao {
	private static final double MAX_DISTANCE = 3000; // km

	public PetInfos getRecommendedPets(String userName) {
		String sql = "SELECT * FROM f_pets WHERE username <> ? LIMIT 0, 10";
		List<Map<String, Object>> list = jdbc.queryForList(sql, userName);
		PetInfos petInfos = new PetInfos();
		List<PetInfo> petInfoList = new ArrayList<PetInfo>();
		petInfos.setList(petInfoList);
		if (list != null) {
			for (Map<String, Object> map : list) {
				PetInfo petInfo = PetInfoDao.convertMapToPetInfo(map);
				petInfoList.add(petInfo);
			}
		}
		return petInfos;
	}

	public PetInfos getNearbyPets(double lng, double lat) {
		String sql = "SELECT * FROM f_location WHERE ABS(longitutde - ?) < 1 AND ABS(latitude - ?) < 1";
		List<Map<String, Object>> list = jdbc.queryForList(sql, lng, lat);
		StringBuffer idBuffer = new StringBuffer();
		if (list != null) {
			for (Map<String, Object> map : list) {
				String longitude = String.valueOf(map
						.get(LocationColum.longitude.name()));
				String latitude = String.valueOf(map.get(LocationColum.latitude
						.name()));
				int id = (Integer) map.get(LocationColum.id.name());
				double lng2 = Double.parseDouble(longitude);
				double lat2 = Double.parseDouble(latitude);
				if (DistanceUtil.getDistance(lng, lat, lng2, lat2) < MAX_DISTANCE) {
					idBuffer.append(id).append(',');
				}
			}
			if (idBuffer.toString().endsWith(",")) {
				idBuffer.deleteCharAt(idBuffer.length() - 1);
			}
		}
		PetInfos petInfos = new PetInfos();
		List<PetInfo> petInfoList = new ArrayList<PetInfo>();
		petInfos.setList(petInfoList);
		if (idBuffer.length() > 0) {
			sql = "SELECT * FROM f_pets WHERE petid IN (" + idBuffer.toString()
					+ ")";
			List<Map<String, Object>> petList = jdbc.queryForList(sql);
			if (petList != null) {
				for (Map<String, Object> map : petList) {
					PetInfo petInfo = PetInfoDao.convertMapToPetInfo(map);
					petInfoList.add(petInfo);
				}
			}
		}

		return petInfos;
	}

	public PetInfos getConcernedPets(String userName) {
		String sql = "SELECT p.* FROM f_pets AS p JOIN f_guanzhu AS g ON p.petid = g.petidWHERE g.loginid = ?";
		List<Map<String, Object>> petList = jdbc.queryForList(sql, userName);
		PetInfos petInfos = PetInfoDao.convertListToPetInfos(petList);
		return petInfos;
	}

	public int concernPet(String userName, String petId) {
		String sql = "SELECT count(id) FROM f_guanzhu WHERE loginid = ? AND petid = ?";
		int count = jdbc.queryForInt(sql, userName, petId);
		if (count > 0) {
			return -2; // already concerned
		} else {
			String sql1 = "INSERT INTO f_guanzhu (loginid, petid) VALUES(?,?)";
			return jdbc.update(sql1, userName, petId);
		}
	}

	public int unconcernPet(String userName, String petId) {
		String sql = "DELETE FROM f_guanzhu WHERE loginid = ? AND petid = ?";
		return jdbc.update(sql, userName, petId);
	}

	public int leaveMsg(String userName, String petId, String content,
			String parentId) {
		String sql = "INSERT INTO f_liuyan (author, content, petid, parentid) VALUES(?, ?, ?, ?)";
		return jdbc.update(sql, userName, content, petId, parentId);
	}

	public int replyMsg(String userName, String content, String parentId) {
		String petId = getPetIdByMsgId(parentId);
		return leaveMsg(userName, petId, content, parentId);
	}

	public boolean isMsgExist(String msgId) {
		String sql = "SELECT count(id) FROM f_liuyan WHERE id = ?";
		int count = jdbc.queryForInt(sql, msgId);
		return count > 0;
	}

	public String getPetIdByMsgId(String msgId) {
		String sql = "SELECT petid FROM f_liuyan WHERE id = ?";
		return jdbc.queryForObject(sql, String.class, msgId);
	}

	public int delMsg(String msgId) {
		String sql = "DELETE FROM f_liuyan WHERE id = ?";
		return jdbc.update(sql, msgId);
	}

	public LeaveMsgs getLeaveMsgs(String petId) {
		String sql = "SELECT l.id as id, l.author as author, l.content as content, l.petid as petid, l.parentid as parentid, UNIX_TIMESTAMP(l.date) AS _date, "
				+ " p.nickname as leaver_nickname, p.sex as leaver_sex, p.avatar as leaver_avatar "
				+ " FROM f_liuyan AS l, f_pets AS p WHERE l.petid = ? AND l.author = p.ownerid";
		List<Map<String, Object>> list = jdbc.queryForList(sql, petId);
		LeaveMsgs leaveMsgs = new LeaveMsgs();
		List<LeaveMsg> msgList = new ArrayList<LeaveMsg>();
		leaveMsgs.setList(msgList);
		if (list != null) {
			for (Map<String, Object> map : list) {
				msgList.add(convertMapToLeaveMsg(map));
			}
		}
		return leaveMsgs;
	}

	private LeaveMsg convertMapToLeaveMsg(Map<String, Object> map) {
		LeaveMsg msg = new LeaveMsg();
		msg.setMsgid(String.valueOf(map.get(LeaveMsgColumn.id.name())));
		msg.setAuthor(String.valueOf(map.get(LeaveMsgColumn.author.name())));
		msg.setContent(String.valueOf(map.get(LeaveMsgColumn.content.name())));
		msg.setPetid(String.valueOf(map.get(LeaveMsgColumn.petid.name())));
		msg.setParentid(String.valueOf(map.get(LeaveMsgColumn.parentid.name())));
		msg.setLeave_timestamp(String.valueOf(map.get("_date")));
		msg.setLeaver_nickname(String.valueOf(map.get("leaver_nickname")));
		msg.setLeaver_sex(String.valueOf(map.get("leaver_sex")));
		msg.setLeaver_avatar(String.valueOf(map.get("leaver_avatar")));
		return msg;
	}

	public LeaveMsg getLeaveMsgDetail(String msgId) {
		String sql = "SELECT l.id as id, l.author as author, l.content as content, l.petid as petid, l.parentid as parentid, UNIX_TIMESTAMP(l.date) AS _date, "
				+ " p.nickname as leaver_nickname, p.sex as leaver_sex, p.avatar as leaver_avatar "
				+ " FROM f_liuyan AS l, f_pets AS p WHERE l.id = ? AND l.author = p.ownerid";
		Map<String, Object> map = jdbc.queryForMap(sql, msgId);
		return convertMapToLeaveMsg(map);
	}
	
	public PetInfos getPetBlackList(String userName) {
		String sql = "SELECT * FROM f_pets WHERE petid IN " +
				"( SELECT blackpetid FROM f_blacklist WHERE loginid = ?) ";
		List<Map<String, Object>> list = jdbc.queryForList(sql, userName);
		return PetInfoDao.convertListToPetInfos(list);
	}
	
	public boolean isPetInBlackList(String petId, String userName) {
		String sql = "SELECT count(id) FROM f_blacklist WHERE loginid = ? AND blackpetid = ?";
		int count = jdbc.queryForInt(sql, userName, petId);
		return count > 0;
	}
	
	public int addPetToBlackList(String petId, String userName) {
		String sql = "INSERT INTO f_blacklist (loginid, blackpetid) VALUES(?,?)";
		return jdbc.update(sql, userName, petId);
	}
	
	public int delPetFromBlackList(String petId, String userName) {
		String sql = "DELETE FROM f_blacklist WHERE loginid = ? AND blackpetid = ?";
		return jdbc.update(sql, userName, petId);
	}
}
