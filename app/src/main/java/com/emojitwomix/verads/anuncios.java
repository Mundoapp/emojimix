package com.emojitwomix.verads;

import android.app.Activity;
import android.widget.FrameLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class anuncios {

    private static anuncios anuncios = null;
    private com.emojitwomix.verads.Admob admob;
    private static boolean isGmsAvailable;
    private static boolean isHmsAvailable;
    public static boolean interstitial_cargado = false;

    public static void Init(Activity context){
        anuncios=new anuncios(context);
    }

    public anuncios(Activity context) {
        int gmsStatus = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);

        isGmsAvailable = (gmsStatus == ConnectionResult.SUCCESS);

            admob=new com.emojitwomix.verads.Admob(context);

    }

    public static void verInterstitialAd(Activity activity) {

        if (anuncios != null && anuncios.admob != null) {
            anuncios.admob.verInterstitialAd(activity);
        }
    }

    public static void baner(FrameLayout adContainerView, final Activity activity) {
            Admob.baner(adContainerView,activity);

    }
}
