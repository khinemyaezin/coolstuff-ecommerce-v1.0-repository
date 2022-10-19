package com.cs.jupiter.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import com.cs.jupiter.model.interfaces.ViewResult;
import com.cs.jupiter.model.table.Brand;
import com.cs.jupiter.model.table.ImageData;
import com.cs.jupiter.model.table.User;
import com.cs.jupiter.model.table.UserType;
import com.cs.jupiter.utility.CommonUtility;
import com.cs.jupiter.utility.PrepareQuery;
import com.cs.jupiter.utility.PrepareQuery.Operator;
import com.cs.jupiter.utility.PrepareQuery.Type;

@Repository
public class BrandDao {
	@Autowired
	Environment env;
	public ViewResult<Brand> saveBrand(Brand data, Connection conn) {
		ViewResult<Brand> rtn = new ViewResult<>();
		try {
			String sql = "INSERT INTO public.brand(id, code, name, status, cdate, mdate, profile_path, cover_path)"
					+ " VALUES (?, ?, ?, ?, ?::date, ?::date,?,?);";
			PreparedStatement stmt = conn.prepareStatement(sql);
			int i = 1;
			stmt.setLong(i++, Long.parseLong(data.getId()));
			stmt.setString(i++, data.getCode());
			stmt.setString(i++, data.getName());
			stmt.setInt(i++, data.getStatus());
			stmt.setString(i++, CommonUtility.convertDate_2db(data.getCdate()));
			stmt.setString(i++, CommonUtility.convertDate_2db(data.getMdate()));
			stmt.setString(i++, data.getProfileImage().getName());
			stmt.setString(i++, data.getCoverImage().getName());
			stmt.executeUpdate();
			rtn.success();
		} catch (Exception e) {
			rtn.error(e.getMessage());
			e.printStackTrace();
		}
		return rtn;
	}

	public ViewResult<Brand> updateBrand(Brand data, Connection conn) {
		ViewResult<Brand> rtn = new ViewResult<>();
		try {
			String sql = "UPDATE public.brand";
			PrepareQuery q = new PrepareQuery(sql);
			q.addSetClause("name", data.getName(), Operator.EQUAL, Type.VARCHAR);
			q.addSetClause("status", data.getStatus(), Operator.EQUAL, Type.NUMBER);

			q.addSetClause("mdate", data.getMdate(), Operator.EQUAL,
					Type.DATE);
			
			q.addWhereClause("id", data.getId(), Operator.EQUAL, Type.ID);
			
			PreparedStatement stmt = q.getPrepareStatement(conn);
			stmt.executeUpdate();
			rtn.success();
		} catch (Exception e) {
			rtn.error(e.getMessage());
			e.printStackTrace();
		}
		return rtn;
	}

	public ViewResult<Brand> deleteBrand(String data, Connection conn) {
		ViewResult<Brand> rtn = new ViewResult<>();
		try {
			String sql = "DELETE FROM public.brand";
			PrepareQuery q = new PrepareQuery(sql);
			q.addWhereClause("id", Long.parseLong(data), Operator.EQUAL, Type.ID);
			
			PreparedStatement stmt = q.getPrepareStatement(conn);
			stmt.executeUpdate();
			rtn.success();
		} catch (Exception e) {
			rtn.error(e.getMessage());
			e.printStackTrace();
		}
		return rtn;
	}

	public ViewResult<Brand> getBrandTable(Brand data,Connection conn){
		ViewResult<Brand> rtn = new ViewResult<>();
		try{
			String sql = "select count(*) over() as total,"
					+ "brand.id as brand_id, brand.code as brand_code, brand.name as brand_name, brand.status as brand_status, brand.cdate brand_cdate, brand.mdate as brand_mdate,"
					+ "myuser.first_name as user_fname,myuser.last_name as user_lname,myuser.nrc as user_nrc,myuser.cdate user_cdate,myuser.mdate user_mdate,myuser.status as user_status,myuser.email as user_email,myuser.phone_no as user_phoneno,myuser.address as user_address,"
					+ "usertype.name as usertype"
					+ " from public.brand"
					+ " inner join myuser on myuser.fk_brand = brand.id"
					+ " inner join user_type usertype on usertype.id = myuser.fk_usertype";
			PrepareQuery q = new PrepareQuery(sql);
			q.addWhereClause("brand.id", data.getId(), Operator.EQUAL, Type.ID);
			q.addWhereClause("brand.code", data.getCode(),Operator.LIKE_ALL, Type.VARCHAR);
			q.addWhereClause("brand.name", data.getName(),Operator.LIKE_ALL, Type.VARCHAR);
			q.addWhereClause("brand.status", data.getStatus(),Operator.EQUAL, Type.NUMBER);
			if(data.getUser(0) != null){
				q.addWhereClause("myuser.first_name", data.getUser(0).getFirstName(),Operator.LIKE_ALL, Type.VARCHAR);
				q.addWhereClause("myuser.last_name", data.getUser(0).getLastName(),Operator.LIKE_ALL, Type.VARCHAR);
				q.addWhereClause("myuser.email", data.getUser(0).getEmail(),Operator.LIKE_ALL, Type.VARCHAR);
				q.addWhereClause("myuser.phone_no", data.getUser(0).getPhoneNo(),Operator.LIKE_ALL, Type.VARCHAR);
				q.addWhereClause("myuser.status", data.getUser(0).getStatus(),Operator.EQUAL, Type.NUMBER);
				if(data.getUser(0).getUserType()!=null)
					q.addWhereClause("usertype.id", data.getUser(0).getUserType().getId(), Operator.EQUAL, Type.ID);
				
			}
			PreparedStatement stmt =  q.getPrepareStatement(conn,data.getCurrentRow(), data.getMaxRowsPerPage(),"",data.getOrderby(), data.getSorting());
			CommonUtility.outputLog(q.getSql(), env);
			ResultSet rs = stmt.executeQuery();
			Brand b = null;
			User u = null;
			UserType utype = null;
			while(rs.next()){
				if(rtn.list.size() == 0) {
					rtn.totalItem = rs.getInt("total");
				}
				b = new Brand();
				b.setId(rs.getString("brand_id"));
				b.setCode(rs.getString("brand_code"));
				b.setName(rs.getString("brand_name"));
				b.setStatus(rs.getInt("brand_status"));
				b.setCdate(rs.getDate("brand_cdate"));
				b.setMdate(rs.getDate("brand_mdate"));
				u = new User();
				u.setFirstName(rs.getString("user_fname"));
				u.setLastName(rs.getString("user_lname"));
				u.setNrc(rs.getString("user_nrc"));
				u.setCdate(rs.getDate("user_cdate"));
				u.setMdate(rs.getDate("user_mdate"));
				u.setStatus(rs.getInt("user_status"));
				u.setEmail(rs.getString("user_email"));
				u.setPhoneNo(rs.getString("user_phoneno"));
				u.setAddress(rs.getString("user_address"));
				utype = new UserType();
				utype.setName(rs.getString("usertype"));
				b.setAccUsers(new ArrayList<User>());
				b.setAccUser(u);
				rtn.list.add(b);
			}
			
			rtn.success();
		} catch (Exception e) {
			rtn.error(e.getMessage());
			e.printStackTrace();
		}
		return rtn;
	}
	
