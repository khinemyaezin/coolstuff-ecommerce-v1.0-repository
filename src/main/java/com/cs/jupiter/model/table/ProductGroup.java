package com.cs.jupiter.model.table;

import java.util.ArrayList;
import java.util.List;

import com.cs.jupiter.model.interfaces.ViewCredential;

public class ProductGroup extends ViewCredential{
	private Brand brand;
	private List<Product> products;
	
	public ProductGroup(){
		this.products = new ArrayList<>();
	}
	public ProductGroup(String id){
		this.setId(id);
		this.products = new ArrayList<>();
	}
	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
}
