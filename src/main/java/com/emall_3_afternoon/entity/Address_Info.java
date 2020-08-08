package com.emall_3_afternoon.entity;

import java.sql.Struct;

public class Address_Info {
    private int address_id;
    private int b_s_id;
    private String address;
    private String address_detail;
    private String addresssee;
    private String telephone;
    private int default_status;
    private String postcode;

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
    }

    public int getB_s_id() {
        return b_s_id;
    }

    public void setB_s_id(int b_s_id) {
        this.b_s_id = b_s_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress_detail() {
        return address_detail;
    }

    public void setAddress_detail(String address_detail) {
        this.address_detail = address_detail;
    }

    public String getAddresssee() {
        return addresssee;
    }

    public void setAddresssee(String addresssee) {
        this.addresssee = addresssee;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getDefault_status() {
        return default_status;
    }

    public void setDefault_status(int default_status) {
        this.default_status = default_status;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
}
