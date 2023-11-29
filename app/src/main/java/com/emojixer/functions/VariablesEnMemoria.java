package com.emojixer.functions;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VariablesEnMemoria {
    public static void readStickerWhassap(Context context){
        List<File> item = new ArrayList<>();
        String path = context.getFilesDir() + "/stickers/";
        File f = new File(path);
        String filename = FileUtil.get_selectedSticker(context);
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
