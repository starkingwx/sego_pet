package com.sego.mvc.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpStatus;

import com.imeeting.constants.DeviceOperationConstants;
import com.imeeting.framework.Configuration;
import com.imeeting.framework.ContextLoader;
import com.richitec.util.HttpUtil;
import com.richitec.util.HttpUtil.HttpResponseResult;
import com.richitec.util.HttpUtil.PostRequestFormat;
import com.richitec.util.JSONUtil;
import com.sego.mvc.model.bean.device.DatabaseOperation;
import com.sego.mvc.model.bean.device.DeviceServerResponse;
import com.sego.mvc.model.bean.device.ErrorMsg;
import com.sego.mvc.model.bean.device.Operation;
import com.sego.mvc.model.bean.device.TerminalInfo;

public class DeviceManager {

	public String bindDevice(String userName, String deviceId) {
		Operation operation = new Operation();
		operation.setCmdtype(DeviceOperationConstants.DATABASE_OPERATION.name());
		
		DatabaseOperation dbop = new DatabaseOperation();
		dbop.setTablename("terminalinfo");
		dbop.setOperation(DeviceOperationConstants.UPDATE.name());
		
		operation.setDatabase_operation(dbop);
		
		TerminalInfo terminalInfo = new TerminalInfo();
		terminalInfo.setDeviceno(deviceId);
		terminalInfo.setUserid(userName);
		
		dbop.setTerminalinfo(terminalInfo);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("c", "MP");
		params.put("op", JSONUtil.toString(operation));
		params.put("t", String.valueOf(System.currentTimeMillis()));
		params.put("u", "");
		params.put("v", "1.0");
		
		String result = "3"; // set failed as default
		Configuration config = ContextLoader.getConfiguration();
		HttpResponseResult response = HttpUtil.postRequestWithSignature(config.getDeviceServerUrl() + "/operate", PostRequestFormat.URLENCODED, params, "");
		if (response.getStatusCode() == HttpStatus.SC_OK) {
			DeviceServerResponse res = JSONUtil.toObject(response.getResponseText(), DeviceServerResponse.class);
			if (res != null) {
				if ("SUCCESS".equals(res.getStatus())) {
					result = "0";
				} else if ("ERROR_MESSAGE".equals(res.getCmdtype())) {
					ErrorMsg errorMsg = res.getError_message();
					if (errorMsg != null) {
						if (errorMsg.getCode() == 1) {
							result = "1"; // device not exist
						} else if (errorMsg.getCode() == 2) {
							result = "2"; // already binded by other user
						}
					}
				}
			}
		}
		return result;
	}
	
	public void insertUserData(String userName, String password) {
		Operation operation = new Operation();
		operation.setCmdtype(DeviceOperationConstants.DATABASE_OPERATION.name());
		
		DatabaseOperation dbop = new DatabaseOperation();
		dbop.setTablename("terminalinfo");
		dbop.setOperation(DeviceOperationConstants.INSERT.name());
		operation.setDatabase_operation(dbop);
		
		TerminalInfo terminalInfo = new TerminalInfo();
		terminalInfo.setUserid(userName);
		terminalInfo.setPassword(password);
		dbop.setTerminalinfo(terminalInfo);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("c", "MP");
		params.put("op", JSONUtil.toString(operation));
		params.put("t", String.valueOf(System.currentTimeMillis()));
		params.put("u", "");
		params.put("v", "1.0");
		
		Configuration config = ContextLoader.getConfiguration();
		HttpResponseResult response = HttpUtil.postRequestWithSignature(config.getDeviceServerUrl() + "/operate", PostRequestFormat.URLENCODED, params, "");
		
	}
	
	
}
