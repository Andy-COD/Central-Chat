package com.example.centralchat.homefragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.centralchat.MemoryData;
import com.example.centralchat.R;
import com.example.centralchat.messages.MessagesAdapter;
import com.example.centralchat.messages.MessagesList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatFragment extends Fragment {
    private final List<MessagesList> messagesLists = new ArrayList<>();
    String indexNum, userName;
    RecyclerView messagesRecyclerView;
    CircleImageView userProfile;
    ProgressDialog progressDialog;

    DatabaseReference dbReference;
    FirebaseAuth mAuth;
    FirebaseUser fireBaseUser;

    private int unseenMessages = 0;
    private String lastMessage = "";
    private String chatKey = "";

    private boolean dataSet = false;
    private MessagesAdapter messagesAdapter;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_chat, container, false);

        userProfile = view.findViewById(R.id.userProfilePicture);
        if (getArguments() != null) {
            indexNum = getArguments().getString("index number");
            userName = getArguments().getString("username");
        }

        dbReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://central-chat-5d62e-default-rtdb.firebaseio.com/");
        mAuth = FirebaseAuth.getInstance();
        fireBaseUser = mAuth.getCurrentUser();

        messagesRecyclerView = view.findViewById(R.id.messagesRecyclerView);

        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));
        messagesRecyclerView.setHasFixedSize(true);

        //set messages adapter to recycler view
        messagesAdapter = new MessagesAdapter(messagesLists, getActivity());

        messagesRecyclerView.setAdapter(messagesAdapter);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        //get profile picture from firebase database
        dbReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                messagesLists.clear();
                unseenMessages = 0;
                lastMessage = "";
                chatKey = "";

                for(DataSnapshot dataSnapshot : snapshot.child("users").getChildren()) {
                    final String userIndex = dataSnapshot.getKey();

                    dataSet = false;

                    if(!Objects.equals(userIndex, indexNum)) {
                        final String getUserName = dataSnapshot.child("username").getValue(String.class);
                        final String getProfilePicture = dataSnapshot.child("profile picture").getValue(String.class);


                        dbReference.child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int getChatCount = (int)snapshot.getChildrenCount();

                                if(getChatCount > 0) {
                                    for (DataSnapshot dataSnapshot1: snapshot.getChildren()) {
                                        final String getKey = dataSnapshot1.getKey();
                                        chatKey = getKey;


                                        if (dataSnapshot1.hasChild("user_1") && dataSnapshot1.hasChild("user_2") && dataSnapshot1.hasChild("messages")) {
                                            final String getInitialUser = dataSnapshot1.child("user_1").getValue(String.class);
                                            final String getSecondUser = dataSnapshot1.child("user_2").getValue(String.class);


                                            if(Objects.equals(getInitialUser, getUserName) && Objects.equals(getSecondUser, userName)
                                                    || (Objects.equals(getInitialUser, userName) && Objects.equals(getSecondUser, getUserName))
                                            ) {
                                                for(DataSnapshot chatDataSnapShot: dataSnapshot1.child("messages").getChildren()) {
                                                    final long getMessageKey = Long.parseLong(Objects.requireNonNull(chatDataSnapShot.getKey()));
                                                    final long getLastSentMessage = Long.parseLong(MemoryData.getLastMsgTS(requireActivity(), getKey));

                                                    Log.d("Message tag", String.valueOf(getLastSentMessage));

                                                    lastMessage = chatDataSnapShot.child("msg").getValue(String.class);
                                                    if(getMessageKey > getLastSentMessage) {
                                                        unseenMessages++;
                                                    }
                                                }
                                            }
                                        }

                                    }
                                }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              if(!dataSet) {
                                    dataSet = true;
                                    MessagesList messagesList = new MessagesList(getUserName, lastMessage, getProfilePicture, unseenMessages, chatKey);
                                    messagesLists.add(messagesList);
                                    messagesAdapter.updateData(messagesLists);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}