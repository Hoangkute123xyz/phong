package vn.hexagon.vietnhat.data.model.chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateGroupData {

	@SerializedName("group_id")
	@Expose
	private int groupId;

	public void setGroupId(int groupId){
		this.groupId = groupId;
	}

	public int getGroupId(){
		return groupId;
	}
}