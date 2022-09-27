package com.example.projcamera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Activity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        ImageView Imagedisplay = (ImageView) findViewById(R.id.dpImage);
        Bitmap image = (Bitmap) extras.get("image");
        Imagedisplay.setImageBitmap(image);

        Button button2 = findViewById(R.id.btn2);

        Spinner spinner = findViewById(R.id.category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Image_Category, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        button2.setOnClickListener(new  View.OnClickListener(){
            public void onClick(View view){
                sendRequest(image);
                finish();
            }
        });
    }

    public void sendRequest(Bitmap bitmap) {
        Spinner category = findViewById(R.id.category);
        String cat = category.getSelectedItem().toString();

        String fullURL = "http://" + "10.153.91.18" + ":" + 8000 + "/";

        OkHttpClient client = new OkHttpClient();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        multipartBodyBuilder.addFormDataPart("image","image"+".jpeg", RequestBody.create(MediaType.parse("image/*jpeg"), byteArray));
        multipartBodyBuilder.addFormDataPart("category", cat);
        RequestBody postBodyImage = multipartBodyBuilder.build();

        Request request = new Request.Builder()
                .post(postBodyImage)
                .url(fullURL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) throws IOException {
                assert response.body() != null;
                final String responseData = response.body().toString();
                System.out.println(responseData);
            }
        });
    }
}