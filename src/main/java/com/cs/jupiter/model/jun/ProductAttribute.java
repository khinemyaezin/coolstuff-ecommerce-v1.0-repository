package com.cs.jupiter.model.jun;

public class ProductAttribute {
	private String categoryId;
	private String optionId;
	private String optionName;
	private String junId;
	private boolean status;
	
	public String getOptionId() {
		return optionId;
	}
	public void setOptionId(String optionId) {
		this.optionId = optionId;
	}
	public String getOptionName() {
		return optionName;
	}
	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}
	public String getJunId() {
		return junId;
	}
	public void setJunId(String junId) {
		this.junId = junId;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	
	
}
