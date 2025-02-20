package com.example.mobileweek4;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.Manifest;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mobileweek4.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding ActivityMainBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Instantiate view binding
        ActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(ActivityMainBinding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        Add url browsing functionality
        ActivityMainBinding.btnWeb.setOnClickListener(v->open_web());
        ActivityMainBinding.btnLocation.setOnClickListener(v->open_map());
        ActivityMainBinding.btCall.setOnClickListener(v -> do_call());
    }

    private void open_web() {
        String url = ActivityMainBinding.etUrl.getText().toString();
        if(!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }

        Intent browseIntent = new Intent(Intent.ACTION_VIEW);
        browseIntent.setData(Uri.parse(url));

        startActivity(browseIntent);
    }

    private void open_map() {
        String location = ActivityMainBinding.etAddress.getText().toString();
        Intent mapIntent = new Intent(Intent.ACTION_VIEW);
        mapIntent.setData(Uri.parse("geo:0,0?q=" + Uri.encode(location)));
        startActivity(mapIntent);
    }

    private void do_call() {
//        Creating an intent now will crash - we have to get permission first
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        } else {
            place_call();
        }
    }

    private void place_call() {
        String phone = ActivityMainBinding.etPhoneNumber.getText().toString();
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phone));
        startActivity(callIntent);
    }
}