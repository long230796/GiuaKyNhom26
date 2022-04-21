package com.nhom26.giuakynhom26.dialog;

import android.app.Dialog;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhom26.adapter.SelectedThietBiAdapter;
import com.nhom26.giuakynhom26.activities.PhongActivity;
import com.nhom26.giuakynhom26.R;

/**
 * Created by long2 on 4/13/2022.
 */
public class DialogThemPhong extends Dialog {

    PhongActivity context;
    int selectedLvItem;

    public DialogThemPhong(PhongActivity context) {
        super(context);
        this.context = context;
        this.setContentView(R.layout.dialog_phong_add);

        context.lvThietBi = (ListView) this.findViewById(R.id.lvThietBi);
        context.selectedThietbiAdapter = new SelectedThietBiAdapter(context, R.layout.phong_custom_equipment);
        context.lvThietBi.setAdapter(context.selectedThietbiAdapter);

        addEvents();

    }

    private void addEvents() {

        context.lvThietBi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Dialog dialogChitietThietbi = new Dialog(context);
                dialogChitietThietbi.setContentView(R.layout.dialog_detail_equipment);

                TextView txtMatb = (TextView) dialogChitietThietbi.findViewById(R.id.txtMatb);
                TextView txtTentb = (TextView) dialogChitietThietbi.findViewById(R.id.txtTentb);
                TextView txtXuatxu = (TextView) dialogChitietThietbi.findViewById(R.id.txtXuatxu);
                TextView txtMaloai = (TextView) dialogChitietThietbi.findViewById(R.id.txtMaloai);
                TextView txtSoluong = (TextView) dialogChitietThietbi.findViewById(R.id.txtSoluong);

                txtMatb.setText(context.selectedThietbiAdapter.getItem(i).getMatb());
                txtTentb.setText(context.selectedThietbiAdapter.getItem(i).getTentb());
                txtXuatxu.setText(context.selectedThietbiAdapter.getItem(i).getXuatxu());
//                txtMaloai.setText(context.selectedThietbiAdapter.getItem(i).getMaloai());
                txtSoluong.setText(context.selectedThietbiAdapter.getItem(i).getSoluong());

                dialogChitietThietbi.show();
            }
        });

        context.lvThietBi.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedLvItem = i;
                return false;
            }

        });
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select The Action");
        menu.add(0, v.getId(), 0, "Edit");
        menu.add(0, v.getId(), 1, "Delete");
    }

    // You should do the processing for the selected context item here. The
    // selected context item gets passed in the MenuItem parameter in
    // the following method. In my answer I am force calling the onContextItemSelected()
    // method but you are free to do the actual processing here itself
    @Override
    public boolean onMenuItemSelected(int aFeatureId, MenuItem aMenuItem) {
        if (aFeatureId == Window.FEATURE_CONTEXT_MENU)
            return onContextItemSelected(aMenuItem);
        else
            return super.onMenuItemSelected(aFeatureId, aMenuItem);
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

    private void hienThiManHinhEditPhong() {
        final Dialog dialogEditSoluong = new Dialog(context);
        dialogEditSoluong.setContentView(R.layout.dialog_phong_edit_equipment);

        TextView txtMatb = (TextView) dialogEditSoluong.findViewById(R.id.txtMatb);
        TextView txtTentb = (TextView) dialogEditSoluong.findViewById(R.id.txtTentb);
        TextView txtXuatxu = (TextView) dialogEditSoluong.findViewById(R.id.txtXuatxu);
        TextView txtMaloai = (TextView) dialogEditSoluong.findViewById(R.id.txtMaloai);
        Button btnHuy = (Button) dialogEditSoluong.findViewById(R.id.btnHuy);
        Button btnSua = (Button) dialogEditSoluong.findViewById(R.id.btnChon);
        final EditText edtSoluong = dialogEditSoluong.findViewById(R.id.edtSoluong);

        txtMatb.setText(context.selectedThietbiAdapter.getItem(selectedLvItem).getMatb());
        txtTentb.setText(context.selectedThietbiAdapter.getItem(selectedLvItem).getTentb());
        txtXuatxu.setText(context.selectedThietbiAdapter.getItem(selectedLvItem).getXuatxu());
        txtMaloai.setText(context.selectedThietbiAdapter.getItem(selectedLvItem).getMaLoai());
        edtSoluong.setText(context.selectedThietbiAdapter.getItem(selectedLvItem).getSoluong());

        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String soluong = edtSoluong.getText().toString();
                context.selectedThietbiAdapter.getItem(selectedLvItem).setSoluong(soluong);
                context.selectedThietbiAdapter.notifyDataSetChanged();

                Toast.makeText(context, "Sửa thành công", Toast.LENGTH_SHORT).show();
                dialogEditSoluong.dismiss();
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogEditSoluong.dismiss();
            }
        });

        dialogEditSoluong.show();
    }


    private void editSoluong(EditText edtSoluong, Dialog dialogEditSoluong) {

    }

    private void hienThiManHinhXoaPhong() {
        final Dialog dialogXoa = new Dialog(context);
        dialogXoa.setContentView(R.layout.dialog_phong_delete);

        Button btnCo = dialogXoa.findViewById(R.id.btnCo);
        Button btnKhong = dialogXoa.findViewById(R.id.btnKhong);

        btnCo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                context.thietBiAdapter.add(context.selectedThietbiAdapter.getItem(selectedLvItem));
                context.selectedThietbiAdapter.remove(context.selectedThietbiAdapter.getItem(selectedLvItem));
                Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                dialogXoa.dismiss();
//                selectedThietbiAdapter.remove(selectedThietbi);
            }
        });

        btnKhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                cancel(dialogXoa);
                Toast.makeText(context, "cancel", Toast.LENGTH_SHORT).show();

            }
        });

        dialogXoa.show();

    }

}