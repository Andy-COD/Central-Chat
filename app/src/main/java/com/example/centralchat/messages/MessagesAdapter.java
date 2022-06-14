package com.example.centralchat.messages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.centralchat.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

    private final List<MessagesList> messagesList;
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
        }else {
            holder.unseenMessages.setVisibility(View.VISIBLE);
        }
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePicture = itemView.findViewById(R.id.profile_picture);
            userName = itemView.findViewById(R.id.userName);
            lastMessage = itemView.findViewById(R.id.last_message);
            unseenMessages = itemView.findViewById(R.id.unseen_messages);
        }
    }
}
