package com.nhom26.giuakynhom26.activities;

import android.app.Dialog;
import android.content.ContentValues;
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
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhom26.adapter.ChitietThietBiPhongAdapter;
import com.nhom26.giuakynhom26.R;
import com.nhom26.model.Chitietsudung;
import com.nhom26.model.Phong;
import com.nhom26.model.Thietbi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ChitietsudungPhongActivity extends AppCompatActivity {

    TextView txtMaP, txtLoaiP, txtTang;
    ImageView imgAdd;
    ListView lvThietBi;
    Phong phong;
    Thietbi selectedThietBi, selectedUnusedThietbi;
    ChitietThietBiPhongAdapter chitietsudungAdapter;
    ArrayAdapter<Thietbi> thietbiAdapter, unusedThietbiAdapter;
    List<String> usedThietbi;

    String queryParamenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctsd_phong);
        addControls();
        addEvents();
        registerForContextMenu(lvThietBi);

    }

    private void addEvents() {
        lvThietBi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getThietBiByMa(chitietsudungAdapter.getItem(i).getMatb());
                hienThiDialogThietBi(chitietsudungAdapter.getItem(i));


            }
        });

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hienThiManHinhTimThietBi();
            }
        });


        lvThietBi.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                getThietBiByMa(chitietsudungAdapter.getItem(i).getMatb());
                return false;
            }
        });

    }



    private void getThietBiByMa(String matb) {
        for (int i = 0; i < thietbiAdapter.getCount(); i ++) {
            System.out.println(matb + ": " + thietbiAdapter.getItem(i).getMatb());
            if (thietbiAdapter.getItem(i).getMatb().equals(matb)) {
                selectedThietBi = thietbiAdapter.getItem(i);
            }
        }
    }


    private void addControls() {
        Intent intent = getIntent();
        phong = (Phong) intent.getSerializableExtra("phong");

        txtMaP = (TextView) findViewById(R.id.txtMaPhong);
        txtLoaiP = (TextView) findViewById(R.id.txtLoaiPhong);
        txtTang = (TextView) findViewById(R.id.txtTang);
        imgAdd = (ImageView) findViewById(R.id.imgAddEquipment);
        lvThietBi = (ListView) findViewById(R.id.lvThietBi);
        thietbiAdapter = new ArrayAdapter<Thietbi>(ChitietsudungPhongActivity.this, android.R.layout.simple_list_item_1);


        txtMaP.setText(phong.getMa());
        txtLoaiP.setText(phong.getLoai());
        txtTang.setText(String.valueOf(phong.getTang()));

        chitietsudungAdapter = new ChitietThietBiPhongAdapter(ChitietsudungPhongActivity.this, R.layout.phong_custom_chitietsudung);
        unusedThietbiAdapter = new ArrayAdapter<Thietbi>(ChitietsudungPhongActivity.this, android.R.layout.simple_list_item_1);
        usedThietbi = new ArrayList<String>();

        lvThietBi.setAdapter(chitietsudungAdapter);

        getPhongHocChitietsudungFromDB(phong);
        getThietBiFromDB();
        getUnusedThietBi();


    }

    private String removeLastCharacter(String str) {
        String result = null;
        if ((str != null) && (str.length() > 0)) {
            result = str.substring(0, str.length() - 1);
        }
        return result;
    }

    private void getUnusedThietBi() {
        MainActivity.database = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = MainActivity.database.rawQuery("SELECT * FROM THIETBI WHERE MATB NOT IN (" + removeLastCharacter(queryParamenter) + ")", usedThietbi.toArray(new String[usedThietbi.size()]));
        unusedThietbiAdapter.clear();
        while (cursor.moveToNext()) {
            String maTb = cursor.getString(0);
            String tentb = cursor.getString(1);
            String xuatxu = cursor.getString(2);
            String maloai = cursor.getString(3);
            Thietbi tb = new Thietbi(maTb, tentb, xuatxu, maloai);
            unusedThietbiAdapter.add(tb);
        }
        cursor.close();

    }


    private void getPhongHocChitietsudungFromDB(Phong phong) {
        MainActivity.database = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = MainActivity.database.rawQuery("SELECT * FROM CHITIETSUDUNG WHERE MAPHONG = ?", new String[]{phong.getMa()});
        chitietsudungAdapter.clear();
        usedThietbi.clear();
        queryParamenter = "";
        while (cursor.moveToNext()) {
            String map = cursor.getString(0);
            String matb = cursor.getString(1);
            String ngaysd = cursor.getString(2);
            String soluong = cursor.getString(3);
            Chitietsudung ct = new Chitietsudung(map, matb, ngaysd, soluong);
            chitietsudungAdapter.add(ct);
            usedThietbi.add(matb);
            queryParamenter += "?,";
        }
        cursor.close();
    }

    private  void getThietBiFromDB() {
        MainActivity.database = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = MainActivity.database.rawQuery("SELECT * FROM THIETBI", null);
        thietbiAdapter.clear();
        while (cursor.moveToNext()) {
            String maTb = cursor.getString(0);
            String tentb = cursor.getString(1);
            String xuatxu = cursor.getString(2);
            String maloai = cursor.getString(3);
            Thietbi tb = new Thietbi(maTb, tentb, xuatxu, maloai);
            thietbiAdapter.add(tb);
        }
        cursor.close();
    }

    private void hienThiManHinhTimThietBi() {
        final Dialog dialogThemThietBi = new Dialog(ChitietsudungPhongActivity.this);
        dialogThemThietBi.setContentView(R.layout.dialog_phong_add_equipment);

        final TextView txtMatb = (TextView) dialogThemThietBi.findViewById(R.id.txtMatb);
        final TextView txtTentb = (TextView) dialogThemThietBi.findViewById(R.id.txtTentb);
        final TextView txtXuatxu = (TextView) dialogThemThietBi.findViewById(R.id.txtXuatxu);
        final TextView txtMaloai = (TextView) dialogThemThietBi.findViewById(R.id.txtMaloai);
        final Button btnHuy = (Button) dialogThemThietBi.findViewById(R.id.btnHuy);
        final EditText edtSoluong = dialogThemThietBi.findViewById(R.id.edtSoluong);
        Button btnChon = (Button) dialogThemThietBi.findViewById(R.id.btnChon);
        selectedUnusedThietbi = null;


        MultiAutoCompleteTextView mactv;
        mactv = (MultiAutoCompleteTextView)dialogThemThietBi.findViewById(R.id.multiAutoCompleteTextView1);
        mactv.setAdapter(unusedThietbiAdapter);
        mactv.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        mactv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedUnusedThietbi = unusedThietbiAdapter.getItem(i);
                txtMatb.setText(selectedUnusedThietbi.getMatb());
                txtTentb.setText(selectedUnusedThietbi.getTentb());
                txtXuatxu.setText(selectedUnusedThietbi.getXuatxu());
                txtMaloai.setText(selectedUnusedThietbi.getMaloai());
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogThemThietBi.dismiss();
            }
        });

        btnChon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedUnusedThietbi != null) {
                    if (!edtSoluong.getText().toString().matches("")) {
                        // tao ngay
                        String pattern = "dd/MM/yyyy";
                        String dateInString =new SimpleDateFormat(pattern).format(new Date());

                        ContentValues values = new ContentValues();
                        values.put("MAPHONG", phong.getMa());
                        values.put("MATB", selectedUnusedThietbi.getMatb());
                        values.put("NGAYSUDUNG", dateInString);
                        values.put("SOLUONG", edtSoluong.getText().toString());
                        long kq = MainActivity.database.insert("CHITIETSUDUNG", null, values);
                        if (kq > 0) {
                            getPhongHocChitietsudungFromDB(phong);
                            getUnusedThietBi();
                            Toast.makeText(ChitietsudungPhongActivity.this, "Thêm thiết bị thành công", Toast.LENGTH_SHORT).show();
                            dialogThemThietBi.dismiss();

                        }
                        dialogThemThietBi.dismiss();
                    } else {
                        Toast.makeText(ChitietsudungPhongActivity.this, "Vui lòng chọn số lượng", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ChitietsudungPhongActivity.this, "Vui lòng chọn thiết bị", Toast.LENGTH_SHORT).show();
                }
            }
        });


        dialogThemThietBi.show();
    }

    private void hienThiDialogThietBi(Chitietsudung ctsd) {
        Dialog dialogChitietThietBi = new Dialog(ChitietsudungPhongActivity.this);
        dialogChitietThietBi.setContentView(R.layout.dialog_ctsd_thietbi_phong);

        TextView txtMatb = dialogChitietThietBi.findViewById(R.id.txtMatb);
        TextView txtTentb = dialogChitietThietBi.findViewById(R.id.txtTentb);
        TextView txtXuatxu = dialogChitietThietBi.findViewById(R.id.txtXuatxu);
        TextView txtMaloai = dialogChitietThietBi.findViewById(R.id.txtMaloai);
        TextView txtSoluong = dialogChitietThietBi.findViewById(R.id.txtSoluong);
        TextView txtNgaySuDung = dialogChitietThietBi.findViewById(R.id.txtNgaySuDung);

        txtMatb.setText(selectedThietBi.getMatb());
        txtTentb.setText(selectedThietBi.getTentb());
        txtXuatxu.setText(selectedThietBi.getXuatxu());
        txtMaloai.setText(selectedThietBi.getMaloai());
        txtSoluong.setText(ctsd.getSoluong());
        txtNgaySuDung.setText(ctsd.getNgaysudung());

        dialogChitietThietBi.show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Chọn hành động");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ctsd_context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuXoa:
                hienThiManHinhXoaThietbi();
                break;

        }
        return super.onContextItemSelected(item);
    }

    private void hienThiManHinhXoaThietbi() {
        final Dialog dialogXoa = new Dialog(ChitietsudungPhongActivity.this);
        dialogXoa.setContentView(R.layout.dialog_phong_delete);

        Button btnCo = dialogXoa.findViewById(R.id.btnCo);
        Button btnKhong = dialogXoa.findViewById(R.id.btnKhong);

        btnCo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xoaThietBi(dialogXoa);
            }
        });

        btnKhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel(dialogXoa);
            }
        });

        dialogXoa.show();
    }

    private void hienThiDialogConfirmPdf() {
        final Dialog dialogConfirmPdf = new Dialog(ChitietsudungPhongActivity.this);
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
        canvas.drawText("PHÒNG",30 , 100, title);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        canvas.drawText("Mã Phòng",60 , 130, title);


        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        canvas.drawText("Loại Phòng",60 , 160, title);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        canvas.drawText("Tầng", 60, 190, title);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        canvas.drawText(phong.getMa(),160 , 130, title);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        canvas.drawText(phong.getLoai(),160 , 160, title);


        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        canvas.drawText(String.valueOf(phong.getTang()),160 , 190, title);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        canvas.drawText("THIẾT BỊ SỬ DỤNG",30 , 240, title);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        canvas.drawText("Mã TB",30 , 270, title);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        canvas.drawText("Tên TB",130 , 270, title);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        canvas.drawText("Xuất xứ",320 , 270, title);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        canvas.drawText("Mã loại",430 , 270, title);


        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        canvas.drawText("Số lượng",530 , 270, title);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        canvas.drawText("Ngày sử dụng",630 , 270, title);

        int heightItem = 270;

        for (int i = 0; i < chitietsudungAdapter.getCount(); i ++) {
            heightItem += 30;
            getThietBiByMa(chitietsudungAdapter.getItem(i).getMatb());

            title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            title.setTextSize(15);
            title.setColor(ContextCompat.getColor(this, android.R.color.black));
            canvas.drawText(selectedThietBi.getMatb(),30 , heightItem, title);

            title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            title.setTextSize(15);
            title.setColor(ContextCompat.getColor(this, android.R.color.black));
            canvas.drawText(selectedThietBi.getTentb(),130 , heightItem, title);

            title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            title.setTextSize(15);
            title.setColor(ContextCompat.getColor(this, android.R.color.black));
            canvas.drawText(selectedThietBi.getXuatxu(),320 , heightItem, title);

            title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            title.setTextSize(15);
            title.setColor(ContextCompat.getColor(this, android.R.color.black));
            canvas.drawText(selectedThietBi.getMaloai(),430 , heightItem, title);

            title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            title.setTextSize(15);
            title.setColor(ContextCompat.getColor(this, android.R.color.black));
            canvas.drawText(chitietsudungAdapter.getItem(i).getSoluong(),530 , heightItem, title);

            title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            title.setTextSize(15);
            title.setColor(ContextCompat.getColor(this, android.R.color.black));
            canvas.drawText(chitietsudungAdapter.getItem(i).getNgaysudung(),630 , heightItem, title);
        }

        pdfDocument.finishPage(myPage);
        File file = new File(Environment.getExternalStorageDirectory(), "GFG.pdf");

        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));

            // below line is to print toast message
            // on completion of PDF generation.
            Toast.makeText(ChitietsudungPhongActivity.this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
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

    private void cancel(Dialog dialogXoa) {
        dialogXoa.dismiss();
    }

    private void xoaThietBi(Dialog dialogXoa) {
        String maphong = phong.getMa();
        String matb = selectedThietBi.getMatb();
        int kq = MainActivity.database.delete("CHITIETSUDUNG", "MAPHONG=? AND MATB=?", new String[]{maphong, matb});
        if (kq > 0) {
            Toast.makeText(ChitietsudungPhongActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
            dialogXoa.dismiss();
            getPhongHocChitietsudungFromDB(phong);
            getUnusedThietBi();
        } else {
            Toast.makeText(ChitietsudungPhongActivity.this, "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
        }
    }

    private void hienThiManHinhEditThietbi() {

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
}