	public ViewResult<Brand> getBrandById(Brand data,Connection conn){
		ViewResult<Brand> rtn = new ViewResult<>();
		System.out.println(data.getId() + ":" +data.getCode());
		try{
			String sql = "select"
					+ " brand.id as brand_id, brand.code as brand_code, brand.name as brand_name, brand.status as brand_status, brand.cdate brand_cdate, brand.mdate as brand_mdate,"
					+ "brand.profile_path,brand.cover_path "
					+ " from public.brand";
			PrepareQuery q = new PrepareQuery(sql);
			q.addWhereClause("brand.id", data.getId(), Operator.EQUAL, Type.ID);
			q.addWhereClause("brand.code", data.getCode(),Operator.EQUAL, Type.VARCHAR);
			q.addWhereClause("brand.name", data.getName(),Operator.LIKE_ALL, Type.VARCHAR);
			q.addWhereClause("brand.status", data.getStatus(),Operator.EQUAL, Type.NUMBER);
//			if(!data.getFromDate().equals("") && !data.getToDate().equals("")){
//				q.addBetween("brand.cdate", data.getFromDate(), data.getToDate());
//			}
			
			PreparedStatement stmt = q.getPrepareStatement(conn,data.getCurrentRow(), data.getMaxRowsPerPage(),"",data.getOrderby(), data.getSorting());
			CommonUtility.outputLog(q.getSql(), env);
			ResultSet rs = stmt.executeQuery();
			Brand b = null;
			if(rs.next()){
				b = new Brand();
				b.setId(rs.getString("brand_id"));
				b.setCode(rs.getString("brand_code"));
				b.setName(rs.getString("brand_name"));
				b.setStatus(rs.getInt("brand_status"));
				b.setCdate(rs.getDate("brand_cdate"));
				b.setMdate(rs.getDate("brand_mdate"));
				b.setProfileImage(new ImageData( rs.getString("profile_path"), "","" , true, ""));
				b.setCoverImage(new ImageData( rs.getString("cover_path"), "","" , true, ""));
				rtn.data = b;
				rtn.success();
			}else{
				rtn.error("brand not found");
			}
			
		} catch (Exception e) {
			rtn.error(e.getMessage());
			e.printStackTrace();
		}
		return rtn;
	}
	
	public ViewResult<Integer> getNewRegisterCount(Brand data,Connection conn){
		ViewResult<Integer> rtn = new ViewResult<>();
		try{
			String sql = "select count(*) over() as total"
					+ " from public.brand"
					+ " inner join myuser on myuser.fk_brand = brand.id"
					+ " inner join user_type usertype on usertype.id = myuser.fk_usertype";
			PrepareQuery q = new PrepareQuery(sql);
			q.addWhereClause("brand.status", data.getStatus(),Operator.EQUAL, Type.NUMBER);
//			if(!data.getFromDate().equals("") && !data.getToDate().equals("")){
//				q.addBetween("brand.cdate", data.getFromDate(), data.getToDate());
//			}

			PreparedStatement stmt =q.getPrepareStatement(conn,data.getCurrentRow(), data.getMaxRowsPerPage(),"",
					data.getOrderby(), data.getSorting());
			ResultSet rs = stmt.executeQuery();
			
			if(rs.next()){
				rtn.data = rs.getInt("total");
				rtn.totalItem = 1;
			}
			rtn.success();
		} catch (Exception e) {
			rtn.error(e.getMessage());
			e.printStackTrace();
		}
		return rtn;
	}
}
