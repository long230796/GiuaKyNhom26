package com.nhom26.giuakynhom26.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhom26.adapter.ChitietPhongAdapter;
import com.nhom26.giuakynhom26.R;
import com.nhom26.model.Chitietsudung;
import com.nhom26.model.Phong_2;
import com.nhom26.model.Thietbi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ChitietsudungTBActivity extends AppCompatActivity {

    TextView txtMaTB;
    TextView txtTenTB;
    TextView txtXuatXu;
    TextView txtMaLoai;
    ListView lvPhong;
    Thietbi thietBi;
    Phong_2 selectedPhong;
    Chitietsudung ctsd;
    ChitietPhongAdapter ctsdPhongAdapter;
    ArrayAdapter<Phong_2> phongAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitietsudung_tb);
        addControls();
        addEvents();

    }

    private void addEvents() {
        lvPhong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedPhong = phongAdapter.getItem(i);
                hienThiDialogChiTietPhong();
            }
        });
    }

    private void hienThiDialogChiTietPhong() {
        Dialog dialogChiTietPhong = new Dialog(ChitietsudungTBActivity.this);
        dialogChiTietPhong.setContentView(R.layout.dialog_phong_detail_v2);

        TextView txtMaP = dialogChiTietPhong.findViewById(R.id.txtMaPhong);
        TextView txtLoaiP = dialogChiTietPhong.findViewById(R.id.txtLoaiPhong);
        TextView txtTang = dialogChiTietPhong.findViewById(R.id.txtTang);

        txtMaP.setText(selectedPhong.getMa());
        txtLoaiP.setText(selectedPhong.getLoai());
        txtTang.setText(String.valueOf(selectedPhong.getTang()));

        dialogChiTietPhong.show();
    }

    private void addControls() {
        Intent intent = getIntent();
        thietBi = (Thietbi) intent.getSerializableExtra("thietBi");

        txtMaTB = (TextView) findViewById(R.id.txtMaTB);
        txtTenTB = (TextView) findViewById(R.id.txtTenTB);
        txtXuatXu = (TextView) findViewById(R.id.txtXuatXu);
        txtMaLoai = (TextView) findViewById(R.id.txtMaLoai);
        lvPhong = (ListView) findViewById(R.id.lvPhong);

        ctsdPhongAdapter = new ChitietPhongAdapter(ChitietsudungTBActivity.this, R.layout.phong_custom_chitietsudung);
        phongAdapter = new ArrayAdapter<Phong_2>(ChitietsudungTBActivity.this, android.R.layout.simple_list_item_1);
        lvPhong.setAdapter(ctsdPhongAdapter);

        txtMaTB.setText(thietBi.getMatb());
        txtTenTB.setText(thietBi.getTentb());
        txtXuatXu.setText(thietBi.getXuatxu());
        txtMaLoai.setText(thietBi.getMatb());

        getUsedPhongByMaTB();


    }

    private void getUsedPhongByMaTB() {
        MainActivity.database = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = MainActivity.database.rawQuery("SELECT * FROM CHITIETSUDUNG WHERE MATB = ?", new String[]{thietBi.getMatb()});
        ctsdPhongAdapter.clear();
        phongAdapter.clear();
        while (cursor.moveToNext()) {
            String maphong = cursor.getString(0);
            String matb = cursor.getString(1);
            String ngaysudung = cursor.getString(2);
            String soluong = cursor.getString(3);
            Chitietsudung ctsd = new Chitietsudung(maphong, matb, ngaysudung, soluong);
            ctsdPhongAdapter.add(ctsd);

            Cursor cursor1 = MainActivity.database.rawQuery("SELECT * FROM PHONGHOC WHERE MAPHONG = ?", new String[]{maphong});
            while (cursor1.moveToNext()) {
                String map = cursor1.getString(0);
                String loaip = cursor1.getString(1);
                int tang = cursor1.getInt(2);

                Phong_2 phong = new Phong_2(map, loaip, tang);
                phongAdapter.add(phong);

            }
            cursor1.close();
        }
        cursor.close();
    }

    private void getPhongByMaPhong(String maphong) {
        MainActivity.database = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = MainActivity.database.rawQuery("SELECT * FROM PHONGHOC WHERE MAPHONG = ?", new String[]{maphong});
        phongAdapter.clear();
        while (cursor.moveToNext()) {
            String map = cursor.getString(0);
            String loaip = cursor.getString(1);
            int tang = cursor.getInt(2);

            Phong_2 phong = new Phong_2(map, loaip, tang);
            phongAdapter.add(phong);

        }
        cursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ctsd_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuPdf:
                hienThiDialogConfirmPdf();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void hienThiDialogConfirmPdf() {
        final Dialog dialogConfirmPdf = new Dialog(ChitietsudungTBActivity.this);
        dialogConfirmPdf.setContentView(R.layout.dialog_confirm_pdf);

        Button btnXuat = dialogConfirmPdf.findViewById(R.id.btnXuat);
        Button btnHuy = dialogConfirmPdf.findViewById(R.id.btnHuy);

        btnXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xuatPdf();
                dialogConfirmPdf.dismiss();
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogConfirmPdf.dismiss();
            }
        });

        dialogConfirmPdf.show();
    }


    private void xuatPdf() {
        int pageHeight = 1280;
        int pagewidth = 768;
        final int PERMISSION_REQUEST_CODE = 200;

        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }

        generatePDF(pagewidth, pageHeight);

    }

    private void generatePDF(int pagewidth, int pageHeight) {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint title = new Paint();
        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);
        Canvas canvas = myPage.getCanvas();

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        title.setTextSize(24);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        title.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("CHI TIẾT SỬ DỤNG", 384, 80, title);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        title.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("THIẾT BỊ",30 , 100, title);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        canvas.drawText("MÃ THIẾT BỊ",60 , 130, title);


        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        canvas.drawText("TÊN THIẾT BỊ",60 , 160, title);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        canvas.drawText("Xuất xứ", 60, 190, title);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        canvas.drawText(thietBi.getMatb(),160 , 130, title);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        canvas.drawText(thietBi.getTentb(),160 , 160, title);


        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        canvas.drawText(String.valueOf(thietBi.getXuatxu()),160 , 190, title);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        canvas.drawText("THIẾT BỊ SỬ DỤNG",30 , 240, title);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        canvas.drawText("Mã phòng",30 , 270, title);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        canvas.drawText("Loại phòng",130 , 270, title);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        canvas.drawText("Tầng",320 , 270, title);


        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        canvas.drawText("Số lượng",430 , 270, title);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        canvas.drawText("Ngày sử dụng",530 , 270, title);

        int heightItem = 270;

        for (int i = 0; i < ctsdPhongAdapter.getCount(); i ++) {
            heightItem += 30;
            Phong_2 phong = phongAdapter.getItem(i);

            title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            title.setTextSize(15);
            title.setColor(ContextCompat.getColor(this, android.R.color.black));
            canvas.drawText(phong.getMa(),30 , heightItem, title);

            title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            title.setTextSize(15);
            title.setColor(ContextCompat.getColor(this, android.R.color.black));
            canvas.drawText(phong.getLoai(),130 , heightItem, title);

            title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            title.setTextSize(15);
            title.setColor(ContextCompat.getColor(this, android.R.color.black));
            canvas.drawText(String.valueOf(phong.getTang()),320 , heightItem, title);


            title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            title.setTextSize(15);
            title.setColor(ContextCompat.getColor(this, android.R.color.black));
            canvas.drawText(ctsdPhongAdapter.getItem(i).getSoluong(),430 , heightItem, title);

            title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            title.setTextSize(15);
            title.setColor(ContextCompat.getColor(this, android.R.color.black));
            canvas.drawText(ctsdPhongAdapter.getItem(i).getNgaysudung(),530 , heightItem, title);
        }

        pdfDocument.finishPage(myPage);
        File file = new File(Environment.getExternalStorageDirectory(), "GFG.pdf");

        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));

            // below line is to print toast message
            // on completion of PDF generation.
            Toast.makeText(ChitietsudungTBActivity.this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // below line is used
            // to handle error
            e.printStackTrace();
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close();


    }

    private boolean checkPermission() {
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 87);
    }
}
