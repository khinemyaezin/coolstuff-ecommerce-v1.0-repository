package com.cs.jupiter.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.jupiter.dao.CategoryDao;
import com.cs.jupiter.model.interfaces.RequestCredential;
import com.cs.jupiter.model.interfaces.ViewResult;
import com.cs.jupiter.model.jun.ProductAttributeSetup;
import com.cs.jupiter.model.jun.SurroundCategory;
import com.cs.jupiter.model.table.Category;
import com.cs.jupiter.model.table.CategoryVariantOption;
import com.cs.jupiter.utility.ComEnum;
import com.cs.jupiter.utility.KeyFactory;

@Service
public class CategoryService {
	private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

	@Autowired
	CategoryDao catDao;
	
	public ViewResult<Category> getCategoryByDepth(int depth, RequestCredential cred, Connection conn) {
		 ViewResult<Category> result = new ViewResult<>();
		try{
			result = catDao.getCategoryByDepth(depth, conn);
		}catch(Exception e){
			result.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	public ViewResult<Category> save(Category data, RequestCredential cred, Connection conn) {
		 ViewResult<Category> rs = new ViewResult<>();
		try{
			int id = catDao.addNodeCategory(data, conn);
			if( id == -1){
				throw new Exception( "");
			} else {
				rs.status = ComEnum.ErrorStatus.Success.getCode();
				rs.data = new Category();
				rs.data.setId(Integer.toString(id));
				rs.data.setName(data.getName());
			}
		}catch(Exception e){
			e.printStackTrace();
			rs.status = ComEnum.ErrorStatus.ServerError.getCode();
		}
		return rs;
	}


	public ViewResult<Category> getAll(Category data, RequestCredential crd, Connection conn) {
		ViewResult<Category> vr = new ViewResult<>();
		try {
			vr.list = catDao.getDepthAllNodes(conn);
			vr.status = ComEnum.ErrorStatus.Success.getCode();
		} catch (Exception e) {
			vr = new ViewResult<Category>();
			vr.status = ComEnum.ErrorStatus.ServerError.getCode();
			vr.message = e.getMessage();
			logger.info("Category_Service");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return vr;
	}
	public ViewResult<Category> searchCategoryLeaf(String data, RequestCredential crd, Connection conn) {
		ViewResult<Category> vr = new ViewResult<>();
		try {
			vr.list = catDao.searchCategoryLeaf(data, conn);
			vr.status = ComEnum.ErrorStatus.Success.getCode();
		} catch (Exception e) {
			vr = new ViewResult<Category>();
			vr.status = ComEnum.ErrorStatus.ServerError.getCode();
			vr.message = e.getMessage();
			logger.info("Category_Service");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return vr;
	}

	
	public ViewResult<Category> getOneLayerOfGivenId(Category data, RequestCredential crd, Connection conn){
		ViewResult<Category> vr = new ViewResult<>();
		try {
			vr.list = catDao.findTheImmediateSubordinatiesOfANode(data.getId(), conn);
			vr.status = ComEnum.ErrorStatus.Success.getCode();
		} catch (Exception e) {
			vr = new ViewResult<>(e);
			logger.info("Category_Service");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return vr;
	}
	public ViewResult<Category> getCategoryLeaf(Category data,RequestCredential crd, Connection conn){
		ViewResult<Category> vr = new ViewResult<>();
		try {
			if(data.getId()!=null && !data.getId().equals("-1")){
				vr = catDao.getCategoryLeafById(data.getId(), conn);
			}else{
				vr = catDao.getCategoryLeaf(data.getPath(), conn);
			}
			
		} catch (Exception e) {
			vr.error(e.getMessage());
			
		}
		return vr;
	}
	public ViewResult<Category> getTopLayer(Category data,RequestCredential crd, Connection conn){
		ViewResult<Category> vr ;
		try {
			vr = catDao.getTopLayer(data.getId(), conn);
		} catch (Exception e) {
			vr = new ViewResult<>(e);
		}
		return vr;
	}
	public ViewResult<SurroundCategory> getSurroundCategory(Category data,RequestCredential crd, Connection conn){
		ViewResult<SurroundCategory> vr  = new ViewResult<>();
		try {
			SurroundCategory result = new SurroundCategory();
			 ViewResult<Category> topResult = catDao.getTopLayer(data.getId(), conn);
			 topResult.list.forEach( top->{
				 if(!top.getId().equals(data.getId()) && !top.getId().equals("1"))
					 result.getUpperLayer().add(top);
			 });
			 List<Category> lowerResult = catDao.findTheImmediateSubordinatiesOfANode(data.getId(), conn);
			 lowerResult.forEach( low->{
				 if(!low.getId().equals(data.getId())){
					 result.getLowerLayer().add(low);
				 }
			 });
			vr.success();
			vr.data = result;
			
		} catch (Exception e) {
			vr = new ViewResult<>(e);
		}
		return vr;
	}
	
	public ViewResult<String> generateLeaf(RequestCredential crd, Connection conn){
		ViewResult<String> result;
		try {
			result = catDao.generateLeaf(conn);
		} catch (Exception e) {
			result = new ViewResult<>(e);
			e.printStackTrace();
		}
		return result;
	}
	
	public ViewResult<CategoryVariantOption> saveCategoryVariantOption(ProductAttributeSetup data,
			RequestCredential cred, Connection conn) {
		ViewResult<CategoryVariantOption> result = new ViewResult<>(null,new ArrayList<>());
		Date today = new Date();
		try {
			//delete
			for(CategoryVariantOption option:data.getCategoryIdsToDelete()) {
				if(!catDao.deleteCategoryVariantOption(option,conn).isSucces()){
					throw new Exception("fail_to_save");
				}
			}
			//save
			for (CategoryVariantOption option : data.getCategoryOptions()) {
				option.setId(KeyFactory.getStringId());
				option.setCdate(today);
				option.setMdate(today);
				if(!catDao.saveCategoryVariantOption(option, conn).isSucces()){
					throw new Exception("fail_to_save");
				}
			}
			result.success();

		} catch (Exception e) {
			result = new ViewResult<>(e);
			e.printStackTrace();
		}
		return result;
	}

	public ViewResult<CategoryVariantOption> deleteCategoryVariantOption(CategoryVariantOption data,
			RequestCredential cred, Connection conn) {
		ViewResult<CategoryVariantOption> result;
		try {
			if (data.getId() == null || data.getId().equals("")) {
				throw new Exception("invalid_id");
			}
			result = catDao.deleteCategoryVariantOption(data, conn);
			if (result.isSucces()) {
				result.data = data;
			}
		} catch (Exception e) {
			result = new ViewResult<>(e);
			e.printStackTrace();
		}
		return result;
	}

}
