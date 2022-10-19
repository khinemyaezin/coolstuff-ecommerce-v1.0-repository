package com.cs.jupiter.model.table;

import com.cs.jupiter.model.interfaces.ViewCredential;

public class ProductVariantTheme extends ViewCredential{
	private ProductVariantOptionHeader firstStockVariantOptionHeader;
	private ProductVariantOptionHeader secondStockVariantOptionHeader;
	private ProductVariantOptionHeader thirdStockVariantOptionHeader;
	private int order;
	
	public ProductVariantTheme(){
		
	}
	public ProductVariantTheme(String id){
		this.setId(id);
	}
	public ProductVariantOptionHeader getFirstStockVariantOptionHeader() {
		return firstStockVariantOptionHeader;
	}
	public void setFirstStockVariantOptionHeader(ProductVariantOptionHeader firstStockVariantOptionHeader) {
		this.firstStockVariantOptionHeader = firstStockVariantOptionHeader;
	}
	public ProductVariantOptionHeader getSecondStockVariantOptionHeader() {
		return secondStockVariantOptionHeader;
	}
	public void setSecondStockVariantOptionHeader(ProductVariantOptionHeader secondStockVariantOptionHeader) {
		this.secondStockVariantOptionHeader = secondStockVariantOptionHeader;
	}
	
	public ProductVariantOptionHeader getThirdStockVariantOptionHeader() {
		return thirdStockVariantOptionHeader;
	}
	public void setThirdStockVariantOptionHeader(ProductVariantOptionHeader thirdStockVariantOptionHeader) {
		this.thirdStockVariantOptionHeader = thirdStockVariantOptionHeader;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	
	
	
}
