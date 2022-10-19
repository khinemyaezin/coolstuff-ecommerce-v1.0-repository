package com.cs.jupiter.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.stereotype.Repository;

import com.cs.jupiter.model.interfaces.ViewResult;
import com.cs.jupiter.model.jun.MyanmarGeometry;
import com.cs.jupiter.model.table.Geometry;
import com.cs.jupiter.utility.CommonUtility;
import com.cs.jupiter.utility.KeyFactory;

@Repository
public class GeometryDao {
	public  int insertState(Geometry data, Connection conn) throws SQLException {
		String sql = "INSERT INTO public.geo_state("
				+ "id,name, status, biz_status, cdate, mdate, type, geometry, pcode, mmr4, shape_leng, shape_area, geometry_type, local_id) "
				+ "VALUES (?,?, ?, ?, ?::timestamp, ?::timestamp, ?, ST_GeomFromGeoJSON(?), ?, ?, ?, ?, ?, ?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		insert(stmt,data);
		return stmt.executeUpdate();

	}

	public  int insertDistrict(Geometry data, Connection conn) throws SQLException {
		String sql = "INSERT INTO public.geo_district("
				+ "id,name, status, biz_status, cdate, mdate, type, geometry,p_pcode, pcode, mmr4, shape_leng, shape_area, geometry_type,"
				+ " local_id) " + "VALUES (?, ?, ?, ?, ?::timestamp, ?::timestamp, ?, ST_GeomFromGeoJSON(?), ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		insert(stmt,data);
		return stmt.executeUpdate();

	}
	
	public  int insertTsp(Geometry data, Connection conn) throws SQLException {
		String sql = "INSERT INTO public.geo_township("
				+ "id, name, status, biz_status, cdate, mdate, type, geometry, p_pcode, pcode, mmr4, shape_leng, shape_area, geometry_type,"
				+ " local_id) " + "VALUES (?, ?,?, ?, ?::timestamp, ?::timestamp, ?, ST_GeomFromGeoJSON(?), ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		insert(stmt,data);
		return stmt.executeUpdate();

	}
	public  int insertVillageTrack(Geometry data, Connection conn) throws SQLException {
		String sql = "INSERT INTO public.geo_villagetrack("
				+ "id, name, status, biz_status, cdate, mdate, type, geometry, pcode, mmr4, shape_leng, shape_area, geometry_type,"
				+ " local_id) " + "VALUES (?, ?,?, ?, ?::timestamp, ?::timestamp, ?, ST_GeomFromGeoJSON(?), ?, ?, ?, ?, ?, ?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		insert(stmt,data);
		return stmt.executeUpdate();

	}
	
	public  int insertTown(Geometry data, Connection conn) throws SQLException {
		String sql = "INSERT INTO public.geo_town("
				+ "id, name, status, biz_status, cdate, mdate, type, geometry, pcode, mmr4, shape_leng, shape_area, geometry_type,"
				+ " local_id) " + "VALUES (?, ?,?, ?, ?::timestamp, ?::timestamp, ?, ST_GeomFromGeoJSON(?), ?, ?, ?, ?, ?, ?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		insert(stmt,data);
		return stmt.executeUpdate();

	}
	public  void insert(PreparedStatement stmt,Geometry data) throws SQLException{
		int i = 1;
		stmt.setLong(i++, KeyFactory.getId());
		stmt.setString(i++, data.getName());
		stmt.setInt(i++, 1);
		stmt.setInt(i++, 1);
		stmt.setString(i++, CommonUtility.convertTimeStamp_2db(new Date()));
		stmt.setString(i++, CommonUtility.convertTimeStamp_2db(new Date()));
		stmt.setString(i++, data.getType());
		stmt.setString(i++, data.getGeoString());
		stmt.setString(i++, data.getParentPcode());
		stmt.setString(i++, data.getPcode());
		stmt.setString(i++, data.getMmr4());
		stmt.setDouble(i++, data.getShape_leng());
		stmt.setDouble(i++, data.getShape_area());
		stmt.setString(i++, data.getGeometryType());
		stmt.setString(i++, data.getLocalId());
	}
	public ViewResult<Geometry> getTown(String townShipCode,Connection conn){
		ViewResult<Geometry> result = new ViewResult<Geometry>(null,new ArrayList<Geometry>());
		try{
			String sql  = "select mmr4,pcode from geo_town where p_pcode=?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			int i=1;
			stmt.setString(i++, townShipCode);
			ResultSet rs = stmt.executeQuery();
			Geometry data;
			while(rs.next()){
				data = new Geometry();
				data.setPcode(rs.getString("pcode"));
				data.setMmr4(rs.getString("mmr4"));
				result.list.add(data);
				
			}
			result.success();
		}catch(Exception e){
			e.printStackTrace();
			result.error(e.getMessage());
		}
		return result;
	}
	
