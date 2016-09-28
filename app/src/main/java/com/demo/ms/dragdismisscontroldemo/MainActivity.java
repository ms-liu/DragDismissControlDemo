package com.demo.ms.dragdismisscontroldemo;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AnimationUtils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.animated.util.AnimatedDrawableUtil;
import com.facebook.imagepipeline.image.ImageInfo;

/**
 * ==============================================
 * 类名：主界面
 *
 * 作者：M-Liu
 *
 * 时间：2016/9/28
 *
 * 邮箱：ms_liu163@163.com
 * ==============================================
 */

@SuppressWarnings("unchecked")
public class MainActivity extends AppCompatActivity {
    public static final String URL = "http://c.hiphotos.baidu.com/image/pic/item/472309f7905298220099cbe5d2ca7bcb0a46d46a.jpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SimpleDraweeView simpleDraweeView = (SimpleDraweeView) findViewById(R.id.iv_photo);

        AbstractDraweeController controller = Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(true)
                .setControllerListener(new ControllerListener<ImageInfo>() {
                    @Override
                    public void onSubmit(String id, Object callerContext) {
//                        System.out.println(id+"=====onSubmit=============="+callerContext);
                    }

                    @Override
                    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
//                        double hypot = Math.hypot(simpleDraweeView.getWidth(), simpleDraweeView.getHeight());
//                        Animator circularReveal = ViewAnimationUtils.createCircularReveal(simpleDraweeView, (int) simpleDraweeView.getWidth()/2, (int) simpleDraweeView.getHeight()/2, 0, (int) hypot);
//                        circularReveal.setDuration(500);
//                        circularReveal.start();
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
                .setUri(Uri.parse(URL)).build();

        simpleDraweeView.setController(controller);

        simpleDraweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pair<View,String> pair = Pair.create((View) simpleDraweeView,"meizhi");
                startActivity(new Intent(MainActivity.this,SecondActivity.class),
                        ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,pair).toBundle());
            }
        });
    }
}
