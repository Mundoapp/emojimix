package com.emojixer.activities;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;
import static com.emojixer.activities.Activityestaticos.api_dominio;
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
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
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
import com.emojixer.adapters.EmojiTopAdapterestaticos;
import com.emojixer.functions.FileUtil;
import com.emojixer.functions.Nemojismodel;
import com.emojixer.functions.RequestNetwork;
import com.emojixer.functions.RequestNetworkController;
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

public class Emojitop_estaticos extends AppCompatActivity {
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
    public static String api_emojis = api_dominio+"/panel/api.php?top=1";
    public static String APITOP = api_dominio+"/panel/apitop.php?";

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
                emojisSlider1.setAdapter(new EmojiTopAdapterestaticos(supportedEmojisList, emojisSlider1LayoutManager, Emojitop_estaticos.this));
//                idemoji1 = Objects.requireNonNull(supportedEmojisList.get(1).get("idemoji1").toString());
//                idemoji2 = Objects.requireNonNull(supportedEmojisList.get(1).get("idemoji2").toString());


                new Handler().postDelayed(() -> {
                    for (int i = 0; i < 2; i++) {
                        Random rand = new Random();
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

                    shouldShowEmoji(false);
                     isFineToUseListeners = false;
                    Log.e("TAG", "addDataToSliders1: "+idemote1+" dos"+idemote2 );

                }
            }
        });


    }






    private void shouldShowEmoji(boolean shouldShow) {
        Log.e("TAG", "onSuccess: "+shouldShow );

        if (shouldShow) {
          //  Log.e("TAG", "aki entro:+finalEmojiURL " );

  //          shadAnim(layoutEmojiCreation, "scaleY", 1, 400);
   //         shadAnim(layoutEmojiCreation, "scaleX", 1, 400);
//            shadAnim(layoutEmojiCreation, "rotation", 360f, 400);
   //         shadAnim(layoutEmojiCreation, "translationY", 0, 400);

//           shadAnim(progressBar, "scaleY", 0, 300);
//           shadAnim(progressBar, "scaleX", 0, 300);
        } else {
        //    Log.e("TAG", "aki entro2:+finalEmojiURL " );
         //   explosion.setImageURI("http://emojixer.emojinew.com/panel/explosion.webp");
   //         shadAnim(layoutEmojiCreation, "scaleY", 0.2, 400);
     //       shadAnim(layoutEmojiCreation, "scaleX", 0.2, 400);
//            shadAnim(layoutEmojiCreation, "rotation", 0f, 400);

    //        shadAnim(layoutEmojiCreation, "translationY", anc, 400);


//           shadAnim(progressBar, "scaleY", 1, 300);
//            shadAnim(progressBar, "scaleX", 1, 300);
        }
    }

    private void shouldEnableSave(boolean shouldShow) {

        if (shouldShow) {
            new Handler().postDelayed(() -> {
                //colorAnimator(saveEmoji, "#2A2B28", "#FF9D05", 250);
                //saveEmoji.setTextColor(Color.parseColor("#422B0D"));
                //saveEmoji.setIconTint(ColorStateList.valueOf(Color.parseColor("#422B0D")));
            }, 1000);
        } else {
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

        Emojitop_estaticos.this.startActivity(new Intent(Emojitop_estaticos.this.getApplicationContext(), ImageViewerActivity.class));

    }
}
