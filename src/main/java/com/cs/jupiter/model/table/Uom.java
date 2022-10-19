package com.cs.jupiter.model.table;


import com.cs.jupiter.model.interfaces.ViewCredential;

public class Uom extends ViewCredential{
	private int rowNumber;

	public Uom(){
		this.rowNumber = 0;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	
	
}
