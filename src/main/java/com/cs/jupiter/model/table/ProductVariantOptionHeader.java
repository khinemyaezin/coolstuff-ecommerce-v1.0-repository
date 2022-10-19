package com.cs.jupiter.model.table;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.cs.jupiter.model.interfaces.ViewCredential;

public class ProductVariantOptionHeader extends ViewCredential {
	private short optionType;
	private String selectedDetail;
	private List<ProductVariantOptionDetail> details = new ArrayList<ProductVariantOptionDetail>();
	
	public ProductVariantOptionHeader (String id) {
		this.setId(id);
	}
	public ProductVariantOptionHeader () {
		super();
	}
	public List<ProductVariantOptionDetail> getDetails() {
		return details;
	}

	public void setDetails(List<ProductVariantOptionDetail> details) {
		this.details = details;
	}
	
	public short getOptionType() {
		return optionType;
	}
	public void setOptionType(short optionType) {
		this.optionType = optionType;
	}
	public String getSelectedDetail() {
		return selectedDetail;
	}
	public void setSelectedDetail(String selectedDetail) {
		this.selectedDetail = selectedDetail;
	}
	public boolean isOk() {
		try {
			Field[] fields = ProductVariantOptionHeader.class.getDeclaredFields();
			for(Field f: fields){
				System.out.println(f.getName());
			}
		} catch (Exception e) {

		}
		return true;
	}
}
