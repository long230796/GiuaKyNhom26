package com.nhom26.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nhom26.giuakynhom26.R;
import com.nhom26.model.Thietbi;

/**
 * Created by long2 on 4/13/2022.
 */

public class SelectedThietBiAdapter extends ArrayAdapter<Thietbi> {
    Activity context;
    int resource;

    public SelectedThietBiAdapter(Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    @android.support.annotation.NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View customView = inflater.inflate(this.resource, null);

        TextView txtTen = customView.findViewById(R.id.txtTenThietbiSelected);
        TextView txtSoluong = customView.findViewById(R.id.txtSoluongSelected);
        ImageView imgDel = customView.findViewById(R.id.imgDeleteSelected);



        Thietbi tb = getItem(position);
        imgDel.setImageResource(R.drawable.ic_remove_circle_black_24dp);
        txtTen.setText(tb.getTentb());
        txtSoluong.setText(tb.getSoluong());
        return customView;
    }

}