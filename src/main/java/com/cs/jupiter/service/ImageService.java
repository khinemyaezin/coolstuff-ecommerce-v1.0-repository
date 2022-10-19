package com.cs.jupiter.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cs.jupiter.dao.ImageDao;
import com.cs.jupiter.model.interfaces.FileProperties;
import com.cs.jupiter.model.interfaces.RequestCredential;
import com.cs.jupiter.model.interfaces.ViewResult;
import com.cs.jupiter.model.table.ImageData;
import com.cs.jupiter.utility.ComEnum;
import com.cs.jupiter.utility.KeyFactory;


@Service
public class ImageService  {

	@Autowired
	ImageDao dao;
	


	@Autowired
	Environment env;

	public ViewResult<ImageData> save(ImageData data, RequestCredential crd, Connection conn) {
		ViewResult<ImageData> result;
		try {
			if(data!=null) {
				if(data.getId()!=null && !data.getId().equals("-1") && !data.getId().equals("")){
					result = dao.update(data, conn);
				}else{
					result = dao.insert(data, conn);
				}
			}else{
				result = new ViewResult<>(new Exception("invalid_error"));
			}
		} catch (Exception e) {
			result = new ViewResult<>();
		}
		return result;
	}
	public ViewResult<ImageData> completeSave(ImageData data, RequestCredential crd, Connection conn) {
		ViewResult<ImageData> result;
		try {
			if(data!=null) {
				ImageData base64Result = this.saveBase64(data);	
				if(base64Result!=null) {
					if(data.getId()!=null && !data.getId().equals("-1") && !data.getId().equals("")){
						result = dao.update(data, conn);
					}else{
						result = dao.insert(data, conn);
						base64Result.setId(result.data.getId());
					}
					
					result.data = base64Result;
					result.success();
				}else{
					result = new ViewResult<>();
					result.error("I/O_error");
				}
			} else {
				result = new ViewResult<>();
				result.error("invalid_data");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = new ViewResult<>(e);
		}
		return result;
	}


	public ViewResult<ImageData> deleteById(ImageData data, RequestCredential crd, Connection conn) {
		ViewResult<ImageData> rs = new ViewResult<>();
		try {
			ViewResult<ImageData> rtn = dao.getAll(data, conn);
			for (ImageData img : rtn.list) {
				File f = new File(img.getPath());
				if (f.delete()) {
					if (dao.deleteById(img.getId(), conn).status != ComEnum.ErrorStatus.Success.getCode()) {
						throw new Exception("002");
					}
				}else{
					throw new Exception("001");
				}
			}
			rs.status = ComEnum.ErrorStatus.Success.getCode();
		} catch (Exception e) {
			rs.status = ComEnum.ErrorStatus.ServerError.getCode();
			rs.message = e.getMessage();
		}
		return rs;

	}

	public ViewResult<ImageData> updateById(ImageData data, RequestCredential crd, Connection conn) {
		ViewResult<ImageData> rtn;
		try {
			rtn = dao.update(data, conn);
		} catch (Exception e) {
			rtn = new ViewResult<>(ComEnum.ErrorStatus.ClientError.getCode(), e.getMessage());
			e.printStackTrace();
		}
		return rtn;
	}

	public ViewResult<ImageData> getAll(ImageData data, RequestCredential crd, Connection conn) {
		ViewResult<ImageData> rtn;
		try {
			rtn = dao.getAll(data, conn);
		} catch (Exception e) {
			rtn = new ViewResult<>(ComEnum.ErrorStatus.ClientError.getCode(), e.getMessage());
			e.printStackTrace();
		}
		return rtn;
	}



	public ViewResult<ImageData> prepareToSave(MultipartFile[] media, String pid, RequestCredential cred,
			Connection conn) {
		ViewResult<ImageData> rtn = null;
		Date d = new Date();
		try {
			String folder = env.getProperty("image-path");
			List<ImageData> images = new ArrayList<>();
			for (MultipartFile f : media) {
				FileProperties pro = getProperties(f.getOriginalFilename());
				String fileName = generateImageName(pid) + "." + (pro.getFiletype().split("/")[1]);
				String filePath = folder + fileName;
				if (writeFile(f, filePath)) {
					ImageData img = new ImageData();
					img.setId(pro.getId());
					//img.setForeignKey(pid);
					img.setPath(filePath);
					img.setDefaults(pro.getIsDefault() == 1 ? true : false);
					img.setCdate(d);
					img.setMdate(d);
					img.setComment(pro.getComment().equals("NA") ? "" : pro.getComment());
					img.setCode(pro.getFiletype());
					img.setName(fileName);
					images.add(img);

				}
			}
			prepareImageFileForStock(pid, images, cred, conn);
			
			rtn = new ViewResult<>();
			rtn.status = ComEnum.ErrorStatus.Success.getCode();

		} catch (Exception e) {
			rtn = new ViewResult<>();
			rtn.status = ComEnum.ErrorStatus.ClientError.getCode();
			rtn.message = e.getMessage();
			e.printStackTrace();
		}
		return rtn;
	}
	public ImageData saveBase64(ImageData image){
		try{
			if(image == null || image.getBase64() == null || image.getBase64().equals("")) return null;
			Pattern base64Regx = Pattern.compile("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$");
			String folder = env.getProperty("image-path");
			String delims="[,]";
		    String[] parts = image.getBase64().split(delims);
		    String imageString = parts[1];
		    if(!base64Regx.matcher(imageString).matches()){
		    	return null;
		    }
		    byte[] imageByteArray = Base64.getDecoder().decode(imageString);
		    InputStream is = new ByteArrayInputStream(imageByteArray);
		    String fileExtension = null;
		    try {
		    	String mimeType = URLConnection.guessContentTypeFromStream(is); //mimeType is something like "image/jpeg"
		        String delimiter="[/]";
		        String[] tokens = mimeType.split(delimiter);
		        fileExtension = tokens[1];
		    } catch (Exception ioException){
		    	fileExtension = parts[0].split("[;]")[0].split("[:]")[1].split("[/]")[1];
		    }
			
			String fileName = KeyFactory.getStringId();
			String fileFullName = fileName.concat(".").concat(fileExtension);
			String filePath = folder.concat(fileFullName);
			FileOutputStream fos = new FileOutputStream(filePath);
			fos.write(imageByteArray);
			fos.close();
			
			image.setPath(filePath);
			image.setName(fileFullName);
			image.setCode(fileExtension);
			
			ImageData result = new ImageData();
			result.setId(image.getId());
			result.setPath(filePath);
			result.setName(fileFullName);
			result.setCode(fileExtension);
			return result;
		}catch(Exception e){
			image = null;
			e.printStackTrace();
		}
		return null;
	}

	private Boolean writeFile(MultipartFile media, String path) {
		Boolean isCreated = false;
		try {
			FileOutputStream fos = new FileOutputStream(path);
			fos.write(media.getBytes());
			fos.close();
			isCreated = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isCreated;
	}

	private String generateImageName(String stockId) {
		return stockId + "_" + KeyFactory.generateRandom(5);

	}

	private FileProperties getProperties(String rawFileName) {
		FileProperties pro = new FileProperties();
		String[] pa = rawFileName.split(":");
		if (pa.length == 5) {
			pro.setId(pa[0]);
			pro.setFiletype(pa[1]);
			pro.setIsDefault(Integer.parseInt(pa[2]));
			pro.setColor(pa[3]);
			pro.setComment(pa[4]);
			return pro;
		} else
			return null;
	}

	public void prepareImageFileForStock(String stockId, List<ImageData> newImages, RequestCredential cred,
			Connection conn) {

		List<ImageData> oldList = dao.getAll(new ImageData("",stockId, "", false, ""), conn).list;
		
		try{
			oldList.forEach( path->{
				File f = new File(path.getPath());
				f.delete();
			});
		}catch(Exception e){
			e.printStackTrace();
		}
		if (newImages.size() > oldList.size()) {
			for (int i = 0; i < newImages.size(); i++) {
				if (i > (oldList.size() - 1)) {
					save(newImages.get(i), cred, conn);
				} else {
					oldList.get(i).setPath(newImages.get(i).getPath());
					oldList.get(i).setDefaults(newImages.get(i).isDefaults());
					oldList.get(i).setCdate(newImages.get(i).getCdate());
					oldList.get(i).setMdate(newImages.get(i).getMdate());
					oldList.get(i).setComment(newImages.get(i).getComment());
					oldList.get(i).setCode(newImages.get(i).getCode());
					oldList.get(i).setName(newImages.get(i).getName());
					updateById(oldList.get(i), cred, conn);
				}
			}

		} else {
			for (int i = 0; i < oldList.size(); i++) {
				if (i > (newImages.size() - 1)) {
					dao.deleteById(oldList.get(i).getId(), conn);
				} else {
					oldList.get(i).setPath(newImages.get(i).getPath());
					oldList.get(i).setDefaults(newImages.get(i).isDefaults());
					oldList.get(i).setCdate(newImages.get(i).getCdate());
					oldList.get(i).setMdate(newImages.get(i).getMdate());
					oldList.get(i).setComment(newImages.get(i).getComment());
					oldList.get(i).setCode(newImages.get(i).getCode());
					oldList.get(i).setName(newImages.get(i).getName());
					updateById(oldList.get(i), cred, conn);
				}
			}
		}

	}
}
