package vn.hexagon.vietnhat.data.model.chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FriendItem {

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("active")
    @Expose
    private String active;

    @SerializedName("ggid")
    @Expose
    private String ggid;

    @SerializedName("apple_id")
    @Expose
    private String appleId;

    @SerializedName("avatar")
    @Expose
    private String avatar;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("fbid")
    @Expose
    private String fbid;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("token_fcm")
    @Expose
    private String tokenFcm;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("account")
    @Expose
    private String account;

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getActive() {
        return active;
    }

    public void setGgid(String ggid) {
        this.ggid = ggid;
    }

    public String getGgid() {
        return ggid;
    }

    public void setAppleId(String appleId) {
        this.appleId = appleId;
    }

    public String getAppleId() {
        return appleId;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setFbid(String fbid) {
        this.fbid = fbid;
    }

    public String getFbid() {
        return fbid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setTokenFcm(String tokenFcm) {
        this.tokenFcm = tokenFcm;
    }

    public String getTokenFcm() {
        return tokenFcm;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccount() {
        return account;
    }
}