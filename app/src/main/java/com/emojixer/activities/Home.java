package com.emojixer.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.emojixer.R;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.drawee.backends.pipeline.Fresco;

public class Home  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.home);
    }
}
