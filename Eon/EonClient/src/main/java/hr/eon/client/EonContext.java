package hr.eon.client;

import hr.eon.client.ClientConstants.ConnectionStatus;
import hr.eon.client.message.MessageDispatcher;
import hr.eon.core.Constants.Messages;
import hr.eon.core.EonMessage;
import hr.eon.core.message.Logout;
import hr.eon.core.message.handler.EonHandler;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class EonContext {

    private static final Logger LOGGER = Logger.getLogger( EonContext.class );

    private ApplicationContext appCtx;

    private ActiveMQConnectionFactory jmsTopicConnFactory;
    private TopicConnection jmsTopicConnection;
    private Session jmsSession;
    private Topic jmsTopic;
    private MessageConsumer jmsConsumer;
    private MessageProducer jmsProducer;

    private ConnectionStatus status = ConnectionStatus.DISCONNECTED;

    public EonContext() {
        try {
            LOGGER.info( "Starting Eon context..." );
            this.appCtx = new ClassPathXmlApplicationContext( "classpath:appCtx.xml" );

            initializeJms();

            status = ConnectionStatus.CONNECTED;
            LOGGER.info( "Eon Context started." );
        }
        catch ( JMSException e ) {
            LOGGER.info( e );
        }

    }

    public Object getBean( String beanId ) {
        return appCtx.getBean( beanId );
    }

    public <C> C getBean( Class<C> clazz ) {
        return (C) appCtx.getBean( clazz );
    }

    public EonHandler getHandlerForMessage( Class<?> clazz ) {
        return (EonHandler) this.getBean( this.getHandlerNameForMessage( clazz.getSimpleName() ) );
    }

    public String getHandlerNameForMessage( String messageName ) {
        return new StringBuffer().append( messageName.toLowerCase().charAt( 0 ) ).append( messageName.substring( 1 ) ).append( "Handler" ).toString();
    }

    public void sendMessage( EonMessage request ) {
        boolean shouldSend = false;
        if ( Messages.LOGIN.getCode() == request.getId() ) {
            shouldSend = true;
        } else if ( EonClient.me.getSessionToken() != null ) {
            shouldSend = true;
            request.setSessionToken( EonClient.me.getSessionToken() );
            request.generateChecksum();
        }

        if ( shouldSend ) {
            try {
                ActiveMQObjectMessage mqResponse = new ActiveMQObjectMessage();
                mqResponse.setObject( request );
                mqResponse.setJMSCorrelationID( request.isBroadcast() ? "BROADCAST" : EonClient.me.getJmsId() );

                jmsProducer.send( mqResponse );
            }
            catch ( JMSException e ) {
                LOGGER.info( "Error occured while sending message " + request, e );
            }
        }

    }

    private void initializeJms() throws JMSException {
        this.jmsTopicConnFactory = (ActiveMQConnectionFactory) getBean( "connectionFactory" );
        this.jmsTopicConnection = jmsTopicConnFactory.createTopicConnection();
        this.jmsSession = jmsTopicConnection.createSession( false, Session.AUTO_ACKNOWLEDGE );

        this.jmsTopic = (Topic) getBean( "jms/EON" );

        this.jmsConsumer = jmsSession.createConsumer( jmsTopic );
        this.jmsProducer = jmsSession.createProducer( jmsTopic );

        this.jmsConsumer.setMessageListener( new MessageDispatcher() );

        this.jmsTopicConnection.start();

        EonClient.me.setJmsId( jmsTopicConnection.getClientID() );

    }

    public void shutdown() {
        status = ConnectionStatus.DISCONNECTED;
        sendMessage( new Logout( EonClient.me.getId() ) );
    }

    public void setConnectionStatus( ConnectionStatus status ) {
        this.status = status;
    }

    public ConnectionStatus getConnectionStatus() {
        return this.status;
    }

    public boolean isServerAlive() {
        return !( status.equals( ConnectionStatus.MASTER_OFFLINE ) || status.equals( ConnectionStatus.DISCONNECTED ) );
    }

}
