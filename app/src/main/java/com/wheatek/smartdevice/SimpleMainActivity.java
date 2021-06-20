package com.wheatek.smartdevice;

import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SimpleMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simplemain_activity);
        DisplayMetrics a = getResources().getDisplayMetrics();
        System.out.println("widthPixels" + a.widthPixels + "heightPixels" + a.heightPixels + "density" + a.density);
    }
}
