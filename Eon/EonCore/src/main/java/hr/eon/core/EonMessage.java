package hr.eon.core;

import hr.eon.core.Constants.MessageDirection;
import hr.eon.core.Constants.Messages;
import hr.eon.core.utils.SecurityUtils;
import java.io.Serializable;
import org.apache.commons.lang3.SerializationUtils;

/**
 * Abstract class that all messages extends to become Eon message.
 * 
 * @author mjovanovic
 * @since 0.0.1
 */
public abstract class EonMessage implements Serializable, Cloneable {

    private static final long serialVersionUID = 6865597114070039268L;

    private int messageId;
    private Integer userId;
    private String name;
    private Class<?> clazz;
    private String sessionToken;
    private String checksum;
    private boolean isBroadcast = false;
    private MessageDirection from;

    public EonMessage( Messages messageType, Class<?> clazz, Integer userId ) {
        this.messageId = messageType.getCode();
        this.name = clazz.getSimpleName();
        this.clazz = clazz;
        this.userId = userId;
    }

    public int getId() {
        return messageId;
    }

    public void setId( int id ) {
        this.messageId = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId( Integer userId ) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public Class<?> getClazz() {
        return this.clazz;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken( String sessionToken ) {
        this.sessionToken = sessionToken;
    }

    public String getChecksum() {
        String sum = this.checksum;
        this.checksum = null;
        return sum;
    }

    public void generateChecksum() {
        this.checksum = SecurityUtils.crc( SerializationUtils.serialize( this ) );
        this.sessionToken = null;
    }

    public void markBroadcast() {
        this.isBroadcast = true;
    }

    public boolean isBroadcast() {
        return this.isBroadcast;
    }

    public MessageDirection getFrom() {
        return from;
    }

    public void setFrom( MessageDirection from ) {
        this.from = from;
    }

    @Override
    public String toString() {
        return String.format( "[%d%s:%s]", this.messageId, isBroadcast ? '*' : "", this.name );
    }

    @SuppressWarnings( "unchecked" )
    public <C> C doClone() throws CloneNotSupportedException {
        return (C) this.clone();
    }

}
