package com.cs.jupiter.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import com.cs.jupiter.model.interfaces.RequestCredential;

@CrossOrigin(origins ="http://localhost:4200",allowedHeaders = "*", allowCredentials = "true")
public class RequestController {

	public RequestCredential getReqHeader(HttpServletRequest request,HttpServletResponse resp){
		return (RequestCredential) request.getAttribute("credential");		
	}
	
}
