package com.emojixer.activities;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;
import static com.emojixer.functions.UIMethods.shadAnim;
import static com.emojixer.functions.Utils.getRecyclerCurrentItem;
import static com.emojixer.functions.Utils.setSnapHelper;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieCompositionFactory;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.emojixer.R;
import com.emojixer.adapters.EmojimixerAdapter;
import com.emojixer.functions.CenterZoomLayoutManager;
import com.emojixer.functions.EmojiMixer;
import com.emojixer.functions.FileUtil;
import com.emojixer.functions.Nemojismodel;
import com.emojixer.functions.RequestNetwork;
import com.emojixer.functions.RequestNetworkController;
import com.emojixer.functions.offsetItemDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Emojitop extends AppCompatActivity {
    private LottieAnimationView saveEmoji;
    private WrapContentDraweeView mixedfondo,mixedEmojicejas,explosion,mixedEmoji0;
    private LottieAnimationView progressBar;
    LottieAnimationView mixedEmojiojos_objetos,mixedEmoji,mixedEmojiojos,mixedEmojibocas,mixedEmojiobjetos,mixedEmojimanos2,mixedEmojimanos,mixedemojiforma;

    private TextView activityDesc;
    private Button emojitop;
    private String emote1;
    private String emote2;
    private String idemote1;
    private int idemoji1,idemoji2;
    private LottieAnimationView mas;

    private String idemote2;
    private String finalEmojiURL;
    private String ojosfinal;

    private RecyclerView emojisSlider1;
    private ArrayList<HashMap<String, Object>> supportedEmojisList = new ArrayList<>();
    private RequestNetwork requestSupportedEmojis;
    private RequestNetwork updatevotos;

    private RequestNetwork.RequestListener  updatevotoslistener;


    private RequestNetwork.RequestListener requestSupportedEmojisListener;
    private SharedPreferences sharedPref;
    private boolean isFineToUseListeners = false;
    private LinearLayoutManager emojisSlider1LayoutManager;
    private SnapHelper emojisSlider1SnapHelper = new LinearSnapHelper();
    FrameLayout layoutEmojiCreation;
    FrameLayout posicioncara;
    FrameLayout posicionemoji;
    FrameLayout posicionem;

    Activity context;
    Nemojismodel totalmodel;
    public static String api_emojis ="http://animated.emojixer.com/panel/api.php?top=1";
    public static String APITOP ="http://animated.emojixer.com/panel/apitop.php?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.emojitop);
        initLogic();
        LOGIC_BACKEND();
//        getemojis();

        context = this;


        //  MiPublicidad.baner( (FrameLayout) findViewById(R.id.ad_view_container), this);

//        OneSignal.initWithContext(this);
//        OneSignal.setAppId("399b14d7-4c2a-4ce3-ab00-ce6358a44afa");

    }




    public void initLogic() {




        progressBar = findViewById(R.id.progressBar);
         mixedEmoji = findViewById(R.id.mixedEmoji);
        mixedEmojiojos = findViewById(R.id.mixedEmojiojos);
        mixedEmojiojos_objetos = findViewById(R.id.mixedEmojiojos_objetos);
        mixedEmojicejas = findViewById(R.id.mixedEmojicejas);
        mixedEmojibocas = findViewById(R.id.mixedEmojibocas);
        mixedEmojiobjetos = findViewById(R.id.mixedEmojiobjetos);
        mixedEmojimanos = findViewById(R.id.mixedEmojimanos);
        mixedEmojimanos2 = findViewById(R.id.mixedEmojimanos2);

        mixedemojiforma = findViewById(R.id.emojiforma);
        mixedfondo= findViewById(R.id.emojifondo);
        mas = findViewById(R.id.iconmas);
        explosion = findViewById(R.id.explosion);

        saveEmoji = findViewById(R.id.saveEmoji);
        emojisSlider1 = findViewById(R.id.emojisSlider1);
        requestSupportedEmojis = new RequestNetwork(this);
        sharedPref = getSharedPreferences("AppData", Activity.MODE_PRIVATE);
        layoutEmojiCreation =  findViewById(R.id.frame_emoji_creation);
        posicioncara =  findViewById(R.id.posicioncara);
        posicionem = findViewById(R.id.posicione);

        emojitop = findViewById(R.id.emojitop);
        emojitop.setOnClickListener(view -> {
          //  MiPublicidad.verInterstitialAd(this);
            Emojitop.this.startActivity(new Intent(Emojitop.this.getApplicationContext(), Emojitop.class));

        });

        posicionemoji = findViewById(R.id.posicionemoji);
        mas.setOnClickListener(view -> {
            Log.e("TAG", "initLogic: aki entro "+emote1+emote2 );
            mas.playAnimation();

        });


        mixedEmoji.setOnClickListener(view -> {
            isFineToUseListeners = false;
            int random1 = 0;
            int random2 = 0;
            for (int i = 0; i < 2; i++) {
                Random rand = new Random();

                int randomNum = rand.nextInt((supportedEmojisList.size()) - 1);
                if (i == 0) {
                    random1 = randomNum;
                    emojisSlider1.smoothScrollToPosition(randomNum);
                }
            }

//

            emote1 = Objects.requireNonNull(supportedEmojisList.get(getRecyclerCurrentItem(emojisSlider1, emojisSlider1SnapHelper, emojisSlider1LayoutManager)).get("emoji_formado")).toString();

        });

        saveEmoji.setOnClickListener(view -> {

          //  MiPublicidad.verInterstitialAd(this);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

                File file = FileUtil.saveEmojiBIG(context, this.layoutEmojiCreation, context.getFilesDir() + "/stickers/");
                String toastText = "Saved emoji";
                Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_LONG).show();
                actualizovotos();
             //   downloadFile(finalEmojiURL);
            } else {
                if (ContextCompat.checkSelfPermission(Emojitop.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    String toastText = "Saved emoji";
                    Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_LONG).show();
                    File file = FileUtil.saveEmojiBIG(context, this.layoutEmojiCreation, context.getFilesDir() + "/stickers/");
                    actualizovotos();
                   //  downloadFile(finalEmojiURL);
                } else {
                    ActivityCompat.requestPermissions(Emojitop.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
            }

        });

        updatevotoslistener = new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response, HashMap<String, Object> responseHeaders) {
                try {

                } catch (Exception ignored) {
                }
            }

            @Override
            public void onErrorResponse(String tag, String message) {

            }
        };


        requestSupportedEmojisListener = new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response, HashMap<String, Object> responseHeaders) {
                try {
                    sharedPref.edit().putString("supportedEmojisList", response).apply();
                    addDataToSliders(response);
                } catch (Exception ignored) {
                }
            }

            @Override
            public void onErrorResponse(String tag, String message) {

            }
        };
    }

    private void actualizovotos(){


        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, APITOP+"emoji1="+idemote1+"&"+"emoji2="+idemote2,
                (String) null, new Response.Listener<JSONObject>() {
            //  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(JSONObject response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              }
        });
          queue.add(request);

    }

    private void LOGIC_BACKEND() {

        emojisSlider1LayoutManager = new CenterZoomLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        setSnapHelper(emojisSlider1, emojisSlider1SnapHelper, emojisSlider1LayoutManager);

        emojisSlider1.setLayoutManager(emojisSlider1LayoutManager);

        emojisSlider1.addItemDecoration(new offsetItemDecoration(this));


        requestSupportedEmojis.startRequestNetwork(RequestNetworkController.GET, api_emojis, "", requestSupportedEmojisListener);


//        if (sharedPref.getString("supportedEmojisList", "").isEmpty()) {
//
//            requestSupportedEmojis.startRequestNetwork(RequestNetworkController.GET, "http://emojixer.emojinew.com/panel/api.php?todos=1", "", requestSupportedEmojisListener);
//
//        } else {
//            addDataToSliders(sharedPref.getString("supportedEmojisList", ""));
//        }
    }


    private void addDataToSliders(String data) {
        isFineToUseListeners = false;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            supportedEmojisList = new Gson().fromJson(data, new TypeToken<ArrayList<HashMap<String, Object>>>() {
            }.getType());
            handler.post(() -> {
                emojisSlider1.setAdapter(new EmojimixerAdapter(supportedEmojisList, emojisSlider1LayoutManager, Emojitop.this));
//                idemoji1 = Objects.requireNonNull(supportedEmojisList.get(1).get("idemoji1").toString());
//                idemoji2 = Objects.requireNonNull(supportedEmojisList.get(1).get("idemoji2").toString());


                new Handler().postDelayed(() -> {
                    for (int i = 0; i < 2; i++) {
                        Random rand = new Random();
                        int randomNum = rand.nextInt((supportedEmojisList.size()) - 1);
                        if (i == 0) {

                                    int centerOfScreen = emojisSlider1.getWidth() / 2 ;
                                    int ancho = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics());

                                    emojisSlider1LayoutManager.scrollToPositionWithOffset(idemoji1, centerOfScreen+ancho);

                        } else {


                        }
                    }

                 //   shouldShowEmoji(false);

//                    emote1 = Objects.requireNonNull(supportedEmojisList.get(getRecyclerCurrentItem(emojisSlider1, emojisSlider1SnapHelper, emojisSlider1LayoutManager)).get("emoji_formado")).toString();
//                    emote2 = Objects.requireNonNull(supportedEmojisList.get(getRecyclerCurrentItem(emojisSlider2, emojisSlider2SnapHelper, emojisSlider2LayoutManager)).get("emoji_formado")).toString();
//
//                    idemote1 = Objects.requireNonNull(supportedEmojisList.get(getRecyclerCurrentItem(emojisSlider1, emojisSlider1SnapHelper, emojisSlider1LayoutManager)).get("Id")).toString();
//                    idemote2 = Objects.requireNonNull(supportedEmojisList.get(getRecyclerCurrentItem(emojisSlider2, emojisSlider2SnapHelper, emojisSlider2LayoutManager)).get("Id")).toString();
//                    Log.e("TAG", "addDataToSliders: "+idemote1+" dos"+idemote2 );
                //    mixEmojis(emote1, emote2, Objects.requireNonNull(supportedEmojisList.get(getRecyclerCurrentItem(emojisSlider1, emojisSlider1SnapHelper, emojisSlider1LayoutManager)).get("Id")).toString());

                    registerViewPagersListener();

                    isFineToUseListeners = true;
                }, 1000);
            });
        });


    }

    private void registerViewPagersListener() {
        Log.e("TAG", "addDataToSliders id entro: "+idemoji1+idemoji2 );

        emojisSlider1.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {


                if (isFineToUseListeners && newState == SCROLL_STATE_IDLE) {
                    emote1 = Objects.requireNonNull(supportedEmojisList.get(getRecyclerCurrentItem(emojisSlider1, emojisSlider1SnapHelper, emojisSlider1LayoutManager)).get("emoji_formado")).toString();
                    idemote1 = Objects.requireNonNull(supportedEmojisList.get(getRecyclerCurrentItem(emojisSlider1, emojisSlider1SnapHelper, emojisSlider1LayoutManager)).get("id_emoji1")).toString();
                    idemote2 = Objects.requireNonNull(supportedEmojisList.get(getRecyclerCurrentItem(emojisSlider1, emojisSlider1SnapHelper, emojisSlider1LayoutManager)).get("id_emoji2")).toString();

                    shouldShowEmoji(false);
                    mixEmojis(emote1, emote2, Objects.requireNonNull(supportedEmojisList.get(getRecyclerCurrentItem(emojisSlider1, emojisSlider1SnapHelper, emojisSlider1LayoutManager)).get("Id")).toString());
                    isFineToUseListeners = false;
                    Log.e("TAG", "addDataToSliders1: "+idemote1+" dos"+idemote2 );

                }
            }
        });


    }



        private void mixEmojis (String emoji1, String emoji2, String date){

            shouldEnableSave(false);
            // progressBar.setVisibility(View.GONE);
            Log.e("TAG", "addDataToSliders: " + emoji1 + " dos" + idemote2);
            if (TextUtils.isEmpty(idemote1)) {
                idemote1 = "12";
                Log.e("TAG", "addDataToSliders entrr: " + emoji1 + " dos" + idemote2);


            }
            if (TextUtils.isEmpty(idemote2)) {
                idemote2 = "26";
                Log.e("TAG", "addDataToSliders entrr: " + emoji1 + " dos" + idemote2);


            }
            EmojiMixer em = new EmojiMixer(emoji1, emoji2, idemote2, idemote1, date, this, new EmojiMixer.EmojiListener() {

                @Override
                public void onSuccess(String emojiUrl, String ojos, String cejas, String objetos, String bocas, String finalojos_objetos, String manos, int ancho, int left, int top, String tipo, String extra, String fondo, float rotacion, int random,String animacion) {

                    shouldEnableSave(true);


                    if (Objects.equals(tipo, "objeto")) {

                        if (emoji1 == emoji2) {
                            mixedemojiforma.setVisibility(View.VISIBLE);

                            mixedemojiforma.setImageURI(Uri.parse(extra));
                            mixedfondo.setVisibility(View.VISIBLE);

                            mixedfondo.setImageURI(Uri.parse(fondo));
                            Log.e("TAG", "objeto igual: " + extra);
                        } else {

                            int ancho2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ancho, getResources().getDisplayMetrics());
                            int left2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, left, getResources().getDisplayMetrics());
                            int top2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, top, getResources().getDisplayMetrics());
                            int ancho3 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, getResources().getDisplayMetrics());


                            posicionemoji.setLayoutParams(new FrameLayout.LayoutParams(ancho2, ancho2));
                            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) posicionemoji.getLayoutParams();
                            params.setMargins(left2, top2, 0, 0);
                            posicionemoji.setLayoutParams(params);
                            posicionemoji.setRotation(rotacion);
                            Log.e("TAG", "aki rotacion objeto: " + rotacion);

                            mixedemojiforma.setVisibility(View.VISIBLE);
                            posicioncara.setRotation(0);

                            mixedemojiforma.setImageURI(Uri.parse(extra));
                            mixedfondo.setVisibility(View.VISIBLE);

                            mixedfondo.setImageURI(Uri.parse(fondo));

                            mixedemojiforma.setLayoutParams(new FrameLayout.LayoutParams(ancho3, ancho3));
                            ViewGroup.MarginLayoutParams params4 = (ViewGroup.MarginLayoutParams) mixedemojiforma.getLayoutParams();
                            params4.setMargins(0, 0, 0, 0);
                            mixedemojiforma.setLayoutParams(params4);
                            mixedemojiforma.setRotation(0);
                        }
                    } else if (Objects.equals(tipo, "objetodoble")) {
                        int ancho2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, getResources().getDisplayMetrics());
                        int left2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics());
                        int top2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, top, getResources().getDisplayMetrics());
                        int ancho3 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, getResources().getDisplayMetrics());


                        posicionemoji.setLayoutParams(new FrameLayout.LayoutParams(ancho2, ancho2));
                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) posicionemoji.getLayoutParams();
                        params.setMargins(left2, top2, 0, 0);
                        posicionemoji.setLayoutParams(params);
                        posicionemoji.setRotation(0);

                        mixedemojiforma.setVisibility(View.VISIBLE);
                        posicioncara.setRotation(0);

                        mixedemojiforma.setImageURI(Uri.parse(extra));
                        mixedfondo.setVisibility(View.VISIBLE);

                        mixedfondo.setImageURI(Uri.parse(fondo));
                        Log.e("TAG", "aki base objeto doble: " + fondo);

                        mixedemojiforma.setLayoutParams(new FrameLayout.LayoutParams(ancho3, ancho3));
                        ViewGroup.MarginLayoutParams params4 = (ViewGroup.MarginLayoutParams) mixedemojiforma.getLayoutParams();
                        params4.setMargins(0, 0, 0, 0);
                        mixedemojiforma.setLayoutParams(params4);
                        mixedemojiforma.setRotation(0);
                    } else if (ancho > 0) {
                        Log.e("TAG", "aki rotacion ANCHO: " + manos);


                        //emojis3 iguales y objeto es emojis3 champi frutass....
                        if (Objects.equals(emote1, emote2) && Objects.equals(tipo, "emojis3")) {

                            posicionemoji.setRotation(0);
                            posicioncara.setRotation(rotacion);
                            mixedemojiforma.setVisibility(View.GONE);
                            int ancho3 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, getResources().getDisplayMetrics());
                            Log.e("TAG", "aki rotacion emojis3: " + ancho);
                            posicionemoji.setLayoutParams(new FrameLayout.LayoutParams(ancho3, ancho3));
                            ViewGroup.MarginLayoutParams params2 = (ViewGroup.MarginLayoutParams) posicionemoji.getLayoutParams();
                            params2.setMargins(0, 0, 0, 0);
                            posicionemoji.setLayoutParams(params2);

                            int ancho2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ancho, getResources().getDisplayMetrics());
                            int left2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, left, getResources().getDisplayMetrics());
                            int top2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, top, getResources().getDisplayMetrics());


                            posicioncara.setLayoutParams(new FrameLayout.LayoutParams(ancho2, ancho2));
                            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) posicioncara.getLayoutParams();
                            params.setMargins(left2, top2, 0, 0);
                            posicioncara.setLayoutParams(params);
                        }


                        //emojis iguales y objeto es emojis
                        else if (Objects.equals(emote1, emote2) && Objects.equals(tipo, "emoji")) {

//                        Careta version
                            if (random == 1) {
                                mixedemojiforma.setVisibility(View.VISIBLE);
                                mixedemojiforma.setImageURI(Uri.parse(extra));
                                //    Log.e("TAG", "aki igual emoji: "+extra);
                                int left2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, left, getResources().getDisplayMetrics());
                                int top2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, top, getResources().getDisplayMetrics());
                                int ancho3 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ancho, getResources().getDisplayMetrics());
                                int ancho4 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ancho - 50, getResources().getDisplayMetrics());

                                mixedemojiforma.setLayoutParams(new FrameLayout.LayoutParams(ancho3, ancho3));
                                ViewGroup.MarginLayoutParams params2 = (ViewGroup.MarginLayoutParams) mixedemojiforma.getLayoutParams();
                                params2.setMargins(0, top2, 0, 0);
                                mixedemojiforma.setLayoutParams(params2);
                                mixedemojiforma.setRotation(0);

                                Log.e("TAG", "aki rotacion careta: " + rotacion);
                                posicionemoji.setLayoutParams(new FrameLayout.LayoutParams(ancho4, ancho4));
                                ViewGroup.MarginLayoutParams params3 = (ViewGroup.MarginLayoutParams) posicionemoji.getLayoutParams();
                                params3.setMargins(left2, top2, 0, 0);
                                posicionemoji.setLayoutParams(params3);

                                posicioncara.setLayoutParams(new FrameLayout.LayoutParams(ancho4, ancho4));
                                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) posicioncara.getLayoutParams();
                                params.setMargins(0, 0, 0, 0);
                                posicioncara.setLayoutParams(params);
                                posicionemoji.setRotation(rotacion);
                                posicioncara.setRotation(0);
                            }
                            //version doble emoji
                            else {

                                mixedemojiforma.setVisibility(View.VISIBLE);
                                mixedemojiforma.setImageURI(Uri.parse(extra));
                                int left2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, left, getResources().getDisplayMetrics());
                                int top2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, top, getResources().getDisplayMetrics());
                                int ancho3 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ancho, getResources().getDisplayMetrics());

                                mixedemojiforma.setLayoutParams(new FrameLayout.LayoutParams(ancho3 - 100, ancho3 - 100));
                                ViewGroup.MarginLayoutParams params2 = (ViewGroup.MarginLayoutParams) mixedemojiforma.getLayoutParams();
                                params2.setMargins(ancho3 - 75, top2 + top2, 0, 0);
                                mixedemojiforma.setLayoutParams(params2);
                                mixedemojiforma.setRotation(-rotacion);

                                Log.e("TAG", "aki rotacion emoji doble: " + rotacion);
                                posicionemoji.setLayoutParams(new FrameLayout.LayoutParams(ancho3, ancho3));
                                ViewGroup.MarginLayoutParams params3 = (ViewGroup.MarginLayoutParams) posicionemoji.getLayoutParams();
                                params3.setMargins(left2, top2, 0, 0);
                                posicionemoji.setLayoutParams(params3);

                                posicioncara.setLayoutParams(new FrameLayout.LayoutParams(ancho3, ancho3));
                                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) posicioncara.getLayoutParams();
                                params.setMargins(0, 0, 0, 0);
                                posicioncara.setLayoutParams(params);
                                posicionemoji.setRotation(rotacion);
                                posicioncara.setRotation(0);
                            }
                        } else {

                            Log.e("TAG", "version normal " + Uri.parse(manos));

                            posicionemoji.setRotation(0);
                            posicioncara.setRotation(rotacion);
                            mixedemojiforma.setVisibility(View.GONE);
                            int ancho3 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, getResources().getDisplayMetrics());
                            posicionemoji.setLayoutParams(new FrameLayout.LayoutParams(ancho3, ancho3));
                            ViewGroup.MarginLayoutParams params2 = (ViewGroup.MarginLayoutParams) posicionemoji.getLayoutParams();
                            params2.setMargins(0, 0, 0, 0);
                            posicionemoji.setLayoutParams(params2);

                            int ancho2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ancho, getResources().getDisplayMetrics());
                            int left2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, left, getResources().getDisplayMetrics());
                            int top2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, top, getResources().getDisplayMetrics());


                            posicioncara.setLayoutParams(new FrameLayout.LayoutParams(ancho2, ancho2));
                            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) posicioncara.getLayoutParams();
                            params.setMargins(left2, top2, 0, 0);
                            posicioncara.setLayoutParams(params);
                        }
                        mixedfondo.setVisibility(View.GONE);
                    } else {

                        int ancho2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, getResources().getDisplayMetrics());
                        posicionemoji.setRotation(0);
                        posicioncara.setRotation(0);


                        posicionemoji.setLayoutParams(new FrameLayout.LayoutParams(ancho2, ancho2));
                        ViewGroup.MarginLayoutParams params2 = (ViewGroup.MarginLayoutParams) posicionemoji.getLayoutParams();
                        params2.setMargins(0, 0, 0, 0);
                        posicionemoji.setLayoutParams(params2);

                        posicioncara.setLayoutParams(new FrameLayout.LayoutParams(ancho2, ancho2));
                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) posicioncara.getLayoutParams();
                        params.setMargins(0, 0, 0, 0);
                        posicioncara.setLayoutParams(params);
                        mixedfondo.setVisibility(View.GONE);
                        mixedemojiforma.setVisibility(View.GONE);


                    }

                    finalEmojiURL = emojiUrl;
                    ojosfinal = ojos;



                    finalEmojiURL = emojiUrl;
                    ojosfinal = ojos;

                    String extension = "";

                    int lastIndexOfDot = finalEmojiURL.lastIndexOf('.');
                    if (lastIndexOfDot != -1) {
                        extension = finalEmojiURL.substring(lastIndexOfDot + 1);
                    }
                    Log.e("TAG", "onSuccess: aki es png "+extension+finalEmojiURL );

                    if ("png".equals(extension)) {
                        mixedEmoji.setVisibility(View.GONE);
                        mixedEmoji0.setVisibility(View.VISIBLE);

                        mixedEmoji0.setImageURI(Uri.parse(finalEmojiURL));
                        Log.e("TAG", "onSuccess: aki es png " );
                    }
                    else {
                        Log.e("TAG", "onSuccess: aki es png no " );

                        LottieCompositionFactory.fromUrl(context, String.valueOf(Uri.parse(finalEmojiURL)))
                                .addListener(composition -> {
                                    mixedEmoji0.setVisibility(View.GONE);
                                    mixedEmoji.setVisibility(View.VISIBLE);
                                    mixedEmoji.setComposition(composition);
                                    mixedEmoji.playAnimation();
                                });
                    }


                    mixedEmojicejas.setImageURI(Uri.parse(cejas));
