//package com.nhom26.adapter;
//
//import android.annotation.NonNull;
//import android.app.Activity;
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.nhom26.giuakynhom26.R;
//import com.nhom26.model.Loai;
//
///**
// * Created by apoll on 4/18/2022.
// */
//
//public class SelectedLoaiTBAdapter extends ArrayAdapter<Loai> {
//
//    Activity context;
//    int resource;
//
//    Loai loai;
//
//    public SelectedLoaiTBAdapter(Activity context, int resource) {
//        super(context, resource);
//        this.context = context;
//        this.resource = resource;
//    }
//    @android.support.annotation.NonNull
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        LayoutInflater inflater = this.context.getLayoutInflater();
//        View customView = inflater.inflate(this.resource, null);
//
//        TextView txtTen = customView.findViewById(R.id.txtTenThietbiSelected);
//        TextView txtSoluong = customView.findViewById(R.id.txtSoluongSelected);
//        ImageView imgDel = customView.findViewById(R.id.imgDeleteSelected);
//
//        loai = getItem(position);
//        imgDel.setImageResource(R.drawable.ic_remove_circle_black_24dp);
//        txtTen.setText(loai);
//        txtSoluong.setText(loai.getSoluong());
//
//        return customView;
//    }
//
//}
