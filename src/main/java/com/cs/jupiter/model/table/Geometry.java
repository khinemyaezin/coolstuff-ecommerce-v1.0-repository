package com.cs.jupiter.model.table;

import org.geojson.Feature;
import org.postgis.PGgeometry;

import com.cs.jupiter.model.interfaces.ViewCredential;

public class Geometry extends ViewCredential {
	private String localId;
	private String type;
	private PGgeometry geometry;
	private String parentPcode;
	private String pcode;
	private String mmr4;
	private double shape_leng;
	private double shape_area;
	private String geometryType;
	private String geoString;
	private Feature feature;

	public String getType() {
		return type;
	}

	public String getGeoString() {
		return geoString;
	}

	public void setGeoString(String geoString) {
		this.geoString = geoString;
	}

	public PGgeometry getGeometry() {
		return geometry;
	}

	public String getPcode() {
		return pcode;
	}

	public String getMmr4() {
		return mmr4;
	}

	public double getShape_leng() {
		return shape_leng;
	}

	public double getShape_area() {
		return shape_area;
	}

	public String getGeometryType() {
		return geometryType;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setGeometry(PGgeometry geometry) {
		this.geometry = geometry;
	}

	public void setPcode(String pcode) {
		this.pcode = pcode;
	}

	public void setMmr4(String mmr4) {
		this.mmr4 = mmr4;
	}

	public void setShape_leng(double shape_leng) {
		this.shape_leng = shape_leng;
	}

	public void setShape_area(double shape_area) {
		this.shape_area = shape_area;
	}

	public void setGeometryType(String geometryType) {
		this.geometryType = geometryType;
	}

	public String getLocalId() {
		return localId;
	}

	public void setLocalId(String localId) {
		this.localId = localId;
	}

	public Feature getFeature() {
		return feature;
	}

	public void setFeature(Feature feature) {
		this.feature = feature;
	}

	public String getParentPcode() {
		return parentPcode;
	}

	public void setParentPcode(String parentPcode) {
		this.parentPcode = parentPcode;
	}

	@Override
	public String toString() {
		return "Geometry [name=" + this.getName() +", parentPcode=" + parentPcode + ", pcode=" + pcode + ", mmr4=" + mmr4 + "]";
	}

}
