package com.cs.jupiter.sesetup;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.GeoJsonObject;
import org.geojson.LngLatAlt;
import org.geojson.MultiPolygon;
import org.postgis.LinearRing;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.postgis.Polygon;

import com.cs.jupiter.model.table.Geometry;
import com.cs.jupiter.utility.CommonUtility;
import com.cs.jupiter.utility.KeyFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class GeoLocation {
	public static Gson gson = new Gson();

	public static void main(String[] args) {
		Connection conn = getConnection();
		if (conn != null) {

			try {
				conn.setAutoCommit(false);
				String statePath = "C:\\Users\\khine\\Documents\\coolstuff\\geo json\\state.json";
				String districtPath = "C:\\Users\\khine\\Desktop\\district.json";
				String tspPath = "C:\\Users\\khine\\Documents\\coolstuff\\geo json\\tsp.json";

				Geometry[] data = convert1(districtPath);
				for (Geometry g : data) {

					int row = insertDistrict(g, conn);
					if (row == 0) {
						throw new Exception("fail");
					}
				}
				
				conn.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static Geometry[] convert1(String path) {
		try {

			InputStream stream = new FileInputStream(path);
			FeatureCollection featureCollection = new ObjectMapper().readValue(stream, FeatureCollection.class);
			Geometry[] mygeo = new Geometry[featureCollection.getFeatures().size()];
			for (int i = 0; i < featureCollection.getFeatures().size(); i++) {
				Geometry geometry = new Geometry();
				Feature f = featureCollection.getFeatures().get(i);
				GeoJsonObject geoJsonObject = f.getGeometry();
				if (geoJsonObject instanceof MultiPolygon) {

					MultiPolygon polygon = (MultiPolygon) geoJsonObject;
					List<List<List<LngLatAlt>>> corrdinates = polygon.getCoordinates();
					for (int firstCount = 0; firstCount < corrdinates.size(); firstCount++) {
						LinearRing[] secRing = new LinearRing[corrdinates.get(firstCount).size()];
						
						for (int secCount = 0; secCount < corrdinates.get(firstCount).size(); secCount++) {
							Point[] points = new Point[corrdinates.get(firstCount).get(secCount).size()];
							for (int thirdCount = 0; thirdCount < corrdinates.get(firstCount).get(secCount)
									.size(); thirdCount++) {
								LngLatAlt lngLat = corrdinates.get(firstCount).get(secCount).get(thirdCount);
								points[thirdCount] = new Point(lngLat.getLongitude(), lngLat.getLatitude());
							}
							secRing[secCount] = new LinearRing(points);
						}
						geometry.setGeometry(new PGgeometry(new Polygon(secRing)));

					}
					geometry.setType("MultiPolygon");
					geometry.setName(f.getProperty("DT"));
					geometry.setPcode(f.getProperty("DT_PCODE"));
					geometry.setMmr4(f.getProperty("DT_MMR4"));
					geometry.setShape_area(f.getProperty("Shape_Area"));
					geometry.setShape_leng(f.getProperty("Shape_Leng"));
					geometry.setGeometryType("the_geom");
					geometry.setLocalId(f.getId());

				}
				mygeo[i] = geometry;

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

	public static int insertState(Geometry data, Connection conn) throws SQLException {
		String sql = "INSERT INTO public.geo_state("
				+ "id, status, biz_status, cdate, mdate, type, geometry, pcode, mmr4, shape_leng, shape_area, geometry_type, local_id) "
				+ "VALUES (?, ?, ?, ?::timestamp, ?::timestamp, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		int i = 1;
		stmt.setLong(i++, KeyFactory.getId());
		stmt.setInt(i++, 1);
		stmt.setInt(i++, 1);
		stmt.setString(i++, CommonUtility.convertTimeStamp_2db(new Date()));
		stmt.setString(i++, CommonUtility.convertTimeStamp_2db(new Date()));
		stmt.setString(i++, data.getType());
		stmt.setObject(i++, data.getGeometry());
		stmt.setString(i++, data.getPcode());
		stmt.setString(i++, data.getMmr4());
		stmt.setDouble(i++, data.getShape_leng());
		stmt.setDouble(i++, data.getShape_area());
		stmt.setString(i++, data.getGeometryType());
		stmt.setString(i++, data.getLocalId());

		return stmt.executeUpdate();

	}

	public static int insertDistrict(Geometry data, Connection conn) throws SQLException {
		String sql = "INSERT INTO public.geo_district("
				+ "id, status, biz_status, cdate, mdate, type, geometry, pcode, mmr4, shape_leng, shape_area, geometry_type,"
				+ " local_id) " + "VALUES (?, ?, ?, ?::timestamp, ?::timestamp, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		int i = 1;
		stmt.setLong(i++, KeyFactory.getId());
		stmt.setInt(i++, 1);
		stmt.setInt(i++, 1);
		stmt.setString(i++, CommonUtility.convertTimeStamp_2db(new Date()));
		stmt.setString(i++, CommonUtility.convertTimeStamp_2db(new Date()));
		stmt.setString(i++, data.getType());
		stmt.setString(i++, data.getGeoString());
		stmt.setString(i++, data.getPcode());
		stmt.setString(i++, data.getMmr4());
		stmt.setDouble(i++, data.getShape_leng());
		stmt.setDouble(i++, data.getShape_area());
		stmt.setString(i++, data.getGeometryType());
		stmt.setString(i++, data.getLocalId());

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
