package com.cs.jupiter.service;
import java.sql.Connection;
import java.util.Date;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.jupiter.component.CrudTemplate;
import com.cs.jupiter.dao.PackTypeDao;
import com.cs.jupiter.model.interfaces.RequestCredential;
import com.cs.jupiter.model.interfaces.ViewResult;
import com.cs.jupiter.model.table.PackType;
import com.cs.jupiter.utility.ComEnum;
import com.cs.jupiter.utility.KeyFactory;

@Service
public class PackTypeService implements CrudTemplate<PackType> {
	private static final Logger logger = LoggerFactory.getLogger(PackTypeService.class);


	@Autowired
	PackTypeDao ptDao;

	@Override
	public ViewResult<PackType> save(PackType data, RequestCredential cred,Connection conn) {
		Date curDate = new Date();
		ViewResult<PackType> vr ;
		try {
			data.setId(Long.toString(KeyFactory.getId()));
			data.setStatus(ComEnum.RowStatus.Normal.getCode());
			data.setCdate(curDate);
			data.setMdate(curDate);
			vr = ptDao.insert(data, conn);
		} catch (Exception e) {
			vr  = new ViewResult<PackType>();
			vr.status = ComEnum.ErrorStatus.ServerError.getCode();
			vr.message = e.getMessage();
			logger.info("PackType_Service");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return vr;
	}

	@Override
	public ViewResult<PackType> deleteAll(RequestCredential crd,Connection conn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ViewResult<PackType> deleteById(PackType data, RequestCredential crd,Connection conn) {
		ViewResult<PackType> vr ;
		try {
			vr = ptDao.delete(data, conn) ;
			vr.status = ComEnum.ErrorStatus.Success.getCode();
		} catch (Exception e) {
			vr = new ViewResult<PackType>();
			vr.status = ComEnum.ErrorStatus.ServerError.getCode();
			vr.message = e.getMessage();
			logger.info("PackType_Service");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return vr;
	}

	@Override
	public ViewResult<PackType> update(PackType data, RequestCredential crd,Connection conn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ViewResult<PackType> updateById(PackType data, RequestCredential crd,Connection conn) {
		Date curDate = new Date();
		ViewResult<PackType> vr ;

		try {
			if (!data.getId().equals("-1") && !data.getId().equals("") && data.getId() != null) {
				data.setMdate(curDate);
				vr = ptDao.update(data, conn);
				
			} else {
				throw new Exception("Invalid PackType");
			}

		} catch (Exception e) {
			vr = new ViewResult<PackType>();
			vr.status = ComEnum.ErrorStatus.ServerError.getCode();
			vr.message = e.getMessage();
			logger.info("PackType_Service");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return vr;
	}

	@Override
	public ViewResult<PackType> getAll(PackType data, RequestCredential crd,Connection conn) {
		ViewResult<PackType> vr;
		try {
			vr = ptDao.getAll(data, conn);
		} catch (Exception e) {
			vr  = new ViewResult<PackType>();
			vr.status = ComEnum.ErrorStatus.ServerError.getCode();
			vr.message = e.getMessage();
			logger.info("PackType_Service");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return vr;
	}

	@Override
	public ViewResult<PackType> inactive(PackType data, RequestCredential crd,Connection conn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ViewResult<PackType> getById(String id, RequestCredential crd,Connection conn) {
		ViewResult<PackType> vr ;
		try {
			PackType cri = new PackType();
			cri.setId(id);
			vr = ptDao.getAll(cri, conn);
			if (vr.list.size() > 0) {
				vr.data = vr.list.get(0);
				vr.status = ComEnum.ErrorStatus.Success.getCode();
			} else {
				vr.data = null;
				vr.status = ComEnum.ErrorStatus.ClientError.getCode();
			}

		} catch (Exception e) {
			vr  = new ViewResult<PackType>();
			vr.status = ComEnum.ErrorStatus.ServerError.getCode();
			vr.message = e.getMessage();
			logger.info("PackType_Service");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return vr;
	}

}
