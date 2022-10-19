package com.cs.jupiter.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import com.cs.jupiter.model.interfaces.ViewResult;
import com.cs.jupiter.model.table.ImageData;
import com.cs.jupiter.model.table.Nrc;
import com.cs.jupiter.model.table.NrcDistrict;
import com.cs.jupiter.model.table.User;
import com.cs.jupiter.model.table.UserType;
import com.cs.jupiter.utility.CommonUtility;
import com.cs.jupiter.utility.PrepareQuery;
import com.cs.jupiter.utility.PrepareQuery.Operator;
import com.cs.jupiter.utility.PrepareQuery.Type;

@Repository
public class UserDao {
	@Autowired
	Environment env;

	public ViewResult<User> getUser(User data, Connection conn) {
		ViewResult<User> rtn = new ViewResult<>();
		try {
			String sql = "SELECT myuser.id, myuser.first_name, myuser.last_name, myuser.cdate, myuser.nrc, myuser.mdate, myuser.status,"
					+ " myuser.email, myuser.phone_no, myuser.address,myuser.signin_method as signin_method,myuser.profile_path as profile,ut.id as ut_id, ut.code as ut_code, ut.name as ut_name "
					+ " FROM public.myuser inner join user_type ut on ut.id = myuser.fk_usertype";
			PrepareQuery q = new PrepareQuery(sql);
			q.addWhereClause("myuser.first_name", data.getFirstName(), Operator.EQUAL, Type.VARCHAR);
			q.addWhereClause("myuser.last_name", data.getLastName(), Operator.EQUAL, Type.VARCHAR);
			q.addWhereClause("myuser.nrc", data.getNrc(), Operator.EQUAL, Type.VARCHAR);
			q.addWhereClause("myuser.mdate", data.getMdate(), Operator.EQUAL,
					Type.DATE);
			q.addWhereClause("myuser.status", data.getStatus(), Operator.EQUAL, Type.NUMBER);

			q.addWhereClause("myuser.email", data.getEmail(), Operator.EQUAL, Type.VARCHAR);
			q.addWhereClause("myuser.phone_no", data.getPhoneNo(), Operator.EQUAL, Type.VARCHAR);
			q.addWhereClause("myuser.address", data.getAddress(), Operator.EQUAL, Type.VARCHAR);
			if (data.getUserType() != null)
				q.addWhereClause("myuser.fk_usertype", data.getUserType().getId(), Operator.EQUAL,
						Type.ID);
			if (data.getBrand() != null) {
				q.addWhereClause("myuser.fk_brand", data.getBrand().getId(), Operator.EQUAL,
						Type.ID);
			}
			
			PreparedStatement stmt = q.getPrepareStatement(conn);
			ResultSet rs = stmt.executeQuery();
			rtn.data = new User();
			if (rs.next()) {
				rtn.data = new User();
				rtn.data.setId(rs.getString("id"));
				rtn.data.setFirstName(rs.getString("first_name"));
				rtn.data.setLastName(rs.getString("last_name"));
				rtn.data.setCdate(rs.getDate("cdate"));
				rtn.data.setNrc(rs.getString("nrc"));
				rtn.data.setMdate(rs.getDate("mdate"));
				rtn.data.setStatus(rs.getInt("status"));
				rtn.data.setEmail(rs.getString("email"));
				rtn.data.setPhoneNo(rs.getString("phone_no"));
				rtn.data.setAddress(rs.getString("address"));
				rtn.data.setSigninMethod(rs.getString("signin_method").charAt(0));
				rtn.data.setProfileImage(new ImageData(rs.getString("profile"),"","",true,""));
				UserType utype = new UserType();
				utype.setId(rs.getString("ut_id"));
				utype.setCode(rs.getString("ut_code"));
				utype.setName(rs.getString("ut_name"));
				rtn.data.setUserType(utype);
				if (data.getBrand() != null) {
					rtn.data.setBrand(data.getBrand());
				}
				rtn.success();
			} else {
				rtn.error("user not found");
			}
		} catch (Exception e) {
			rtn.error(e.getMessage());
			e.printStackTrace();
		}
		return rtn;
	}
	public ViewResult<User> getUserById(String id,Connection conn) {
		ViewResult<User> rtn = new ViewResult<>();
		try {
			String sql = "SELECT * from myuser where id=?";
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setLong(1, Long.parseLong(id));
			ResultSet rs = stmt.executeQuery();
			rtn.data = new User();
			if (rs.next()) {
				rtn.data = new User();
				rtn.data.setId(rs.getString("id"));
				rtn.data.setFirstName(rs.getString("first_name"));
				rtn.data.setLastName(rs.getString("last_name"));
				rtn.data.setCdate(rs.getDate("cdate"));
				rtn.data.setNrc(rs.getString("nrc"));
				rtn.data.setMdate(rs.getDate("mdate"));
				rtn.data.setStatus(rs.getInt("status"));
				rtn.data.setEmail(rs.getString("email"));
				rtn.data.setPhoneNo(rs.getString("phone_no"));
				rtn.data.setAddress(rs.getString("address"));
				rtn.data.setSigninMethod(rs.getString("signin_method").charAt(0));
				rtn.data.setProfileImage(new ImageData(rs.getString("profile_path"),"","",true,""));
				rtn.data.setPassword(rs.getString("password"));
			
				rtn.success();
			} else {
				rtn.error("user not found");
			}
		} catch (Exception e) {
			rtn.error(e.getMessage());
			e.printStackTrace();
		}
		return rtn;
	}

