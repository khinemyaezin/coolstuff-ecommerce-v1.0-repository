package com.cs.jupiter.model.interfaces;

public class FileProperties {
	private String id;
	private String filetype;
	private int isDefault;
	private String color;
	private String comment;
	public FileProperties(){
		this.id = "-1";
		this.filetype = "";
		this.isDefault = 0;
		this.color = "";
		this.comment = "";
	}
	public String getId() {
		return id;
	}
	public String getFiletype() {
		return filetype;
	}
	public int getIsDefault() {
		return isDefault;
	}
	public String getColor() {
		return color;
	}
	public String getComment() {
		return comment;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}
	public void setIsDefault(int isDefault) {
		this.isDefault = isDefault;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}
