package com.cs.jupiter.sesetup;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.postgis.LinearRing;
import org.postgis.Point;
import org.postgis.Polygon;

import com.cs.jupiter.model.table.Geometry;
import com.cs.jupiter.utility.CommonUtility;
import com.cs.jupiter.utility.KeyFactory;
import com.google.gson.Gson;

public class GeoLocation2 {
	public static Gson gson = new Gson();

	public static void main(String[] args) {
		Connection conn = getConnection();
		if (conn != null) {

			try {
				conn.setAutoCommit(false);
				
				String statePath = "C:\\Users\\khine\\Documents\\coolstuff\\geo json\\state_sub.json";
				String districtPath = "C:\\Users\\khine\\Desktop\\district.json";
				String tspPath = "C:\\Users\\khine\\Documents\\coolstuff\\geo json\\tsp.json";
				String villageTrack = "C:\\Users\\khine\\Documents\\coolstuff\\geo json\\magway_villagetrack.json";
				String townPath = "C:\\Users\\khine\\Documents\\coolstuff\\geo json\\town.json";
				String yangonWard = "C:\\Users\\khine\\Documents\\coolstuff\\geo json\\ward\\yangon_ward.json";
				String countrywide_ward = "C:\\Users\\khine\\Documents\\coolstuff\\geo json\\ward\\countrywide_ward.json";

				//Geometry[] data1 = convert1(statePath,1);
				//Geometry[] data2 = convert1(districtPath,2);
				//Geometry[] data3 = convert1(tspPath,3);
				
				/*File folder = new File("C:\\Users\\khine\\Documents\\coolstuff\\geo json\\villagetrack");
				File[] listOfFiles = folder.listFiles();

				for (int i = 0; i < listOfFiles.length; i++) {
				  if (listOfFiles[i].isFile()) {
					  String path = listOfFiles[i].getAbsolutePath();
					  Geometry[] data4 = convert1(path,4);
						for (Geometry g : data4) {
							int row = insertVillage(g, conn);
							if (row == 0) {
								throw new Exception("fail");
							}
						}
				  } 
				}*/
				
				
				Geometry[] data1 = convert1(countrywide_ward,6);
				for (Geometry g : data1) {
					if(!g.getPcode().equals("")){
						int row = updateWard(g, conn);
						if (row == 0) {
							throw new Exception("fail");
						}
					}
					
				}
				conn.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static Geometry[] convert1(String path,int type) {
		try {
			
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(new FileReader(path));
			 
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray features = (JSONArray) jsonObject.get("features");
			Geometry[] mygeo = new Geometry[features.size()];
			for(int i = 0;i <features.size();i++){
				Geometry geometry = new Geometry();
				JSONObject feature = (JSONObject) features.get(i);
				JSONObject property = (JSONObject) feature.get("properties");
				JSONObject fgeometry = (JSONObject)feature.get("geometry");
				
				String str = (fgeometry).toJSONString();
				geometry.setGeoString(str);
				geometry.setType("MultiPolygon");
				geometry.setLocalId((String) feature.get("id"));
				geometry.setGeometryType((String) feature.get("geometry_name"));
				if(type==1){
					geometry.setName((String) property.get("ST"));
					geometry.setPcode((String) property.get("ST_PCODE"));
					geometry.setMmr4((String) property.get("Name_MMR4"));
					geometry.setParentPcode("");
					
				}else if(type==2){
					geometry.setName((String) property.get("DT"));
					geometry.setPcode((String) property.get("DT_PCODE"));
					geometry.setMmr4((String) property.get("DT_MMR4"));
					geometry.setShape_area((double) property.get("Shape_Area"));
					geometry.setShape_leng((double) property.get("Shape_Leng"));
					geometry.setParentPcode((String)property.get("ST_PCODE"));
				}else if(type==3){
					geometry.setName((String) property.get("TS"));
					geometry.setPcode((String) property.get("TS_PCODE"));
					geometry.setMmr4((String) property.get("TS_MMR4"));
					geometry.setParentPcode((String)property.get("DT_PCODE"));
					
				}else if(type==4){
					geometry.setName((String) property.get("VT"));
					geometry.setPcode((String) property.get("VT_PCODE"));
					geometry.setMmr4((String) property.get("VT_MYA_M3"));
					geometry.setParentPcode((String) property.get("TS_PCODE"));
				}
				
				else if(type==5){
					geometry.setName((String) property.get("Town"));
					geometry.setPcode((String) property.get("Town_Pcode"));
					geometry.setMmr4((String) property.get("VT_MYA_M3"));
				} else {
					geometry.setPcode((String) property.get("Ward_Pcode"));
				}
				
				mygeo[i] = geometry;
				System.out.println(geometry.toString());
			}
			return mygeo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void convert() {
		Polygon geoa = new Polygon(new LinearRing[] {
				new LinearRing(new Point[] { new Point(-1.0d, -1.0d, 0.5d), new Point(1.0d, -1.0d, 0.0d),
						new Point(1.0d, 1.0d, -0.5d), new Point(-1.0d, 1.0d, 0.0d), new Point(-1.0d, -1.0d, 0.5d) }) });
		Gson g = new Gson();
		System.out.println(g.toJson(geoa));
		return;

	}
	public static void insert(PreparedStatement stmt,Geometry data) throws SQLException{
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

	public static int insertState(Geometry data, Connection conn) throws SQLException {
		String sql = "INSERT INTO public.geo_state("
				+ "id,name, status, biz_status, cdate, mdate, type, geometry,p_pcode, pcode, mmr4, shape_leng, shape_area, geometry_type, local_id) "
				+ "VALUES (?,?, ?, ?, ?::timestamp, ?::timestamp, ?, ST_GeomFromGeoJSON(?),?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		insert(stmt,data);
		return stmt.executeUpdate();

	}

	public static int insertDistrict(Geometry data, Connection conn) throws SQLException {
		String sql = "INSERT INTO public.geo_district("
				+ "id,name, status, biz_status, cdate, mdate, type, geometry,p_pcode, pcode, mmr4, shape_leng, shape_area, geometry_type,"
				+ " local_id) " + "VALUES (?, ?, ?, ?, ?::timestamp, ?::timestamp, ?, ST_GeomFromGeoJSON(?), ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		insert(stmt,data);
		return stmt.executeUpdate();

	}
	
	public static int insertTsp(Geometry data, Connection conn) throws SQLException {
		String sql = "INSERT INTO public.geo_township("
				+ "id, name, status, biz_status, cdate, mdate, type, geometry, p_pcode, pcode, mmr4, shape_leng, shape_area, geometry_type,"
				+ " local_id) " + "VALUES (?, ?,?, ?, ?::timestamp, ?::timestamp, ?, ST_GeomFromGeoJSON(?), ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		insert(stmt,data);
		return stmt.executeUpdate();

	}
	
	public static int insertVillage(Geometry data, Connection conn) throws SQLException {
		String sql = "INSERT INTO public.temp("
				+ "id, name, status,"
				+ " biz_status, cdate, mdate,"
				+ " type, geometry, p_pcode,"
				+ " pcode, mmr4, shape_leng, "
				+ "shape_area, geometry_type,"
				+ " local_id) " + "VALUES (?, ?, ?, ?, ?::timestamp, ?::timestamp, ?, ST_GeomFromGeoJSON(?),?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		insert(stmt,data);
		return stmt.executeUpdate();

	}
	
	public static int insertTown(Geometry data, Connection conn) throws SQLException {
		String sql = "INSERT INTO public.geo_town("
				+ "id, name, status, biz_status, cdate, mdate, type, geometry, pcode, mmr4, shape_leng, shape_area, geometry_type,"
				+ " local_id) " + "VALUES (?, ?,?, ?, ?::timestamp, ?::timestamp, ?, ST_GeomFromGeoJSON(?), ?, ?, ?, ?, ?, ?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		insert(stmt,data);
		return stmt.executeUpdate();

	}
	public static int updateWard(Geometry data, Connection conn) throws SQLException {
		String sql = "UPDATE public.geo_ward SET geometry = ST_GeomFromGeoJSON(?) where pcode=?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, data.getGeoString());
		stmt.setString(2, data.getPcode());
		return stmt.executeUpdate();

	}
	
	public static String getData(Connection conn) throws SQLException {
		String sql = "select ST_ASTEXT(geometry) as poly from geo_district";
		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		if(rs.next()){
			return rs.getString("poly");
		}
		else return "";
	}

	public static Connection getConnection() {

		String url = "jdbc:postgresql://localhost:5432/coolstuff";
		Properties props = new Properties();
		props.setProperty("user", "postgres");
		props.setProperty("password", "123");
		try {
			Class.forName("org.postgresql.Driver");
			return DriverManager.getConnection(url, props);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
}
