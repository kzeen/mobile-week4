package com.example.mobileweek4;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.Manifest;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mobileweek4.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding ActivityMainBinding;
    private ActivityResultLauncher<Intent> ImagePickerResultLauncher, activityResultLauncher;


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
        ActivityMainBinding.btnWeb.setOnClickListener(v -> open_web());
        ActivityMainBinding.btnLocation.setOnClickListener(v -> open_map());
        ActivityMainBinding.btCall.setOnClickListener(v -> do_call());
        ActivityMainBinding.btnPick.setOnClickListener(v -> pick_image());
        ActivityMainBinding.btStartActivity.setOnClickListener(v -> start_activity(v));

        register_ImagePickerResult();
        register_ActivityResult();
    }

    private void open_web() {
        String url = ActivityMainBinding.etUrl.getText().toString();
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                place_call();
            } else {
                Toast.makeText(this, "Permission denied, enable it in settings", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void register_ImagePickerResult() {
        ImagePickerResultLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri imageUri = data.getData();
                            ActivityMainBinding.tvPickurl.setText(imageUri.toString());
                            ActivityMainBinding.ivPickedImage.setImageURI(imageUri);
                        }
                    }
                });
    }

    private void register_ActivityResult() {
        activityResultLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        if (data != null) {
                            int value = data.getIntExtra("result", 0);
                            Toast.makeText(this, "Result: " + value, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void pick_image() {
        Intent imagePickerIntent = new Intent(Intent.ACTION_PICK);
        imagePickerIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI); // Alias to base path/uri of device gallery
        imagePickerIntent.setType("image/*");
        ImagePickerResultLauncher.launch(imagePickerIntent); // We need a launcher - startactivity is not enough to capture the result
    }

    public void start_activity(View view) {
        Intent intent = new Intent(this, ResultActivity.class);
        activityResultLauncher.launch(intent);
    }
}