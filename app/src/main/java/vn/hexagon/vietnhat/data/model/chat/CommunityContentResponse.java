package vn.hexagon.vietnhat.data.model.chat;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class CommunityContentResponse{

	@SerializedName("data")
	private List<CommunityContent> data;

	@SerializedName("errorId")
	private int errorId;

	@SerializedName("message")
	private String message;

	public void setData(List<CommunityContent> data){
		this.data = data;
	}

	public List<CommunityContent> getData(){
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