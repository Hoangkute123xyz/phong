package vn.hexagon.vietnhat.data.model.chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GroupChat {

	@SerializedName("date")
	@Expose
	private String date;

	@SerializedName("list_user_id")
	@Expose
	private ArrayList<String> listUserId;

	@SerializedName("user_id_create")
	@Expose
	private String userIdCreate;

	@SerializedName("name")
	@Expose
	private String name;

	@SerializedName("active")
	@Expose
	private String active;

	@SerializedName("last_message")
	@Expose
	private String lastMessage;

	@SerializedName("id")
	@Expose
	private String id;

	@SerializedName("avatar")
	@Expose
	private String avatar;

	public void setDate(String date){
		this.date = date;
	}

	public String getDate(){
		return date;
	}

	public ArrayList<String> getListUserId() {
		return listUserId;
	}

	public void setListUserId(ArrayList<String> listUserId) {
		this.listUserId = listUserId;
	}

	public void setUserIdCreate(String userIdCreate){
		this.userIdCreate = userIdCreate;
	}

	public String getUserIdCreate(){
		return userIdCreate;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setActive(String active){
		this.active = active;
	}

	public String getActive(){
		return active;
	}

	public void setLastMessage(String lastMessage){
		this.lastMessage = lastMessage;
	}

	public String getLastMessage(){
		return lastMessage;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setAvatar(String avatar){
		this.avatar = avatar;
	}

	public String getAvatar(){
		return avatar;
	}
}