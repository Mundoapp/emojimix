package com.emojixer.functions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.view.View;
import android.widget.Toast;


import com.emojixer.BuildConfig;
import com.orhanobut.hawk.Hawk;


import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class FileUtil {
    private static final String TAG = "FileUtil";
    public static String Stickername = "Stickername";
    public static String SNAMAE = "sname";
    public static String getFileName(boolean anim) {
        if(anim)
            return "AImage" + new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date()) + ".webp";
        else
            return "SImage" + new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date()) + ".webp";
    }
    public static String getFileNameGif() {
        return "Gif" + new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date()) + ".gif";
    }



    public static File saveEmojiBIG(Activity context, View view, String path) {
        File file = getOutputMediaFile(path, getFileName(false));
        if (file == null) {
            return null;
        }
        Bitmap bitmap = convertViewToBitmap(view);
        if (bitmap == null) {
            return null;
        }
//        Bitmap bitmap2 = crop(bitmap);
       Bitmap bitmap2 = Bitmap.createScaledBitmap(
                bitmap, 512, 512, false);

        //  saveGif(context,path,bitmap2);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap2.compress(Bitmap.CompressFormat.WEBP, 90, fos);
            fos.close();
            MediaScannerConnection.scanFile(context, new String[]{file.getPath()}, new String[]{"image/webp"}, null);
            //    addWhatsapp(context, file);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setStickerPack(Context context)
    {
        int id = 0;
        List<String> mEmojis = new ArrayList<>();
        mEmojis.add("");
        ArrayList<StickerPack> stickerPacks = new ArrayList<>();

        List<File> item = new ArrayList<>();
        String path = context.getFilesDir() + "/stickers/";
        File f = new File(path);
      //  String filename = FileUtil.get_selectedSticker(context);
        if(f.exists()) {
            File[] files = f.listFiles();
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (!file.isDirectory())
                    item.add(file);
            }
            for (int i = 0; i < item.size() - 1; i++) {
                for (int j = i + 1; j < item.size(); j++) {
                    if (item.get(i).lastModified() > item.get(j).lastModified()) {
                        File variableauxiliar = item.get(i);
                        item.set(i, item.get(j));
                        item.set(j, variableauxiliar);
                    }
                }
            }
            List<Sticker> mStickers = new ArrayList<>();
            List<Sticker> aStickers = new ArrayList<>();
            if(files!=null) {
                item = FileUtil.getAllFiles(path);
                for(int k=0;k<item.size();k++)
                {
                    if(item.get(k).getName().contains("AImage"))
                         aStickers.add(new Sticker(
                              item.get(k).getName(),
                              mEmojis
                    ));
                    else
                        mStickers.add(new Sticker(
                                item.get(k).getName(),
                                mEmojis
                        ));
                }


            }
            if(aStickers.size()>0) {
                stickerPacks.add(new StickerPack(
                        "AImage",
                        "",
                        "Emoji mix maker",
                        "sticker.webp",
                        "",
                        "",
                        "",
                        ""
                ));
                stickerPacks.get(id).setStickers(aStickers);
                Hawk.put("AImage", aStickers);
                id = 1;
            }
            if(mStickers.size()>0) {
                stickerPacks.add(new StickerPack(
                        "SImage",
                        "",
                        "Emoji mix maker",
                        "sticker.webp",
                        "",
                        "",
                        "",
                        ""
                ));
                stickerPacks.get(id).setStickers(mStickers);
                Hawk.put("SImage", mStickers);
            }
        }
        Hawk.put("sticker_packs", stickerPacks);
    }


    public static void set_selectedSticker(String filename,Context context)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(Stickername, 0).edit();
        editor.putString(SNAMAE, filename);
        editor.apply();
    }
    public static String get_selectedSticker(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Stickername, 0);
        return sharedPreferences.getString(SNAMAE,"SImage");
    }

    @SuppressLint("ResourceAsColor")
    public static void addWhatsapp(final Activity mContext, File f) {
        new Thread(new Runnable() {
            public void run() {
                VariablesEnMemoria.readStickerWhassap(mContext);
                for (int i = 1; i <= ((GlobalVariable.stickersWhassap.size() - 1) / 29) + 1; i++) {
                    if (!WhitelistCheck.isWhitelisted(mContext, i + ""))
                        addStickerPackToWhatsApp(i + "", "Emoji mix maker " + i, mContext);
                }
            }
        }).start();
    }
    public static void addStickerPackToWhatsApp(String identifier, String stickerPackName, Activity context) {
        Intent intent = new Intent();
        intent.setAction("com.whatsapp.intent.action.ENABLE_STICKER_PACK");
        intent.putExtra("sticker_pack_id", identifier);
        intent.putExtra("sticker_pack_authority", BuildConfig.CONTENT_PROVIDER_AUTHORITY);
        intent.putExtra("sticker_pack_name", stickerPackName);
        try {
            context.startActivityForResult(intent, 200);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "ERROR", Toast.LENGTH_LONG).show();
        }
    }
    public static List<File> getAllFiles(String dirPath) {
        List<File> filesList = new ArrayList<>();
        File[] files = new File(dirPath).listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".webp")) {
                    filesList.add(file);
                }
            }
        }
        return filesList;
    }

    private static boolean createDirectoryIfNotExits(String dirPath) {
        File mediaStorageDirectory = new File(dirPath);
        if (!mediaStorageDirectory.exists()) {
            return mediaStorageDirectory.mkdirs();
        }
        return true;
    }

    private static File getOutputMediaFile(String dirPath, String filename) {
        if (!createDirectoryIfNotExits(dirPath)) {
            return null;
        }
        return new File(dirPath + filename);
    }

    public static Bitmap convertViewToBitmap(View view) {
        try {
            view.setDrawingCacheEnabled(true);
            view.setDrawingCacheBackgroundColor(0);
            view.buildDrawingCache(true);
            Bitmap b = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);
            return b;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap crop(Bitmap bitmap) {
        try {
            int height = bitmap.getHeight();
            int width = bitmap.getWidth();
            int[] empty = new int[width];
            int[] buffer = new int[width];
            Arrays.fill(empty, 0);
            int top = 0;
            int left = 0;
            int botton = height;
            int right = width;
            int y = 0;
            while (true) {
                if (y >= height) {
                    break;
                }
                bitmap.getPixels(buffer, 0, width, 0, y, width, 1);
                if (!Arrays.equals(empty, buffer)) {
                    top = y;
                    break;
                }
                y++;
            }
            int y2 = height - 1;
            while (true) {
                if (y2 <= top) {
                    break;
                }
                bitmap.getPixels(buffer, 0, width, 0, y2, width, 1);
                if (!Arrays.equals(empty, buffer)) {
                    botton = y2;
                    break;
                }
                y2--;
            }
            int bufferSize = (botton - top) + 1;
            int[] empty2 = new int[bufferSize];
            int[] buffer2 = new int[bufferSize];
            Arrays.fill(empty2, 0);
            int x = 0;
            while (true) {
                if (x >= width) {
                    break;
                }
                bitmap.getPixels(buffer2, 0, 1, x, top + 1, 1, bufferSize);
                if (!Arrays.equals(empty2, buffer2)) {
                    left = x;
                    break;
                }
                x++;
            }
            int x2 = width - 1;
            while (true) {
                if (x2 <= left) {
                    break;
                }
                bitmap.getPixels(buffer2, 0, 1, x2, top + 1, 1, bufferSize);
                if (!Arrays.equals(empty2, buffer2)) {
                    right = x2;
                    break;
                }
                x2--;
            }
            return Bitmap.createBitmap(bitmap, left, top, right - left, botton - top);
        } catch (Exception e) {
            e.printStackTrace();
            return bitmap;
        }
    }

    public static boolean isAppSupportImageSharing(Context context, String packageName) {
        try {
            List<String> packages = new ArrayList<>();
            Intent sendIntent = new Intent();
            sendIntent.setAction("android.intent.action.SEND");
            sendIntent.setType("image/png");
            for (ResolveInfo resolveInfo : context.getPackageManager().queryIntentActivities(sendIntent, 0)) {
                packages.add(resolveInfo.activityInfo.packageName);
            }
            if (packages.contains(packageName)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static File saveEmoji_aux(Activity context, Bitmap bitmap, String path) {
        File file = new File(path);
        if (file == null) {
            return null;
        }
        if (bitmap == null) {
            return null;
        }
//        Bitmap bitmap2 = crop(bitmap);
        Bitmap bitmap2 = Bitmap.createScaledBitmap(
                bitmap, 512, 512, false);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap2.compress(Bitmap.CompressFormat.WEBP, 90, fos);
            fos.close();
            MediaScannerConnection.scanFile(context, new String[]{file.getPath()}, new String[]{"image/webp"}, null);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
