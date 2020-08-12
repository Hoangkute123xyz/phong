package vn.hexagon.vietnhat.data.model.chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommunityContent {

	@SerializedName("date")
	@Expose
	private String date;

	@SerializedName("user_avatar")
	@Expose
	private String userAvatar;

	@SerializedName("user_id")
	@Expose
	private String userId;

	@SerializedName("user_name")
	@Expose
	private String userName;

	@SerializedName("active")
	@Expose
	private String active;

	@SerializedName("id")
	@Expose
	private String id;

	@SerializedName("type")
	@Expose
	private String type;

	@SerializedName("content")
	@Expose
	private String content;

	public void setDate(String date){
		this.date = date;
	}

	public String getDate(){
		return date;
	}

	public void setUserAvatar(String userAvatar){
		this.userAvatar = userAvatar;
	}

	public String getUserAvatar(){
		return userAvatar;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setUserName(String userName){
		this.userName = userName;
	}

	public String getUserName(){
		return userName;
	}

	public void setActive(String active){
		this.active = active;
	}

	public String getActive(){
		return active;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setContent(String content){
		this.content = content;
	}

	public String getContent(){
		return content;
	}

	public CommunityContent(String date, String userAvatar, String userId, String userName, String active, String id, String type, String content) {
		this.date = date;
		this.userAvatar = userAvatar;
		this.userId = userId;
		this.userName = userName;
		this.active = active;
		this.id = id;
		this.type = type;
		this.content = content;
	}
}