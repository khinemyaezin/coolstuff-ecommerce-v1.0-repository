package com.cs.jupiter.model.jun;

import java.util.ArrayList;
import java.util.List;

import com.cs.jupiter.model.table.Category;

public class SurroundCategory {
	private List<Category> upperLayer = new ArrayList<>();
	private List<Category> lowerLayer = new ArrayList<>();
	
	public List<Category> getUpperLayer() {
		return upperLayer;
	}
	public List<Category> getLowerLayer() {
		return lowerLayer;
	}
	public void setUpperLayer(List<Category> upperLayer) {
		this.upperLayer = upperLayer;
	}
	public void setLowerLayer(List<Category> lowerLayer) {
		this.lowerLayer = lowerLayer;
	}
	
	
}
