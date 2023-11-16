package com.emojixer.activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;
import static com.airbnb.lottie.LottieDrawable.INFINITE;
import static com.emojixer.functions.UIMethods.shadAnim;
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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;

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
import com.emojixer.functions.RequestNetwork;
import com.emojixer.functions.RequestNetworkController;
import com.emojixer.functions.offsetItemDecoration;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


public class MainActivity extends AppCompatActivity {

    private AlertDialog alertDialog;
    private AlertDialog alertDialogemojis;
    private AlertDialog alertDialogemojis2;
    String extension = "";
    LottieAnimationView lodinglottie;
    ProgressBar progressdialog;
    ImageView capturedImageView;
    TextView texto, textotitulo;
    FrameLayout guardarbtn, progreso;
    private LottieAnimationView progressBar;
    public static final int NUM_FRAMES = 38; // Número total de fotogramas
    public static final int FRAME_DELAY_MS = 20; // Delay entre fotogramas en milisegundos
    public WrapContentDraweeView sticker;

    private int currentFrame = 0;
    private final ArrayList<Bitmap> capturedImages = new ArrayList<>();
    private final ArrayList<Bitmap> capturedImages2 = new ArrayList<>();

    private final Handler captureHandler = new Handler();

    static {
        System.loadLibrary("gif_encoder");
    }

    public native int convertFrameToWebP(Bitmap[] bitmaps, String outputPath, int quality);

    private LottieAnimationView saveEmoji;
    private WrapContentDraweeView mixedEmoji0;
    LottieAnimationView mixedfondo, mixedEmojiojos_objetos, mixedEmoji, mixedEmojiojos, mixedEmojibocas, mixedEmojiobjetos, mixedEmojimanos2, mixedEmojimanos, mixedemojiforma, marca;
    private String emote1;
    private String emote2;
    private String idemote1;
    private int idemoji1, idemoji2;
    private LottieAnimationView mas;

    private String idemote2;
    private String finalEmojiURL;
    private String ojosfinal, bocafinal, ojosobjetosfinal, objetosfinal, manosfinal, anchofinal, leftfinal, topfinal, tipofinal, extrafinal, fondofinal, rotacionfinal, animacionfinal;

    private RecyclerView emojisSlider1;
    private RecyclerView dialogemojisSlider1, dialogemojisSlider2;
    private RecyclerView emojisSlider2;
    private ArrayList<HashMap<String, Object>> supportedEmojisList = new ArrayList<>();
    private RequestNetwork requestSupportedEmojis;
    public static int posicionX = 245;
    private RequestNetwork.RequestListener requestSupportedEmojisListener;
    private SharedPreferences sharedPref;
    private boolean isFineToUseListeners = false;
    private LinearLayoutManager emojisSlider1LayoutManager;
    private LinearLayoutManager emojisSlider2LayoutManager;
    private final SnapHelper emojisSlider1SnapHelper = new LinearSnapHelper();
    private final SnapHelper emojisSlider2SnapHelper = new LinearSnapHelper();
    FrameLayout layoutEmojiCreation;
    FrameLayout posicioncara;
    FrameLayout posicionemoji;
    FrameLayout posicionem;
    String dateTimeKey;
    Activity context;
    long days;
    ReviewInfo reviewInfo;
    public static String APITOP = "http://animated.emojixer.com/panel/apitop.php?";
    private AdView adView;
    AtomicInteger operationsCompleted = new AtomicInteger();
    final int TOTAL_OPERATIONS = 6; // Ajusta este número según cuántas operaciones estás realizando.
    HashMap<String, LottieComposition> compositionsMap = new HashMap<>();
    boolean restraso = false;
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

