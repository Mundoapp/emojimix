package com.emojitwomix.verads;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.emojitwomix.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.Arrays;
import java.util.List;

public class Admob {

    public static final List<String> TEST = Arrays.asList("");  //Ivan, Kai, Juanma, Denis
    public static boolean publicidad = true; // Muesta o no muestra publicidad
    private static final int TIEMPO_INTERSTITIAL = 60000; // 60000 es un minutos ya que estas en milisegundos un segundo on 1000 milesegundos y un munuto son 60 segundos asi que 1000*60

    private InterstitialAd mInterstitialAd;
    public boolean controltiempo=true;
    public static int heigth_banner=0;

    public Admob(Activity context) {
        MobileAds.setRequestConfiguration(MobileAds.getRequestConfiguration().toBuilder().setTestDeviceIds(TEST).build());
        cargarInterstitialAd(context);
    }

    public void cargarInterstitialAd(final Activity context) {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(context,context.getString(R.string.intersticial_admob), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        mInterstitialAd=null;
                        cargarInterstitialAd(context);
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                        super.onAdFailedToShowFullScreenContent(adError);
                        mInterstitialAd = null;
                        cargarInterstitialAd(context);
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent();
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(10000);
                        } catch(InterruptedException e) {}
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cargarInterstitialAd(context);
                            }
                        });
                    }
                }).start();
            }
        });
    }
    public void verInterstitialAd(Activity activity) {
        if (publicidad) {
            if(controltiempo) {
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(activity);
                    controltiempo=false;
                    contatTiempo();
                }
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
//            heigth_banner=adSize.getHeight();
            adView.loadAd(adRequest);

            adView.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    adView.loadAd(adRequest);
                }

                public void onAdLoaded() {
                }

                @Override
                public void onAdFailedToLoad(LoadAdError errorCode) {
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                Thread.sleep(10000);
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
            });
        }
    }
    private void contatTiempo()
    {
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
