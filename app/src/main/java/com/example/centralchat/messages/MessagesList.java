package com.example.centralchat.messages;

public class MessagesList {
    private String username, indexNum, lastMessage, profilePic, chatKey;
    private int unseenMessages;

    public MessagesList(String username, String indexNum, String lastMessage, String profilePic, int unseenMessages,String chatKey) {
        this.username = username;
        this.indexNum = indexNum;
        this.lastMessage = lastMessage;
        this.profilePic = profilePic;
        this.unseenMessages = unseenMessages;
        this.chatKey = chatKey;
    }


    public String getProfilePic() {
        return profilePic;
    }

    public String getUsername() {
        return username;
    }

    public String getIndexNum() {
        return indexNum;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public int getUnseenMessages() {
        return unseenMessages;
    }

    public String getChatKey() {
        return chatKey;
    }
}
