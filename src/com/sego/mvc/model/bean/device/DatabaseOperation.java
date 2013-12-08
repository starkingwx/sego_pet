package com.sego.mvc.model.bean.device;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DatabaseOperation {
	private String tablename;
	private String operation;
	private TerminalInfo terminalinfo;
	private String affectid;
	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public TerminalInfo getTerminalinfo() {
		return terminalinfo;
	}

	public void setTerminalinfo(TerminalInfo terminalinfo) {
		this.terminalinfo = terminalinfo;
	}

	public String getAffectid() {
		return affectid;
	}

	public void setAffectid(String affectid) {
		this.affectid = affectid;
	}

}
