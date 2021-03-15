package com.hendri.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegiterOneAct extends AppCompatActivity {

    Button btn_continue_RO;
    LinearLayout btn_back_RO;
    EditText username, password, email_address;

    DatabaseReference reference, referense_username;
    String USERNAME_KEY ="usernamekey";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regiter_one);

        btn_back_RO = findViewById(R.id.btn_back_RO);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email_address = findViewById(R.id.email_address);

        btn_back_RO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_continue_RO = findViewById(R.id.btn_continue_RO);
        btn_continue_RO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ubah state menjadi loading
               stateButtonflase();

                //mengambil username pada firebase
                referense_username = FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(username.getText().toString());
                referense_username.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        final String username1 = username.getText().toString();
                        final String password1 = password.getText().toString();
                        final String email1 = email_address.getText().toString();

                        //validasi user, pw, dan email kosong
                        if (username1.isEmpty() || password1.isEmpty() || email1.isEmpty()){ // validasi username sudah ada
                            Toast.makeText(getApplicationContext()," Data tidak boleh kosong!", Toast.LENGTH_SHORT).show();

                            //UBAH STATE JADI ACTIVE
                            stateButtontrue();

                        }else {

                            if (snapshot.exists()){
                                Toast.makeText(getApplicationContext(),"Username sudah tersedia!",
                                        Toast.LENGTH_SHORT).show();
                                //UBAH STATE JADI ACTIVE
                                stateButtontrue();

                            }else{
                                //menyimpan data secara local storage handphone
                                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(username_key, username.getText().toString());
                                editor.apply();

                                // menyimpan pada firebase
                                reference = FirebaseDatabase.getInstance().getReference().child("Users").
                                        child(username.getText().toString());
                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        snapshot.getRef().child("username").setValue(username.getText().toString());
                                        snapshot.getRef().child("password").setValue(password.getText().toString());
                                        snapshot.getRef().child("email_address").setValue(email_address.getText().toString());
                                        snapshot.getRef().child("user_balance").setValue(800);

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                //berpindah activity
                                Intent gotoregister = new Intent(RegiterOneAct.this, RegiterTwoAct.class);
                                startActivity(gotoregister);

                                //UBAH STATE JADI ACTIVE
                                stateButtontrue();

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }
    public void stateButtonflase(){
        btn_continue_RO.setEnabled(false);
        btn_continue_RO.setText("Loading ...");
    }

    public void stateButtontrue(){
        btn_continue_RO.setEnabled(true);
        btn_continue_RO.setText("CONTINUE");
    }
}