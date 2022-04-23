package com.nhom26.giuakynhom26.activities;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nhom26.giuakynhom26.*;
import com.nhom26.model.Loai;
import com.nhom26.model.Thietbi;

import java.util.Random;

public class ThietBiActivity extends AppCompatActivity {

    ListView lvTB;
    ArrayAdapter<Thietbi> tBAdapter;
    Thietbi selectedTB = null;
    Dialog dialogThaoTac;
    Dialog dialogChinhSua;
    Dialog dialogChitiet;
    Dialog dialogXoa;
    Dialog dialogThem;

    Spinner spLoai;
    ArrayAdapter<Loai>loaiArrayAdapter;
    Loai selectedLoai=null;

    TextView maTB;
    EditText edtTB;
    EditText edtXuatxu;
    TextView txtM;

    Button btnLuu;
    Button btnHuy;
    Button btnThem;
    ImageView imgThemTB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thiet_bi);
        addControls();
        getTBFromDB();
        addEvents();
        registerForContextMenu(lvTB);
    }



    private void addEvents() {
        lvTB.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedTB = tBAdapter.getItem(i);
                hienThiManHinhChiTiet();
            }
        });

        lvTB.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedTB = tBAdapter.getItem(i);
                return false;

            }
        });

        imgThemTB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hienThiManHinhThemTB();
            }
        });


    }

    private void addControls() {

        lvTB = (ListView) findViewById(R.id.lvLoaiTB);
        tBAdapter = new ArrayAdapter<Thietbi>(ThietBiActivity.this, android.R.layout.simple_list_item_1);
        lvTB.setAdapter(tBAdapter);
        imgThemTB = (ImageView) findViewById(R.id.imgThemTB);



    }

    private void getLoaiTBFromDB() {
        com.nhom26.giuakynhom26.activities.MainActivity.database = openOrCreateDatabase(com.nhom26.giuakynhom26.activities.MainActivity.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = com.nhom26.giuakynhom26.activities.MainActivity.database.rawQuery("SELECT * FROM LOAITHIETBI", null);
        loaiArrayAdapter.clear();
        while (cursor.moveToNext()) {
            String ma = cursor.getString(0);
            String ten = cursor.getString(1);
            Loai loai = new Loai(ma, ten);
            loaiArrayAdapter.add(loai);
        }
        cursor.close();
    }

    private void getTBFromDB() {
        com.nhom26.giuakynhom26.activities.MainActivity.database = openOrCreateDatabase(com.nhom26.giuakynhom26.activities.MainActivity.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = com.nhom26.giuakynhom26.activities.MainActivity.database.rawQuery("SELECT * FROM THIETBI", null);
        tBAdapter.clear();
        while (cursor.moveToNext()) {
            String ma = cursor.getString(0);
            String ten = cursor.getString(1);
            String xuatxu = cursor.getString(2);
            String maloai = cursor.getString(3);
            Thietbi thietbi = new Thietbi(ma, ten, xuatxu,maloai);
            tBAdapter.add(thietbi);
        }
        cursor.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem mnuSearch = menu.findItem(R.id.mnuSearch);
        SearchView searchView = (SearchView) mnuSearch.getActionView();

//        MenuItemCompat.setOnActionExpandListener(mnuSearch, new MenuItemCompat.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem item) {
//                return false;
//            }
//
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) {
//                return false;
//            }
//        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                tBAdapter.getFilter().filter(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {

            case R.id.mnuPhong:
                intent = new Intent(ThietBiActivity.this, PhongActivity.class);
                startActivity(intent);
                break;

            case R.id.mnuChitiet:
                Dialog dialogThongTinPhanMem = new Dialog(ThietBiActivity.this);
                dialogThongTinPhanMem.setContentView(R.layout.dialog_thongtinphanmem);

                Button btnLienHe = dialogThongTinPhanMem.findViewById(R.id.btnLienHe);

                btnLienHe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ThietBiActivity.this, SendMailActivity.class);
                        startActivity(intent);
                    }
                });
                dialogThongTinPhanMem.show();
                break;

            case R.id.mnuLoaiTb:
                intent = new Intent(ThietBiActivity.this, LoaiTBActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Chọn hành động");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.phong_context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }



    private void hienThiManHinhChiTiet(){
        dialogChitiet = new Dialog(ThietBiActivity.this);
        dialogChitiet.setContentView(R.layout.dialog_tb_detail);

        TextView txtMa = dialogChitiet.findViewById(R.id.txtMaTBdetail);
        TextView txtLoai = dialogChitiet.findViewById(R.id.txtTenTBdetail);
        TextView txtXuatxu = dialogChitiet.findViewById(R.id.txtXuatXudetail);
        TextView txtLoaiTB = dialogChitiet.findViewById(R.id.txtLoaiTBdetailTB);

        txtMa.setText(selectedTB.getMatb());
        txtLoai.setText(selectedTB.getTentb());
        txtXuatxu.setText(selectedTB.getXuatxu());
        txtLoaiTB.setText(findTenLoaiTB(selectedTB.getMaLoai()));

        dialogChitiet.show();
    }

    private void hienThiManHinhThaoTacLoai() {
        dialogThaoTac = new Dialog(ThietBiActivity.this);
        dialogThaoTac.setContentView(R.layout.dialog_phong_action);
        dialogThaoTac.show();
    }

    private void hienThiManHinhThemTB() {

        dialogThem = new Dialog(ThietBiActivity.this);
        dialogThem.setContentView(R.layout.dialog_tb_add);

        Random r = new Random();
        int randomInt = r.nextInt(10000) + 1;

        btnThem = (Button) dialogThem.findViewById(R.id.btnThemTB);
        btnHuy = (Button) dialogThem.findViewById(R.id.btnHuyThemTB);
        maTB = (TextView) dialogThem.findViewById(R.id.txtMaTBadd);
        edtTB =(EditText) dialogThem.findViewById(R.id.edtTenTBadd);
        edtXuatxu =(EditText) dialogThem.findViewById(R.id.edtXuatXuadd);
        spLoai = (Spinner) dialogThem.findViewById(R.id.spLoaiTBadd);
        txtM = (TextView) dialogThem.findViewById(R.id.txtMaLoaiTBadd);
        maTB.setText(layMa(edtTB.getText().toString())+String.valueOf(randomInt));

        loaiArrayAdapter = new ArrayAdapter<Loai>(ThietBiActivity.this, R.layout.simple_spinner_item);
        loaiArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spLoai.setAdapter(loaiArrayAdapter);
        getLoaiTBFromDB();
        selectedLoai=null;

        spLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedLoai = loaiArrayAdapter.getItem(i);
                txtM.setText(selectedLoai.getMaLoai());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                add();
//                Toast.makeText(ThietBiActivity.this, "Thêm thành công", Toast.LENGTH_LONG).show();

            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel(dialogThem);
            }
        });

        dialogThem.show();
    }




    private void hienThiManHinhEditTB() {
        dialogChinhSua = new Dialog(ThietBiActivity.this);
        dialogChinhSua.setContentView(R.layout.dialog_tb_edit);

        maTB = (TextView) dialogChinhSua.findViewById(R.id.txtMaTBedit);
        edtTB = (EditText) dialogChinhSua.findViewById(R.id.edtTenTBedit);
        edtXuatxu = (EditText) dialogChinhSua.findViewById(R.id.edtXuatXuedit);
        spLoai = (Spinner) dialogChinhSua.findViewById(R.id.spLoaiTB);
        txtM = (TextView) dialogChinhSua.findViewById(R.id.txtM);

        loaiArrayAdapter = new ArrayAdapter<Loai>(ThietBiActivity.this, R.layout.simple_spinner_item);
        loaiArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spLoai.setAdapter(loaiArrayAdapter);
        getLoaiTBFromDB();

        selectedLoai=null;

        btnLuu = (Button) dialogChinhSua.findViewById(R.id.btnCapNhatLoai);
        btnHuy = (Button) dialogChinhSua.findViewById(R.id.btnHuyCapNhatLoai);

        maTB.setText(selectedTB.getMatb());
        edtTB.setText(selectedTB.getTentb());
        edtXuatxu.setText(selectedTB.getXuatxu());
        spLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedLoai = loaiArrayAdapter.getItem(i);
                txtM.setText(selectedLoai.getMaLoai());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
                Toast.makeText(ThietBiActivity.this, "Cập nhật thành công", Toast.LENGTH_LONG).show();

            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel(dialogChinhSua);
            }
        });

        dialogChinhSua.show();
    }

    private void hienThiManHinhXoaTB() {
        dialogXoa = new Dialog(ThietBiActivity.this);
        dialogXoa.setContentView(R.layout.dialog_phong_delete);

        Button btnCo = dialogXoa.findViewById(R.id.btnCo);
        Button btnKhong = dialogXoa.findViewById(R.id.btnKhong);

        btnCo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
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

    private void add () {

        ContentValues values = new ContentValues();
        values.put("MATB", maTB.getText().toString());
        values.put("TENTB", edtTB.getText().toString());
        values.put("XUATXU", edtXuatxu.getText().toString());
        values.put("MALOAI", txtM.getText().toString());

        int kq = (int) MainActivity.database.insert("THIETBI",null , values);
        if (kq > 0) {
            Toast.makeText(ThietBiActivity.this, "Thêm thành công", Toast.LENGTH_LONG).show();
            dialogThem.dismiss();
            getTBFromDB();
        } else {
            Toast.makeText(ThietBiActivity.this, "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_LONG).show();
        }
    }

    private void update () {
        ContentValues values = new ContentValues();
        values.put("MATB", maTB.getText().toString());
        values.put("TENTB", edtTB.getText().toString());
        values.put("XUATXU", edtXuatxu.getText().toString());
        values.put("MALOAI", txtM.getText().toString());

        int kq = com.nhom26.giuakynhom26.activities.MainActivity.database.update("THIETBI", values, "MATB=?", new String[]{maTB.getText().toString()});
        if (kq > 0) {
            Toast.makeText(ThietBiActivity.this, "Cập nhật thành công", Toast.LENGTH_LONG).show();
            dialogChinhSua.dismiss();
            getTBFromDB();
        } else {
            Toast.makeText(ThietBiActivity.this, "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_LONG).show();
        }
    }

    private void delete() {
        int kq = com.nhom26.giuakynhom26.activities.MainActivity.database.delete("THIETBI", "MATB=?", new String[]{selectedTB.getMatb()});
        if (kq > 0) {
            Toast.makeText(ThietBiActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
            dialogXoa.dismiss();
            getTBFromDB();
        } else {
            Toast.makeText(ThietBiActivity.this, "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
        }

    }


    private  void cancel(Dialog dialog) {
        dialog.dismiss();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuSua:
                hienThiManHinhEditTB();
                break;

            case R.id.mnuXoa:
                hienThiManHinhXoaTB();
                break;

        }
        return super.onContextItemSelected(item);
    }

    public String findTenLoaiTB(String m){
        loaiArrayAdapter = new ArrayAdapter<Loai>(ThietBiActivity.this, android.R.layout.simple_list_item_1);
        getLoaiTBFromDB();
        String tenLoai="";
        for (int i = 0; i < loaiArrayAdapter.getCount(); i ++){
            System.out.println(m + ":" + loaiArrayAdapter.getItem(i).getMaLoai());
            if (loaiArrayAdapter.getItem(i).getMaLoai().equals(m)){
                   tenLoai=loaiArrayAdapter.getItem(i).getTenLoai();
                break;
             }
        }
        return tenLoai;
    }

    //Hàm lấy chữ cái đầu
    public String layMa(String str){
        String ten = "";
        String[] tu = str.split(" ");
        for (String s : tu) {
            if (!s.equals("") && !s.equals(null)) {
//                System.out.println(String.valueOf(s.charAt(0)));
                ten+=String.valueOf(s.charAt(0));
                ten.toUpperCase();
            }
        }
        return ten;
    }

}
