package com.cs.jupiter.model.table;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cs.jupiter.model.interfaces.ViewCredential;

public class ProductVariant extends ViewCredential{
	private String sellerSku;
	private Product product;
	private ProductVariantOptionHeader firstStockVariantOptionHeader;
	private ProductVariantOptionDetail firstStockVariantOptionDetail;
	private String firstVariantTitle;
	private ProductVariantOptionHeader secondStockVariantOptionHeader;
	private ProductVariantOptionDetail secondStockVariantOptionDetail;
	private String secondVariantTitle;
	private ProductVariantOptionHeader thirdStockVariantOptionHeader;
	private ProductVariantOptionDetail thirdStockVariantOptionDetail;
	private String thirdVariantTitle;
	private double price;
	private double sellingPrice;
	private int quantity;
	private String conditionDesc;
	private Date startDate;
	private Date endDate;
	private Condition condition;
	private List<ProductImage> image;
	private ImageData profile;
	private boolean defaultProduct;
	private String description;
	private String[] features;
	
	public ProductVariant(){
		this.image = new ArrayList<>();
	}
	public ProductVariant(String id){
		this.setId(id);
	}
	public String getSellerSku() {
		return sellerSku;
	}
	public void setSellerSku(String sellerSku) {
		this.sellerSku = sellerSku;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	
	public ProductVariantOptionHeader getFirstStockVariantOptionHeader() {
		return firstStockVariantOptionHeader;
	}
	public void setFirstStockVariantOptionHeader(ProductVariantOptionHeader firstStockVariantOptionHeader) {
		this.firstStockVariantOptionHeader = firstStockVariantOptionHeader;
	}
	public ProductVariantOptionDetail getFirstStockVariantOptionDetail() {
		return firstStockVariantOptionDetail;
	}
	public void setFirstStockVariantOptionDetail(ProductVariantOptionDetail firstStockVariantOptionDetail) {
		this.firstStockVariantOptionDetail = firstStockVariantOptionDetail;
	}
	public String getFirstVariantTitle() {
		return firstVariantTitle;
	}
	public void setFirstVariantTitle(String firstVariantTitle) {
		this.firstVariantTitle = firstVariantTitle;
	}
	public ProductVariantOptionHeader getSecondStockVariantOptionHeader() {
		return secondStockVariantOptionHeader;
	}
	public void setSecondStockVariantOptionHeader(ProductVariantOptionHeader secondStockVariantOptionHeader) {
		this.secondStockVariantOptionHeader = secondStockVariantOptionHeader;
	}
	public ProductVariantOptionDetail getSecondStockVariantOptionDetail() {
		return secondStockVariantOptionDetail;
	}
	public void setSecondStockVariantOptionDetail(ProductVariantOptionDetail secondStockVariantOptionDetail) {
		this.secondStockVariantOptionDetail = secondStockVariantOptionDetail;
	}
	public String getSecondVariantTitle() {
		return secondVariantTitle;
	}
	public void setSecondVariantTitle(String secondVariantTitle) {
		this.secondVariantTitle = secondVariantTitle;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getSellingPrice() {
		return sellingPrice;
	}
	public void setSellingPrice(double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getConditionDesc() {
		return conditionDesc;
	}
	public void setConditionDesc(String conditionDesc) {
		this.conditionDesc = conditionDesc;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public Condition getCondition() {
		return condition;
	}
	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public List<ProductImage> getImage() {
		return image;
	}
	public void setImage(List<ProductImage> image) {
		this.image = image;
	}
	public ImageData getProfile() {
		return profile;
	}
	public void setProfile(ImageData profile) {
		this.profile = profile;
	}
	public boolean isDefaultProduct() {
		return defaultProduct;
	}
	public void setDefaultProduct(boolean defaultProduct) {
		this.defaultProduct = defaultProduct;
	}
	public ProductVariantOptionHeader getThirdStockVariantOptionHeader() {
		return thirdStockVariantOptionHeader;
	}
	public void setThirdStockVariantOptionHeader(ProductVariantOptionHeader thirdStockVariantOptionHeader) {
		this.thirdStockVariantOptionHeader = thirdStockVariantOptionHeader;
	}
	public ProductVariantOptionDetail getThirdStockVariantOptionDetail() {
		return thirdStockVariantOptionDetail;
	}
	public void setThirdStockVariantOptionDetail(ProductVariantOptionDetail thirdStockVariantOptionDetail) {
		this.thirdStockVariantOptionDetail = thirdStockVariantOptionDetail;
	}
	public String getThirdVariantTitle() {
		return thirdVariantTitle;
	}
	public void setThirdVariantTitle(String thirdVariantTitle) {
		this.thirdVariantTitle = thirdVariantTitle;
	}
	
	public String getDescription() {
		return description;
	}
	public String[] getFeatures() {
		return features;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setFeatures(String[] features) {
		this.features = features;
	}
	
}
