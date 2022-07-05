package com.example.centralchat.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    DatabaseReference dbReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://central-chat-5d62e-default-rtdb.firebaseio.com/");

    private final List<ChatList> chatLists = new ArrayList<>();
    String getUserName;
    private String chatKey;
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private boolean loadingFirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final ImageView backBtn = findViewById(R.id.backBtn);
        final ImageView sendBtn = findViewById(R.id.sendBtn);
        final TextView userName = findViewById(R.id.userName);
        final EditText messageInputTxt = findViewById(R.id.messageEditTxt);
        final CircleImageView profileImage = findViewById(R.id.profilePic);

        //Get data from messages adapter class
        final String getForiegnUsername = getIntent().getStringExtra("username");
        final String getProfilePic = getIntent().getStringExtra("profileImage");
        chatKey = getIntent().getStringExtra("chat_key");


        chatRecyclerView = findViewById(R.id.chatsRecyclerView);

        //Get account username from memory
        getUserName = MemoryData.getUserName(ChatActivity.this);

        userName.setText(getForiegnUsername);
        Glide.with(ChatActivity.this).load(getProfilePic).placeholder(R.drawable.ic_profile).into(profileImage);

        chatRecyclerView.setHasFixedSize(true);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));

        chatAdapter = new ChatAdapter(chatLists, ChatActivity.this);
        chatRecyclerView.setAdapter(chatAdapter);


            dbReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(chatKey.isEmpty()) {
                        //generate chat key.BY default chat key is 1
                        chatKey = "1";
                        if (snapshot.hasChild("chat")) {
                            chatKey = String.valueOf(snapshot.child("chat").getChildrenCount() + 1);
                        }
                    }
                    if (snapshot.hasChild("chat")) {
                        if(snapshot.child("chat").child(chatKey).hasChild("messages")) {
                            chatLists.clear();
                            for(DataSnapshot messagesSnapShot: snapshot.child("chat").child(chatKey).child("messages").getChildren()) {

                                if(messagesSnapShot.hasChild("msg") && messagesSnapShot.hasChild("username")) {
                                    final String messagesTimeStamp = messagesSnapShot.getKey();
                                    final String username = messagesSnapShot.child("username").getValue(String.class);
                                    final String getMsg = messagesSnapShot.child("msg").getValue(String.class);

                                    assert messagesTimeStamp != null;
                                    Timestamp timestamp = new Timestamp(Long.parseLong(messagesTimeStamp));
                                    Date date = new Date(timestamp.getTime());
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                                    SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
                                    ChatList chatList = new ChatList(username, getMsg, simpleDateFormat.format(date), simpleTimeFormat.format(date));
                                    chatLists.add(chatList);

                                    if(loadingFirstTime || Long.parseLong(messagesTimeStamp) > Long.parseLong(MemoryData.getLastMsgTS(ChatActivity.this, chatKey))) {
                                        MemoryData.saveLastMsgTS(messagesTimeStamp, chatKey, ChatActivity.this);

                                        loadingFirstTime = false;
                                        chatAdapter.updateChat(chatLists);
                                        chatRecyclerView.scrollToPosition(chatLists.size() - 1);
                                    }
                                }

                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        sendBtn.setOnClickListener(v -> {
            String getTxtMessage = messageInputTxt.getText().toString();

            //Get current timeStamps
            final String currentTimeStamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);

            MemoryData.saveLastMsgTS(currentTimeStamp, chatKey, ChatActivity.this);
            dbReference.child("chat").child(chatKey).child("user_1").setValue(getForiegnUsername);
            dbReference.child("chat").child(chatKey).child("user_2").setValue(getUserName);
            dbReference.child("chat").child(chatKey).child("messages").child(currentTimeStamp).child("msg").setValue(getTxtMessage);
            dbReference.child("chat").child(chatKey).child("messages").child(currentTimeStamp).child("user").setValue(getForiegnUsername);

            //clear text on submit button click
            messageInputTxt.setText("");
            hideSoftKeyBoard();
        });
        backBtn.setOnClickListener(v -> {
            finish();
        });
    }
    //Function to close virtual keyboard
    private void hideSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if(imm.isAcceptingText()) { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}