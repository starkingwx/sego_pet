package com.sego.mvc.model.dao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import com.imeeting.framework.ContextLoader;
import com.richitec.dao.BaseDao;
import com.richitec.dao.BaseDao.TableField;
import com.richitec.util.DistanceUtil;
import com.richitec.util.StringUtil;
import com.sego.mvc.model.DeviceManager;
import com.sego.mvc.model.bean.LeaveMsg;
import com.sego.mvc.model.bean.LeaveMsgs;
import com.sego.mvc.model.bean.PetInfo;
import com.sego.mvc.model.bean.PetInfos;
import com.sego.mvc.model.bean.device.TrackSdata;
import com.sego.table.LeaveMsgColumn;
import com.sego.table.LocationColum;
import com.sego.table.PetInfoColumn;

@Transactional
public class CommunityDao extends BaseDao {
	private static Log log = LogFactory.getLog(CommunityDao.class);
	
	private static final long HOUR = 60 * 60 * 1000; // ms
	private static final double MAX_DISTANCE = 10000; // m
	public int getTotalPetsWithPhoto() {
		String sql = "SELECT count(p.petid) FROM f_pets AS p WHERE p.ownerid IN (SELECT DISTINCT(ownerid) FROM photo GROUP BY ownerid)";
		return jdbc.queryForInt(sql);
	}
	
