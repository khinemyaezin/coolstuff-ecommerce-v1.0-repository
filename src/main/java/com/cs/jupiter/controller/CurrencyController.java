package com.cs.jupiter.controller;


import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cs.jupiter.model.interfaces.ViewResult;
import com.cs.jupiter.model.table.Currency;
import com.cs.jupiter.service.CurrencyService;
import com.cs.jupiter.utility.ComEnum;

@RestController
@RequestMapping("/currency-setup")
public class CurrencyController extends RequestController{
	@Autowired
	DataSource ds;
	
	@Autowired
	CurrencyService curService;

	
	@PostMapping(value = "/save")
	public ViewResult<Currency> saveBrand(
			@RequestBody Currency data,
			HttpServletRequest req,HttpServletResponse resp){
		try (Connection con = ds.getConnection()) {
			return curService.save(data, getReqHeader(req, resp), con);
		} catch (SQLException e) {
			return new ViewResult<Currency>(ComEnum.ErrorStatus.DatabaseError.getCode(), e.getMessage());
		}
	}
	@PostMapping(value = "/getall")
	public ViewResult<Currency> getCurrency(@RequestBody Currency cri,HttpServletRequest req, HttpServletResponse resp){
		try (Connection con = ds.getConnection()) {
			return curService.getAll(cri, getReqHeader(req,resp), con);
		} catch (SQLException e) {
			return new ViewResult<Currency>(ComEnum.ErrorStatus.DatabaseError.getCode(), e.getMessage());
		}
	}
	
	@GetMapping(value = "/getbyid/{id}")
	public ViewResult<Currency> getById(@PathVariable("id") String id,HttpServletRequest req, HttpServletResponse resp){
		try (Connection con = ds.getConnection()) {
			return curService.getById(id, getReqHeader(req,resp), con);
		} catch (SQLException e) {
			return new ViewResult<Currency>(ComEnum.ErrorStatus.DatabaseError.getCode(), e.getMessage());
		}
	}
	@PostMapping(value = "/update")
	public ViewResult<Currency> updateCurrency(@RequestBody Currency cri,HttpServletRequest req, HttpServletResponse resp){
		try (Connection con = ds.getConnection()) {
			return curService.updateById(cri, getReqHeader(req,resp), con);
		} catch (SQLException e) {
			return new ViewResult<Currency>(ComEnum.ErrorStatus.DatabaseError.getCode(), e.getMessage());
		}
	}
	@PostMapping(value = "/delete")
	public ViewResult<Currency> deleteCurrency(@RequestBody Currency cri,HttpServletRequest req, HttpServletResponse resp){
		try (Connection con = ds.getConnection()) {
			return curService.deleteById(cri, getReqHeader(req,resp), con);
		} catch (SQLException e) {
			return new ViewResult<Currency>(ComEnum.ErrorStatus.DatabaseError.getCode(), e.getMessage());
		}
	}

}
