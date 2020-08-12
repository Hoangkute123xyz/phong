package vn.hexagon.vietnhat.data.model.comment;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ListFavoritePhoneResponse {

	@SerializedName("data")
	private List<ItemFavoritePhone> data;

	@SerializedName("errorId")
	private int errorId;

	@SerializedName("message")
	private String message;

	public void setData(List<ItemFavoritePhone> data){
		this.data = data;
	}

	public List<ItemFavoritePhone> getData(){
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