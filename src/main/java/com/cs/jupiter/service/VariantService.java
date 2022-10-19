package com.cs.jupiter.service;

import java.sql.Connection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.jupiter.dao.VariantDao;
import com.cs.jupiter.model.interfaces.RequestCredential;
import com.cs.jupiter.model.interfaces.ViewResult;
import com.cs.jupiter.model.jun.ProductAttribute;
import com.cs.jupiter.model.table.ProductVariantOptionDetail;
import com.cs.jupiter.model.table.ProductVariantOptionHeader;
import com.cs.jupiter.model.table.ProductVariantTheme;
import com.cs.jupiter.utility.ComEnum;
import com.cs.jupiter.utility.KeyFactory;

@Service
public class VariantService {
	@Autowired
	VariantDao dao;

	/*
	 * public ViewResult<StockVariantThemeHeader>
	 * getStockVariantThemes(StockVariantThemeHeader data,RequestCredential
	 * cred, Connection conn) { ViewResult<StockVariantThemeHeader> result; try{
	 * 
	 * result.success(); }catch(Exception e){ result.error(e.getMessage()); } }
	 */

	public ViewResult<ProductVariantOptionHeader> saveStockVariantOption(ProductVariantOptionHeader data,
			RequestCredential cred, Connection conn) {
		ViewResult<ProductVariantOptionHeader> rtn = new ViewResult<>();
		try {
			if (data.getName().equals("") || data.getName() == null)
				throw new Exception("");
			data.setId(KeyFactory.getStringId());
			boolean res = dao.saveVariantOptionHeader(data, conn);
			if (!res)
				throw new Exception("");
			ViewResult<ProductVariantOptionDetail> detailResult;
			int count = 1;
			for (ProductVariantOptionDetail detail : data.getDetails()) {
				detail.setOrder(count++);
				detailResult = this.saveStockVariantOptionDetail(data.getId(), detail, conn);
				if (detailResult.status != ComEnum.ErrorStatus.Success.getCode()) {
					throw new Exception(detailResult.message);
				}
			}
			rtn.status = ComEnum.ErrorStatus.Success.getCode();
			rtn.data = data;
		} catch (Exception e) {
			e.printStackTrace();
			rtn.status = ComEnum.ErrorStatus.ServerError.getCode();
		}
		return rtn;
	}

	public ViewResult<ProductVariantOptionHeader> updateStockVariantOption(ProductVariantOptionHeader data,
			RequestCredential cred, Connection conn) {
		ViewResult<ProductVariantOptionHeader> rtn = new ViewResult<>();
		try {
			if (data.getName().equals("") || data.getName() == null)
				throw new Exception("");
			if (data.getId().equals("0") || data.getId().equals("") || data.getId() == null) {
				throw new Exception("");
			}
			boolean res = dao.updateVariantOptionHeader(data, conn);
			if (!res)
				throw new Exception("");
			ViewResult<ProductVariantOptionDetail> detailResult;
			for (ProductVariantOptionDetail detail : data.getDetails()) {
				if (data.getId().equals("") || data.getId().equals("0")) {
					detailResult = this.saveStockVariantOptionDetail(data.getId(), detail, conn);
				} else if (data.getId() == null) {
					throw new Exception("");
				} else {
					detailResult = this.updateStockVariantOptionDetail(detail, conn);
				}

				if (detailResult.status != ComEnum.ErrorStatus.Success.getCode()) {
					throw new Exception(detailResult.message);
				}
			}
			rtn.status = ComEnum.ErrorStatus.Success.getCode();
			rtn.data = data;
		} catch (Exception e) {
			e.printStackTrace();
			rtn.status = ComEnum.ErrorStatus.ServerError.getCode();
		}
		return rtn;
	}

	public ViewResult<String> deleteStockVariantOption(String id, RequestCredential cred, Connection conn) {
		ViewResult<String> rtn = new ViewResult<>();
		try {
			if (!dao.deleteOptionDetailByHeaderId(id, rtn, conn)) {
				throw new Exception(rtn.message);
			}
			if (!dao.deleteVariantOptionHeader(id, rtn, conn)) {
				throw new Exception(rtn.message);
			}
			rtn.status = ComEnum.ErrorStatus.Success.getCode();
		} catch (Exception e) {
			rtn.message = e.getMessage();
			rtn.status = ComEnum.ErrorStatus.ServerError.getCode();
		}
		return rtn;
	}

