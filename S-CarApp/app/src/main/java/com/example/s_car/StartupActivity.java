package com.example.s_car;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class StartupActivity extends AppCompatActivity {

    ImageView imageView;
    long time = 5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        imageView = (ImageView) findViewById(R.id.startupWhiteCover);
        Animation();
    }
    public void Animation(){
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView,"x",1000f);
        animator.setDuration(time);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator);
        animatorSet.start();
    }
}