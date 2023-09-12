package com.emojixer.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.airbnb.lottie.LottieAnimationView;
import com.emojixer.R;
import com.emojixer.mipublicidad.MiPublicidad;
public class SplashActivity extends BaseActivity {
    private int cuenta = 0;
    LottieAnimationView hello2;
    private Handler handler = new Handler();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        MiPublicidad.Init(this);
        cargar();
        hello2 = findViewById(R.id.hello2);

    }

    public void cargar() {
        handler.postDelayed(() -> {
            if (cuenta <= 1300) {
                cuenta += 100;
                cargar();
            } else {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
              //  MiPublicidad.verInterstitialAd(null);
                finish();
            }
        }, 300);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hello2.cancelAnimation();
        hello2 = null;
        handler.removeCallbacksAndMessages(null);


    }
}
