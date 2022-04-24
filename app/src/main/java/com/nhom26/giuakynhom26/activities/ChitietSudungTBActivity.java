package com.nhom26.giuakynhom26.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.nhom26.giuakynhom26.R;
import com.nhom26.model.Chitietsudung;
import com.nhom26.model.Phong;
import com.nhom26.model.Thietbi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ChitietSudungTBActivity extends AppCompatActivity {
    TextView txtMaTB, txtTenTB, txtXuatXu, txtMaLoai;
    ListView lvPhong;
    ArrayAdapter<String> adapterPhong;
    ArrayAdapter<Chitietsudung> chitietsudungAdapter;
    ArrayAdapter<Phong> phongAdapter;
    Thietbi thietbi;
    Phong selectedPhong = null;
    String queryParamenter,tmp="";
    Dialog dialogChitiet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitiet_sudung_tb);
        addControl();
        addEvent();
        registerForContextMenu(lvPhong);

    }

    private void addEvent() {
        lvPhong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedPhong = phongAdapter.getItem(i);
                hienThiManHinhChiTiet();

            }
        });
    }

    private void addControl() {
        Intent intent = getIntent();
        thietbi = (Thietbi) intent.getSerializableExtra("thietbi");

        txtMaTB = (TextView) findViewById(R.id.txtMaTBCTSD);
        txtTenTB = (TextView) findViewById(R.id.txtTenTBCTSD);
        txtXuatXu = (TextView) findViewById(R.id.txtXuatXuCTSD);
        txtMaLoai = (TextView) findViewById(R.id.txtMaLoaiCTSD);
        lvPhong = (ListView) findViewById(R.id.lvPhongCTSD);
        adapterPhong = new ArrayAdapter<String>(ChitietSudungTBActivity.this, android.R.layout.simple_list_item_1);
        lvPhong.setAdapter(adapterPhong);

        txtMaTB.setText(thietbi.getMatb());
        txtTenTB.setText(thietbi.getTentb());
        txtXuatXu.setText(thietbi.getXuatxu());
        txtMaLoai.setText(thietbi.getMaLoai());
        tmp="STT | Loại Phòng | Tầng | Ngày Sử Dụng | SL";
        adapterPhong.add(tmp);
        chitietsudungAdapter = new ArrayAdapter<Chitietsudung>(ChitietSudungTBActivity.this,  android.R.layout.simple_list_item_1);
        getPhongHocChitietsudungFromDB(thietbi);
        phongAdapter = new ArrayAdapter<Phong>(ChitietSudungTBActivity.this,  android.R.layout.simple_list_item_1);
        getPhongHocFromDB();

    }
    private void getPhongHocFromDB() {
        MainActivity.database = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = MainActivity.database.rawQuery("SELECT * FROM PHONGHOC", null);
        phongAdapter.clear();
        while (cursor.moveToNext()) {
            String ma = cursor.getString(0);
            String ten = cursor.getString(1);
            int tang = cursor.getInt(2);
            Phong phong = new Phong(ma, ten, tang);
            phongAdapter.add(phong);
            for (int i = 0;i<chitietsudungAdapter.getCount();i++){
                if (chitietsudungAdapter.getItem(i).getMaphong().equals(ma)){
                    tmp=String.valueOf(i+1)+" | "+ten+" | "+tang+" | "+chitietsudungAdapter.getItem(i).getNgaysudung()+" | "+chitietsudungAdapter.getItem(i).getSoluong();
                    adapterPhong.add(tmp);
                }
            }
        }
        cursor.close();
    }

    private void getPhongHocChitietsudungFromDB(Thietbi thietbi) {
        MainActivity.database = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = MainActivity.database.rawQuery("SELECT * FROM CHITIETSUDUNG WHERE MATB = ?", new String[]{thietbi.getMatb()});
        chitietsudungAdapter.clear();
        queryParamenter = "";
        while (cursor.moveToNext()) {
            String map = cursor.getString(0);
            String matb = cursor.getString(1);
            String ngaysd = cursor.getString(2);
            String soluong = cursor.getString(3);
            Chitietsudung ct = new Chitietsudung(map, matb, ngaysd, soluong);
            chitietsudungAdapter.add(ct);
            queryParamenter += "?,";
        }
        cursor.close();
    }

    private void hienThiManHinhChiTiet(){
        dialogChitiet = new Dialog(ChitietSudungTBActivity.this);
        dialogChitiet.setContentView(R.layout.dialog_phong_detail);

        TextView txtMa = dialogChitiet.findViewById(R.id.txtMaPhong);
        TextView txtLoai = dialogChitiet.findViewById(R.id.txtLoaiPhong);
        TextView txtTang = dialogChitiet.findViewById(R.id.txtTang);
        Button btnChitietsudung = dialogChitiet.findViewById(R.id.btnChiTietSuDung);

        txtMa.setText(selectedPhong.getMa());
        txtLoai.setText(selectedPhong.getLoai());
        txtTang.setText(String.valueOf(selectedPhong.getTang()));

        btnChitietsudung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChitietSudungTBActivity.this, ChitietsudungPhongActivity.class);
                intent.putExtra("phong", selectedPhong);

                startActivity(intent);
            };
        });

        dialogChitiet.show();
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
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 87) {
            if (grantResults.length > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denined.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
    private void hienThiDialogConfirmPdf() {
        final Dialog dialogConfirmPdf = new Dialog(ChitietSudungTBActivity.this);
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
        canvas.drawText("Thiết bị",30 , 100, title);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        canvas.drawText("Mã Thiết Bị",60 , 130, title);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        canvas.drawText("Tên Thiết Bị",60 , 160, title);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        canvas.drawText(thietbi.getMatb(),160 , 130, title);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        canvas.drawText(thietbi.getTentb(),160 , 160, title);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        canvas.drawText("PHÒNG SỬ DỤNG",30 , 240, title);

        int heightItem = 270;

        for (int i = 0; i < adapterPhong.getCount(); i ++) {
            heightItem += 30;

            title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            title.setTextSize(15);
            title.setColor(ContextCompat.getColor(this, android.R.color.black));
            canvas.drawText(adapterPhong.getItem(i),30 , heightItem, title);

        }

        pdfDocument.finishPage(myPage);
        File file = new File(Environment.getExternalStorageDirectory(), "GFG.pdf");

        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));

            // below line is to print toast message
            // on completion of PDF generation.
            Toast.makeText(ChitietSudungTBActivity.this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
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
