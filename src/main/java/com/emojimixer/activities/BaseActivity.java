package com.emojimixer.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.emojimixer.functions.SettingPreference;

public class BaseActivity extends AppCompatActivity {
    public boolean isActivityVisible;
    public SettingPreference settingPreference;
    private Toolbar toolbar;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar();
        this.settingPreference = new SettingPreference(getApplicationContext());
    }

    private void hideStatusBar() {
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
    }

    /* access modifiers changed from: protected */
    public void showBackOnToolbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }



    public void setContentView(@LayoutRes int layoutResID) {
//        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.setContentView(layoutResID);


    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        this.isActivityVisible = true;
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        this.isActivityVisible = false;
        super.onPause();
    }

    public void onBack() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }



    public int getResourceId(String drawableRes) {
        return getResources().getIdentifier(drawableRes, "drawable", getPackageName());
    }

    public Typeface getTypeface() {
        return Typeface.createFromAsset(getAssets(), "font/font13.otf");
    }
}
