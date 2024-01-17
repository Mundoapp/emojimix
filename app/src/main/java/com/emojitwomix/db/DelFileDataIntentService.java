package com.emojitwomix.db;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.emojitwomix.functions.FileUtil;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public class DelFileDataIntentService extends IntentService {
    public DelFileDataIntentService() {
        super("DelFileDataIntentService");
    }

    public void onCreate() {
        super.onCreate();
    }

    /* access modifiers changed from: protected */
    public void onHandleIntent(@Nullable Intent intent) {
        ContentValues values = new ContentValues();
        values.put(DBMS.COL_STATUS, 1);
        DbHelper.updateData(DBMS.TblSticker, (String) null, values);
        List<File> files = FileUtil.getAllFiles(getFilesDir() + "/stickers/");
        if (files != null && !files.isEmpty()) {
            Iterator<File> it = files.iterator();
            while (it.hasNext()) {
                ContentValues cv = new ContentValues();
                cv.put(DBMS.COL_STATUS, 0);
                DbHelper.updateData(DBMS.TblSticker, " where FileName = '" + it.next().getName() + "'", cv);
            }
        }
        DbHelper.deleteRecord(DBMS.TblSticker, " where Status = 1");
    }
}
