package com.cs.jupiter.model.table;
import java.util.ArrayList;
import java.util.List;
import com.cs.jupiter.model.interfaces.ViewCredential;

public class Product extends ViewCredential{
	private PackType packtype;
	private Brand brand;
	private String manufacture;
	private String stockBrandTitle;
	private Category category;
	private String description;
	private String features;
	private ImageData image;
	private List<ProductVariant> variant;
	private ProductVariant standAlone;
	private boolean hasVariant;
	private ProductGroup productGroup;
	private int variantCount = 0 ;
	
	public Product(){
		this.variant = new ArrayList<>();
	}
	public Product(String id){
		this.setId(id);
	}
	
	public PackType getPacktype() {
		return packtype;
	}
	public void setPacktype(PackType packtype) {
		this.packtype = packtype;
	}
	
	public Brand getBrand() {
		return brand;
	}
	public void setBrand(Brand brand) {
		this.brand = brand;
	}
	public String getManufacture() {
		return manufacture;
	}
	public void setManufacture(String manufacture) {
		this.manufacture = manufacture;
	}
	public String getStockBrandTitle() {
		return stockBrandTitle;
	}
	public void setStockBrandTitle(String stockBrandTitle) {
		this.stockBrandTitle = stockBrandTitle;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public List<ProductVariant> getVariant() {
		return variant;
	}
	public void setVariant(List<ProductVariant> variant) {
		this.variant = variant;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFeatures() {
		return features;
	}
	public void setFeatures(String features) {
		this.features = features;
	}
	public ImageData getImage() {
		return image;
	}
	public void setImage(ImageData image) {
		this.image = image;
	}
	
	public ProductVariant getStandAlone() {
		return standAlone;
	}
	public void setStandAlone(ProductVariant standAlone) {
		this.standAlone = standAlone;
	}
	public ProductGroup getProductGroup() {
		return productGroup;
	}
	public void setProductGroup(ProductGroup productGroup) {
		this.productGroup = productGroup;
	}
	public int getVariantCount() {
		return variantCount;
	}
	public boolean isHasVariant() {
		return hasVariant;
	}
	public void setHasVariant(boolean hasVariant) {
		this.hasVariant = hasVariant;
	}
	public void setVariantCount(int variantCount) {
		this.variantCount = variantCount;
	}
	
	
}