        if (days <= 0) {
            Date dateStr = Calendar.getInstance().getTime();
            sharedPref.edit().putLong(dateTimeKey, dateStr.getTime()).apply();
        }


//        getemojis();
        context = this;
        idemoji1 = 20;
        idemoji2 = 12;
        LOGIC_BACKEND();
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
        Log.e(TAG, "onCreate: entro");
    }


    private void initreview() {
        ReviewManager manager = ReviewManagerFactory.create(this);
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // We can get the ReviewInfo object
                reviewInfo = task.getResult();
            }  // There was some problem, log or handle the error code.

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
        mixedEmoji0 = findViewById(R.id.mixedEmoji0);

        mixedEmojiojos = findViewById(R.id.mixedEmojiojos);
        mixedEmojiojos_objetos = findViewById(R.id.mixedEmojiojos_objetos);
        mixedEmojibocas = findViewById(R.id.mixedEmojibocas);
        mixedEmojiobjetos = findViewById(R.id.mixedEmojiobjetos);
        mixedEmojimanos = findViewById(R.id.mixedEmojimanos);
        mixedEmojimanos2 = findViewById(R.id.mixedEmojimanos2);

        mixedemojiforma = findViewById(R.id.emojiforma);
        mixedfondo = findViewById(R.id.emojifondo);
        mas = findViewById(R.id.iconmas);
        marca = findViewById(R.id.marca);

        saveEmoji = findViewById(R.id.saveEmoji);
        saveEmoji.playAnimation();

        emojisSlider1 = findViewById(R.id.emojisSlider1);
        emojisSlider2 = findViewById(R.id.emojisSlider2);
        requestSupportedEmojis = new RequestNetwork(this);
        sharedPref = getSharedPreferences("AppData", Activity.MODE_PRIVATE);
        layoutEmojiCreation = findViewById(R.id.frame_emoji_creation);
        posicioncara = findViewById(R.id.posicioncara);
        posicionem = findViewById(R.id.posicione);
        configurarDialogoEmojis();


        Button emojitop = findViewById(R.id.emojitop);
        emojitop.setOnClickListener(view -> {
            //  MiPublicidad.verInterstitialAd(this);
            MainActivity.this.startActivity(new Intent(MainActivity.this.getApplicationContext(), Emojitop.class));

            Log.e(TAG, "configurar initlogic: entro");

        });

        posicionemoji = findViewById(R.id.posicionemoji);

        saveEmoji.setOnClickListener(view -> {

            //   MiPublicidad.verInterstitialAd(this);
        //    Date today = new Date();
       //     long diff = today.getTime() - days;
       //     int numOfDays = 2;
//            int hours = (int) (diff / (1000 * 60 * 60));
//            int minutes = (int) (diff / (1000 * 60));
         //   Log.e("TAG", "aki entro dias: " + numOfDays);

//            if (numOfDays >= 1 && numOfDays < 5) {
//                Log.e("TAG", "aki entro review: ");
//                initreview();
//
//            }

            //  Log.e("TAG", "aki min dias: "+days+" actual "+ minutes );
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                captureMultipleImages(layoutEmojiCreation);
                String toastText = "Saved emoji";
                Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_LONG).show();
                actualizovotos();
            } else {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    String toastText = "Saved emoji";
                    Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_LONG).show();
                    captureMultipleImages(layoutEmojiCreation);
                    actualizovotos();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
            }
        });



        requestSupportedEmojisListener = new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response, HashMap<String, Object> responseHeaders) {
                try {
                    Log.e(TAG, "onResponse: addslide ");
                    sharedPref.edit().putString("supportedEmojisList", response).apply();
                    addDataToSliders(response);
                } catch (Exception ignored) {
                    Log.e(TAG, "conexion error0: entro ");

                }
            }

            @Override
            public void onErrorResponse(String tag, String message) {
                Log.e(TAG, "conexion error1: entro " + message);
                reiniciarActividad();
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
        String fondoTOP = "";
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


            if (fondofinal != null)
                fondoTOP = obtenerNombreArchivo(fondofinal);

            String rotacionTOP = rotacionfinal;
            if (Objects.equals(rotacionTOP, "images_formas"))
                rotacionTOP = "";
            String animacionTOP = animacionfinal;
            if (Objects.equals(animacionTOP, "images_formas"))
                animacionTOP = "";

            Log.e(TAG, "actualizovotos: cosas " + tipofinal);

            RequestQueue queue = Volley.newRequestQueue(context);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, APITOP + "emoji1=" + idemote1 + "&" + "emoji2=" + idemote2 + "&tipoTOP=" + tipoTOP + "&ojosTOP=" + ojosTOP + "&baseTOP=" + baseTOP + "&ojosobjetosTOP=" + ojos_objetosTOP + "&bocaTOP=" + bocaTOP + "&objetosTOP=" + objetosTOP + "&manosTOP=" + manosTOP + "&anchoTOP=" + anchoTOP + "&leftTOP=" + leftTOP + "&topTOP=" + topTOP + "&extraTOP=" + extraTOP + "&fondoTOP=" + fondoTOP + "&rotacionTOP=" + rotacionTOP + "&animacionTOP=" + animacionTOP,
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
        Log.e(TAG, "conexion logicback: entro ");
        LinearLayoutManager emojisSlider1ManagerDialog = new GridLayoutManager(this, 4);

        setSnapHelper(dialogemojisSlider1, emojisSlider1SnapHelper, emojisSlider1ManagerDialog);
        dialogemojisSlider1.setLayoutManager(emojisSlider1ManagerDialog);
        dialogemojisSlider1.setAdapter(new EmojimixerAdapter(supportedEmojisList, emojisSlider1ManagerDialog, MainActivity.this));
        LinearLayoutManager emojisSlider2ManagerDialog = new GridLayoutManager(this, 4);

        setSnapHelper(dialogemojisSlider2, emojisSlider1SnapHelper, emojisSlider2ManagerDialog);
        dialogemojisSlider2.setLayoutManager(emojisSlider2ManagerDialog);

        dialogemojisSlider2.setAdapter(new EmojimixerAdapter(supportedEmojisList, emojisSlider2ManagerDialog, MainActivity.this));


        emojisSlider1LayoutManager = new CenterZoomLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        emojisSlider2LayoutManager = new CenterZoomLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        setSnapHelper(emojisSlider1, emojisSlider1SnapHelper, emojisSlider1LayoutManager);
        setSnapHelper(emojisSlider2, emojisSlider2SnapHelper, emojisSlider2LayoutManager);

        emojisSlider1.setLayoutManager(emojisSlider1LayoutManager);
        emojisSlider2.setLayoutManager(emojisSlider2LayoutManager);

        emojisSlider1.addItemDecoration(new offsetItemDecoration(this));
        emojisSlider2.addItemDecoration(new offsetItemDecoration(this));


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

        Log.e(TAG, "addDataToSliders: entro");
        isFineToUseListeners = false;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            supportedEmojisList = new Gson().fromJson(data, new TypeToken<ArrayList<HashMap<String, Object>>>() {
            }.getType());
            handler.post(() -> {
                emojisSlider1.setAdapter(new EmojimixerAdapter(supportedEmojisList, emojisSlider1LayoutManager, MainActivity.this));
                emojisSlider2.setAdapter(new EmojimixerAdapter(supportedEmojisList, emojisSlider2LayoutManager, MainActivity.this));

                new Handler().postDelayed(() -> {
                    for (int i = 0; i < 2; i++) {
                        Random rand = new Random();
                      //  int randomNum = rand.nextInt((supportedEmojisList.size()) - 1);
                        if (i == 0) {

                            int centerOfScreen = emojisSlider1.getWidth() / 2;
                            int ancho = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics());

                            emojisSlider1LayoutManager.scrollToPositionWithOffset(idemoji1, centerOfScreen + ancho);

                        } else {


                            emojisSlider2.smoothScrollToPosition(idemoji2);
                        }
                    }

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
                    Log.e("TAG", "addDataToSliders1: " + idemote1 + " dos" + idemote2);

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
                    Log.e("TAG", "addDataToSliders2: " + idemote1 + " dos" + idemote2);

                }
            }
        });
    }


    private void mixEmojis(String emoji1, String emoji2, String date) {

        shouldEnableSave(false);
        progressBar.setVisibility(View.GONE);
        Log.e("TAG", "addDataToSliders: " + emoji1 + " dos" + idemote2);
        if (TextUtils.isEmpty(idemote1)) {
            idemote1 = "26";
            Log.e("TAG", "addDataToSliders entrr: " + emoji1 + " dos" + idemote2);


        }
        if (TextUtils.isEmpty(idemote2)) {
            idemote2 = "12";
            Log.e("TAG", "addDataToSliders entrr: " + emoji1 + " dos" + idemote2);


        }
        EmojiMixer em = new EmojiMixer(emoji1, emoji2, idemote2, idemote1, date, this, new EmojiMixer.EmojiListener() {
            Animation animation;

            @Override
            public void onSuccess(String emojiUrl, String ojos, String cejas, String objetos, String bocas, String finalojos_objetos, String manos, int ancho, int left, int top, String tipo, String extra, String fondo, float rotacion, int random, String animacion) {

              //  Log.e("TAG", "aki datos api: " + emojiUrl + " ojos" + ojos + objetos + bocas + fondo);

                shouldEnableSave(true);
                mixedEmojibocas.setVisibility(View.VISIBLE);
                mixedEmojiojos.setVisibility(View.VISIBLE);
                mixedEmojimanos.setVisibility(View.VISIBLE);
                mixedEmojiojos_objetos.setVisibility(View.VISIBLE);
                mixedEmojiobjetos.setVisibility(View.VISIBLE);
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
                Log.e(TAG, "onSuccess:aki datos "+idemote1+"  "+idemote2 );

                if (animacion != null & tipofinal != null) {
// Obtener el identificador de recurso entero para la animación
                    int animationResourceId = context.getResources().getIdentifier(animacion, "anim", context.getPackageName());

                    if (animationResourceId != 0) {
                        // Cargar la animación si se encontró el recurso
                        animation = AnimationUtils.loadAnimation(context, animationResourceId);
                        Log.e(TAG, "onSuccess: aki anima " + animation);
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
                } else {
                    //     posicioncara.setRotation(rotacion);


                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.animacion);

                    posicionemoji.startAnimation(animation);
                    posicionemoji.clearAnimation();
                    posicionemoji.setRotation(rotacion);
                    // Manejar el caso en el que la animación no se encuentra en los recursos
                    // Aquí puedes mostrar un mensaje de error o tomar otra acción apropiada.
                }

                if (Objects.equals(tipofinal, "objeto")) {
                    Log.e("TAG", "aki condiciones objeto 1: " + tipofinal);

                    if (Objects.equals(idemote1, idemote2)) {
                        mixedemojiforma.setVisibility(View.VISIBLE);
                        mixedemojiforma.setImageURI(Uri.parse(extra));
                        mixedfondo.setVisibility(View.VISIBLE);

                        LottieCompositionFactory.fromUrl(context, String.valueOf(Uri.parse(fondo)))
                                .addListener(composition -> {
                                    mixedfondo.setComposition(composition);
                                    mixedfondo.playAnimation();
                                }).addFailureListener(exception -> {
                                    // Si falla cargar desde la URL, carga el archivo local
                                    //    Log.e("TAG", "aki emoji final2: "+String.valueOf(Uri.parse(finalojos_objetos)) );
                                    mixedfondo.setVisibility(View.INVISIBLE);
                                    mixedfondo.setAnimation(R.raw.vacio);
                                });
                        Log.e("TAG", "aki condiciones 2 objeto igual: " + extra);
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

                        mixedemojiforma.setVisibility(View.VISIBLE);
                        posicioncara.setRotation(0);

                        posicioncara.setLayoutParams(new FrameLayout.LayoutParams(ancho2, ancho2));
                        ViewGroup.MarginLayoutParams params2 = (ViewGroup.MarginLayoutParams) posicioncara.getLayoutParams();
                        params.setMargins(left2, top2, 0, 0);
                        posicioncara.setLayoutParams(params2);
                        int lastIndexOfDot = finalEmojiURL.lastIndexOf('.');
                        if (lastIndexOfDot != -1) {
                            extension = finalEmojiURL.substring(lastIndexOfDot + 1);
                        }
                        if ("png".equals(extension)) {
                            mixedEmoji0.setVisibility(View.VISIBLE);

                        } else {
                            //    Log.e("xomplete1", "aki base ");
                            mixedEmoji0.setVisibility(View.GONE);
                        }

                        LottieCompositionFactory.fromUrl(context, String.valueOf(Uri.parse(extra)))
                                .addListener(composition -> {

                                    mixedemojiforma.setComposition(composition);
                                    mixedemojiforma.playAnimation();
                                })
                        ;
                        mixedfondo.setVisibility(View.VISIBLE);
                        LottieCompositionFactory.fromUrl(context, String.valueOf(Uri.parse(fondo)))
                                .addListener(composition -> {
                                    mixedfondo.setComposition(composition);
                                    mixedfondo.playAnimation();
                                }).addFailureListener(exception -> {
                                    // Si falla cargar desde la URL, carga el archivo local
                                    //    Log.e("TAG", "aki emoji final2: "+String.valueOf(Uri.parse(finalojos_objetos)) );
                                    mixedfondo.setVisibility(View.INVISIBLE);
                                    mixedfondo.setAnimation(R.raw.vacio);
                                });
                        ;
                        // mixedemojiforma.setImageURI(Uri.parse(extra));


                        mixedemojiforma.setLayoutParams(new FrameLayout.LayoutParams(ancho3, ancho3));
                        ViewGroup.MarginLayoutParams params4 = (ViewGroup.MarginLayoutParams) mixedemojiforma.getLayoutParams();
                        params4.setMargins(0, 0, 0, 0);
                        mixedemojiforma.setLayoutParams(params4);
                        mixedemojiforma.setRotation(0);


                        Log.e("TAG", "aki condicion objeto: " + finalEmojiURL);

                    }
                } else if (Objects.equals(tipo, "objetodoble")) {
                    int ancho2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, getResources().getDisplayMetrics());
                    int ancho3 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, getResources().getDisplayMetrics());


                    for (LottieAnimationView lottieAnimationView : Arrays.asList(mixedEmojibocas,mixedEmojiojos,mixedEmojimanos, mixedEmojiojos_objetos, mixedEmojiobjetos)) {
                        lottieAnimationView.setVisibility(View.GONE);
                    }

                    posicionemoji.setLayoutParams(new FrameLayout.LayoutParams(ancho2, ancho2));
                   // ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) posicionemoji.getLayoutParams();
                    //  params.setMargins(left2, top2, 0, 0);
                    // posicionemoji.setLayoutParams(params);
                    posicionemoji.setRotation(0);

                    mixedemojiforma.setVisibility(View.VISIBLE);
                    posicioncara.setRotation(0);

                    LottieCompositionFactory.fromUrl(context, extra)
                            .addListener(composition -> {
                                mixedemojiforma.setComposition(composition);
                                mixedemojiforma.playAnimation();
                            })
                    ;
                    mixedfondo.setVisibility(View.VISIBLE);

                    LottieCompositionFactory.fromUrl(context, String.valueOf(Uri.parse(fondo)))
                            .addListener(composition -> {
                                mixedfondo.setComposition(composition);
                                mixedfondo.playAnimation();
                            }).addFailureListener(exception -> {
                                // Si falla cargar desde la URL, carga el archivo local
                                //    Log.e("TAG", "aki emoji final2: "+String.valueOf(Uri.parse(finalojos_objetos)) );
                                mixedfondo.setVisibility(View.INVISIBLE);
                                mixedfondo.setAnimation(R.raw.vacio);
                            });

                    mixedemojiforma.setLayoutParams(new FrameLayout.LayoutParams(ancho3, ancho3));
                    ViewGroup.MarginLayoutParams params4 = (ViewGroup.MarginLayoutParams) mixedemojiforma.getLayoutParams();
                    params4.setMargins(0, 0, 0, 0);
                    // mixedemojiforma.setLayoutParams(params4);
                    mixedemojiforma.setRotation(0);
                    Log.e("TAG", "aki condiciones objeto doble: " + extra);

                } else if (ancho > 0) {


                    //emojis3 iguales y objeto es emojis3 champi frutass....
                    if (Objects.equals(emote1, emote2) && Objects.equals(tipo, "emojis3")) {

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
                        Log.e("TAG", "aki condiciones emojis3: " + ancho);

                    }


                    //emojis iguales y objeto es emojis
                    else if (Objects.equals(emote1, emote2) && Objects.equals(tipo, "emoji")) {

//                        Careta version

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
                        int ancho4 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ancho - 50, getResources().getDisplayMetrics());

                        mixedemojiforma.setLayoutParams(new FrameLayout.LayoutParams(ancho3, ancho3));
                        ViewGroup.MarginLayoutParams params2 = (ViewGroup.MarginLayoutParams) mixedemojiforma.getLayoutParams();
                        params2.setMargins(0, top2, 0, 0);
                        mixedemojiforma.setLayoutParams(params2);
                        mixedemojiforma.setRotation(0);


                        posicionemoji.setLayoutParams(new FrameLayout.LayoutParams(ancho4, ancho4));
                        ViewGroup.MarginLayoutParams params3 = (ViewGroup.MarginLayoutParams) posicionemoji.getLayoutParams();
                        params3.setMargins(left2, top2, 0, 0);
                        posicionemoji.setLayoutParams(params3);
                        //posicionemoji.setVisibility(View.INVISIBLE);

                        posicioncara.setLayoutParams(new FrameLayout.LayoutParams(ancho4, ancho4));
                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) posicioncara.getLayoutParams();
                        params.setMargins(0, 0, 0, 0);
                        posicioncara.setLayoutParams(params);

                        Log.e("TAG", "aki condiciones careta: " + extra);

                        posicioncara.setRotation(0);


                        //version doble emoji
//                        else {
//
//                            mixedemojiforma.setVisibility(View.VISIBLE);
//                            mixedemojiforma.setImageURI(Uri.parse(extra));
//                            int left2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, left, getResources().getDisplayMetrics());
//                            int top2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, top, getResources().getDisplayMetrics());
//                            int ancho3 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ancho, getResources().getDisplayMetrics());
//
//                            mixedemojiforma.setLayoutParams(new FrameLayout.LayoutParams(ancho3-100, ancho3-100));
//                            ViewGroup.MarginLayoutParams params2 = (ViewGroup.MarginLayoutParams) mixedemojiforma.getLayoutParams();
//                            params2.setMargins(ancho3-75, top2+top2, 0, 0);
//                            mixedemojiforma.setLayoutParams(params2);
//                            mixedemojiforma.setRotation(-rotacion);
//
//                            posicionemoji.setLayoutParams(new FrameLayout.LayoutParams(ancho3, ancho3));
//                            ViewGroup.MarginLayoutParams params3 = (ViewGroup.MarginLayoutParams) posicionemoji.getLayoutParams();
//                            params3.setMargins(left2, top2, 0, 0);
//                            posicionemoji.setLayoutParams(params3);
//
//                            posicioncara.setLayoutParams(new FrameLayout.LayoutParams(ancho3, ancho3));
//                            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) posicioncara.getLayoutParams();
//                            params.setMargins(0, 0, 0, 0);
//                            posicioncara.setLayoutParams(params);
//                            posicionemoji.setRotation(rotacion);
//                            posicioncara.setRotation(0);
//
//                            Log.e("TAG", "aki condiciones emoji doble: " + rotacion );
//
//                        }
                    } else {


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
                        Log.e("TAG", "aki condicion normal con ancho: " + ancho);
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


                    Log.e("TAG", "aki condicion normal: " + ancho);
                }


                int lastIndexOfDot = finalEmojiURL.lastIndexOf('.');
                if (lastIndexOfDot != -1) {
                    extension = finalEmojiURL.substring(lastIndexOfDot + 1);
                }
                Log.e("TAG", "onSuccess: aki extension" + extension + finalEmojiURL);


                if ("png".equals(extension)) {
                    mixedEmoji.setVisibility(View.GONE);
                    mixedEmoji0.setVisibility(View.VISIBLE);
                    mixedEmoji0.setImageURI(Uri.parse(finalEmojiURL));

                } else {
                    //    Log.e("xomplete1", "aki base ");

                    mixedEmoji.setVisibility(View.VISIBLE);
                    mixedEmoji0.setVisibility(View.GONE);
                    //     Log.e("TAG", "onSuccess: aki es png no ");
                    LottieCompositionFactory.fromUrl(context, String.valueOf(Uri.parse(finalEmojiURL)))
                            .addListener(composition -> {
                                mixedEmoji.setComposition(composition);
                                mixedEmoji.playAnimation();
                                mixedEmoji.setRepeatCount(INFINITE);
                            })
                            .addFailureListener(exception -> {

                            });
                }


                if (objetos != null) {
                    //   Log.e("xomplete2", "aki pjetos ");

                    LottieCompositionFactory.fromUrl(context, String.valueOf(Uri.parse(objetos)))
                            .addListener(composition -> {
                                mixedEmojiobjetos.setVisibility(View.VISIBLE);

                                mixedEmojiobjetos.setComposition(composition);
                                mixedEmojiobjetos.playAnimation();
                                mixedEmojiobjetos.setRepeatCount(INFINITE);
                            })
                            .addFailureListener(exception -> {
                                mixedEmojiobjetos.setVisibility(View.INVISIBLE);


                            });
                }


                if (bocas != null) {
                    Log.e("xomplete3", "aki bocas ");

                    LottieCompositionFactory.fromUrl(context, String.valueOf(Uri.parse(bocas)))
                            .addListener(composition -> {
                                mixedEmojibocas.setVisibility(View.VISIBLE);

                                mixedEmojibocas.setComposition(composition);
                                mixedEmojibocas.playAnimation();
                                mixedEmojibocas.setRepeatCount(INFINITE);
                            })
                            .addFailureListener(exception -> {

                            });
                }

                if (ojosfinal != null) {
                    Log.e("xomplete4", "aki ojos" + ojosfinal);

                    LottieCompositionFactory.fromUrl(context, String.valueOf(Uri.parse(ojosfinal)))
                            .addListener(composition -> {
                                mixedEmojiojos.setVisibility(View.VISIBLE);

                                mixedEmojiojos.setComposition(composition);
                                mixedEmojiojos.playAnimation();
                                mixedEmojiojos.setRepeatCount(INFINITE);
                                Log.e("xomplete4", "aki ojos compo" + ojosfinal);

                            }).addFailureListener(exception -> {
                                // Si falla cargar desde la URL, carga el archivo local
                                Log.e("xomplete4", "aki ojos fail" + ojosfinal);

                            });
                }


                if (ojosobjetosfinal != null) {
                    Log.e("xomplete5", "aki ojos jetos ");

                    LottieCompositionFactory.fromUrl(context, String.valueOf(Uri.parse(ojosobjetosfinal)))
                            .addListener(composition -> {

                                mixedEmojiojos_objetos.setComposition(composition);
                                mixedEmojiojos_objetos.playAnimation();
                                mixedEmojiojos_objetos.setRepeatCount(INFINITE);
                                mixedEmojiojos_objetos.setVisibility(View.VISIBLE);

                            })
                            .addFailureListener(exception -> {
                                // Si falla cargar desde la URL, carga el archivo local
                                mixedEmojiojos_objetos.setVisibility(View.INVISIBLE);
                                mixedEmojiojos_objetos.setAnimation(R.raw.vacio);

                            });
                }


                if (Objects.equals(idemote1, "26") || Objects.equals(idemote2, "26")) {
                    //   Log.e("xomplete6", "aki manos 26."+manos);

                    mixedEmojimanos2.setVisibility(View.VISIBLE);
                    //    mixedEmojimanos2.setImageURI(Uri.parse(manos));
                    mixedEmojimanos.setVisibility(View.GONE);


                    LottieCompositionFactory.fromUrl(context, String.valueOf(Uri.parse(manos)))
                            .addListener(composition -> {


                                mixedEmojimanos2.setComposition(composition);
                                mixedEmojimanos2.playAnimation();
                                mixedEmojimanos2.setRepeatCount(INFINITE);
                            }).addFailureListener(exception -> {
                                // Si falla cargar desde la URL, carga el archivo local
                                mixedEmojimanos2.setAnimation(R.raw.vacio);
                                mixedEmojimanos2.setVisibility(View.GONE);
                                //   mixedEmojimanos2.playAnimation();

                            });

                } else {
                    mixedEmojimanos.setVisibility(View.VISIBLE);
                    //   mixedEmojimanos.setImageURI(Uri.parse(manos));
                    mixedEmojimanos2.setVisibility(View.GONE);

                    //   Log.e("xcomplete6", "aki manos."+manos);

                    LottieCompositionFactory.fromUrl(context, String.valueOf(Uri.parse(manos)))
                            .addListener(composition -> {

                                mixedEmojimanos.setComposition(composition);
                                mixedEmojimanos.playAnimation();
                                mixedEmojimanos.setRepeatCount(INFINITE);
                            }).addFailureListener(exception -> {
                                // Si falla cargar desde la URL, carga el archivo local
                                mixedEmojimanos.setAnimation(R.raw.vacio);
                                mixedEmojimanos.setVisibility(View.GONE);
                                //     mixedEmojimanos.playAnimation();

                            });
                }


                isFineToUseListeners = true;
                shouldShowEmoji(true);

            }


            @Override
            public void onFailure(String failureReason) {
                shouldEnableSave(false);

            }
        });
        Thread thread = new Thread(em);
        thread.start();
        Log.e("TAG", "aki resultado operaciones: " + operationsCompleted.get());

    }

    private void shouldShowEmoji(boolean shouldShow) {


        if (shouldShow) {
            //  Log.e("TAG", "aki entro:+finalEmojiURL " );
            shadAnim(layoutEmojiCreation, "scaleY", 1, 400, 50);
            shadAnim(layoutEmojiCreation, "scaleX", 1, 400, 50);
            shadAnim(layoutEmojiCreation, "alpha", 1f, 800, 50); // Cambia el valor "0.5f" según tus necesidades
        } else {
            //    Log.e("TAG", "aki entro2:+finalEmojiURL " );
            mas.playAnimation();
            shadAnim(layoutEmojiCreation, "scaleY", 0, 400, 0);
            shadAnim(layoutEmojiCreation, "scaleX", 0, 400, 0);
            shadAnim(layoutEmojiCreation, "alpha", 0.0f, 800, 0); // Cambia el valor "0.5f" según tus necesidades

        }
    }

    private void shouldEnableSave(boolean shouldShow) {

        if (shouldShow) {
            new Handler().postDelayed(() -> {
                saveEmoji.setEnabled(true);
            }, 1000);
        } else {
            saveEmoji.setEnabled(false);

        }
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

    private void reiniciarActividad() {
        // En versiones anteriores a Android 11, puedes reiniciar la actividad de la siguiente manera:
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }


    public void abriremojis(View view) {
        //    MiPublicidad.verInterstitialAd(this);

        MainActivity.this.startActivity(new Intent(MainActivity.this.getApplicationContext(), ImageViewerActivity.class));

    }

    public void captureMultipleImages(FrameLayout layoutEmojiCreation) {
        // ... (código para configurar el AlertDialog y las vistas)
        currentFrame = 0;


        alertDialog.show();
        mixedEmojiojos_objetos.setProgress(0f); // Establece el progreso al principio
        mixedEmojiojos_objetos.getDuration();

        Log.e(TAG, "captureMultipleImages: duracion ojos objeto " + mixedEmojiojos_objetos.getFrame() + "| objetos " + mixedEmojiobjetos.getDuration() + "| ojos " + mixedEmojiojos.getDuration() + "| bocas" + mixedEmojibocas.getDuration() + "| base" + mixedEmoji.getDuration() + " | manos" + mixedEmojimanos.getDuration() + "| forma " + mixedemojiforma.getDuration());

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


        Log.e("TAG", "captureMultipleImages3: " + capturedImages);
        progreso.setVisibility(View.VISIBLE);
        guardarbtn.setVisibility(View.INVISIBLE);
        textotitulo.setText("Capturando fotogramas");
        alertDialog.setTitle("Guardar emoji");

        // Luego, cuando estés listo para mostrar el GIF, puedes hacerlo aquí

        runOnUiThread(() -> {

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

    private void captureNextFrame(FrameLayout layoutEmojiCreation, ArrayList<Bitmap> capturedImages,
                                  ImageView capturedImageView) {
        capturedImageView.setVisibility(View.VISIBLE);
        sticker.setVisibility(View.INVISIBLE);
        if (currentFrame < NUM_FRAMES) {

            Pair<Bitmap, Bitmap> bitmaps = FileUtil.captureFrameLayout(layoutEmojiCreation);
            Bitmap originalBitmap = bitmaps.first;
            Bitmap convertedBitmap = bitmaps.second;
// Define el número total deseado
            int total = 100;

// Luego, en tu código donde actualizas el progreso y el texto:
            runOnUiThread(() -> {
                //  Log.e("TAG", "captureMultipleImages2: " + currentFrame);
                capturedImages.add(convertedBitmap);
                capturedImages2.add(originalBitmap);

                capturedImageView.setImageBitmap(originalBitmap);

                // Calcula el progreso actual basado en los elementos actuales y el número total deseado
                int progress = (currentFrame * total) / 38; // Suponiendo que tienes 50 elementos

                progressdialog.setProgress(progress);

                // Muestra el texto con el progreso actual y el número total deseado
                texto.setText(progress + "/" + total);
            });


            currentFrame++;
            if (currentFrame == NUM_FRAMES) {
                runOnUiThread(() -> {
                    textotitulo.setText("Convirtiendo archivo");
                    texto.setText("Espera...");
                });
            }

            // Programar la captura del siguiente fotograma
            captureHandler.postDelayed(() -> captureNextFrame(layoutEmojiCreation, capturedImages, capturedImageView), FRAME_DELAY_MS);
        } else {

            runOnUiThread(() -> {
                marca.setVisibility(View.INVISIBLE);

            });


            final File[] file = new File[1];
            String baseFileName = "AImage" + System.currentTimeMillis();
            String webpPath = new File(getFilesDir() + "/stickers/", baseFileName + ".webp").getAbsolutePath();
            String gifPath = new File(getFilesDir() + "/", baseFileName + ".gif").getAbsolutePath();
            file[0] = generateGIF(capturedImages2, gifPath);
            MainActivity tuClase = new MainActivity();
            Bitmap[] bitmapArray = capturedImages.toArray(new Bitmap[capturedImages.size()]);
          //  Log.e(TAG, "captureNextFrame: " + capturedImages.size() + " otro" + bitmapArray.length);

            int quality;
            if (animacionfinal != null)
                quality = 30;
            else quality = 57;
            int result = tuClase.convertFrameToWebP(bitmapArray, webpPath, quality);
            if (result == 0) {
                progreso.setVisibility(View.INVISIBLE);
                alertDialog.setTitle("Emoji Guardado");
                // sticker.setImageURI(webpPath);
                Uri uri = Uri.fromFile(new File(webpPath));
                sticker.setImageURI(uri);
                capturedImageView.setVisibility(View.INVISIBLE);
                sticker.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Conversión exitosa. WebP guardado en: " + gifPath, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Error en la conversión: " + result, Toast.LENGTH_SHORT).show();
            }

            capturedImages.clear();
            capturedImages2.clear();
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
