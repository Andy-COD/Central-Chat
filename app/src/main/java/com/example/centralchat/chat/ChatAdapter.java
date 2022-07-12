package com.example.centralchat.chat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.centralchat.MemoryData;
import com.example.centralchat.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private List<ChatList> chatLists;
    private final Context context;
    private final String userName;

    public ChatAdapter(List<ChatList> chatLists, Context context) {
        this.chatLists = chatLists;
        this.context = context;
        this.userName = MemoryData.getUserName(context);
    }

    @NonNull
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.MyViewHolder holder, int position) {
        ChatList list2 = chatLists.get(position);


    }

    @Override
    public int getItemCount() {
        return chatLists.size();
    }

    public void updateChatList(List<ChatList> chatLists) {
        this.chatLists = chatLists;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout friendLayout;
        private final LinearLayout myLayout;
        private TextView friendMessage, myMessage;
        private TextView friendTime, myTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            friendLayout = itemView.findViewById(R.id.friendLayout);
            myLayout = itemView.findViewById(R.id.myLayout);
            friendMessage = itemView.findViewById(R.id.friendMessage);
            myMessage = itemView.findViewById(R.id.myMessage);
            friendTime = itemView.findViewById(R.id.friendMsgTime);
            myTime = itemView.findViewById(R.id.myMsgTime);
        }
    }
}
