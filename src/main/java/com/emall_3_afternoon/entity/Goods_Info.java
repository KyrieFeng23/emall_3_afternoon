package com.emall_3_afternoon.entity;

import com.emall_3_afternoon.mapper.Goods_Photo_Path_InfoMapper;

import java.util.*;

public class Goods_Info {
    private int goods_id;
    private String goods_name;
    private String goods_describe;
    private float goods_price;
    private float goods_actual_price;
    private int goods_stock;
    private String goods_begin_time;
    private String goods_end_time;
    private int goods_status;
    private int store_id;
    private String dictionary_code;
    //分别用来存储图片和属性的多条记录的list
    private List<Goods_Property_Info> goods_property_infoList = new ArrayList<>();
    private List<Goods_Photo_Path_Info> goods_photo_path_infoList = new ArrayList<>();

    public List<Goods_Photo_Path_Info> getGoods_photo_path_infoList() {
        return goods_photo_path_infoList;
    }

    public void setGoods_photo_path_infoList(List<Goods_Photo_Path_Info> goods_photo_path_infoList) {
        this.goods_photo_path_infoList = goods_photo_path_infoList;
    }

    public List<Goods_Property_Info> getGoods_property_infoList() {
        return goods_property_infoList;
    }

    public void setGoods_property_infoList(List<Goods_Property_Info> goods_property_infoList) {
        this.goods_property_infoList = goods_property_infoList;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_describe() {
        return goods_describe;
    }

    public void setGoods_describe(String goods_describe) {
        this.goods_describe = goods_describe;
    }

    public float getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(float goods_price) {
        this.goods_price = goods_price;
    }

    public float getGoods_actual_price() {
        return goods_actual_price;
    }

    public void setGoods_actual_price(float goods_actual_price) {
        this.goods_actual_price = goods_actual_price;
    }

    public int getGoods_stock() {
        return goods_stock;
    }

    public void setGoods_stock(int goods_stock) {
        this.goods_stock = goods_stock;
    }

    public String getGoods_begin_time() {
        return goods_begin_time;
    }

    public void setGoods_begin_time(String goods_begin_time) {
        this.goods_begin_time = goods_begin_time;
    }

    public String getGoods_end_time() {
        return goods_end_time;
    }

    public void setGoods_end_time(String goods_end_time) {
        this.goods_end_time = goods_end_time;
    }

    public int getGoods_status() {
        return goods_status;
    }

    public void setGoods_status(int goods_status) {
        this.goods_status = goods_status;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public String getDictionary_code() {
        return dictionary_code;
    }

    public void setDictionary_code(String dictionary_code) {
        this.dictionary_code = dictionary_code;
    }
}
