package com.webabcd.androiddemo.view;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.webabcd.androiddemo.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ViewDemo3 extends AppCompatActivity {

    private TextView mTextView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_viewdemo3);

        mTextView3 = findViewById(R.id.textView3);

        sample();
    }

    private void sample() {
        mTextView3.setTextColor(Color.argb(0xff, 0xff, 0x00, 0x00));
        mTextView3.setBackgroundColor(getResources().getColor(R.color.blue));
    }
}
