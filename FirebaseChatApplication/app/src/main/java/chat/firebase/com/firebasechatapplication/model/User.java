package chat.firebase.com.firebasechatapplication.model;

/**
 * Created by ronnykibet on 11/23/17.
 */

public class User {

    private String userId;
    private String displayName;
    private String email;
    private String image;

    public User() {
    }
    public String name;


    public User(String username, String email) {
        this.name = username;
        this.email = email;
    }
    public User(String userId, String displayName, String email) {
        this.userId = userId;
        this.displayName = displayName;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
