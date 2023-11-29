package com.emojixer.adapters;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.airbnb.lottie.LottieDrawable.INFINITE;
import static com.emojixer.activities.Activityestaticos.api_dominio;
import static com.emojixer.activities.MainActivity.APITOP;
import static com.emojixer.activities.MainActivity.FRAME_DELAY_MS;
import static com.emojixer.activities.MainActivity.NUM_FRAMES;
import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieCompositionFactory;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.emojixer.R;
import com.emojixer.activities.MainActivity;
import com.emojixer.activities.WrapContentDraweeView;
import com.emojixer.functions.FileUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.waynejo.androidndkgif.GifEncoder;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class EmojiTopAdapterestaticos extends RecyclerView.Adapter<EmojiTopAdapterestaticos.ViewHolder> {
    private final Context mContext;
    private RecyclerView slider;
    private LinearLayoutManager sliderLayoutManager;
    private final SharedPreferences sharedPref;
    boolean restraso = false;
    private static ArrayList<HashMap<String, Object>> data;

    public String API_formas = api_dominio+"/panel/images_formas/";
    public String API_manual = api_dominio+"/panel/images_manual/";
    public static String APITOP = api_dominio+"/panel/apitop.php?";

    public String API_formado = api_dominio+"/panel/emoji_formado/";
    private AlertDialog alertDialog;
    public WrapContentDraweeView sticker,sticker2;

    private int currentFrame = 0;
    private ArrayList<Bitmap> capturedImages = new ArrayList<>();
    private Handler captureHandler = new Handler();
    static {
        System.loadLibrary("gif_encoder");
    }
    LottieAnimationView lottieView,lodinglottie;
    ProgressBar progressdialog;
    ImageView capturedImageView;
    TextView texto,textotitulo;
    FrameLayout guardarbtn , progreso;
    RecyclerView.State state;
    Activity context;
    public EmojiTopAdapterestaticos(ArrayList<HashMap<String, Object>> _arr, LinearLayoutManager layoutManager, Context context) {
        mContext = context;
        data = _arr;
        sliderLayoutManager = layoutManager;
        sharedPref = context.getSharedPreferences("AppData", Activity.MODE_PRIVATE);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.emojis_top_item_estaticos, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        View view = holder.itemView;

        String tipo = Objects.requireNonNull(data.get(position).get("tipo")).toString();


        String emoji_formado = Objects.requireNonNull(data.get(position).get("emoji_formado")).toString();
        String emoji_formado2 = Objects.requireNonNull(data.get(position).get("emoji_formado2")).toString();

        String base = Objects.requireNonNull(data.get(position).get("base")).toString();

        Object animacionObj = data.get(position).get("animacion");
        String animacion = animacionObj != null ? animacionObj.toString() : "";

        String ojos = data.get(position).get("ojos") != null ? data.get(position).get("ojos").toString() : "";
        String ojos_objetos = data.get(position).get("ojos_objetos") != null ? data.get(position).get("ojos_objetos").toString() : "";
        String bocas = data.get(position).get("bocas") != null ? data.get(position).get("bocas").toString() : "";
        String objetos = data.get(position).get("objetos") != null ? data.get(position).get("objetos").toString() : "";
        String manos = data.get(position).get("manos") != null ? data.get(position).get("manos").toString() : "";
        String fondo = data.get(position).get("fondo") != null ? data.get(position).get("fondo").toString() : "";
        String extra = data.get(position).get("extra") != null ? data.get(position).get("extra").toString() : "";

        String rotation = data.get(position).get("rotation") != null ? data.get(position).get("rotation").toString() : "";
        String cejas = data.get(position).get("cejas") != null ? data.get(position).get("cejas").toString() : "";


        int ancho = parseInteger(data.get(position).get("width_height"));
        int left = parseInteger(data.get(position).get("margin_left"));
        int top = parseInteger(data.get(position).get("margin_top"));
     //   int rotation = parseInteger(data.get(position).get("rotation"));
        int votos = parseInteger(data.get(position).get("votos"));
        int emoji1 = parseInteger(data.get(position).get("id_emoji1"));
        int emoji2 = parseInteger(data.get(position).get("id_emoji2"));
        int random = parseInteger(data.get(position).get("random"));

        Log.e(TAG, "onSuccess: aki rota "+rotation);
        Context contexto = holder.itemView.getContext(); // Obtiene el contexto desde la vista del elemento de lista

        String imageURL = API_formado + emoji_formado;
        Glide.with(mContext)
                .load(imageURL)
                 .into(holder.femoji1);
        String imageURL2 = API_formado + emoji_formado2;
        Glide.with(mContext)
                .load(imageURL2)
                .into(holder.femoji2);

      //  holder.femoji1.setImageURI(Uri.parse(API_formado+emoji_formado));
        holder.votostxt.setText(formatNumber(votos));
        //alertdialog
        View dialogView = LayoutInflater.from(contexto).inflate(R.layout.dialog_layout, null);
        progressdialog = dialogView.findViewById(R.id.progressBar);
        sticker = dialogView.findViewById(R.id.sticker);

        capturedImageView = dialogView.findViewById(R.id.capturedImageView);
        textotitulo = dialogView.findViewById(R.id.textoti);

        texto = dialogView.findViewById(R.id.textoprogress);
        guardarbtn = dialogView.findViewById(R.id.guardarbtn);
        progreso = dialogView.findViewById(R.id.progreso);
        lodinglottie = dialogView.findViewById(R.id.lodinglottie);

        Activity activity = (Activity) contexto;

        holder.guardar.setOnClickListener(v -> {
         //   captureMultipleImages(holder.layoutEmojiCreation);

            File file = FileUtil.saveEmojiBIG(activity, holder.layoutEmojiCreation, contexto.getFilesDir() + "/stickers/");


            String toastText = "Saved emoji";
            Toast.makeText(contexto.getApplicationContext(), toastText, Toast.LENGTH_LONG).show();
            actualizovotos(emoji1,emoji2,activity);

        });


        if (Objects.equals(tipo, "manual")) {
            holder.mixedEmojibocas.setVisibility(View.GONE);
            holder.mixedEmojiojos.setVisibility(View.GONE);
            holder.mixedEmojiojos_objetos.setVisibility(View.GONE);
            holder.mixedEmojimanos.setVisibility(View.GONE);
            holder.mixedemojiforma.setVisibility(View.GONE);
            holder.mixedEmojiobjetos.setVisibility(View.GONE);

            holder.mixedEmojimanos2.setVisibility(View.GONE);
            holder.mixedEmojicejas.setVisibility(View.GONE);

            holder.mixedEmoji.setVisibility(View.VISIBLE);
            holder.mixedEmoji0.setVisibility(View.GONE);
            holder.posicionemoji.setRotation(0);

            holder.posicioncara.setRotation(0);

            int ancho3 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, contexto.getResources().getDisplayMetrics());


            holder.posicionemoji.setLayoutParams(new FrameLayout.LayoutParams(ancho3, ancho3));
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.posicionemoji.getLayoutParams();
            params.setMargins(0, 0, 0, 0);
            holder.posicionemoji.setLayoutParams(params);
            holder.mixedEmoji.setImageURI(Uri.parse(NULL));

            holder.mixedEmoji.setImageURI(Uri.parse(API_manual+base));

        }
        else {
            holder.mixedEmojicejas.setVisibility(View.VISIBLE);

            holder.mixedEmojibocas.setVisibility(View.VISIBLE);
            holder.mixedEmojiojos.setVisibility(View.VISIBLE);
            holder.mixedEmojiojos_objetos.setVisibility(View.VISIBLE);
            holder.mixedEmojimanos.setVisibility(View.VISIBLE);
            holder.mixedemojiforma.setVisibility(View.VISIBLE);
            holder.mixedEmojiobjetos.setVisibility(View.VISIBLE);


            holder.mixedEmoji.setVisibility(View.GONE);
            holder.mixedEmoji0.setVisibility(View.VISIBLE);





     if (Objects.equals(tipo, "objeto")) {

            if(emoji1==emoji2) {
                holder.mixedemojiforma.setVisibility(View.VISIBLE);

               holder.mixedemojiforma.setImageURI(Uri.parse(API_formas+extra));



                holder.mixedfondo.setVisibility(View.VISIBLE);

                holder.mixedfondo.setImageURI(Uri.parse(API_formas+fondo));


                Log.e("TAG", "aki condicion objeto igual: "+position );
            }
            else {

                int ancho2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ancho, contexto.getResources().getDisplayMetrics());
                int left2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, left, contexto.getResources().getDisplayMetrics());
                int top2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, top, contexto.getResources().getDisplayMetrics());
                int ancho3 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, contexto.getResources().getDisplayMetrics());


                holder.posicionemoji.setLayoutParams(new FrameLayout.LayoutParams(ancho2, ancho2));
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.posicionemoji.getLayoutParams();
                params.setMargins(left2, top2, 0, 0);
                holder.posicionemoji.setLayoutParams(params);
                holder.posicionemoji.setRotation(Float.parseFloat(rotation));
                Log.e("TAG", "aki condicion rotation objeto: " + position+ "fondo"+fondo+"extras "+extra );

                holder.mixedemojiforma.setVisibility(View.VISIBLE);
                holder.posicioncara.setRotation(0);

                holder.posicioncara.setLayoutParams(new FrameLayout.LayoutParams(ancho2, ancho2));
                ViewGroup.MarginLayoutParams params2 = (ViewGroup.MarginLayoutParams) holder.posicioncara.getLayoutParams();
                params.setMargins(left2, top2, 0, 0);
                holder.posicioncara.setLayoutParams(params2);


                holder.mixedemojiforma.setImageURI(Uri.parse(API_formas+extra));

                // mixedemojiforma.setImageURI(Uri.parse(extra));
                holder.mixedfondo.setVisibility(View.VISIBLE);

                holder.mixedfondo.setImageURI(Uri.parse(API_formas+fondo));

                holder.mixedemojiforma.setLayoutParams(new FrameLayout.LayoutParams(ancho3, ancho3));
                ViewGroup.MarginLayoutParams params4 = (ViewGroup.MarginLayoutParams) holder.mixedemojiforma.getLayoutParams();
                params4.setMargins(0, 0, 0, 0);
                holder.mixedemojiforma.setLayoutParams(params4);
                holder.mixedemojiforma.setRotation(0);
            }
        }
        else if (Objects.equals(tipo, "objetodoble")) {
            int ancho2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, contexto.getResources().getDisplayMetrics());
            int left2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, contexto.getResources().getDisplayMetrics());
            int top2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, top, contexto.getResources().getDisplayMetrics());
            int ancho3 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, contexto.getResources().getDisplayMetrics());


            holder.posicionemoji.setLayoutParams(new FrameLayout.LayoutParams(ancho2, ancho2));
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.posicionemoji.getLayoutParams();
           // params.setMargins(left2, top2, 0, 0);
            //holder.posicionemoji.setLayoutParams(params);
            holder.posicionemoji.setRotation(0);

            holder.mixedemojiforma.setVisibility(View.VISIBLE);
            holder.posicioncara.setRotation(0);

           // holder.mixedemojiforma.setImageURI(Uri.parse(extra));
            holder.mixedfondo.setVisibility(View.VISIBLE);

         holder.mixedemojiforma.setImageURI(Uri.parse(API_formas+extra));

         holder.mixedfondo.setImageURI(Uri.parse(API_formas+fondo));

         Log.e("TAG", "aki condicion objeto doble: "+position );

            holder.mixedemojiforma.setLayoutParams(new FrameLayout.LayoutParams(ancho3, ancho3));
            ViewGroup.MarginLayoutParams params4 = (ViewGroup.MarginLayoutParams) holder.mixedemojiforma.getLayoutParams();
            params4.setMargins(0, 0, 0, 0);
          //holder.mixedemojiforma.setLayoutParams(params4);
            holder.mixedemojiforma.setRotation(0);
        }

        else if (ancho > 0) {


            //emojis3 iguales y objeto es emojis3 champi frutass....
            if(Objects.equals(emoji1, emoji2) && Objects.equals(tipo, "emojis3")) {

                holder.posicionemoji.setRotation(0);
                holder.posicioncara.setRotation(Float.parseFloat(rotation));
                holder.mixedemojiforma.setVisibility(View.GONE);
                int ancho3 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, contexto.getResources().getDisplayMetrics());
                Log.e("TAG", "aki condicion rotation emojis3: " + position );
                holder.posicionemoji.setLayoutParams(new FrameLayout.LayoutParams(ancho3, ancho3));
                ViewGroup.MarginLayoutParams params2 = (ViewGroup.MarginLayoutParams) holder.posicionemoji.getLayoutParams();
                params2.setMargins(0, 0, 0, 0);
                holder.posicionemoji.setLayoutParams(params2);

                int ancho2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ancho, contexto.getResources().getDisplayMetrics());
                int left2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, left, contexto.getResources().getDisplayMetrics());
                int top2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, top, contexto.getResources().getDisplayMetrics());



                holder.posicioncara.setLayoutParams(new FrameLayout.LayoutParams(ancho2, ancho2));
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.posicioncara.getLayoutParams();
                params.setMargins(left2, top2, 0, 0);
                holder.posicioncara.setLayoutParams(params);
            }


            //emojis iguales y objeto es emojis
            else if(Objects.equals(emoji1, emoji2) && Objects.equals(tipo, "emoji")) {

                if(random ==1) {
//                  Careta version

                    holder.mixedemojiforma.setVisibility(View.VISIBLE);
                    // mixedemojiforma.setImageURI(Uri.parse(extra));
                    holder.mixedemojiforma.setImageURI(Uri.parse(API_formas + extra));

                    restraso = true;

                    //    Log.e("TAG", "aki igual emoji: "+extra);
                    int left2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, left, contexto.getResources().getDisplayMetrics());
                    int top2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, top, contexto.getResources().getDisplayMetrics());
                    int ancho3 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ancho, contexto.getResources().getDisplayMetrics());
                    int ancho4 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ancho - 50, contexto.getResources().getDisplayMetrics());

                    holder.mixedemojiforma.setLayoutParams(new FrameLayout.LayoutParams(ancho3, ancho3));
                    ViewGroup.MarginLayoutParams params2 = (ViewGroup.MarginLayoutParams) holder.mixedemojiforma.getLayoutParams();
                    params2.setMargins(0, top2, 0, 0);
                    holder.mixedemojiforma.setLayoutParams(params2);
                    holder.mixedemojiforma.setRotation(0);

                    Log.e("TAG", "aki condicion rotation careta: " + extra);
                    holder.posicionemoji.setLayoutParams(new FrameLayout.LayoutParams(ancho4, ancho4));
                    ViewGroup.MarginLayoutParams params3 = (ViewGroup.MarginLayoutParams) holder.posicionemoji.getLayoutParams();
                    params3.setMargins(left2, top2, 0, 0);
                    holder.posicionemoji.setLayoutParams(params3);
                    //   holder.posicionemoji.setVisibility(View.INVISIBLE);

                    holder.posicioncara.setLayoutParams(new FrameLayout.LayoutParams(ancho4, ancho4));
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.posicioncara.getLayoutParams();
                    params.setMargins(0, 0, 0, 0);
                    holder.posicioncara.setLayoutParams(params);


                    holder.posicioncara.setRotation(Float.parseFloat(rotation));
                   }
                    else if(random ==2) {

                    holder.mixedemojiforma.setVisibility(View.VISIBLE);
                    holder.mixedemojiforma.setImageURI(Uri.parse(API_formado+extra));
                        int left2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, left, contexto.getResources().getDisplayMetrics());
                        int top2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, top, contexto.getResources().getDisplayMetrics());
                        int ancho3 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ancho, contexto.getResources().getDisplayMetrics());

                    holder.mixedemojiforma.setLayoutParams(new FrameLayout.LayoutParams(ancho3-100, ancho3-100));
                        ViewGroup.MarginLayoutParams params2 = (ViewGroup.MarginLayoutParams) holder.mixedemojiforma.getLayoutParams();
                        params2.setMargins(ancho3-75, top2+top2, 0, 0);
                    holder.mixedemojiforma.setLayoutParams(params2);
                    holder.mixedemojiforma.setRotation(-Float.parseFloat(rotation));

                        Log.e("TAG", "aki rotation emoji doble: " + API_formas+extra );
                    holder.posicionemoji.setLayoutParams(new FrameLayout.LayoutParams(ancho3, ancho3));
                        ViewGroup.MarginLayoutParams params3 = (ViewGroup.MarginLayoutParams) holder.posicionemoji.getLayoutParams();
                        params3.setMargins(left2, top2, 0, 0);
                    holder.posicionemoji.setLayoutParams(params3);

                    holder.posicioncara.setLayoutParams(new FrameLayout.LayoutParams(ancho3, ancho3));
                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.posicioncara.getLayoutParams();
                        params.setMargins(0, 0, 0, 0);
                    holder.posicioncara.setLayoutParams(params);
                    holder.posicionemoji.setRotation(Float.parseFloat(rotation));
                    holder.posicioncara.setRotation(0);
                    }

            }


            else {


                holder.posicionemoji.setRotation(0);
                holder.posicioncara.setRotation(Float.parseFloat(rotation));
                holder.mixedemojiforma.setVisibility(View.GONE);
                int ancho3 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, contexto.getResources().getDisplayMetrics());
                holder.posicionemoji.setLayoutParams(new FrameLayout.LayoutParams(ancho3, ancho3));
                ViewGroup.MarginLayoutParams params2 = (ViewGroup.MarginLayoutParams) holder.posicionemoji.getLayoutParams();
                params2.setMargins(0, 0, 0, 0);
                holder.posicionemoji.setLayoutParams(params2);

                int ancho2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ancho, contexto.getResources().getDisplayMetrics());
                int left2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, left, contexto.getResources().getDisplayMetrics());
                int top2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, top, contexto.getResources().getDisplayMetrics());

                Log.e("TAG", "aki condicion normal con ancho: " + ancho );

                holder.posicioncara.setLayoutParams(new FrameLayout.LayoutParams(ancho2, ancho2));
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.posicioncara.getLayoutParams();
                params.setMargins(left2, top2, 0, 0);
                holder.posicioncara.setLayoutParams(params);


            }
            holder.mixedfondo.setVisibility(View.GONE);
        } else {

            int ancho2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, contexto.getResources().getDisplayMetrics());
            if (holder.posicionemoji != null) {
                holder.posicionemoji.setRotation(0);
            }

            if (holder.posicioncara != null) {
                holder.posicioncara.setRotation(0);
            }

            Log.e("TAG", "aki condicion emoji y objeto2: " + position );


            holder.posicionemoji.setLayoutParams(new FrameLayout.LayoutParams(ancho2, ancho2));
            ViewGroup.MarginLayoutParams params2 = (ViewGroup.MarginLayoutParams) holder.posicionemoji.getLayoutParams();
            params2.setMargins(0, 0, 0, 0);
            holder.posicionemoji.setLayoutParams(params2);

            if (holder.posicioncara != null) {
                holder.posicioncara.setLayoutParams(new FrameLayout.LayoutParams(ancho2, ancho2));
            }
            if (holder.posicioncara != null) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.posicioncara.getLayoutParams();
                // Realiza las operaciones que necesites con params aquí
                params.setMargins(0, 0, 0, 0);
                holder.posicioncara.setLayoutParams(params);
            }

            holder.mixedfondo.setVisibility(View.GONE);
            holder.mixedemojiforma.setVisibility(View.GONE);



        }



        String extension = "";

        int lastIndexOfDot = base.lastIndexOf('.');
        if (lastIndexOfDot != -1) {
            extension = base.substring(lastIndexOfDot + 1);
        }
        Log.e("TAG", "onSuccess: aki ojos"+ojos+position);





            holder.mixedEmoji.setVisibility(View.GONE);
            holder.mixedEmoji0.setVisibility(View.VISIBLE);
            if (Objects.equals(tipo, "objeto")) {

                holder.mixedEmoji0.setImageURI(Uri.parse(API_formado + base));
            }
            else
            {
                holder.mixedEmoji0.setImageURI(Uri.parse(API_formas + base));

            }
            Log.e("TAG", "onSuccess: aki es png ");



 //                mixedEmojiobjetos.setImageURI(Uri.parse(objetos));

            holder.mixedEmojiobjetos.setImageURI(Uri.parse(API_formas+objetos));

            holder.mixedEmojibocas.setImageURI(NULL);
            holder.mixedEmojibocas.setImageURI(Uri.parse(API_formas+bocas));


            holder.mixedEmojiojos.setImageURI(Uri.parse(API_formas+ojos));
            holder.mixedEmojiojos_objetos.setImageURI(Uri.parse(API_formas+ojos_objetos));

            holder.mixedEmojicejas.setImageURI(Uri.parse(API_formas+cejas));





        if (Objects.equals(emoji1, 26) || Objects.equals(emoji2, 26)) {
            holder.mixedEmojimanos.setVisibility(View.GONE);
            holder.mixedEmojimanos2.setVisibility(View.VISIBLE);
            holder.mixedEmojimanos2.setImageURI(Uri.parse(API_formas+manos));




        }
        else {
            holder.mixedEmojimanos.setVisibility(View.VISIBLE);
            holder.mixedEmojimanos2.setVisibility(View.GONE);
            Log.e("LottieError", "aki manos."+manos);
            holder.mixedEmojimanos.setImageURI(Uri.parse(API_formas+manos));


        }

        }




        String pos = String.valueOf(position+1);


        holder.idemoji.setText("Top "+ pos);
        //agrego emoji mini en slider


        RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(_lp);

    }
    public static String formatNumber(int number) {
        if (number < 1000) {
            return String.valueOf(number); // Números menores a 1000 se mantienen igual
        } else if (number < 1_000_000) {
            return String.format("%.1fK", number / 1000.0); // Números entre 1000 y 999,999 se abrevian en K
        } else {
            return String.format("%.1fM", number / 1_000_000.0); // Números mayores o iguales a 1,000,000 se abrevian en M
        }
    }

    private int parseInteger(Object obj) {
        if (obj != null) {
            try {
                return Integer.parseInt(obj.toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return 0; // Valor predeterminado si el objeto es nulo o no se puede analizar como entero.
    }



    private void captureNextFrame(FrameLayout layoutEmojiCreation, ArrayList<Bitmap> capturedImages,
                                  ImageView capturedImageView,Activity contexto) {
        capturedImageView.setVisibility(View.VISIBLE);
        sticker.setVisibility(View.INVISIBLE);
        if (currentFrame < NUM_FRAMES) {

            Pair<Bitmap, Bitmap> bitmaps = FileUtil.captureFrameLayout(layoutEmojiCreation);
            Bitmap originalBitmap = bitmaps.first;
            Bitmap convertedBitmap = bitmaps.second;

            //     Bitmap bitmap = getBitmapFromView(layoutEmojiCreation);

// Agrega el bitmap convertido al array
            capturedImages.add(convertedBitmap);
//            Bitmap image = captureFrameLayout(layoutEmojiCreation);
//            capturedImages.add(image);
            int finalI = currentFrame;
// Define el número total deseado
            int total = 100;

// Luego, en tu código donde actualizas el progreso y el texto:
            contexto.runOnUiThread(() -> {

                //  Log.e("TAG", "captureMultipleImages2: " + currentFrame);

                capturedImageView.setImageBitmap(originalBitmap);

                // Calcula el progreso actual basado en los elementos actuales y el número total deseado
                int progress = (currentFrame * total) / 50; // Suponiendo que tienes 50 elementos

                progressdialog.setProgress(progress);

                // Muestra el texto con el progreso actual y el número total deseado
                texto.setText(progress + "/" + total);
            });


            currentFrame++;
            if(currentFrame==50){
                context.runOnUiThread(() -> {
                    textotitulo.setText("Convirtiendo archivo");
                    texto.setText("Espera...");
                });
            }

            // Programar la captura del siguiente fotograma
            captureHandler.postDelayed(() -> captureNextFrame(layoutEmojiCreation, capturedImages, capturedImageView,contexto), FRAME_DELAY_MS);
        } else {
//            lodinglottie.setVisibility(View.VISIBLE);
//            lodinglottie.setAnimation(R.raw.loading);
//
//            lodinglottie.playAnimation();


//            context.runOnUiThread(() -> {
//                marca.setVisibility(View.INVISIBLE);
//
//            });


            final File[] file = new File[1];
            //  guardarbtn.setVisibility(View.VISIBLE);
            // Resto de tu código para generar el GIF y mostrarlo en DisplayBitmapActivity
            String gifPath = new File(contexto.getFilesDir(), "generatedGif_" + System.currentTimeMillis() + ".gif").getAbsolutePath();

            //  file[0] = FileUtil.onCreateWebpFile(capturedImages, MainActivity.this, getFilesDir() + "/stickers");


            file[0] = generateGIF(capturedImages, gifPath);

            MainActivity tuClase = new MainActivity();
            Bitmap[] bitmapArray = capturedImages.toArray(new Bitmap[capturedImages.size()]);
            Log.e(TAG, "captureNextFrame: "+capturedImages.size()+" otro"+bitmapArray.length );
            String webpPath = new File(contexto.getFilesDir()+ "/stickers/", "AImage" + System.currentTimeMillis() + ".webp").getAbsolutePath();

            File webpfilePath = new File(contexto.getFilesDir()+ "/stickers/", "AImage" + System.currentTimeMillis() + ".webp");

            int result = tuClase.convertFrameToWebP(bitmapArray, webpPath, 1);
            if (result == 0) {
                progreso.setVisibility(View.INVISIBLE);
                alertDialog.setTitle("Guardado correctamente");
                // sticker.setImageURI(webpPath);
                Uri uri = Uri.fromFile(new File(webpfilePath.getAbsolutePath()));


                sticker.setImageURI(uri);

                Log.e(TAG, "path: "+Uri.parse(webpfilePath.getAbsolutePath()) );

//
                capturedImageView.setVisibility(View.INVISIBLE);
                sticker.setVisibility(View.VISIBLE);

             }
            capturedImages.clear();
        }
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
    @SuppressLint("SuspiciousIndentation")
    private void actualizovotos(Integer idemote1, Integer idemote2, Activity contexto){
        String fondoTOP = "";
        String bocaTOP = "";
        String objetosTOP = "";
        String manosTOP = "";
        String ojos_objetosTOP = "";

        RequestQueue queue = Volley.newRequestQueue(contexto);
        Log.e(TAG, "actualizovotos: "+APITOP+"emoji1="+idemote1+"&"+"emoji2="+idemote2);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, APITOP+"emoji1="+idemote1+"&"+"emoji2="+idemote2+"&actualizotop=1",
                (String) null, new Response.Listener<JSONObject>() {
            //  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(JSONObject response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(request);

    }

    private void loadEmojiFromUrl(ImageView image,  String url) {
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
                                return false;
                            }
                        }
                )
                .into(image);
    }
    public File generateGIF(ArrayList<Bitmap> frames, String path) {
        if (frames.size() == 0) return null;

        int width = frames.get(0).getWidth();
        int height = frames.get(0).getHeight();

        GifEncoder encoder = new GifEncoder();
        try {
            encoder.init(width, height, path, GifEncoder.EncodingType.ENCODING_TYPE_SIMPLE_FAST);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Configurar la velocidad de reproducción del GIF (en milisegundos)
        int frameDelay = FRAME_DELAY_MS; // 30 FPS (ajusta según tu FPS deseado)

        for (Bitmap bitmap : frames) {
            encoder.encodeFrame(bitmap, 55);
            //   Log.e(TAG, "generateGIF: " );
        }

        encoder.close();
        // Devolver el archivo del GIF generado
        return new File(path);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        WrapContentDraweeView mixedfondo,marca,mixedEmojiojos,mixedEmojibocas,mixedEmojicejas,mixedEmojiojos_objetos,mixedEmojiobjetos,mixedEmojimano,mixedemojiformas,mixedEmojimanos,mixedEmoji,mixedEmojimanos2,mixedemojiforma;
        WrapContentDraweeView emoji,mixedEmoji0;
        TextView idemoji,votostxt;
        FrameLayout posicioncara;
        FrameLayout posicionemoji,layoutEmojiCreation;
        ImageView femoji1, femoji2,guardar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            femoji1 = itemView.findViewById(R.id.femoji1);
            femoji2 = itemView.findViewById(R.id.femoji2);
            layoutEmojiCreation =  itemView.findViewById(R.id.frame_emoji_creation);
            guardar = itemView.findViewById(R.id.guardar);
            posicioncara =  itemView.findViewById(R.id.posicioncara);


            emoji = itemView.findViewById(R.id.emoji);
            mixedEmojiojos = itemView.findViewById(R.id.mixedEmojiojos);
            mixedEmojibocas = itemView.findViewById(R.id.mixedEmojibocas);
            mixedEmojicejas = itemView.findViewById(R.id.mixedEmojicejas);
            mixedEmojiojos_objetos = itemView.findViewById(R.id.mixedEmojiojos_objetos);
            mixedEmojiobjetos = itemView.findViewById(R.id.mixedEmojiobjetos);
            mixedEmojimanos = itemView.findViewById(R.id.mixedEmojimanos);
            posicionemoji = itemView.findViewById(R.id.posicionemoji);
            votostxt =  itemView.findViewById(R.id.votos);
            marca = itemView.findViewById(R.id.marca);

            mixedEmoji = itemView.findViewById(R.id.mixedEmoji);
            mixedEmoji0 = itemView.findViewById(R.id.mixedEmoji0);

            mixedEmojimanos2 = itemView.findViewById(R.id.mixedEmojimanos2);

            mixedemojiforma = itemView.findViewById(R.id.emojiforma);
            mixedfondo= itemView.findViewById(R.id.emojifondo);

            idemoji = itemView.findViewById(R.id.idemojitxt);

        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView currentSlider) {
        super.onAttachedToRecyclerView(currentSlider);
        slider = currentSlider;
    }

}
