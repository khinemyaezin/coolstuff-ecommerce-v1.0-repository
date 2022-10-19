package com.cs.jupiter.service;
import java.sql.Connection;
import java.util.Date;

import javax.sql.DataSource;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.jupiter.component.CrudTemplate;
import com.cs.jupiter.dao.PriceTypeDao;
import com.cs.jupiter.model.interfaces.RequestCredential;
import com.cs.jupiter.model.interfaces.ViewResult;
import com.cs.jupiter.model.table.PriceType;
import com.cs.jupiter.utility.ComEnum;
import com.cs.jupiter.utility.KeyFactory;

@Service
public class PriceTypeService implements CrudTemplate<PriceType> {
	private static final Logger logger = LoggerFactory.getLogger(PriceTypeService.class);
	@Autowired
	DataSource ds;

	@Autowired
	PriceTypeDao priceType;

	@Override
	public ViewResult<PriceType> save(PriceType data, RequestCredential cred,Connection conn) {
		Date curDate = new Date();
		ViewResult<PriceType> vr ;
		try {
			data.setId(Long.toString(KeyFactory.getId()));
			data.setStatus(ComEnum.RowStatus.Normal.getCode());
			data.setCdate(curDate);
			data.setMdate(curDate);
			vr = priceType.insert(data);
		} catch (Exception e) {
			vr  = new ViewResult<PriceType>();
			vr.status = ComEnum.ErrorStatus.ServerError.getCode();
			vr.message = e.getMessage();
			logger.info("PriceType_Service");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return vr;
	}

	@Override
	public ViewResult<PriceType> deleteAll(RequestCredential crd,Connection conn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ViewResult<PriceType> deleteById(PriceType data, RequestCredential crd,Connection conn) {
		ViewResult<PriceType> vr ;
		try {
			vr = priceType.delete(data) ;
			vr.status = ComEnum.ErrorStatus.Success.getCode();
		} catch (Exception e) {
			vr = new ViewResult<PriceType>();
			vr.status = ComEnum.ErrorStatus.ServerError.getCode();
			vr.message = e.getMessage();
			logger.info("PriceType_Service");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return vr;
	}

	@Override
	public ViewResult<PriceType> update(PriceType data, RequestCredential crd,Connection conn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ViewResult<PriceType> updateById(PriceType data, RequestCredential crd,Connection conn) {
		Date curDate = new Date();
		ViewResult<PriceType> vr ;

		try {
			if (!data.getId().equals("-1") && !data.getId().equals("") && data.getId() != null) {
				data.setMdate(curDate);
				vr = priceType.update(data);
				
			} else {
				throw new Exception("Invalid PriceType");
			}

		} catch (Exception e) {
			vr = new ViewResult<PriceType>();
			vr.status = ComEnum.ErrorStatus.ServerError.getCode();
			vr.message = e.getMessage();
			logger.info("PriceType_Service");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return vr;
	}

	@Override
	public ViewResult<PriceType> getAll(PriceType data, RequestCredential crd,Connection conn) {
		ViewResult<PriceType> vr;
		try {
			vr = priceType.getAll(data);
		} catch (Exception e) {
			vr  = new ViewResult<PriceType>();
			vr.status = ComEnum.ErrorStatus.ServerError.getCode();
			vr.message = e.getMessage();
			logger.info("PriceType_Service");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return vr;
	}

	@Override
	public ViewResult<PriceType> inactive(PriceType data, RequestCredential crd,Connection conn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ViewResult<PriceType> getById(String id, RequestCredential crd,Connection conn) {
		ViewResult<PriceType> vr ;
		try {
			PriceType cri = new PriceType();
			cri.setId(id);
			vr = priceType.getAll(cri);
			if (vr.list.size() > 0) {
				vr.data = vr.list.get(0);
				vr.status = ComEnum.ErrorStatus.Success.getCode();
			} else {
				vr.data = null;
				vr.status = ComEnum.ErrorStatus.ClientError.getCode();
			}

		} catch (Exception e) {
			vr  = new ViewResult<PriceType>();
			vr.status = ComEnum.ErrorStatus.ServerError.getCode();
			vr.message = e.getMessage();
			logger.info("PriceType_Service");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return vr;
	}

}
