package hr.eon.client.message;

import hr.eon.client.ClientConstants.ConnectionStatus;
import hr.eon.client.EonClient;
import hr.eon.core.Constants.MessageDirection;
import hr.eon.core.Constants.Messages;
import hr.eon.core.EonMessage;
import hr.eon.core.message.LoginResponse;
import hr.eon.core.message.MessageList;
import hr.eon.core.message.handler.EonHandler;
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

    public EonMessage dispatch( EonMessage message ) {
        if ( isValid( message ) ) {
            LOGGER.debug( "Message is valid. Processing..." );
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
                LOGGER.debug( "First message in message list is " + handledMessage );
            }

            if ( Messages.LOGIN_RESPONSE.getCode() != handledMessage.getId()
                 && ConnectionStatus.AUTHORIZED.getCode() != EonClient.eonContext.getConnectionStatus().getCode() ) {
                LOGGER.debug( "I'm not autorized with system." );
                return null;
            }

            EonHandler msgHandler = EonClient.eonContext.getHandlerForMessage( handler );
            EonMessage response = msgHandler.handle( handledMessage, listMessage );

            if ( response != null && Messages.LOGIN_RESPONSE.getCode() != response.getId() ) {
                LOGGER.debug( "Response is not login related. Calculating checksum." );
                response.setFrom( MessageDirection.CLIENT );
                response.setSessionToken( EonClient.me.getSessionToken() );
                response.generateChecksum();
            }

            return response;
        }
        LOGGER.warn( "Received invalid message. Message: " + message.toString() );
        return null;
    }

    private boolean isValid( EonMessage message ) {

        if ( !MessageDirection.SERVER.equals( message.getFrom() ) ) {
            LOGGER.debug( "Message didn't sent from server." );
            return false;
        }

        if ( Messages.MESSAGE_LIST.getCode() == message.getId()
             && Messages.LOGIN_RESPONSE.getCode() == ( (MessageList) message ).getList().get( 0 ).getId() ) {
            LoginResponse resp = (LoginResponse) ( (MessageList) message ).getList().get( 0 );
            EonClient.me.setSessionToken( resp.getSessionToken() );
        }

        String receivedChecksum = message.getChecksum();
        message.setSessionToken( EonClient.me.getSessionToken() );
        message.generateChecksum();
        String calculatedChecksum = message.getChecksum();

        if ( !receivedChecksum.equals( calculatedChecksum ) ) {
            LOGGER.debug( "Message integrity check fail." );
            return false;
        }
        return true;
    }

    @Override
    public void onMessage( Message response ) {
        try {
            LOGGER.debug( "Received message with correlId " + response.getJMSCorrelationID() );

            if ( !response.getJMSCorrelationID().equals( EonClient.me.getJmsId() ) && !response.getJMSCorrelationID().equals( "BROADCAST" ) ) {
                LOGGER.debug( "Not message of my interest." );
                return;
            }

            ActiveMQObjectMessage amqom = (ActiveMQObjectMessage) response;
            dispatch( (EonMessage) amqom.getObject() );
        }
        catch ( JMSException e ) {
            LOGGER.error( "Error processing received message", e );
        }
    }
}
