package hr.eon.server.message;

import hr.eon.core.Constants.MessageDirection;
import hr.eon.core.Constants.Messages;
import hr.eon.core.EonMessage;
import hr.eon.core.message.MessageList;
import hr.eon.core.message.handler.EonHandler;
import hr.eon.server.EonServer;
import java.util.List;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.log4j.Logger;

/**
 * Message dispatcher which consume received JMS message and load correspond
 * handler and raise handle event. Also check message integrity.
 * 
 * @author mjovanovic
 * @since 0.0.1
 */
public class MessageDispatcher implements MessageListener {

    private static final Logger LOGGER = Logger.getLogger( MessageDispatcher.class );

    @Override
    public void onMessage( Message request ) {
        try {
            LOGGER.debug( "Received message with correlId " + request.getJMSCorrelationID() );
            EonMessage message = (EonMessage) ( (ActiveMQObjectMessage) request ).getObject();

            if ( MessageDirection.SERVER.equals( message.getFrom() ) && Messages.CHATMESSAGE.getCode() != message.getId() ) {
                LOGGER.debug( "Message is from me." );
                return;
            }

            if ( isValid( message ) ) {
                LOGGER.debug( "Message is valid." );
                Class<?> handler = message.getClazz();
                EonMessage handledMessage = message;
                List<EonMessage> listMessage = null;

                if ( Messages.MESSAGE_LIST.getCode() == message.getId() ) {
                    LOGGER.debug( "Message is MessageList. Extract first message for event raise" );
                    MessageList ml = (MessageList) message;

                    handledMessage = ml.getList().get( 0 );
                    handler = handledMessage.getClass();
                    ml.getList().remove( 0 );

                    listMessage = ml.getList();
                    LOGGER.debug( "Event " + handledMessage );
                }

                EonHandler msgHandler = EonServer.eonContext.getHandlerForMessage( handler );
                EonMessage response = msgHandler.handle( handledMessage, listMessage );

                if ( response != null && Messages.LOGIN_RESPONSE.getCode() != response.getId() ) {
                    LOGGER.debug( "Response is not login related. Calculating checksum." );
                    response.setFrom( MessageDirection.SERVER );
                    String sesTkn = response.getUserId() == -1 ? "BROADCAST" : EonServer.eonContext.getUserById( response.getUserId() ).getSession();
                    response.setSessionToken( sesTkn );
                    response.generateChecksum();

                    EonServer.eonContext.sendMessage( request, response );
                } else if ( response != null && Messages.LOGIN_RESPONSE.getCode() != response.getId() ) {
                    EonServer.eonContext.sendMessage( request, response );
                }

            } else {
                LOGGER.warn( "Received invalid message. Message: " + message.toString() );
            }
        }
        catch ( JMSException e ) {
            LOGGER.error( "Error occured while parsing incomed message.", e );
        }
    }

    private boolean isValid( EonMessage message ) {

        if ( message.getId() != Messages.LOGIN.getCode() ) {
            String receivedChecksum = message.getChecksum();
            message.generateChecksum();
            String calculatedChecksum = message.getChecksum();

            if ( !receivedChecksum.equals( calculatedChecksum ) ) {
                LOGGER.debug( "Message integrity check failed." );
                return false;
            }
        }
        return true;
    }

}
