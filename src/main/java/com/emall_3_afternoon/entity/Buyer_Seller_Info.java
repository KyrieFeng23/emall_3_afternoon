package com.emall_3_afternoon.entity;

public class Buyer_Seller_Info {
    private int b_s_id;
    private String b_s_name;
    private String nickname;
    private int sex;
    private String icon;
    private String telephone;
    private String pwd;
    private String home;
    private String home_detail;
    private int status;
    private int b_s_status;
    private String email;
    private String last_login_time;
    private String register_time;

    public String getLast_login_time() {
        return last_login_time;
    }

    public void setLast_login_time(String last_login_time) {
        this.last_login_time = last_login_time;
    }

    public String getRegister_time() {
        return register_time;
    }

    public void setRegister_time(String register_time) {
        this.register_time = register_time;
    }

    public int getB_s_id() {
        return b_s_id;
    }

    public void setB_s_id(int b_s_id) {
        this.b_s_id = b_s_id;
    }

    public String getB_s_name() {
        return b_s_name;
    }

    public void setB_s_name(String b_s_name) {
        this.b_s_name = b_s_name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getHome_detail() {
        return home_detail;
    }

    public void setHome_detail(String home_detail) {
        this.home_detail = home_detail;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getB_s_status() {
        return b_s_status;
    }

    public void setB_s_status(int b_s_status) {
        this.b_s_status = b_s_status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
