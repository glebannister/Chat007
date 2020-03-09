package android.example.chat007.data;

import android.net.Uri;

public class Users {
    private String name;
    private String email;
    private String id;
    private int avatarResource;



    public Users(String name, String email, String id, String sender, String recipient, int avatarResource) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.avatarResource = avatarResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAvatarResource() {
        return avatarResource;
    }

    public void setAvatarResource(int avatarResource) {
        this.avatarResource = avatarResource;
    }


    public Users() {
    }

}
