package com.cs.jupiter.controller;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cs.jupiter.model.interfaces.ViewResult;
import com.cs.jupiter.model.jun.ProductDetail;
import com.cs.jupiter.model.jun.ProductFilter;
import com.cs.jupiter.model.table.ProductVariantOptionHeader;
import com.cs.jupiter.service.ProductService;
import com.cs.jupiter.service.VariantService;
import com.cs.jupiter.utility.AuthGuard;

@RestController
@RequestMapping("/market")
public class ProductMarketController  extends RequestController{
	@Autowired
	DataSource ds;

	@Autowired
	VariantService variantService;

	@Autowired
	ProductService stockService;
	
	@Autowired
	AuthGuard auth;
	
	@GetMapping(value = "/product")
	public ViewResult<ProductDetail> getProduct(@RequestParam(name = "keyword",required = false) String keyword,
			@RequestParam(name = "depid",required = false) String departmentId,
			@RequestParam(name = "option1",required = false) String optionDetail1Id,
			@RequestParam(name = "option2",required = false) String optionDetail2Id,
			@RequestParam(name = "option3",required = false) String optionDetail3Id,
			@RequestParam(name = "varid",required = false) String variantId,
			@RequestParam(name = "prodid",required = false) String productId,
			HttpServletRequest req, HttpServletResponse resp) {
		ViewResult<ProductDetail> res;
		try (Connection con = ds.getConnection()) {
			ProductFilter filter = new ProductFilter();
			filter.setSearchKeyWord(keyword);
			filter.setCategoryId(departmentId);
			filter.setOptionDetail1Id(optionDetail1Id);
			filter.setOptionDetail2Id(optionDetail2Id);
			filter.setOptionDetail3Id(optionDetail3Id);
			filter.setId(variantId);
			filter.setParentId(productId);
			res = stockService.getProducts(filter, getReqHeader(req, resp), con);
			return res;
		} catch (SQLException e) {
			return new ViewResult<>(e);
		}

	}
	
	
	
	@GetMapping(value = "/options/get")
	public ViewResult<ProductVariantOptionHeader> getProduct(@RequestParam(name = "prodId",required = true) String productId,
			HttpServletRequest req, HttpServletResponse resp) {
		ViewResult<ProductVariantOptionHeader> res;
		try (Connection con = ds.getConnection()) {
			res = stockService.getProductVariantOptions(productId, getReqHeader(req, resp), con);
		} catch (SQLException e) {
			res =  new ViewResult<>(e);
		}
		return res;
	}
	
}
