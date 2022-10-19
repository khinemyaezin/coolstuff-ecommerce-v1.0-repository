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
import com.cs.jupiter.model.table.Brand;
import com.cs.jupiter.service.BrandService;
import com.cs.jupiter.utility.ComEnum;

@RestController
@RequestMapping("/brand")
public class BrandController extends RequestController{
	@Autowired
	DataSource ds;
	
	@Autowired
	BrandService service;
	
	@PostMapping(value = "/get")
	public ViewResult<Brand> getBrand(
			@RequestBody Brand data,
			HttpServletRequest req,HttpServletResponse resp){
		ViewResult<Brand> result ;
		try (Connection con = ds.getConnection()) {
			result =  service.getBrandTable(data, getReqHeader(req, resp), con);
			return result;
		} catch (SQLException e) {
			return new ViewResult<Brand>(ComEnum.ErrorStatus.DatabaseError.getCode(), e.getMessage());
		}
	}
	
	@PostMapping(value = "/get/new-register-count")
	public ViewResult<Integer> getNewRegisterCount(
			@RequestBody Brand data,
			HttpServletRequest req,HttpServletResponse resp){
		ViewResult<Integer> result ;
		try (Connection con = ds.getConnection()) {
			result =  service.getNewRegisterCount(data, getReqHeader(req, resp), con);
			return result;
		} catch (SQLException e) {
			return new ViewResult<Integer>(ComEnum.ErrorStatus.DatabaseError.getCode(), e.getMessage());
		}
	}
	
	@GetMapping(value = "/get/detail/{code}")
	public ViewResult<Brand> getBrandById(
			@PathVariable("code") String data,
			HttpServletRequest req,HttpServletResponse resp){
		ViewResult<Brand> result ;
		try (Connection con = ds.getConnection()) {
			Brand b = new Brand();
			b.setCode(data);
			result =  service.getBrandById(b, getReqHeader(req, resp), con);
			return result;
		} catch (SQLException e) {
			return new ViewResult<Brand>(ComEnum.ErrorStatus.DatabaseError.getCode(), e.getMessage());
		}
	}
	
	@PostMapping(value = "/register")
	public ViewResult<Brand> saveBrand(
			@RequestBody Brand data,
			HttpServletRequest req,HttpServletResponse resp){
		try (Connection con = ds.getConnection()) {
			con.setAutoCommit(false);
			ViewResult<Brand> result =  service.saveBrand(data, getReqHeader(req, resp), con);
			result.completeTransaction(con);
			return result;
		} catch (SQLException e) {
			return new ViewResult<Brand>(e);
		}
	}
	
	@PostMapping(value = "/update")
	public ViewResult<Brand> updateBrand(
			@RequestBody Brand data,
			HttpServletRequest req,HttpServletResponse resp){
		ViewResult<Brand> result ;
		try (Connection con = ds.getConnection()) {
			con.setAutoCommit(false);
			result =  service.updateBrand(data, getReqHeader(req, resp), con);
			if(result.status == ComEnum.ErrorStatus.Success.getCode()) {
				con.commit();
			}
			else {
				con.rollback();
			}
			return result;
		} catch (SQLException e) {
			return new ViewResult<Brand>(ComEnum.ErrorStatus.DatabaseError.getCode(), e.getMessage());
		}
	}
	
	@GetMapping(value = "/update/change-status/{id}/{status}")
	public ViewResult<Brand> changeStatus(@PathVariable("id") String id,@PathVariable("status") String status,HttpServletRequest req, HttpServletResponse resp){
		ViewResult<Brand> result ;
		try (Connection con = ds.getConnection()) {
			con.setAutoCommit(false);
			result =  service.changeStatus(status,id, getReqHeader(req,resp),con);
			if(result.status == ComEnum.ErrorStatus.Success.getCode()) {
				con.commit();
			}
			else {
				con.rollback();
			}
			return result;
		} catch (SQLException e) {
			return new ViewResult<Brand>(ComEnum.ErrorStatus.DatabaseError.getCode(), e.getMessage());
		}
	}
	
	@GetMapping(value = "/delete/{id}")
	public ViewResult<Brand> delete(@PathVariable("id") String id,@PathVariable("status") String status,HttpServletRequest req, HttpServletResponse resp){
		ViewResult<Brand> result ;
		try (Connection con = ds.getConnection()) {
			con.setAutoCommit(false);
			result =  service.delete(id, getReqHeader(req,resp),con);
			if(result.status == ComEnum.ErrorStatus.Success.getCode()) {
				con.commit();
			}
			else {
				con.rollback();
			}
			return result;
		} catch (SQLException e) {
			return new ViewResult<Brand>(ComEnum.ErrorStatus.DatabaseError.getCode(), e.getMessage());
		}
	}
}
