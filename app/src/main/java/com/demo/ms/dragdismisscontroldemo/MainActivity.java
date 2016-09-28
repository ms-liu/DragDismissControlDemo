package com.demo.ms.dragdismisscontroldemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;

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
        simpleDraweeView.setImageURI(Uri.parse(URL));
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
