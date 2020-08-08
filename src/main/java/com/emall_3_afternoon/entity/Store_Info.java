package com.emall_3_afternoon.entity;

public class Store_Info {
    private int store_id;
    private String store_name;
    private String store_describe;
    private int b_s_id;
    private int link_man_id;
    private int credit;
    private float describe_mark;
    private float service_mark;
    private float logistics_mark;

    public float getDescribe_mark() {
        return describe_mark;
    }

    public void setDescribe_mark(float describe_mark) {
        this.describe_mark = describe_mark;
    }

    public float getService_mark() {
        return service_mark;
    }

    public void setService_mark(float service_mark) {
        this.service_mark = service_mark;
    }

    public float getLogistics_mark() {
        return logistics_mark;
    }

    public void setLogistics_mark(float logistics_mark) {
        this.logistics_mark = logistics_mark;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_describe() {
        return store_describe;
    }

    public void setStore_describe(String store_describe) {
        this.store_describe = store_describe;
    }

    public int getB_s_id() {
        return b_s_id;
    }

    public void setB_s_id(int b_s_id) {
        this.b_s_id = b_s_id;
    }

    public int getLink_man_id() {
        return link_man_id;
    }

    public void setLink_man_id(int link_man_id) {
        this.link_man_id = link_man_id;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }
}
