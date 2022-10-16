package com.emojimix.mipublicidad;

import android.app.Activity;
import android.widget.FrameLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MiPublicidad {

    private static MiPublicidad miPublicidad = null;
    private com.emojimix.mipublicidad.Admob admob;
    private static boolean isGmsAvailable;
    private static boolean isHmsAvailable;
    public static boolean interstitial_cargado = false;

    public static void Init(Activity context){
        miPublicidad=new MiPublicidad(context);
    }

    public MiPublicidad(Activity context) {
        int gmsStatus = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);

        isGmsAvailable = (gmsStatus == ConnectionResult.SUCCESS);

            admob=new com.emojimix.mipublicidad.Admob(context);

    }

    public static void verInterstitialAd(Activity activity) {

            miPublicidad.admob.verInterstitialAd(activity);
           }

    public static void baner(FrameLayout adContainerView, final Activity activity) {
            Admob.baner(adContainerView,activity);

    }
}
