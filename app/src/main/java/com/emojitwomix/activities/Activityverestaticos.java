package com.emojitwomix.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.emojitwomix.R;
import com.emojitwomix.db.DbHelper;
import com.emojitwomix.functions.FileUtil;
import com.emojitwomix.functions.GlobalClass;
import com.emojitwomix.functions.GlobalVariable;
import com.emojitwomix.functions.StickerPack;
import com.emojitwomix.functions.Utility;
import com.emojitwomix.functions.VariablesEnMemoria;
import com.emojitwomix.functions.WhitelistCheck;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Activityverestaticos extends BaseActivity {
    private static final int REQ_EDIT_EMOJI = 4660;
    /* access modifiers changed from: private */
    public List<ItemFrament> fragments;
//    TextView tvCount;
    TextView tvTitle;
//    ViewPager viewPager;
    private View addButton;
    private StickerPack stickerPack;
    private AdapterStickersestaticos stickerPreviewAdapter;




    public static class ItemFrament extends BaseFragment {
        private File file;
        ImageView ivEmoji;

        public static ItemFrament getInstance(File file2) {
            ItemFrament fragment = new ItemFrament();
            Bundle bundle = new Bundle();
            bundle.putSerializable("File", file2);
            fragment.setArguments(bundle);
            return fragment;
        }

        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.file = (File) getArguments().getSerializable("File");


        }

        public File getFile() {
            return this.file;
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_view_image, null);
        }





        @SuppressLint("WrongConstant")
        public void loadImage() {
            if (this.file != null) {
                if (!DbHelper.isFileExist(this.file.getName())) {
                    getView().findViewById(R.id.edit_layout).setVisibility(View.GONE);

                }
                if (this.ivEmoji.getDrawable() == null) {
                    Glide.with(getContext()).load(this.file).override(400, 400).into(this.ivEmoji);
                }
            }
        }

        public void shareEmoji(View view) {
            try {
                Utility.shareFile(getContext(), this.file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        tvTitle = (TextView) findViewById(R.id.tv_title);
//        tvCount = (TextView) findViewById(R.id.tv_count);
//        viewPager = (ViewPager) findViewById(R.id.view_pager);
        addButton = findViewById(R.id.add_to_whatsapp_button);


    //   anuncios.baner( (FrameLayout) findViewById(R.id.ad_view_container), this);


        Activity activity = this;
        final boolean[] verbotonwhassap = {false};
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // anuncios.verInterstitialAd(activity);

                VariablesEnMemoria.readStickerWhassapestaticos(Activityverestaticos.this);
                if(GlobalVariable.stickersWhassapestaticos.size()>=3) {
                    GlobalClass.addWhatsappestaticos(activity);
                    for (int i = 1; i <= ((GlobalVariable.stickersWhassapestaticos.size() - 1) / 29) + 1; i++) {
                        if (!WhitelistCheck.isWhitelisted(activity, i + ""))
                            verbotonwhassap[0] = true;
                    }
                    if (!verbotonwhassap[0])
                        addButton.setVisibility(View.GONE);
                    else
                        addButton.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(Activityverestaticos.this, "Minimum 3 sticker required to add whatsapp", Toast.LENGTH_SHORT).show();
                }

            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);
        final AdapterStickersestaticos adapteremojis = new AdapterStickersestaticos(getLayoutInflater(),this,"SImage");
        recyclerView.setAdapter(adapteremojis);

    }

    /* access modifiers changed from: protected */
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mPostCreate(getIntent(),getBaseContext());
    }

    public void mPostCreate(Intent intent, Context context) {
        List<File> files = FileUtil.getAllFiles(context.getFilesDir() + "/stickers/");
        if (files.isEmpty()) {
//            this.tvCount.setText("[ 0/0 ]");
            return;
        }
        Collections.reverse(files);
        this.fragments = new ArrayList();
        for (File file : files) {
            this.fragments.add(ItemFrament.getInstance(file));
        }
    }


    public void onBackPressed() {

        super.onBackPressed();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 4660 && resultCode == -1) {
            mPostCreate(data,getBaseContext());
        }
        if (requestCode == 200) {
            if (resultCode == Activity.RESULT_CANCELED && data != null) {
                final String validationError = data.getStringExtra("validation_error");
                if (validationError != null) {
                    if (Boolean.parseBoolean("true")) {
                        //error should be shown to developer only, not users.
                    }
                    Log.e("ImageView", "Validation failed:" + validationError);
                }
            }
        }
    }



}
