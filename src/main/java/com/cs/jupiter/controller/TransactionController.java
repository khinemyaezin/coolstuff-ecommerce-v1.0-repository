package com.cs.jupiter.controller;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cs.jupiter.model.interfaces.ViewResult;
import com.cs.jupiter.model.table.Order;
import com.cs.jupiter.model.table.PaymentType;
import com.cs.jupiter.service.TransactionService;

@RestController
@RequestMapping("/transaction")
public class TransactionController extends RequestController{
	@Autowired
	DataSource ds;
	
	@Autowired
	TransactionService service;
	
	@PostMapping(value = "/order/place")
	public ViewResult<String> save(@RequestBody Order data,
			HttpServletRequest req,HttpServletResponse resp){
		ViewResult<String> result;
		try (Connection con = ds.getConnection()) {
			con.setAutoCommit(false);
			result= service.saveOrder(data, getReqHeader(req,resp), con);
			result.completeTransaction(con);
		} catch (SQLException e) {
			result =  new ViewResult<String>(e);
		}
		return result;
	}
	
	@GetMapping(value="/payment-type/get")
	public ViewResult<PaymentType> getPaymentTypes(HttpServletRequest req,HttpServletResponse resp){
		ViewResult<PaymentType> result;
		try(Connection conn = ds.getConnection()){
			result = service.getPaymentTypes(getReqHeader(req, resp), conn);
		}catch(Exception e){
			result = new ViewResult<PaymentType>(e);
		}
		return result;
	}
}
