package com.emojixer.activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;
import static com.emojixer.functions.UIMethods.shadAnimestaticos;
import static com.emojixer.functions.Utils.getRecyclerCurrentItem;
import static com.emojixer.functions.Utils.setSnapHelper;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.view.LayoutInflater;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.emojixer.adapters.Adapterestaticos;
import com.emojixer.functions.CenterZoomLayoutManager;
import com.emojixer.functions.EmojiMixerestaticos;
import com.emojixer.functions.FileUtil;
import com.emojixer.functions.Nemojismodel;
import com.emojixer.functions.RequestNetwork;
import com.emojixer.functions.RequestNetworkController;
import com.emojixer.functions.offsetItemDecoration;
import com.emojixer.mipublicidad.MiPublicidad;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Activityestaticos extends AppCompatActivity {
     private WrapContentDraweeView mixedemojiforma,mixedfondo,mixedEmoji,mixedEmojiojos,mixedEmojiojos_objetos,mixedEmojicejas,mixedEmojibocas,mixedEmojiobjetos,mixedEmojimanos,mixedEmojimanos2,explosion;
    private LottieAnimationView saveEmoji, progressBar;
    private TextView activityDesc;
    private Button emojitop;
    private String emote1;
    private String emote2;
    private String idemote1;
    private int randomfinal;
    private int idemoji1 = 16;
    private int idemoji2 = 11;
    private LottieAnimationView mas;
    private String ojosfinal,cejasfinal, bocafinal, ojosobjetosfinal, objetosfinal, manosfinal, anchofinal, leftfinal, topfinal, tipofinal, extrafinal, fondofinal, rotacionfinal, animacionfinal;

    private String idemote2;
    private String finalEmojiURL;
    private RecyclerView dialogemojisSlider1, dialogemojisSlider2;

    private RecyclerView emojisSlider1;
    private RecyclerView emojisSlider2;
    private ArrayList<HashMap<String, Object>> supportedEmojisList = new ArrayList<>();
    private RequestNetwork requestSupportedEmojis;
    private RequestNetwork updatevotos;

    private RequestNetwork.RequestListener  updatevotoslistener;


    private RequestNetwork.RequestListener requestSupportedEmojisListener;
    private SharedPreferences sharedPref;
    private boolean isFineToUseListeners = false;
    private LinearLayoutManager emojisSlider1LayoutManager;
    private LinearLayoutManager emojisSlider2LayoutManager;
    private SnapHelper emojisSlider1SnapHelper = new LinearSnapHelper();
    private SnapHelper emojisSlider2SnapHelper = new LinearSnapHelper();
    FrameLayout layoutEmojiCreation;
    FrameLayout posicioncara;
    FrameLayout posicionemoji;
    FrameLayout posicionem;
    String dateTimeKey;
    Activity context;
    long days;
    ReviewInfo reviewInfo;
    Nemojismodel totalmodel;
    public static String api_dominio = "https://emojixer.com/";
    public static String api_emojis = api_dominio+"/panel/api.php?todos=1";
    public static String APITOP = api_dominio+"/panel/apitop.php?";
    private AdView adView;
    private int posicionanterior = -1;
    private int posicionanterior2 = -1;
    private AlertDialog alertDialogemojis;
    private AlertDialog alertDialogemojis2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        AudienceNetworkAds.initialize(this);

        AppRater.app_launched(this);


        setContentView(R.layout.activity_estaticos);
        initLogic();
        dateTimeKey = "com.example.app.datetime";

// use a default value using new Date()
        days = sharedPref.getLong(dateTimeKey, Activity.MODE_PRIVATE);

        if(days <=0){
        Date dateStr = Calendar.getInstance().getTime();
        sharedPref.edit().putLong(dateTimeKey, dateStr.getTime()).apply();
        }




//        getemojis();

        context = this;

        LOGIC_BACKEND();

     MiPublicidad.baner( (FrameLayout) findViewById(R.id.ad_view_container), this);

//        OneSignal.initWithContext(this);
//        OneSignal.setAppId("399b14d7-4c2a-4ce3-ab00-ce6358a44afa");

    }


