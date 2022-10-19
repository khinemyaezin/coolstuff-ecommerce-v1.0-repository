package com.cs.jupiter.service;
import java.sql.Connection;
import java.util.Date;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.jupiter.component.CrudTemplate;
import com.cs.jupiter.dao.PackSizeDao;
import com.cs.jupiter.model.interfaces.RequestCredential;
import com.cs.jupiter.model.interfaces.ViewResult;
import com.cs.jupiter.model.table.PackSize;
import com.cs.jupiter.utility.ComEnum;
import com.cs.jupiter.utility.KeyFactory;

@Service
public class PackSizeService implements CrudTemplate<PackSize> {
	private static final Logger logger = LoggerFactory.getLogger(PackSizeService.class);

	@Autowired
	PackSizeDao ptDao;

	@Override
	public ViewResult<PackSize> save(PackSize data, RequestCredential cred,Connection conn) {
		Date curDate = new Date();
		ViewResult<PackSize> vr ;
		try {
			data.setId(Long.toString(KeyFactory.getId()));
			data.setStatus(ComEnum.RowStatus.Normal.getCode());
			data.setCdate(curDate);
			data.setMdate(curDate);
			vr = ptDao.insert(data, conn);
		} catch (Exception e) {
			vr  = new ViewResult<PackSize>();
			vr.status = ComEnum.ErrorStatus.ServerError.getCode();
			vr.message = e.getMessage();
			logger.info("PackSize_Service");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return vr;
	}

	@Override
	public ViewResult<PackSize> deleteAll(RequestCredential crd,Connection conn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ViewResult<PackSize> deleteById(PackSize data, RequestCredential crd,Connection conn) {
		ViewResult<PackSize> vr ;
		try {
			vr = ptDao.delete(data, conn) ;
			vr.status = ComEnum.ErrorStatus.Success.getCode();
		} catch (Exception e) {
			vr = new ViewResult<PackSize>();
			vr.status = ComEnum.ErrorStatus.ServerError.getCode();
			vr.message = e.getMessage();
			logger.info("PackSize_Service");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return vr;
	}

	@Override
	public ViewResult<PackSize> update(PackSize data, RequestCredential crd,Connection conn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ViewResult<PackSize> updateById(PackSize data, RequestCredential crd,Connection conn) {
		Date curDate = new Date();
		ViewResult<PackSize> vr ;

		try {
			if (!data.getId().equals("-1") && !data.getId().equals("") && data.getId() != null) {
				data.setMdate(curDate);
				vr = ptDao.update(data, conn);
				
			} else {
				throw new Exception("Invalid PackSize");
			}

		} catch (Exception e) {
			vr = new ViewResult<PackSize>();
			vr.status = ComEnum.ErrorStatus.ServerError.getCode();
			vr.message = e.getMessage();
			logger.info("PackSize_Service");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return vr;
	}

	@Override
	public ViewResult<PackSize> getAll(PackSize data, RequestCredential crd,Connection conn) {
		ViewResult<PackSize> vr;
		try {
			vr = ptDao.getAll(data, conn);
		} catch (Exception e) {
			vr  = new ViewResult<PackSize>();
			vr.status = ComEnum.ErrorStatus.ServerError.getCode();
			vr.message = e.getMessage();
			logger.info("PackSize_Service");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return vr;
	}

	@Override
	public ViewResult<PackSize> inactive(PackSize data, RequestCredential crd,Connection conn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ViewResult<PackSize> getById(String id, RequestCredential crd,Connection conn) {
		ViewResult<PackSize> vr ;
		try {
			PackSize cri = new PackSize();
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
			vr  = new ViewResult<PackSize>();
			vr.status = ComEnum.ErrorStatus.ServerError.getCode();
			vr.message = e.getMessage();
			logger.info("PackSize_Service");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return vr;
	}

}
