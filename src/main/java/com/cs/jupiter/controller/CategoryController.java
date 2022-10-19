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
import com.cs.jupiter.model.jun.SurroundCategory;
import com.cs.jupiter.model.table.Category;
import com.cs.jupiter.service.CategoryService;

@RestController
@RequestMapping("/category")
public class CategoryController extends RequestController{
	@Autowired
	DataSource ds;
	
	@Autowired
	CategoryService catService;
	
	@GetMapping(value = "/bydepth/{depth}")
	public ViewResult<Category> getCategoryByDepth(@PathVariable("depth") int depth,HttpServletRequest req, HttpServletResponse resp){
		try (Connection con = ds.getConnection()) {
			return catService.getCategoryByDepth(depth, getReqHeader(req,resp),con);
		} catch (SQLException e) {
			return new ViewResult<Category>(e);
		}
	}
	
	@PostMapping(value = "/get/leaf")
	public ViewResult<Category> getCateogryLeaf(
			@RequestBody Category data,
			HttpServletRequest req,HttpServletResponse resp){
		ViewResult<Category> rs;
		try (Connection con = ds.getConnection()) {
		
			rs = catService.getCategoryLeaf(data,  getReqHeader(req, resp), con);
		} catch (SQLException e) {
			rs  = new ViewResult<Category>();
			rs.error(e.getMessage());
		}
		return rs;
	}
	
	@PostMapping(value = "/top-layer")
	public ViewResult<Category> getUpperLayerById(
			@RequestBody Category data,
			HttpServletRequest req,HttpServletResponse resp){
		ViewResult<Category> rs;
		try (Connection con = ds.getConnection()) {
			rs = catService.getTopLayer(data,  getReqHeader(req, resp), con);
		} catch (SQLException e) {
			rs = new ViewResult<>(e);
		}
		return rs;
	}
	@PostMapping(value = "/surround-category")
	public ViewResult<SurroundCategory> getSurroundCategory(
			@RequestBody Category data,
			HttpServletRequest req,HttpServletResponse resp){
		ViewResult<SurroundCategory> rs;
		try (Connection con = ds.getConnection()) {
			rs = catService.getSurroundCategory(data,  getReqHeader(req, resp), con);
		} catch (SQLException e) {
			rs = new ViewResult<>(e);
		}
		return rs;
	}

	
	@PostMapping(value = "/save")
	public ViewResult<Category> saveBrand(
			@RequestBody Category data,
			HttpServletRequest req,HttpServletResponse resp){
		ViewResult<Category> rs;
		try (Connection con = ds.getConnection()) {
			con.setAutoCommit(false);
			rs =  catService.save(data, getReqHeader(req, resp),con);
			rs.completeTransaction(con);
		} catch (SQLException e) {
			return new ViewResult<Category>(e);
		}
		return rs;
	}
	@PostMapping(value = "/getall")
	public ViewResult<Category> getCategory(@RequestBody Category cri,HttpServletRequest req, HttpServletResponse resp){
		try (Connection con = ds.getConnection()) {
			return catService.getAll(cri, getReqHeader(req,resp),con);
		} catch (SQLException e) {
			return new ViewResult<Category>(e);
		}
	}
	@PostMapping(value = "/getchild-byparent")
	public ViewResult<Category> getChildNodeByParentNode(@RequestBody Category cri,HttpServletRequest req, HttpServletResponse resp){
		try (Connection con = ds.getConnection()) {
			return catService.getOneLayerOfGivenId(cri, getReqHeader(req,resp),con);
		} catch (SQLException e) {
			return new ViewResult<Category>(e);
		}
	}
	@GetMapping(value = "/search/{title}")
	public ViewResult<Category> searchCategoryLeaf(@PathVariable("title") String title,HttpServletRequest req, HttpServletResponse resp){
		try (Connection con = ds.getConnection()) {
			return catService.searchCategoryLeaf(title, getReqHeader(req,resp),con);
		} catch (SQLException e) {
			return new ViewResult<Category>(e);
		}
	}
	
	
	@GetMapping(value = "/generate-leaf")
	public ViewResult<String> getById(HttpServletRequest req, HttpServletResponse resp){
		ViewResult<String> result;
		try (Connection con = ds.getConnection()) {
			con.setAutoCommit(false);
			result = catService.generateLeaf(getReqHeader(req,resp),con);
			result.completeTransaction(con);
		} catch (SQLException e) {
			result =  new ViewResult<String>(e);
		}
		return result;
	}
	
	

}
