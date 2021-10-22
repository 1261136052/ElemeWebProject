package org.example.eleme.model;



public class orderone {
    private int id;
    private int orderid;
    private int shopid;

    private int shopnum;
    private Float price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public int getShopid() {
        return shopid;
    }

    public void setShopid(int shopid) {
        this.shopid = shopid;
    }

    public int getShopnum() {
        return shopnum;
    }

    public void setShopnum(int shopnum) {
        this.shopnum = shopnum;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
