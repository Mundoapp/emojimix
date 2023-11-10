package com.emojixer.functions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.arthenica.mobileffmpeg.FFmpeg;
import com.bumptech.glide.Glide;
import com.bumptech.glide.gifdecoder.StandardGifDecoder;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.emojixer.BuildConfig;
import com.emojixer.GifEncoder.AnimatedGifEncoder;
import com.emojixer.R;
import com.emojixer.activities.MainActivity;
import com.orhanobut.hawk.Hawk;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FileUtil {
        private static final String TAG = "FileUtil";
        public static String Stickername = "Stickername";
        public static String SNAMAE = "sname";

        public static String getFileName(boolean anim) {
            if (anim)
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
                Log.e(TAG, "saveEmojiBIG: " + fos);

                fos.close();
                MediaScannerConnection.scanFile(context, new String[]{file.getPath()}, new String[]{"image/webp"}, null);
                //    addWhatsapp(context, file);
                return file;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public static File saveEmoji(Activity context, View view, String path) {
            File file = getOutputMediaFile(path, getFileName(false));
            if (file == null) {
                return null;
            }
            Bitmap bitmap = convertViewToBitmap(view);
            if (bitmap == null) {
                return null;
            }
//        Bitmap bitmap2 = crop(bitmap);
//        Bitmap bitmap2 = Bitmap.createScaledBitmap(
//                bitmap, 512, 512, false);

            Bitmap bitmap2 = Bitmap.createBitmap(
                    bitmap, MainActivity.posicionX, 0, 512, 512);
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

        public static void setStickerPack(Context context) {
            int id = 0;
            List<String> mEmojis = new ArrayList<>();
            mEmojis.add("");
            ArrayList<StickerPack> stickerPacks = new ArrayList<>();

            List<File> item = new ArrayList<>();
            String path = context.getFilesDir() + "/stickers/";
            File f = new File(path);
            //  String filename = FileUtil.get_selectedSticker(context);
            if (f.exists()) {
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
                if (files != null) {
                    item = FileUtil.getAllFiles(path);
                    for (int k = 0; k < item.size(); k++) {
                        if (item.get(k).getName().contains("AImage"))
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
                if (aStickers.size() > 0) {
                    stickerPacks.add(new StickerPack(
                            "AImage",
                            "",
                            "EmojiSet Mundoapp",
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
                if (mStickers.size() > 0) {
                    stickerPacks.add(new StickerPack(
                            "SImage",
                            "",
                            "EmojiSet Mundoapp",
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

        public static File onCreateWebpFile(ArrayList<Bitmap> frames, Context context, String path) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            String filename = getFileName(true);
            File file = getOutputMediaFile(path, "/"+filename);
            String result = filename.substring(0, filename.lastIndexOf("."));
            Log.e("aki run result", filename + "  " + file.getAbsolutePath());
            File Giffile = getOutputMediaFile(path + "/gif/", result + ".gif");
            Log.e("aki run GifFilepath", Giffile.getAbsolutePath());

            generateGIF(frames, Giffile.getAbsolutePath(),100);
            Log.e("Path", Giffile.getAbsolutePath());
            final String ffmpegCommand = String.format("-hide_banner -i %s -c:v libwebp -b:v 0.7M -vf \"scale=512:512:force_original_aspect_ratio=decrease,pad=512:512:-1:-1:color=#00000000\" -q:v 70  -lossless 0  -method 0 %s", Giffile.getAbsolutePath(), file.getAbsolutePath());
            FFmpeg.execute(ffmpegCommand);



            return file;
            //  Toast.makeText(context, path, Toast.LENGTH_LONG).show();
        }

        public static void set_selectedSticker(String filename, Context context) {
            SharedPreferences.Editor editor = context.getSharedPreferences(Stickername, 0).edit();
            editor.putString(SNAMAE, filename);
            editor.apply();
        }

        public static String get_selectedSticker(Context context) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(Stickername, 0);
            return sharedPreferences.getString(SNAMAE, "AImage");
        }

        public static ArrayList<Bitmap> GiftoFrames(Context context, Bitmap upperimage) {
            ArrayList<Bitmap> bitmaps = new ArrayList<>();
            ArrayList<Bitmap> Frames = new ArrayList<>();
            Glide.with(context)
                    .asGif()
                    .load(R.drawable.base25)
                    .into(new SimpleTarget<GifDrawable>() {
                        @Override
                        public void onResourceReady(@NonNull GifDrawable resource, @Nullable Transition<? super GifDrawable> transition) {
                            try {
                                Object GifState = resource.getConstantState();
                                Field frameLoader = GifState.getClass().getDeclaredField("frameLoader");
                                frameLoader.setAccessible(true);
                                Object gifFrameLoader = frameLoader.get(GifState);

                                Field gifDecoder = gifFrameLoader.getClass().getDeclaredField("gifDecoder");
                                gifDecoder.setAccessible(true);
                                StandardGifDecoder standardGifDecoder = (StandardGifDecoder) gifDecoder.get(gifFrameLoader);
                                for (int i = 0; i < standardGifDecoder.getFrameCount(); i++) {
                                    standardGifDecoder.advance();
                                    bitmaps.add(standardGifDecoder.getNextFrame());
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        }
                    });
            Log.e("Bitmapframes", bitmaps.size() + "");
            for (int i = 0; i < bitmaps.size(); i++) {

                Frames.add(CombineImage(bitmaps.get(i), upperimage));
            }
            return Frames;
        }

        public static ArrayList<Bitmap> GiftoFrames(Context context) {
            ArrayList<Bitmap> bitmaps = new ArrayList<>();
            ArrayList<Bitmap> Frames = new ArrayList<>();
            Glide.with(context)
                    .asGif()
                    .load(R.drawable.base25)
                    .into(new SimpleTarget<GifDrawable>() {
                        @Override
                        public void onResourceReady(@NonNull GifDrawable resource, @Nullable Transition<? super GifDrawable> transition) {
                            try {
                                Object GifState = resource.getConstantState();
                                Field frameLoader = GifState.getClass().getDeclaredField("frameLoader");
                                frameLoader.setAccessible(true);
                                Object gifFrameLoader = frameLoader.get(GifState);

                                Field gifDecoder = gifFrameLoader.getClass().getDeclaredField("gifDecoder");
                                gifDecoder.setAccessible(true);
                                StandardGifDecoder standardGifDecoder = (StandardGifDecoder) gifDecoder.get(gifFrameLoader);
                                for (int i = 0; i < standardGifDecoder.getFrameCount(); i++) {
                                    standardGifDecoder.advance();
                                    bitmaps.add(standardGifDecoder.getNextFrame());
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        }
                    });
        /*for(int i = 0;i<bitmaps.size();i++)
        {
            Frames.add(CombineImage(bitmaps.get(i),upperimage));
        }*/
            return bitmaps;
        }
    public static void generateGIF(List<Bitmap> frames, String outputPath, int delay) {
        try {
            FileOutputStream outputStream = new FileOutputStream(outputPath);
            AnimatedGifEncoder gifEncoder = new AnimatedGifEncoder();
            gifEncoder.start(outputStream);
            gifEncoder.setDelay(delay); // Especifica el retraso entre los fotogramas en milisegundos
            gifEncoder.setTransparent(Color.TRANSPARENT); // Establece el color transparente predeterminado
            gifEncoder.setQuality(0); // Configura la profundidad de color
            gifEncoder.setRepeat(2);

            for (Bitmap frame : frames) {
                gifEncoder.addFrame(frame);
            }

            gifEncoder.finish();
            outputStream.close();
     } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
//   public static void saveGif(Activity ctx,String path,Bitmap upperImage) {
//        try {
//            ArrayList<Bitmap> Frames = new ArrayList<>();
//            File giffile = getOutputMediaFile(path,getFileNameGif());
//            Log.e("Gifwriter Path",giffile.getAbsolutePath());
//            Frames = GiftoFrames(ctx,upperImage);
//            FileOutputStream outStream = new FileOutputStream(path+getFileNameGif());
//            outStream.write(generateGIF(Frames));
//            outStream.close();
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//    }

        public static Bitmap CombineImage(Bitmap bottomImage, Bitmap topImage) {
            Log.e("Combine frames", "coming here");
            int bitmap1Width = bottomImage.getWidth();
            int bitmap1Height = bottomImage.getHeight();
            int bitmap2Width = topImage.getWidth();
            int bitmap2Height = topImage.getHeight();

            float marginLeft = (float) (bitmap1Width * 0 - bitmap2Width * 0);
            float marginTop = (float) (bitmap1Height * 1 - bitmap2Height * 1);
            Log.e("MarginLeft Top", "" + marginLeft + " " + marginTop);
            Bitmap finalBitmap = Bitmap.createBitmap(bitmap1Width, bitmap1Height, bottomImage.getConfig());
            Canvas canvas = new Canvas(finalBitmap);
            canvas.drawBitmap(bottomImage, new Matrix(), null);
            canvas.drawBitmap(topImage, marginLeft, marginTop, null);
            return finalBitmap;
            // Get your images from their files
        /*Bitmap bottomImage = BitmapFactory.decodeFile("myFirstPNG.png");
        Bitmap topImage = BitmapFactory.decodeFile("myOtherPNG.png");*/

// As described by Steve Pomeroy in a previous comment,
// use the canvas to combine them.
// Start with the first in the constructor..
            //    Canvas comboImage = new Canvas(bottomImage);
// Then draw the second on top of that
            //   comboImage.drawBitmap(topImage, 0f, 0f, null);

// comboImage is now a composite of the two.

// To write the file out to the SDCard:
        /*OutputStream os = null;
        try {
            os = new FileOutputStream("/sdcard/DCIM/Camera/" + "myNewFileName.png");
            bottomImage.compress(Bitmap.CompressFormat.PNG, 50, os);

        } catch(IOException e) {
            e.printStackTrace();
        }*/
            //  Log.e("Combine frames","coming here");
            //   return bottomImage;
        }

        @SuppressLint("ResourceAsColor")
        public static void addWhatsapp(final Activity mContext, File f) {
            new Thread(new Runnable() {
                public void run() {
                    VariablesEnMemoria.readStickerWhassap(mContext);
                    for (int i = 1; i <= ((GlobalVariable.stickersWhassap.size() - 1) / 29) + 1; i++) {
                        if (!WhitelistCheck.isWhitelisted(mContext, i + ""))
                            addStickerPackToWhatsApp(i + "", "EmojiSet " + i, mContext);
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



        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Guardar el estado actual de dibujo
        canvas.save();

        // Dibujar el contenido de la vista en el lienzo
        view.layout(0, 0, view.getLayoutParams().width, view.getLayoutParams().height);
        view.draw(canvas);

        // Restaurar el estado de dibujo original
        canvas.restore();

        return bitmap;
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




    public static Pair<Bitmap, Bitmap> captureFrameLayout(FrameLayout layout) {
        // Crear una variable para almacenar el resultado
        final Pair<Bitmap, Bitmap>[] result = new Pair[1];

        // Crear un nuevo hilo para realizar la captura y el procesamiento

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

                // Almacena el resultado en el arreglo
                result[0] = new Pair<>(bitmap, scaledRgbaBitmap);


        // Devolver el resultado
        return result[0];
    }


}
