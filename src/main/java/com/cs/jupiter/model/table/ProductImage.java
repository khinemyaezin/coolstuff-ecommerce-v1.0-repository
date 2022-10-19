package com.cs.jupiter.model.table;

import com.cs.jupiter.model.interfaces.ViewCredential;

public class ProductImage extends ViewCredential{
	private Product stock;
	private ProductVariant variant;
	private String path;
	private ImageData image;
	private int imageType; //1 -> default, 2,3,4,5
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Product getStock() {
		return stock;
	}

	public void setStock(Product stock) {
		this.stock = stock;
	}

	public ProductVariant getVariant() {
		return variant;
	}

	public void setVariant(ProductVariant variant) {
		this.variant = variant;
	}

	public ImageData getImage() {
		return image;
	}

	public void setImage(ImageData image) {
		this.image = image;
	}

	public int getImageType() {
		return imageType;
	}

	public void setImageType(int imageType) {
		this.imageType = imageType;
	}
	
}
