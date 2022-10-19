package com.cs.jupiter.service;

import java.sql.Connection;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.cs.jupiter.dao.ImageDao;
import com.cs.jupiter.dao.UserDao;
import com.cs.jupiter.model.interfaces.RequestCredential;
import com.cs.jupiter.model.interfaces.ViewResult;
import com.cs.jupiter.model.interfaces.Auth;
import com.cs.jupiter.model.table.ImageData;
import com.cs.jupiter.model.table.Nrc;
import com.cs.jupiter.model.table.NrcDistrict;
import com.cs.jupiter.model.table.User;
import com.cs.jupiter.model.table.UserType;
import com.cs.jupiter.utility.AuthGuard;
import com.cs.jupiter.utility.ComEnum;
import com.cs.jupiter.utility.CommonUtility;
import com.cs.jupiter.utility.KeyFactory;

@Service
public class UserService {
	private static final Logger logger = LogManager.getLogger(UserService.class);
	@Autowired
	UserDao dao;
	@Autowired
	ImageService imgService;
	@Autowired
	ImageDao imgDao;
	@Autowired
	Environment env;


	public ViewResult<User> getUser(User data, RequestCredential cred, Connection conn) {
		ViewResult<User> rtn;
		try {
			rtn = dao.getUser(data, conn);
			if (rtn.status != ComEnum.ErrorStatus.Success.getCode()) {
				throw new Exception("");
			}
		} catch (Exception e) {
			rtn = new ViewResult<>();
			rtn.error(e.getMessage());
			e.printStackTrace();
			logger.info("[exception]",e.getMessage());
		}
		return rtn;
	}

	public ViewResult<User> registerUser(User data, RequestCredential cred, Connection conn) {
		ViewResult<User> rtn;
		Date today = new Date();
		try {
			data.setId(KeyFactory.getStringId());
			data.setCdate(today);
			data.setMdate(today);
			data.setPassword(AuthGuard.bcrypt(data.getPassword()));
			ViewResult<UserType> usertype = dao.selectUserTypeById(data.getUserType().getId(), conn);
			if (usertype.data == null)
				throw new Exception("invalid user type");

			rtn = dao.insertUser(data, conn);
			if(!rtn.isSucces()){
				if(rtn.message.contains("duplicate key value violates unique constraint")){
					rtn.message= "email or phone number already used";
				}
			}
		} catch (Exception e) {
			rtn = new ViewResult<>(e);
			e.printStackTrace();
		}
		return rtn;
	}

	public ViewResult<UserType> getUserTypeSetup(String parmCode,RequestCredential cred, Connection conn) {
		ViewResult<UserType> rtn;
		try {
			rtn = dao.selectUserType(parmCode,conn);
			if (rtn.status != ComEnum.ErrorStatus.Success.getCode()) {
				throw new Exception(rtn.message);
			}
			rtn.success();
		} catch (Exception e) {
			rtn = new ViewResult<>();
			rtn.error(e.getMessage());
			CommonUtility.outputLog(e.getMessage(), env);
		}
		return rtn;
	}

	public ViewResult<Nrc> getNrcState(RequestCredential cred, Connection conn) {
		ViewResult<Nrc> rtn;
		try {
			rtn = dao.getNrcState(conn);
			if (rtn.status != ComEnum.ErrorStatus.Success.getCode()) {
				throw new Exception(rtn.message);
			}
			rtn.success();
		} catch (Exception e) {
			rtn = new ViewResult<>();
			rtn.error(e.getMessage());
			CommonUtility.outputLog(e.getMessage(), env);
		}
		return rtn;
	}

	public ViewResult<NrcDistrict> getNrcDistrict(String stateId, RequestCredential cred, Connection conn) {
		ViewResult<NrcDistrict> rtn;
		try {
			rtn = dao.getNrcDistrict(stateId, conn);
			if (rtn.status != ComEnum.ErrorStatus.Success.getCode()) {
				throw new Exception(rtn.message);
			}
			rtn.success();
		} catch (Exception e) {
			rtn = new ViewResult<>();
			rtn.error(e.getMessage());
			CommonUtility.outputLog(e.getMessage(), env);
		}
		return rtn;
	}

