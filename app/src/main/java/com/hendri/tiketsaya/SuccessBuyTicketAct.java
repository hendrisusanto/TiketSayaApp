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

public class SuccessBuyTicketAct extends AppCompatActivity {

    Animation app_splash, buttomtotop, toptobottom;
    ImageView icon_success_ticket;
    TextView app_title_SBT, app_subtitle_SBT;
    Button btn_view_ticket, btn_my_dashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_buy_ticket);

        icon_success_ticket = findViewById(R.id.icon_success_ticket);

        app_title_SBT = findViewById(R.id.app_title_SBT);
        app_subtitle_SBT = findViewById(R.id.app_subtitle_SBT);

        btn_view_ticket = findViewById(R.id.btn_view_ticket);
        btn_my_dashboard = findViewById(R.id.btn_my_dashboard);


        // load animations
        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        buttomtotop = AnimationUtils.loadAnimation(this, R.anim.bottomtotop);
        toptobottom = AnimationUtils.loadAnimation(this, R.anim.toptobottom);

        // run animations
        icon_success_ticket.startAnimation(app_splash);

        app_title_SBT.startAnimation(toptobottom);
        app_subtitle_SBT.startAnimation(toptobottom);

        btn_view_ticket.startAnimation(buttomtotop);
        btn_my_dashboard.startAnimation(buttomtotop);

        btn_view_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // berpindah activity
                Intent gotomyprofie = new Intent(SuccessBuyTicketAct.this, MyProfileAct.class);
                startActivity(gotomyprofie);
            }
        });

        btn_my_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // berpindah activity
                Intent gotohome = new Intent(SuccessBuyTicketAct.this, HomeAct.class);
                startActivity(gotohome);
            }
        });


    }

}