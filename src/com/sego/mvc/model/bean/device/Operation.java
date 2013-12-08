package com.sego.mvc.model.bean.device;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Operation {
	private String cmdtype;
	private DatabaseOperation database_operation;

	public String getCmdtype() {
		return cmdtype;
	}

	public void setCmdtype(String cmdtype) {
		this.cmdtype = cmdtype;
	}

	public DatabaseOperation getDatabase_operation() {
		return database_operation;
	}

	public void setDatabase_operation(DatabaseOperation database_operation) {
		this.database_operation = database_operation;
	}

}
