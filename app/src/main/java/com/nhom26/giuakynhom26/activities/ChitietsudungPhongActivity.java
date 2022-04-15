package com.nhom26.giuakynhom26.activities;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        inflater.inflate(R.menu.phong_context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuSua:
                hienThiManHinhEditThietbi();
                break;

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

}
