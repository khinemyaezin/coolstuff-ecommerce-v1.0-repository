package com.cs.jupiter.model.table;

import com.cs.jupiter.model.interfaces.ViewCredential;

public class StockHolding extends ViewCredential {
	private Product stock;
	private Brand brand;
	private Inventory inventory;
	private int quantity;
	
	public Product getStock() {
		return stock;
	}
	public void setStock(Product stock) {
		this.stock = stock;
	}
	public Brand getBrand() {
		return brand;
	}
	public void setBrand(Brand brand) {
		this.brand = brand;
	}
	public Inventory getInventory() {
		return inventory;
	}
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
