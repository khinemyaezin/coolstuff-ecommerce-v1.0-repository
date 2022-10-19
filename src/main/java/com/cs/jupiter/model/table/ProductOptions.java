package com.cs.jupiter.model.table;

import com.cs.jupiter.model.interfaces.ViewCredential;

public class ProductOptions extends ViewCredential{
	private String stockId;
	private String optionHeaderId;
	private String optionDetailId;
	private String path;
	public String getStockId() {
		return stockId;
	}
	public String getOptionHeaderId() {
		return optionHeaderId;
	}
	public String getOptionDetailId() {
		return optionDetailId;
	}
	public String getPath() {
		return path;
	}
	public void setStockId(String stockId) {
		this.stockId = stockId;
	}
	public void setOptionHeaderId(String optionHeaderId) {
		this.optionHeaderId = optionHeaderId;
	}
	public void setOptionDetailId(String optionDetailId) {
		this.optionDetailId = optionDetailId;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
}
