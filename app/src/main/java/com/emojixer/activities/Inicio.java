package com.emojixer.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.emojixer.R;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.drawee.backends.pipeline.Fresco;

public class Inicio  extends AppCompatActivity {

    LottieAnimationView estaticos,animados;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.inicio);
        estaticos = findViewById(R.id.estaticos);
        animados = findViewById(R.id.animados);
        animados.playAnimation();

        estaticos.playAnimation();
    }
    public void abrir_estaticos(View view) {
        Intent intent = new Intent(Inicio.this, Activityestaticos.class);
        startActivity(intent);
    }
    public void abrir_animados(View view) {
        Intent intent = new Intent(Inicio.this, MainActivity.class);
        startActivity(intent);
    }
}
