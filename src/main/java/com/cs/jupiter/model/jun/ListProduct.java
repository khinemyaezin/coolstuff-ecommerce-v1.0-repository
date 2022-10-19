package com.cs.jupiter.model.jun;

import com.cs.jupiter.model.table.Brand;

public class ListProduct {
	private String id;
	private String title;
	private String manufacture;
	private String description;
	private String[] features;
	private String[] bulletPoints;
	private Brand brand;
	
	public String getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}
	public String getManufacture() {
		return manufacture;
	}
	public String getDescription() {
		return description;
	}
	public String[] getFeatures() {
		return features;
	}
	public String[] getBulletPoints() {
		return bulletPoints;
	}
	public Brand getBrand() {
		return brand;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setManufacture(String manufacture) {
		this.manufacture = manufacture;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setFeatures(String[] features) {
		this.features = features;
	}
	public void setBulletPoints(String[] bulletPoints) {
		this.bulletPoints = bulletPoints;
	}
	public void setBrand(Brand brand) {
		this.brand = brand;
	}
	
	
}
