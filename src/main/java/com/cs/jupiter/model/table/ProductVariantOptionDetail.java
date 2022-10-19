package com.cs.jupiter.model.table;

import com.cs.jupiter.model.interfaces.ViewCredential;

public class ProductVariantOptionDetail extends ViewCredential{
	private ProductVariantOptionHeader header;
	private String path;
	private int order;
	private short optionType;
	public ProductVariantOptionDetail(){
	}
	public ProductVariantOptionDetail(String id){
		this.setId(id);
	}
	
	public ProductVariantOptionHeader getHeader() {
		return header;
	}

	public void setHeader(ProductVariantOptionHeader header) {
		this.header = header;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	public short getOptionType() {
		return optionType;
	}
	public void setOptionType(short optionType) {
		this.optionType = optionType;
	}
	
}
