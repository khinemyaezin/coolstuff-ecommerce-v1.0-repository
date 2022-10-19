package com.cs.jupiter.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import org.springframework.stereotype.Repository;

import com.cs.jupiter.model.interfaces.ViewResult;
import com.cs.jupiter.model.jun.LoginData;
import com.cs.jupiter.model.table.SessionData;
import com.cs.jupiter.model.table.UserType;
import com.cs.jupiter.utility.CommonUtility;

@Repository
public class AuthDao {
	public ViewResult<LoginData> auth(String data,Connection conn){
		ViewResult<LoginData> result = new ViewResult<>();
		try{
			String sql = "select "
					+ "u.id user_id,"
					+ "u.first_name user_firstname, "
					+ "u.last_name user_lasetname,"
					+ "u.cdate user_cdate,"
					+ "u.nrc user_nrc,"
					+ "u.mdate,"
					+ "u.status,"
					+ "u.password user_password,"
					+ "ut.code usertype_code,"
					+ "ut.name usertype_name,"
					+ "ut.auth_path auth_path,"
					+ "brand.code brand_code "
					+ "from myuser u "
					+ "left join brand on brand.id=u.fk_brand "
					+ "inner join user_type ut on ut.id=u.fk_usertype "
					+ "where u.email=? or u.phone_no=?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			int i=1;
			stmt.setString(i++, data);
			stmt.setString(i++, data);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				result.data = new LoginData();
				result.data.setUserId(rs.getString("user_id"));
				result.data.setBizId(rs.getString("brand_code"));
				result.data.setEncPassword(rs.getString("user_password"));
				result.data.setUserType(new UserType());
				result.data.getUserType().setCode(rs.getString("usertype_code"));
				result.data.getUserType().setName(rs.getString("usertype_name"));
				result.data.getUserType().setAuthPath(rs.getString("auth_path"));
				result.success();
			}else{
				result.error("user_not_found");
			}
		}catch(Exception e){
			result.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	public ViewResult<String> insertSession(SessionData data,Connection conn){
		ViewResult<String> result = new ViewResult<>();
		try{
			String sql = "insert into public.session(status, fk_user, biz_id, start_time, end_time) "
					+ "VALUES (?, ?, ?, ?, ?);";
			PreparedStatement stmt = conn.prepareStatement(sql);
			int i=1;
			stmt.setInt(i++, data.getStatus());
			stmt.setLong(i++, Long.parseLong(data.getUserId()));
			stmt.setString(i++, CommonUtility.convertTimeStamp_2db(data.getStartTime()));
			stmt.setString(i++, CommonUtility.convertTimeStamp_2db(data.getEndTime()));
			stmt.executeUpdate();
		}catch(Exception e){
			result.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	public ViewResult<SessionData> getSession(String value,Date sessTime,Connection conn){
		ViewResult<SessionData> result = new ViewResult<>();
		try{
			String sql = "select id, status,biz_id, start_time, end_time from public.session "
					+ "where fk_user=? and (start_time, end_time) overlaps (?::timestamp,?::timestamp);";
			PreparedStatement stmt = conn.prepareStatement(sql);
			int i=1;
			stmt.setLong(i++, Long.parseLong(value));
			stmt.setString(i++, CommonUtility.convertTimeStamp_2db(sessTime));
			stmt.setString(i++, CommonUtility.convertTimeStamp_2db(sessTime));
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				SessionData s = new SessionData();
				s.setId(rs.getString("id"));
				s.setStartTime(CommonUtility.convertTimeStamp_bydb(rs.getString("start_time")));
				s.setEndTime(CommonUtility.convertTimeStamp_bydb(rs.getString("end_time")));
				s.setStatus(rs.getInt("status"));
				s.setBizId(rs.getString("biz_id"));
				s.setUserId(value);
				result.success();
			}else{
				result.error("no_record");
			}
		}catch(Exception e){
			result.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
}
