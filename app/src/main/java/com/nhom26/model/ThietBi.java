package com.nhom26.model;

import java.io.Serializable;

/**
 * Created by apoll on 4/14/2022.
 */

public class thietBi implements Serializable {
    private String maTB;
    private String tenTB;
    private String xuatXu;
    private String maLoai;

    public String getMaTB() {
        return maTB;
    }

    public void setMaTB(String maTB) {
        this.maTB = maTB;
    }

    public String getTenTB() {
        return tenTB;
    }

    public void setTenTB(String tenTB) {
        this.tenTB = tenTB;
    }

    public String getXuatXu() {
        return xuatXu;
    }

    public void setXuatXu(String xuatXu) {
        this.xuatXu = xuatXu;
    }

    public String getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(String maLoai) {
        this.maLoai = maLoai;
    }

    public thietBi() {
    }

    public thietBi(String maTB, String xuatXu, String tenTB, String maLoai) {
        this.maTB = maTB;
        this.xuatXu = xuatXu;
        this.tenTB = tenTB;
        this.maLoai = maLoai;
    }
}
