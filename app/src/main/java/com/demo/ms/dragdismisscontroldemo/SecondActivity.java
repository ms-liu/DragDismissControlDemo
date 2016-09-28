package com.demo.ms.dragdismisscontroldemo;

import android.animation.Animator;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.transition.Explode;
import android.transition.Fade;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SecondActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        setTransition();
        final SimpleDraweeView simpleDraweeView = (SimpleDraweeView) findViewById(R.id.iv_big_photo);
        AbstractDraweeController controller = Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(true)
                .setControllerListener(new ControllerListener<ImageInfo>() {
                    @Override
                    public void onSubmit(String id, Object callerContext) {
                        System.out.println(id+"=====onSubmit=============="+callerContext);
                    }

                    @Override
                    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                        double hypot = Math.hypot(0, 1080);
                        Animator circularReveal = ViewAnimationUtils.createCircularReveal(simpleDraweeView, 720 / 2,1080 / 2, 0, (int) hypot);
                        circularReveal.setInterpolator(new AccelerateDecelerateInterpolator());
                        circularReveal.setDuration(500);
                        circularReveal.start();
//                        System.out.println(imageInfo.getWidth()+"=======onFinalImageSet========="+imageInfo.getHeight());
//                        System.out.println(id+"=====onFinalImageSet=============="+imageInfo+"=========="+animatable);
                    }

                    @Override
                    public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
//                        System.out.println(id+"=====onIntermediateImageSet=============="+imageInfo);
                    }

                    @Override
                    public void onIntermediateImageFailed(String id, Throwable throwable) {
//                        System.out.println(id+"=====onIntermediateImageFailed=============="+throwable.getMessage());
                    }

                    @Override
                    public void onFailure(String id, Throwable throwable) {
//                        System.out.println(id+"=====onFailure=============="+throwable.getMessage());
                    }

                    @Override
                    public void onRelease(String id) {
//                        System.out.println(id+"=====onRelease=============="+id);
                    }
                })
                .setUri(Uri.parse(
                        MainActivity.URL)).build();

        simpleDraweeView.setController(controller);
    }

    private void setTransition() {
        getWindow().setSharedElementEnterTransition(new ChangeBounds());
    }
}
