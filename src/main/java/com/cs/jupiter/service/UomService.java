package com.cs.jupiter.service;
import java.sql.Connection;
import java.util.Date;

import javax.sql.DataSource;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.jupiter.component.CrudTemplate;
import com.cs.jupiter.dao.UomDao;
import com.cs.jupiter.model.interfaces.RequestCredential;
import com.cs.jupiter.model.interfaces.ViewResult;
import com.cs.jupiter.model.table.Uom;
import com.cs.jupiter.utility.ComEnum;
import com.cs.jupiter.utility.KeyFactory;

@Service
public class UomService implements CrudTemplate<Uom> {
	private static final Logger logger = LoggerFactory.getLogger(UomService.class);
	@Autowired
	DataSource ds;

	@Autowired
	UomDao uomDao;

	@Override
	public ViewResult<Uom> save(Uom data, RequestCredential cred,Connection conn) {
		Date curDate = new Date();
		ViewResult<Uom> vr ;
		try {
			data.setId(Long.toString(KeyFactory.getId()));
			data.setStatus(ComEnum.RowStatus.Normal.getCode());
			data.setCdate(curDate);
			data.setMdate(curDate);
			vr = uomDao.insert(data, conn);
		} catch (Exception e) {
			vr  = new ViewResult<Uom>();
			vr.status = ComEnum.ErrorStatus.ServerError.getCode();
			vr.message = e.getMessage();
			logger.info("Uom_Service");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return vr;
	}

	@Override
	public ViewResult<Uom> deleteAll(RequestCredential crd,Connection conn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ViewResult<Uom> deleteById(Uom data, RequestCredential crd,Connection conn) {
		ViewResult<Uom> vr ;
		try {
			vr = uomDao.delete(data, conn) ;
			vr.status = ComEnum.ErrorStatus.Success.getCode();
		} catch (Exception e) {
			vr = new ViewResult<Uom>();
			vr.status = ComEnum.ErrorStatus.ServerError.getCode();
			vr.message = e.getMessage();
			logger.info("Uom_Service");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return vr;
	}

	@Override
	public ViewResult<Uom> update(Uom data, RequestCredential crd,Connection conn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ViewResult<Uom> updateById(Uom data, RequestCredential crd,Connection conn) {
		Date curDate = new Date();
		ViewResult<Uom> vr ;

		try {
			if (!data.getId().equals("-1") && !data.getId().equals("") && data.getId() != null) {
				data.setMdate(curDate);
				vr = uomDao.update(data, conn);
				
			} else {
				throw new Exception("Invalid Uom");
			}

		} catch (Exception e) {
			vr = new ViewResult<Uom>();
			vr.status = ComEnum.ErrorStatus.ServerError.getCode();
			vr.message = e.getMessage();
			logger.info("Uom_Service");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return vr;
	}

	@Override
	public ViewResult<Uom> getAll(Uom data, RequestCredential crd,Connection conn) {
		ViewResult<Uom> vr;
		try {
			vr = uomDao.getAll(data, conn);
		} catch (Exception e) {
			vr  = new ViewResult<Uom>();
			vr.status = ComEnum.ErrorStatus.ServerError.getCode();
			vr.message = e.getMessage();
			logger.info("Uom_Service");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return vr;
	}

	@Override
	public ViewResult<Uom> inactive(Uom data, RequestCredential crd,Connection conn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ViewResult<Uom> getById(String id, RequestCredential crd,Connection conn) {
		ViewResult<Uom> vr ;
		try {
			Uom cri = new Uom();
			cri.setId(id);
			vr = uomDao.getAll(cri, conn);
			if (vr.list.size() > 0) {
				vr.data = vr.list.get(0);
				vr.status = ComEnum.ErrorStatus.Success.getCode();
			} else {
				vr.data = null;
				vr.status = ComEnum.ErrorStatus.ClientError.getCode();
			}

		} catch (Exception e) {
			vr  = new ViewResult<Uom>();
			vr.status = ComEnum.ErrorStatus.ServerError.getCode();
			vr.message = e.getMessage();
			logger.info("Uom_Service");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return vr;
	}

}
