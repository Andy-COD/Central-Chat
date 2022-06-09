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
import com.example.centralchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatFragment extends Fragment {
    String indexNum, userName;
    RecyclerView messagesRecyclerView;
    CircleImageView userProfile;
    ProgressDialog progressDialog;

    DatabaseReference dbReference;
    FirebaseAuth mAuth;
    FirebaseUser fireBaseUser;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_chat, container, false);

        userProfile = view.findViewById(R.id.userProfilePicture);
        if (getArguments() != null) {
            indexNum = getArguments().getString("index number");
            userName = getArguments().getString("username");
        }


        Log.d("Message tag", indexNum);

        dbReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://central-chat-5d62e-default-rtdb.firebaseio.com/");
        mAuth = FirebaseAuth.getInstance();
        fireBaseUser = mAuth.getCurrentUser();

        messagesRecyclerView = view.findViewById(R.id.messagesRecyclerView);

        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));
        messagesRecyclerView.setHasFixedSize(true);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        //get profile picture from firebase database
        dbReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("Message tag", snapshot.child(indexNum).toString());
                String profileImgUrl = snapshot.child(indexNum).child("profile picture").getValue(String.class);
                if(Objects.requireNonNull(profileImgUrl).isEmpty()) {
                    Picasso.get().load(profileImgUrl).into(userProfile);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}