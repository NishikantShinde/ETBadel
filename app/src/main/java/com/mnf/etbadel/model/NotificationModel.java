package com.mnf.etbadel.model;

public class NotificationModel {

    int Id;
    int User_Id;
    String UserName;
    int Sender_Id;
    String SenderName;
    int Item_Id;
    String ItemName;
    int Type_Id;
    String Type;
    boolean Is_active;
    String C_date;
    String U_date;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(int user_Id) {
        User_Id = user_Id;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public int getSender_Id() {
        return Sender_Id;
    }

    public void setSender_Id(int sender_Id) {
        Sender_Id = sender_Id;
    }

    public String getSenderName() {
        return SenderName;
    }

    public void setSenderName(String senderName) {
        SenderName = senderName;
    }

    public int getItem_Id() {
        return Item_Id;
    }

    public void setItem_Id(int item_Id) {
        Item_Id = item_Id;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public int getType_Id() {
        return Type_Id;
    }

    public void setType_Id(int type_Id) {
        Type_Id = type_Id;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
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
}
