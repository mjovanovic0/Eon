package hr.eon.server.dao.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table( name = "ROOMS" )
public class Room {

    private int id;
    private String name;
    private int capacity;
    private List<User> users;

    public Room() {}

    @Id
    @GeneratedValue
    @Column( name = "ROOM_ID" )
    public int getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }

    @Column( name = "ROOM_NAME", nullable = false )
    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    @Column( name = "ROOM_CAPACITY", nullable = false )
    public int getCapacity() {
        return capacity;
    }

    public void setCapacity( int capacity ) {
        this.capacity = capacity;
    }

    @ManyToMany( fetch = FetchType.LAZY, cascade = CascadeType.ALL )
    @JoinTable( name = "USERS_IN_ROOM", joinColumns = { @JoinColumn( name = "ROOM_ID" ) }, inverseJoinColumns = { @JoinColumn( name = "USER_ID" ) } )
    public List<User> getUsers() {
        return users;
    }

    public void setUsers( List<User> users ) {
        this.users = users;
    }
}
