package com.emojixer.activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;
import static com.airbnb.lottie.LottieDrawable.INFINITE;
import static com.emojixer.functions.UIMethods.shadAnim;
import static com.emojixer.functions.Utils.getRecyclerCurrentItem;
import static com.emojixer.functions.Utils.setSnapHelper;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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

import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.RenderMode;
import com.airbnb.lottie.model.LottieCompositionCache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.emojixer.R;
import com.emojixer.adapters.EmojimixerAdapter;
import com.emojixer.functions.CenterZoomLayoutManager;
import com.emojixer.functions.EmojiMixer;
import com.emojixer.functions.FileUtil;
import com.emojixer.functions.Nemojismodel;
import com.emojixer.functions.RequestNetwork;
import com.emojixer.functions.RequestNetworkController;
import com.emojixer.functions.Utility;
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
import com.waynejo.androidndkgif.GifEncoder;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    private AlertDialog alertDialog;
    private AlertDialog alertDialogemojis,alertDialogemojis2;

    ImageView animatedImageView;
    LottieAnimationView lottieView,lodinglottie;
    ProgressBar progressdialog;
    ImageView capturedImageView;
    TextView texto,textotitulo;
    FrameLayout guardarbtn , progreso;
    private LottieAnimationView progressBar;
    public static final int NUM_FRAMES = 37; // Número total de fotogramas
    public static final int FRAME_DELAY_MS = 11; // Delay entre fotogramas en milisegundos
    public WrapContentDraweeView sticker,sticker2;

    private int currentFrame = 0;
    private ArrayList<Bitmap> capturedImages = new ArrayList<>();
    private Handler captureHandler = new Handler();
    static {
        System.loadLibrary("gif_encoder");
    }
    public native int convertFrameToWebP(Bitmap[] bitmaps, String outputPath, int quality);

    private LottieAnimationView saveEmoji;
    private WrapContentDraweeView mixedfondo,mixedEmojicejas,explosion,mixedEmoji0;
    private TextView activityDesc;
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    LottieAnimationView mixedEmojiojos_objetos,mixedEmoji,mixedEmojiojos,mixedEmojibocas,mixedEmojiobjetos,mixedEmojimanos2,mixedEmojimanos,mixedemojiforma,marca;
    private Button emojitop;
    private LottieAnimationView buscar1,buscar2;
    private String emote1;
    private String emote2;
    private  WrapContentDraweeView img;
    private String idemote1;
    private int idemoji1,idemoji2;
    private LottieAnimationView mas;
    private  GifImageView prog;

    private String idemote2;
    private String finalEmojiURL;
    private String ojosfinal,bocafinal,ojosobjetosfinal,objetosfinal,manosfinal,anchofinal,leftfinal,topfinal,tipofinal,extrafinal,fondofinal,rotacionfinal,animacionfinal;

    private RecyclerView emojisSlider1;
    private RecyclerView dialogemojisSlider1,dialogemojisSlider2;
    private RecyclerView emojisSlider2;
    private ArrayList<HashMap<String, Object>> supportedEmojisList = new ArrayList<>();
    private RequestNetwork requestSupportedEmojis;
    private RequestNetwork updatevotos;
    private RecyclerView emojisSlider;
    private RecyclerView.LayoutManager layoutManager;
    private SnapHelper snapHelper;
    private RequestNetwork.RequestListener  updatevotoslistener;
    int i=0;
    public static int posicionX = 245;
    ArrayList<Bitmap> savewebp = new ArrayList<>();
    private RequestNetwork.RequestListener requestSupportedEmojisListener;
    private SharedPreferences sharedPref;
    private boolean isFineToUseListeners = false;
    private LinearLayoutManager emojisSlider1LayoutManager,emojisSlider1ManagerDialog,emojisSlider2ManagerDialog;
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
    private AlertDialog dialog;  // Decláralo aquí
    ReviewInfo reviewInfo;
    Nemojismodel totalmodel;
    public static String api_emojis ="http://animated.emojixer.com/panel/api.php?todos=1";
    public static String APITOP ="http://animated.emojixer.com/panel/apitop.php?";
    private AdView adView;
    AtomicInteger operationsCompleted = new AtomicInteger();
    final int TOTAL_OPERATIONS = 6; // Ajusta este número según cuántas operaciones estás realizando.
    HashMap<String, LottieComposition> compositionsMap = new HashMap<>();
    boolean restraso = false;
    private RenderMode tipo = RenderMode.valueOf("SOFTWARE");
    static Bitmap reusableBitmap;
    private int posicionanterior = -1;
    private int posicionanterior2 = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        AudienceNetworkAds.initialize(this);

        AppRater.app_launched(this);





        setContentView(R.layout.activity_main);
        initLogic();


        dateTimeKey = "com.example.app.datetime";
        ensureStickersFolderExists(this);

// use a default value using new Date()
        days = sharedPref.getLong(dateTimeKey, Activity.MODE_PRIVATE);

        if(days <=0){
            Date dateStr = Calendar.getInstance().getTime();
            sharedPref.edit().putLong(dateTimeKey, dateStr.getTime()).apply();
        }




//        getemojis();

        context = this;

        numeroemojis();

      //  MiPublicidad.baner( (FrameLayout) findViewById(R.id.ad_view_container), this);

