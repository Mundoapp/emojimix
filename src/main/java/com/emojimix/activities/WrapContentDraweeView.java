package com.emojimix.activities;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

public class WrapContentDraweeView extends SimpleDraweeView {

    private Context context=null;
    private final ControllerListener listener = new BaseControllerListener<ImageInfo>() {
        @Override
        public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
            updateViewSize(imageInfo);
        }

        @Override
        public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
            updateViewSize(imageInfo);
        }
    };

    public WrapContentDraweeView(Context context) {
        super(context);
        this.context=context;
    }

    public WrapContentDraweeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    public WrapContentDraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context=context;
    }

    @Override
    public void setImageURI(Uri uri, Object callerContext) {
        DraweeController controller = ((PipelineDraweeControllerBuilder)getControllerBuilder())
                .setControllerListener(listener)
                .setCallerContext(callerContext)
                .setUri(uri)
                .setOldController(getController())
                .setAutoPlayAnimations(true)
                .build();
        setController(controller);
    }

    void updateViewSize(@Nullable ImageInfo imageInfo) {
        if (imageInfo != null) {
            int width = imageInfo.getWidth();
            if(context!=null) {
                width=context.getResources().getDisplayMetrics().widthPixels;
            }
            if(getLayoutParams().height== ViewGroup.LayoutParams.WRAP_CONTENT) {
                if (imageInfo.getWidth() > width) {
                    getLayoutParams().width = (int)(width* 0.6f);
                    getLayoutParams().height = (int)(width* 0.6f);
                } else {
//                    getLayoutParams().width = imageInfo.getWidth();
//                    getLayoutParams().height = imageInfo.getWidth();
                    getLayoutParams().width = (int)(width* 0.7f);;
                    getLayoutParams().height = (int)(width* 0.7f);;
                }
            }
//            Log.e("width --->",""+getLayoutParams().width);
//            Log.e("height --->",""+getLayoutParams().height);
            setAspectRatio((float) imageInfo.getWidth() / imageInfo.getHeight());
        }
    }
}