	private ViewResult<ProductVariantOptionDetail> saveStockVariantOptionDetail(String headerId,
			ProductVariantOptionDetail data, Connection conn) {
		ViewResult<ProductVariantOptionDetail> result = new ViewResult<>();
		try {
			data.setId(KeyFactory.getStringId());
			data.setHeader(new ProductVariantOptionHeader(headerId));
			if (!dao.saveOptionDetail(data, conn)) {
				throw new Exception("");
			}
			result.status = ComEnum.ErrorStatus.Success.getCode();

		} catch (Exception e) {
			e.printStackTrace();
			result.message = e.getMessage();
			result.status = ComEnum.ErrorStatus.ServerError.getCode();
		}
		return result;
	}

	private ViewResult<ProductVariantOptionDetail> updateStockVariantOptionDetail(ProductVariantOptionDetail data,
			Connection conn) {
		ViewResult<ProductVariantOptionDetail> result = new ViewResult<>();
		try {
			if (!dao.updateOptionDetail(data, conn)) {
				throw new Exception("");
			}
			result.status = ComEnum.ErrorStatus.Success.getCode();
		} catch (Exception e) {
			e.printStackTrace();
			result.message = e.getMessage();
			result.status = ComEnum.ErrorStatus.ServerError.getCode();
		}
		return result;
	}

	public ViewResult<ProductVariantOptionDetail> deleteStockVariantOptionDetail(String data, RequestCredential cred,
			Connection conn) {
		ViewResult<ProductVariantOptionDetail> rtn = new ViewResult<>();
		try {
			if (dao.deleteOptionDetailById(data, rtn, conn)) {
				rtn.status = ComEnum.ErrorStatus.Success.getCode();
			} else {
				rtn.status = ComEnum.ErrorStatus.ClientError.getCode();
			}
		} catch (Exception e) {
			e.printStackTrace();
			rtn.message = e.getMessage();
		}
		return rtn;
	}

	public ViewResult<ProductVariantOptionHeader> getOptionHeader(ProductVariantOptionHeader data, 
			String categoryId,
			RequestCredential cred, Connection conn) {
		ViewResult<ProductVariantOptionHeader> rtn = new ViewResult<>();
		try {
			if(data.getName()==null || data.getName().equals("")) {
				throw new Exception("option name must not be empty");
			}
			if(categoryId.equals("-1")) {
				rtn = dao.getOptionHeader(data, conn);
			}else{
				rtn = dao.getOptionHeaderUnderCategory(categoryId, data, conn);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			rtn.message = e.getMessage();
		}
		return rtn;
	}
	public ViewResult<ProductAttribute> getOptionHeaderCategory(String categoryId, 
			RequestCredential cred, Connection conn) {
		ViewResult<ProductAttribute> rtn ;
		try {
			rtn = dao.getOptionHeaderWithJunction(categoryId, conn);
		} catch (Exception e) {
			e.printStackTrace();
			rtn = new ViewResult<>(e);
		}
		return rtn;
	}

	public ViewResult<ProductVariantOptionDetail> getStockVariantOptionDetail(ProductVariantOptionDetail data,
			RequestCredential cred, Connection conn) {
		ViewResult<ProductVariantOptionDetail> rtn = new ViewResult<>();
		try {
			rtn = dao.getOptionDetail(data, conn);
		} catch (Exception e) {
			rtn.error(e.getMessage());
		}
		return rtn;
	}

	public ViewResult<ProductVariantTheme> getStockVariantTheme(ProductVariantTheme data, RequestCredential cred,
			Connection conn) {
		ViewResult<ProductVariantTheme> result;
		try {
			result = dao.getStockVariantTheme(data, conn);
			result.success();
		} catch (Exception e) {
			result = new ViewResult<>(e);
		}
		return result;
	}

	public ViewResult<ProductVariantTheme> saveTheme(ProductVariantTheme data, RequestCredential cred,
			Connection conn) {
		ViewResult<ProductVariantTheme> result;
		try {
			Date today = new Date();
			data.setMdate(today);
			data.setCdate(today);
			data.setId(KeyFactory.getStringId());
			data.setStatus(1);
			data.setOrder(0);
			result = dao.saveVariantTheme(data, conn);
			result.success();
		} catch (Exception e) {
			result = new ViewResult<>(e);
		}
		return result;
	}
}
