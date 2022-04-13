package com.nhom26.giuakynhom26.dialog;

import android.app.Dialog;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.nhom26.giuakynhom26.PhongActivity;
import com.nhom26.giuakynhom26.R;

/**
 * Created by long2 on 4/13/2022.
 */
public class DialogThemPhong extends Dialog {

    PhongActivity context;
    ListView lvThietbi;
    int selectedLvItem;

    public DialogThemPhong( PhongActivity context) {
        super(context);
        this.context = context;

    }

    private void addEvents() {
        lvThietbi.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
        System.out.print("mnuSua");
        if (aFeatureId == Window.FEATURE_CONTEXT_MENU)
            return onContextItemSelected(aMenuItem);
        else
            return super.onMenuItemSelected(aFeatureId, aMenuItem);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mnuSua:
//                hienThiManHinhEditPhong();
                Toast.makeText(context, "mnuSua", Toast.LENGTH_SHORT).show();
                break;

            case R.id.mnuXoa:
                Toast.makeText(context, "mnuXoa", Toast.LENGTH_SHORT).show();
                hienThiManHinhXoaPhong();
                break;

        }
        return super.onContextItemSelected(item);
    }

    private void hienThiManHinhXoaPhong() {
        final Dialog dialogXoa = new Dialog(context);
        dialogXoa.setContentView(R.layout.activity_phong_delete);

        Button btnCo = dialogXoa.findViewById(R.id.btnCo);
        Button btnKhong = dialogXoa.findViewById(R.id.btnKhong);

        btnCo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lvThietbi = context.lvThietBi;
                addEvents();
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