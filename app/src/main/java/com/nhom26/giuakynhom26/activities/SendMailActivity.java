package com.nhom26.giuakynhom26.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.nhom26.giuakynhom26.R;

public class SendMailActivity extends AppCompatActivity {

    EditText edtTieuDe;
    EditText edtTinNhan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setIcon(R.drawable.ic_email_black_24dp);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        addControls();
    }

    private void addControls() {
        edtTieuDe = (EditText) findViewById(R.id.edtTieuDe);
        edtTinNhan = (EditText) findViewById(R.id.edtTinNhan);

    }

    protected void sendEmail() {
        String[] TO = {"n18dcat044@student.ptithcm.edu.vn"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, edtTieuDe.getText().toString());
        System.out.println(edtTieuDe.getText());
        emailIntent.putExtra(Intent.EXTRA_TEXT, edtTinNhan.getText());

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(SendMailActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuSendMail:
                sendEmail();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sendmail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
