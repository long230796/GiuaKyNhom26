package com.nhom26.giuakynhom26.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhom26.giuakynhom26.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    public static String DATABASE_NAME = "dbGiuaKyNhom26.db";
    String DB_PATH_SUFFIX = "/databases/";
    public static SQLiteDatabase database = null;

    TextView programName;
    ImageView mainImage;
    Button btnLoai, btnPhong, btnThietbi, btnChitiet;

    String sharePreferenceName = "trangthai";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        processCopy();
        addControls();

    }

    private void processCopy() {
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                copyDataBaseFromAsset();
                Toast.makeText(this, "Copying success from Assets folder" , Toast.LENGTH_LONG).show();

            }
            catch (Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getDataBasePath() {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }

    public void copyDataBaseFromAsset() {
        try {
            InputStream myInput;
            myInput = getAssets().open(DATABASE_NAME);
            String outFileName = getDataBasePath();
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists())
                f.mkdir();
            OutputStream myOuput = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOuput.write(buffer, 0, length);
            }
            myOuput.flush();
            myOuput.close();
            myInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addControls() {
        programName = (TextView) findViewById(R.id.txtProgramName);
        mainImage = (ImageView) findViewById(R.id.imgMain);
        btnLoai = (Button) findViewById(R.id.btnLoai );
        btnPhong = (Button) findViewById(R.id.btnPhong );
        btnThietbi = (Button) findViewById(R.id.btnThietbi );
        btnChitiet = (Button) findViewById(R.id.btnChitiet );

        docSharedPreferences();
    }

    public void quanLyPhong(View view) {
        Intent intent = new Intent(MainActivity.this, PhongActivity.class);
        startActivity(intent);
    }

    public void quanLyThietBi(View view) {
        Intent intent = new Intent(com.nhom26.giuakynhom26.activities.MainActivity.this, ThietBiActivity.class);
        startActivity(intent);
    }

    public void quanLyLoai(View view) {
        Intent intent = new Intent(com.nhom26.giuakynhom26.activities.MainActivity.this, LoaiTBActivity.class);
        startActivity(intent);
    }

    public void thongTinPhanMem(View view) {
        Dialog dialogThongTinPhanMem = new Dialog(MainActivity.this);
        dialogThongTinPhanMem.setContentView(R.layout.dialog_thongtinphanmem);

        Button btnLienHe = dialogThongTinPhanMem.findViewById(R.id.btnLienHe);

        btnLienHe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SendMailActivity.class);
                startActivity(intent);
            }
        });
        dialogThongTinPhanMem.show();
    }


    private void docSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences(sharePreferenceName, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String imageString = preferences.getString("imageString", "");
        String title = preferences.getString("programeName", "");

        if (imageString.matches("") && title.matches("")) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.imagemain);
            String tempString = BitMapToString(bitmap);
            luuImageSharedPreferences(tempString);
            luuTitleSharedPreferences("Chương trình quản lý thiết bị phòng học");
            docImageSharedPreferences();
            docTitleSharedPreferences();

        } else if (imageString.matches("") && !title.matches("")) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.imagemain);
            String tempString = BitMapToString(bitmap);
            luuImageSharedPreferences(tempString);
            docImageSharedPreferences();
            programName.setText(title);

        } else if (!imageString.matches("") && title.matches("")) {
            luuTitleSharedPreferences("Chương trình quản lý thiết bị phòng học");
            docTitleSharedPreferences();
            mainImage.setImageBitmap(StringToBitMap(imageString));

        } else {
            programName.setText(title);
            mainImage.setImageBitmap(StringToBitMap(imageString));

        }


    }

    private void docImageSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences(sharePreferenceName, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String imageString = preferences.getString("imageString", "");
        Bitmap hinh = StringToBitMap(imageString);
        mainImage.setImageBitmap(hinh);
    }

    private void docTitleSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences(sharePreferenceName, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        programName.setText(preferences.getString("programeName", ""));
    }


    private void luuTitleSharedPreferences(String title) {
        SharedPreferences preferences = getSharedPreferences(sharePreferenceName, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("programeName", title);
        editor.commit();
    }

    private void luuImageSharedPreferences(String imageName) {
        SharedPreferences preferences = getSharedPreferences(sharePreferenceName, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("imageString", imageName);
        editor.commit();
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuEditTitle:
                final Dialog dialogMainEditTitle = new Dialog(MainActivity.this);
                dialogMainEditTitle.setContentView(R.layout.dialog_main_title_edit);
                dialogMainEditTitle.show();

                final EditText edtTitle = dialogMainEditTitle.findViewById(R.id.edtTitle);
                Button btnLuu = dialogMainEditTitle.findViewById(R.id.btnLuu);
                Button btnHuy = dialogMainEditTitle.findViewById(R.id.btnHuy);

                btnLuu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!edtTitle.getText().toString().matches("")) {
                            luuTitleSharedPreferences(edtTitle.getText().toString());
                            docTitleSharedPreferences();
                            dialogMainEditTitle.dismiss();
                            Toast.makeText(MainActivity.this, "Sửa tiêu đề thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Vui Lòng nhập tiêu đề", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogMainEditTitle.dismiss();
                    }
                });
                break;
            case R.id.mnuChangeImage:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent.createChooser(intent, "Chon Hinh"), 113);

                break;

            case R.id.mnuDefault:
                SharedPreferences preferences = getSharedPreferences(sharePreferenceName, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.imagemain);
                String tempString = BitMapToString(bitmap);
                luuImageSharedPreferences(tempString);
                luuTitleSharedPreferences("Chương trình quản lý thiết bị phòng học");
                docImageSharedPreferences();
                docTitleSharedPreferences();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 113 && resultCode == RESULT_OK) {
            try {
                Uri imgURI = data.getData();
                Bitmap hinh = MediaStore.Images.Media.getBitmap(getContentResolver(), imgURI);
                String imageString = BitMapToString(hinh);
                luuImageSharedPreferences(imageString);
                docImageSharedPreferences();
            } catch (Exception ex) {
                Log.e("loi: ", ex.toString());
            }
        }
    }
}