	public ViewResult<User> insertUser(User data, Connection conn) {
		ViewResult<User> rtn = new ViewResult<>();
		try {
			String sql = "insert into public.myuser"
					+ " ( id,name, first_name, last_name, cdate, nrc, mdate, status, email, phone_no, address,fk_usertype,fk_brand,signin_method,profile_path,password) "
					+ " values (?,?, ?, ?, ?::date, ?, ?::date, ?, ?, ?, ?,?,?,?,?,?);";
			PreparedStatement stmt = conn.prepareStatement(sql);
			int i = 1;
			stmt.setLong(i++, Long.parseLong(data.getId()));
			stmt.setString(i++, data.getName());
			stmt.setString(i++, data.getFirstName());
			stmt.setString(i++, data.getLastName());
			stmt.setString(i++, CommonUtility.convertDate_2db(data.getCdate()));
			stmt.setString(i++, data.getNrc());
			stmt.setString(i++, CommonUtility.convertDate_2db(data.getMdate()));
			stmt.setInt(i++, data.getStatus());
			stmt.setString(i++, data.getEmail());
			stmt.setString(i++, data.getPhoneNo());
			stmt.setString(i++, data.getAddress());
			stmt.setLong(i++, Long.parseLong(data.getUserType().getId()));
			if (data.getBrand() == null)
				stmt.setNull(i++, Types.BIGINT);
			else
				stmt.setLong(i++, Long.parseLong(data.getBrand().getId()));
			stmt.setString(i++, String.valueOf(data.getSigninMethod()));
			if (data.getProfileImage() == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, data.getProfileImage().getName());
			if (data.getPassword() == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, data.getPassword());
			stmt.executeUpdate();
			rtn.success();

		} catch (Exception e) {
			rtn.error(e.getMessage());
			e.printStackTrace();
		}
		return rtn;
	}

