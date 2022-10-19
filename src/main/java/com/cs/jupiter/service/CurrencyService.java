package com.cs.jupiter.service;

import java.sql.Connection;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.jupiter.component.CrudTemplate;
import com.cs.jupiter.dao.CurrencyDao;
import com.cs.jupiter.model.interfaces.RequestCredential;
import com.cs.jupiter.model.interfaces.ViewResult;
import com.cs.jupiter.model.table.Currency;
import com.cs.jupiter.utility.ComEnum;
import com.cs.jupiter.utility.KeyFactory;

@Service
public class CurrencyService implements CrudTemplate<Currency> {
	private static final Logger logger = LoggerFactory.getLogger(CurrencyService.class);
	
	@Autowired
	CurrencyDao curDao;
	
	@Override
	public ViewResult<Currency> save(Currency data, RequestCredential crd, Connection conn) {
		Date curDate = new Date();
		ViewResult<Currency> vr ;
		try {
			data.setId(Long.toString(KeyFactory.getId()));
			data.setStatus(ComEnum.RowStatus.Normal.getCode());
			data.setCdate(curDate);
			data.setMdate(curDate);
			vr = curDao.insert(data, conn);
		} catch (Exception e) {
			vr  = new ViewResult<Currency>();
			vr.status = ComEnum.ErrorStatus.ServerError.getCode();
			vr.message = e.getMessage();
			logger.info("Currency_Service");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return vr;
	}

	@Override
	public ViewResult<Currency> inactive(Currency data, RequestCredential crd, Connection conn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ViewResult<Currency> deleteAll(RequestCredential crd, Connection conn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ViewResult<Currency> deleteById(Currency data, RequestCredential crd, Connection conn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ViewResult<Currency> update(Currency data, RequestCredential crd, Connection conn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ViewResult<Currency> updateById(Currency data, RequestCredential crd, Connection conn) {
		Date curDate = new Date();
		ViewResult<Currency> vr ;

		try {
			if (!data.getId().equals("-1") && !data.getId().equals("") && data.getId() != null) {
				data.setMdate(curDate);
				vr = curDao.update(data, conn);
				
			} else {
				throw new Exception("Invalid BrandOwner");
			}

		} catch (Exception e) {
			vr = new ViewResult<Currency>();
			vr.status = ComEnum.ErrorStatus.ServerError.getCode();
			vr.message = e.getMessage();
			logger.info("BrandOwner_Service");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return vr;
	}

	@Override
	public ViewResult<Currency> getAll(Currency data, RequestCredential crd, Connection conn) {
		ViewResult<Currency> vr;
		try {
			vr = curDao.getAll(data, conn);
		} catch (Exception e) {
			vr  = new ViewResult<Currency>();
			vr.status = ComEnum.ErrorStatus.ServerError.getCode();
			vr.message = e.getMessage();
			logger.info("Currency_Service");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return vr;
	}

	@Override
	public ViewResult<Currency> getById(String id, RequestCredential crd, Connection conn) {
		ViewResult<Currency> vr ;
		try {
			Currency cri = new Currency();
			cri.setId(id);
			vr = curDao.getAll(cri, conn);
			if (vr.list.size() > 0) {
				vr.data = vr.list.get(0);
				vr.status = ComEnum.ErrorStatus.Success.getCode();
			} else {
				vr.data = null;
				vr.status = ComEnum.ErrorStatus.ClientError.getCode();
			}

		} catch (Exception e) {
			vr  = new ViewResult<Currency>();
			vr.status = ComEnum.ErrorStatus.ServerError.getCode();
			vr.message = e.getMessage();
			logger.info("Currency_Service");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return vr;
	}
	
}
