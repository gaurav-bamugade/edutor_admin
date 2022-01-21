package com.example.miniproj2admin.models;

public class productsModel {

    String productId;
    String productName;
    String prodDesc;
    int stockCount;
    int productPrice;
    String imageUrl;
    String prodCategory;

    public productsModel(String productId, String productName, int stockCount, int productPrice, String imageUrl, String prodCategory, String prodDesc) {
        this.productId=productId;
        this.productName = productName;
        this.stockCount = stockCount;
        this.productPrice = productPrice;
        this.imageUrl = imageUrl;
        this.prodCategory=prodCategory;
        this.prodDesc=prodDesc;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getStockCount() {
        return stockCount;
    }

    public void setStockCount(int stockCount) {
        this.stockCount = stockCount;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getProdDesc() {
        return prodDesc;
    }

    public void setProdDesc(String prodDesc) {
        this.prodDesc = prodDesc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProdCategory() {
        return prodCategory;
    }

    public void setProdCategory(String prodCategory) {
        this.prodCategory = prodCategory;
    }
}
