package com.cs.jupiter.model.table;

import com.cs.jupiter.model.interfaces.ViewCredential;

public class NrcDistrict extends ViewCredential{
	private Nrc nrcState;
	public NrcDistrict(){
		super();
	}
	public Nrc getNrcState() {
		return nrcState;
	}
	public void setNrcState(Nrc nrcState) {
		this.nrcState = nrcState;
	}
}
