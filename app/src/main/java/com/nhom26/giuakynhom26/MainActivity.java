package com.nhom26.giuakynhom26;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    }

    public void quanLyPhong(View view) {
        Intent intent = new Intent(MainActivity.this, PhongActivity.class);
        startActivity(intent);
    }

    public void quanLyThietBi(View view) {
        Intent intent = new Intent(MainActivity.this, ThietBiActivity.class);
        startActivity(intent);
    }

    public void quanLyLoai(View view) {
        Intent intent = new Intent(MainActivity.this, LoaiTBActivity.class);
        startActivity(intent);
   }
}
