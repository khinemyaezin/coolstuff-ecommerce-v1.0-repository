package com.cs.jupiter.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.jupiter.model.interfaces.ViewResult;
import com.cs.jupiter.model.table.PackType;
import com.cs.jupiter.utility.ComEnum;
import com.cs.jupiter.utility.CommonUtility;
import com.cs.jupiter.utility.PrepareQuery;

@Service
public class PackTypeDao {
	
	@Autowired
	DataSource ds;

	public ViewResult<PackType> insert(PackType data,Connection conn) {
		ViewResult<PackType> rtn = new ViewResult<>();
		
		try  {

			String sql = "INSERT INTO public.packtype("
					+ "id, code, name, status, mdate, cdate) VALUES (?, ?, ?, ?, ?::date, ?::date);";
			PreparedStatement stmt = conn.prepareStatement(sql);
			int i = 1;
			stmt.setLong(i++, Long.parseLong(data.getId()));
			stmt.setString(i++, data.getCode());
			stmt.setString(i++, data.getName());
			stmt.setInt(i++, data.getStatus());
			stmt.setString(i++, CommonUtility.convertDate_2db(data.getMdate()));
			stmt.setString(i++, CommonUtility.convertDate_2db(data.getCdate()));
			if (stmt.executeUpdate() > 0) {
				rtn.status = ComEnum.ErrorStatus.Success.getCode();
			}
		} catch (SQLException e) {
			rtn.status = ComEnum.ErrorStatus.DatabaseError.getCode();
			rtn.message = e.getMessage();
		}
		return rtn;

	}
	public ViewResult<PackType> getAll(PackType cri,Connection conn)  {
		ViewResult<PackType> rtn = new ViewResult<>();
		try  {
		String sql = "select row_number() over(order by name asc) as row_num,"
				+ "count(*) over() as total,*"
				+ " from packtype";
		PrepareQuery ps = new PrepareQuery(sql);
		ps.addWhereClause("id", cri.getId(),PrepareQuery.Operator.EQUAL,PrepareQuery.Type.ID);
		ps.addWhereClause("code", cri.getCode(),PrepareQuery.Operator.EQUAL, PrepareQuery.Type.VARCHAR);
		ps.addWhereClause("name", cri.getName(),PrepareQuery.Operator.LIKE_ALL, PrepareQuery.Type.VARCHAR);
		ps.addWhereClause("status", cri.getStatus(), PrepareQuery.Operator.EQUAL, PrepareQuery.Type.NUMBER);
		PreparedStatement stmt = ps.getPrepareStatement(conn,cri.getCurrentRow(), cri.getMaxRowsPerPage(),"","name", "asc");
		ResultSet rs = stmt.executeQuery();
		
		List<PackType> list = new ArrayList<>();
		PackType b = null;
		while (rs.next()){
			b = new PackType();
			b.setRowNumber(rs.getInt("row_num"));
			if(list.size()==0) rtn.totalItem = rs.getInt("total");
			b.setId(rs.getString("id"));
			b.setCode(rs.getString("code"));
			b.setName(rs.getString("name"));
			b.setStatus(rs.getInt("status"));
			b.setCdate(rs.getDate("cdate"));
			b.setMdate(rs.getDate("mdate"));
			list.add(b);
		}
		rtn.list = list;
		rtn.success();
		} catch (Exception e) {
			rtn.error(e.getMessage());
		}
		return rtn;
	}
	public ViewResult<PackType> read(String id,Connection conn)  {
		ViewResult<PackType> rtn = new ViewResult<>();
		try  {
		String sql = "select * from packtype where id=?";
		
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setLong(1, Long.parseLong(id));
		ResultSet rs = stmt.executeQuery();
		PackType b = null;
		if (rs.next()){
			b = new PackType();
			b.setId(rs.getString("id"));
			b.setCode(rs.getString("code"));
			b.setName(rs.getString("name"));
			b.setStatus(rs.getInt("status"));
			b.setCdate(rs.getDate("cdate"));
			b.setMdate(rs.getDate("mdate"));
			rtn.data = b;
		}
		rtn.status = ComEnum.ErrorStatus.Success.getCode();
		} catch (Exception e) {
			rtn.status = ComEnum.ErrorStatus.DatabaseError.getCode();
			rtn.message = e.getMessage();
			e.printStackTrace();
		}
		return rtn;
	}

	public ViewResult<PackType> update(PackType data,Connection conn) throws Exception {
		ViewResult<PackType> rtn = new ViewResult<>();
		try  {

			String sql = "UPDATE public.packtype ";
			PrepareQuery p = new PrepareQuery(sql);
			p.addSetClause("mdate", data.getMdate(), PrepareQuery.Operator.EQUAL,
					PrepareQuery.Type.DATE);
			p.addSetClause("code", data.getCode(), PrepareQuery.Operator.EQUAL, PrepareQuery.Type.VARCHAR);
			p.addSetClause("name", data.getName(), PrepareQuery.Operator.EQUAL, PrepareQuery.Type.VARCHAR);
			p.addSetClause("status", data.getStatus(), PrepareQuery.Operator.EQUAL, PrepareQuery.Type.NUMBER);
			
			p.addWhereClause("id", Long.parseLong(data.getId()), PrepareQuery.Operator.EQUAL, PrepareQuery.Type.ID);

			PreparedStatement stmt = p.getPrepareStatement(conn);
			stmt.executeUpdate();
			rtn.status = ComEnum.ErrorStatus.Success.getCode();
		} catch (Exception e) {
			rtn.status = ComEnum.ErrorStatus.DatabaseError.getCode();
			rtn.message = e.getMessage();
			e.printStackTrace();
		}
		return rtn;
	}

	public ViewResult<PackType> delete(PackType data,Connection conn) throws NumberFormatException, Exception {
		ViewResult<PackType> rtn = new ViewResult<>();
		try  {

			String sql = "DELETE FROM public.packtype";
			PrepareQuery ps = new PrepareQuery(sql);
			ps.addWhereClause("id", Long.parseLong(data.getId()), PrepareQuery.Operator.EQUAL, PrepareQuery.Type.ID);
		
			PreparedStatement stmt = ps.getPrepareStatement(conn);
			stmt.executeUpdate();
			rtn.status = ComEnum.ErrorStatus.Success.getCode();
		} catch (Exception e) {
			rtn.status = ComEnum.ErrorStatus.DatabaseError.getCode();
			rtn.message = e.getMessage();
			e.printStackTrace();
		}
		return rtn;

	}
	
}
