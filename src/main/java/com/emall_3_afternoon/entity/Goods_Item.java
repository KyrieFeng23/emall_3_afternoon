package com.emall_3_afternoon.entity;
//用于放在店铺_item里面做为子属性的类，对应的是店铺下面的一个商品明细
public class Goods_Item {
    private int account = 0;
    private float goods_money = 0;
    private String goods_photo_path="";
    //这里面有个问题，就是不知道阿里巴巴的反序列化的类库能不能反到4层以上。
    private Goods_Info goods_info;

    public String getGoods_photo_path() {
        return goods_photo_path;
    }

    public void setGoods_photo_path(String goods_photo_path) {
        this.goods_photo_path = goods_photo_path;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public float getGoods_money() {
        return goods_money;
    }

    public void setGoods_money(float goods_money) {
        this.goods_money = goods_money;
    }

    public Goods_Info getGoods_info() {
        return goods_info;
    }

    public void setGoods_info(Goods_Info goods_info) {
        this.goods_info = goods_info;
    }
}
