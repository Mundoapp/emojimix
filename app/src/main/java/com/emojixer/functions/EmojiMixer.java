package com.emojixer.functions;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class EmojiMixer implements Runnable {

    private final String emoji_1;
    private final String emoji_2;
    private final String idemote_1;
    private final String idemote_2;
    private final String creation_date;
    private String finalbase;
    private String finalojos;
    private String finalojos_objetos;
    private String finalcejas;
    private String finalbocas;
    private String finalobjetos;
    private String finalmanos;
    private int finalancho;
    private int finalleft;
    private int finaltop;
    private int finalrandom;

    private String finaltipo;
    private String finalextra;
    private String finalfondo;
    private float finalrotacion;

    private final Activity mContext;
    private final String LOG = "EMOJI_LOGS";
    public String API = "http://emojixer.com/panel/images_formas/";
    public String API4 = "http://emojixer.com/panel/emoji_formado/";

    public String API3 = "http://emojixer.com/panel/images_formas/vacio.png";

    public String API2 = "http://emojixer.com/panel/images_manual/";

    public EmojiListener listener;

    private String finalURL;
    private String ojos;
    emojismodel totalmodel;

    private String failure_reason;
    private boolean isTaskSuccessful = false;
    private boolean shouldAbortTask = false;
    public static String api_emojismix ="http://emojixer.com/panel/api.php?";

    public EmojiMixer(String emoji1, String emoji2,String idemote2,String idemote1, String date, Activity context, EmojiListener emojiListener) {
        this.listener = emojiListener;
        mContext = context;
        emoji_1 = emoji1;
        emoji_2 = emoji2;
        idemote_1 = idemote1;
        idemote_2 = idemote2;
        creation_date = date;
    }


    @Override
    public void run() {


      //  Log.d(LOG, "Emojis checker started with the following data:\nEmojis 1: " + idemote_1 + "\nEmoji 2: " + idemote_2 + "\nDate: " + creation_date);
        if (isConnected()) {

            checkIfImageEmojiInServer(idemote_1,idemote_2);
        }
        mContext.runOnUiThread(() -> {

            if (isTaskSuccessful) {

                 if (listener != null) {
                     listener.onSuccess(finalbase,finalojos,finalcejas,finalobjetos,finalbocas,finalojos_objetos,finalmanos,finalancho,finalleft,finaltop,finaltipo,finalextra,finalfondo,finalrotacion,finalrandom);

                }
            } else {
                if (listener != null)
                    listener.onFailure(failure_reason);
            }
        });
    }

    public void createemoji() {





        RequestQueue queue = Volley.newRequestQueue(mContext);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, api_emojismix+"emoji1="+idemote_1+"&emoji2="+idemote_2,
                (String) null, new Response.Listener<JSONObject>() {
            //  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(JSONObject response) {



                    // Successfully called Graph. Process data and send to UI.
                    Gson gson = new Gson();
                    totalmodel = gson.fromJson(response.toString(), emojismodel.class);
                    finalojos = API + totalmodel.getojos();
                    finalbocas = API + totalmodel.getbocas();
                    finalojos_objetos = API + totalmodel.getojos_objetos();
                    finalcejas = API + totalmodel.getcejas();
                    finalobjetos = API + totalmodel.getobjetos();
                    finalmanos = API + totalmodel.getmanos();
                    finalancho = totalmodel.getancho();
                    finalleft = totalmodel.getleft();
                    finaltop = totalmodel.gettop();
                    finalrotacion= totalmodel.getrotacion();
                String tipo1 = totalmodel.gettipo1();
                String tipo2 = totalmodel.gettipo2();

                 if(Objects.equals(tipo1, "objeto") || Objects.equals(tipo2, "objeto")  && Objects.equals(idemote_1, idemote_2)) {
                    finalextra = API + totalmodel.getextra();
                    finalrandom= totalmodel.getrandom();
                  }

                 else  if(idemote_1==idemote_2) {
                        finalextra = API4 + totalmodel.getextra();
                        finalrandom= totalmodel.getrandom();

                    }
                 else {
                     finalextra = API + totalmodel.getextra();
                 }


                if(Objects.equals(tipo1, "objeto") && Objects.equals(tipo2, "objeto") && Objects.equals(idemote_1, idemote_2)){
                    finaltipo = "objetodoble";
                    finalbase = API4 + totalmodel.getbase();
                    finalfondo = API + totalmodel.getfondo();
              //      Log.e("TAG", "aki base objeto mism: "+finalbase );

                }

               else if(Objects.equals(tipo1, "emoji3") && Objects.equals(tipo2, "emoji3") && Objects.equals(idemote_1, idemote_2)){
                    finaltipo = "emojis3";
                    finalbase = API + totalmodel.getbase();
                    finalfondo = API + totalmodel.getfondo();
                      //    Log.e("TAG", "aki rotacion m entro: "+finalbase );

                }
                else if(Objects.equals(tipo1, "objeto") && Objects.equals(tipo2, "objeto")){
                    finaltipo = "objetodoble";
                    finalbase = API + totalmodel.getbase();
                    finalfondo = API + totalmodel.getfondo();
               //     Log.e("TAG", "aki base objeto: "+finalbase );

                }

                  else  if(Objects.equals(tipo1, "objeto") || Objects.equals(tipo2, "objeto")){
                        finaltipo = "objeto";
                        finalbase = API4 + totalmodel.getbase();
                        finalfondo = API + totalmodel.getfondo();

                    }


                    else {
                    finalbase = API + totalmodel.getbase();
                    finaltipo = "emoji";

                }


                Log.i("tag", "aki rotacion mixer" + finaltipo);

                    enviar();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Error.
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);



    }




    public void checkIfImageEmojiInServer(String emoji1, String emoji2) {
        if (!shouldAbortTask) {
            String Combination = emoji1 + "_" + emoji2 + ".png";
            finalURL = API2 + Combination;

            Log.d(LOG, "Checking url: " + finalURL);
            if (checkImage(finalURL)) {
                finalbase = finalURL;
                finalojos = API3;
                finalbocas = API3;
                finalojos_objetos = API3;
                finalcejas = API3;
                finalobjetos = API3;
                finalmanos = API3;
                 isTaskSuccessful = true;
            } else {
                Log.d(LOG, "Couldn't find a combination in the regular order, swap emojis then recheck...");
                checkReversedEmojis(emoji1,emoji2);
            }
        }
    }


    public void checkReversedEmojis(String emoji1, String emoji2) {
        if (!shouldAbortTask) {
            String Combination2 = emoji2+"_"+emoji1+".png";
            finalURL = API2 + Combination2;
            Log.d(LOG, "Checking reversed url: " + finalbase);
            if (checkImage(finalURL)) {
                finalbase = finalURL;
                finalojos = API3;
                finalbocas = API3;
                finalojos_objetos = API3;
                finalcejas = API3;
                finalobjetos = API3;
                finalmanos = API3;
                isTaskSuccessful = true;
            } else {
                  createemoji();

            }
        }
    }

    public boolean checkImage(String url) {
        if (isConnected()) {
            try {
                HttpURLConnection.setFollowRedirects(false);
                HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
                con.setRequestMethod("HEAD");
                return con.getResponseCode() == HttpURLConnection.HTTP_OK;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }
    public void enviar() {



        Log.i("tag", "sumat2" + finalbase);


        listener.onSuccess(finalbase,finalojos,finalcejas,finalobjetos,finalbocas,finalojos_objetos,finalmanos,finalancho,finalleft,finaltop,finaltipo,finalextra,finalfondo,finalrotacion,finalrandom);

    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            return true;
        } else {
            Log.d(LOG, "Device is not connected.");
            failure_reason = "Your device is not connected to the internet.";
            isTaskSuccessful = false;
            shouldAbortTask = true;
        }
        return false;
    }

    public void setListener(EmojiListener listener) {
        this.listener = listener;
    }


    public interface EmojiListener {

        void onSuccess(String emojiUrl,String ojos,String cejas, String objetos, String bocas, String finalojos_objetos,String manos,int ancho,int left,int top,String tipo,String extra,String fondo,float rotacion,int random );

        void onFailure(String failureReason);
    }

}
