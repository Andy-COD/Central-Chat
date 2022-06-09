package com.example.centralchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Authentication extends AppCompatActivity {

    VideoView videoView;
    Button loginBtn, registerBtn;

    FirebaseUser firebaseUser;


    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //Check if user exist already
        if(firebaseUser != null) {
            Intent intent = new Intent(Authentication.this, HomePage.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        registerBtn = findViewById(R.id.signUpBtn);
        loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(v -> startActivity(new Intent(Authentication.this, Login.class)));

        registerBtn.setOnClickListener(v -> startActivity(new Intent(Authentication.this, SignUp.class)));

        videoView = findViewById(R.id.bg_Video);
        String path = "android.resource://com.example.centralchat/" + R.raw.background_video;
        Uri vid = Uri.parse(path);

        videoView.setVideoURI(vid);
        videoView.start();

        videoView.setOnPreparedListener(mp -> mp.setLooping(true));
    }
}