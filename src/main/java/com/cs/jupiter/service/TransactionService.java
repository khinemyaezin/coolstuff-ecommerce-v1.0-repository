package com.cs.jupiter.service;

import java.sql.Connection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.jupiter.dao.TransactionDao;
import com.cs.jupiter.model.interfaces.RequestCredential;
import com.cs.jupiter.model.interfaces.ViewResult;
import com.cs.jupiter.model.table.Order;
import com.cs.jupiter.model.table.OrderItem;
import com.cs.jupiter.model.table.PaymentType;
import com.cs.jupiter.utility.CommonUtility;
import com.cs.jupiter.utility.KeyFactory;

@Service
public class TransactionService {
	@Autowired
	TransactionDao dao;
	
	public ViewResult<String> saveOrder(Order data,RequestCredential req,Connection conn){
		ViewResult<String> result = new ViewResult<>();
		try{
			Date today = new Date();
			if(data.getLatitude() == 0 && data.getLongitude()==0) {
				data.setLocation(null);
			}else{
				
				data.setLocation(CommonUtility.formatGeoPoint(data.getLongitude(), data.getLatitude()));
			}
			data.setId(KeyFactory.getStringId());
			data.setStatus(1);
			data.setBizStatus(1);
			data.setCdate(today);
			data.setMdate(today);
			data.setOrderedDate(today);
			ViewResult<String> hResult = 
			dao.insertOrderHeader(data, conn);
			if(!hResult.isSucces()) throw new Exception("fail_to_save_header");
			
			double totalAmount = 0;
			for(OrderItem item: data.getProducts()){
				item.setOrder(data);
				if(!saveOrderItem(item,req,conn)){
					throw new Exception("fail_to_save_detail");
				}
				totalAmount += item.getPriceTotal();
			}
			data.setTotal(totalAmount);
			ViewResult<String> resultUpdateTotalAmt = dao.updateTotalPriceOrderHeader(data, conn);
			if(!resultUpdateTotalAmt.isSucces()){
				throw new Exception(resultUpdateTotalAmt.message);
			}
			result.success();
		}catch(Exception e){
			result.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	private boolean saveOrderItem(OrderItem item,RequestCredential req,Connection conn){
		Date today = new Date();
		item.setId(KeyFactory.getStringId());
		item.setCdate(today);
		item.setMdate(today);
		item.setStatus(1);
		item.setBizStatus(1);
		ViewResult<String> result = dao.insertOrderItem(item, conn);
		return result.isSucces();
	}
	
	public ViewResult<PaymentType> getPaymentTypes(RequestCredential cred,Connection conn){
		ViewResult<PaymentType> result;
		try{
			result = dao.getPaymentTypes(conn);
		}catch(Exception e){
			result = new ViewResult<>(e);
		}
		return result;
	}
}
