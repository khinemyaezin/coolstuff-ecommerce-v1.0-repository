package com.cs.jupiter.model.table;

import com.cs.jupiter.model.interfaces.ViewCredential;

public class CategoryVariantOption extends ViewCredential{
	private Category category;
	private ProductVariantOptionHeader option;
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public ProductVariantOptionHeader getOption() {
		return option;
	}
	public void setOption(ProductVariantOptionHeader option) {
		this.option = option;
	}
	
	
}
