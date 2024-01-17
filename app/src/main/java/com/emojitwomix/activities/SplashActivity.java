package com.emojitwomix.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;
import com.emojitwomix.R;
import com.emojitwomix.verads.anuncios;
public class SplashActivity extends BaseActivity {
    private int cuenta = 0;
    LottieAnimationView hello2;
    private Handler handler = new Handler();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        anuncios.Init(this);
        cargar();
        hello2 = findViewById(R.id.hello2);

    }

    public void cargar() {
        handler.postDelayed(() -> {
            if (cuenta <= 10) {
                cuenta += 100;
                cargar();
            } else {
                Intent intent = new Intent(SplashActivity.this, Inicio.class);
                startActivity(intent);
              //  anuncios.verInterstitialAd(null);
                finish();
            }
        }, 1650);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hello2.cancelAnimation();
        hello2 = null;
        handler.removeCallbacksAndMessages(null);


    }
}
