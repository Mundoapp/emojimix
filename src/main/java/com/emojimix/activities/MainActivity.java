package com.emojimix.activities;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;
import static com.emojimix.functions.UIMethods.shadAnim;
import static com.emojimix.functions.Utils.getRecyclerCurrentItem;
import static com.emojimix.functions.Utils.setSnapHelper;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.emojimix.R;
import com.emojimix.adapters.EmojisSliderAdapter;
import com.emojimix.functions.CenterZoomLayoutManager;
import com.emojimix.functions.EmojiMixer;
import com.emojimix.functions.FileUtil;
import com.emojimix.functions.RequestNetwork;
import com.emojimix.functions.RequestNetworkController;
import com.emojimix.functions.offsetItemDecoration;
import com.emojimix.mipublicidad.Admob;
import com.emojimix.mipublicidad.MiPublicidad;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

public class MainActivity extends AppCompatActivity {
    private Button saveEmoji;
    private ImageView mixedEmoji;
    private CircularProgressIndicator progressBar;
    private TextView activityDesc;
    private String emote1;
    private String emote2;
    private String finalEmojiURL;
    private RecyclerView emojisSlider1;
    private RecyclerView emojisSlider2;
    private ArrayList<HashMap<String, Object>> supportedEmojisList = new ArrayList<>();
    private RequestNetwork requestSupportedEmojis;
    private RequestNetwork.RequestListener requestSupportedEmojisListener;
    private SharedPreferences sharedPref;
    private boolean isFineToUseListeners = false;
    private LinearLayoutManager emojisSlider1LayoutManager;
    private LinearLayoutManager emojisSlider2LayoutManager;
    private SnapHelper emojisSlider1SnapHelper = new LinearSnapHelper();
    private SnapHelper emojisSlider2SnapHelper = new LinearSnapHelper();
    FrameLayout layoutEmojiCreation;
    Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLogic();
        LOGIC_BACKEND();

        context = this;

        MiPublicidad.Init(this);

