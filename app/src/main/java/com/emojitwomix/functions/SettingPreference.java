package com.emojitwomix.functions;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingPreference {
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;

    public SettingPreference(Context activity) {
        this.preferences = activity.getSharedPreferences("SETTINGS", 0);
        editor = this.preferences.edit();
    }

    public void clearPreference() {
        this.editor.clear();
        this.editor.commit();
    }

    public void setAdStatus(boolean _ads) {
        this.editor.putBoolean("ADMOB", _ads);
        this.editor.commit();
    }

    public boolean isAdShow() {
        return this.preferences.getBoolean("ADMOB", true);
    }

    public void setAdPurchased(boolean _ads) {
        this.editor.putBoolean("Purchased", _ads);
        this.editor.commit();
    }

    public boolean isAdPurchased() {
        return this.preferences.getBoolean("Purchased", false);
    }
}
