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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.cs.jupiter.model.interfaces.Auth;
import com.cs.jupiter.model.interfaces.ViewResult;
import com.cs.jupiter.model.table.Nrc;
import com.cs.jupiter.model.table.NrcDistrict;
import com.cs.jupiter.model.table.User;
import com.cs.jupiter.model.table.UserType;
import com.cs.jupiter.service.UserService;
import com.cs.jupiter.utility.ComEnum;

@RestController
@RequestMapping("/account")
public class AccountController extends RequestController{
	@Autowired
	DataSource ds;
	
	@Autowired
	UserService userService;
	
	@PostMapping(value = "/update")
	public ViewResult<User> updateUser(
			@RequestBody User data,
			HttpServletRequest req,HttpServletResponse resp){
		ViewResult<User> result ;
		try(Connection conn = ds.getConnection()){
			conn.setAutoCommit(false);
			
			result = userService.updateAccountUser(data,getReqHeader(req, resp), conn);
			if(result.isSucces()) {
				conn.commit();
			} else{
				conn.rollback();
			}
			return result;
		} catch (SQLException e) {
			return new ViewResult<User>(e);
		}
	}
	@PostMapping(value = "/register")
	public ViewResult<User> registerUser(
			@RequestBody User data,
			HttpServletRequest req,HttpServletResponse resp){
		ViewResult<User> result ;
		try(Connection conn = ds.getConnection()){
			conn.setAutoCommit(false);
			result = userService.registerUser(data,getReqHeader(req, resp), conn);
			result.completeTransaction(conn);
		} catch (SQLException e) {
			return new ViewResult<User>(e);
		}
		return result;
	}
	@PostMapping(value = "/update/profile")
	public ViewResult<User> updateUserProfile(
			@RequestBody User data,
			HttpServletRequest req,HttpServletResponse resp){
		ViewResult<User> result ;
		try(Connection conn = ds.getConnection()){
			conn.setAutoCommit(false);
			result = userService.updateAccountUserProfile(data,getReqHeader(req, resp), conn);
			if(result.isSucces()) {
				conn.commit();
			} else{
				conn.rollback();
			}
			return result;
		} catch (SQLException e) {
			return new ViewResult<User>(ComEnum.ErrorStatus.DatabaseError.getCode(), e.getMessage());
		}
	}
	@PostMapping(value = "/update/password")
	public ViewResult<User> changePassword(
			@RequestBody Auth data,
			HttpServletRequest req,HttpServletResponse resp){
		ViewResult<User> result ;
		try(Connection conn = ds.getConnection()){
			conn.setAutoCommit(false);
			result = userService.changePassword(data,getReqHeader(req, resp), conn);
			if(result.isSucces()) {
				conn.commit();
			} else{
				conn.rollback();
			}
			return result;
		} catch (SQLException e) {
			return new ViewResult<User>(ComEnum.ErrorStatus.DatabaseError.getCode(), e.getMessage());
		}
	}
	
	@PostMapping(value = "/get")
	public ViewResult<User> getUser(
			@RequestBody User data,
			HttpServletRequest req,HttpServletResponse resp) {
		ViewResult<User> result ;
		try(Connection conn = ds.getConnection()){
			result = userService.getUser(data,getReqHeader(req, resp), conn);
			return result;
		} catch (Exception e) {
			return new ViewResult<User>(e);
		}
	}
	
	@GetMapping(value = "/user-type/get")
	public ViewResult<UserType> getUserType(
			@RequestParam(required = false,name = "code") String code,
			HttpServletRequest req,HttpServletResponse resp){
		ViewResult<UserType> result ;
		try(Connection conn = ds.getConnection()){
			result = userService.getUserTypeSetup(code,getReqHeader(req, resp), conn);
			return result;
		} catch (SQLException e) {
			return new ViewResult<UserType>(ComEnum.ErrorStatus.DatabaseError.getCode(), e.getMessage());
		}
	}
	
	@GetMapping(value = "/nrc/state/get")
	public ViewResult<Nrc> getNrcState(HttpServletRequest req,HttpServletResponse resp){
		ViewResult<Nrc> result ;
		try(Connection conn = ds.getConnection()){
			result = userService.getNrcState(getReqHeader(req, resp), conn);
			return result;
		} catch (SQLException e) {
			return new ViewResult<Nrc>(ComEnum.ErrorStatus.DatabaseError.getCode(), e.getMessage());
		}
	}
	
	@GetMapping(value = "/nrc/district/get/{stateid}")
	public ViewResult<NrcDistrict> getNrcState(
			@PathVariable("stateid") String id,
			HttpServletRequest req,HttpServletResponse resp){
		ViewResult<NrcDistrict> result ;
		try(Connection conn = ds.getConnection()){
			result = userService.getNrcDistrict(id,getReqHeader(req, resp), conn);
			return result;
		} catch (SQLException e) {
			return new ViewResult<NrcDistrict>(ComEnum.ErrorStatus.DatabaseError.getCode(), e.getMessage());
		}
	}
}
