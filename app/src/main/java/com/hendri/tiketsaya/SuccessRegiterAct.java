package com.hendri.tiketsaya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SuccessRegiterAct extends AppCompatActivity {

    Button btn_explore;
    Animation app_splash, buttomtotop, toptobottom;
    ImageView icon_success;
    TextView app_title, app_subtitle_SR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_regiter);

        btn_explore = findViewById(R.id.btn_explore);
        icon_success = findViewById(R.id.icon_success);
        app_title = findViewById(R.id.app_title);
        app_subtitle_SR = findViewById(R.id.app_subtitle_SR);

        // load animations
        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        buttomtotop = AnimationUtils.loadAnimation(this, R.anim.bottomtotop);
        toptobottom = AnimationUtils.loadAnimation(this, R.anim.toptobottom);

        // run animations
        btn_explore.startAnimation(buttomtotop);
        icon_success.startAnimation(app_splash);
        app_title.startAnimation(toptobottom);
        app_subtitle_SR.startAnimation(toptobottom);

        btn_explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gogohome =  new Intent(SuccessRegiterAct.this, HomeAct.class);
                startActivity(gogohome);
            }
        });
    }
}