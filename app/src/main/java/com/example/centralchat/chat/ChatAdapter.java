package com.example.centralchat.chat;

import android.content.Context;
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

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.Holder> {

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
    public ChatAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.Holder holder, int position) {

        ChatList list2 = chatLists.get(position);

        if(list2.getUsername().equals(userName)) {
            holder.myLayout.setVisibility(View.VISIBLE);
            holder.friendsLayout.setVisibility(View.GONE);

            holder.myMsg.setText(list2.getMessage());
            holder.myTimeStamp.setText(list2.getDate()+ "" + list2.getTime());
        }else {
            holder.myLayout.setVisibility(View.GONE);
            holder.friendsLayout.setVisibility(View.VISIBLE);

            holder.friendsMsg.setText(list2.getMessage());
            holder.friendsTimeStamp.setText(list2.getDate()+ "" + list2.getTime());
        }
    }

    @Override
    public int getItemCount() {
        return chatLists.size();
    }

    public void updateChat(List<ChatList> chatLists) {
        this.chatLists = chatLists;
    }

    static class Holder extends RecyclerView.ViewHolder {

        private final LinearLayout friendsLayout;
        private final LinearLayout myLayout;
        private final TextView friendsMsg;
        private final TextView myMsg;
        private final TextView friendsTimeStamp;
        private final TextView myTimeStamp;
        public Holder(@NonNull View itemView) {
            super(itemView);

            friendsLayout = itemView.findViewById(R.id.friendLayout);
            myLayout = itemView.findViewById(R.id.myLayout);
            friendsMsg = itemView.findViewById(R.id.friendMessage);
            myMsg = itemView.findViewById(R.id.myMessage);
            friendsTimeStamp = itemView.findViewById(R.id.friendMessageTime);
            myTimeStamp = itemView.findViewById(R.id.myMessageTime);
        }
    }
}
