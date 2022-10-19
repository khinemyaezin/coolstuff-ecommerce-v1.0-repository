package com.cs.jupiter.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import com.cs.jupiter.model.interfaces.ViewResult;
import com.cs.jupiter.model.table.ImageData;
import com.cs.jupiter.utility.ComEnum;
import com.cs.jupiter.utility.CommonUtility;
import com.cs.jupiter.utility.PrepareQuery;
import com.cs.jupiter.utility.PrepareQuery.Operator;
import com.cs.jupiter.utility.PrepareQuery.Type;

@Repository
public class ImageDao {
	@Autowired
	Environment env;

	public ViewResult<ImageData> insert(ImageData data, Connection conn) {
		ViewResult<ImageData> rtn = new ViewResult<>();
		try {
			String sql = "INSERT INTO public.image(code, name, status, def, path, comment)"
					+ "VALUES (?, ?, ?, ?, ?, ?) RETURNING id;";
			PreparedStatement stmt = conn.prepareStatement(sql);
			int i = 1;
			stmt.setString(i++, data.getCode());
			stmt.setString(i++, data.getName());
			stmt.setInt(i++, data.getStatus());
			stmt.setBoolean(i++, data.isDefaults());
			stmt.setString(i++, data.getPath());
			stmt.setString(i++, data.getComment());
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				rtn.data = new ImageData(rs.getString("id"));
				rtn.success();
			} else {
				rtn.error();
			}

		} catch (SQLException e) {
			rtn.error(e.getMessage());
			e.printStackTrace();
		}
		return rtn;
	}

	public ViewResult<ImageData> update(ImageData data, Connection conn) throws Exception {
		ViewResult<ImageData> rtn = new ViewResult<>();
		try {

			String sql = "UPDATE public.image ";
			PrepareQuery p = new PrepareQuery(sql);

			p.addSetClause("code", data.getCode(), Operator.EQUAL, Type.VARCHAR);
			p.addSetClause("name", data.getName(), Operator.EQUAL, Type.VARCHAR);
			p.addSetClause("status", data.getStatus(), Operator.EQUAL, Type.NUMBER);
			p.addSetClause("def", data.isDefaults(), Operator.EQUAL, Type.BOOLEAN);
			p.addSetClause("path", data.getPath(), Operator.EQUAL, Type.VARCHAR);
			p.addSetClause("comment", data.getComment(), Operator.EQUAL, Type.VARCHAR);
			p.addWhereClause("id", data.getId(), Operator.EQUAL, Type.ID);
			PreparedStatement stmt = p.getPrepareStatement(conn);
			stmt.executeUpdate();
			rtn.success();
		} catch (Exception e) {
			rtn.status = ComEnum.ErrorStatus.DatabaseError.getCode();
			rtn.message = e.getMessage();
			e.printStackTrace();
		}
		return rtn;
	}

	public ViewResult<ImageData> getAll(ImageData data, Connection conn) {
		ViewResult<ImageData> rtn = new ViewResult<>();
		try {
			String sql = "select id, code, name, status, def, path, comment from image";
			PrepareQuery q = new PrepareQuery(sql);
			q.addWhereClause("id", data.getId(), Operator.EQUAL, Type.ID);
		
			PreparedStatement stmt = q.getPrepareStatement(conn);
			ResultSet rs = stmt.executeQuery();
			ImageData i;
			while (rs.next()) {
				i = new ImageData();
				i.setId(rs.getString("id"));
				i.setStatus(rs.getInt("status"));
				i.setComment(rs.getString("comment"));
				i.setDefaults(rs.getBoolean("def"));
				i.setPath(rs.getString("path"));
				i.setName(rs.getString("name"));
				rtn.list.add(i);
			}
			rtn.status = ComEnum.ErrorStatus.Success.getCode();
		} catch (Exception e) {
			rtn.status = ComEnum.ErrorStatus.DatabaseError.getCode();
			rtn.message = e.getMessage();
			e.printStackTrace();
		}
		return rtn;
	}

	public ViewResult<ImageData> get(ImageData data, Connection conn) {
		ViewResult<ImageData> rtn = new ViewResult<>();
		try {
			String sql = "select * from image";
			PrepareQuery q = new PrepareQuery(sql);
			q.addWhereClause("id", Long.parseLong(data.getId()), Operator.EQUAL, Type.ID);
			q.addWhereClause("name", data.getName(), Operator.EQUAL, Type.VARCHAR);
			
			PreparedStatement stmt = q.getPrepareStatement(conn);
			ResultSet rs = stmt.executeQuery();
			ImageData i;
			if (rs.next()) {
				i = new ImageData();
				i.setId(rs.getString("id"));
				i.setName(rs.getString("name"));
				i.setStatus(rs.getInt("status"));
				i.setPath(rs.getString("path"));
				i.setComment(rs.getString("comment"));
				i.setDefaults(rs.getBoolean("def"));
				rtn.data = i;
				rtn.success();
				return rtn;
			}
			rtn.error();
			return rtn;
		} catch (Exception e) {
			rtn.error(e.getMessage());
			CommonUtility.outputLog(e.getMessage(), env);
		}
		return rtn;
	}

	public ViewResult<ImageData> delete(ImageData data, Connection conn) {
		ViewResult<ImageData> rtn = new ViewResult<>();
		try {
			String sql = "delete from image";
			PrepareQuery q = new PrepareQuery(sql);
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

	public ViewResult<ImageData> deleteById(String id, Connection conn) {
		ViewResult<ImageData> rtn = new ViewResult<>();
		try {
			String sql = "delete from image where id=?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setLong(1, Long.parseLong(id));
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
