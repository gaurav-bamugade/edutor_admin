package com.example.miniproj2admin.models;

public class approvModel {
    String userName,userEmail,redeemId,uid,approval,prod_id,product_image_url,product_name,product_price,pleasewait,waitIcon;

    public approvModel(String redeemId,String userEmail,String userName,String uid,String approval, String prod_id, String product_image_url, String product_name, String product_price,String pleasewait,String waitIcon) {
        this.approval = approval;
        this.prod_id = prod_id;
        this.product_image_url = product_image_url;
        this.product_name = product_name;
        this.product_price = product_price;
        this.pleasewait=pleasewait;
        this.waitIcon=waitIcon;
        this.redeemId=redeemId;
        this.uid=uid;
        this.userEmail=userEmail;
        this.userName=userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getRedeemId() {
        return redeemId;
    }

    public void setRedeemId(String redeemId) {
        this.redeemId = redeemId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public approvModel() {
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }

    public String getProd_id() {
        return prod_id;
    }

    public void setProd_id(String prod_id) {
        this.prod_id = prod_id;
    }

    public String getProduct_image_url() {
        return product_image_url;
    }

    public void setProduct_image_url(String product_image_url) {
        this.product_image_url = product_image_url;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getPleasewait() {
        return pleasewait;
    }

    public void setPleasewait(String pleasewait) {
        this.pleasewait = pleasewait;
    }

    public String getWaitIcon() {
        return waitIcon;
    }

    public void setWaitIcon(String waitIcon) {
        this.waitIcon = waitIcon;
    }
}
