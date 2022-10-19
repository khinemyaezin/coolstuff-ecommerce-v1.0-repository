package com.cs.jupiter.service;

import java.sql.Connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.jupiter.dao.GeometryDao;
import com.cs.jupiter.model.interfaces.RequestCredential;
import com.cs.jupiter.model.interfaces.ViewResult;
import com.cs.jupiter.model.jun.MyanmarGeometry;
import com.cs.jupiter.model.table.Geometry;

@Service
public class GeometryService {
	@Autowired
	GeometryDao dao;

	public ViewResult<MyanmarGeometry> locatePosition(double lat, double lng, RequestCredential cred, Connection conn) {
		ViewResult<MyanmarGeometry> result;
		try {
			ViewResult<MyanmarGeometry> resultTownship = dao.locatePositionToTownship(lat, lng, conn);
			if (!resultTownship.isSucces()) {

			}
			MyanmarGeometry data = resultTownship.data;
			if (data.getTownship().getPcode() != null
					&& !data.getTownship().getPcode().equals("")) {
				dao.locatePositionToTown(lat, lng,
						data.getTownship().getPcode(), data, conn);
				if(data.getTown()==null){
					//1 data by ward boundries
					//2 try to get by range
					ViewResult<Geometry> townMappingVillage = dao.townMappingVillage(lat, lng, conn);
					if(townMappingVillage.isSucces()){
						data.setTown(townMappingVillage.data);
						data.setWardList( dao.getWard(data.getTown().getPcode(), conn).list);
					}
				}
				if(data.getTown()==null){
					ViewResult<Geometry> villageByRange = dao.villageByRange(lat, lng,data, conn);
					if(villageByRange.isSucces() && villageByRange.data !=null){
						data.setVillage(villageByRange.data);
					}
					if(villageByRange.isSucces() && villageByRange.data ==null){
						data.setVillageList(villageByRange.list);
					}
				}
			}

			result = resultTownship;
		} catch (Exception e) {
			e.printStackTrace();
			result = new ViewResult<>(e);
		}
		return result;
	}
	public ViewResult<Geometry> getTown(String tspPcode, RequestCredential cred, Connection conn) {
		ViewResult<Geometry> result;
		try {
			result = dao.getTown(tspPcode, conn);
		} catch (Exception e) {
			e.printStackTrace();
			result = new ViewResult<>(e);
		}
		return result;
	}
	public ViewResult<Geometry> getWard(String tPcode, RequestCredential cred, Connection conn) {
		ViewResult<Geometry> result;
		try {
			result = dao.getWard(tPcode, conn);
		} catch (Exception e) {
			e.printStackTrace();
			result = new ViewResult<>(e);
		}
		return result;
	}
}
