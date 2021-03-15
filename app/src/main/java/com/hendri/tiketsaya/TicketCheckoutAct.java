package com.hendri.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class TicketCheckoutAct extends AppCompatActivity {

    LinearLayout btn_back_TC;
    Button btn_pay_now_TC;
    ImageView btn_minus, btn_plus, notice_uang;
    TextView textjumlah_tiket, textmybalance, texttotalharga, nama_wisata, lokasi, ketentuan;
    Integer valuejumlahtiket = 1;
    Integer mybalance = 0;
    Integer valuetotalharga = 0;
    Integer valuehargatiket = 0;
    Integer sisa_balance = 0;

    DatabaseReference reference, reference2, reference3, reference4;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    String date_wisata = "";
    String time_wisata = "";

    // generate  nomor interger secara random (no trasaksi secara unik)
    Integer nomor_transaksi = new Random().nextInt();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_checkout);

        getUsernameLocal();

        // mengambil data dari intent
        Bundle bundle = getIntent().getExtras();
        final String jenis_tiket_baru = bundle.getString("jenis_tiket");

        btn_back_TC =  findViewById(R.id.btn_back_TC);
        btn_pay_now_TC =  findViewById(R.id.btn_pay_now_TC);

        btn_minus =  findViewById(R.id.btn_minus);
        btn_plus =  findViewById(R.id.btn_plus);
        notice_uang =  findViewById(R.id.notice_uang);
        textjumlah_tiket =  findViewById(R.id.textjumlah_tiket);

        textmybalance =  findViewById(R.id.textmybalance);
        texttotalharga =  findViewById(R.id.texttotalharga);

        nama_wisata =  findViewById(R.id.nama_wisata);
        lokasi =  findViewById(R.id.lokasi);
        ketentuan =  findViewById(R.id.ketentuan);

        //setting value baru untuk beberapa komponen
        textjumlah_tiket.setText(valuejumlahtiket.toString());
        texttotalharga.setText("US$ "+valuehargatiket.toString());



        // mengamabil data user dari firebase
        reference2 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mybalance = Integer.valueOf(snapshot.child("user_balance").getValue().toString());
                textmybalance.setText("US$ "+mybalance.toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // mengambil data dari firebass berdasarkan intent
        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(jenis_tiket_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nama_wisata.setText(snapshot.child("nama_wisata").getValue().toString());
                lokasi.setText(snapshot.child("lokasi").getValue().toString());
                ketentuan.setText(snapshot.child("ketentuan").getValue().toString());

                date_wisata = snapshot.child("date_wisata").getValue().toString();
                time_wisata = snapshot.child("time_wisata").getValue().toString();

                valuehargatiket = Integer.valueOf(snapshot.child("harga_tiket").getValue().toString());

                valuetotalharga = valuehargatiket*valuejumlahtiket;
                texttotalharga.setText("US$ "+valuetotalharga.toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //secara default btn minus hide
        btn_minus.animate().alpha(0).setDuration(300).start();
        btn_minus.setEnabled(false);
        notice_uang.setVisibility(View.GONE);

        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valuejumlahtiket+=1;
                textjumlah_tiket.setText(valuejumlahtiket.toString());
                if (valuejumlahtiket >1){
                    btn_minus.animate().alpha(1).setDuration(300).start();
                    btn_minus.setEnabled(true);
                }
                valuetotalharga = valuehargatiket*valuejumlahtiket;
                texttotalharga.setText("US$ "+valuetotalharga.toString());

                if (valuetotalharga > mybalance){
                    btn_pay_now_TC.animate().translationY(250).alpha(0).setDuration(350).start();
                    btn_pay_now_TC.setEnabled(false);
                    textmybalance.setTextColor(Color.parseColor("#D1206B"));
                    notice_uang.setVisibility(View.VISIBLE);
                }

            }
        });



        btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valuejumlahtiket-=1;
                textjumlah_tiket.setText(valuejumlahtiket.toString());
                if (valuejumlahtiket <2){
                    btn_minus.animate().alpha(0).setDuration(300).start();
                    btn_minus.setEnabled(false);
                }
                valuetotalharga = valuehargatiket*valuejumlahtiket;
                texttotalharga.setText("US$ "+valuetotalharga.toString());

                if (valuetotalharga < mybalance){
                    btn_pay_now_TC.animate().translationY(0).alpha(1).setDuration(350).start();
                    btn_pay_now_TC.setEnabled(true);
                    textmybalance.setTextColor(Color.parseColor("#203DD1"));
                    notice_uang.setVisibility(View.GONE);
                }
            }
        });

        btn_back_TC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        btn_pay_now_TC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // menyimpan data user ke firebase dan membuat tabel baru "MyTickets"
                reference3 = FirebaseDatabase.getInstance().getReference().child("MyTickets")
                        .child(username_key_new).child(nama_wisata.getText().toString() + nomor_transaksi);
                reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        reference3.getRef().child("id_ticket").setValue(nama_wisata.getText().toString() + nomor_transaksi);
                        reference3.getRef().child("nama_wisata").setValue(nama_wisata.getText().toString());
                        reference3.getRef().child("lokasi").setValue(lokasi.getText().toString());
                        reference3.getRef().child("ketentuan").setValue(ketentuan.getText().toString());
                        reference3.getRef().child("jumlah_tiket").setValue(valuejumlahtiket.toString());
                        reference3.getRef().child("date_wisata").setValue(date_wisata);
                        reference3.getRef().child("time_wisata").setValue(time_wisata);

                        Intent gotosuccessticket = new Intent(TicketCheckoutAct.this, SuccessBuyTicketAct.class);
                        startActivity(gotosuccessticket);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //update data balance kepada user (yang saat ini login)
                // mengamabil data user dari firebase
                reference4 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
                reference4.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        sisa_balance = mybalance - valuetotalharga;
                        reference4.getRef().child("user_balance").setValue(sisa_balance);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }
}