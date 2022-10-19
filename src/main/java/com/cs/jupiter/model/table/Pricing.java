package com.cs.jupiter.model.table;


import com.cs.jupiter.model.interfaces.ViewCredential;

public class Pricing extends ViewCredential{
	private int rowNumber;
	private Currency currency;
	private UomStock uomStock;
	private double price;
	private double rate;
	
	public Pricing(){
		this.rowNumber = 0;
		this.price = 0.0;
		this.rate = 0.0;
	}
	
	public int getRowNumber() {
		return rowNumber;
	}
	public Currency getCurrency() {
		return currency;
	}
	public UomStock getUomStock() {
		return uomStock;
	}
	public double getPrice() {
		return price;
	}
	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	public void setUomStock(UomStock uomStock) {
		this.uomStock = uomStock;
	}
	public void setPrice(double price) {
		this.price = price;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}
}
