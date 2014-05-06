package hr.eon.core.message;

import hr.eon.core.Constants.Messages;
import hr.eon.core.EonMessage;
import java.sql.Timestamp;
import java.util.Date;

public class ChatMessage extends EonMessage {

    private static final long serialVersionUID = -2876254021417022410L;

    private String senderName;
    private int senderId;
    private int roomId;
    private String text;
    private Timestamp date;

    public ChatMessage( String senderName, int userId, int roomId, String text ) {
        super( Messages.CHATMESSAGE, ChatMessage.class, userId );
        this.setSenderName( senderName );
        this.senderId = userId;
        this.roomId = roomId;
        this.text = text;
        this.date = new Timestamp( new Date().getTime() );
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId( int senderId ) {
        this.senderId = senderId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId( int roomId ) {
        this.roomId = roomId;
    }

    public String getText() {
        return text;
    }

    public void setText( String text ) {
        this.text = text;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName( String senderName ) {
        this.senderName = senderName;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate( Timestamp date ) {
        this.date = date;
    }
}
