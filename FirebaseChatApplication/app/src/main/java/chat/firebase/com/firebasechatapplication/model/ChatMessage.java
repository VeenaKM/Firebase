package chat.firebase.com.firebasechatapplication.model;

/**
 * Created by ronnykibet on 11/23/17.
 */

public class ChatMessage {
    private String message;
    private String senderId;
    private String receiverId;

    private String chatRoom;

    public ChatMessage() {
    }

    public ChatMessage(String message, String senderId, String receiverId) {
        this.message = message;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public String getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(String chatRoom) {
        this.chatRoom = chatRoom;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }
}
