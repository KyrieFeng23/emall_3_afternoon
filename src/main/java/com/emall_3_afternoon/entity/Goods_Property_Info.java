package com.emall_3_afternoon.entity;

import java.util.List;

public class Goods_Property_Info {
    private int goods_property_id;
    private int  goods_id;
    private String property_name;
    private String property_value;

    public int getGoods_property_id() {
        return goods_property_id;
    }

    public void setGoods_property_id(int goods_property_id) {
        this.goods_property_id = goods_property_id;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public String getProperty_name() {
        return property_name;
    }

    public void setProperty_name(String property_name) {
        this.property_name = property_name;
    }

    public String getProperty_value() {
        return property_value;
    }

    public void setProperty_value(String property_value) {
        this.property_value = property_value;
    }
}
