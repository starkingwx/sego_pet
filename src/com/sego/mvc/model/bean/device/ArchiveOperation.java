package com.sego.mvc.model.bean.device;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ArchiveOperation {
	private String tablename;
	private String operation;
	private List<Terminal> terminal;
	private String affectid;
	private List<String> field;
	private List<WhereCondition> wherecond;
	private int offset;
	private int rows;
	private List<TrackSdata> track_sdata;

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

	public List<Terminal> getTerminal() {
		return terminal;
	}

	public void setTerminal(List<Terminal> terminal) {
		this.terminal = terminal;
	}

	public List<String> getField() {
		return field;
	}

	public void setField(List<String> field) {
		this.field = field;
	}

	public String getAffectid() {
		return affectid;
	}

	public void setAffectid(String affectid) {
		this.affectid = affectid;
	}

	public List<WhereCondition> getWherecond() {
		return wherecond;
	}

	public void setWherecond(List<WhereCondition> wherecond) {
		this.wherecond = wherecond;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public List<TrackSdata> getTrack_sdata() {
		return track_sdata;
	}

	public void setTrack_sdata(List<TrackSdata> track_sdata) {
		this.track_sdata = track_sdata;
	}

}
