package com.nhom26.model;

import java.io.Serializable;

/**
 * Created by long2 on 3/23/2022.
 */

public class Phong_2 implements Serializable {
    private String ma;
    private String loai;
    private int tang;

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public int getTang() {
        return tang;
    }

    public void setTang(int tang) {
        this.tang = tang;
    }

    public Phong_2() {

    }

    @Override
    public String toString() {
        return this.ma;
    }

    public Phong_2(String ma, String loai, int tang) {

        this.ma = ma;
        this.loai = loai;
        this.tang = tang;
    }
}