	public ViewResult<Geometry> getWard(String townCode,Connection conn){
		ViewResult<Geometry> result = new ViewResult<Geometry>(null,new ArrayList<Geometry>());
		try{
			String sql  = "select mmr4,pcode from geo_ward where p_pcode=?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			int i=1;
			stmt.setString(i++, townCode);
			ResultSet rs = stmt.executeQuery();
			Geometry data;
			while(rs.next()){
				data = new Geometry();
				data.setPcode(rs.getString("pcode"));
				data.setMmr4(rs.getString("mmr4"));
				result.list.add(data);
				
			}
			result.success();
		}catch(Exception e){
			e.printStackTrace();
			result.error(e.getMessage());
		}
		return result;
	}
	
	public ViewResult<MyanmarGeometry> locatePositionToTownship(double lat,double lng,Connection conn){
		ViewResult<MyanmarGeometry> result = new ViewResult<MyanmarGeometry>();
		try{
			String sql  = "select state_mm,state_pcode,district_mm,district_pcode,township_mm,township_pcode from get_location(?,?)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			int i=1;
			stmt.setDouble(i++, lat);
			stmt.setDouble(i++, lng);
			ResultSet rs = stmt.executeQuery();
			MyanmarGeometry data;
			if(rs.next()){
				data = new MyanmarGeometry();
				data.setState(new Geometry());
				data.getState().setMmr4(rs.getString("state_mm"));
				data.getState().setPcode(rs.getString("state_pcode"));
				data.setDistrict(new Geometry());
				data.getDistrict().setMmr4(rs.getString("district_mm"));
				data.getDistrict().setPcode(rs.getString("district_pcode"));
				data.setTownship(new Geometry());
				data.getTownship().setMmr4(rs.getString("township_mm"));
				data.getTownship().setPcode(rs.getString("township_pcode"));
				result.data = data;
				result.success();
			}else{
				result.error();
			}
		}catch(Exception e){
			e.printStackTrace();
			result.error(e.getMessage());
		}
		return result;
	}
	
