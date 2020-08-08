package com.emall_3_afternoon.entity;

public class Logistics_Item_Info {
    private int logistics_item_id;
    private int logistics_id;
    private String logistics_item_time;
    private String logistics_item_remark;

    public int getLogistics_item_id() {
        return logistics_item_id;
    }

    public void setLogistics_item_id(int logistics_item_id) {
        this.logistics_item_id = logistics_item_id;
    }

    public int getLogistics_id() {
        return logistics_id;
    }

    public void setLogistics_id(int logistics_id) {
        this.logistics_id = logistics_id;
    }

    public String getLogistics_item_time() {
        return logistics_item_time;
    }

    public void setLogistics_item_time(String logistics_item_time) {
        this.logistics_item_time = logistics_item_time;
    }

    public String getLogistics_item_remark() {
        return logistics_item_remark;
    }

    public void setLogistics_item_remark(String logistics_item_remark) {
        this.logistics_item_remark = logistics_item_remark;
    }
}

