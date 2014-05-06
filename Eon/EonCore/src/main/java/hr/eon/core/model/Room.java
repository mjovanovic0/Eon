package hr.eon.core.model;

import java.io.Serializable;
import java.util.List;

public class Room implements Serializable {

    private static final long serialVersionUID = -926785556416749278L;

    private int id;

    private String name;
    private int capacity;
    private List<ChatUser> users;

    public Room( int id, String name, int capacity, List<ChatUser> users ) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.users = users;
    }

    public void addUser( ChatUser user ) {
        if ( this.users.size() != this.capacity ) {
            this.users.add( user );
        }
    }

    public int getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public int getOccupied() {
        return this.users.size();
    }

    public List<ChatUser> getUsers() {
        return this.users;
    }

    public boolean isEmpty() {
        return this.users.isEmpty();
    }

    public boolean isFull() {
        return this.users.size() == this.capacity;
    }

}
