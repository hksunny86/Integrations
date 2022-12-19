package com.inov8.jsblconsumer.model;

public class MessageModel {

	public String code = null;
	public String level = null;
	public String descr = null;

	public MessageModel(String code, String level, String descr) {
		super();
		this.code = code;
		this.level = level;
		this.descr = descr;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}
}
