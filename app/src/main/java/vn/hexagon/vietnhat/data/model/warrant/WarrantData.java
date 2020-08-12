package vn.hexagon.vietnhat.data.model.warrant;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WarrantData {

	@SerializedName("image")
	@Expose
	private String image;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@SerializedName("date")
	@Expose
	private String date;

	@SerializedName("note")
	@Expose
	private String note;

	@SerializedName("date_start")
	@Expose
	private String dateStart;

	@SerializedName("code")
	@Expose
	private String code;

	@SerializedName("phone")
	@Expose
	private String phone;

	@SerializedName("price")
	@Expose
	private String price;

	@SerializedName("name")
	@Expose
	private String name;

	@SerializedName("imei")
	@Expose
	private String imei;

	@SerializedName("active")
	@Expose
	private String active;

	@SerializedName("date_end")
	@Expose
	private String dateEnd;

	@SerializedName("id")
	@Expose
	private String id;

	@SerializedName("time")
	@Expose
	private String time;

	public void setDate(String date){
		this.date = date;
	}

	public String getDate(){
		return date;
	}

	public void setNote(String note){
		this.note = note;
	}

	public String getNote(){
		return note;
	}

	public void setDateStart(String dateStart){
		this.dateStart = dateStart;
	}

	public String getDateStart(){
		return dateStart;
	}

	public void setCode(String code){
		this.code = code;
	}

	public String getCode(){
		return code;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setPrice(String price){
		this.price = price;
	}

	public String getPrice(){
		return price;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setImei(String imei){
		this.imei = imei;
	}

	public String getImei(){
		return imei;
	}

	public void setActive(String active){
		this.active = active;
	}

	public String getActive(){
		return active;
	}

	public void setDateEnd(String dateEnd){
		this.dateEnd = dateEnd;
	}

	public String getDateEnd(){
		return dateEnd;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setTime(String time){
		this.time = time;
	}

	public String getTime(){
		return time;
	}
}