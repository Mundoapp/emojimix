package com.emojitwomix.functions;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VariablesEnMemoria {

    //aki genero la lista de stickers whatsapp
    public static void cleanStickerFolder(Context context) {
        String path = context.getFilesDir() + "/stickers/";
        File folder = new File(path);
        if (folder.exists()) {
            File[] files = folder.listFiles();
            for (File file : files) {
                if (!file.isDirectory() && file.length() >= 500 * 1024) { // Si el archivo es más grande de 500 KB
                    file.delete(); // Eliminar o mover a otra ubicación
                }
            }
        }
    }

    public static void readStickerWhassap(Context context){
        cleanStickerFolder(context); // Limpia la carpeta primero

        List<File> item = new ArrayList<>();
        String path = context.getFilesDir() + "/stickers/";
        File f = new File(path);
        String filename = FileUtil.get_selectedSticker(context);
        if(f.exists()) {
            File[] files = f.listFiles();
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (!file.isDirectory()) {// Verifica que el archivo no sea un directorio y que pese menos de 500 KB
                    item.add(file);
                    Log.e("TAG", "readStickerWhassap: " + file.length());
                }
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
            List<String> salida = new ArrayList<>();
            for (int i = 0; i < item.size(); i++) {
                if(item.get(i).getName().contains(filename) ) {
                    salida.add(item.get(i).getName());

                }
            }
            GlobalVariable.stickersWhassap.clear();
            GlobalVariable.stickersWhassap = salida;

        }
    }
    public static void readStickerWhassapestaticos(Context context){
        List<File> item = new ArrayList<>();
        String path = context.getFilesDir() + "/stickers/";
        Log.e("TAG", "readStickerWhassap: aki entro 1" );
        File f = new File(path);
        String filename = FileUtil.get_selectedStickerestatico(context);
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
            List<String> salida = new ArrayList<>();
            for (int i = 0; i < item.size(); i++) {
                if(item.get(i).getName().contains(filename)) {
                    salida.add(item.get(i).getName());
                    Log.e("TAG", "readStickerWhassap: aki entro for " );

                }
            }
            GlobalVariable.stickersWhassapestaticos.clear();
            GlobalVariable.stickersWhassapestaticos = salida;
        }
    }
}
