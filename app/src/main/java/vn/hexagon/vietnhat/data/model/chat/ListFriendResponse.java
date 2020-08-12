package vn.hexagon.vietnhat.data.model.chat;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListFriendResponse{

	@SerializedName("data")
	@Expose
	private List<FriendItem> data;

	@SerializedName("errorId")
	@Expose
	private int errorId;

	@SerializedName("message")
	@Expose
	private String message;

	public void setData(List<FriendItem> data){
		this.data = data;
	}

	public List<FriendItem> getData(){
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