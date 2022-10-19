package com.cs.jupiter.model.jun;

import java.util.List;

import com.cs.jupiter.model.table.Condition;
import com.cs.jupiter.model.table.ProductImage;
import com.cs.jupiter.model.table.ProductVariantOptionDetail;
import com.cs.jupiter.model.table.ProductVariantOptionHeader;

public class ProductDetail {
	private ListProduct parent;
	private String id;
	private ProductVariantOptionHeader firstStockVariantOptionHeader;
	private ProductVariantOptionDetail firstStockVariantOptionDetail;
	private String firstVariantTitle;
	private ProductVariantOptionHeader secondStockVariantOptionHeader;
	private ProductVariantOptionDetail secondStockVariantOptionDetail;
	private String secondVariantTitle;
	private ProductVariantOptionHeader thirdStockVariantOptionHeader;
	private ProductVariantOptionDetail thirdStockVariantOptionDetail;
	private String thirdVariantTitle;
	private double sellingPrice;
	private int quantity;
	private Condition condition;
	private List<ProductImage> imageList;
	private String path;
	private String[] features;
	private String description;
	public ProductVariantOptionHeader getFirstStockVariantOptionHeader() {
		return firstStockVariantOptionHeader;
	}
	public ProductVariantOptionDetail getFirstStockVariantOptionDetail() {
		return firstStockVariantOptionDetail;
	}
	public String getFirstVariantTitle() {
		return firstVariantTitle;
	}
	public ProductVariantOptionHeader getSecondStockVariantOptionHeader() {
		return secondStockVariantOptionHeader;
	}
	public ProductVariantOptionDetail getSecondStockVariantOptionDetail() {
		return secondStockVariantOptionDetail;
	}
	public String getSecondVariantTitle() {
		return secondVariantTitle;
	}
	public ProductVariantOptionHeader getThirdStockVariantOptionHeader() {
		return thirdStockVariantOptionHeader;
	}
	public ProductVariantOptionDetail getThirdStockVariantOptionDetail() {
		return thirdStockVariantOptionDetail;
	}
	public String getThirdVariantTitle() {
		return thirdVariantTitle;
	}
	public double getSellingPrice() {
		return sellingPrice;
	}
	public int getQuantity() {
		return quantity;
	}
	public Condition getCondition() {
		return condition;
	}
	
	public void setFirstStockVariantOptionHeader(ProductVariantOptionHeader firstStockVariantOptionHeader) {
		this.firstStockVariantOptionHeader = firstStockVariantOptionHeader;
	}
	public void setFirstStockVariantOptionDetail(ProductVariantOptionDetail firstStockVariantOptionDetail) {
		this.firstStockVariantOptionDetail = firstStockVariantOptionDetail;
	}
	public void setFirstVariantTitle(String firstVariantTitle) {
		this.firstVariantTitle = firstVariantTitle;
	}
	public void setSecondStockVariantOptionHeader(ProductVariantOptionHeader secondStockVariantOptionHeader) {
		this.secondStockVariantOptionHeader = secondStockVariantOptionHeader;
	}
	public void setSecondStockVariantOptionDetail(ProductVariantOptionDetail secondStockVariantOptionDetail) {
		this.secondStockVariantOptionDetail = secondStockVariantOptionDetail;
	}
	public void setSecondVariantTitle(String secondVariantTitle) {
		this.secondVariantTitle = secondVariantTitle;
	}
	public void setThirdStockVariantOptionHeader(ProductVariantOptionHeader thirdStockVariantOptionHeader) {
		this.thirdStockVariantOptionHeader = thirdStockVariantOptionHeader;
	}
	public void setThirdStockVariantOptionDetail(ProductVariantOptionDetail thirdStockVariantOptionDetail) {
		this.thirdStockVariantOptionDetail = thirdStockVariantOptionDetail;
	}
	public void setThirdVariantTitle(String thirdVariantTitle) {
		this.thirdVariantTitle = thirdVariantTitle;
	}
	public void setSellingPrice(double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ListProduct getParent() {
		return parent;
	}
	public void setParent(ListProduct parent) {
		this.parent = parent;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public List<ProductImage> getImageList() {
		return imageList;
	}
	public void setImageList(List<ProductImage> imageList) {
		this.imageList = imageList;
	}
	public String[] getFeatures() {
		return features;
	}
	public String getDescription() {
		return description;
	}
	public void setFeatures(String[] features) {
		this.features = features;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
