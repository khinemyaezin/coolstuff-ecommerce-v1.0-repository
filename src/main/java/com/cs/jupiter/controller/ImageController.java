package com.cs.jupiter.controller;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cs.jupiter.model.interfaces.ViewResult;
import com.cs.jupiter.service.ImageService;
import com.cs.jupiter.utility.ComEnum;


@RestController
@RequestMapping("/image")
public class ImageController  extends RequestController{
	
	@Autowired
	ImageService service;
	@Autowired
	Environment env;
	@Autowired
	DataSource ds;
	
	

	@GetMapping(value = "/getpath")
	public ViewResult<String> getPath(
			HttpServletRequest req,HttpServletResponse resp){
		ViewResult<String> rtn ;
		try (Connection con = ds.getConnection()) {
			rtn =  new ViewResult<>();
			rtn.data = env.getProperty("image-path");
			rtn.status = ComEnum.ErrorStatus.Success.getCode();
		}catch (Exception e) {
			return new ViewResult<String>(ComEnum.ErrorStatus.DatabaseError.getCode(), e.getMessage());
		}
		return rtn;
	}
	
	
}
