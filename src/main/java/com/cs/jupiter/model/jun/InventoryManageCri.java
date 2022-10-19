package com.cs.jupiter.model.jun;

import java.util.List;

import com.cs.jupiter.model.interfaces.ViewCredential;

public class InventoryManageCri extends ViewCredential{
	
	private String manufacture;
	private String brand;
	private String sellerSKU;
	private String brandId;
	private String brandCode;
	private String variantId;
	private String productGroup;
	
	private List<String> deleteId;
	
	private boolean requestAdditionalDetail = false;
	public String getManufacture() {
		return manufacture;
	}
	public void setManufacture(String manufacture) {
		this.manufacture = manufacture;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getSellerSKU() {
		return sellerSKU;
	}
	public String getBrandId() {
		return brandId;
	}
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	public void setSellerSKU(String sellerSKU) {
		this.sellerSKU = sellerSKU;
	}
	public String getVariantId() {
		return variantId;
	}
	public void setVariantId(String variantId) {
		this.variantId = variantId;
	}
	public boolean isRequestAdditionalDetail() {
		return requestAdditionalDetail;
	}
	public void setRequestAdditionalDetail(boolean requestAdditionalDetail) {
		this.requestAdditionalDetail = requestAdditionalDetail;
	}
	public List<String> getDeleteId() {
		return deleteId;
	}
	public void setDeleteId(List<String> deleteId) {
		this.deleteId = deleteId;
	}
	public String getBrandCode() {
		return brandCode;
	}
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	public String getProductGroup() {
		return productGroup;
	}
	public void setProductGroup(String productGroup) {
		this.productGroup = productGroup;
	}
	
	
	
}
