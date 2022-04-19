package com.nhom26.model;

/**
 * Created by long2 on 4/14/2022.
 */

public class Chitietsudung {
    String maphong;
    String matb;
    String ngaysudung;
    String soluong;

    public Chitietsudung() {
    }

    public Chitietsudung(String maphong, String matb, String ngaysudung, String soluong) {

        this.maphong = maphong;
        this.matb = matb;
        this.ngaysudung = ngaysudung;
        this.soluong = soluong;
    }

    public String getMaphong() {
        return maphong;
    }

    public void setMaphong(String maphong) {
        this.maphong = maphong;
    }

    public String getMatb() {
        return matb;
    }

    public void setMatb(String matb) {
        this.matb = matb;
    }

    public String getNgaysudung() {
        return ngaysudung;
    }

    public void setNgaysudung(String ngaysudung) {
        this.ngaysudung = ngaysudung;
    }

    public String getSoluong() {
        return soluong;
    }

    public void setSoluong(String soluong) {
        this.soluong = soluong;
    }

    @Override
    public String toString() {
        return this.getMatb();
    }
}
