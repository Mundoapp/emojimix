package com.emojitwomix.functions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.util.Log;

import androidx.core.content.FileProvider;

 import com.emojitwomix.BuildConfig;
import com.emojitwomix.activities.BitmapTransform;

import java.io.File;

public class GlobalClass {

    @SuppressLint("ResourceAsColor")
    public static void addWhatsapp(final Activity mContext) {
        new Thread(new Runnable() {
            public void run() {
                VariablesEnMemoria.readStickerWhassap(mContext);
                FileUtil.setStickerPack(mContext);
                for (int i = 1; i <= ((GlobalVariable.stickersWhassap.size() - 1) / 29) + 1; i++) {
                    if (!WhitelistCheck.isWhitelisted(mContext, i + ""))
                        if(FileUtil.get_selectedSticker(mContext).equals("SImage"))
                            addStickerPackToWhatsApp(FileUtil.get_selectedSticker(mContext), "My avatar emoji static" , mContext);
                        else
                            addStickerPackToWhatsApp(FileUtil.get_selectedSticker(mContext), "My avatar emoji animated" , mContext);
                }
            }
        }).start();
    }
    private static void addStickerPackToWhatsApp(String identifier, String stickerPackName, Activity context) {
        Intent intent = new Intent();
        intent.setAction("com.whatsapp.intent.action.ENABLE_STICKER_PACK");
        intent.putExtra("sticker_pack_id", identifier);
        intent.putExtra("sticker_pack_authority", BuildConfig.CONTENT_PROVIDER_AUTHORITY);
        intent.putExtra("sticker_pack_name", stickerPackName);
        try {
            context.startActivityForResult(intent, 200);
        } catch (ActivityNotFoundException e) {
            Log.e("Error",e.toString());
            e.printStackTrace();
            //Toast.makeText(context, "ERROR", Toast.LENGTH_LONG).show();
        }
    }
    public static Bitmap mergeToPin(Bitmap firstImage, Bitmap secondImage, Activity activity){
        Bitmap bmp2 = secondImage.copy(secondImage.getConfig(), true);
        Bitmap result = BitmapTransform.createBitmap(activity,firstImage,
                bmp2, PorterDuff.Mode.SRC_OVER, true, false);
        return result;
    }
    public static void compartir(final Activity context, Bitmap bitmap) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File dir = new File(context.getFilesDir() + "/fileaux");
                if (dir.isDirectory()) {
                    String[] children = dir.list();
                    for (int i = 0; i < children.length; i++) {
                        new File(dir, children[i]).delete();
                    }
                }
                dirChecker(context.getFilesDir() + "/fileaux");
                String path = context.getFilesDir() + "/fileaux/compartir.webp";
                File file =  FileUtil.saveEmoji_aux(context, bitmap, path);
                Uri imageUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "Share");
                intent.putExtra(Intent.EXTRA_STREAM, imageUri);
                intent.setType("image/*");
                context.startActivity(intent);
            }
        }).start();
    }
    private static void dirChecker(String dir) {
        File f = new File(dir);
        if (!f.isDirectory()) {
            f.mkdirs();
        }
    }
}
