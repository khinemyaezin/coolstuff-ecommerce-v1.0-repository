package com.cs.jupiter.model.table;


import com.cs.jupiter.model.interfaces.ViewCredential;

public class Category extends ViewCredential{
	private int lft;
	private int rgt;
	private int depth;
	private String path;
	private String parentId; //category id by depth level to insert product attributes
	private String[] searchNames;
	public Category(){
		lft = -1;
		rgt = -1;
		depth = -1;
		this.path = "";
		
	}
	public Category(String id){
		this.setId(id);
	}

	public int getLft() {
		return lft;
	}

	public void setLft(int lft) {
		this.lft = lft;
	}

	public int getRgt() {
		return rgt;
	}

	public void setRgt(int rgt) {
		this.rgt = rgt;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String[] getSearchNames() {
		return searchNames;
	}

	public void setSearchNames(String[] searchNames) {
		this.searchNames = searchNames;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
}
