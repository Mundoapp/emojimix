package com.emojitwomix.adapters;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.emojitwomix.R;
import com.emojitwomix.activities.WrapContentDraweeView;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class Adapterestaticos extends RecyclerView.Adapter<Adapterestaticos.ViewHolder> {
    private final Context mContext;
    private RecyclerView slider;
    private LinearLayoutManager sliderLayoutManager;
    private final SharedPreferences sharedPref;
    private ArrayList<HashMap<String, Object>> data;
    RecyclerView.State state;

    public Adapterestaticos(ArrayList<HashMap<String, Object>> _arr, LinearLayoutManager layoutManager, Context context) {
        mContext = context;
        data = _arr;
        sliderLayoutManager = layoutManager;
        sharedPref = context.getSharedPreferences("AppData", Activity.MODE_PRIVATE);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.emojis_slider_item, parent, false));
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        View view = holder.itemView;
        String base = Objects.requireNonNull(data.get(position).get("base")).toString();
        String ojos = Objects.requireNonNull(data.get(position).get("ojos")).toString();
        String ojos_objetos = Objects.requireNonNull(data.get(position).get("ojos_objetos")).toString();



        String cejas = Objects.requireNonNull(data.get(position).get("cejas")).toString();
        String bocas = Objects.requireNonNull(data.get(position).get("bocas")).toString();

        String objetos = Objects.requireNonNull(data.get(position).get("objetos")).toString();

        String manos = Objects.requireNonNull(data.get(position).get("manos")).toString();

        String tipo = Objects.requireNonNull(data.get(position).get("tipo")).toString();


        String emoji_formado = Objects.requireNonNull(data.get(position).get("emoji_formado")).toString();
        String idemo = Objects.requireNonNull(data.get(position).get("Id")).toString();

        String emojiURL = "http://emojixer.com/panel/images_formas/";
        String emojiURL2 = "http://emojixer.com/panel/emoji_formado/";


        Log.e("TAG", "emoji posicion: "+position );

        holder.idemoji.setText(idemo);
        //agrego emoji mini en slider
        if(tipo.equals("emoji")) {
            loadEmojiFromUrl(holder.emoji, holder.progressBar, emojiURL + base);
            loadEmojiFromUrl(holder.mixedEmojiojos, holder.progressBar, emojiURL + ojos);
            loadEmojiFromUrl(holder.mixedEmojibocas, holder.progressBar, emojiURL + bocas);
            loadEmojiFromUrl(holder.mixedEmojicejas, holder.progressBar, emojiURL + cejas);
            loadEmojiFromUrl(holder.mixedEmojiobjetos, holder.progressBar, emojiURL + objetos);
            loadEmojiFromUrl(holder.mixedEmojiojos_objetos, holder.progressBar, emojiURL + ojos_objetos);
            loadEmojiFromUrl(holder.mixedEmojimanos, holder.progressBar, emojiURL + manos);

            loadEmojiFromUrl(holder.mixedformado, holder.progressBar, emojiURL2 + emoji_formado);

        }
        else {

             loadEmojiFromUrl(holder.mixedEmojiojos, holder.progressBar, emojiURL);
            loadEmojiFromUrl(holder.mixedEmojibocas, holder.progressBar, emojiURL);
            loadEmojiFromUrl(holder.mixedEmojicejas, holder.progressBar, emojiURL);
            loadEmojiFromUrl(holder.mixedEmojiobjetos, holder.progressBar, emojiURL);
            loadEmojiFromUrl(holder.mixedEmojiojos_objetos, holder.progressBar, emojiURL);
            loadEmojiFromUrl(holder.mixedEmojimanos, holder.progressBar, emojiURL);

            loadEmojiFromUrl(holder.emoji, holder.progressBar, emojiURL2 + emoji_formado);

        }
        holder.emoji.setOnClickListener(v -> {

            //scrollToCenter(view);
        });

        RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(_lp);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void loadEmojiFromUrl(ImageView image, CircularProgressIndicator progressBar, String url) {
        Glide.with(mContext)
                .load(url)
                .fitCenter()
                .transition(DrawableTransitionOptions.withCrossFade())
                .listener(
                        new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        }
                )
                .into(image);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        WrapContentDraweeView emoji,mixedEmojiojos,mixedEmojibocas,mixedEmojicejas,mixedEmojiojos_objetos,mixedEmojiobjetos,mixedEmojimanos,mixedformado;
        CircularProgressIndicator progressBar;
        TextView idemoji;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            emoji = itemView.findViewById(R.id.emoji);
            mixedEmojiojos = itemView.findViewById(R.id.mixedEmojiojos);
            mixedEmojibocas = itemView.findViewById(R.id.mixedEmojibocas);
            mixedEmojicejas = itemView.findViewById(R.id.mixedEmojicejas);
            mixedEmojiojos_objetos = itemView.findViewById(R.id.mixedEmojiojos_objetos);
            mixedEmojiobjetos = itemView.findViewById(R.id.mixedEmojiobjetos);
            mixedEmojimanos = itemView.findViewById(R.id.mixedEmojimanos);

            mixedformado = itemView.findViewById(R.id.mixedformado);

            idemoji = itemView.findViewById(R.id.idemojitxt);

            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView currentSlider) {
        super.onAttachedToRecyclerView(currentSlider);
        slider = currentSlider;
    }

    private void scrollToCenter(View v) {
        int itemToScroll = slider.getChildLayoutPosition(v);

//        int centerOfScreen = slider.getWidth() - v.getWidth();
       sliderLayoutManager.scrollToPositionWithOffset(itemToScroll, 50);
        final RecyclerView.State mState = new RecyclerView.State();
     //   sliderLayoutManager.smoothScrollToPosition(slider,state ,itemToScroll);
        Log.e("aki", "scrollToCenter: "+itemToScroll );

    }
}
