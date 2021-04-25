package com.xuancanh.studentmanager.model;

import java.io.Serializable;

public class Student implements Serializable {
    private int stu_id;
    private String stu_name;
    private String stu_no;
    private String stu_email;
    private int stu_gender;
    private String stu_dob;
    private String stu_class;
    private byte[] stu_avt;
    private String stu_phone;

    public Student() {}

    public Student(int stu_id, String stu_name, String stu_no, String stu_email, int stu_gender,
                   String stu_dob, String stu_class, byte[] stu_avt, String stu_phone) {
        this.stu_id = stu_id;
        this.stu_name = stu_name;
        this.stu_no = stu_no;
        this.stu_email = stu_email;
        this.stu_gender = stu_gender;
        this.stu_dob = stu_dob;
        this.stu_class = stu_class;
        this.stu_avt = stu_avt;
        this.stu_phone = stu_phone;
    }

    public int getStu_id() {
        return stu_id;
    }

    public void setStu_id(int stu_id) {
        this.stu_id = stu_id;
    }

    public String getStu_name() {
        return stu_name;
    }

    public void setStu_name(String stu_name) {
        this.stu_name = stu_name;
    }

    public String getStu_no() {
        return stu_no;
    }

    public void setStu_no(String stu_no) {
        this.stu_no = stu_no;
    }

    public String getStu_email() {
        return stu_email;
    }

    public void setStu_email(String stu_email) {
        this.stu_email = stu_email;
    }

    public int getStu_gender() {
        return stu_gender;
    }

    public void setStu_gender(int stu_gender) {
        this.stu_gender = stu_gender;
    }

    public String getStu_dob() {
        return stu_dob;
    }

    public void setStu_dob(String stu_dob) {
        this.stu_dob = stu_dob;
    }

    public String getStu_class() {
        return stu_class;
    }

    public void setStu_class(String stu_class) {
        this.stu_class = stu_class;
    }

    public byte[] getStu_avt() {
        return stu_avt;
    }

    public void setStu_avt(byte[] stu_avt) {
        this.stu_avt = stu_avt;
    }

    public String getStu_phone() {
        return stu_phone;
    }

    public void setStu_phone(String stu_phone) {
        this.stu_phone = stu_phone;
    }
}
