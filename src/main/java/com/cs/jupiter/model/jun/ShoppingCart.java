package com.cs.jupiter.model.jun;

public class ShoppingCart {
	private String id;
	private String title;
	private double eachPrice;
	private int quantity;
	private String imageLink;
	public String getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}
	public double getEachPrice() {
		return eachPrice;
	}
	public int getQuantity() {
		return quantity;
	}
	public String getImageLink() {
		return imageLink;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setEachPrice(double eachPrice) {
		this.eachPrice = eachPrice;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}
	
	
}
