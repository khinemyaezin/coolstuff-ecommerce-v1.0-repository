package com.cs.jupiter.model.jun;

import com.cs.jupiter.model.interfaces.ViewCredential;

public class ProductSummary extends ViewCredential{
	private double totalSellingPrice;
	private double totalPrice;
	private String brandId;
	private double profit;
	private int totalProduct;
	
	public double getTotalSellingPrice() {
		return totalSellingPrice;
	}
	public void setTotalSellingPrice(double totalSellingPrice) {
		this.totalSellingPrice = totalSellingPrice;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getBrandId() {
		return brandId;
	}
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	public double getProfit() {
		return profit;
	}
	public void setProfit(double profit) {
		this.profit = profit;
	}
	public int getTotalProduct() {
		return totalProduct;
	}
	public void setTotalProduct(int totalProduct) {
		this.totalProduct = totalProduct;
	}
	
	
}
