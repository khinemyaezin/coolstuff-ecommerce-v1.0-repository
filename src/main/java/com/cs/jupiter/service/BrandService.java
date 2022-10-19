package com.cs.jupiter.service;

import java.sql.Connection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.jupiter.model.interfaces.RequestCredential;
import com.cs.jupiter.model.interfaces.ViewResult;
import com.cs.jupiter.model.table.Brand;
import com.cs.jupiter.model.table.ImageData;
import com.cs.jupiter.model.table.User;
import com.cs.jupiter.utility.ComEnum;
import com.cs.jupiter.utility.ComEnum.RowStatus;
import com.cs.jupiter.utility.KeyFactory;
import com.cs.jupiter.dao.BrandDao;

@Service
public class BrandService {
	@Autowired
	BrandDao dao;

	@Autowired
	UserService userService;

	@Autowired
	ImageService imageService;

	public String generateCode() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 5; i++) {
			sb.append(KeyFactory.getRandomString(KeyFactory.alb_num, 4));
			if (i != 4) {
				sb.append("_");
			}
		}
		return sb.toString();
	}

	public ViewResult<Brand> saveBrand(Brand data, RequestCredential cred, Connection conn) {
		ViewResult<Brand> result;
		try {
			if (!validation(data))
				throw new Exception("INVALID_DATA");
			Date today = new Date();
			data.setCdate(today);
			data.setMdate(today);
			data.setId(KeyFactory.getStringId());
			data.setCode(generateCode());

			ImageData profilePath = imageService.saveBase64(data.getProfileImage());
			ImageData coverPath = imageService.saveBase64(data.getCoverImage());

			ViewResult<ImageData> imgResult;
			if (profilePath != null && coverPath != null) {
				imgResult = imageService.save(data.getProfileImage(), cred, conn);
				if (!imgResult.isSucces())
					throw new Exception("cannot proceed image data");
				imgResult = imageService.save(data.getCoverImage(), cred, conn);
				if (!imgResult.isSucces())
					throw new Exception("cannot proceed image data");

			} else {
				profilePath = new ImageData();
				coverPath = new ImageData();
				profilePath.setName(null);
				coverPath.setName(null);
			}
			data.setProfileImage(profilePath);
			data.setCoverImage(coverPath);
			result = dao.saveBrand(data, conn);

			if (result.status != ComEnum.ErrorStatus.Success.getCode()) {
				throw new Exception("");
			}
			ViewResult<User> userRtn = null;
			for (User user : data.getAccUsers()) {
				user.setBrand(data);
				userRtn = userService.registerUser(user, cred, conn);
				if (userRtn.status != ComEnum.ErrorStatus.Success.getCode()) {
					throw new Exception(userRtn.message);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			result = new ViewResult<>();
			result.error(e.getMessage());
		}
		return result;

	}

	private boolean validation(Brand data) {
		boolean ok = true;
		if (data.getName() == null || data.getName().equals(""))
			ok = false;
		if (data.getStatus() == -1)
			ok = false;
		return ok;
	}

	public ViewResult<Brand> updateBrand(Brand data, RequestCredential cred, Connection conn) {
		ViewResult<Brand> result;
		try {
			boolean valid = true;
			if (data.getId().equals("") || data.getId().equals("0"))
				valid = false;
			if (!valid)
				throw new Exception("INVALID_DATA");
			Date today = new Date();
			data.setMdate(today);
			result = dao.updateBrand(data, conn);
			if (result.status != ComEnum.ErrorStatus.Success.getCode()) {
				throw new Exception("");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = new ViewResult<>();
			result.error(e.getMessage());
		}
		return result;

	}

	public ViewResult<Brand> changeStatus(String data, String id, RequestCredential cred, Connection conn) {
		ViewResult<Brand> result;
		try {
			RowStatus status = ComEnum.RowStatus.valueOf(data);
			Brand brand = new Brand();
			brand.setId(id);
			brand.setStatus(status.getCode());
			result = dao.updateBrand(brand, conn);
			if (result.status != ComEnum.ErrorStatus.Success.getCode()) {
				throw new Exception("");
			}

		} catch (Exception e) {
			e.printStackTrace();
			result = new ViewResult<>();
			result.error(e.getMessage());
		}
		return result;

	}

	public ViewResult<Brand> delete(String id, RequestCredential cred, Connection conn) {
		ViewResult<Brand> result;
		try {
			result = dao.deleteBrand(id, conn);
			if (result.status != ComEnum.ErrorStatus.Success.getCode()) {
				throw new Exception("");
			}

		} catch (Exception e) {
			e.printStackTrace();
			result = new ViewResult<>();
			result.error(e.getMessage());
		}
		return result;

	}

	public ViewResult<Brand> getBrandTable(Brand data, RequestCredential cred, Connection conn) {
		ViewResult<Brand> result;
		try {
			result = dao.getBrandTable(data, conn);
			if (result.status != ComEnum.ErrorStatus.Success.getCode()) {
				throw new Exception("");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = new ViewResult<>();
			result.error(e.getMessage());
		}
		return result;

	}

	public ViewResult<Brand> getBrandById(Brand data, RequestCredential cred, Connection conn) {
		ViewResult<Brand> result;
		try {
			result = dao.getBrandById(data, conn);
			result.success();
		} catch (Exception e) {
			e.printStackTrace();
			result = new ViewResult<>();
			result.error(e.getMessage());
		}
		return result;

	}

	public ViewResult<Integer> getNewRegisterCount(Brand data, RequestCredential cred, Connection conn) {
		ViewResult<Integer> result;
		try {
			result = dao.getNewRegisterCount(data, conn);
			if (result.status != ComEnum.ErrorStatus.Success.getCode()) {
				throw new Exception("");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = new ViewResult<>();
			result.error(e.getMessage());
		}
		return result;

	}
}
