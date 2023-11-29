package com.emojixer.adapters;


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
import com.emojixer.R;
import com.emojixer.activities.WrapContentDraweeView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.slider.Slider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class EmojimixerAdapterAnimated extends RecyclerView.Adapter<EmojimixerAdapterAnimated.ViewHolder> {
    private final Context mContext;
    private RecyclerView slider;
    private LinearLayoutManager sliderLayoutManager;
    private final SharedPreferences sharedPref;
    private static ArrayList<HashMap<String, Object>> data;
    RecyclerView.State state;

    public EmojimixerAdapterAnimated(ArrayList<HashMap<String, Object>> _arr, LinearLayoutManager layoutManager, Context context) {
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

        String tipo = Objects.requireNonNull(data.get(position).get("tipo")).toString();


        String emoji_formado = Objects.requireNonNull(data.get(position).get("emoji_formado")).toString();

        String emojiURL2 = "http://animated.emojixer.com/panel/emoji_formado/";

        String pos = String.valueOf(getEmojiValue(position));


        holder.idemoji.setText(pos);
        //agrego emoji mini en slider
        if(tipo.equals("emoji")) {
            loadEmojiFromUrl(holder.emoji, holder.progressBar, emojiURL2 + emoji_formado);


        }
        else {


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
    public static String getEmojiValue(int position) {

        if (position >= 0 && position < data.size()) {

            return  Objects.requireNonNull(data.get(position).get("Id")).toString();
        }
        return null; // Devuelve nulo si la posición es inválida    }

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
        private Slider slider;

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
