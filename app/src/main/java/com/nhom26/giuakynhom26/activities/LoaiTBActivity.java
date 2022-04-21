package com.nhom26.giuakynhom26.activities;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.nhom26.giuakynhom26.R;
import com.nhom26.model.Loai;

import java.util.Random;

public class LoaiTBActivity extends AppCompatActivity {

    ListView lvLoaiTB;
    ArrayAdapter<Loai> loaiAdapter;
    Loai selectedLoai = null;
    Dialog dialogThaoTac;
    Dialog dialogThem;
    Dialog dialogChinhSua;
    Dialog dialogChitiet;
    Dialog dialogXoa;

    EditText edtLoaiTB;
    EditText edtMaLoai;
    TextView maLoai;
    Button btnLuu;
    Button btnHuy;
    ImageView imgThemLoaiTB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loai_tb);
        addControls();
        getLoaiTBFromDB();
        addEvents();
        registerForContextMenu(lvLoaiTB);
    }

    private void addEvents() {
        lvLoaiTB.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedLoai = loaiAdapter.getItem(i);
                hienThiManHinhChiTiet();
            }
        });

        lvLoaiTB.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedLoai = loaiAdapter.getItem(i);
                return false;

            }
        });

        imgThemLoaiTB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hienThiManHinhThemLoai();
            }
        });


    }

    private void addControls() {
        lvLoaiTB = (ListView) findViewById(R.id.lvLoaiTB);

        loaiAdapter = new ArrayAdapter<Loai>(LoaiTBActivity.this, android.R.layout.simple_list_item_1);
        lvLoaiTB.setAdapter(loaiAdapter);

        imgThemLoaiTB = (ImageView) findViewById(R.id.imgThemLoaiTB);


    }

    private void getLoaiTBFromDB() {
        com.nhom26.giuakynhom26.activities.MainActivity.database = openOrCreateDatabase(com.nhom26.giuakynhom26.activities.MainActivity.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = com.nhom26.giuakynhom26.activities.MainActivity.database.rawQuery("SELECT * FROM LOAITHIETBI", null);
        loaiAdapter.clear();
        while (cursor.moveToNext()) {
            String ma = cursor.getString(0);
            String ten = cursor.getString(1);
            Loai loai = new Loai(ma, ten);
            loaiAdapter.add(loai);
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
                loaiAdapter.getFilter().filter(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.mnuThietbi:
                intent = new Intent(LoaiTBActivity.this, ThietBiActivity.class);
                startActivity(intent);
                break;

            case R.id.mnuPhong:
                intent = new Intent(LoaiTBActivity.this, PhongActivity.class);
                startActivity(intent);
                break;

            case R.id.mnuChitiet:
                Dialog dialogThongTinPhanMem = new Dialog(LoaiTBActivity.this);
                dialogThongTinPhanMem.setContentView(R.layout.dialog_thongtinphanmem);

                Button btnLienHe = dialogThongTinPhanMem.findViewById(R.id.btnLienHe);

                btnLienHe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(LoaiTBActivity.this, SendMailActivity.class);
                        startActivity(intent);
                    }
                });
                dialogThongTinPhanMem.show();
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




    private void hienThiManHinhChiTiet() {
        dialogChitiet = new Dialog(LoaiTBActivity.this);
        dialogChitiet.setContentView(R.layout.dialog_loaitb_detail);

        TextView txtMa = dialogChitiet.findViewById(R.id.txtMaLoaidetail);
        TextView txtLoai = dialogChitiet.findViewById(R.id.txtLoaiTBdetail);

        txtMa.setText(selectedLoai.getMaLoai());
        txtLoai.setText(selectedLoai.getTenLoai());

        dialogChitiet.show();
    }

    private void hienThiManHinhThaoTacLoai() {
        dialogThaoTac = new Dialog(LoaiTBActivity.this);
        dialogThaoTac.setContentView(R.layout.dialog_phong_action);
        dialogThaoTac.show();
    }

    private void hienThiManHinhThemLoai() {
        dialogThem = new Dialog(LoaiTBActivity.this);
        dialogThem.setContentView(R.layout.dialog_loai_add);

        Random r = new Random();
        int randomInt = r.nextInt(10000) + 1;

        edtLoaiTB = (EditText) dialogThem.findViewById(R.id.edtLoaiadd);
        maLoai=(TextView) dialogThem.findViewById(R.id.txtMaLoaiadd);
        btnLuu = (Button) dialogThem.findViewById(R.id.btnThemLoai);
        btnHuy = (Button) dialogThem.findViewById(R.id.btnHuyThemLoai);
        maLoai.setText(layMa(edtLoaiTB.getText().toString())+String.valueOf(randomInt));

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
//                Toast.makeText(LoaiTBActivity.this, "Them thành công", Toast.LENGTH_LONG).show();

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


    private void hienThiManHinhEditLoai() {
        dialogChinhSua = new Dialog(LoaiTBActivity.this);
        dialogChinhSua.setContentView(R.layout.dialog_loai_edit);

        edtLoaiTB = (EditText) dialogChinhSua.findViewById(R.id.edtLoaiedit);
        maLoai = (TextView) dialogChinhSua.findViewById(R.id.txtMaLoaiedit);
        btnLuu = (Button) dialogChinhSua.findViewById(R.id.btnCapNhatLoai);
        btnHuy = (Button) dialogChinhSua.findViewById(R.id.btnHuyCapNhatLoai);

        maLoai.setText(selectedLoai.getMaLoai());
        edtLoaiTB.setText(selectedLoai.getTenLoai());

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

    private void hienThiManHinhXoaLoai() {
        dialogXoa = new Dialog(LoaiTBActivity.this);
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


    private void update () {

        ContentValues values = new ContentValues();
        values.put("MALOAI", maLoai.getText().toString());
        values.put("TENLOAI", edtLoaiTB.getText().toString());

        int kq = com.nhom26.giuakynhom26.activities.MainActivity.database.update("LOAITHIETBI", values, "MALOAI=?", new String[]{maLoai.getText().toString()});
        if (kq > 0) {
            Toast.makeText(LoaiTBActivity.this, "Cập nhật thành công", Toast.LENGTH_LONG).show();
            dialogChinhSua.dismiss();
            getLoaiTBFromDB();
        } else {
            Toast.makeText(LoaiTBActivity.this, "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_LONG).show();
        }
    }

    private void add () {

        ContentValues values = new ContentValues();
        values.put("MALOAI", maLoai.getText().toString());
        values.put("TENLOAI", edtLoaiTB.getText().toString());

        int kq = (int) MainActivity.database.insert("LOAITHIETBI",null, values);
        if (kq > 0) {
            Toast.makeText(LoaiTBActivity.this, "Thêm thành công", Toast.LENGTH_LONG).show();
            dialogThem.dismiss();
            getLoaiTBFromDB();
        } else {
            Toast.makeText(LoaiTBActivity.this, "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_LONG).show();
        }
    }

    private void delete() {
        int kq = com.nhom26.giuakynhom26.activities.MainActivity.database.delete("LOAITHIETBI", "MALOAI=?", new String[]{selectedLoai.getMaLoai()});
        if (kq > 0) {
            Toast.makeText(LoaiTBActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
            dialogXoa.dismiss();
            getLoaiTBFromDB();
        } else {
            Toast.makeText(LoaiTBActivity.this, "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
        }

    }


    private  void cancel(Dialog dialog) {
        dialog.dismiss();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuSua:
                hienThiManHinhEditLoai();
                break;

            case R.id.mnuXoa:
                hienThiManHinhXoaLoai();
                break;

        }
        return super.onContextItemSelected(item);
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
