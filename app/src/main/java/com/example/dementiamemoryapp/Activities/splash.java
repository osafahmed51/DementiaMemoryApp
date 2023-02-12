package com.example.dementiamemoryapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dementiamemoryapp.R;

public class splash extends AppCompatActivity {


    Animation animation;
    ImageView logoImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        logoImage=findViewById(R.id.logo);

        getSupportActionBar().hide();


        rotateAnimiation();
        logoImage.animate().translationY(-3000).setDuration(1000).setStartDelay(3500);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intentsplash=new Intent(splash.this,HomeActivity.class);
                startActivity(intentsplash);
                finish();
            }
        },2500);


    }

    private void rotateAnimiation() {
        animation= AnimationUtils.loadAnimation(this,R.anim.anim);
        logoImage.startAnimation(animation);
    }

}