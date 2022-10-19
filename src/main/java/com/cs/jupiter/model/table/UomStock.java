package com.cs.jupiter.model.table;

import com.cs.jupiter.model.interfaces.ViewCredential;

public class UomStock extends ViewCredential{

	private int rowNumber;
	private double specficPrice;	
	private int base;
	private PriceType priceType;
	private Product stock;
	private Uom uom;
	private int ratio;
	private Currency currency;
	private double price;
	private double rate;
	
	public UomStock(){
		this.specficPrice = 0.0;
		this.base = -1;
		this.ratio = 0;
		this.price = 0.0;
		this.rate = 0.0;
	}
	
	public int getRowNumber() {
		return rowNumber;
	}
	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}
	
	public double getSpecficPrice() {
		return specficPrice;
	}
	public void setSpecficPrice(double specficPrice) {
		this.specficPrice = specficPrice;
	}

	public Product getStock() {
		return stock;
	}
	public Uom getUom() {
		return uom;
	}
	public void setStock(Product stock) {
		this.stock = stock;
	}
	public void setUom(Uom uom) {
		this.uom = uom;
	}
	public PriceType getPriceType() {
		return priceType;
	}
	public void setPriceType(PriceType priceType) {
		this.priceType = priceType;
	}

	public int getBase() {
		return base;
	}

	public void setBase(int base) {
		this.base = base;
	}

	public int getRatio() {
		return ratio;
	}

	public void setRatio(int ratio) {
		this.ratio = ratio;
	}

	public Currency getCurrency() {
		return currency;
	}

	public double getPrice() {
		return price;
	}

	public double getRate() {
		return rate;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}
	
	
}
