package hr.eon.core.model;

import java.io.Serializable;

public class ChatUser implements Serializable {

    private static final long serialVersionUID = 2819471536088948296L;

    private int id;
    private String username;

    public ChatUser( int id, String username ) {
        this.id = id;
        this.username = username;
    }

    public int getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

}