	public ViewResult<User> updateUser(User data, Connection conn) {
		ViewResult<User> rtn = new ViewResult<>();
		try {
			String sql = "update public.myuser";
			PrepareQuery q = new PrepareQuery(sql);
			q.addSetClause("first_name", data.getFirstName(), Operator.EQUAL, Type.VARCHAR);
			q.addSetClause("last_name", data.getLastName(), Operator.EQUAL, Type.VARCHAR);
			q.addSetClause("nrc", data.getNrc(), Operator.EQUAL, Type.VARCHAR);
			q.addSetClause("mdate", data.getMdate(), Operator.EQUAL,
					Type.DATE);
			q.addSetClause("status", data.getStatus(), Operator.EQUAL, Type.NUMBER);

			q.addSetClause("email", data.getEmail(), Operator.EQUAL, Type.VARCHAR);
			q.addSetClause("phone_no", data.getPhoneNo(), Operator.EQUAL, Type.VARCHAR);
			q.addSetClause("address", data.getAddress(), Operator.EQUAL, Type.VARCHAR);
			
			q.addSetClause("signin_method", data.getSigninMethod(), Operator.EQUAL, Type.CHAR);
			if (data.getUserType() != null)
				q.addWhereClause("fk_usertype", data.getUserType().getId(), Operator.EQUAL,
						Type.ID);

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
	public ViewResult<User> updateUserProfile(User data, Connection conn) {
		ViewResult<User> rtn = new ViewResult<>();
		try {
			String sql = "update public.myuser set profile_path=? where id=?";
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			int i=1;
			stmt.setString(i++, data.getProfileImage().getName());
			stmt.setLong(i++, Long.parseLong(data.getId()));
			stmt.executeUpdate();
			rtn.success();
		} catch (Exception e) {
			rtn.error(e.getMessage());
			e.printStackTrace();
		}
		return rtn;
	}
	public ViewResult<User> updateUserPassword(User data, Connection conn) {
		ViewResult<User> rtn = new ViewResult<>();
		try {
			String sql = "update public.myuser set password=? where id=?";
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			int i=1;
			stmt.setString(i++, data.getPassword());
			stmt.setLong(i++, Long.parseLong(data.getId()));
			stmt.executeUpdate();
			rtn.success();
		} catch (Exception e) {
			rtn.error(e.getMessage());
			e.printStackTrace();
		}
		return rtn;
	}
	

	public ViewResult<User> deleteUser(User data, Connection conn) {
		ViewResult<User> rtn = new ViewResult<>();
		try {
			String sql = "delete from public.myuser where id=?";

			PreparedStatement stmt = conn.prepareStatement(sql);
			int i = 1;
			stmt.setLong(i++, Long.parseLong(data.getId()));
			stmt.executeUpdate();
			rtn.success();
		} catch (Exception e) {
			rtn.error(e.getMessage());
			e.printStackTrace();
		}
		return rtn;
	}

	public ViewResult<UserType> selectUserType(String code,Connection conn) {
		ViewResult<UserType> rtn = new ViewResult<>();
		try {
			String sql = "select id, name, cdate, mdate, status, code, ref_brand from public.user_type";
			PrepareQuery q = new PrepareQuery(sql);
			q.addWhereClause("code", code, Operator.EQUAL, Type.VARCHAR);
			ResultSet rs = q.getPrepareStatement(conn).executeQuery();
			UserType data;
			while (rs.next()) {
				data = new UserType();
				data.setId(rs.getString("id"));
				data.setName(rs.getString("name"));
				data.setCdate(CommonUtility.convertDate_bydb(rs.getString("cdate")));
				data.setMdate(CommonUtility.convertDate_bydb(rs.getString("mdate")));
				data.setStatus(rs.getInt("status"));
				data.setCode(rs.getString("code"));
				data.setRefBrand(rs.getInt("ref_brand"));
				rtn.list.add(data);
			}
			rtn.totalItem = rtn.list.size();
			if(rtn.list.size() == 1){
				rtn.data = rtn.list.get(0);
				rtn.list = null;
			}
			

			rtn.success();
		} catch (Exception e) {
			rtn.error(e.getMessage());
			e.printStackTrace();
		}
		return rtn;
	}

	public ViewResult<UserType> selectUserTypeById(String id, Connection conn) {
		ViewResult<UserType> rtn = new ViewResult<>();
		try {
			String sql = "select id, name, cdate, mdate, status, code from public.user_type where id=?";

			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setLong(1, Long.parseLong(id));
			ResultSet rs = stmt.executeQuery();
			UserType data;
			while (rs.next()) {
				data = new UserType();
				data.setId(rs.getString("id"));
				data.setName(rs.getString("name"));
				data.setCdate(CommonUtility.convertDate_bydb(rs.getString("cdate")));
				data.setMdate(CommonUtility.convertDate_bydb(rs.getString("mdate")));
				data.setStatus(rs.getInt("status"));
				data.setCode(rs.getString("code"));
				rtn.data = data;

				rtn.totalItem = 1;
				rtn.success();
				return rtn;
			}

			rtn.error("user not found");
			return rtn;
		} catch (Exception e) {
			rtn.error(e.getMessage());
			e.printStackTrace();
			return rtn;
		}

	}

	public ViewResult<Nrc> getNrcState(Connection conn) {
		ViewResult<Nrc> rtn = new ViewResult<>();
		try {
			String sql = "select id,code from public.nrc_state where status<>4;";

			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			Nrc data;
			while (rs.next()) {
				data = new Nrc();
				data.setId(rs.getString("id"));
				data.setCode(rs.getString("code"));
				rtn.list.add(data);
			}
			rtn.totalItem = rtn.list.size();

			rtn.success();
		} catch (Exception e) {
			rtn.error(e.getMessage());
			e.printStackTrace();
		}
		return rtn;
	}

	public ViewResult<NrcDistrict> getNrcDistrict(String stateId, Connection conn) {
		ViewResult<NrcDistrict> rtn = new ViewResult<>();
		try {
			String sql = "select id,code,fk_nrc_state from public.nrc_district where fk_nrc_state=? and status<>4;";

			PreparedStatement stmt = conn.prepareStatement(sql);
			int i = 1;
			stmt.setLong(i++, Long.parseLong(stateId));
			ResultSet rs = stmt.executeQuery();
			NrcDistrict data;
			while (rs.next()) {
				data = new NrcDistrict();
				data.setId(rs.getString("id"));
				data.setCode(rs.getString("code"));
				data.setNrcState(new Nrc(rs.getString("fk_nrc_state")));
				rtn.list.add(data);
			}
			rtn.totalItem = rtn.list.size();

			rtn.success();
		} catch (Exception e) {
			rtn.error(e.getMessage());
			e.printStackTrace();
		}
		return rtn;
	}
	
	

}
