package com.nhom26.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nhom26.giuakynhom26.R;
import com.nhom26.model.Chitietsudung;
import com.nhom26.model.Phong;

/**
 * Created by long2 on 4/13/2022.
 */

public class ChitietPhongAdapter extends ArrayAdapter<Chitietsudung> {
    Activity context;
    int resource;


    Phong p;

    public ChitietPhongAdapter(Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    @android.support.annotation.NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View customView = inflater.inflate(this.resource, null);

        TextView txtTenPhong = customView.findViewById(R.id.txtMaTB);
        TextView txtSoluong = customView.findViewById(R.id.txtSoluong);
        TextView txtNgaySuDung = customView.findViewById(R.id.txtNgaySuDung);

        Chitietsudung ctsd = getItem(position);
        txtTenPhong.setText(ctsd.getMaphong());
        txtSoluong.setText(ctsd.getSoluong());
        txtNgaySuDung.setText(ctsd.getNgaysudung());


        return customView;
    }


}