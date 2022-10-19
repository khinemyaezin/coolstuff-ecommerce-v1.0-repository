package com.cs.jupiter.model.table;

import java.util.ArrayList;
import java.util.List;

import com.cs.jupiter.model.interfaces.ViewCredential;

public class Nrc extends ViewCredential{
	private List<NrcDistrict> district;
	
	public Nrc(){
		this.district = new ArrayList<>();
	}
	public Nrc(String id){
		this.setId(id);
		this.district = new ArrayList<>();
	}
	public List<NrcDistrict> getDistrict() {
		return district;
	}
	public void setDistrict(List<NrcDistrict> district) {
		this.district = district;
	}
}
