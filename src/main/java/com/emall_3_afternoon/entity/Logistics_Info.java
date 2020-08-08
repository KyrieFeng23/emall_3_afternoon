package com.emall_3_afternoon.entity;

import java.util.ArrayList;
import java.util.List;

public class Logistics_Info {
    private int logistics_id;
    private int order_id;
    private String link_man;
    private String link_telephone;
    private String goods_address;
    private String address_1;
    private String address_detail;
    private int logistics_status;
    private List<Logistics_Item_Info> logistics_item_infoList = new ArrayList<Logistics_Item_Info>();

    public int getLogistics_id() {
        return logistics_id;
    }

    public void setLogistics_id(int logistics_id) {
        this.logistics_id = logistics_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getLink_man() {
        return link_man;
    }

    public void setLink_man(String link_man) {
        this.link_man = link_man;
    }

    public String getLink_telephone() {
        return link_telephone;
    }

    public void setLink_telephone(String link_telephone) {
        this.link_telephone = link_telephone;
    }

    public String getGoods_address() {
        return goods_address;
    }

    public void setGoods_address(String goods_address) {
        this.goods_address = goods_address;
    }

    public String getAddress_1() {
        return address_1;
    }

    public void setAddress_1(String address_1) {
        this.address_1 = address_1;
    }

    public String getAddress_detail() {
        return address_detail;
    }

    public void setAddress_detail(String address_detail) {
        this.address_detail = address_detail;
    }

    public int getLogistics_status() {
        return logistics_status;
    }

    public void setLogistics_status(int logistics_status) {
        this.logistics_status = logistics_status;
    }

    public List<Logistics_Item_Info> getLogistics_item_infoList() {
        return logistics_item_infoList;
    }

    public void setLogistics_item_infoList(List<Logistics_Item_Info> logistics_item_infoList) {
        this.logistics_item_infoList = logistics_item_infoList;
    }
}
