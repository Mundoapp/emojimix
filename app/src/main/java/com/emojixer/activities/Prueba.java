package com.emojixer.activities;

import android.os.Bundle;

import com.emojixer.R;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.drawee.backends.pipeline.Fresco;

public class Prueba extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.gc();

        setContentView(R.layout.prueba);

    }
}
