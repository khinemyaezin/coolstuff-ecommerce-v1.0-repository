package com.cs.jupiter.service;

import java.sql.Connection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.jupiter.component.CrudTemplate;
import com.cs.jupiter.dao.UomStockDao;
import com.cs.jupiter.model.interfaces.RequestCredential;
import com.cs.jupiter.model.interfaces.ViewResult;
import com.cs.jupiter.model.table.UomStock;
import com.cs.jupiter.utility.ComEnum;
import com.cs.jupiter.utility.KeyFactory;

@Service
public class UomStockService implements CrudTemplate<UomStock>{
	@Autowired
	UomStockDao dao;
	
	@Override
	public ViewResult<UomStock> save(UomStock us, RequestCredential crd,Connection conn) {
		ViewResult<UomStock> rtn ;
		Date today = new Date();
		try{
			us.setId(  Long.toString(KeyFactory.getId()));
			us.setCdate( today);
			us.setMdate( today);
			us.setStatus( ComEnum.RowStatus.Normal.getCode());
			rtn = dao.insert(us, conn);
		}catch(Exception e){
			rtn = new ViewResult<>();
			rtn.status = ComEnum.ErrorStatus.ServerError.getCode();
			rtn.message = e.getMessage();
			e.printStackTrace();
		}
		return rtn;
	}

	@Override
	public ViewResult<UomStock> inactive(UomStock data, RequestCredential crd,Connection conn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ViewResult<UomStock> deleteAll(RequestCredential crd,Connection conn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ViewResult<UomStock> deleteById(UomStock data, RequestCredential crd,Connection conn) {
		// TODO Auto-generated method stub
		return null;
	}
	public ViewResult<UomStock> deleteByStockId(String data, RequestCredential crd,Connection conn) {
		ViewResult<UomStock> rtn ;
		try{
			rtn = dao.delete(data, conn);
		}catch(Exception e){
			rtn = new ViewResult<>();
			rtn.status = ComEnum.ErrorStatus.ServerError.getCode();
			rtn.message = e.getMessage();
			e.printStackTrace();
		}
		return rtn;
	}


	@Override
	public ViewResult<UomStock> update(UomStock data, RequestCredential crd,Connection conn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ViewResult<UomStock> updateById(UomStock data, RequestCredential crd,Connection conn) {
		ViewResult<UomStock> rtn ;
		Date today = new Date();
		try{
			data.setMdate( today);
			if(data.getStatus()== ComEnum.RowStatus.Deleted.getCode()){
				rtn = deleteById(data, crd, conn);
			}else{
				rtn = dao.update(data, conn);
			}
			
		}catch(Exception e){
			rtn = new ViewResult<>();
			rtn.status = ComEnum.ErrorStatus.ServerError.getCode();
			rtn.message = e.getMessage();
			e.printStackTrace();
		}
		return rtn;
	}

	@Override
	public ViewResult<UomStock> getAll(UomStock data, RequestCredential crd,Connection conn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ViewResult<UomStock> getById(String id, RequestCredential crd,Connection conn) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
