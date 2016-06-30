package utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.media.FaceDetector;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;

import com.example.mystudy.MainActivity;
import com.example.mystudy.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.AutoRotateDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;

/**
 * Created by Administrator on 2016/6/29.
 */
public class ImageloadFresco {
    private SimpleDraweeView mSimpleDraweeView;
    private Context mContext;

    private ImageloadFresco(ImageloadFrescoBuilder builder){
        this.mContext = builder.mContext;
        this.mSimpleDraweeView = builder.mSimpleDraweeView;
        ImageRequest request = null;

        if(builder.isFaceDetact){
            request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(builder.uri))
                    .setPostprocessor(getFaceDetact())
                    .build();
        }else {
            request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(builder.uri))
                    .build();
        }

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(mSimpleDraweeView.getController())
                .setImageRequest(request)
                .setTapToRetryEnabled(true)
                .build();

        GenericDraweeHierarchyBuilder builderM = new GenericDraweeHierarchyBuilder(mContext.getResources())
                .setProgressBarImage(builder.mProgressBarImage)
                .setActualImageFocusPoint(new PointF(1f,1f));


        mSimpleDraweeView.setHierarchy(builderM.build());
        mSimpleDraweeView.setController(controller);
    }

    private Postprocessor getFaceDetact(){
        return new BasePostprocessor() {
            @Override
            public void process(Bitmap bitmap) {
                super.process(bitmap);
                final PointF pointF = faceDetact(bitmap);
                if(pointF.x != 0){
                    ((MainActivity)mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mSimpleDraweeView
                                    .getHierarchy()
                                    .setActualImageFocusPoint(pointF);
                        }
                    });
                }
            }
        };
    }

    private PointF faceDetact(Bitmap bitmap) {
        PointF pointF = new PointF(0,0);
        Bitmap bitmap1 = bitmap.copy(Bitmap.Config.RGB_565, true);
        float width = bitmap1.getWidth();
        float height = bitmap1.getHeight();
        Log.i("AAA","bitmap"+width+"   "+height);
        FaceDetector faceDetector = new FaceDetector((int)width,(int)height,1);
        FaceDetector.Face[] face = new FaceDetector.Face[1];
        int nFace = faceDetector.findFaces(bitmap1, face);
        if(nFace > 0){
            FaceDetector.Face f = face[0];
            f.getMidPoint(pointF);
            Log.i("AAA","point"+pointF.x+"   "+pointF.y);
        }
        float x = pointF.x/width;
        float y = pointF.y/height;
        Log.i("AAA","xy"+ x+"   "+y);
        return new PointF(x,y);
    }

    public static class ImageloadFrescoBuilder{
        private String uri;
        private Context mContext;
        private SimpleDraweeView mSimpleDraweeView;

        private boolean isFaceDetact;


        private Drawable mProgressBarImage;//loading图
        private Drawable mRetryImage;//占位图


        public ImageloadFrescoBuilder(String uri, Context mContext, SimpleDraweeView mSimpleDraweeView) {
            this.uri = uri;
            this.mContext = mContext;
            this.mSimpleDraweeView = mSimpleDraweeView;
        }

        public ImageloadFrescoBuilder setProgressBarImage(Drawable drawable){
            if(drawable == null){
                Drawable drawable1 = DrawableCompat.wrap(ContextCompat.getDrawable(mContext, R.drawable.ic_toys_black_48dp).mutate());
                DrawableCompat.setTintList(drawable1, ContextCompat.getColorStateList(mContext, R.color.tint_list_pink));
                mProgressBarImage = new AutoRotateDrawable(drawable1,1500);
            }else{
                mProgressBarImage = drawable;
            }
            return this;
        }

        public ImageloadFrescoBuilder setRetryImage(Drawable drawable){
            this.mRetryImage = drawable;
            return this;
        }

        public ImageloadFrescoBuilder setIsFaceDetact(boolean flag){
            this.isFaceDetact = flag;
            return this;
        }

        public ImageloadFresco build(){
            return new ImageloadFresco(this);
        }
    }
}
