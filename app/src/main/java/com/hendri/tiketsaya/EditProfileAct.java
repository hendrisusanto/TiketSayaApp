package com.hendri.tiketsaya;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditProfileAct extends AppCompatActivity {

    LinearLayout btn_back_edit_profile;
    Button btn_save_EP;

    DatabaseReference reference;
    Uri photo_location;
    Integer photo_max = 1;

    StorageReference storage;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    ImageView photo_edit_profile, btn_add_photo_EP;
    EditText nama_lengkap_EP, bio_EP, username_EP, password_EP, email_EP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getUsernameLocal();

        btn_back_edit_profile = findViewById(R.id.btn_back_edit_profile);
        btn_save_EP = findViewById(R.id.btn_save_EP);

        btn_add_photo_EP = findViewById(R.id.btn_add_photo_EP);

        photo_edit_profile = findViewById(R.id.photo_edit_profile);

        nama_lengkap_EP = findViewById(R.id.nama_lengkap_EP);
        bio_EP = findViewById(R.id.bio_EP);
        username_EP = findViewById(R.id.username_EP);
        password_EP = findViewById(R.id.password_EP);
        email_EP = findViewById(R.id.email_EP);

        reference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(username_key_new);

        storage = FirebaseStorage.getInstance().getReference().child("Photousers").child(username_key_new);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nama_lengkap_EP.setText(snapshot.child("nama_lengkap").getValue().toString());
                bio_EP.setText(snapshot.child("bio").getValue().toString());
                username_EP.setText(snapshot.child("username").getValue().toString());
                password_EP.setText(snapshot.child("password").getValue().toString());
                email_EP.setText(snapshot.child("email_address").getValue().toString());

                Picasso.with(EditProfileAct.this).load(snapshot.child("url_photo_profile").getValue()
                        .toString()).centerCrop().fit().into(photo_edit_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btn_back_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_save_EP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ubah state mmenjadi loading
                btn_save_EP.setEnabled(true);
                btn_save_EP.setText("SIGN IN");

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    snapshot.getRef().child("nama_lengkap").setValue(nama_lengkap_EP.getText().toString());
                    snapshot.getRef().child("bio").setValue(bio_EP.getText().toString());
                    snapshot.getRef().child("username").setValue(username_EP.getText().toString());
                    snapshot.getRef().child("password").setValue(password_EP.getText().toString());
                    snapshot.getRef().child("email_address").setValue(email_EP.getText().toString());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


                if (photo_location !=null){
                    StorageReference storageReference1 = storage.child(System.currentTimeMillis() + "." +
                            getFileExtentions(photo_location));

                    storageReference1.putFile(photo_location).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            storageReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String uri_photo = uri.toString();
                                    reference.getRef().child("url_photo_profile").setValue(uri_photo);
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    //berpindah activity
                                    Intent gotopofile = new Intent(EditProfileAct.this, MyProfileAct.class);
                                    startActivity(gotopofile);

                                }
                            });

                        }
                    }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        }
                    });
                }

            }
        });

        btn_add_photo_EP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPhoto();
            }
        });
    }


    String getFileExtentions(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void findPhoto(){
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic, photo_max);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == photo_max && resultCode == RESULT_OK && data != null && data.getData() !=null)
        {
            photo_location = data.getData();
            Picasso.with(this).load(photo_location).centerCrop().fit().into(photo_edit_profile);
        }
    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }
}