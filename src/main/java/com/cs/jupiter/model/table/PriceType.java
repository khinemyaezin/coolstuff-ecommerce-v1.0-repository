package com.cs.jupiter.model.table;


import com.cs.jupiter.model.interfaces.ViewCredential;

public class PriceType extends ViewCredential{

	private int rowNumber;
	
	
	public PriceType(){
		

	}
	public int getRowNumber() {
		return rowNumber;
	}
	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}
	
}
