package com.emojixer.activities;

import static com.facebook.drawee.backends.pipeline.Fresco.hasBeenInitialized;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.emojixer.R;
import com.emojixer.functions.GlobalClass;
import com.emojixer.functions.Utility;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.core.ImagePipeline;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdapterStickers extends RecyclerView.Adapter<AdapterStickers.MViewHolder>
{
    private LayoutInflater layoutInflater;
    private Activity mContext;
    public static List<String> stickersWhassap= new ArrayList<>();

    public static void readStickerWhassap(Context context, String filename) {
        List<File> item = new ArrayList<>();
        File f = new File(context.getFilesDir(), "stickers");
        File[] files = f.listFiles();
        if (files != null) {
            long maxSize = 500 * 1024; // 500 KB en bytes
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (!file.isDirectory()) {
                    long fileSize = file.length(); // Tamaño del archivo en bytes
                    if (fileSize <= maxSize) {
                        item.add(file);
                        Log.e("Files aki", file.getName());

                    }
                }
            }

            for (int i = 0; i < item.size() - 1; i++) {
                for (int j = i + 1; j < item.size(); j++) {
                    if (item.get(i).lastModified() > item.get(j).lastModified()) {
                        File variableauxiliar = item.get(i);
                        item.set(i, item.get(j));
                        item.set(j, variableauxiliar);
                    }
                }
            }

            List<String> salida = new ArrayList<>();

            if (files != null) {
                for (File file : files) {
                    if (!file.isDirectory() && file.getName().contains(filename)) {
                        long fileSize = file.length(); // Tamaño del archivo en bytes
                        if (fileSize <= maxSize) {
                            salida.add(file.getName());
                            Log.e("Files aki", file.getName());
                        }
                    }
                }
            }
            stickersWhassap = salida;
        }
    }

    public static void deleteStickerWhassap(Context context)
    {
        List<File> item = new ArrayList<>();
        File f =  new File(context.getFilesDir(), "stickers");
        File[] files = f.listFiles();
        if(files!=null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (!file.isDirectory())
                    file.delete();
            }
        }
    }
    protected final class MViewHolder extends RecyclerView.ViewHolder {
        public WrapContentDraweeView sticker;
      //  public GifImageView sticker;
        public ImageView compartir,download;
        public ImageView borrar;
        public LinearLayout bloquesticker;
        public MViewHolder(View view) {
            super(view);
            this.sticker = view.findViewById(R.id.sticker);
            this.compartir = view.findViewById(R.id.compartir);
          //  this.download = view.findViewById(R.id.download);

            this.borrar = view.findViewById(R.id.borrar);

            this.bloquesticker = view.findViewById(R.id.bloquesticker);

        }
    }

    public AdapterStickers(@NonNull final LayoutInflater layoutInflater, Activity context, String filename) {
        this.layoutInflater = layoutInflater;
        this.mContext = context;
        readStickerWhassap(context,filename);
    }

    public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (!hasBeenInitialized()) {
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            imagePipeline.clearMemoryCaches();
            imagePipeline.clearDiskCaches();
            imagePipeline.clearCaches();
        }
            View itemView = layoutInflater.inflate(R.layout.activity_vista_sticker, parent, false);
            MViewHolder vh = new MViewHolder(itemView);
            return vh;

    }

    @Override
    public void onBindViewHolder(final MViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        Uri uri = Uri.fromFile(new File(mContext.getFilesDir() + "/stickers/" + stickersWhassap.get(position)));
        Uri urigif = Uri.fromFile(new File(mContext.getFilesDir() + "/" + stickersWhassap.get(position)));


        String extension = stickersWhassap.get(position).substring(stickersWhassap.get(position).lastIndexOf("."));
     //   ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithResourceId(R.drawable.base25).build();
        Log.e("files","Gif detected "+uri.getPath());
        // Verificar el tamaño del archivo
        File file = new File(uri.getPath());
        long fileSize = file.length(); // Tamaño del archivo en bytes
        long maxSize = 500 * 1024; // 500 KB en bytes

        if (fileSize <= maxSize) {
            // El archivo es menor o igual a 500 KB, lo mostramos
            viewHolder.sticker.setImageURI(uri);
            // Resto del código para compartir y otras operaciones
        } else {
            // El archivo es mayor a 500 KB, no lo mostramos
            viewHolder.bloquesticker.setVisibility(View.GONE);
        }

        if(extension.equals(".gif"))
        {
            Log.e("files","Gif detected "+uri.getPath());
            DraweeController controller =
                    Fresco.newDraweeControllerBuilder()
                            .setUri(uri)
                            .setAutoPlayAnimations(true)
                            .build();
            viewHolder.sticker.setController(controller);
           /* Glide.with( mContext )
                    .asGif()
                    .load(uri)
                    .into( viewHolder.sticker );*/
           // Glide.with(mContext).load(R.drawable.base25).into(viewHolder.sticker);
         //   viewHolder.sticker.setImageURI(Uri.parse("file://" + uri.getPath()));
          //  viewHolder.sticker.setImageURI(uri);
        }


        viewHolder.sticker.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                dialogoImagen(position);
            }
        });


        viewHolder.compartir.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                Log.e("TAG", "aki gif onClick: "+urigif );

                try {
                    Utility.shareFile(mContext,new File(urigif.getPath()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        viewHolder.borrar.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {



                File fdelete = new File(uri.getPath());
                if (fdelete.exists()) {
                    if (fdelete.delete()) {
                        System.out.println("file Deleted :" + uri.getPath());
                        ImageViewerActivity.actualizar();
                        viewHolder.bloquesticker.setVisibility(View.GONE);
                    } else {
                        System.out.println("file not Deleted :" + uri.getPath());
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return stickersWhassap.size();
    }
    private void dialogoImagen(int pos) {
        final Dialog dialog = new Dialog(mContext);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.boder);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dilog_listaimagenes);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());

        Rect displayRectangle = new Rect();
        Window window = mContext.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        try {
            if(stickersWhassap.get(pos)!=null) {
                Uri uri = Uri.fromFile(new File(mContext.getFilesDir() + "/stickers/" + stickersWhassap.get(pos)));
                int bitmapHeight = 450;
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
                    if (bitmap != null) {
                        bitmapHeight = bitmap.getHeight();
                }
                float diferencia = ((float) displayRectangle.height() / (float) bitmapHeight);
                if (diferencia > 1.9)
                    diferencia = (((float) bitmapHeight * 1.9f) / (float) displayRectangle.height());
                else if (diferencia > 1)
                    diferencia = ((float) bitmapHeight / (float) displayRectangle.height());
                lp.width = (int) (displayRectangle.width() * 0.9f);
                lp.height = (int) (displayRectangle.height() * diferencia);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ViewPager viewPager = (ViewPager) dialog.findViewById(R.id.image_slider);
        ImagePagerAdapter myPager = new ImagePagerAdapter(mContext);
        viewPager.setAdapter(myPager);
        viewPager.setCurrentItem(pos);

        ImageView flecha_anterior = dialog.findViewById(R.id.flecha_anterior);
        ImageView flecha_sigiente = dialog.findViewById(R.id.flecha_sigiente);

        dialog.show();
        dialog.getWindow().setAttributes(lp);

        if (viewPager.getCurrentItem() == 0)
            flecha_anterior.setVisibility(View.INVISIBLE);
        if (stickersWhassap.size() - 1 == viewPager.getCurrentItem())
            flecha_sigiente.setVisibility(View.INVISIBLE);

        flecha_anterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            }
        });
        flecha_sigiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i > 0 && i < stickersWhassap.size() - 1) {
                    flecha_anterior.setVisibility(View.VISIBLE);
                    flecha_sigiente.setVisibility(View.VISIBLE);
                } else if (i == 0) {
                    flecha_anterior.setVisibility(View.INVISIBLE);
                    flecha_sigiente.setVisibility(View.VISIBLE);
                }else if (i == stickersWhassap.size() - 1) {
                    flecha_sigiente.setVisibility(View.INVISIBLE);
                    flecha_anterior.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }
    private class ImagePagerAdapter extends PagerAdapter {
        private Activity context;

        public ImagePagerAdapter(Activity context) {
            super();
            this.context = context;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(context).inflate(R.layout.dialogo_trasparente_imagen, null);
            WrapContentDraweeView imageView = view.findViewById(R.id.sticker_preview);
            Uri uri = Uri.fromFile(new File(mContext.getFilesDir() + "/stickers/" + stickersWhassap.get(position)));
            // Verificar el tamaño del archivo
            File file = new File(uri.getPath());
            long fileSize = file.length(); // Tamaño del archivo en bytes
            long maxSize = 500 * 1024; // 500 KB en bytes

            if (fileSize <= maxSize) {
                // El archivo es menor o igual a 500 KB, lo mostramos
                imageView.setImageURI(uri);
                container.addView(view);
                // Resto del código para compartir y otras operaciones
                view.findViewById(R.id.compartir).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (new File(uri.getPath()).getName().contains("AImage")) {
                            Bitmap transBmp = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888);
                            transBmp.eraseColor(Color.WHITE);
                            Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());
                            transBmp = GlobalClass.mergeToPin(transBmp, bitmap, mContext);
                            if (transBmp != null)
                                GlobalClass.compartir(mContext, transBmp);
                        } else {
                            try {
                                Utility.shareFile(context, new File(uri.getPath()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            } else {
                // El archivo es mayor a 500 KB, no lo mostramos
                container.removeView(view);

            }

            return view;
        }



        @Override
        public void destroyItem(ViewGroup container, int position, Object view) {
            container.removeView((View) view);
        }

        @Override
        public int getCount() {
            return stickersWhassap.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object == view;
        }
    }
}