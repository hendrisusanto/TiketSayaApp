package com.hendri.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyTicketDetailAct extends AppCompatActivity {

    LinearLayout btn_back_MTD;

    DatabaseReference reference;

    TextView nama_wisata_MTD, lokasi_MTD, date_wisata_MTD, time_wisata_MTD, ketentuan_MTD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ticket_detail);

        btn_back_MTD = findViewById(R.id.btn_back_MTD);

        nama_wisata_MTD = findViewById(R.id.nama_wisata_MTD);
        lokasi_MTD = findViewById(R.id.lokasi_MTD);
        date_wisata_MTD = findViewById(R.id.date_wisata_MTD);
        time_wisata_MTD = findViewById(R.id.time_wisata_MTD);
        ketentuan_MTD = findViewById(R.id.ketentuan_MTD);

        // mengambil data dari intent
        Bundle bundle = getIntent().getExtras();
        final String nama_wisata_baru = bundle.getString("nama_wisata");

        // mengambil data dari firebase
        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(nama_wisata_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nama_wisata_MTD.setText(snapshot.child("nama_wisata").getValue().toString());
                lokasi_MTD.setText(snapshot.child("lokasi").getValue().toString());
                date_wisata_MTD.setText(snapshot.child("date_wisata").getValue().toString());
                time_wisata_MTD.setText(snapshot.child("time_wisata").getValue().toString());
                ketentuan_MTD.setText(snapshot.child("ketentuan").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_back_MTD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}