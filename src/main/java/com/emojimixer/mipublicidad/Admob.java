package com.emojimixer.mipublicidad;

import android.app.Activity;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.FrameLayout;

 import com.emojimixer.R;
import com.emojimixer.activities.MainActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.Arrays;
import java.util.List;

public class Admob {

    public static Admob miPublicidad = null;
    public static final List<String> TEST = Arrays.asList("B539BC74B2C51868C8468FA7825F18CA", "97BCB2B7686B6303A268200D3FBE84AEquit", "6DEE5C562BBDC09E9B42CE646056C5BA","24E5FE9F4C50836AA080AFF9F5F059BD");  //Ivan, Kai, Juanma, Denis
    public static boolean publicidad = true; // Muesta o no muestra publicidad
    private static final int TIEMPO_INTERSTITIAL = 60000; // 60000 es un minutos ya que estas en milisegundos un segundo on 1000 milesegundos y un munuto son 60 segundos asi que 1000*60

    private InterstitialAd mInterstitialAd;
    public boolean controltiempo=true;
    public boolean interstitial_cargado = true;
    public static int heigth_banner=0;
    public static boolean bandera_activity=false;

    public Admob(Activity context) {
        MobileAds.setRequestConfiguration(MobileAds.getRequestConfiguration().toBuilder().setTestDeviceIds(TEST).build());
        cargarInterstitialAd(context);
    }
    public static void Init(Activity context){
        miPublicidad=new Admob(context);
    }
    public void cargarInterstitialAd(final Activity context) {
        interstitial_cargado=false;
//        Log.e("mInterstitialAd","cargarInterstitialAd");
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(context.getString(R.string.intersticial_admob));
        final AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                Log.e("mInterstitialAd","onAdClosed-1");
                interstitial_cargado=false;
                mInterstitialAd.loadAd(adRequest);
                if(bandera_activity) {
                    context.startActivity(new Intent(context, MainActivity.class));
                    bandera_activity=false;
                }
            }

            public void onAdLoaded() {
                Log.e("mInterstitialAd","Cargado con exito-1");
                interstitial_cargado=true;
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.e("mInterstitialAd","Error de carga-1: "+errorCode);
                interstitial_cargado=true;
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(5000);
                        } catch(InterruptedException e) {}
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mInterstitialAd.loadAd(adRequest);
                            }
                        });
                    }
                }).start();
            }

            @Override
            public void onAdOpened() {
                Log.e("mInterstitialAd","Open-1");
            }

            @Override
            public void onAdClicked() {
                Log.e("mInterstitialAd","Click-1");
            }

            @Override
            public void onAdLeftApplication() {
                Log.e("mInterstitialAd","Salimos de la app-1");
            }
        });
    }
    public void verInterstitialAd(Activity context) {
        if(context!=null)
            bandera_activity=true;
        if (publicidad) {
            if(controltiempo) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    controltiempo=false;
                    contatTiempo();
                }
            }else{
                bandera_activity=false;
                if(context!=null)
                    context.startActivity(new Intent(context, MainActivity.class));
            }
        }
    }
    public static void baner(FrameLayout adContainerView, final Activity activity) {
        if (publicidad) {
            final AdView adView = new AdView(activity);
            adView.setAdUnitId(activity.getResources().getString(R.string.banner_admob));
            adContainerView.addView(adView);

            final AdRequest adRequest = new AdRequest.Builder().build();
            AdSize adSize = getAdSize(activity);
            adView.setAdSize(adSize);
            heigth_banner=adSize.getHeight();
            adView.loadAd(adRequest);

            adView.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    adView.loadAd(adRequest);
                }

                public void onAdLoaded() {
                    Log.e("bannerfail","Cargado con exito-1");
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    Log.e("bannerfail","Error de carga-1: "+errorCode);
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                Thread.sleep(5000);
                            } catch(InterruptedException e) {}
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adView.loadAd(adRequest);
                                }
                            });
                        }
                    }).start();
                }

                @Override
                public void onAdOpened() {
                }

                @Override
                public void onAdClicked() {
                }

                @Override
                public void onAdLeftApplication() {
                }
            });
        }
    }
    private void contatTiempo() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(TIEMPO_INTERSTITIAL);
                } catch(InterruptedException e) {}
                controltiempo=true;
            }
        }).start();
    }
    private static AdSize getAdSize(Activity context) {
        Display display = context.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth);
    }
}
