package com.nhom26.giuakynhom26;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhom26.model.Phong;
import com.nhom26.model.Thietbi;

//import android.widget.SearchView;

public class PhongActivity extends AppCompatActivity {

    ListView lvPhong;
    ArrayAdapter<Phong> phongAdapter;
    Phong selectedPhong = null;

    Dialog dialogThaoTac;
    Dialog dialogChinhSua;
    Dialog dialogChitiet;
    Dialog dialogXoa;
    Dialog dialogThemPhong;
    Dialog dialogThemThietBi;

    EditText edtLoaiPhong;
    EditText edtTang;
    TextView maPhong;
    Button btnLuu;
    Button btnHuy;
    ImageView imgThemPhong, imgThemThietBi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phong);
        addControls();
        getPhongHocFromDB();
        addEvents();
        registerForContextMenu(lvPhong);
    }

    private void addEvents() {
        lvPhong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedPhong = phongAdapter.getItem(i);
                hienThiManHinhChiTiet();
            }
        });

        lvPhong.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedPhong = phongAdapter.getItem(i);
                return false;

            }
        });

        imgThemPhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hienThiManHinhThemPhong();
            }
        });


    }

    private void addControls() {
        lvPhong = (ListView) findViewById(R.id.lvPhong);

        phongAdapter = new ArrayAdapter<Phong>(PhongActivity.this, android.R.layout.simple_list_item_1);
        lvPhong.setAdapter(phongAdapter);

        imgThemPhong = (ImageView) findViewById(R.id.imgThemPhong);


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
        }
        cursor.close();
    }

    private  void getThietBiFromDB(ArrayAdapter<Thietbi> adapter) {
        MainActivity.database = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = MainActivity.database.rawQuery("SELECT * FROM THIETBI", null);
        adapter.clear();
        while (cursor.moveToNext()) {
            String matb = cursor.getString(0);
            String tentb = cursor.getString(1);
            String xuatxu = cursor.getString(2);
            String maloai = cursor.getString(3);
            Thietbi tb = new Thietbi(matb, tentb, xuatxu, maloai);
            adapter.add(tb);
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
                phongAdapter.getFilter().filter(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.phong_context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }




    private void hienThiManHinhChiTiet() {
        dialogChitiet = new Dialog(PhongActivity.this);
        dialogChitiet.setContentView(R.layout.activity_phong_detail);

        TextView txtMa = dialogChitiet.findViewById(R.id.txtMaPhong);
        TextView txtLoai = dialogChitiet.findViewById(R.id.txtLoaiPhong);
        TextView txtTang = dialogChitiet.findViewById(R.id.txtTang);

        txtMa.setText(selectedPhong.getMa());
        txtLoai.setText(selectedPhong.getLoai());
        txtTang.setText(String.valueOf(selectedPhong.getTang()));

        dialogChitiet.show();
    }

    private void hienThiManHinhThaoTac() {
        dialogThaoTac = new Dialog(PhongActivity.this);
        dialogThaoTac.setContentView(R.layout.activity_phong_action);
        dialogThaoTac.show();
    }

    private void hienThiManHinhThemPhong() {
        dialogThemPhong = new Dialog(PhongActivity.this);
        dialogThemPhong.setContentView(R.layout.activity_phong_add);

        imgThemThietBi = dialogThemPhong.findViewById(R.id.imgAddEquipment);
        imgThemThietBi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hienThiManHinhTimThietBi();
            }
        });

        dialogThemPhong.show();
    }

    private void hienThiManHinhTimThietBi() {

        dialogThemThietBi = new Dialog(PhongActivity.this);
        dialogThemThietBi.setContentView(R.layout.activity_phong_add_equipment);

        final TextView txtMatb = (TextView) dialogThemThietBi.findViewById(R.id.txtMatb);
        final TextView txtTentb = (TextView) dialogThemThietBi.findViewById(R.id.txtTentb);
        final TextView txtXuatxu = (TextView) dialogThemThietBi.findViewById(R.id.txtXuatxu);
        final TextView txtMaloai = (TextView) dialogThemThietBi.findViewById(R.id.txtMaloai);
        Button btnChon = (Button) dialogThemThietBi.findViewById(R.id.btnChon);
        final Button btnHuy = (Button) dialogThemThietBi.findViewById(R.id.btnHuy);


        MultiAutoCompleteTextView mactv;
        mactv = (MultiAutoCompleteTextView)dialogThemThietBi.findViewById(R.id.multiAutoCompleteTextView1);
        final ArrayAdapter<Thietbi> thietBiAdapter = new ArrayAdapter<Thietbi>(PhongActivity.this, android.R.layout.simple_list_item_1);
        getThietBiFromDB(thietBiAdapter);

        mactv.setAdapter(thietBiAdapter);
        mactv.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        mactv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 Thietbi thietBi = thietBiAdapter.getItem(i);
                txtMatb.setText(thietBi.getMatb());
                txtTentb.setText(thietBi.getTentb());
                txtXuatxu.setText(thietBi.getXuatxu());
                txtMaloai.setText(thietBi.getMaloai());
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogThemThietBi.dismiss();
            }
        });


        dialogThemThietBi.show();
    }

    private void hienThiManHinhEditPhong() {
        dialogChinhSua = new Dialog(PhongActivity.this);
        dialogChinhSua.setContentView(R.layout.activity_phong_edit);

        edtLoaiPhong = (EditText) dialogChinhSua.findViewById(R.id.edtLoaiPhong);
        edtTang = (EditText) dialogChinhSua.findViewById(R.id.edtTang);
        maPhong = (TextView) dialogChinhSua.findViewById(R.id.txtMaPhongUpdateView);
        btnLuu = (Button) dialogChinhSua.findViewById(R.id.btnCapNhatPhong);
        btnHuy = (Button) dialogChinhSua.findViewById(R.id.btnHuyCapNhatPhong);

        maPhong.setText(selectedPhong.getMa());
        edtTang.setText(String.valueOf(selectedPhong.getTang()));
        edtLoaiPhong.setText(selectedPhong.getLoai());

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
//                Toast.makeText(PhongActivity.this, "Cập nhật thành công", Toast.LENGTH_LONG).show();

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

    private void hienThiManHinhXoaPhong() {
        dialogXoa = new Dialog(PhongActivity.this);
        dialogXoa.setContentView(R.layout.activity_phong_delete);

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


    private void update () {

        ContentValues values = new ContentValues();
        values.put("LOAIPHONG", edtLoaiPhong.getText().toString());
        values.put("TANG", edtTang.getText().toString());

        int kq = MainActivity.database.update("PHONGHOC", values, "MAPHONG=?", new String[]{maPhong.getText().toString()});
        if (kq > 0) {
            Toast.makeText(PhongActivity.this, "Cập nhật thành công", Toast.LENGTH_LONG).show();
            dialogChinhSua.dismiss();
            getPhongHocFromDB();
        } else {
            Toast.makeText(PhongActivity.this, "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_LONG).show();
        }
    }

    private void delete() {
        int kq = MainActivity.database.delete("PHONGHOC", "MAPHONG=?", new String[]{selectedPhong.getMa()});
        if (kq > 0) {
            Toast.makeText(PhongActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
            dialogXoa.dismiss();
            getPhongHocFromDB();
        } else {
            Toast.makeText(PhongActivity.this, "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
        }

    }


    private  void cancel(Dialog dialog) {
        dialog.dismiss();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuSua:
                hienThiManHinhEditPhong();
                break;

            case R.id.mnuXoa:
                hienThiManHinhXoaPhong();
                break;

        }
        return super.onContextItemSelected(item);
    }
}
