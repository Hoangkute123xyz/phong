package vn.hexagon.vietnhat.data.model.warrant;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WarrantResponse{

	@SerializedName("data")
	@Expose
	private WarrantData data;

	@SerializedName("errorId")
	@Expose
	private int errorId;

	@SerializedName("message")
	@Expose
	private String message;

	public void setData(WarrantData data){
		this.data = data;
	}

	public WarrantData getData(){
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