	public ViewResult<MyanmarGeometry> locatePositionToTown(double lat,double lng,String tspPcode,MyanmarGeometry data,Connection conn){
		ViewResult<MyanmarGeometry> result = new ViewResult<MyanmarGeometry>();
		try{
			String sql  = "select "
					+ "town_mm,town_pcode,ward_mm,ward_pcode"
					+ " from get_location_town(?,?,?"
					+ ");";
			PreparedStatement stmt = conn.prepareStatement(sql);
			int i=1;
			stmt.setDouble(i++, lat);
			stmt.setDouble(i++, lng);
			stmt.setString(i++, tspPcode);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				data.setTown(new Geometry());
				data.getTown().setMmr4(rs.getString("town_mm"));
				data.getTown().setPcode(rs.getString("town_pcode"));
				data.setWard(new Geometry());
				data.getWard().setMmr4(rs.getString("ward_mm"));
				data.getWard().setPcode(rs.getString("ward_pcode"));
				result.success();
			}else{
				result.error();
			}
		}catch(Exception e){
			e.printStackTrace();
			result.error(e.getMessage());
		}
		return result;
	}
	
	public ViewResult<Geometry> townByRange(double lat,double lng,Connection conn){
		ViewResult<Geometry> result = new ViewResult<Geometry>(null,new ArrayList<>());
		try{
			String sql  = "select mmr4,pcode,p_pcode from geo_town where "
					+ "ST_DWithin( geometry ,  ST_GeomFromText(?,4326) , ?, true );";
			PreparedStatement stmt = conn.prepareStatement(sql);
			int i=1;
			stmt.setString(i++, CommonUtility.formatGeoPoint(lng, lat));
			stmt.setInt(i++, 6000);
			ResultSet rs = stmt.executeQuery();
			Geometry data;
			while(rs.next()){
				data = new Geometry();
				data.setMmr4(rs.getString("mmr4"));
				data.setPcode(rs.getString("pcode"));
				data.setParentPcode(rs.getString("p_pcode"));
				result.list.add(data);
			}
			if(result.list.size() == 1){
				result.data = result.list.get(0);
			}
			if(result.data == null && result.list.size() == 0){
				result.error();
			}else{
				result.success();
			}
			
		}catch(Exception e){
			e.printStackTrace();
			result.error(e.getMessage());
		}
		return result;
	}
	
	public ViewResult<Geometry> townMappingVillage(double lat,double lng,Connection conn){
		ViewResult<Geometry> result = new ViewResult<Geometry>(null,new ArrayList<>());
		try{
			String sql  = "select town.mmr4,town.pcode from geo_villageTrack vt "
					+ "inner join geo_town town on town.pcode = vt.pcode "
					+ "where st_contains(vt.geometry, ST_GeomFromText(?, 4326))";
			PreparedStatement stmt = conn.prepareStatement(sql);
			int i=1;
			stmt.setString(i++, CommonUtility.formatGeoPoint(lng, lat));
			ResultSet rs = stmt.executeQuery();
			Geometry data;
			while(rs.next()){
				data = new Geometry();
				data.setMmr4(rs.getString("mmr4"));
				data.setPcode(rs.getString("pcode"));
				result.list.add(data);
			}
			if(result.list.size() == 1){
				result.data = result.list.get(0);
			}
			if(result.data == null && result.list.size() == 0){
				result.error();
			}else{
				result.success();
			}
			
		}catch(Exception e){
			e.printStackTrace();
			result.error(e.getMessage());
		}
		return result;
	}
	public ViewResult<Geometry> villageByRange(double lat,double lng,MyanmarGeometry mmData,Connection conn){
		ViewResult<Geometry> result = new ViewResult<Geometry>(null,new ArrayList<>());
		try{
			String sql  = "select vt.mmr4 as vt_mmr4,vt.pcode as vt_pcode,v.mmr4 v_mmr4,v.pcode as v_pcode from geo_village v "
					+ "inner join geo_villagetrack vt on vt.pcode=v.p_pcode "
					+ "where ST_DWithin( v.geometry , ST_GeomFromText(?,4326) , ?, true );";
			PreparedStatement stmt = conn.prepareStatement(sql);
			int i=1;
			stmt.setString(i++, CommonUtility.formatGeoPoint(lng, lat));
			stmt.setInt(i++, 2500);
			ResultSet rs = stmt.executeQuery();
			Geometry data;
			while(rs.next()){
				data = new Geometry();
				data.setMmr4(rs.getString("v_mmr4"));
				data.setPcode(rs.getString("v_pcode"));
				
				if(result.list.size() == 0 && mmData!=null){
					Geometry villageTrack = new Geometry();
					villageTrack.setMmr4(rs.getString("vt_mmr4"));
					villageTrack.setPcode(rs.getString("vt_pcode"));
					mmData.setVillageTrack(villageTrack);
				}
				result.list.add(data);
			}
			if(result.list.size() == 1){
				result.data = result.list.get(0);
			}
			if(result.data == null && result.list.size() == 0){
				result.error();
			}else{
				result.success();
			}
			
		}catch(Exception e){
			e.printStackTrace();
			result.error(e.getMessage());
		}
		return result;
	}
}
