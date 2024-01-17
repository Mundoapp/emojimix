package com.emojitwomix.activities;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;
import static com.emojitwomix.functions.Utils.getRecyclerCurrentItem;
import static com.emojitwomix.functions.Utils.setSnapHelper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.airbnb.lottie.LottieAnimationView;
import com.emojitwomix.R;
import com.emojitwomix.adapters.EmojiTopAdapter;
import com.emojitwomix.functions.Nemojismodel;
import com.emojitwomix.functions.RequestNetwork;
import com.emojitwomix.functions.RequestNetworkController;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Emojitop extends AppCompatActivity {
    private LottieAnimationView saveEmoji;
    private WrapContentDraweeView mixedfondo,mixedEmojicejas,explosion,mixedEmoji0;
    private LottieAnimationView progressBar;
    LottieAnimationView btnhome,mixedEmojiojos_objetos,mixedEmoji,mixedEmojiojos,mixedEmojibocas,mixedEmojiobjetos,mixedEmojimanos2,mixedEmojimanos,mixedemojiforma;

    private TextView activityDesc;
    private Button emojitop,btnmixestaticos,btnmixanimados;
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


        //  anuncios.baner( (FrameLayout) findViewById(R.id.ad_view_container), this);


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
        btnmixanimados = findViewById(R.id.mixanimados);
        btnmixestaticos = findViewById(R.id.mixestaticos);
        btnhome = findViewById(R.id.home);
        btnhome.playAnimation();
        btnmixanimados.setOnClickListener(view -> {
            this.startActivity(new Intent(this.getApplicationContext(), MainActivity.class));
        });
        btnmixestaticos.setOnClickListener(view -> {
            this.startActivity(new Intent(this.getApplicationContext(), ImageViewerActivity.class));
        });
        btnhome.setOnClickListener(view -> {
            this.startActivity(new Intent(this.getApplicationContext(), Inicio.class));
        });


        mixedemojiforma = findViewById(R.id.emojiforma);
        mixedfondo= findViewById(R.id.emojifondo);
        mas = findViewById(R.id.iconmas);
        explosion = findViewById(R.id.explosion);

         emojisSlider1 = findViewById(R.id.emojisSlider1);
        requestSupportedEmojis = new RequestNetwork(this);
        sharedPref = getSharedPreferences("AppData", Activity.MODE_PRIVATE);
        layoutEmojiCreation =  findViewById(R.id.frame_emoji_creation);
        posicioncara =  findViewById(R.id.posicioncara);
        posicionem = findViewById(R.id.posicione);


        posicionemoji = findViewById(R.id.posicionemoji);





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

    private void LOGIC_BACKEND() {

        emojisSlider1LayoutManager = new LinearLayoutManager(this);
        emojisSlider1LayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // Opcional, para una lista vertical

        emojisSlider1.setLayoutManager(emojisSlider1LayoutManager);
        setSnapHelper(emojisSlider1, emojisSlider1SnapHelper, emojisSlider1LayoutManager);

        emojisSlider1.setLayoutManager(emojisSlider1LayoutManager);

       // emojisSlider1.addItemDecoration(new offsetItemDecoration(this));


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
                emojisSlider1.setAdapter(new EmojiTopAdapter(supportedEmojisList, emojisSlider1LayoutManager, Emojitop.this));
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

                     isFineToUseListeners = false;
                    Log.e("TAG", "addDataToSliders1: "+idemote1+" dos"+idemote2 );

                }
            }
        });


    }





    public void abriremojis(View view) {
       // anuncios.verInterstitialAd(this);

        Emojitop.this.startActivity(new Intent(Emojitop.this.getApplicationContext(), ImageViewerActivity.class));

    }
}
