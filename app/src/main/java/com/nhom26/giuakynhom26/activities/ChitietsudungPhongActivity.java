package com.nhom26.giuakynhom26.activities;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhom26.adapter.ChitietThietBiPhongAdapter;
import com.nhom26.giuakynhom26.R;
import com.nhom26.model.Chitietsudung;
import com.nhom26.model.Phong;
import com.nhom26.model.Thietbi;

import java.util.ArrayList;

public class ChitietsudungPhongActivity extends AppCompatActivity {

    TextView txtMaP, txtLoaiP, txtTang;
    ListView lvThietBi;
    Phong phong;
    Thietbi selectedThietBi;
    ChitietThietBiPhongAdapter chitietsudungAdapter;
    ArrayList<Thietbi> arrayThietbi;

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
//                Toast.makeText(ChitietsudungPhongActivity.this, selectedThietBi.getTentb(), Toast.LENGTH_SHORT).show();
                hienThiDialogThietBi(chitietsudungAdapter.getItem(i));


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
        for (int i = 0; i < arrayThietbi.size(); i ++) {
            System.out.println(matb + ": " + arrayThietbi.get(i).getMatb());
            if (arrayThietbi.get(i).getMatb().equals(matb)) {
                selectedThietBi = arrayThietbi.get(i);
            }
        }
    }


    private void addControls() {
        Intent intent = getIntent();
        phong = (Phong) intent.getSerializableExtra("phong");

        txtMaP = (TextView) findViewById(R.id.txtMaPhong);
        txtLoaiP = (TextView) findViewById(R.id.txtLoaiPhong);
        txtTang = (TextView) findViewById(R.id.txtTang);
        lvThietBi = (ListView) findViewById(R.id.lvThietBi);
        arrayThietbi = new ArrayList<Thietbi>();


        txtMaP.setText(phong.getMa());
        txtLoaiP.setText(phong.getLoai());
        txtTang.setText(String.valueOf(phong.getTang()));

        chitietsudungAdapter = new ChitietThietBiPhongAdapter(ChitietsudungPhongActivity.this, R.layout.phong_custom_chitietsudung);
        lvThietBi.setAdapter(chitietsudungAdapter);

        getPhongHocChitietsudungFromDB(phong);
        getThietBiFromDB();


    }


    private void getPhongHocChitietsudungFromDB(Phong phong) {
        MainActivity.database = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = MainActivity.database.rawQuery("SELECT * FROM CHITIETSUDUNG WHERE MAPHONG = ?", new String[]{phong.getMa()});
        chitietsudungAdapter.clear();
        while (cursor.moveToNext()) {
            String map = cursor.getString(0);
            String matb = cursor.getString(1);
            String ngaysd = cursor.getString(2);
            String soluong = cursor.getString(3);
            Chitietsudung ct = new Chitietsudung(map, matb, ngaysd, soluong);
            chitietsudungAdapter.add(ct);
        }
        cursor.close();
    }

    private  void getThietBiFromDB() {
        MainActivity.database = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = MainActivity.database.rawQuery("SELECT * FROM THIETBI", null);
        arrayThietbi.clear();
        while (cursor.moveToNext()) {
            String maTb = cursor.getString(0);
            String tentb = cursor.getString(1);
            String xuatxu = cursor.getString(2);
            String maloai = cursor.getString(3);
            Thietbi tb = new Thietbi(maTb, tentb, xuatxu, maloai);
            arrayThietbi.add(tb);
        }
        cursor.close();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Chọn hành động");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.phong_context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
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
        } else {
            Toast.makeText(ChitietsudungPhongActivity.this, "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
        }
    }

    private void hienThiManHinhEditThietbi() {

    }

}
