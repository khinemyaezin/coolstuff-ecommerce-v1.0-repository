package com.cs.jupiter.sesetup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cs.jupiter.model.table.Geometry;
import com.cs.jupiter.utility.CommonUtility;
import com.cs.jupiter.utility.KeyFactory;

public class Excel {

	public static void main(String[] args) {

		start();

	}

	public static void start() {
		Connection conn = getConnection();
		if (conn != null) {

			try {
				conn.setAutoCommit(false);
				List<Geometry> data = read();
				for (Geometry g : data) {
					int row = insert(g, conn);
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

	public static List<Geometry> read() {
		try {
			String path = "C:\\Users\\khine\\Documents\\Github repo\\mimu\\Myanmar PCodes Release_9.3_Jan2021_Countrywide.xlsx";
			@SuppressWarnings("resource")
			XSSFWorkbook workbook = new XSSFWorkbook(CommonUtility.readFile(path));
			XSSFSheet worksheet = workbook.getSheetAt(4);
			List<Geometry> dataList = new ArrayList<>();
			for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
				Geometry data = new Geometry();
				XSSFRow row = worksheet.getRow(i);
				try {
					
						data.setPcode( row.getCell(6).getStringCellValue());
						data.setMmr4(row.getCell(8).getStringCellValue());
						data.setName(row.getCell(7).getStringCellValue());
						data.setParentPcode(row.getCell(4).getStringCellValue());
						try{
							data.setGeoString(CommonUtility.formatGeoPoint(
									row.getCell(9).getNumericCellValue()
									, 
									row.getCell(10).getNumericCellValue()
									));
						}catch(Exception e){
							System.out.println("remove");
						}
						
						dataList.add(data);
						System.out.println(data.toString());
					
					
				} catch (NullPointerException e) {
					break;
				}

				
			}
			return dataList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void insert(PreparedStatement stmt, Geometry data) throws SQLException {
		int i = 1;
		stmt.setLong(i++, KeyFactory.getId());
		stmt.setString(i++, data.getName());
		stmt.setInt(i++, 1);
		stmt.setInt(i++, 1);
		stmt.setString(i++, CommonUtility.convertTimeStamp_2db(new Date()));
		stmt.setString(i++, CommonUtility.convertTimeStamp_2db(new Date()));
		stmt.setString(i++, data.getType() == null ? "" : data.getType());
		stmt.setString(i++, data.getParentPcode());
		stmt.setString(i++, data.getPcode());
		stmt.setString(i++, data.getMmr4());
		stmt.setDouble(i++, data.getShape_leng());
		stmt.setDouble(i++, data.getShape_area());
		stmt.setString(i++, data.getGeometryType() == null ? "" : data.getGeometryType());
		stmt.setString(i++, data.getLocalId() == null ? "" : data.getType());
		stmt.setString(i++, data.getGeoString());
	}

	public static int insert(Geometry data, Connection conn) throws SQLException {
		String sql = "INSERT INTO public.geo_town("
				+ "id,name, status, biz_status, cdate, mdate, type, p_pcode, pcode, mmr4, shape_leng, shape_area, geometry_type, local_id, geometry) "
				+ "VALUES (?,?, ?, ?, ?::timestamp, ?::timestamp, ?, ?, ?, ?, ?, ?, ?, ?, ST_GeomFromText(?))";
		PreparedStatement stmt = conn.prepareStatement(sql);
		insert(stmt, data);
		return stmt.executeUpdate();

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
