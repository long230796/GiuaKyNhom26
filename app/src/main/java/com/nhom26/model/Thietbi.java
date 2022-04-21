package com.nhom26.model;

import java.io.Serializable;

/**
 * Created by long2 on 4/9/2022.
 */

public class Thietbi implements Serializable {
    private String matb;
    private String tentb;
    private String xuatxu;
    private String maLoai;
    private String soluong;

    public String getMatb() {
        return matb;
    }

    public void setMatb(String matb) {
        this.matb = matb;
    }

    public String getTentb() {
        return tentb;
    }

    public void setTentb(String tentb) {
        this.tentb = tentb;
    }

    public String getXuatxu() {
        return xuatxu;
    }

    public void setXuatxu(String xuatxu) {
        this.xuatxu = xuatxu;
    }

    public String getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(String maLoai) {
        this.maLoai = maLoai;
    }

    public String getSoluong() {
        return soluong;
    }

    public void setSoluong(String soluong) {
        this.soluong = soluong;
    }

    public Thietbi() {
    }

    public Thietbi(String matb, String tentb, String xuatxu, String maLoai) {
        this.matb = matb;
        this.tentb = tentb;
        this.xuatxu = xuatxu;
        this.maLoai = maLoai;
    }

    public Thietbi(String matb, String tentb, String xuatxu, String maLoai, String soluong) {
        this.matb = matb;
        this.tentb = tentb;
        this.xuatxu = xuatxu;
        this.maLoai = maLoai;
        this.soluong = soluong;
    }

    public Thietbi(String matb, String tentb, String xuatxu) {
        this.matb = matb;
        this.tentb = tentb;
        this.xuatxu = xuatxu;
    }

    @Override
    public String toString() {
        return this.tentb;
    }
}
