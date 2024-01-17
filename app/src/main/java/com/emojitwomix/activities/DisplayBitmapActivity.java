package com.emojitwomix.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.emojitwomix.R;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class DisplayBitmapActivity extends AppCompatActivity {
    private GifImageView gifImageView;
    private Button button;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_bitmap);
        gifImageView = findViewById(R.id.gifImageView);
        button = findViewById(R.id.button);

        button.setOnClickListener(view -> {
            Intent intent = new Intent(DisplayBitmapActivity.this, MainActivity.class);
            startActivity(intent);

        });
        Intent intent = getIntent();

        // 2. Extraer el String asociado con la clave "gif_path"
        String gifPath = intent.getStringExtra("gif_path");
        GifDrawable gifDrawable = null;
        try {
            gifDrawable = new GifDrawable(gifPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        gifImageView.setImageDrawable(gifDrawable);



    }

}
