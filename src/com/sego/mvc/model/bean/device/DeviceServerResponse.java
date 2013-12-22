package com.sego.mvc.model.bean.device;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DeviceServerResponse {
	
	public static enum Status {
		SUCCESS,
		FAILED
	}
	
	public static enum CommandType {
		ARCHIVE_OPERATION,
		ERROR_MESSAGE
	}
	
	private String status;
	private long ts;
	private String cmdtype;
	private ArchiveOperation archive_operation;
	private ErrorMsg error_message;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getTs() {
		return ts;
	}

	public void setTs(long ts) {
		this.ts = ts;
	}

	public String getCmdtype() {
		return cmdtype;
	}

	public void setCmdtype(String cmdtype) {
		this.cmdtype = cmdtype;
	}

	public ArchiveOperation getArchive_operation() {
		return archive_operation;
	}

	public void setArchive_operation(ArchiveOperation database_operation) {
		this.archive_operation = database_operation;
	}

	public ErrorMsg getError_message() {
		return error_message;
	}

	public void setError_message(ErrorMsg error_message) {
		this.error_message = error_message;
	}

}