private void initreview(){
    ReviewManager manager = ReviewManagerFactory.create(this);
    Task<ReviewInfo> request = manager.requestReviewFlow();
    request.addOnCompleteListener(task -> {
        if (task.isSuccessful()) {
            // We can get the ReviewInfo object
            reviewInfo = task.getResult();
        } else {
            // There was some problem, log or handle the error code.
        }
    });

}

    private void configurarDialogoEmojis() {

        // Configura AlertDialog y RecyclerItemClickListener aquí
        LottieAnimationView buscar1 = findViewById(R.id.icbuscar1);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view2 = LayoutInflater.from(this).inflate(R.layout.dialog_emojis1, null);
        dialogemojisSlider1 = view2.findViewById(R.id.dialogemojisSlider1);
        buscar1.playAnimation();
        dialogemojisSlider1.addOnItemTouchListener(new RecyclerItemClickListener(this, dialogemojisSlider1, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (position < 2) {
                    emojisSlider1.smoothScrollToPosition(position);
                } else if (posicionanterior > position) {
                    emojisSlider1.smoothScrollToPosition(position - 2);
                } else {
                    emojisSlider1.smoothScrollToPosition(position + 2);
                }

                posicionanterior = position;

                alertDialogemojis.dismiss();
            }
        }));


        builder.setView(view2);

        alertDialogemojis = builder.create();

        buscar1.setOnClickListener(view -> {
            alertDialogemojis.show();


        });


        LottieAnimationView buscar2 = findViewById(R.id.icbuscar2);
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        View view3 = LayoutInflater.from(this).inflate(R.layout.dialog_emojis1, null);
        dialogemojisSlider2 = view3.findViewById(R.id.dialogemojisSlider2);
        buscar2.playAnimation();

        dialogemojisSlider2.addOnItemTouchListener(new RecyclerItemClickListener(this, dialogemojisSlider2, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Aquí obtienes la posición del emoji seleccionado
                // Puedes usar la posición o el emoji seleccionado según tus necesidades
                // Por ejemplo, puedes mostrar un mensaje con el emoji o almacenar la posición en una variable
                registerViewPagersListener();
                if (position < 2) {
                    emojisSlider2.smoothScrollToPosition(position);
                } else if (posicionanterior2 > position) {
                    emojisSlider2.smoothScrollToPosition(position - 2);
                } else {
                    emojisSlider2.smoothScrollToPosition(position + 2);
                }
                posicionanterior2 = position;
                alertDialogemojis2.dismiss();
            }
        }));
        builder2.setView(view3);
        alertDialogemojis2 = builder2.create();
        buscar2.setOnClickListener(view -> {
            alertDialogemojis2.show();
        });
        Log.e(TAG, "configurar altert: entro");

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
        emojisSlider2 = findViewById(R.id.emojisSlider2);
        requestSupportedEmojis = new RequestNetwork(this);
        sharedPref = getSharedPreferences("AppData", Activity.MODE_PRIVATE);
        layoutEmojiCreation =  findViewById(R.id.frame_emoji_creation);
        posicioncara =  findViewById(R.id.posicioncara);
        posicionem = findViewById(R.id.posicione);

        configurarDialogoEmojis();


        emojitop = findViewById(R.id.emojitop);
        emojitop.setOnClickListener(view -> {
          //  MiPublicidad.verInterstitialAd(this);
            Activityestaticos.this.startActivity(new Intent(Activityestaticos.this.getApplicationContext(), Emojitop_estaticos.class));


        });

        posicionemoji = findViewById(R.id.posicionemoji);
//        mas.setOnClickListener(view -> {
//            Log.e("TAG", "initLogic: aki entro "+emote1+emote2 );
//            mixEmojis(emote2, emote1, Objects.requireNonNull(supportedEmojisList.get(getRecyclerCurrentItem(emojisSlider2, emojisSlider2SnapHelper, emojisSlider2LayoutManager)).get("Id")).toString());
//            mas.playAnimation();
//
//        });


