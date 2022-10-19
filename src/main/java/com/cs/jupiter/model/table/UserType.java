package com.cs.jupiter.model.table;

import com.cs.jupiter.model.interfaces.ViewCredential;

public class UserType extends ViewCredential{
	private int refBrand;
	private String authPath;
	
	public UserType(){
		super();
		this.refBrand = 0;
	}

	public int getRefBrand() {
		return refBrand;
	}

	public void setRefBrand(int refBrand) {
		this.refBrand = refBrand;
	}

	public String getAuthPath() {
		return authPath;
	}

	public void setAuthPath(String authPath) {
		this.authPath = authPath;
	}
	
}
