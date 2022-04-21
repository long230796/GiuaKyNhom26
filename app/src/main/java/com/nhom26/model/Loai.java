package com.nhom26.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by apoll on 4/14/2022.
 */

public class Loai implements Serializable {
    private String maLoai;
    private String tenLoai;

    public String getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(String maLoai) {
        this.maLoai = maLoai;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public Loai() {
    }

    public Loai(String maLoai, String tenLoai) {
        this.maLoai = maLoai;
        this.tenLoai = tenLoai;
    }

    @Override
    public String toString() {
        return this.tenLoai;

    }
}
