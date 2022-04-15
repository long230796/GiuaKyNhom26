package com.nhom26.model;

import java.io.Serializable;

/**
 * Created by long2 on 4/9/2022.
 */

public class Thietbi implements Serializable {
    private String matb;
    private String tentb;
    private String xuatxu;
    private Loai loai;
    private String soluong;



//    public Thietbi(String matb, String tentb, String xuatxu, String maloai, String soluong) {
//
//        this.matb = matb;
//        this.tentb = tentb;
//        this.xuatxu = xuatxu;
//        this.maloai = maloai;
//        this.soluong = soluong;
//    }

    public Thietbi(String matb, String tentb, String xuatxu) {

        this.matb = matb;
        this.tentb = tentb;
        this.xuatxu = xuatxu;
    }

    public Thietbi() {
    }

    public String getMatb() {
        return matb;
    }

    public void setMatb(String matb) {
        this.matb = matb;
    }

    public String getSoluong() {
        return soluong;
    }

    public void setSoluong(String soluong) {
        this.soluong = soluong;
    }

    public Loai getLoai() {
        return loai;
    }

    public void setLoai(Loai loai) {
        this.loai = loai;
    }

    public String getXuatxu() {
        return xuatxu;
    }

    public void setXuatxu(String xuatxu) {
        this.xuatxu = xuatxu;
    }

    public String getTentb() {
        return tentb;
    }

    public void setTentb(String tentb) {
        this.tentb = tentb;
    }

    @Override
    public String toString() {
        return this.tentb;
    }
}