//        OneSignal.initWithContext(this);
//        OneSignal.setAppId("399b14d7-4c2a-4ce3-ab00-ce6358a44afa");

        //alertdialog
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_layout, null);
        progressdialog = dialogView.findViewById(R.id.progressBar);
        sticker = dialogView.findViewById(R.id.sticker);

        capturedImageView = dialogView.findViewById(R.id.capturedImageView);
        textotitulo = dialogView.findViewById(R.id.textoti);

        texto = dialogView.findViewById(R.id.textoprogress);
        guardarbtn = dialogView.findViewById(R.id.guardarbtn);
        progreso = dialogView.findViewById(R.id.progreso);
        lodinglottie = dialogView.findViewById(R.id.lodinglottie);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle("Guardar Emoji");
        alertDialog = builder.create();
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
        buscar1 = findViewById(R.id.icbuscar1);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view2 = LayoutInflater.from(this).inflate(R.layout.dialog_emojis1, null);
        dialogemojisSlider1 = view2.findViewById(R.id.dialogemojisSlider1);
        buscar1.playAnimation();
        dialogemojisSlider1.addOnItemTouchListener(new RecyclerItemClickListener(this, dialogemojisSlider1, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (position < 2) {
                    emojisSlider1.smoothScrollToPosition(position);
                }
                else  if (posicionanterior > position) {
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


        buscar2 = findViewById(R.id.icbuscar2);
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
                }
                else if (posicionanterior2 > position) {
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

    }
    public void initLogic() {


        progressBar = findViewById(R.id.progressBar);
        mixedEmoji = findViewById(R.id.mixedEmoji);
        mixedEmoji0 = findViewById(R.id.mixedEmoji0);

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
        marca = findViewById(R.id.marca);

        saveEmoji = findViewById(R.id.saveEmoji);
        saveEmoji.playAnimation();

        emojisSlider1 = findViewById(R.id.emojisSlider1);
        emojisSlider2 = findViewById(R.id.emojisSlider2);
        requestSupportedEmojis = new RequestNetwork(this);
        sharedPref = getSharedPreferences("AppData", Activity.MODE_PRIVATE);
        layoutEmojiCreation =  findViewById(R.id.frame_emoji_creation);
        posicioncara =  findViewById(R.id.posicioncara);
        posicionem = findViewById(R.id.posicione);
        configurarDialogoEmojis();
//        layoutEmojiCreation.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//
//        mixedEmojiojos_objetos.setRenderMode(tipo);
//        mixedEmoji.setRenderMode(tipo);
//        mixedEmojiojos.setRenderMode(tipo);
//        mixedEmojibocas.setRenderMode(tipo);
//        mixedEmojiobjetos.setRenderMode(tipo);
//        mixedEmojimanos2.setRenderMode(tipo);
//        mixedEmojimanos.setRenderMode(tipo);
//        mixedemojiforma.setRenderMode(tipo);


        emojitop = findViewById(R.id.emojitop);
        emojitop.setOnClickListener(view -> {
            //  MiPublicidad.verInterstitialAd(this);
            MainActivity.this.startActivity(new Intent(MainActivity.this.getApplicationContext(), Emojitop.class));


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
                captureMultipleImages(layoutEmojiCreation);

                //  showAnimatedFile();
                //  captureFrames(layoutEmojiCreation);
                //    File file = FileUtil.saveEmojiBIG(context, this.layoutEmojiCreation, context.getFilesDir() + "/stickers/");
                String toastText = "Saved emoji";
                Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_LONG).show();
                actualizovotos();
                //   downloadFile(finalEmojiURL);
            } else {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    String toastText = "Saved emoji";
                    Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_LONG).show();

                    File file = FileUtil.saveEmojiBIG(context, this.layoutEmojiCreation, context.getFilesDir() + "/stickers/");
                    actualizovotos();
                    //  downloadFile(finalEmojiURL);
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
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
    @SuppressLint("SuspiciousIndentation")
    private void actualizovotos(){
        String fondoTOP = "";
        String bocaTOP = "";
        String objetosTOP = "";
        String manosTOP = "";
        String ojos_objetosTOP = "";
        String ojosTOP = obtenerNombreArchivo(ojosfinal);
        String baseTOP = obtenerNombreArchivo(finalEmojiURL);

        if(ojosobjetosfinal != null)
        ojos_objetosTOP = obtenerNombreArchivo(ojosobjetosfinal);


        if(bocafinal != null)
        bocaTOP = obtenerNombreArchivo(bocafinal);

        if(objetosfinal != null)
         objetosTOP = obtenerNombreArchivo(objetosfinal);

        if(manosfinal != null)
         manosTOP = obtenerNombreArchivo(manosfinal);

        String anchoTOP = anchofinal;
        if(Objects.equals(anchoTOP, "images_formas"))
            anchoTOP = "";
        String leftTOP = leftfinal;
        if(Objects.equals(leftTOP, "images_formas"))
            leftTOP = "";
        String topTOP = topfinal;
        if(Objects.equals(topTOP, "images_formas"))
            topTOP = "";
        String tipoTOP = tipofinal;
        if(Objects.equals(tipoTOP, "images_formas"))
            tipoTOP = "";

        String extraTOP = extrafinal;
        if(Objects.equals(extraTOP, "images_formas"))
            extraTOP = "";


            if(fondofinal != null)
                fondoTOP = obtenerNombreArchivo(fondofinal);

        String rotacionTOP = rotacionfinal;
        if(Objects.equals(rotacionTOP, "images_formas"))
            rotacionTOP ="";
        String animacionTOP = animacionfinal;
        if(Objects.equals(animacionTOP, "images_formas"))
            animacionTOP = "";



        Log.e(TAG, "actualizovotos: cosas "+bocafinal);

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, APITOP+"emoji1="+idemote1+"&"+"emoji2="+idemote2+"&ojosTOP="+ojosTOP+"&baseTOP="+baseTOP+"&ojosobjetosTOP="+ojos_objetosTOP+"&bocaTOP="+bocaTOP+"&objetosTOP="+objetosTOP+"&manosTOP="+manosTOP+"&anchoTOP="+anchoTOP+"&leftTOP="+leftTOP+"&topTOP="+topTOP+"&extraTOP="+extraTOP+"&fondoTOP="+fondoTOP+"&rotacionTOP="+rotacionTOP+"&animacionTOP="+animacionTOP,
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

    public void numeroemojis() {


        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "http://animated.emojixer.com/panel/api.php?nemojis=1",
                (String) null, new Response.Listener<JSONObject>() {
            //  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(JSONObject response) {



                // Successfully called Graph. Process data and send to UI.
                Gson gson = new Gson();
                totalmodel = gson.fromJson(response.toString(), Nemojismodel.class);
                idemoji1 =  totalmodel.getnemoji1();
                idemoji2 =  totalmodel.getnemoji2();

                //   Log.e("TAG", "aki entro: " );
                LOGIC_BACKEND();

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

    private void LOGIC_BACKEND() {


        emojisSlider1ManagerDialog = new GridLayoutManager(this, 4);



        setSnapHelper(dialogemojisSlider1, emojisSlider1SnapHelper, emojisSlider1ManagerDialog);
        dialogemojisSlider1.setLayoutManager(emojisSlider1ManagerDialog);

        dialogemojisSlider1.setAdapter(new EmojimixerAdapter(supportedEmojisList, emojisSlider1ManagerDialog, MainActivity.this));

        emojisSlider2ManagerDialog = new GridLayoutManager(this, 4);

        setSnapHelper(dialogemojisSlider2, emojisSlider1SnapHelper, emojisSlider2ManagerDialog);
        dialogemojisSlider2.setLayoutManager(emojisSlider2ManagerDialog);

        dialogemojisSlider2.setAdapter(new EmojimixerAdapter(supportedEmojisList, emojisSlider2ManagerDialog, MainActivity.this));


        emojisSlider1LayoutManager = new CenterZoomLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        emojisSlider2LayoutManager = new CenterZoomLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        setSnapHelper(emojisSlider1, emojisSlider1SnapHelper, emojisSlider1LayoutManager);
        setSnapHelper(emojisSlider2, emojisSlider2SnapHelper, emojisSlider2LayoutManager);

        emojisSlider1.setLayoutManager(emojisSlider1LayoutManager);
        emojisSlider2.setLayoutManager(emojisSlider2LayoutManager);



        requestSupportedEmojis.startRequestNetwork(RequestNetworkController.GET, "http://animated.emojixer.com/panel/api.php?todos=1", "", requestSupportedEmojisListener);


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
                emojisSlider1.setAdapter(new EmojimixerAdapter(supportedEmojisList, emojisSlider1LayoutManager, MainActivity.this));
                emojisSlider2.setAdapter(new EmojimixerAdapter(supportedEmojisList, emojisSlider2LayoutManager, MainActivity.this));
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
         progressBar.setVisibility(View.GONE);
        Log.e("TAG", "addDataToSliders: "+emoji1+" dos"+idemote2 );
        if (TextUtils.isEmpty(idemote1)) {
            idemote1 = "26";
            Log.e("TAG", "addDataToSliders entrr: "+emoji1+" dos"+idemote2 );


        }
        if (TextUtils.isEmpty(idemote2)) {
            idemote2 = "12";
            Log.e("TAG", "addDataToSliders entrr: "+emoji1+" dos"+idemote2 );


        }
        EmojiMixer em = new EmojiMixer(emoji1, emoji2, idemote2, idemote1, date, this, new EmojiMixer.EmojiListener() {
            Animation animation;
            @Override
            public void onSuccess(String emojiUrl, String ojos, String cejas, String objetos, String bocas, String finalojos_objetos,String manos,int ancho,int left, int top,String tipo,String extra,String fondo,float rotacion,int random,String animacion)  {
                Log.e("TAG", "aki datos api: "+emojiUrl+" ojos"+ojos+objetos+bocas );

                shouldEnableSave(true);

                restraso = false;
                finalEmojiURL = emojiUrl;
                ojosfinal = ojos;
                objetosfinal = objetos;
                bocafinal = bocas;
                ojosobjetosfinal = finalojos_objetos;
                manosfinal = manos;
                anchofinal = String.valueOf(ancho);
                leftfinal = String.valueOf(left);
                topfinal = String.valueOf(top);
                tipofinal = tipo;
                extrafinal = extra;
                fondofinal = fondo;
                rotacionfinal = String.valueOf(rotacion);
                animacionfinal = animacion;

                if (animacion != null) {
// Obtener el identificador de recurso entero para la animación
                    int animationResourceId = context.getResources().getIdentifier(animacion, "anim", context.getPackageName());

                    if (animationResourceId != 0) {
                        // Cargar la animación si se encontró el recurso
                        animation = AnimationUtils.loadAnimation(context, animationResourceId);
                        Log.e(TAG, "onSuccess: aki anima "+animation );


                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                // No se necesita implementación
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                // La animación de entrada ha terminado
                                // Inicia la animación inversa después de un retraso
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        posicionemoji.startAnimation(animation);

                                    }
                                }, 0); // Espera 2000 milisegundos (2 segundos) antes de iniciar la animación inversa
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                                // No se necesita implementación
                            }
                        });
                        posicionemoji.startAnimation(animation);


                        // Aplicar la animación a una vista, si es necesario
                        // view.startAnimation(animation);
                    }
                }
                else {
                    //     posicioncara.setRotation(rotacion);


                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.animacion);

                    posicionemoji.startAnimation(animation);
                    posicionemoji.clearAnimation();
                    posicionemoji.setRotation(rotacion);
                    // Manejar el caso en el que la animación no se encuentra en los recursos
                    // Aquí puedes mostrar un mensaje de error o tomar otra acción apropiada.
                }

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
                        Log.e("TAG", "aki rotacion objeto: " + ancho3);

                        mixedemojiforma.setVisibility(View.VISIBLE);
                        posicioncara.setRotation(0);

                        posicioncara.setLayoutParams(new FrameLayout.LayoutParams(ancho2, ancho2));
                        ViewGroup.MarginLayoutParams params2 = (ViewGroup.MarginLayoutParams) posicioncara.getLayoutParams();
                        params.setMargins(left2, top2, 0, 0);
                        posicioncara.setLayoutParams(params2);

                        LottieCompositionFactory.fromUrl(context, String.valueOf(Uri.parse(extra)))
                                .addListener(composition -> {
                                    mixedemojiforma.setComposition(composition);
                                    mixedemojiforma.playAnimation();
                                })
                        ;
                        // mixedemojiforma.setImageURI(Uri.parse(extra));
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

                    LottieCompositionFactory.fromUrl(context,extra)
                            .addListener(composition -> {
                                mixedemojiforma.setComposition(composition);
                                mixedemojiforma.playAnimation();
                            })
                    ;
                    mixedfondo.setVisibility(View.VISIBLE);

                    mixedfondo.setImageURI(Uri.parse(fondo));
                    Log.e("TAG", "aki base objeto doble: "+extra );

                    mixedemojiforma.setLayoutParams(new FrameLayout.LayoutParams(ancho3, ancho3));
                    ViewGroup.MarginLayoutParams params4 = (ViewGroup.MarginLayoutParams) mixedemojiforma.getLayoutParams();
                    params4.setMargins(0, 0, 0, 0);
                   // mixedemojiforma.setLayoutParams(params4);
                    mixedemojiforma.setRotation(0);
                }

                else if (ancho > 0) {


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
                            // mixedemojiforma.setImageURI(Uri.parse(extra));
                            LottieCompositionFactory.fromUrl(context, String.valueOf(Uri.parse(extra)))
                                    .addListener(composition -> {
                                        mixedemojiforma.setComposition(composition);
                                        mixedemojiforma.playAnimation();
                                    })
                            ;
                            restraso = true;

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

                            Log.e("TAG", "aki rotacion careta: " + extra );
                            posicionemoji.setLayoutParams(new FrameLayout.LayoutParams(ancho4, ancho4));
                            ViewGroup.MarginLayoutParams params3 = (ViewGroup.MarginLayoutParams) posicionemoji.getLayoutParams();
                            params3.setMargins(left2, top2, 0, 0);
                            posicionemoji.setLayoutParams(params3);
                            posicionemoji.setVisibility(View.INVISIBLE);

                            posicioncara.setLayoutParams(new FrameLayout.LayoutParams(ancho4, ancho4));
                            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) posicioncara.getLayoutParams();
                            params.setMargins(0, 0, 0, 0);
                            posicioncara.setLayoutParams(params);



                            Handler handler = new Handler();
                            handler.postDelayed(() -> {

                                //  posicionemoji.setRotation(rotacion);
                                ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(posicionemoji, "rotation", 0f, rotacion);

// Configura la duración de la animación (por ejemplo, 1000 milisegundos)
                                rotationAnimator.setDuration(1880);
// Animación de desplazamiento horizontal
                                ObjectAnimator translateXAnimator = ObjectAnimator.ofFloat(posicionemoji, "translationX", 0f, 30f);
                                translateXAnimator.setDuration(2880);
                                translateXAnimator.setRepeatCount(ObjectAnimator.INFINITE);

// Animación de desplazamiento vertical
                                ObjectAnimator translateYAnimator = ObjectAnimator.ofFloat(posicionemoji, "translationY", 0f, 0f);
                                translateYAnimator.setDuration(1000);
// Animación de transparencia (fade out y luego fade in)
                                ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(posicionemoji, "alpha",  0f, 1f);
                                alphaAnimator.setDuration(1000);

// Combina las animaciones para ejecutarlas simultáneamente
                                AnimatorSet animatorSet = new AnimatorSet();
                                animatorSet.playTogether(rotationAnimator, translateXAnimator, translateYAnimator,alphaAnimator);
                                // animatorSet.start();



// Si quieres que la animación se repita indefinidamente, puedes usar los siguientes métodos:
                                rotationAnimator.setRepeatCount(ObjectAnimator.INFINITE);
                                rotationAnimator.setRepeatMode(ObjectAnimator.RESTART);


                            }, 0); // 5000 milisegundos = 5 segundos



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
                        //    posicioncara.setRotation(rotacion);

//
//
//// Define las coordenadas iniciales y finales de la escala
//                        float scaleFromX = 1.2f; // Escala inicial en el eje X
//                        float scaleToX = 1.0f;   // Escala final en el eje X
//                        float scaleFromY = 1.2f; // Escala inicial en el eje Y
//                        float scaleToY = 1.0f;   // Escala final en el eje Y
//
//                        int durationMillis = 1000; // Por ejemplo, 1 segundo
//
//                        ScaleAnimation scaleAnimation = new ScaleAnimation(
//                                scaleFromX, scaleToX, scaleFromY, scaleToY,
//                                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.2f
//                        );
//                        scaleAnimation.setRepeatCount(-1);
//
//                        scaleAnimation.setDuration(durationMillis);
//
//                        posicioncara.startAnimation(scaleAnimation);


//                        AnimationDrawable animationDrawable = (AnimationDrawable) getResources().getDrawable(R.drawable.animacion);
//                        posicioncara.setBackground(animationDrawable);
//                        animationDrawable.start();

//                        Animation customAnimation = AnimationUtils.loadAnimation(context, R.anim.animacion);
//                        customAnimation.setRepeatMode(Animation.INFINITE);
//                        customAnimation.setRepeatCount(Animation.INFINITE);
//
//                        posicioncara.startAnimation(customAnimation);


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

                    Log.e("TAG", "emoji y objeto2: " + ancho2 );


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



                String extension = "";

                int lastIndexOfDot = finalEmojiURL.lastIndexOf('.');
                if (lastIndexOfDot != -1) {
                    extension = finalEmojiURL.substring(lastIndexOfDot + 1);
                }
                Log.e("TAG", "onSuccess: aki ojos"+extension+extra);




                if ("png".equals(extension)) {
                    mixedEmoji.setVisibility(View.GONE);
                    mixedEmoji0.setVisibility(View.VISIBLE);
                    mixedEmoji0.setImageURI(Uri.parse(finalEmojiURL));
               //     Log.e("TAG", "onSuccess: aki es png ");
                    operationsCompleted.getAndIncrement();
                    checkAllOperationsCompleted();
                } else {
                    Log.e("xomplete1", "aki base ");

                    mixedEmoji.setVisibility(View.VISIBLE);
                    mixedEmoji0.setVisibility(View.GONE);
               //     Log.e("TAG", "onSuccess: aki es png no ");
                    LottieCompositionFactory.fromUrl(context, String.valueOf(Uri.parse(finalEmojiURL)))
                            .addListener(composition -> {
                                compositionsMap.put("finalEmojiURL", composition);
                                operationsCompleted.getAndIncrement();
                                checkAllOperationsCompleted();
                            })
                             .addFailureListener(exception -> {
                                 operationsCompleted.getAndIncrement();
                        checkAllOperationsCompleted();
                    });
                }


             //   mixedEmojicejas.setImageURI(Uri.parse(cejas));
//                mixedEmojiobjetos.setImageURI(Uri.parse(objetos));

                if (objetos != null) {
                 //   Log.e("xomplete2", "aki pjetos ");

                    LottieCompositionFactory.fromUrl(context, String.valueOf(Uri.parse(objetos)))
                            .addListener(composition -> {
                                mixedEmojiobjetos.setVisibility(View.VISIBLE);

                                compositionsMap.put("objetos", composition);
                                operationsCompleted.getAndIncrement();
                                checkAllOperationsCompleted();
                            })
                            .addFailureListener(exception -> {
                                mixedEmojiobjetos.setVisibility(View.INVISIBLE);



                                //  Log.e("LottieError", "Error al cargar animación desde URL. Usando animación local objetos.", exception);
                                operationsCompleted.getAndIncrement();
                                checkAllOperationsCompleted();
                            });
                }




                if (bocas != null) {
                 //   Log.e("xomplete3", "aki bocas ");

                    LottieCompositionFactory.fromUrl(context, String.valueOf(Uri.parse(bocas)))
                            .addListener(composition -> {
                                compositionsMap.put("bocas", composition);
                                operationsCompleted.getAndIncrement();
                                checkAllOperationsCompleted();
                            })
                            .addFailureListener(exception -> {
                                // Si falla cargar desde la URL, carga el archivo local

//                                mixedEmojibocas.setAnimation(R.raw.vacio);
                                //  mixedEmojibocas.playAnimation();
                                operationsCompleted.getAndIncrement();
                                checkAllOperationsCompleted();
                                //  Log.e("LottieError", "Error al cargar animación desde URL. Usando animación local.", exception);
                            });
                }

                if (ojosfinal != null) {
                //    Log.e("xomplete4", "aki ojos");

                    LottieCompositionFactory.fromUrl(context, String.valueOf(Uri.parse(ojosfinal)))
                            .addListener(composition -> {
                                compositionsMap.put("ojosfinal", composition);
                                operationsCompleted.getAndIncrement();
                                checkAllOperationsCompleted();
                            }).addFailureListener(exception -> {
                                // Si falla cargar desde la URL, carga el archivo local

//                                mixedEmojiojos.setAnimation(R.raw.vacio);
                                // mixedEmojiojos.playAnimation();
                                operationsCompleted.getAndIncrement();
                                checkAllOperationsCompleted();
                                //   Log.e("LottieError", "Error al cargar animación desde URL. Usando animación local.", exception);
                            });
                }


                if (finalojos_objetos != null) {
                //    Log.e("xomplete5", "aki ojos jetos ");

                    LottieCompositionFactory.fromUrl(context, String.valueOf(Uri.parse(finalojos_objetos)))
                            .addListener(composition -> {
                                compositionsMap.put("finalojos_objetos", composition);
                                operationsCompleted.getAndIncrement();
                                checkAllOperationsCompleted();
                                mixedEmojiojos_objetos.setVisibility(View.VISIBLE);

                            })
                            .addFailureListener(exception -> {
                                // Si falla cargar desde la URL, carga el archivo local
                                //    Log.e("TAG", "aki emoji final2: "+String.valueOf(Uri.parse(finalojos_objetos)) );
                                mixedEmojiojos_objetos.setVisibility(View.INVISIBLE);
                                mixedEmojiojos_objetos.setAnimation(R.raw.vacio);
                                //   mixedEmojiojos_objetos.playAnimation();
                                operationsCompleted.getAndIncrement();
                                checkAllOperationsCompleted();
                                // Log.e("LottieError", "Error al cargar animación desde URL. Usando animación local.", exception);
                            });
                }



                if (Objects.equals(idemote1, "26") || Objects.equals(idemote2, "26")) {
                 //   Log.e("xomplete6", "aki manos 26."+manos);

                    mixedEmojimanos2.setVisibility(View.VISIBLE);
                    mixedEmojimanos2.setImageURI(Uri.parse(manos));
                    mixedEmojimanos.setVisibility(View.GONE);


                    LottieCompositionFactory.fromUrl(context, String.valueOf(Uri.parse(manos)))
                            .addListener(composition -> {
                                compositionsMap.put("manos", composition);
                                operationsCompleted.getAndIncrement();
                                checkAllOperationsCompleted();
                            }).addFailureListener(exception -> {
                                // Si falla cargar desde la URL, carga el archivo local

                                mixedEmojimanos2.setAnimation(R.raw.vacio);
                                //   mixedEmojimanos2.playAnimation();
                                operationsCompleted.getAndIncrement();
                                checkAllOperationsCompleted();
                            });

                }
                else {
                    mixedEmojimanos.setVisibility(View.VISIBLE);
                    //   mixedEmojimanos.setImageURI(Uri.parse(manos));
                    mixedEmojimanos2.setVisibility(View.GONE);

                 //   Log.e("xcomplete6", "aki manos."+manos);

                    LottieCompositionFactory.fromUrl(context, String.valueOf(Uri.parse(manos)))
                            .addListener(composition -> {
                                compositionsMap.put("manos", composition);
                                operationsCompleted.getAndIncrement();
                                checkAllOperationsCompleted();
                            }).addFailureListener(exception -> {
                                // Si falla cargar desde la URL, carga el archivo local

                                mixedEmojimanos.setVisibility(View.GONE);
                                //     mixedEmojimanos.playAnimation();
                                operationsCompleted.getAndIncrement();
                                checkAllOperationsCompleted();
                            });
                }



                isFineToUseListeners = true;

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
        Log.e("TAG", "aki resultado operaciones: "+operationsCompleted.get() );

    }
    Runnable delayedAnimation = new Runnable() {
        @Override
        public void run() {
            mostraranimacion();
        }
    };
    private void checkAllOperationsCompleted() {
        Log.e(TAG, "checkAllOperationsCompleted: "+operationsCompleted.get() );
        if (operationsCompleted.get() == TOTAL_OPERATIONS) {
            operationsCompleted.set(0);

            if (restraso == true) {
                Handler handler = new Handler();
                // Elimina cualquier tarea pendiente
                handler.removeCallbacks(delayedAnimation);
                posicionemoji.setVisibility(View.VISIBLE);

                // Envía una nueva tarea al Handler
                handler.postDelayed(delayedAnimation, 50);
            }
            else {
                mostraranimacion();
            }
        }
    }
    public void mostraranimacion(){

        // Todas las operaciones se han completado
        if (compositionsMap.containsKey("finalEmojiURL")) {
            mixedEmoji.setComposition(compositionsMap.get("finalEmojiURL"));
            mixedEmoji.playAnimation();
            mixedEmoji.setRepeatCount(INFINITE);
        }
        if (compositionsMap.containsKey("objetos")) {
            mixedEmojiobjetos.setComposition(compositionsMap.get("objetos"));
            mixedEmojiobjetos.playAnimation();
            mixedEmojiobjetos.setRepeatCount(INFINITE);
        }
        if (compositionsMap.containsKey("bocas")) {
            Log.e(TAG, "mostraranimacion: aki bocas "+bocafinal);

            mixedEmojibocas.setComposition(compositionsMap.get("bocas"));
            mixedEmojibocas.playAnimation();
            mixedEmojibocas.setRepeatCount(INFINITE);
            Log.e("TAG", "aki resultado de bocas: " + compositionsMap.get("bocas"));

        }
        if (compositionsMap.containsKey("ojosfinal")) {
            Log.e(TAG, "mostraranimacion: aki ojos "+ojosfinal );

            mixedEmojiojos.setComposition(compositionsMap.get("ojosfinal"));
            mixedEmojiojos.playAnimation();
            mixedEmojiojos.setRepeatCount(INFINITE);
        }
        if (compositionsMap.containsKey("finalojos_objetos")) {
            mixedEmojiojos_objetos.setComposition(compositionsMap.get("finalojos_objetos"));
            mixedEmojiojos_objetos.playAnimation();
            mixedEmojiojos_objetos.setRepeatCount(INFINITE);
        }
        if (compositionsMap.containsKey("manos")) {
            mixedEmojimanos2.setComposition(compositionsMap.get("manos"));
            mixedEmojimanos2.playAnimation();
            mixedEmojimanos2.setRepeatCount(INFINITE);
            mixedEmojimanos.setComposition(compositionsMap.get("manos"));
            mixedEmojimanos.playAnimation();
            mixedEmojimanos.setRepeatCount(INFINITE);
        }

        shouldShowEmoji(true);
    }

    private void shouldShowEmoji(boolean shouldShow) {


        if (shouldShow) {
            //  Log.e("TAG", "aki entro:+finalEmojiURL " );

           shadAnim(layoutEmojiCreation, "scaleY", 1, 400,50);
          shadAnim(layoutEmojiCreation, "scaleX", 1, 400,50);
//            shadAnim(layoutEmojiCreation, "rotation", 360f, 400);
  // shadAnim(layoutEmojiCreation, "translationX", 1, 400);
            shadAnim(layoutEmojiCreation, "alpha", 1f, 800,50); // Cambia el valor "0.5f" según tus necesidades


           shadAnim(progressBar, "scaleY", 0, 300,1);
          shadAnim(progressBar, "scaleX", 0, 300,1);
        } else {
            //    Log.e("TAG", "aki entro2:+finalEmojiURL " );
            mas.playAnimation();
            //   explosion.setImageURI("http://emojixer.emojinew.com/panel/explosion.webp");
            shadAnim(layoutEmojiCreation, "scaleY", 0, 400,0);
         shadAnim(layoutEmojiCreation, "scaleX", 0, 400,0);
       //   shadAnim(layoutEmojiCreation, "translationX", 0, 400);
            shadAnim(layoutEmojiCreation, "alpha", 0.0f, 800,0); // Cambia el valor "0.5f" según tus necesidades

//            shadAnim(layoutEmojiCreation, "rotation", 0f, 400);
//            int anc = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, getResources().getDisplayMetrics());
//
//           shadAnim(layoutEmojiCreation, "translationY", anc, 400);

            progressBar.setVisibility(View.VISIBLE);
          shadAnim(progressBar, "scaleY", 1, 300,1);
           shadAnim(progressBar, "scaleX", 1, 300,1);
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



    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }
    private HandlerThread handlerThread;
    private Handler backgroundHandler;
    public void initHandlerThread() {
        handlerThread = new HandlerThread("CaptureThread");
        handlerThread.start();
        backgroundHandler = new Handler(handlerThread.getLooper());
    }


    private void cancelAllAnimations() {
        if (mixedEmojiojos_objetos != null) {
            mixedEmojiojos_objetos.cancelAnimation();
        }
        if (mixedEmoji != null) {
            mixedEmoji.cancelAnimation();
        }
        if (mixedEmojiojos != null) {
            mixedEmojiojos.cancelAnimation();
        }
        if (mixedEmojibocas != null) {
            mixedEmojibocas.cancelAnimation();
        }
        if (mixedEmojiobjetos != null) {
            mixedEmojiobjetos.cancelAnimation();
        }
        if (mixedEmojimanos2 != null) {
            mixedEmojimanos2.cancelAnimation();
        }
        if (mixedEmojimanos != null) {
            mixedEmojimanos.cancelAnimation();
        }
        if (mixedemojiforma != null) {
            mixedemojiforma.cancelAnimation();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelAllAnimations();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelAllAnimations();
        // Aquí puedes liberar otros recursos o detener cualquier otro proceso que ya no sea necesario
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

        MainActivity.this.startActivity(new Intent(MainActivity.this.getApplicationContext(), ImageViewerActivity.class));

    }

    public void captureMultipleImages(FrameLayout layoutEmojiCreation) {
        // ... (código para configurar el AlertDialog y las vistas)
        currentFrame = 0;


        alertDialog.show();
        // Crear un ProgressDialog determinado por el progreso

        // Cuando el AlertDialog se muestra, comienza la captura de frames

        mixedEmojiojos_objetos.setProgress(0f); // Establece el progreso al principio
        mixedEmojiojos_objetos.getDuration();

        Log.e(TAG, "captureMultipleImages: duracion ojos objeto "+mixedEmojiojos_objetos.getFrame()+"| objetos "+mixedEmojiobjetos.getDuration()+"| ojos "+mixedEmojiojos.getDuration()+"| bocas"+mixedEmojibocas.getDuration()+"| base"+mixedEmoji.getDuration()+" | manos"+mixedEmojimanos.getDuration()+"| forma "+mixedemojiforma.getDuration() );

        mixedEmojiobjetos.setProgress(0f); // Establece el progreso al principio

        mixedEmojimanos2.setProgress(0f); // Establece el progreso al principio

        mixedEmojimanos.setProgress(0f); // Establece el progreso al principio

        mixedEmojiojos.setProgress(0f); // Establece el progreso al principio

        mixedEmoji.setProgress(0f); // Establece el progreso al principio

        mixedEmojibocas.setProgress(0f); // Establece el progreso al principio

        mixedemojiforma.setProgress(0f); // Establece el progreso al principio
        mixedEmojibocas.setRepeatCount(0);
        mixedEmojiobjetos.setRepeatCount(0);
        mixedEmojimanos2.setRepeatCount(0);
        mixedEmojimanos.setRepeatCount(0);
        mixedEmojiojos.setRepeatCount(0);
        mixedEmoji.setRepeatCount(0);
        mixedemojiforma.setRepeatCount(0);
        mixedEmojiojos_objetos.setRepeatCount(0);
        captureNextFrame(layoutEmojiCreation, capturedImages, capturedImageView);
         marca.setAnimation(R.raw.marca);
        marca.setVisibility(View.VISIBLE);
        marca.playAnimation();



//        mixedEmojiojos_objetos.cancelAnimation();
//        mixedEmojiobjetos.cancelAnimation();
//        mixedEmojimanos.cancelAnimation();
//        mixedemojiforma.cancelAnimation();
//        mixedEmojibocas.cancelAnimation();
//        mixedEmoji.cancelAnimation();
//        mixedEmojiojos.cancelAnimation();
//        mixedEmojimanos2.cancelAnimation();
        Log.e("TAG", "captureMultipleImages3: " + capturedImages);
        progreso.setVisibility(View.VISIBLE);
        guardarbtn.setVisibility(View.INVISIBLE);
        textotitulo.setText("Capturando fotogramas");
        alertDialog.setTitle("Guardar emoji");

        // Luego, cuando estés listo para mostrar el GIF, puedes hacerlo aquí

        runOnUiThread(() -> {

//            Uri gifUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", new File(gifPath));

            // Establecer la URI en el ImageView
            // gifImageView.setImageURI(gifUri);

            // alertDialog.dismiss();
        });
    }
    public static void ensureStickersFolderExists(Context context) {
        File stickersFolder = new File(context.getFilesDir(), "stickers");

        if (!stickersFolder.exists()) {
            if (stickersFolder.mkdirs()) {
                Log.d("StickersFolder", "Carpeta stickers creada correctamente.");
            } else {
                Log.e("StickersFolder", "No se pudo crear la carpeta stickers.");
            }
        } else {
            Log.d("StickersFolder", "La carpeta stickers ya existe.");
        }
    }
    private static Bitmap getBitmapFromView(FrameLayout layout) {
        Bitmap bitmap = Bitmap.createBitmap(layout.getWidth(), layout.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        layout.draw(canvas);


        // Convierte de ARGB a RGBA
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap rgbaBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas rgbaCanvas = new Canvas(rgbaBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                0, 0, 1, 0, 0, // Red pasa a Blue
                0, 1, 0, 0, 0, // Green pasa a Green
                1, 0, 0, 0, 0, // Blue pasa a Red
                0, 0, 0, 1, 0  // Alpha se mantiene igual
        });
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(filter);
        rgbaCanvas.drawBitmap(bitmap, 0, 0, paint);

        // Escala el bitmap convertido a 512x512
        int targetSize = 512;
        Bitmap scaledRgbaBitmap = Bitmap.createScaledBitmap(rgbaBitmap, targetSize, targetSize, false);
        return scaledRgbaBitmap;
    }

    private void captureNextFrame(FrameLayout layoutEmojiCreation, ArrayList<Bitmap> capturedImages,
                                  ImageView capturedImageView) {
        capturedImageView.setVisibility(View.VISIBLE);
        sticker.setVisibility(View.INVISIBLE);
        if (currentFrame < NUM_FRAMES) {

            Pair<Bitmap, Bitmap> bitmaps = FileUtil.captureFrameLayout(layoutEmojiCreation);
            Bitmap originalBitmap = bitmaps.first;
            Bitmap convertedBitmap = bitmaps.second;

            //     Bitmap bitmap = getBitmapFromView(layoutEmojiCreation);

// Agrega el bitmap convertido al array
            capturedImages.add(convertedBitmap);
//            Bitmap image = captureFrameLayout(layoutEmojiCreation);
//            capturedImages.add(image);
            int finalI = currentFrame;
// Define el número total deseado
            int total = 100;

// Luego, en tu código donde actualizas el progreso y el texto:
            runOnUiThread(() -> {
              //  Log.e("TAG", "captureMultipleImages2: " + currentFrame);

                capturedImageView.setImageBitmap(originalBitmap);

                // Calcula el progreso actual basado en los elementos actuales y el número total deseado
                int progress = (currentFrame * total) / 50; // Suponiendo que tienes 50 elementos

                progressdialog.setProgress(progress);

                // Muestra el texto con el progreso actual y el número total deseado
                texto.setText(progress + "/" + total);
            });


            currentFrame++;
            if(currentFrame==50){
                runOnUiThread(() -> {
                    textotitulo.setText("Convirtiendo archivo");
                    texto.setText("Espera...");
                });
            }

            // Programar la captura del siguiente fotograma
            captureHandler.postDelayed(() -> captureNextFrame(layoutEmojiCreation, capturedImages, capturedImageView), FRAME_DELAY_MS);
        } else {
//            lodinglottie.setVisibility(View.VISIBLE);
//            lodinglottie.setAnimation(R.raw.loading);
//
//            lodinglottie.playAnimation();


            runOnUiThread(() -> {
                marca.setVisibility(View.INVISIBLE);

            });


            final File[] file = new File[1];
            //  guardarbtn.setVisibility(View.VISIBLE);
            // Resto de tu código para generar el GIF y mostrarlo en DisplayBitmapActivity
            String gifPath = new File(getFilesDir(), "generatedGif_" + System.currentTimeMillis() + ".gif").getAbsolutePath();

            //  file[0] = FileUtil.onCreateWebpFile(capturedImages, MainActivity.this, getFilesDir() + "/stickers");


            file[0] = generateGIF(capturedImages, gifPath);

            MainActivity tuClase = new MainActivity();
            Bitmap[] bitmapArray = capturedImages.toArray(new Bitmap[capturedImages.size()]);
            Log.e(TAG, "captureNextFrame: "+capturedImages.size()+" otro"+bitmapArray.length );
            String webpPath = new File(getFilesDir()+ "/stickers/", "AImage" + System.currentTimeMillis() + ".webp").getAbsolutePath();

            File webpfilePath = new File(getFilesDir()+ "/stickers/", "AImage" + System.currentTimeMillis() + ".webp");

            int result = tuClase.convertFrameToWebP(bitmapArray, webpPath, 1);
            if (result == 0) {
                progreso.setVisibility(View.INVISIBLE);
                alertDialog.setTitle("Guardado correctamente");
                // sticker.setImageURI(webpPath);
                Uri uri = Uri.fromFile(new File(webpfilePath.getAbsolutePath()));


                sticker.setImageURI(uri);

                Log.e(TAG, "path: "+Uri.parse(webpfilePath.getAbsolutePath()) );

//
                capturedImageView.setVisibility(View.INVISIBLE);
                sticker.setVisibility(View.VISIBLE);

                Toast.makeText(this, "Conversión exitosa. WebP guardado en: " + gifPath, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Error en la conversión: " + result, Toast.LENGTH_SHORT).show();
            }

            capturedImages.clear();
        }
    }
    public File generateGIF(ArrayList<Bitmap> frames, String path) {
        if (frames.size() == 0) return null;

        int width = frames.get(0).getWidth();
        int height = frames.get(0).getHeight();

        GifEncoder encoder = new GifEncoder();
        try {
            encoder.init(width, height, path, GifEncoder.EncodingType.ENCODING_TYPE_SIMPLE_FAST);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Configurar la velocidad de reproducción del GIF (en milisegundos)
        int frameDelay = FRAME_DELAY_MS; // 30 FPS (ajusta según tu FPS deseado)

        for (Bitmap bitmap : frames) {
            encoder.encodeFrame(bitmap, 55);
            //   Log.e(TAG, "generateGIF: " );
        }

        encoder.close();
        // Devolver el archivo del GIF generado
        return new File(path);
    }

}