//        mixedEmoji.setOnClickListener(view -> {
//            isFineToUseListeners = false;
//            int random1 = 0;
//            int random2 = 0;
//            for (int i = 0; i < 2; i++) {
//                Random rand = new Random();
//
//                int randomNum = rand.nextInt((supportedEmojisList.size()) - 1);
//                if (i == 0) {
//                    random1 = randomNum;
//                    if(randomNum<=0)
//                        randomNum=26;
//                    emojisSlider1.smoothScrollToPosition(randomNum);
//                } else {
//                  random2 = randomNum;
//                    if(randomNum<=0)
//                        randomNum=12;
//                    emojisSlider2.smoothScrollToPosition(randomNum);
//                }
//            }
//
////
//
//            emote1 = Objects.requireNonNull(supportedEmojisList.get(getRecyclerCurrentItem(emojisSlider1, emojisSlider1SnapHelper, emojisSlider1LayoutManager)).get("emoji_formado")).toString();
//            emote2 = Objects.requireNonNull(supportedEmojisList.get(getRecyclerCurrentItem(emojisSlider2, emojisSlider2SnapHelper, emojisSlider2LayoutManager)).get("emoji_formado")).toString();
//            mixEmojis(emote1, emote2, Objects.requireNonNull(supportedEmojisList.get(getRecyclerCurrentItem(emojisSlider2, emojisSlider2SnapHelper, emojisSlider2LayoutManager)).get("Id")).toString());
//
//        });

        saveEmoji.setOnClickListener(view -> {

         //   MiPublicidad.verInterstitialAd(this);
            Date today = new Date();
            long diff =  today.getTime() - days;
            int numOfDays = 2;
//            int hours = (int) (diff / (1000 * 60 * 60));
//            int minutes = (int) (diff / (1000 * 60));
            Log.e("TAG", "aki entro dias: "+numOfDays );

            if(numOfDays>=1 && numOfDays<5){
                Log.e("TAG", "aki entro review: " );
                initreview();

            }


          //  Log.e("TAG", "aki min dias: "+days+" actual "+ minutes );


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

                File file = FileUtil.saveEmojiBIG(context, this.layoutEmojiCreation, context.getFilesDir() + "/stickers/");
                String toastText = "Saved emoji";
                Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_LONG).show();
                actualizovotos();
             //   downloadFile(finalEmojiURL);
            } else {
                if (ContextCompat.checkSelfPermission(Activityestaticos.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    String toastText = "Saved emoji";
                    Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_LONG).show();
                    File file = FileUtil.saveEmojiBIG(context, this.layoutEmojiCreation, context.getFilesDir() + "/stickers/");
                    actualizovotos();
                   //  downloadFile(finalEmojiURL);
                } else {
                    ActivityCompat.requestPermissions(Activityestaticos.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
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

    public static String obtenerNombreArchivo(String url) {
        String[] partes = url.split("/");
        return partes[partes.length - 1];
    }

    public static String obtenerNombreArchivo2(String url) {
        String[] partes = url.split("/");
        return partes[partes.length - 2];
    }

    @SuppressLint("SuspiciousIndentation")
    private void actualizovotos() {
        int randomTOP =0;
        String fondoTOP = "";
        String cejasTOP = "";
        String bocaTOP = "";
        String objetosTOP = "";
        String manosTOP = "";
        String ojos_objetosTOP = "";
        String baseTOP = obtenerNombreArchivo2(finalEmojiURL);
        Log.e(TAG, "actualizovotos: " + baseTOP);
        if (Objects.equals(baseTOP, "images_manual")) {
            baseTOP = obtenerNombreArchivo(finalEmojiURL);
            String tipoTOP = "manual";

            RequestQueue queue = Volley.newRequestQueue(context);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, APITOP + "emoji1=" + idemote1 + "&" + "emoji2=" + idemote2 + "&baseTOP=" + baseTOP + "&tipoTOP=" + tipoTOP,
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
        } else {
            baseTOP = obtenerNombreArchivo(finalEmojiURL);

            String ojosTOP = obtenerNombreArchivo(ojosfinal);

            if (ojosobjetosfinal != null)
                ojos_objetosTOP = obtenerNombreArchivo(ojosobjetosfinal);

            if (cejasfinal != null)
                cejasTOP = obtenerNombreArchivo(cejasfinal);


            if (bocafinal != null)
                bocaTOP = obtenerNombreArchivo(bocafinal);

            if (objetosfinal != null)
                objetosTOP = obtenerNombreArchivo(objetosfinal);

            if (manosfinal != null)
                manosTOP = obtenerNombreArchivo(manosfinal);

            String anchoTOP = anchofinal;
            if (Objects.equals(anchoTOP, "images_formas"))
                anchoTOP = "";
            String leftTOP = leftfinal;
            if (Objects.equals(leftTOP, "images_formas"))
                leftTOP = "";
            String topTOP = topfinal;
            if (Objects.equals(topTOP, "images_formas"))
                topTOP = "";
            String tipoTOP = tipofinal;
            if (Objects.equals(tipoTOP, "images_formas"))
                tipoTOP = "";

            String extraTOP = obtenerNombreArchivo(extrafinal);
            if (Objects.equals(extraTOP, "images_formas"))
                extraTOP = "";

            randomTOP = randomfinal;
            if (fondofinal != null)
                fondoTOP = obtenerNombreArchivo(fondofinal);

            String rotacionTOP = rotacionfinal;
            if (Objects.equals(rotacionTOP, "images_formas"))
                rotacionTOP = "";
            String animacionTOP = animacionfinal;
            if (Objects.equals(animacionTOP, "images_formas"))
                animacionTOP = "";

           // Log.e(TAG, "actualizovotos: cosas " + APITOP + "emoji1=" + idemote1 + "&" + "emoji2=" + idemote2 + "&tipoTOP=" + tipoTOP + "&ojosTOP=" + ojosTOP + "&baseTOP=" + baseTOP + "&ojosobjetosTOP=" + ojos_objetosTOP + "&bocaTOP=" + bocaTOP + "&objetosTOP=" + objetosTOP + "&manosTOP=" + manosTOP + "&anchoTOP=" + anchoTOP + "&leftTOP=" + leftTOP + "&topTOP=" + topTOP + "&extraTOP=" + extraTOP + "&fondoTOP=" + fondoTOP + "&rotacionTOP=" + rotacionTOP + "&animacionTOP=" + animacionTOP+"&randomTOP="+randomTOP+ "&cejasTOP=" + cejasTOP);

            RequestQueue queue = Volley.newRequestQueue(context);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, APITOP + "emoji1=" + idemote1 + "&" + "emoji2=" + idemote2 + "&tipoTOP=" + tipoTOP + "&ojosTOP=" + ojosTOP + "&baseTOP=" + baseTOP + "&ojosobjetosTOP=" + ojos_objetosTOP + "&bocaTOP=" + bocaTOP + "&objetosTOP=" + objetosTOP + "&manosTOP=" + manosTOP + "&anchoTOP=" + anchoTOP + "&leftTOP=" + leftTOP + "&topTOP=" + topTOP + "&extraTOP=" + extraTOP + "&fondoTOP=" + fondoTOP + "&rotacionTOP=" + rotacionTOP + "&animacionTOP=" + animacionTOP+"&randomTOP="+randomTOP+ "&cejasTOP=" + cejasTOP,
                    (String) null, new Response.Listener<JSONObject>() {
                //  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onResponse(JSONObject response) {
                }
            }, error -> {

            });
            queue.add(request);
        }


    }


    private void LOGIC_BACKEND() {

        emojisSlider1LayoutManager = new CenterZoomLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        emojisSlider2LayoutManager = new CenterZoomLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        setSnapHelper(emojisSlider1, emojisSlider1SnapHelper, emojisSlider1LayoutManager);
        setSnapHelper(emojisSlider2, emojisSlider2SnapHelper, emojisSlider2LayoutManager);

        emojisSlider1.setLayoutManager(emojisSlider1LayoutManager);
        emojisSlider2.setLayoutManager(emojisSlider2LayoutManager);

        emojisSlider1.addItemDecoration(new offsetItemDecoration(this));
        emojisSlider2.addItemDecoration(new offsetItemDecoration(this));


        requestSupportedEmojis.startRequestNetwork(RequestNetworkController.GET, "http://emojixer.com/panel/api.php?todos=1", "", requestSupportedEmojisListener);


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
                LinearLayoutManager emojisSlider1ManagerDialog = new GridLayoutManager(this, 4);

                setSnapHelper(dialogemojisSlider1, emojisSlider1SnapHelper, emojisSlider1ManagerDialog);
                dialogemojisSlider1.setLayoutManager(emojisSlider1ManagerDialog);
                dialogemojisSlider1.setAdapter(new Adapterestaticos(supportedEmojisList, emojisSlider1ManagerDialog, Activityestaticos.this));
                LinearLayoutManager emojisSlider2ManagerDialog = new GridLayoutManager(this, 4);

                setSnapHelper(dialogemojisSlider2, emojisSlider1SnapHelper, emojisSlider2ManagerDialog);
                dialogemojisSlider2.setLayoutManager(emojisSlider2ManagerDialog);

                dialogemojisSlider2.setAdapter(new Adapterestaticos(supportedEmojisList, emojisSlider2ManagerDialog, Activityestaticos.this));


                emojisSlider1LayoutManager = new CenterZoomLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                emojisSlider2LayoutManager = new CenterZoomLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                setSnapHelper(emojisSlider1, emojisSlider1SnapHelper, emojisSlider1LayoutManager);
                setSnapHelper(emojisSlider2, emojisSlider2SnapHelper, emojisSlider2LayoutManager);



                emojisSlider1.setAdapter(new Adapterestaticos(supportedEmojisList, emojisSlider1LayoutManager, Activityestaticos.this));
                emojisSlider2.setAdapter(new Adapterestaticos(supportedEmojisList, emojisSlider2LayoutManager, Activityestaticos.this));
//                idemoji1 = Objects.requireNonNull(supportedEmojisList.get(1).get("idemoji1").toString());
//                idemoji2 = Objects.requireNonNull(supportedEmojisList.get(1).get("idemoji2").toString());


                new Handler().postDelayed(() -> {
                    for (int i = 0; i < 2; i++) {
                        Random rand = new Random();
                        int randomNum = rand.nextInt((supportedEmojisList.size()) - 1);
                        if (i == 0) {

                                    Log.e("TAG", "addDataToSliders id: "+idemoji1+idemoji2 );
                                    int centerOfScreen = emojisSlider1.getWidth() / 2 ;
                                    int ancho = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics());

                                    emojisSlider1LayoutManager.scrollToPositionWithOffset(idemoji1, centerOfScreen+ancho);

                        } else {


                            emojisSlider2.smoothScrollToPosition(idemoji2);
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

        emojisSlider1.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {


                if (isFineToUseListeners && newState == SCROLL_STATE_IDLE) {
                    emote1 = Objects.requireNonNull(supportedEmojisList.get(getRecyclerCurrentItem(emojisSlider1, emojisSlider1SnapHelper, emojisSlider1LayoutManager)).get("emoji_formado")).toString();
                    idemote1 = Objects.requireNonNull(supportedEmojisList.get(getRecyclerCurrentItem(emojisSlider1, emojisSlider1SnapHelper, emojisSlider1LayoutManager)).get("Id")).toString();

                    shouldShowEmoji(false);
                    mixEmojis(emote1, emote2, Objects.requireNonNull(supportedEmojisList.get(getRecyclerCurrentItem(emojisSlider1, emojisSlider1SnapHelper, emojisSlider1LayoutManager)).get("Id")).toString());
                    isFineToUseListeners = false;
                    Log.e("TAG", "addDataToSliders1: "+idemote1+" dos"+idemote2 );

                }
            }
        });

        emojisSlider2.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (isFineToUseListeners && newState == SCROLL_STATE_IDLE) {
                    idemote1 = Objects.requireNonNull(supportedEmojisList.get(getRecyclerCurrentItem(emojisSlider1, emojisSlider1SnapHelper, emojisSlider1LayoutManager)).get("Id")).toString();

                    idemote2 = Objects.requireNonNull(supportedEmojisList.get(getRecyclerCurrentItem(emojisSlider2, emojisSlider2SnapHelper, emojisSlider2LayoutManager)).get("Id")).toString();

                    emote2 = Objects.requireNonNull(supportedEmojisList.get(getRecyclerCurrentItem(emojisSlider2, emojisSlider2SnapHelper, emojisSlider2LayoutManager)).get("emoji_formado")).toString();
                    shouldShowEmoji(false);
                    mixEmojis(emote1, emote2, Objects.requireNonNull(supportedEmojisList.get(getRecyclerCurrentItem(emojisSlider2, emojisSlider2SnapHelper, emojisSlider2LayoutManager)).get("Id")).toString());
                    isFineToUseListeners = false;
                    Log.e("TAG", "addDataToSliders2: "+idemote1+" dos"+idemote2 );

                }
            }
        });
    }


    private void mixEmojis(String emoji1, String emoji2, String date) {

        shouldEnableSave(false);
       // progressBar.setVisibility(View.GONE);
              Log.e("TAG", "addDataToSliders: "+emoji1+" dos"+idemote2 );
        if (TextUtils.isEmpty(idemote1)) {
            idemote1 = "26";
            Log.e("TAG", "addDataToSliders entrr: "+emoji1+" dos"+idemote2 );


        }
        if (TextUtils.isEmpty(idemote2)) {
            idemote2 = "12";
            Log.e("TAG", "addDataToSliders entrr: "+emoji1+" dos"+idemote2 );


        }
        EmojiMixerestaticos em = new EmojiMixerestaticos(emoji1, emoji2, idemote2, idemote1, date, this, new EmojiMixerestaticos.EmojiListener() {

            @Override
            public void onSuccess(String emojiUrl, String ojos, String cejas, String objetos, String bocas, String finalojos_objetos,String manos,int ancho,int left, int top,String tipo,String extra,String fondo,float rotacion,int random) {

                shouldEnableSave(true);
                finalEmojiURL = emojiUrl;
                ojosfinal = ojos;
                objetosfinal = objetos;
                bocafinal = bocas;
                cejasfinal = cejas;
                ojosobjetosfinal = finalojos_objetos;
                manosfinal = manos;
                anchofinal = String.valueOf(ancho);
                leftfinal = String.valueOf(left);
                topfinal = String.valueOf(top);
                tipofinal = tipo;
                extrafinal = extra;
                fondofinal = fondo;
                randomfinal = random;
                Log.e(TAG, "onSuccess: actualizovotos 1"+random );
                rotacionfinal = String.valueOf(rotacion);

                if (Objects.equals(tipo, "objeto")) {

                    if(emoji1==emoji2) {
                        mixedemojiforma.setVisibility(View.VISIBLE);

                        mixedemojiforma.setImageURI(Uri.parse(extra));
                        mixedfondo.setVisibility(View.VISIBLE);

                        mixedfondo.setImageURI(Uri.parse(fondo));
                        Log.e("TAG", "objeto igual: "+extra );
                    }
                    else {

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
                }
               else if (Objects.equals(tipo, "objetodoble")) {
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
                        Log.e("TAG", "aki base objeto doble: "+fondo );

                    mixedemojiforma.setLayoutParams(new FrameLayout.LayoutParams(ancho3, ancho3));
                    ViewGroup.MarginLayoutParams params4 = (ViewGroup.MarginLayoutParams) mixedemojiforma.getLayoutParams();
                    params4.setMargins(0, 0, 0, 0);
                    mixedemojiforma.setLayoutParams(params4);
                    mixedemojiforma.setRotation(0);
                }

                else if (ancho > 0) {
                    Log.e("TAG", "aki rotacion ANCHO: " + manos );


                    //emojis3 iguales y objeto es emojis3 champi frutass....
                   if(Objects.equals(emote1, emote2) && Objects.equals(tipo, "emojis3")) {

                        posicionemoji.setRotation(0);
                        posicioncara.setRotation(rotacion);
                        mixedemojiforma.setVisibility(View.GONE);
                        int ancho3 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, getResources().getDisplayMetrics());
                        Log.e("TAG", "aki rotacion emojis3: " + ancho );
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
                   else if(Objects.equals(emote1, emote2) && Objects.equals(tipo, "emoji")) {

//                        Careta version
                        if(random ==1){
                            mixedemojiforma.setVisibility(View.VISIBLE);
                            mixedemojiforma.setImageURI(Uri.parse(extra));
                        //    Log.e("TAG", "aki igual emoji: "+extra);
                            int left2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, left, getResources().getDisplayMetrics());
                            int top2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, top, getResources().getDisplayMetrics());
                            int ancho3 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ancho, getResources().getDisplayMetrics());
                            int ancho4 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ancho-50, getResources().getDisplayMetrics());

                            mixedemojiforma.setLayoutParams(new FrameLayout.LayoutParams(ancho3, ancho3));
                            ViewGroup.MarginLayoutParams params2 = (ViewGroup.MarginLayoutParams) mixedemojiforma.getLayoutParams();
                            params2.setMargins(0, top2, 0, 0);
                            mixedemojiforma.setLayoutParams(params2);
                            mixedemojiforma.setRotation(0);

                          Log.e("TAG", "aki rotacion careta: " + rotacion );
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

                        mixedemojiforma.setLayoutParams(new FrameLayout.LayoutParams(ancho3-100, ancho3-100));
                        ViewGroup.MarginLayoutParams params2 = (ViewGroup.MarginLayoutParams) mixedemojiforma.getLayoutParams();
                        params2.setMargins(ancho3-75, top2+top2, 0, 0);
                        mixedemojiforma.setLayoutParams(params2);
                        mixedemojiforma.setRotation(-rotacion);

                        Log.e("TAG", "aki rotacion emoji doble: " + rotacion );
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
                    }


                    else {
                        posicionemoji.setRotation(0);
                        posicioncara.setRotation(rotacion);
                        mixedemojiforma.setVisibility(View.GONE);
                        int ancho3 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, getResources().getDisplayMetrics());
                        Log.e("TAG", "aki rotacion normal: " + ancho );
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
                mixedEmoji.setImageURI(Uri.parse(finalEmojiURL));
                mixedEmojiojos.setImageURI(Uri.parse(ojosfinal));
                mixedEmojicejas.setImageURI(Uri.parse(cejas));
                mixedEmojiobjetos.setImageURI(Uri.parse(objetos));
                mixedEmojibocas.setImageURI(Uri.parse(bocas));
                mixedEmojiojos_objetos.setImageURI(Uri.parse(finalojos_objetos));
                if (Objects.equals(idemote1, "26") || Objects.equals(idemote2, "26")) {
                    mixedEmojimanos2.setVisibility(View.VISIBLE);
                    mixedEmojimanos2.setImageURI(Uri.parse(manos));
                    mixedEmojimanos.setVisibility(View.GONE);
                }
                else {
                    mixedEmojimanos.setVisibility(View.VISIBLE);
                    mixedEmojimanos.setImageURI(Uri.parse(manos));
                    mixedEmojimanos2.setVisibility(View.GONE);

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

            shadAnimestaticos(layoutEmojiCreation, "scaleY", 1, 400);
            shadAnimestaticos(layoutEmojiCreation, "scaleX", 1, 400);
//            shadAnim(layoutEmojiCreation, "rotation", 360f, 400);
            shadAnimestaticos(layoutEmojiCreation, "translationY", 0, 400);

//           shadAnim(progressBar, "scaleY", 0, 300);
//           shadAnim(progressBar, "scaleX", 0, 300);
        } else {
        //    Log.e("TAG", "aki entro2:+finalEmojiURL " );
            mas.playAnimation();
         //   explosion.setImageURI("http://emojixer.emojinew.com/panel/explosion.webp");
            shadAnimestaticos(layoutEmojiCreation, "scaleY", 0.2, 400);
            shadAnimestaticos(layoutEmojiCreation, "scaleX", 0.2, 400);
//            shadAnim(layoutEmojiCreation, "rotation", 0f, 400);
            int anc = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, getResources().getDisplayMetrics());

            shadAnimestaticos(layoutEmojiCreation, "translationY", anc, 400);


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
    //    MiPublicidad.verInterstitialAd(this);

        Activityestaticos.this.startActivity(new Intent(Activityestaticos.this.getApplicationContext(), Activityverestaticos.class));

    }
}
