package com.demo.ms.dragdismisscontroldemo;

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

import com.facebook.drawee.view.SimpleDraweeView;
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SecondActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        setTransition();
        SimpleDraweeView viewById = (SimpleDraweeView) findViewById(R.id.iv_big_photo);
        viewById.setImageURI(Uri.parse(MainActivity.URL));
    }

    private void setTransition() {
        getWindow().setSharedElementEnterTransition(new ChangeBounds());
    }
}