	public ViewResult<User> updateAccountUser(User data, RequestCredential cred, Connection conn) {
		ViewResult<User> rtn;
		try {
			ViewResult<Auth> auth = confirmIdentity(data.getAuthencation(), cred , conn);
			if(!auth.isSucces()) throw new Exception(auth.message);
			rtn = dao.updateUser(data, conn);
			if(!rtn.isSucces()){
				throw new Exception(rtn.message);
			}
			rtn.success();
			rtn.data = data;
		} catch (Exception e) {
			rtn = new ViewResult<>();
			rtn.error(e.getMessage());
			CommonUtility.outputLog(e.getMessage(), env);
		}
		return rtn;
	}

	public ViewResult<User> updateAccountUserProfile(User data, RequestCredential cred, Connection conn) {
		ViewResult<User> rtn;
		try {
			if (data.getProfileImage() != null) {
				ViewResult<ImageData> img = imgDao.get(data.getProfileImage(), conn);
				if (img.isSucces()) {
					if (imgService.saveBase64(data.getProfileImage()) == null) {
						data.setProfileImage(null);
					} else {
						ImageData updatedImage = new ImageData();
						updatedImage.setId(img.data.getId());
						updatedImage.setPath(data.getProfileImage().getPath());
						updatedImage.setCode(data.getProfileImage().getCode());
						updatedImage.setName(data.getProfileImage().getName());

						imgDao.update(updatedImage, conn);
					}
				} else {
					if (imgService.saveBase64(data.getProfileImage()) == null) {
						data.setProfileImage(null);
					} else {
						img = imgDao.insert(data.getProfileImage(), conn);
						if (!img.isSucces())
							throw new Exception("invalid image data");
					}
				}
			}
			rtn = dao.updateUserProfile(data, conn);
			if (rtn.status != ComEnum.ErrorStatus.Success.getCode()) {
				throw new Exception(rtn.message);
			}
			rtn.success();
			rtn.data = data;
		} catch (Exception e) {
			rtn = new ViewResult<>();
			rtn.error(e.getMessage());
			CommonUtility.outputLog(e.getMessage(), env);
		}
		return rtn;
	}

	public ViewResult<User> changePassword(Auth data, RequestCredential cred, Connection conn) {
		ViewResult<User> rtn;
		try {

			ViewResult<Auth> authResult = confirmIdentity(data, cred, conn);
			if(!authResult.isSucces()) throw new Exception(authResult.message);
			User u = new User();
			u.setId(data.getId());
			u.setPassword(AuthGuard.bcrypt(data.getNewPassword()));
			rtn = dao.updateUserPassword(u, conn);
			if (!rtn.isSucces()) {
				throw new Exception("Fail to change password");
			}

		} catch (Exception e) {
			rtn = new ViewResult<>();
			rtn.error(e.getMessage());
			CommonUtility.outputLog(e.getMessage(), env);
		}
		return rtn;
	}

	public ViewResult<Auth> confirmIdentity(Auth data, RequestCredential cred, Connection conn) {
		ViewResult<Auth> result = new ViewResult<>(); 
		try {
			if(data.getId() ==null || data.getId().equals("") || data.getId().equals("-1")) {
				throw new Exception("Invalid data");
			}
			ViewResult<User> user = dao.getUserById(data.getId(), conn);
			if (!user.isSucces()) {
				throw new Exception("Invalid user");
			}
			if (!AuthGuard.bcryptCheck(data.getCurrentPassword(), user.data.getPassword())) {
				throw new Exception("Invalid old password");
			}
			result.success();
		} catch (Exception e) {
			result.error(e.getMessage());
		}
		return result;
	}
}
