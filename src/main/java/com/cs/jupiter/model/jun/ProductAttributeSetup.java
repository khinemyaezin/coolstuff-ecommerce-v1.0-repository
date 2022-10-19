package com.cs.jupiter.model.jun;

import java.util.List;

import com.cs.jupiter.model.table.CategoryVariantOption;

public class ProductAttributeSetup {
	private List<CategoryVariantOption> categoryIdsToDelete;
	private List<CategoryVariantOption> categoryOptions;
	
	
	public List<CategoryVariantOption> getCategoryOptions() {
		return categoryOptions;
	}
	public void setCategoryOptions(List<CategoryVariantOption> categoryOptions) {
		this.categoryOptions = categoryOptions;
	}
	public List<CategoryVariantOption> getCategoryIdsToDelete() {
		return categoryIdsToDelete;
	}
	public void setCategoryIdsToDelete(List<CategoryVariantOption> categoryIdsToDelete) {
		this.categoryIdsToDelete = categoryIdsToDelete;
	}
	
}
