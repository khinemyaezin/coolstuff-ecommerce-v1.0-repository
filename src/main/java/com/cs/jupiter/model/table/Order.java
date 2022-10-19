package com.cs.jupiter.model.table;

import java.util.Date;
import java.util.List;

import com.cs.jupiter.model.interfaces.ViewCredential;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Order extends ViewCredential{
	private Date orderedDate;
	private String userAddress;
	private String userName;
	private String userPhoneNo;
	private String userEmail;
	private double longitude;
	private double latitude;
	@JsonIgnore
	private String location;
	private List<OrderItem> products;
	private double total;
	private PaymentType paymentType;
	private String comment;
	private String deliveryTime;
	public Order(){
		
	}
	public Order(String id){
		this.setId(id);
	}
	public Date getOrderedDate() {
		return orderedDate;
	}
	public String getUserAddress() {
		return userAddress;
	}
	public String getUserName() {
		return userName;
	}
	public String getUserPhoneNo() {
		return userPhoneNo;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setOrderedDate(Date orderedDate) {
		this.orderedDate = orderedDate;
	}
	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setUserPhoneNo(String userPhoneNo) {
		this.userPhoneNo = userPhoneNo;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public List<OrderItem> getProducts() {
		return products;
	}
	
	public void setProducts(List<OrderItem> products) {
		this.products = products;
	}
	public double getLongitude() {
		return longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public PaymentType getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}
	public String getComment() {
		return comment;
	}
	public String getDeliveryTime() {
		return deliveryTime;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	
}
