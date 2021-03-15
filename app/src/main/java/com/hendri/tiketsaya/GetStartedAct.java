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

public class GetStartedAct extends AppCompatActivity {

    Button bt_signin,btn_new_account_create;
    Animation toptobottom, bottomtotop;
    ImageView emblem_app;
    TextView intro_app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        // load animations
        toptobottom = AnimationUtils.loadAnimation(this, R.anim.toptobottom);
        bottomtotop = AnimationUtils.loadAnimation(this, R.anim.bottomtotop);

        bt_signin = findViewById(R.id.bt_signin);
        btn_new_account_create = findViewById(R.id.btn_new_account_create);

        emblem_app = findViewById(R.id.emblem_app);
        intro_app = findViewById(R.id.intro_app);

        // run animations
        emblem_app.startAnimation(toptobottom);
        intro_app.startAnimation(toptobottom);

        bt_signin.startAnimation(bottomtotop);
        btn_new_account_create.startAnimation(bottomtotop);

        bt_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotosign = new Intent(GetStartedAct.this, SigninAct.class);
                        startActivity(gotosign);
            }
        });

        btn_new_account_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoregisterone = new Intent(GetStartedAct.this, RegiterOneAct.class);
                startActivity(gotoregisterone);
            }
        });
    }
}