package com.emojitwomix.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.concurrent.atomic.AtomicInteger;

public class DatabaseManager {
    private static DatabaseManager instance;
    private static SQLiteOpenHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;
    private AtomicInteger mOpenCounter = new AtomicInteger();

    public static synchronized void initializeInstance(Context context, String dbName) {
        synchronized (DatabaseManager.class) {
            if (instance == null) {
                instance = new DatabaseManager();
                mDatabaseHelper = DBMS.getInstance(context, dbName);
            }
        }
    }

    public static synchronized DatabaseManager getInstance() {
        DatabaseManager databaseManager;
        synchronized (DatabaseManager.class) {
            if (instance == null) {
                throw new IllegalStateException(DatabaseManager.class.getSimpleName() + " is not initialized, call initializeInstance(..) method first.");
            }
            databaseManager = instance;
        }
        return databaseManager;
    }

    public synchronized SQLiteDatabase openDatabase() {
        if (this.mOpenCounter.incrementAndGet() == 1) {
            this.mDatabase = mDatabaseHelper.getWritableDatabase();
        }
        return this.mDatabase;
    }

    public synchronized void closeDatabase() {
        if (this.mOpenCounter.decrementAndGet() == 0) {
            this.mDatabase.close();
        }
    }
}
