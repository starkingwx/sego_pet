package com.sego.mvc.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.imeeting.constants.DeviceOperationConstants;
import com.imeeting.framework.Configuration;
import com.imeeting.framework.ContextLoader;
import com.richitec.util.CryptoUtil;
import com.richitec.util.HttpUtil;
import com.richitec.util.HttpUtil.HttpResponseResult;
import com.richitec.util.HttpUtil.PostRequestFormat;
import com.richitec.util.JSONUtil;
import com.sego.mvc.model.bean.DeviceBindResult;
import com.sego.mvc.model.bean.device.ArchiveOperation;
import com.sego.mvc.model.bean.device.DeviceServerResponse;
import com.sego.mvc.model.bean.device.ErrorMsg;
import com.sego.mvc.model.bean.device.Operation;
import com.sego.mvc.model.bean.device.Terminal;
import com.sego.mvc.model.bean.device.TrackSdata;
import com.sego.mvc.model.bean.device.WhereCondition;
import com.sego.mvc.model.bean.device.DeviceServerResponse.CommandType;
import com.sego.mvc.model.constant.DeviceField;

public class DeviceManager {
	private static Log log = LogFactory.getLog(DeviceManager.class);

	public DeviceBindResult bindDevice(int userId, String deviceId) {
		Operation operation = new Operation();
		operation.setCmdtype(DeviceOperationConstants.ARCHIVE_OPERATION.name());

		ArchiveOperation dbop = new ArchiveOperation();
		dbop.setTablename("terminal");
		dbop.setOperation(DeviceOperationConstants.UPDATE.name());

		operation.setArchive_operation(dbop);

		Terminal terminalInfo = new Terminal();
		terminalInfo.setDeviceno(deviceId);
		terminalInfo.setUserid(userId);

		List<Terminal> terms = new ArrayList<Terminal>();
		terms.add(terminalInfo);
		dbop.setTerminal(terms);

		DeviceBindResult bindResult = new DeviceBindResult();
		bindResult.setResult("3"); // set failed as default
		HttpResponseResult response = postToDeviceServer(
				JSONUtil.toString(operation), deviceId);

		log.info("bindDevice - status: " + response.getStatusCode()
				+ " response text: " + response.getResponseText());
		if (response.getStatusCode() == HttpStatus.SC_OK) {
			DeviceServerResponse res = JSONUtil.toObject(
					response.getResponseText(), DeviceServerResponse.class);
			if (res != null) {
				if (DeviceServerResponse.Status.SUCCESS.name().equals(
						res.getStatus())) {
					if (res.getArchive_operation() != null) {
						bindResult.setResult("0");
						List<Terminal> terminalList = res.getArchive_operation().getTerminal();
						if (terminalList.size() > 0) {
							Terminal term = terminalList.get(0);
							bindResult.setDevice_id(term.getId());
							bindResult.setDevice_password(term.getPassword());
						}
					} 
				} else if (CommandType.ERROR_MESSAGE.name().equals(
						res.getCmdtype())) {
					ErrorMsg errorMsg = res.getError_message();
					if (errorMsg != null) {
						if (errorMsg.getCode() == 1) {
							bindResult.setResult("1");// device not exist
						} else if (errorMsg.getCode() == 2) {
							bindResult.setResult("2"); // already binded by
														// other user
						}
					}
				}
			}
		}
		return bindResult;
	}

	private HttpResponseResult postToDeviceServer(String op, String deviceno) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("c", "MP");
		params.put("op", op);
		params.put("t", String.valueOf(System.currentTimeMillis() + timeDelta));
		params.put("d", deviceno);
		params.put("v", "1.0");

