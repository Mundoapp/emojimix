package com.emojixer.functions;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import  com.emojixer.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Utility {
    public static int calculateNoOfColumns(Context context, int columnWidth) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((((float) displayMetrics.widthPixels) / displayMetrics.density) / ((float) columnWidth));
    }

    public static int getRandomNumberFrom(int min, int max) {
        return new Random().nextInt((max + 1) - min) + min;
    }

    public static void shareApp(Activity activity) {
        try {
            Intent i = new Intent("android.intent.action.SEND");
            i.setType("text/plain");
            i.putExtra("android.intent.extra.SUBJECT", activity.getResources().getString(R.string.app_name));
            i.putExtra("android.intent.extra.TEXT", "I want to share this app with you. Click on this link to download the app:\n\n" + "https://play.google.com/store/apps/details?id=" + activity.getPackageName());
            activity.startActivity(Intent.createChooser(i, "Share app"));
        } catch (Exception e) {
        }
    }

    public static void rateUsApp(Activity activity) {
        try {
            activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + activity.getPackageName())));
        } catch (ActivityNotFoundException e) {
            activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + activity.getPackageName())));
        }
    }

    public static void sendEmail(Activity activity) {
        try {
            Intent email = new Intent("android.intent.action.SEND");
            email.putExtra("android.intent.extra.EMAIL", new String[]{"anandgour06@gmail.com"});
            email.putExtra("android.intent.extra.SUBJECT", activity.getResources().getString(R.string.app_name));
            email.setType("message/rfc822");
            activity.startActivity(Intent.createChooser(email, "Choose an email client :"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openImageInGallery(Activity activity, File file) {
        try {
            activity.startActivity(new Intent("android.intent.action.VIEW", Uri.fromFile(file)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printHashKey(Context pContext) {
        try {
            for (Signature signature : pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), 64).signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), 0));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }
    private static void dirChecker(String dir) {
        File f = new File(dir);
        if (!f.isDirectory()) {
            f.mkdirs();
        }
    }
    public static void shareFile(Context context, File file) throws IOException {
        Log.e("Filename",file.getName());
        if(file.getName().contains("SImage")) {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.fromFile(file));
            Bitmap newBmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(newBmp);
            c.drawBitmap(bitmap, 0, 0, new Paint());
            dirChecker(context.getFilesDir() + "/fileaux");
            String path = context.getFilesDir() + "/fileaux/compartir.webp";
            quitaAlpha(path, newBmp);
            Uri imageUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", new File(path));
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, "share" + '\n' + "http://bit.ly/MundoappEmojiMaker");
            intent.putExtra(Intent.EXTRA_STREAM, imageUri);
            intent.setType("image/*");
            context.startActivity(intent);
        }else{
            String result = file.getName().substring(0, file.getName().lastIndexOf("."));
            String path = context.getFilesDir()+"/stickers/Gifs/"+result+".gif";

            File GifFile = new File(path);
            if(GifFile.exists()) {
                Uri imageUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", new File(path));
                Log.e("Share path",imageUri.getPath());
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "Share" + '\n' + "http://bit.ly/MundoappEmojiMaker");
                intent.putExtra(Intent.EXTRA_STREAM, imageUri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setType("*/*");
                context.startActivity(intent);
            }else{
                Toast.makeText(context, "File not exist", Toast.LENGTH_SHORT).show();
            }
        }
    }



    public static boolean isInputMethodEnabled(Context context) {
        boolean isIME = false;
        for (InputMethodInfo inputMethod : ((InputMethodManager) context.getSystemService("input_method")).getEnabledInputMethodList()) {
            if (inputMethod.getPackageName().equals(context.getPackageName())) {
                isIME = true;
            }
        }
        return isIME;
    }

    private static void quitaAlpha(String path, Bitmap bitmap) {
        int quality = 45;
        FileOutputStream outs = null;
        try {
            outs = new FileOutputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (int x = 0; x < bitmap.getWidth(); x++)
            for (int y = 0; y < bitmap.getHeight(); y++)
                if (bitmap.getPixel(x, y) == Color.TRANSPARENT)
                    bitmap.setPixel(x, y, Color.WHITE);

        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.WEBP,
                    quality,
                    bos);
        } catch (Exception e) {
        }
        try {
            outs.write(bos.toByteArray());
            outs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
