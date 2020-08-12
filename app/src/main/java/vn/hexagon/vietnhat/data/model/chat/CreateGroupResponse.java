package vn.hexagon.vietnhat.data.model.chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateGroupResponse{

	@SerializedName("data")
	@Expose
	private CreateGroupData data;

	@SerializedName("errorId")
	@Expose
	private int errorId;

	@SerializedName("message")
	@Expose
	private String message;

	public void setData(CreateGroupData data){
		this.data = data;
	}

	public CreateGroupData getData(){
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