package com.emojimixer.db;

import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBMS extends SQLiteOpenHelper {
    public static final String COL_FILENAME = "FileName";
    public static final String COL_FLIP = "Flip";
    public static final String COL_IMG_NAME = "ImageName";
    public static final String COL_RANDOM = "Random";
    public static final String COL_ROTATION = "Rotation";
    public static final String COL_SCALE = "Scale";
    public static final String COL_STATUS = "Status";
    public static final String COL_TYPE = "Type";
    public static final String COL_X = "X";
    public static final String COL_Y = "Y";
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "DBMS";
    public static final String TblSticker = "Sticker";
    private static Context context;
    private static SQLiteDatabase db;
    private static DBMS instance;

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS  Sticker (FileName VARCHAR, ImageName VARCHAR, Type VARCHAR, Scale FLOAT, Rotation FLOAT, X FLOAT, Y FLOAT, Random INTEGER, Flip INTEGER, Status INTEGER)");
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    private DBMS(Context context2, String DATABASE_NAME) {
        super(context2, DATABASE_NAME, null, 1);
        context = context2;
    }

    public synchronized void close() {
        if (instance != null) {
            db.close();
        }
    }

    public static synchronized DBMS getInstance(Context context2, String DATABASE_NAME) {
        DBMS dbms;
        synchronized (DBMS.class) {
            if (instance == null) {
                instance = new DBMS(context2, DATABASE_NAME);
                db = instance.getWritableDatabase();
            }
            dbms = instance;
        }
        return dbms;
    }

    public int getNumberRows(String table) {
        return (int) DatabaseUtils.queryNumEntries(db, table);
    }
}
