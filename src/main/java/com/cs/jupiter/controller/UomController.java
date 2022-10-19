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
import com.cs.jupiter.model.table.Uom;
import com.cs.jupiter.service.UomService;
import com.cs.jupiter.utility.ComEnum;

@RestController
@RequestMapping("/uom-setup")
public class UomController extends RequestController{
	@Autowired
	DataSource ds;
	
	@Autowired
	UomService uomService;

	
	@PostMapping(value = "/save")
	public ViewResult<Uom> saveBrand(
			@RequestBody Uom data,
			HttpServletRequest req,HttpServletResponse resp){
		try (Connection con = ds.getConnection()) {
			return uomService.save(data, getReqHeader(req, resp), con);
		} catch (SQLException e) {
			return new ViewResult<Uom>(ComEnum.ErrorStatus.DatabaseError.getCode(), e.getMessage());
		}
	}
	@PostMapping(value = "/getall")
	public ViewResult<Uom> getUom(@RequestBody Uom cri,HttpServletRequest req, HttpServletResponse resp){
		try (Connection con = ds.getConnection()) {
			return uomService.getAll(cri, getReqHeader(req,resp), con);
		} catch (SQLException e) {
			return new ViewResult<Uom>(ComEnum.ErrorStatus.DatabaseError.getCode(), e.getMessage());
		}
	}
	
	@GetMapping(value = "/getbyid/{id}")
	public ViewResult<Uom> getById(@PathVariable("id") String id,HttpServletRequest req, HttpServletResponse resp){
		try (Connection con = ds.getConnection()) {
			return uomService.getById(id, getReqHeader(req,resp), con);
		} catch (SQLException e) {
			return new ViewResult<Uom>(ComEnum.ErrorStatus.DatabaseError.getCode(), e.getMessage());
		}
	}
	@PostMapping(value = "/update")
	public ViewResult<Uom> updateUom(@RequestBody Uom cri,HttpServletRequest req, HttpServletResponse resp){
		try (Connection con = ds.getConnection()) {
			return uomService.updateById(cri, getReqHeader(req,resp), con);
		} catch (SQLException e) {
			return new ViewResult<Uom>(ComEnum.ErrorStatus.DatabaseError.getCode(), e.getMessage());
		}
		
	}
	@PostMapping(value = "/delete")
	public ViewResult<Uom> deleteUom(@RequestBody Uom cri,HttpServletRequest req, HttpServletResponse resp){
		try (Connection con = ds.getConnection()) {
			return uomService.deleteById(cri, getReqHeader(req,resp), con);
		} catch (SQLException e) {
			return new ViewResult<Uom>(ComEnum.ErrorStatus.DatabaseError.getCode(), e.getMessage());
		}
	}

}
