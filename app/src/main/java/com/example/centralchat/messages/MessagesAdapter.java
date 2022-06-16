package com.example.centralchat.messages;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.centralchat.R;
import com.example.centralchat.chat.ChatActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

    private List<MessagesList> messagesList;
    private final Context context;

    public MessagesAdapter(List<MessagesList> messagesList, Context context) {
        this.messagesList = messagesList;
        this.context = context;
    }

    @NonNull
    @Override
    public MessagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesAdapter.ViewHolder holder, int position) {
        MessagesList list2 = messagesList.get(position);

        if(!list2.getProfilePicture().isEmpty()) {
            Picasso.get().load(list2.getProfilePicture()).resize(100, 100).centerCrop().into(holder.profilePicture);
        }
        holder.userName.setText(list2.getUsername());
        holder.lastMessage.setText(list2.getLastMessage());

        if(list2.getUnseenMessages() == 0) {
            holder.unseenMessages.setVisibility(View.GONE);
            holder.unseenMessages.setTextColor(Color.parseColor("#959595"));
        }else {
            holder.unseenMessages.setVisibility(View.VISIBLE);
            holder.lastMessage.setTextColor(context.getResources().getColor(R.color.blue_400));
            holder.unseenMessages.setText(list2.getUnseenMessages()+"");
        }

        holder.rootLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("username", list2.getUsername());
            intent.putExtra("profileImage", list2.getProfilePicture());
            intent.putExtra("chat_key", list2.getChatKey());
            context.startActivity(intent);
        });
    }

    public void updateData(List<MessagesList> messagesList) {
        this.messagesList = messagesList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final CircleImageView profilePicture;
        private final TextView userName;
        private final TextView lastMessage;
        private final TextView unseenMessages;
        private final LinearLayout rootLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePicture = itemView.findViewById(R.id.profile_picture);
            userName = itemView.findViewById(R.id.userName);
            lastMessage = itemView.findViewById(R.id.last_message);
            unseenMessages = itemView.findViewById(R.id.unseen_messages);
            rootLayout = itemView.findViewById(R.id.root_layout);
        }
    }
}
