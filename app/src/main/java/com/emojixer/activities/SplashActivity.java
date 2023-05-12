package com.emojixer.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.emojixer.R;

public class SplashActivity extends BaseActivity {
    private int cuenta = 0;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
    //    MiPublicidad.Init(this);
        Log.e("TAG", "aki run1: "+cuenta );
        cargar(this);
    }





    public void onDestroy() {
        super.onDestroy();
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

    }
    public void cargar(final Activity activity) {
        new Thread(new Runnable() {
            public void run() {
                Log.e("TAG", "aki run: "+cuenta );
                new Thread(new Runnable() {
                    public void run() {
                        while (cuenta <= 1300 ) { //|| !Publicidad.interstitial_cargado || !GlobalVariable.bandera_carga
//                    Log.e("SPLASH"," CUENTA: "+cuenta+" carga: "+GlobalVariable.bandera_carga);
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            cuenta = cuenta + 100;
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(intent);
                             //   MiPublicidad.verInterstitialAd(null);
                                finish();
                            }
                        });
                    }
                }).start();
            }
        }).start();
    }
}
