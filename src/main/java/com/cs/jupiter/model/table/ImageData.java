package com.cs.jupiter.model.table;

import com.cs.jupiter.model.interfaces.ViewCredential;

public class ImageData extends ViewCredential {
	
	private String base64;
	private String path;
	private boolean defaults;
	private String comment;
	public ImageData(String name,String base64, String path, boolean defaults, String comment) {
		super();
		this.setName(name);
		this.base64 = base64;
		this.path = path;
		this.defaults = defaults;
		this.comment = comment;
	}
	public ImageData(){
		super();
		this.base64 = "";
		this.path = "";
		this.defaults = false;
		this.comment = "";
	}
	public ImageData(String base64, String path){
		super();
		this.base64 = base64;
		this.path = path;
		this.defaults = false;
		this.comment = "";
	}
	public ImageData(String id){
		super();
		this.setId(id);
	}
	
	public String getPath() {
		return path;
	}
	public boolean isDefaults() {
		return defaults;
	}
	public String getComment() {
		return comment;
	}

	public void setPath(String path) {
		this.path = path;
	}
	public void setDefaults(boolean defaults) {
		this.defaults = defaults;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getBase64() {
		return base64;
	}
	public void setBase64(String base64) {
		this.base64 = base64;
	}
	
}
