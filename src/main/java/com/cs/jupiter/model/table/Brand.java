package com.cs.jupiter.model.table;

import java.util.ArrayList;
import java.util.List;

import com.cs.jupiter.model.interfaces.ViewCredential;


public class Brand extends ViewCredential{
	private List<User> accUsers = new ArrayList<>();
	private User accUser;
	private ImageData profileImage;
	private ImageData coverImage;
	public Brand(){
	
	}
	public Brand(String id){
		this.setId(id);
	}
	public List<User> getAccUsers() {
		return accUsers;
	}

	public void setAccUsers(List<User> accUsers) {
		this.accUsers = accUsers;
	}
	public User getUser(int index){
		if(this.accUsers != null && this.accUsers.size() !=0){
			return this.accUsers.get(index);
		}else{
			return null;
		}
	}
	public User getAccUser() {
		return accUser;
	}
	public void setAccUser(User accUser) {
		this.accUser = accUser;
	}
	public ImageData getProfileImage() {
		return profileImage;
	}
	public void setProfileImage(ImageData profileImage) {
		this.profileImage = profileImage;
	}
	public ImageData getCoverImage() {
		return coverImage;
	}
	public void setCoverImage(ImageData coverImage) {
		this.coverImage = coverImage;
	}
	
}