		Configuration config = ContextLoader.getConfiguration();
		HttpResponseResult response = HttpUtil.postRequestWithSignature(
				config.getDeviceServerUrl() + "/rest/operate",
				PostRequestFormat.URLENCODED, params, CryptoUtil.md5("123456"));
		return response;
	}

	// @Deprecated
	// public void insertUserData(String userName, String password) {
	// Operation operation = new Operation();
	// operation.setCmdtype(DeviceOperationConstants.ARCHIVE_OPERATION.name());
	//
	// ArchiveOperation dbop = new ArchiveOperation();
	// dbop.setTablename("terminalinfo");
	// dbop.setOperation(DeviceOperationConstants.INSERT.name());
	// operation.setArchive_operation(dbop);
	//
	// Terminal terminalInfo = new Terminal();
	// terminalInfo.setUserid(userName.hashCode());
	// terminalInfo.setPassword(password);
	// List<Terminal> terms = new ArrayList<Terminal>();
	// terms.add(terminalInfo);
	// dbop.setTerminal(terms);
	//
	// Map<String, String> params = new HashMap<String, String>();
	// params.put("c", "MP");
	// params.put("op", JSONUtil.toString(operation));
	// params.put("t", String.valueOf(System.currentTimeMillis()));
	// params.put("u", "");
	// params.put("v", "1.0");
	//
	// Configuration config = ContextLoader.getConfiguration();
	// HttpResponseResult response = HttpUtil.postRequestWithSignature(
	// config.getDeviceServerUrl() + "/operate",
	// PostRequestFormat.URLENCODED, params, "");
	//
	// }

	public List<TrackSdata> queryNearbyPets(long longitude, long latitude,
			int radius, String deviceId) {
		Operation operation = new Operation();
		operation.setCmdtype(DeviceOperationConstants.ARCHIVE_OPERATION.name());

		ArchiveOperation arop = new ArchiveOperation();
		arop.setTablename("neighbour");
		arop.setOperation(DeviceOperationConstants.QUERY.name());

		List<String> field = new ArrayList<String>();
		field.add(DeviceField.termid.name());
		field.add(DeviceField.x.name());
		field.add(DeviceField.y.name());
		field.add(DeviceField.termtime.name());
		field.add(DeviceField.status.name());
		field.add(DeviceField.vitality.name());
		field.add(DeviceField.address.name());

		arop.setField(field);

		List<WhereCondition> wherecond = new ArrayList<WhereCondition>();
		WhereCondition cond = new WhereCondition();
		cond.setName("centx");
		cond.setValue(String.valueOf(longitude));
		wherecond.add(cond);

		cond = new WhereCondition();
		cond.setName("centy");
		cond.setValue(String.valueOf(latitude));
		wherecond.add(cond);

		cond = new WhereCondition();
		cond.setName("radius");
		cond.setValue(String.valueOf(radius));
		wherecond.add(cond);

		arop.setWherecond(wherecond);
		arop.setOffset(0);
		arop.setRows(20);

		operation.setArchive_operation(arop);

		HttpResponseResult response = postToDeviceServer(
				JSONUtil.toString(operation), deviceId);

		log.info("queryNearbyPets - status: " + response.getStatusCode()
				+ " response text: " + response.getResponseText());

		List<TrackSdata> trackData = new ArrayList<TrackSdata>();
		if (response.getStatusCode() == HttpStatus.SC_OK) {
			DeviceServerResponse res = JSONUtil.toObject(
					response.getResponseText(), DeviceServerResponse.class);
			if (res != null) {
				if (DeviceServerResponse.Status.SUCCESS.name().equals(
						res.getStatus())
						&& res.getArchive_operation() != null) {
					trackData = res.getArchive_operation().getTrack_sdata();
				}
			}
		}
		return trackData;
	}

	private long timeDelta = 0;
	private int count = 0;

	public void syncTime() {
		log.debug("### sync begin - count: " + count + " ###");
		count++;

		Configuration config = ContextLoader.getConfiguration();
		long sysTimeBegin = System.currentTimeMillis();
		HttpResponseResult response = HttpUtil.getRequest(
				config.getDeviceServerUrl() + "/rest/time", null);
		long sysTimeReturn = System.currentTimeMillis();
		int cost = (int) (sysTimeReturn - sysTimeBegin);
		log.debug("systime begin: " + sysTimeBegin + " systime return: "
				+ sysTimeReturn + " cost: " + cost);
		if (response.getStatusCode() == HttpStatus.SC_OK) {
			log.debug("Device system time: " + response.getResponseText());
			long deviceSysTime = Long.parseLong(response.getResponseText());
			long localSysTime = sysTimeBegin + cost / 2;
			timeDelta = deviceSysTime - localSysTime;
			log.debug("Local systime: " + localSysTime + " time delta: "
					+ timeDelta);
		}
		log.debug("### sync time end ###");
	}
}
