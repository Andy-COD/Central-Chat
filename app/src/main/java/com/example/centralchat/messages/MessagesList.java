package com.example.centralchat.messages;

public class MessagesList {
    private String username, lastMessage, profilePicture;
    private int unseenMessages;

    public MessagesList(String username, String lastMessage, String profilePicture, int unseenMessages) {
        this.username = username;
        this.lastMessage = lastMessage;
        this.profilePicture = profilePicture;
        this.unseenMessages = unseenMessages;
    }

    public String getUsername() {
        return username;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public int getUnseenMessages() {
        return unseenMessages;
    }

    public String getProfilePicture() {
        return profilePicture;
    }
}
