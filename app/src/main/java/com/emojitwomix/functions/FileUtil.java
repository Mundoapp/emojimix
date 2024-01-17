package com.emojitwomix.functions;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.media.MediaScannerConnection;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.FrameLayout;


import com.emojitwomix.GifEncoder.AnimatedGifEncoder;
import com.emojitwomix.R;
import com.orhanobut.hawk.Hawk;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
            if (anim)
                return "AImage" + new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date()) + ".webp";
            else
                return "SImage" + new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date()) + ".webp";
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
                    if (!file.isDirectory() && file.length() < 500 * 1024) // Verifica que el archivo no sea un directorio y que pese menos de 500 KB

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
                            "Emoji2 mix",
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
                            "Emoji2 mix",
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

        public static String get_selectedSticker(Context context) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(Stickername, 0);
            return sharedPreferences.getString(SNAMAE, "AImage");
        }
    public static String get_selectedStickerestatico(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Stickername, 0);
        return sharedPreferences.getString(SNAMAE, "SImage");
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
        //view.layout(0, 0, view.getLayoutParams().width, view.getLayoutParams().height);
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