//                mixedEmojiobjetos.setImageURI(Uri.parse(objetos));
                    if (objetos != null) {
                        LottieCompositionFactory.fromUrl(context, String.valueOf(Uri.parse(objetos)))
                                .addListener(composition -> {
                                    mixedEmojiobjetos.setComposition(composition);
                                    mixedEmojiobjetos.playAnimation();
                                })
                                .addFailureListener(exception -> {
                                    // Si falla cargar desde la URL, carga el archivo local

                                    mixedEmojiobjetos.setAnimation(R.raw.vacio);
                                    mixedEmojiobjetos.playAnimation();
                                    Log.e("LottieError", "Error al cargar animación desde URL. Usando animación local.", exception);
                                });
                    }


                    if (bocas != null) {
                        LottieCompositionFactory.fromUrl(context, String.valueOf(Uri.parse(bocas)))
                                .addListener(composition -> {
                                    mixedEmojibocas.setComposition(composition);
                                    mixedEmojibocas.playAnimation();
                                })
                                .addFailureListener(exception -> {
                                    // Si falla cargar desde la URL, carga el archivo local

                                    mixedEmojibocas.setAnimation(R.raw.vacio);
                                    mixedEmojibocas.playAnimation();
                                    Log.e("LottieError", "Error al cargar animación desde URL. Usando animación local.", exception);
                                });
                    }

                    if (ojosfinal != null) {
                        LottieCompositionFactory.fromUrl(context, String.valueOf(Uri.parse(ojosfinal)))
                                .addListener(composition -> {
                                    mixedEmojiojos.setComposition(composition);
                                    mixedEmojiojos.playAnimation();
                                }).addFailureListener(exception -> {
                                    // Si falla cargar desde la URL, carga el archivo local

                                    mixedEmojibocas.setAnimation(R.raw.vacio);
                                    mixedEmojibocas.playAnimation();
                                    Log.e("LottieError", "Error al cargar animación desde URL. Usando animación local.", exception);
                                });
                    }

                    Log.e("TAG", "aki emoji final1 ojos_objeto: "+finalojos_objetos+" boca"+bocas+" ojos"+ojosfinal  );

                    if (finalojos_objetos != null) {
                        Log.e("TAG", "aki emoji final2: "+String.valueOf(Uri.parse(finalojos_objetos)) );

                        LottieCompositionFactory.fromUrl(context, String.valueOf(Uri.parse(finalojos_objetos)))
                                .addListener(composition -> {
                                    mixedEmojiojos_objetos.setComposition(composition);
                                    mixedEmojiojos_objetos.playAnimation();
                                })
                                .addFailureListener(exception -> {
                                    // Si falla cargar desde la URL, carga el archivo local

                                    mixedEmojiojos_objetos.setAnimation(R.raw.vacio);
                                    mixedEmojiojos_objetos.playAnimation();
                                    Log.e("LottieError", "Error al cargar animación desde URL. Usando animación local.", exception);
                                });
                    }



                    if (Objects.equals(idemote1, "26") || Objects.equals(idemote2, "26")) {
                        mixedEmojimanos2.setVisibility(View.VISIBLE);
                        mixedEmojimanos2.setImageURI(Uri.parse(manos));
                        mixedEmojimanos.setVisibility(View.GONE);


                        LottieCompositionFactory.fromUrl(context, String.valueOf(Uri.parse(manos)))
                                .addListener(composition -> {
                                    mixedEmojimanos2.setComposition(composition);
                                    mixedEmojimanos2.playAnimation();
                                }).addFailureListener(exception -> {
                                    // Si falla cargar desde la URL, carga el archivo local

                                    mixedEmojimanos2.setAnimation(R.raw.vacio);
                                    mixedEmojimanos2.playAnimation();
                                });

                    }
                    else {
                        mixedEmojimanos.setVisibility(View.VISIBLE);
                        //   mixedEmojimanos.setImageURI(Uri.parse(manos));
                        mixedEmojimanos2.setVisibility(View.GONE);

                        Log.e("LottieError", "aki manos."+manos);

                        LottieCompositionFactory.fromUrl(context, String.valueOf(Uri.parse(manos)))
                                .addListener(composition -> {
                                    mixedEmojimanos.setComposition(composition);
                                    mixedEmojimanos.playAnimation();
                                }).addFailureListener(exception -> {
                                    // Si falla cargar desde la URL, carga el archivo local

                                    mixedEmojimanos.setAnimation(R.raw.vacio);
                                    mixedEmojimanos.playAnimation();
                                });
                    }


                    isFineToUseListeners = true;
                    shouldShowEmoji(true);
                }


                @Override
                public void onFailure(String failureReason) {
                    //  changeActivityDesc(failureReason);
                    shouldEnableSave(false);
                    //  mixedEmoji.setImageResource(R.drawable.sad);
                    //  mixedEmoji.setImageURI(Uri.parse(""));

                    //  shouldShowEmoji(true);
                }
            });
            Thread thread = new Thread(em);
            thread.start();
        }



    private void shouldShowEmoji(boolean shouldShow) {
        Log.e("TAG", "onSuccess: "+shouldShow );

        if (shouldShow) {
          //  Log.e("TAG", "aki entro:+finalEmojiURL " );

            shadAnim(layoutEmojiCreation, "scaleY", 1, 400);
            shadAnim(layoutEmojiCreation, "scaleX", 1, 400);
//            shadAnim(layoutEmojiCreation, "rotation", 360f, 400);
            shadAnim(layoutEmojiCreation, "translationY", 0, 400);

//           shadAnim(progressBar, "scaleY", 0, 300);
//           shadAnim(progressBar, "scaleX", 0, 300);
        } else {
        //    Log.e("TAG", "aki entro2:+finalEmojiURL " );
            mas.playAnimation();
         //   explosion.setImageURI("http://emojixer.emojinew.com/panel/explosion.webp");
            shadAnim(layoutEmojiCreation, "scaleY", 0.2, 400);
            shadAnim(layoutEmojiCreation, "scaleX", 0.2, 400);
//            shadAnim(layoutEmojiCreation, "rotation", 0f, 400);
            int anc = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, getResources().getDisplayMetrics());

            shadAnim(layoutEmojiCreation, "translationY", anc, 400);


//           shadAnim(progressBar, "scaleY", 1, 300);
//            shadAnim(progressBar, "scaleX", 1, 300);
        }
    }

    private void shouldEnableSave(boolean shouldShow) {

        if (shouldShow) {
            new Handler().postDelayed(() -> {
                saveEmoji.setEnabled(true);
                //colorAnimator(saveEmoji, "#2A2B28", "#FF9D05", 250);
                //saveEmoji.setTextColor(Color.parseColor("#422B0D"));
                //saveEmoji.setIconTint(ColorStateList.valueOf(Color.parseColor("#422B0D")));
            }, 1000);
        } else {
            saveEmoji.setEnabled(false);
            //colorAnimator(saveEmoji, "#FF9D05", "#2A2B28", 250);
            //saveEmoji.setTextColor(Color.parseColor("#A3A3A3"));
            //saveEmoji.setIconTint(ColorStateList.valueOf(Color.parseColor("#A3A3A3")));
        }
    }

    private void setImageFromUrl(ImageView image, String url) {
        Glide.with(this)
                .load(url)
                .fitCenter()
                .transition(DrawableTransitionOptions.withCrossFade())
                .listener(
                        new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                shouldShowEmoji(true);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                shouldShowEmoji(true);
                                return false;
                            }
                        }
                )
                .into(image);
    }




    private void downloadFile(String url) {
        Toast.makeText(this, "Download started, check notifications bar.", Toast.LENGTH_SHORT).show();
        String fileName = "MixedEmoji_" + new SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.US).format(new Date());
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                DownloadManager downloadmanager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setMimeType(getContentResolver().getType(Uri.parse(url)));
                request.addRequestHeader("cookie", CookieManager.getInstance().getCookie(url));
                request.setTitle(fileName);
                request.setDescription(getString(R.string.downloading));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/MixedEmojis/" + fileName + URLUtil.guessFileName(url, "", ""));
                downloadmanager.enqueue(request);
            } catch (Exception e) {
                Log.e("Download error", e.toString());
            }
        });
    }

    public void abriremojis(View view) {
       // MiPublicidad.verInterstitialAd(this);

        Emojitop.this.startActivity(new Intent(Emojitop.this.getApplicationContext(), ImageViewerActivity.class));

    }
}
