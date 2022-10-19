package com.cs.jupiter.controller;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cs.jupiter.model.interfaces.ViewResult;
import com.cs.jupiter.model.table.ImageData;
import com.cs.jupiter.service.ImageService;
import com.cs.jupiter.utility.ComEnum;

@RestController
@RequestMapping("/upload")
public class FileUploadController extends RequestController {
	@Autowired
	DataSource ds;

	@Autowired
	ImageService service;
	
	@Autowired
	Environment env;

	@PostMapping(value = "/fileupload", consumes = { "multipart/form-data" })
	public ViewResult<ImageData> uploadFile(
			@RequestParam("file") MultipartFile[] fileList,
			@RequestParam("pid") String pid,
			HttpServletRequest req, HttpServletResponse resp) {
		try (Connection con = ds.getConnection()) {
			return service.prepareToSave(fileList,pid,getReqHeader(req, resp), con);
		} catch (SQLException e) {
			return new ViewResult<ImageData>(ComEnum.ErrorStatus.DatabaseError.getCode(), e.getMessage());
		}
	}


}
