package com.mnf.etbadel.model;

public class UserModel {
    private int Id;
    private String F_Name;
    private String L_Name;
    private String Email;
    private String Profile_Image;
    private String Profile_Image_url;
    private String Password;
    private	boolean Is_FB_Login;
    private String FB_Token;
    private	boolean Is_Gmail_Login;
    private String Gmail_Token;
    private	boolean Is_online;
    private	boolean Is_active;
    private String C_date;
    private String U_date;
    private String Device_Id;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getF_Name() {
        return F_Name;
    }

    public void setF_Name(String f_Name) {
        F_Name = f_Name;
    }

    public String getL_Name() {
        return L_Name;
    }

    public void setL_Name(String l_Name) {
        L_Name = l_Name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getProfile_Image() {
        return Profile_Image;
    }

    public void setProfile_Image(String profile_Image) {
        Profile_Image = profile_Image;
    }

    public String getProfile_Image_url() {
        return Profile_Image_url;
    }

    public void setProfile_Image_url(String profile_Image_url) {
        Profile_Image_url = profile_Image_url;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public boolean isIs_FB_Login() {
        return Is_FB_Login;
    }

    public void setIs_FB_Login(boolean is_FB_Login) {
        Is_FB_Login = is_FB_Login;
    }

    public String getFB_Token() {
        return FB_Token;
    }

    public void setFB_Token(String FB_Token) {
        this.FB_Token = FB_Token;
    }

    public boolean isIs_Gmail_Login() {
        return Is_Gmail_Login;
    }

    public void setIs_Gmail_Login(boolean is_Gmail_Login) {
        Is_Gmail_Login = is_Gmail_Login;
    }

    public String getGmail_Token() {
        return Gmail_Token;
    }

    public void setGmail_Token(String gmail_Token) {
        Gmail_Token = gmail_Token;
    }

    public boolean isIs_online() {
        return Is_online;
    }

    public void setIs_online(boolean is_online) {
        Is_online = is_online;
    }

    public boolean isIs_active() {
        return Is_active;
    }

    public void setIs_active(boolean is_active) {
        Is_active = is_active;
    }

    public String getC_date() {
        return C_date;
    }

    public void setC_date(String c_date) {
        C_date = c_date;
    }

    public String getU_date() {
        return U_date;
    }

    public void setU_date(String u_date) {
        U_date = u_date;
    }

    public String getDevice_Id() {
        return Device_Id;
    }

    public void setDevice_Id(String device_Id) {
        Device_Id = device_Id;
    }
}
