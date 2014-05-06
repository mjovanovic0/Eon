package hr.eon.core.message;

import hr.eon.core.Constants.Messages;
import hr.eon.core.EonMessage;
import java.util.ArrayList;
import java.util.List;

/**
 * Message which is wrapper for sending more than one message at one time.
 * 
 * @author mjovanovic
 * @since 0.0.2
 */
public class MessageList extends EonMessage {

    private static final long serialVersionUID = -6052671323723869798L;

    private List<EonMessage> list;

    public MessageList( int userId ) {
        super( Messages.MESSAGE_LIST, MessageList.class, userId );
        list = new ArrayList<>();
    }

    /**
     * Function return reference to live list.
     */
    public List<EonMessage> getList() {
        return list;
    }

    public void addMessage( EonMessage msg ) {
        if ( this.isBroadcast() ) {
            msg.markBroadcast();
        }
        this.list.add( msg );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append( String.format( "[%d:%s ", this.getId(), this.getName() ) ).append( "{" );

        for ( EonMessage message : this.list ) {
            sb.append( String.format( "\n\t[%d:%s]", message.getId(), message.getName() ) );
        }
        if ( this.list.size() > 0 ) {
            sb.append( "\n" );
        }

        sb.append( "}]" );

        return sb.toString();
    }
}
