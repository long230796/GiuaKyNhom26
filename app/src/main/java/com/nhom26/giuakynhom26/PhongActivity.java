package com.nhom26.giuakynhom26;

import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.nhom26.model.Phong;

public class PhongActivity extends AppCompatActivity {

    ListView lvPhong;
        ArrayAdapter<Phong> phongAdapter;
    Phong selectedPhong = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phong);
        addControls();
        getPhongHocFromDB();
        addEvents();
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
                hienThiThaoTac();
                return false;

            }
        });
    }

    private void addControls() {
        lvPhong = (ListView) findViewById(R.id.lvPhong);

        phongAdapter = new ArrayAdapter<Phong>(PhongActivity.this, android.R.layout.simple_list_item_1);
        lvPhong.setAdapter(phongAdapter);
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


    private void hienThiManHinhChiTiet() {
        Dialog dialog = new Dialog(PhongActivity.this);
        dialog.setContentView(R.layout.activity_phong_detail);

        TextView txtMa = dialog.findViewById(R.id.txtMaPhong);
        TextView txtLoai = dialog.findViewById(R.id.txtLoaiPhong);
        TextView txtTang = dialog.findViewById(R.id.txtTang);

        txtMa.setText(selectedPhong.getMa());
        txtLoai.setText(selectedPhong.getLoai());
        txtTang.setText(String.valueOf(selectedPhong.getTang()));

        dialog.show();
    }

    private void hienThiThaoTac() {
        Dialog dialog = new Dialog(PhongActivity.this);
        dialog.setContentView(R.layout.activity_phong_action);
        dialog.show();
    }
}
