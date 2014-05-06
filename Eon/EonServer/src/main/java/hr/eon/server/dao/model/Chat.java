package hr.eon.server.dao.model;

import hr.eon.core.message.ChatMessage;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table( name = "CHAT" )
public class Chat {

    private int id;
    private int roomId;
    private User user;
    private Timestamp sent;
    private String text;

    public Chat() {}

    public Chat( ChatMessage message, User user ) {
        this.roomId = message.getRoomId();
        this.user = user;
        this.sent = message.getDate();
        this.text = message.getText();
    }

    @Id
    @GeneratedValue
    @Column( name = "CHAT_ID" )
    public int getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }

    @Column( name = "CHAT_ROOM_ID", nullable = false )
    public int getRoomId() {
        return roomId;
    }

    public void setRoomId( int roomId ) {
        this.roomId = roomId;
    }

    @OneToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "CHAT_SENDER_ID", nullable = false )
    public User getUser() {
        return user;
    }

    public void setUser( User user ) {
        this.user = user;
    }

    @Column( name = "CHAT_SENT", nullable = false )
    public Timestamp getSent() {
        return sent;
    }

    public void setSent( Timestamp sent ) {
        this.sent = sent;
    }

    @Column( name = "CHAT_MESSAGE", nullable = false )
    public String getText() {
        return text;
    }

    public void setText( String text ) {
        this.text = text;
    }
}
