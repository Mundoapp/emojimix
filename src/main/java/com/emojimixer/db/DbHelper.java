package com.emojimixer.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbHelper {
    public static void clearTable(String tableName) {
        DatabaseManager.getInstance().openDatabase().delete(tableName, null, null);
        DatabaseManager.getInstance().closeDatabase();
    }

    public static long getRowCount(String tableName) {
        long cnt = DatabaseUtils.queryNumEntries(DatabaseManager.getInstance().openDatabase(), tableName);
        DatabaseManager.getInstance().closeDatabase();
        return cnt;
    }

    public static int insert(String tableName, ContentValues values, String whereClause) {
        if (whereClause != null && whereClause.toLowerCase().contains("where")) {
            whereClause = whereClause.replace("where", "").replace("WHERE", "");
        }
        int rowId = (int) DatabaseManager.getInstance().openDatabase().insert(tableName, whereClause, values);
        DatabaseManager.getInstance().closeDatabase();
        return rowId;
    }

    public static int insertData(String tableName, HashMap<String, Object> hashMap, String whereClause) throws Exception {
        ContentValues contentValues = new ContentValues();
        for (Map.Entry<String, Object> entry : hashMap.entrySet()) {
            if (entry.getValue() instanceof Double) {
                contentValues.put(entry.getKey(), (Double) entry.getValue());
            } else if (entry.getValue() instanceof Integer) {
                contentValues.put(entry.getKey(), (Integer) entry.getValue());
            } else {
                contentValues.put(entry.getKey(), ((String) entry.getValue()) == null ? "" : (String) entry.getValue());
            }
        }
        return insert(tableName, contentValues, whereClause);
    }

    public static <T> int insertData(String tableName, T t, String whereClause) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        for (Field field : t.getClass().getDeclaredFields()) {
            try {
                if (!field.isSynthetic()) {
                    map.put(field.getName(), field.get(t));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return insertData(tableName, map, whereClause);
    }

    public static <T> void insertDataListObject(String tableName, List<T> tList, String whereClause) throws Exception {
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        for (T t : tList) {
            HashMap<String, Object> hashMap = new HashMap<>();
            for (Field field : t.getClass().getDeclaredFields()) {
                try {
                    if (!field.isSynthetic()) {
                        hashMap.put(field.getName(), field.get(t));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ContentValues contentValues = new ContentValues();
            for (Map.Entry<String, Object> entry : hashMap.entrySet()) {
                if (entry.getValue() instanceof Double) {
                    contentValues.put(entry.getKey(), (Double) entry.getValue());
                } else if (entry.getValue() instanceof Integer) {
                    contentValues.put(entry.getKey(), (Integer) entry.getValue());
                } else {
                    contentValues.put(entry.getKey(), ((String) entry.getValue()) == null ? "" : (String) entry.getValue());
                }
            }
            database.insert(tableName, whereClause, contentValues);
        }
        DatabaseManager.getInstance().closeDatabase();
    }

    public static <T> void insertDataListMap(String tableName, List<HashMap<String, Object>> hashMaps, String whereClause) throws Exception {
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        for (HashMap<String, Object> hashMap : hashMaps) {
            ContentValues contentValues = new ContentValues();
            for (Map.Entry<String, Object> entry : hashMap.entrySet()) {
                if (entry.getValue() instanceof Double) {
                    contentValues.put(entry.getKey(), (Double) entry.getValue());
                } else if (entry.getValue() instanceof Integer) {
                    contentValues.put(entry.getKey(), (Integer) entry.getValue());
                } else {
                    contentValues.put(entry.getKey(), ((String) entry.getValue()) == null ? "" : (String) entry.getValue());
                }
            }
            database.insert(tableName, whereClause, contentValues);
        }
        DatabaseManager.getInstance().closeDatabase();
    }

    public static <T> void insertDataListContentValues(String tableName, List<ContentValues> contentValues, String whereClause) throws Exception {
        if (whereClause != null && whereClause.toLowerCase().contains("where")) {
            whereClause = whereClause.replace("where", "").replace("WHERE", "");
        }
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        for (ContentValues contentValue : contentValues) {
            database.insert(tableName, whereClause, contentValue);
        }
        DatabaseManager.getInstance().closeDatabase();
    }

    public static <T> List<T> getAllData(String tableName, String whereClause, Class<T> tClass) throws Exception {
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        Class<?> myType = Integer.TYPE;
        Class<?> myTypeDob = Double.TYPE;
        Class<?> myTypeFlot = Float.TYPE;
        List<T> tList = new ArrayList<>();
        Cursor cursor = null;
        try {
            String sql = "select * from " + tableName;
            if (whereClause != null) {
                sql = sql + " " + whereClause;
            }
            cursor = database.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    T t = tClass.newInstance();
                    for (Field field : t.getClass().getDeclaredFields()) {
                        if (!field.isSynthetic()) {
                            if (field.getType().isAssignableFrom(myType)) {
                                field.set(t, Integer.valueOf(cursor.getInt(cursor.getColumnIndex(field.getName()))));
                            } else if (field.getType().isAssignableFrom(myTypeFlot)) {
                                field.set(t, Float.valueOf(cursor.getFloat(cursor.getColumnIndex(field.getName()))));
                            } else if (field.getType().isAssignableFrom(myTypeDob)) {
                                field.set(t, Double.valueOf(cursor.getDouble(cursor.getColumnIndex(field.getName()))));
                            } else {
                                field.set(t, cursor.getString(cursor.getColumnIndex(field.getName())));
                            }
                        }
                    }
                    tList.add(t);
                } while (cursor.moveToNext());
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
        DatabaseManager.getInstance().closeDatabase();
        return tList;
    }

    public static <T> T getData(String tableName, String whereClause, Class<T> tClass) throws Exception {
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        T t = tClass.newInstance();
        Class<?> myType = Integer.TYPE;
        Class<?> myTypeDob = Double.TYPE;
        Cursor cursor = null;
        try {
            String sql = "select * from " + tableName;
            if (whereClause != null) {
                sql = sql + " " + whereClause;
            }
            cursor = database.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                for (Field field : t.getClass().getDeclaredFields()) {
                    if (!field.isSynthetic()) {
                        if (field.getType().isAssignableFrom(myType)) {
                            field.set(t, Integer.valueOf(cursor.getInt(cursor.getColumnIndex(field.getName()))));
                        } else if (field.getType().isAssignableFrom(myTypeDob)) {
                            field.set(t, Double.valueOf(cursor.getDouble(cursor.getColumnIndex(field.getName()))));
                        } else {
                            field.set(t, cursor.getString(cursor.getColumnIndex(field.getName())));
                        }
                    }
                }
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
        DatabaseManager.getInstance().closeDatabase();
        return t;
    }

    public static void deleteRecord(String tblName, String whereClause) {
        if (whereClause != null && whereClause.toLowerCase().contains("where")) {
            whereClause = whereClause.replace("where", "").replace("WHERE", "");
        }
        int delete = DatabaseManager.getInstance().openDatabase().delete(tblName, whereClause, null);
        DatabaseManager.getInstance().closeDatabase();
    }

    public static boolean updateData(String tableName, String whereClause, ContentValues values) {
        if (whereClause != null && whereClause.toLowerCase().contains("where")) {
            whereClause = whereClause.replace("where", "").replace("WHERE", "");
        }
        int rowID = DatabaseManager.getInstance().openDatabase().update(tableName, values, whereClause, null);
        DatabaseManager.getInstance().closeDatabase();
        return rowID > 0;
    }

    public static boolean updateData(String tableName, String whereClause, HashMap<String, Object> hashMap) {
        ContentValues contentValues = new ContentValues();
        for (Map.Entry<String, Object> entry : hashMap.entrySet()) {
            if (entry.getValue() instanceof Double) {
                contentValues.put(entry.getKey(), (Double) entry.getValue());
            } else if (entry.getValue() instanceof Integer) {
                contentValues.put(entry.getKey(), (Integer) entry.getValue());
            } else {
                contentValues.put(entry.getKey(), ((String) entry.getValue()) == null ? "" : (String) entry.getValue());
            }
        }
        return updateData(tableName, whereClause, contentValues);
    }

    public static <T> boolean updateData(String tableName, String whereClause, T t) {
        HashMap<String, Object> map = new HashMap<>();
        for (Field field : t.getClass().getDeclaredFields()) {
            try {
                if (!field.isSynthetic()) {
                    map.put(field.getName(), field.get(t));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return updateData(tableName, whereClause, map);
    }

    public static boolean isFileExist(String fileName) {
        return DatabaseManager.getInstance().openDatabase().rawQuery(new StringBuilder().append("select Type from Sticker where FileName = '").append(fileName).append("'").toString(), null).getCount() > 0;
    }
}
