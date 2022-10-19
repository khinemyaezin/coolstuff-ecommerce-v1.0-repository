package com.cs.jupiter.controller;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cs.jupiter.model.interfaces.ViewResult;
import com.cs.jupiter.model.jun.MyanmarGeometry;
import com.cs.jupiter.model.table.Geometry;
import com.cs.jupiter.service.GeometryService;

@RestController
@RequestMapping("/geo")
public class GeometryController extends RequestController{
	@Autowired
	DataSource ds;
	
	@Autowired
	GeometryService service;
	
	@GetMapping(value = "/location/locate/{lat}/{lng}")
	public ViewResult<MyanmarGeometry> save(@PathVariable("lat") double latitude,@PathVariable("lng") double longitude,
			HttpServletRequest req,HttpServletResponse resp){
		ViewResult<MyanmarGeometry> result;
		try (Connection con = ds.getConnection()) {
			con.setAutoCommit(false);
			result= service.locatePosition(latitude,longitude, getReqHeader(req,resp), con);
			result.completeTransaction(con);
		} catch (SQLException e) {
			result =  new ViewResult<MyanmarGeometry>(e);
		}
		return result;
	}
	
	@GetMapping(value = "/reference/ward/get/{townpcode}")
	public ViewResult<Geometry> save(@PathVariable("townpcode") String townpcode,
			HttpServletRequest req,HttpServletResponse resp){
		ViewResult<Geometry> result;
		try (Connection con = ds.getConnection()) {
			result = service.getWard(townpcode, getReqHeader(req,resp), con);
		} catch (SQLException e) {
			result =  new ViewResult<Geometry>(e);
		}
		return result;
	}
}
