package vn.hexagon.vietnhat.data.model.chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommunityChatResponse{

	@SerializedName("data")
	@Expose
	private String data;

	@SerializedName("errorId")
	@Expose
	private int errorId;

	@SerializedName("message")
	@Expose
	private String message;

//	@SerializedName("user_id")
//	@Expose
//	private String userID;
//
//	public String getUserID() {
//		return userID;
//	}
//
//	public void setUserID(String userID) {
//		this.userID = userID;
//	}

	public void setData(String data){
		this.data = data;
	}

	public String getData(){
		return data;
	}

	public void setErrorId(int errorId){
		this.errorId = errorId;
	}

	public int getErrorId(){
		return errorId;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}