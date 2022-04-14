package com.nhom26.giuakynhom26;

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
import android.widget.TextView;
import android.widget.Toast;
import com.nhom26.model.Loai;

public class LoaiTBActivity extends AppCompatActivity {

    ListView lvLoaiTB;
    ArrayAdapter<Loai> loaiAdapter;
    Loai selectedLoai = null;
    Dialog dialogThaoTac;
    Dialog dialogChinhSua;
    Dialog dialogChitiet;
    Dialog dialogXoa;
//    Dialog dialogThemLoai;

    EditText edtLoaiTB;
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

//        imgThemLoaiTB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                hienThiManHinhThemLoai();
//            }
//        });


    }

    private void addControls() {
        lvLoaiTB = (ListView) findViewById(R.id.lvLoaiTB);

        loaiAdapter = new ArrayAdapter<Loai>(LoaiTBActivity.this, android.R.layout.simple_list_item_1);
        lvLoaiTB.setAdapter(loaiAdapter);

        imgThemLoaiTB = (ImageView) findViewById(R.id.imgThemLoaiTB);


    }

    private void getLoaiTBFromDB() {
        MainActivity.database = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = MainActivity.database.rawQuery("SELECT * FROM LOAITHIETBI", null);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.phong_context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }


    private void hienThiManHinhChiTiet() {
        dialogChitiet = new Dialog(LoaiTBActivity.this);
        dialogChitiet.setContentView(R.layout.activity_loaitb_detail);

        TextView txtMa = dialogChitiet.findViewById(R.id.txtMaLoaidetail);
        TextView txtLoai = dialogChitiet.findViewById(R.id.txtLoaiTBdetail);

        txtMa.setText(selectedLoai.getMaLoai());
        txtLoai.setText(selectedLoai.getTenLoai());

        dialogChitiet.show();
    }

    private void hienThiManHinhThaoTacLoai() {
        dialogThaoTac = new Dialog(LoaiTBActivity.this);
        dialogThaoTac.setContentView(R.layout.activity_phong_action);
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
//                    long kq = MainActivity.database.insert("PHONGHOC", null, values);
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




    private void hienThiManHinhEditLoai() {
        dialogChinhSua = new Dialog(LoaiTBActivity.this);
        dialogChinhSua.setContentView(R.layout.activity_loai_edit);

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
        values.put("MALOAI", maLoai.getText().toString());
        values.put("TENLOAI", edtLoaiTB.getText().toString());

        int kq = MainActivity.database.update("LOAITHIETBI", values, "MALOAI=?", new String[]{maLoai.getText().toString()});
        if (kq > 0) {
            Toast.makeText(LoaiTBActivity.this, "Cập nhật thành công", Toast.LENGTH_LONG).show();
            dialogChinhSua.dismiss();
            getLoaiTBFromDB();
        } else {
            Toast.makeText(LoaiTBActivity.this, "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_LONG).show();
        }
    }

    private void delete() {
        int kq = MainActivity.database.delete("LOAITHIETBI", "MALOAI=?", new String[]{selectedLoai.getMaLoai()});
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


}
