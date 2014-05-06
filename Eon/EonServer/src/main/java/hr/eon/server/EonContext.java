package hr.eon.server;

import hr.eon.core.EonMessage;
import hr.eon.core.message.handler.EonHandler;
import hr.eon.server.dao.model.User;
import hr.eon.server.message.MessageDispatcher;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.jms.JMSException;
import javax.jms.Message;
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

    private List<User> activeUsers;

    public EonContext() {
        try {
            activeUsers = new ArrayList<>();

            LOGGER.info( "Starting Eon context..." );
            this.appCtx = new ClassPathXmlApplicationContext( "classpath:appCtx.xml" );

            initializeJmsAsTopic();

            LOGGER.info( "Eon Context started." );
        }
        catch ( JMSException e ) {
            LOGGER.error( "Fail to start EonContext.", e );
        }

    }

    public User getUserById( int id ) {
        for ( User user : activeUsers ) {
            if ( user.getId() == id ) {
                return user;
            }
        }
        return null;
    }

    public void addUser( User user ) {
        activeUsers.add( user );
    }

    public void removeUser( int id ) {
        Iterator<User> i = activeUsers.iterator();
        while ( i.hasNext() ) {
            User u = i.next();
            if ( u.getId() == id ) {
                i.remove();
                return;
            }
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

    public void sendMessage( Message request, EonMessage response ) {
        LOGGER.debug( "Sending message " + response );
        try {
            ActiveMQObjectMessage mqResponse = new ActiveMQObjectMessage();
            mqResponse.setObject( response );
            mqResponse.setJMSCorrelationID( response.isBroadcast() ? "BROADCAST" : request.getJMSCorrelationID() );

            jmsProducer.send( mqResponse );
        }
        catch ( JMSException e ) {
            LOGGER.info( "Error occured while sending message " + response, e );
        }
    }

    private void initializeJmsAsTopic() throws JMSException {
        this.jmsTopicConnFactory = (ActiveMQConnectionFactory) getBean( "connectionFactory" );
        this.jmsTopicConnection = jmsTopicConnFactory.createTopicConnection();
        this.jmsSession = jmsTopicConnection.createSession( false, Session.AUTO_ACKNOWLEDGE );

        this.jmsTopic = (Topic) getBean( "jms/EON" );

        this.jmsConsumer = jmsSession.createConsumer( jmsTopic );
        this.jmsProducer = jmsSession.createProducer( jmsTopic );

        this.jmsConsumer.setMessageListener( new MessageDispatcher() );

        this.jmsTopicConnection.start();

    }
}
