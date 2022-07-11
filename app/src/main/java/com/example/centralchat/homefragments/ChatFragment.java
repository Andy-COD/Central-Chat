package com.example.centralchat.homefragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.centralchat.MemoryData;
import com.example.centralchat.R;
import com.example.centralchat.messages.MessagesAdapter;
import com.example.centralchat.messages.MessagesList;
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

    DatabaseReference dbReference;
    View view;
    private int unseenMessages = 0;
    private String lastMessage = "";
    private MessagesAdapter messagesAdapter;
    private String chatKey = "";
    private boolean dataSet = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_chat, container, false);

        Context context = requireActivity().getApplicationContext();

        RecyclerView messagesRecyclerView = view.findViewById(R.id.messagesRecyclerView);
        final List<MessagesList> messagesLists = new ArrayList<>();
        dbReference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://central-chat-5d62e-default-rtdb.firebaseio.com/");

        final CircleImageView profileImage = view.findViewById(R.id.userProfilePicture);


        //get bundle arguments from viewpager
        assert getArguments() != null;
        final String userName = getArguments().getString("username");
        final String indexNum = getArguments().getString("indexNum");


        messagesRecyclerView.setHasFixedSize(true);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        messagesAdapter = new MessagesAdapter(messagesLists, getContext());
        messagesRecyclerView.setAdapter(messagesAdapter);

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String profilePic = snapshot.child("users")
                        .child(indexNum).child("profile picture").getValue(String.class);
                assert profilePic != null;
                if(profilePic.isEmpty()) {
                    Glide.with(getContext()).load(R.drawable.ic_profile)
                            .placeholder(R.drawable.ic_profile).into(profileImage);
                }else {
                    Glide.with(getContext()).load(profilePic).into(profileImage);
                }
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

                    final String getIndexNum = dataSnapshot.getKey();
                    dataSet = false;

                    assert getIndexNum != null;
                    //As long as the received index is not the same as index stored in memory
                    if(!getIndexNum.equals(indexNum)) {
                        final String getUserName = dataSnapshot.child("username")
                                .getValue(String.class);
                        final String getProfilePic = dataSnapshot.child("profile picture")
                                .getValue(String.class);


                        dbReference.child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int getChatCounts = (int)snapshot.getChildrenCount();

                                if(getChatCounts > 0) {
                                    for(DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                        final String getKey = dataSnapshot1.getKey();
                                        chatKey = getKey;

                                        if (dataSnapshot1.hasChild("user_1") && dataSnapshot1.hasChild("user_2") && dataSnapshot1.hasChild("messages")) {
                                            final String getUserOne = dataSnapshot1.child("user_1").getValue(String.class);
                                            final String getUserTwo = dataSnapshot1.child("user_2").getValue(String.class);
                                            assert getUserTwo != null;
                                            assert getUserOne != null;

                                            if (getUserOne.equals(userName) && getUserTwo.equals(getUserName) || getUserTwo.equals(userName) && getUserOne.equals(getUserName)) {
                                                for (DataSnapshot chatDataSnapshot: dataSnapshot1.child("messages").getChildren()) {

                                                    final long getMessageKey = Long.parseLong(Objects.requireNonNull(chatDataSnapshot.getKey()));
                                                    final long getLastSeenMessages = Long.parseLong(MemoryData.getLastMsgTS(context, getKey));

                                                    Log.d("Message tag", String.valueOf(getMessageKey));
                                                    Log.d("Message tag", String.valueOf(getLastSeenMessages));

                                                    lastMessage = chatDataSnapshot.child("msg").getValue(String.class);
                                                    if (getMessageKey > getLastSeenMessages) {
                                                        unseenMessages++;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                if (!dataSet) {
                                    dataSet = true;
                                    MessagesList messagesList = new MessagesList(getUserName, getIndexNum, lastMessage, getProfilePic, unseenMessages, chatKey);
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