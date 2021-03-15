package com.hendri.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.florent37.shapeofview.shapes.CircleView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeAct extends AppCompatActivity {

    CircleView btn_to_profile;
    LinearLayout btn_ticket_pisa, btn_ticket_torri, btn_ticket_pagoda;
    LinearLayout btn_ticket_sphinx, btn_ticket_monas, btn_ticket_candi;

    ImageView photo_home_user;
    TextView nama_lengkap_home, bio_home, user_balance;

    DatabaseReference reference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getUsernameLocal();

        btn_to_profile = findViewById(R.id.btn_to_profile);

        btn_ticket_pisa = findViewById(R.id.btn_ticket_pisa);
        btn_ticket_torri = findViewById(R.id.btn_ticket_torri);
        btn_ticket_pagoda = findViewById(R.id.btn_ticket_pagoda);
        btn_ticket_sphinx = findViewById(R.id.btn_ticket_sphinx);
        btn_ticket_monas = findViewById(R.id.btn_ticket_monas);
        btn_ticket_candi = findViewById(R.id.btn_ticket_candi);

        photo_home_user = findViewById(R.id.photo_home_user);
        nama_lengkap_home = findViewById(R.id.nama_lengkap_home);
        bio_home = findViewById(R.id.bio_home);
        user_balance = findViewById(R.id.user_balance);

        reference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(username_key_new);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nama_lengkap_home.setText(snapshot.child("nama_lengkap").getValue().toString());
                bio_home.setText(snapshot.child("bio").getValue().toString());
                user_balance.setText("US$ " + snapshot.child("user_balance").getValue().toString());
                Picasso.with(HomeAct.this).load(snapshot.child("url_photo_profile").getValue()
                        .toString()).centerCrop().fit().into(photo_home_user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_to_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoprofile = new Intent(HomeAct.this, MyProfileAct.class);
                startActivity(gotoprofile);
            }
        });

            // BATAS
        btn_ticket_pisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotopisaticket = new Intent(HomeAct.this, TicketDetailAct.class);
                // meletakkan data pada intent
                gotopisaticket.putExtra("jenis_tiket", "Pisa");
                startActivity(gotopisaticket);
            }
        });

        btn_ticket_torri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoTorriticket = new Intent(HomeAct.this, TicketDetailAct.class);
                gotoTorriticket.putExtra("jenis_tiket", "Torri");
                startActivity(gotoTorriticket);
            }
        });

        btn_ticket_pagoda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoPagodaticket = new Intent(HomeAct.this, TicketDetailAct.class);
                gotoPagodaticket.putExtra("jenis_tiket", "Pagoda");
                startActivity(gotoPagodaticket);
            }
        });

        btn_ticket_sphinx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoSphinxticket = new Intent(HomeAct.this, TicketDetailAct.class);
                gotoSphinxticket.putExtra("jenis_tiket", "Sphinx");
                startActivity(gotoSphinxticket);
            }
        });

        btn_ticket_monas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoMonasticket = new Intent(HomeAct.this, TicketDetailAct.class);
                gotoMonasticket.putExtra("jenis_tiket", "Monas");
                startActivity(gotoMonasticket);
            }
        });

        btn_ticket_candi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoCanditicket = new Intent(HomeAct.this, TicketDetailAct.class);
                gotoCanditicket.putExtra("jenis_tiket", "Candi");
                startActivity(gotoCanditicket);
            }
        });
    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }
}