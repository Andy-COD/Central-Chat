package com.example.centralchat.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.centralchat.MemoryData;
import com.example.centralchat.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {
    DatabaseReference dbReference;
    private String chatKey;
    String getUserName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        dbReference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://central-chat-5d62e-default-rtdb.firebaseio.com/");

        final ImageView backBtn = findViewById(R.id.backBtn);
        final TextView otherUserName = findViewById(R.id.userName);
        final EditText messageEditText = findViewById(R.id.messageEditTxt);
        final ImageView sendBtn = findViewById(R.id.sendBtn);
        final CircleImageView profilePic = findViewById(R.id.profilePic);

        //get data from messages adapter class
        final String getName = getIntent().getStringExtra("name");
        final String getProfilePic = getIntent().getStringExtra("profile_pic");
        final String getOtherIndexNum = getIntent().getStringExtra("indexNum");
        chatKey = getIntent().getStringExtra("chat_key");

        //get user name from memory
        getUserName = MemoryData.getUserName(Chat.this);
        otherUserName.setText(getName);
        Glide.with(Chat.this).load(getProfilePic).into(profilePic);

        if (chatKey.isEmpty()) {
            dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    //generate chatKey. By default chat key is 1
                    chatKey = "1";
                    if (snapshot.hasChild("chat")) {
                        chatKey = String.valueOf(snapshot.child("chat").getChildrenCount() + 1);
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        sendBtn.setOnClickListener(v -> {

            final String getTxtMessage = messageEditText.getText().toString();

            //get current timestamp
            final String currentTimestamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);

            MemoryData.saveLastMsgTS(currentTimestamp, chatKey, Chat.this);
            dbReference.child("chat").child(chatKey).child("user_1").setValue(getUserName);
            dbReference.child("chat").child(chatKey).child("user_2").setValue(getName);
            dbReference.child("chat").child(chatKey).child("messages").child(currentTimestamp).child("msg").setValue(getTxtMessage);
            dbReference.child("chat").child(chatKey).child("messages").child(currentTimestamp).child("indexNum").setValue(getUserName);

            messageEditText.setText("");
        });
        backBtn.setOnClickListener(v -> finish());
    }
}