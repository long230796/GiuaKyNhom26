package com.nhom26.model;

import java.io.Serializable;

/**
 * Created by apoll on 4/14/2022.
 */

public class chiTietSuDung implements Serializable {
    private String maPhong;
    private String maTB;
    private String ngaySD;
    private int soLuong;

    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getNgaySD() {
        return ngaySD;
    }

    public void setNgaySD(String ngaySD) {
        this.ngaySD = ngaySD;
    }

    public String getMaTB() {
        return maTB;
    }

    public void setMaTB(String maTB) {
        this.maTB = maTB;
    }

    public chiTietSuDung() {
    }

    public chiTietSuDung(String maPhong, String maTB, String ngaySD, int soLuong) {
        this.maPhong = maPhong;
        this.maTB = maTB;
        this.ngaySD = ngaySD;
        this.soLuong = soLuong;
    }
}