        Fresco.initialize(this);
        MiPublicidad.baner( (FrameLayout) findViewById(R.id.ad_view_container), this);

//        OneSignal.initWithContext(this);
//        OneSignal.setAppId("399b14d7-4c2a-4ce3-ab00-ce6358a44afa");

    }

    public void initLogic() {
        progressBar = findViewById(R.id.progressBar);
         mixedEmoji = findViewById(R.id.mixedEmoji);
        saveEmoji = findViewById(R.id.saveEmoji);
        emojisSlider1 = findViewById(R.id.emojisSlider1);
        emojisSlider2 = findViewById(R.id.emojisSlider2);
        requestSupportedEmojis = new RequestNetwork(this);
        sharedPref = getSharedPreferences("AppData", Activity.MODE_PRIVATE);
        layoutEmojiCreation = (FrameLayout) findViewById(R.id.frame_emoji_creation);

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
                } else {
                    random2 = randomNum;
                    emojisSlider2.smoothScrollToPosition(randomNum);
                }
            }

            emote1 = Objects.requireNonNull(supportedEmojisList.get(random1).get("emojiUnicode")).toString();
            emote2 = Objects.requireNonNull(supportedEmojisList.get(random2).get("emojiUnicode")).toString();
            mixEmojis(emote1, emote2, Objects.requireNonNull(supportedEmojisList.get(random1).get("date")).toString());
        });

        saveEmoji.setOnClickListener(view -> {

            MiPublicidad.verInterstitialAd(this);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

                File file = FileUtil.saveEmojiBIG(context, this.layoutEmojiCreation, context.getFilesDir() + "/stickers/");
                String toastText = "Saved emoji";
                Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_LONG).show();
             //   downloadFile(finalEmojiURL);
            } else {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    String toastText = "Saved emoji";
                    Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_LONG).show();
                    File file = FileUtil.saveEmojiBIG(context, this.layoutEmojiCreation, context.getFilesDir() + "/stickers/");

                  //  downloadFile(finalEmojiURL);
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
            }

        });

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
        emojisSlider1LayoutManager = new CenterZoomLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        emojisSlider2LayoutManager = new CenterZoomLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        setSnapHelper(emojisSlider1, emojisSlider1SnapHelper, emojisSlider1LayoutManager);
        setSnapHelper(emojisSlider2, emojisSlider2SnapHelper, emojisSlider2LayoutManager);

        emojisSlider1.setLayoutManager(emojisSlider1LayoutManager);
        emojisSlider2.setLayoutManager(emojisSlider2LayoutManager);

        emojisSlider1.addItemDecoration(new offsetItemDecoration(this));

        if (sharedPref.getString("supportedEmojisList", "").isEmpty()) {
            requestSupportedEmojis.startRequestNetwork(RequestNetworkController.GET, "http://emojimix.appsloki.com/supported_emojis.json", "", requestSupportedEmojisListener);
        } else {
            addDataToSliders(sharedPref.getString("supportedEmojisList", ""));
        }
    }


    private void addDataToSliders(String data) {
        isFineToUseListeners = false;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            supportedEmojisList = new Gson().fromJson(data, new TypeToken<ArrayList<HashMap<String, Object>>>() {
            }.getType());
            handler.post(() -> {
                emojisSlider1.setAdapter(new EmojisSliderAdapter(supportedEmojisList, emojisSlider1LayoutManager, MainActivity.this));
                emojisSlider2.setAdapter(new EmojisSliderAdapter(supportedEmojisList, emojisSlider2LayoutManager, MainActivity.this));
                new Handler().postDelayed(() -> {
                    for (int i = 0; i < 2; i++) {
                        Random rand = new Random();
                        int randomNum = rand.nextInt((supportedEmojisList.size()) - 1);
                        if (i == 0) {
                            emojisSlider1.smoothScrollToPosition(randomNum);
                        } else {
                            emojisSlider2.smoothScrollToPosition(randomNum);
                        }
                    }

                    shouldShowEmoji(false);
                    emote1 = Objects.requireNonNull(supportedEmojisList.get(getRecyclerCurrentItem(emojisSlider1, emojisSlider1SnapHelper, emojisSlider1LayoutManager)).get("emojiUnicode")).toString();
                    emote2 = Objects.requireNonNull(supportedEmojisList.get(getRecyclerCurrentItem(emojisSlider2, emojisSlider2SnapHelper, emojisSlider2LayoutManager)).get("emojiUnicode")).toString();
                    mixEmojis(emote1, emote2, Objects.requireNonNull(supportedEmojisList.get(getRecyclerCurrentItem(emojisSlider1, emojisSlider1SnapHelper, emojisSlider1LayoutManager)).get("date")).toString());
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
                    emote1 = Objects.requireNonNull(supportedEmojisList.get(getRecyclerCurrentItem(emojisSlider1, emojisSlider1SnapHelper, emojisSlider1LayoutManager)).get("emojiUnicode")).toString();
                    shouldShowEmoji(false);
                    mixEmojis(emote1, emote2, Objects.requireNonNull(supportedEmojisList.get(getRecyclerCurrentItem(emojisSlider1, emojisSlider1SnapHelper, emojisSlider1LayoutManager)).get("date")).toString());
                }
            }
        });

        emojisSlider2.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (isFineToUseListeners && newState == SCROLL_STATE_IDLE) {
                    emote2 = Objects.requireNonNull(supportedEmojisList.get(getRecyclerCurrentItem(emojisSlider2, emojisSlider2SnapHelper, emojisSlider2LayoutManager)).get("emojiUnicode")).toString();
                    shouldShowEmoji(false);
                    mixEmojis(emote1, emote2, Objects.requireNonNull(supportedEmojisList.get(getRecyclerCurrentItem(emojisSlider2, emojisSlider2SnapHelper, emojisSlider2LayoutManager)).get("date")).toString());
                }
            }
        });
    }


    private void mixEmojis(String emoji1, String emoji2, String date) {
        shouldEnableSave(false);
        progressBar.setVisibility(View.VISIBLE);

        EmojiMixer em = new EmojiMixer(emoji1, emoji2, date, this, new EmojiMixer.EmojiListener() {
            @Override
            public void onSuccess(String emojiUrl) {
                finalEmojiURL = emojiUrl;
                shouldEnableSave(true);
                setImageFromUrl(mixedEmoji, emojiUrl);
            }

            @Override
            public void onFailure(String failureReason) {
              //  changeActivityDesc(failureReason);
                shouldEnableSave(false);
                mixedEmoji.setImageResource(R.drawable.sad);
                shouldShowEmoji(true);
            }
        });
        Thread thread = new Thread(em);
        thread.start();
    }


    private void shouldShowEmoji(boolean shouldShow) {
        isFineToUseListeners = true;
        if (shouldShow) {
            shadAnim(mixedEmoji, "scaleY", 1, 300);
            shadAnim(mixedEmoji, "scaleX", 1, 300);
            shadAnim(progressBar, "scaleY", 0, 300);
            shadAnim(progressBar, "scaleX", 0, 300);
        } else {
            shadAnim(mixedEmoji, "scaleY", 0, 300);
            shadAnim(mixedEmoji, "scaleX", 0, 300);
            shadAnim(progressBar, "scaleY", 1, 300);
            shadAnim(progressBar, "scaleX", 1, 300);
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
        MiPublicidad.verInterstitialAd(this);

        MainActivity.this.startActivity(new Intent(MainActivity.this.getApplicationContext(), ImageViewerActivity.class));

    }
}
