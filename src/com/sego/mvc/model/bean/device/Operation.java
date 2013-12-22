package com.sego.mvc.model.bean.device;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Operation {
	private String cmdtype;
	private ArchiveOperation archive_operation;

	public String getCmdtype() {
		return cmdtype;
	}

	public void setCmdtype(String cmdtype) {
		this.cmdtype = cmdtype;
	}

	public ArchiveOperation getArchive_operation() {
		return archive_operation;
	}

	public void setArchive_operation(ArchiveOperation archive_operation) {
		this.archive_operation = archive_operation;
	}

}
