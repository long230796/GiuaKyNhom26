package com.nhom26.giuakynhom26.activities;

import android.app.Dialog;
import android.content.ContentValues;
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

public class ThietBiActivity extends AppCompatActivity {

    ListView lvTB;
    ArrayAdapter<Thietbi> tBAdapter;
    Thietbi selectedTB = null;
    Dialog dialogThaoTac;
    Dialog dialogChinhSua;
    Dialog dialogChitiet;
    Dialog dialogXoa;
//    Dialog dialogThemLoai;

    Spinner spLoai;
    ArrayAdapter<Loai>loaiArrayAdapter;
    Loai selectedLoai=null;

    TextView maTB;
    EditText edtTB;
    EditText edtXuatxu;

    Button btnLuu;
    Button btnHuy;
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

//        imgThemLoaiTB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                hienThiManHinhThemLoai();
//            }
//        });


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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.phong_context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }



    private void hienThiManHinhChiTiet(){
        dialogChitiet = new Dialog(ThietBiActivity.this);
        dialogChitiet.setContentView(R.layout.activity_tb_detail);

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

//    private void hienThiManHinhThemLoai() {
//        dialogThemLoai = new DialogThemPhong(LoaiTBActivity.this);
//
//        Button btnLuuPhong = dialogThemPhong.findViewById(R.id.btnLuuPhong);
//        Button btnHuyPhong = dialogThemPhong.findViewById(R.id.btnHuyPhong);
//        final EditText edtLoaiPhong = dialogThemPhong.findViewById(R.id.edtLoaiPhong);
//        final EditText edtTang = dialogThemPhong.findViewById(R.id.edtTang);
//
//
//        imgThemThietBi = dialogThemPhong.findViewById(R.id.imgAddEquipment);
//        imgThemThietBi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                hienThiManHinhTimThietBi();
//            }
//        });
//
//        registerForContextMenu(lvThietBi);
//
//        btnLuuPhong.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (edtLoaiPhong.getText() != null && edtTang != null) {
//                    // tao maphong
//                    Random r = new Random();
//                    int randomInt = r.nextInt(10000) + 1;
//
//                    // tao ngay
//                    String pattern = "dd/MM/yyyy";
//                    String dateInString =new SimpleDateFormat(pattern).format(new Date());
//
//
//                    // them phong
//                    ContentValues values = new ContentValues();
//                    values.put("MAPHONG", "p" + String.valueOf(randomInt));
//                    values.put("LOAIPHONG", edtLoaiPhong.getText().toString());
//                    values.put("TANG", edtTang.getText().toString());
//                    if (kq > 0) {
//                        values.remove("LOAIPHONG");
//                        values.remove("TANG");
//                        long ok = 0;
//                        for (int i = 0; i < selectedThietbiAdapter.getCount(); i ++) {
//                            values.put("MATB", selectedThietbiAdapter.getItem(i).getMatb());
//                            values.put("SOLUONG", selectedThietbiAdapter.getItem(i).getSoluong());
//                            values.put("NGAYSUDUNG", dateInString);
//                            ok = MainActivity.database.insert("CHITIETSUDUNG", null, values);
//                            if (ok <= 0) {
//                                Toast.makeText(PhongActivity.this, "Không thể thêm chi tiết sử dụng, thiết bị: " + selectedThietbiAdapter.getItem(i).getMatb() , Toast.LENGTH_SHORT).show();
//                                break;
//                            }
//                        }
//                        Toast.makeText(PhongActivity.this, "Thêm phòng thành công", Toast.LENGTH_SHORT).show();
//                        dialogThemPhong.dismiss();
//                        getPhongHocFromDB();
//                    } else {
//                        Toast.makeText(PhongActivity.this, "Thêm phòng thất bại, vui lòng thử lại", Toast.LENGTH_SHORT).show();
//                    }
//
//                    Toast.makeText(PhongActivity.this, "finish", Toast.LENGTH_SHORT).show();
//
//                } else {
//                    Toast.makeText(PhongActivity.this, "Vui lòng điền đủ thông tin", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        btnHuyPhong.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // reset thietbiAdapter
//                thietBiAdapter = null;
//                dialogThemPhong.dismiss();
//            }
//        });
//
//        dialogThemPhong.show();
//    }




    private void hienThiManHinhEditTB() {
        dialogChinhSua = new Dialog(ThietBiActivity.this);
        dialogChinhSua.setContentView(R.layout.activity_tb_edit);

        maTB = (TextView) dialogChinhSua.findViewById(R.id.txtMaTBedit);
        edtTB = (EditText) dialogChinhSua.findViewById(R.id.edtTenTBedit);
        edtXuatxu = (EditText) dialogChinhSua.findViewById(R.id.edtXuatXuedit);
        spLoai = (Spinner) dialogChinhSua.findViewById(R.id.spLoaiTB);
        loaiArrayAdapter = new ArrayAdapter<Loai>(ThietBiActivity.this, android.R.layout.simple_spinner_item);
        loaiArrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        spLoai.setAdapter(loaiArrayAdapter);

        selectedLoai=null;
        loaiArrayAdapter = new ArrayAdapter<Loai>(ThietBiActivity.this, android.R.layout.simple_list_item_1);
        getLoaiTBFromDB();

        btnLuu = (Button) dialogChinhSua.findViewById(R.id.btnCapNhatLoai);
        btnHuy = (Button) dialogChinhSua.findViewById(R.id.btnHuyCapNhatLoai);

        maTB.setText(selectedTB.getMatb());
        edtTB.setText(selectedTB.getTentb());
        edtXuatxu.setText(selectedTB.getXuatxu());
        spLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedLoai = loaiArrayAdapter.getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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


    private void update () {



        ContentValues values = new ContentValues();
        values.put("MALOAI", maTB.getText().toString());
        values.put("TENLOAI", edtTB.getText().toString());
        values.put("XUATXU", edtXuatxu.getText().toString());
        values.put("MALOAI", selectedLoai.getMaLoai().toString());
        //maloai

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
}
