package com.hendri.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SigninAct extends AppCompatActivity {

    TextView btn_new_acccount;
    EditText xusername, xpassword;
    Button btn_signin_SA;

    DatabaseReference reference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        btn_new_acccount = findViewById(R.id.btn_new_acccount);
        btn_signin_SA = findViewById(R.id.btn_signin_SA);

        xusername = findViewById(R.id.xusername);
        xpassword = findViewById(R.id.xpassword);

        btn_signin_SA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ubah state menjadi loading
                stateButtonflase();

                final String username = xusername.getText().toString();
                final String password = xpassword.getText().toString();

                if (username.isEmpty()){
                    Toast.makeText(getApplicationContext()," Username kosong!", Toast.LENGTH_SHORT).show();
                    //ubah button menjadi enable
                   stateButtontrue();
                }else {
                    if (password.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Password kosong!", Toast.LENGTH_SHORT).show();
                        //ubah button menjadi enable
                        stateButtontrue();

                    }else{
                        reference = FirebaseDatabase.getInstance().getReference().child("Users")
                                .child(username);

                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){

                                    // data password dari firebass
                                    String passwordFromFirebase = snapshot.child("password").getValue().toString();

                                    // validasi password dengan password firebass
                                    if (password.equals(passwordFromFirebase)){

                                        // simpan username (key) kepada local
                                        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(username_key, xusername.getText().toString());
                                        editor.apply();

                                        // berpindah activity
                                        Intent gotohome = new Intent(SigninAct.this, HomeAct.class);
                                        startActivity(gotohome);

                                    }else{
                                        Toast.makeText(getApplicationContext(),"Password salah!", Toast.LENGTH_SHORT).show();
                                        stateButtontrue();
                                    }

                                }else{
                                    Toast.makeText(getApplicationContext(),"Username salah!", Toast.LENGTH_SHORT).show();
                                    stateButtontrue();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getApplicationContext(),"Databse error!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }


            }
        });

        btn_new_acccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // berpindah activity
                Intent gotoregisterone = new Intent(SigninAct.this, RegiterOneAct.class);
                startActivity(gotoregisterone);

            }
        });
    }

    public void stateButtonflase(){
        btn_signin_SA.setEnabled(false);
        btn_signin_SA.setText("Loading ...");
    }

    public void stateButtontrue(){
        btn_signin_SA.setEnabled(true);
        btn_signin_SA.setText("SIGN IN");
    }
}