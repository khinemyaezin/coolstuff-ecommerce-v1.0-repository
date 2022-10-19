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
import com.cs.jupiter.model.table.PackSize;
import com.cs.jupiter.service.PackSizeService;
import com.cs.jupiter.utility.ComEnum;

@RestController
@RequestMapping("/packsize-setup")
public class PackSizeController extends RequestController{
	@Autowired
	DataSource ds;
	@Autowired
	PackSizeService ptService;

	
	@PostMapping(value = "/save")
	public ViewResult<PackSize> saveBrand(
			@RequestBody PackSize data,
			HttpServletRequest req,HttpServletResponse resp){
		try (Connection con = ds.getConnection()) {
			return ptService.save(data, getReqHeader(req, resp),con);
		} catch (SQLException e) {
			return new ViewResult<PackSize>(ComEnum.ErrorStatus.DatabaseError.getCode(), e.getMessage());
		}
	}
	@PostMapping(value = "/getall")
	public ViewResult<PackSize> getPackSize(@RequestBody PackSize cri,HttpServletRequest req, HttpServletResponse resp){
		try (Connection con = ds.getConnection()) {
			return ptService.getAll(cri, getReqHeader(req,resp),con);
		} catch (SQLException e) {
			return new ViewResult<PackSize>(ComEnum.ErrorStatus.DatabaseError.getCode(), e.getMessage());
		}
	}
	
	@GetMapping(value = "/getbyid/{id}")
	public ViewResult<PackSize> getById(@PathVariable("id") String id,HttpServletRequest req, HttpServletResponse resp){
		try (Connection con = ds.getConnection()) {
			return ptService.getById(id, getReqHeader(req,resp),con);
		} catch (SQLException e) {
			return new ViewResult<PackSize>(ComEnum.ErrorStatus.DatabaseError.getCode(), e.getMessage());
		}
	}
	@PostMapping(value = "/update")
	public ViewResult<PackSize> updatePackSize(@RequestBody PackSize cri,HttpServletRequest req, HttpServletResponse resp){
		try (Connection con = ds.getConnection()) {
			return ptService.updateById(cri, getReqHeader(req,resp),con);
		} catch (SQLException e) {
			return new ViewResult<PackSize>(ComEnum.ErrorStatus.DatabaseError.getCode(), e.getMessage());
		}
		
	}
	@PostMapping(value = "/delete")
	public ViewResult<PackSize> deletePackSize(@RequestBody PackSize cri,HttpServletRequest req, HttpServletResponse resp){
		try (Connection con = ds.getConnection()) {
			return ptService.deleteById(cri, getReqHeader(req,resp),con);
		} catch (SQLException e) {
			return new ViewResult<PackSize>(ComEnum.ErrorStatus.DatabaseError.getCode(), e.getMessage());
		}
	}

}
