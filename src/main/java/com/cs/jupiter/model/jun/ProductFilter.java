package com.cs.jupiter.model.jun;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ProductFilter {
	private String id;
	private String searchKeyWord;
	private String categoryId;
	private String optionDetail1Id;
	private String optionDetail2Id;
	private String optionDetail3Id;
	private String parentId;
	
	@JsonIgnore
	private boolean defaultProduct;
	
	public String getSearchKeyWord() {
		return searchKeyWord;
	}
	public boolean isDefaultProduct() {
		return defaultProduct;
	}
	public void setSearchKeyWord(String searchKeyWord) {
		this.searchKeyWord = searchKeyWord;
	}
	public void setDefaultProduct(boolean defaultProduct) {
		this.defaultProduct = defaultProduct;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getOptionDetail1Id() {
		return optionDetail1Id;
	}
	public String getOptionDetail2Id() {
		return optionDetail2Id;
	}
	public String getOptionDetail3Id() {
		return optionDetail3Id;
	}
	public String getId() {
		return id;
	}
	public void setOptionDetail1Id(String optionDetail1Id) {
		this.optionDetail1Id = optionDetail1Id;
	}
	public void setOptionDetail2Id(String optionDetail2Id) {
		this.optionDetail2Id = optionDetail2Id;
	}
	public void setOptionDetail3Id(String optionDetail3Id) {
		this.optionDetail3Id = optionDetail3Id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	
	
}
