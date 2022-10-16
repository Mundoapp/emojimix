package com.emojimix.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.emojimix.R;
import com.emojimix.db.DbHelper;
import com.emojimix.functions.FileUtil;
import com.emojimix.functions.GlobalClass;
import com.emojimix.functions.GlobalVariable;
import com.emojimix.functions.Sticker;
import com.emojimix.functions.StickerPack;
import com.emojimix.functions.Utility;
import com.emojimix.functions.VariablesEnMemoria;
import com.emojimix.functions.WhitelistCheck;
import com.emojimix.mipublicidad.Admob;
import com.emojimix.mipublicidad.MiPublicidad;
import com.facebook.drawee.backends.pipeline.Fresco;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImageViewerActivity extends BaseActivity {
    private static final int REQ_EDIT_EMOJI = 4660;
    /* access modifiers changed from: private */
    public List<ItemFrament> fragments;
//    TextView tvCount;
    TextView tvTitle;
//    ViewPager viewPager;
    private View addButton;
    private StickerPack stickerPack;
    private AdapterStickers stickerPreviewAdapter;



    private class ImageItemAdapter extends FragmentStatePagerAdapter {
        public ImageItemAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            return (Fragment) ImageViewerActivity.this.fragments.get(position);
        }

        public int getCount() {
            return ImageViewerActivity.this.fragments.size();
        }

        public int getItemPosition(Object object) {
            return -2;
        }
    }

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


        public static void eliminar_sticker(Context thisContext, Sticker thisSticker, int pos, Dialog viewPager, StickerPack stickerPack, Activity activity, AdapterStickers stickerPreviewAdapter, LayoutInflater layoutInflater){
            Log.e("aki", "entro");

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(thisContext);
            View dialogView;
            if(activity!=null)
                dialogView = activity.getLayoutInflater().inflate(R.layout.custom_dialog_deleteimage, null);
            else
                dialogView = layoutInflater.inflate(R.layout.custom_dialog_deleteimage, null);
            dialogBuilder.setView(dialogView);
            TextView btn_no = dialogView.findViewById(R.id.btn_no);
            TextView btn_yes = dialogView.findViewById(R.id.btn_yes);
            TextView tv_title = dialogView.findViewById(R.id.tv_title);
            TextView tv_mensage = dialogView.findViewById(R.id.tv_mensage);
            ImageView image = dialogView.findViewById(R.id.iv_dialog);
            image.setImageURI(thisSticker.getUri());
//            tv_title.setText(R.string.labal_warning);
//            tv_mensage.setText(R.string.delete_sticker_dialog_title);

            AlertDialog alertDialog = dialogBuilder.create();

            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            btn_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });

            btn_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (stickerPack.getActualStickers().size() > 3 || !WhitelistCheck.isWhitelisted(thisContext, stickerPack.getIdentifier())) {
                        try {
                            alertDialog.dismiss();
                            if(viewPager!=null)
                                viewPager.dismiss();
                            stickerPack.deleteSticker(pos, thisSticker);
                            stickerPreviewAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                        }
                    } else {
                        alertDialog.dismiss();
//                        if (activity != null)
//                            GlobalClass.dialogoUnico_con_boton(activity, R.string.less_stickers, false);
                    }
                }
            });
            alertDialog.show();
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

//        public void deleteEmoji(View view) {
//            ((ImageViewerActivity) getActivity()).performDelete();
//        }

        public void editEmoji(View view) {
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.putExtra("File", this.file);
            getActivity().startActivityForResult(intent, 4660);
        }
    }

    public static void actualizar() {

    }

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        tvTitle = (TextView) findViewById(R.id.tv_title);
//        tvCount = (TextView) findViewById(R.id.tv_count);
//        viewPager = (ViewPager) findViewById(R.id.view_pager);
        addButton = findViewById(R.id.add_to_whatsapp_button);


        MiPublicidad.baner( (FrameLayout) findViewById(R.id.ad_view_container), this);


        Activity activity = this;
        final boolean[] verbotonwhassap = {false};
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    MiPublicidad.verInterstitialAd(activity);

                VariablesEnMemoria.readStickerWhassap(ImageViewerActivity.this);
                if(GlobalVariable.stickersWhassap.size()>=3) {
                    GlobalClass.addWhatsapp(activity);
                    for (int i = 1; i <= ((GlobalVariable.stickersWhassap.size() - 1) / 29) + 1; i++) {
                        if (!WhitelistCheck.isWhitelisted(activity, i + ""))
                            verbotonwhassap[0] = true;
                    }
                    if (!verbotonwhassap[0])
                        addButton.setVisibility(View.GONE);
                    else
                        addButton.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(ImageViewerActivity.this, "Minimum 3 sticker required to add whatsapp", Toast.LENGTH_SHORT).show();
                }

            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);
        final AdapterStickers adapterAvatares = new AdapterStickers(getLayoutInflater(),this,"SImage");
        recyclerView.setAdapter(adapterAvatares);

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
//        this.viewPager.setAdapter(new ImageItemAdapter(getSupportFragmentManager()));
//        this.viewPager.setOffscreenPageLimit(1);
//        File selectedFile = (File) intent.getSerializableExtra("File");
//        if (selectedFile != null) {
//            int index = files.indexOf(selectedFile);
//            if (index != -1) {
//                this.viewPager.setCurrentItem(index);
//            }
//        }
////        setCount();
//        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            }
//
//            public void onPageSelected(int position) {
////                ImageViewerActivity.this.setCount();
//                ImageViewerActivity.this.loadFragmentImage();
//            }
//
//            public void onPageScrollStateChanged(int state) {
//            }
//        });
//        this.viewPager.post(new Runnable() {
//            public void run() {
//                ImageViewerActivity.this.loadFragmentImage();
//            }
//        });
    }

    /* access modifiers changed from: private */
//    public void loadFragmentImage() {
//        try {
//            this.fragments.get(this.viewPager.getCurrentItem()).loadImage();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /* access modifiers changed from: private */
//    public void setCount() {
//        this.tvCount.setText("[ " + (this.viewPager.getCurrentItem() + 1) + "/" + this.fragments.size() + " ]");
//    }

    public void onCloseEvent(View view) {
         onBackPressed();
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

//    /* access modifiers changed from: private */
//    public void performDelete() {
//        int index = this.viewPager.getCurrentItem();
//        if (this.fragments.get(index).getFile().delete()) {
//            this.fragments.remove(index);
//            this.viewPager.getAdapter().notifyDataSetChanged();
////            setCount();
//            loadFragmentImage();
//            showToast("Emoji deleted!");
//        }
//    }



}