	public PetInfos getRecommendedPets(String userName) {
		int total = getTotalPetsWithPhoto();
		int start = 0;
		final int pageSize = 8;
		if (total > pageSize) {
			int diff = total - pageSize + 1;
			Random random = new Random();
			start = random.nextInt(diff);
		}
		log.info("getRecommendedPets start: " + start);
		
		String sql = "SELECT p.* FROM f_pets AS p WHERE p.ownerid IN (SELECT DISTINCT(ownerid) FROM photo GROUP BY ownerid) LIMIT ?,?";
		List<Map<String, Object>> list = jdbc.queryForList(sql, start, pageSize);
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

	private static final long GPS_TRANSFORM_UNIT = 1000000;
	
	public PetInfos getNearbyPets(String petId, float lng, float lat) {
		log.info("latitude: " + lat + " longitude: " + lng);
		
		long longitude = (long) (lng * GPS_TRANSFORM_UNIT);
		long latitude = (long) (lat * GPS_TRANSFORM_UNIT);
		PetInfos petInfos = new PetInfos();
		
		PetInfoDao petInfoDao = ContextLoader.getPetInfoDao();
		PetInfo petInfo = petInfoDao.getPetDetail(petId);
		if (petInfo == null) {
			petInfos.setResult("3"); // pet doesn't exist
			return petInfos;
		}
		String deviceId = petInfo.getDeviceid();
		log.info("getNearbyPets - deviceId: " + deviceId);
		if (!StringUtil.isNullOrEmpty(deviceId)) {
			List<TrackSdata> trackData = ContextLoader.getDeviceManager().queryNearbyPets(longitude, latitude, 1000, deviceId);
			log.debug("Tracksdata size: " + trackData.size());
			if (trackData.size() > 0) {
				StringBuffer deviceIdList = new StringBuffer();
				for (TrackSdata data : trackData) {
					deviceIdList.append(data.getTermid()).append(',');
				}
				deviceIdList = StringUtil.deleteLastChar(deviceIdList, ',');
				String sql = "SELECT * FROM f_pets WHERE deviceId IN (" + deviceIdList.toString() + ")";
				log.debug("SQL: " + sql);
				List<Map<String, Object>> list = jdbc.queryForList(sql);
				log.debug("Query list size: " + list.size());
				petInfos = PetInfoDao.convertListToPetInfos(list);
				for (PetInfo pet : petInfos.getList()) {
					for (TrackSdata data : trackData) {
						if (pet.getDeviceid().equals(String.valueOf(data.getTermid()))) {
							pet.setLongitude(data.getX());
							pet.setLatitude(data.getY());
							pet.setAddress(data.getRoughaddr());
							pet.setTermtime(data.getTermtime());
							pet.setVitality(data.getVitality());
							double petLng = pet.getLongitude() * 1.0f / GPS_TRANSFORM_UNIT;
							double petLat = pet.getLatitude() * 1.0f / GPS_TRANSFORM_UNIT;
							double distance = DistanceUtil.getDistance(lng, lat, petLng, petLat);
							if (distance <= 1000) {
								pet.setDistance_desc("1公里以内");
							} else if (distance <= 3000) {
								pet.setDistance_desc("3公里以内");
							} else {
								pet.setDistance_desc("10公里以内");
							}
							break;
						}
					}
				}
			}
		}
		petInfos.setResult("0");

		//TODO： query from phone's location table
		String sql = "SELECT * FROM f_location WHERE ABS(longitude - ?) <= 1 AND ABS(latitude - ?) <= 1 AND shijian >= ?";
		List<Map<String, Object>> list = jdbc.queryForList(sql, lng, lat, (System.currentTimeMillis() - HOUR) / 1000);
		StringBuffer idBuffer = new StringBuffer();
		if (list != null) {
			for (Map<String, Object> map : list) {
				String lngTmp = String.valueOf(map
						.get(LocationColum.longitude.name()));
				String latTmp = String.valueOf(map.get(LocationColum.latitude
						.name()));
				int id = (Integer) map.get(LocationColum.petid.name());
				if (isPetIdContainedInPetInfoList(petInfos.getList(), id)) {
					continue;
				}
				double lng2 = Double.parseDouble(lngTmp);
				double lat2 = Double.parseDouble(latTmp);
				if (DistanceUtil.getDistance(lng, lat, lng2, lat2) <= MAX_DISTANCE) {
					idBuffer.append(id).append(',');
				}
			}
			if (idBuffer.toString().endsWith(",")) {
				idBuffer.deleteCharAt(idBuffer.length() - 1);
			}
		}
	
		if (petInfos.getList() == null) {
			petInfos.setList(new ArrayList<PetInfo>());
		}
		if (idBuffer.length() > 0) {
			sql = "SELECT p.*, l.longitude, l.latitude FROM f_pets p JOIN f_location l ON p.petid = l.petid WHERE p.petid IN (" + idBuffer.toString()
					+ ")";
			List<Map<String, Object>> petList = jdbc.queryForList(sql);
			if (petList != null) {
				List<PetInfo> petInfoList = petInfos.getList();
				for (Map<String, Object> map : petList) {
					PetInfo petInfoTmp = PetInfoDao.convertMapToPetInfo(map);
					
					String lngTmp = String.valueOf(map
							.get(LocationColum.longitude.name()));
					String latTmp = String.valueOf(map.get(LocationColum.latitude
							.name()));
					double lng2 = Double.parseDouble(lngTmp);
					double lat2 = Double.parseDouble(latTmp);
					double dis = DistanceUtil.getDistance(lng, lat, lng2, lat2);
					if (dis <= 1000) {
						petInfoTmp.setDistance_desc("1公里以内");
					} else if (dis <= 3000) {
						petInfoTmp.setDistance_desc("3公里以内");
					} else {
						petInfoTmp.setDistance_desc("10公里以内");
					}
					
					petInfoList.add(petInfoTmp);
				}
			}
		}

		return petInfos;
	}
	
	private boolean isPetIdContainedInPetInfoList(List<PetInfo> list, int petId) {
		if (list == null) {
			return false;
		}
		for (PetInfo petInfo : list) {
			if (petInfo.getPetid() == petId) {
				return true;
			}
		}
		return false;
	}

	public PetInfos getConcernedPets(String userName) {
		String sql = "SELECT p.* FROM f_pets AS p JOIN f_guanzhu AS g ON p.petid = g.petid WHERE g.loginid = ?";
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

	public int leaveMsg(String userName, String leaverPetId, String receiverPetId, String content,
			String msgEntryId) {
		String sql = "INSERT INTO message (author, content, leaver_petid, petid, parentid) VALUES(?, ?, ?, ?, ?)";
		return jdbc.update(sql, userName, content, leaverPetId, receiverPetId, msgEntryId);
	}
	
	public long createLeaveMsgEntry(String leaverPetId, String receiverPetId) {
		TableField[] values = new TableField[]{ new TableField("leaver_petid", leaverPetId, Types.INTEGER),
				new TableField("receiver_petid", receiverPetId, Types.INTEGER)};
		String[] keys = new String[] { LeaveMsgColumn.id.name() };
		return insert("leave_msg_entry", values, keys);
	}

	public int replyMsg(String userName, String leaverPetId, String content, String msgEntryId) {
		String petId = getReceiverPetIdByMsgId(msgEntryId);
		return leaveMsg(userName, leaverPetId, petId, content, msgEntryId);
	}

	public boolean isMsgEntryExist(String msgId) {
		String sql = "SELECT count(id) FROM leave_msg_entry WHERE id = ?";
		int count = jdbc.queryForInt(sql, msgId);
		return count > 0;
	}

	public String getReceiverPetIdByMsgId(String msgId) {
		String sql = "SELECT receiver_petid FROM leave_msg_entry WHERE id = ?";
		return jdbc.queryForObject(sql, String.class, msgId);
	}

	public int delLeaveMsg(String msgId) {
		String sql = "DELETE FROM leave_msg_entry WHERE id = ?";
		return jdbc.update(sql, msgId);
	}

	public LeaveMsgs getLeaveMsgsByPet(String petId) {
//		String sql = "SELECT l.id as id, l.author as author, l.content as content, l.petid as petid, l.parentid as parentid, UNIX_TIMESTAMP(l.date) AS _date, "
//				+ " p.nickname as leaver_nickname, p.sex as leaver_sex, p.avatar as leaver_avatar "
//				+ " FROM message AS l, f_pets AS p WHERE l.author = p.ownerid AND (l.petid = ? OR l.author = ?) AND l.parentid = 0 ";
		
		String sql = "SELECT fm.`parentid` AS id, fm.`author` AS author, fm.`content` AS content, " +
				"fm.`petid` AS petid, fm.`parentid` AS parentid, " +
				"UNIX_TIMESTAMP(fm.date) AS _date, p.`nickname` AS leaver_nickname, p.`sex` AS leaver_sex, p.`avatar` AS leaver_avatar " +
				" FROM message AS fm JOIN f_pets AS p " +
				"ON p.`petid` = fm.`leaver_petid` WHERE fm.id IN " +
				"(SELECT MAX(m.id) AS maxid " +
				"FROM message AS m WHERE m.`parentid` IN " +
				"	(SELECT lme.id " +
				"	FROM leave_msg_entry AS lme" +
				"	WHERE leaver_petid = ? OR receiver_petid = ?" +
				"	) " +
				"GROUP BY m.`parentid`) " +
				"ORDER BY fm.`parentid` ASC";
		List<Map<String, Object>> list = jdbc.queryForList(sql, petId, petId);
		LeaveMsgs leaveMsgs = new LeaveMsgs();
		List<LeaveMsg> msgList = new ArrayList<LeaveMsg>();
		leaveMsgs.setList(msgList);
		if (list != null) {
			for (Map<String, Object> map : list) {
				if (map.get(LeaveMsgColumn.id.name()) != null) {
					msgList.add(convertMapToLeaveMsg(map));
				}
			}
		}
		return leaveMsgs;
	}

	private LeaveMsg convertMapToLeaveMsg(Map<String, Object> map) {
		LeaveMsg msg = new LeaveMsg();
		
		msg.setMsgid((Integer) (map.get(LeaveMsgColumn.id.name())));
		msg.setAuthor((String) (map.get(LeaveMsgColumn.author.name())));
		msg.setContent((String) (map.get(LeaveMsgColumn.content.name())));
		msg.setPetid((Integer)(map.get(LeaveMsgColumn.petid.name())));
		msg.setParentid((Integer)(map.get(LeaveMsgColumn.parentid.name())));
		msg.setLeave_timestamp((Long)(map.get("_date")));
		msg.setLeaver_nickname((String)(map.get("leaver_nickname")));
		Integer sex = (Integer) (map.get("leaver_sex"));
		msg.setLeaver_sex(sex == null ? 0 : sex);
		msg.setLeaver_avatar((String) (map.get("leaver_avatar")));
		return msg;
	}

	public LeaveMsgs getRelatedMsgs(String msgId) {
//		String sql = "SELECT l.id as id, l.author as author, l.content as content, l.petid as petid, l.parentid as parentid, UNIX_TIMESTAMP(l.date) AS _date, "
//				+ "  p.nickname as leaver_nickname, p.sex as leaver_sex, p.avatar as leaver_avatar "
//				+ "   FROM ((SELECT * FROM message AS liu WHERE liu.parentid = 0) "
//				+ "   UNION (SELECT c.* FROM message AS parent LEFT JOIN message AS c ON c.parentid = parent.id WHERE parent.id = ?)) AS l LEFT JOIN "
//				+ "   f_pets AS p ON l.author = p.ownerid ";
		String sql = "SELECT fm.`id` AS id, fm.`author` AS author, fm.`content` AS content, fm.`petid` AS petid, fm.`parentid` AS parentid," +
				" UNIX_TIMESTAMP(fm.date) AS _date, " +
				" p.`nickname` AS leaver_nickname, p.`sex` AS leaver_sex, p.`avatar` AS leaver_avatar" +
				" FROM message AS fm JOIN f_pets AS p" +
				" ON fm.`leaver_petid` = p.`petid`" +
				" WHERE fm.`parentid` = ? ORDER BY fm.date ASC ";
		
		List<Map<String, Object>> list = jdbc.queryForList(sql, msgId);
		LeaveMsgs leaveMsgs = new LeaveMsgs();
		List<LeaveMsg> msgList = new ArrayList<LeaveMsg>();
		leaveMsgs.setList(msgList);
		if (list != null) {
			for (Map<String, Object> map : list) {
				if (map.get(LeaveMsgColumn.id.name()) != null) {
					msgList.add(convertMapToLeaveMsg(map));
				}
			}
		}
		return leaveMsgs;
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
	
	public long updateLocation(String petId, String longitude, String latitude) {
		String sql = "SELECT count(petid) FROM f_location WHERE petid = ?";
		int count = jdbc.queryForInt(sql, petId);
		if (count > 0) {
			// update location
			sql = "UPDATE f_location SET longitude=?,latitude=? WHERE petid = ?";
			return jdbc.update(sql, longitude, latitude, petId);
		} else {
			// insert location
			sql = "INSERT INTO f_location ( petid, longitude, latitude) VALUES(?,?,?)";
			return jdbc.update(sql, petId, longitude, latitude);
		}
	}
}
