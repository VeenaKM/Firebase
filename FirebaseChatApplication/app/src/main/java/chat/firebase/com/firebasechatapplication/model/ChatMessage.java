package chat.firebase.com.firebasechatapplication.model;

/**
 * Created by ronnykibet on 11/23/17.
 */

public class ChatMessage {
    private String message;
    private String senderId;
    private String receiverId;
    private FileModel file;

    private String chatRoom;

    public ChatMessage() {
    }

    public ChatMessage(String message, String senderId, String receiverId,FileModel file) {
        this.message = message;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.file = file;
    }

    public FileModel getFile() {
        return file;
    }

    public void setFile(FileModel file) {
        this.file = file;
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
