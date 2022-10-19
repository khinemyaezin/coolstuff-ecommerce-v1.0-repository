package com.cs.jupiter.model.table;

import com.cs.jupiter.model.interfaces.ViewCredential;

public class OrderItem extends ViewCredential{
	private Order order;
	private ProductVariant product;
	private double priceEach;
	private int quantity;
	private double priceTotal;
	private String productName;
	
	public OrderItem(){
		
	}
	public OrderItem(String id){
		this.setId(id);
	}
	public Order getOrder() {
		return order;
	}
	public ProductVariant getProduct() {
		return product;
	}
	public double getPriceEach() {
		return priceEach;
	}
	public int getQuantity() {
		return quantity;
	}
	public double getPriceTotal() {
		return priceTotal;
	}
	public String getProductName() {
		return productName;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public void setProduct(ProductVariant product) {
		this.product = product;
	}
	public void setPriceEach(double priceEach) {
		this.priceEach = priceEach;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public void setPriceTotal(double priceTotal) {
		this.priceTotal = priceTotal;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
}
