package hr.eon.server.dao.impl;

import hr.eon.core.message.ChatMessage;
import hr.eon.core.model.ChatUser;
import hr.eon.server.dao.DatabaseDao;
import hr.eon.server.dao.model.Chat;
import hr.eon.server.dao.model.Room;
import hr.eon.server.dao.model.User;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 * Implementation of DAO layer of Server application.
 * 
 * @author mjovanovic
 * @since 0.0.1
 */
@Repository
public class DatabaseDaoImpl implements DatabaseDao {

    private static final Logger LOGGER = Logger.getLogger( DatabaseDaoImpl.class );

    private SessionFactory sessionFactory;

    public void setSessionFactory( SessionFactory sessionFactory ) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User doLogin( String username, String password ) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        User user = null;
        try {
            transaction = session.beginTransaction();

            String hashedPassword = "P" + DigestUtils.shaHex( password ).toUpperCase();

            // @formatter:off
            user = (User) session.createCriteria( User.class )
                .add( Restrictions.eq( "username", username ) )
                .add( Restrictions.eq( "password", hashedPassword ) )
                .uniqueResult();
            // @formatter:on

            if ( user != null ) {
                user.generateSessionToken();
            }

            transaction.commit();
        }
        catch ( HibernateException he ) {
            transaction.rollback();
            LOGGER.error( "Hibernate exception: " + he.getMessage() );
        }
        finally {
            session.close();
        }

        return user;
    }

    @Override
    public User getUserById( int id ) {
        Session session = sessionFactory.openSession();

        User user = null;
        try {
            // @formatter:off
            user = (User) session.createCriteria( User.class )
                .add( Restrictions.eq( "id", id ) )
                .uniqueResult();
            // @formatter:on
        }
        catch ( HibernateException he ) {
            LOGGER.error( "Hibernate exception: " + he.getMessage() );
        }
        finally {
            session.close();
        }

        return user;
    }

    @SuppressWarnings( "unchecked" )
    @Override
    public List<hr.eon.core.model.Room> getRooms() {
        List<hr.eon.core.model.Room> rooms = new ArrayList<>();
        Session session = sessionFactory.openSession();

        try {
            List<Room> dbRooms = session.createCriteria( Room.class ).list();

            rooms = remap( dbRooms );
        }
        catch ( HibernateException he ) {
            LOGGER.error( "Hibernate exception: " + he.getMessage() );
        }
        finally {
            session.close();
        }

        return rooms;
    }

    @SuppressWarnings( "unchecked" )
    @Override
    public void joinRoom( int roomId, int userId ) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            User u = (User) session.createCriteria( User.class ).add( Restrictions.eq( "id", userId ) ).uniqueResult();
            List<Room> rooms = session.createCriteria( Room.class ).list();
            for ( Room r : rooms ) {
                if ( r.getId() == roomId ) {
                    r.getUsers().add( u );
                    session.save( r );
                } else if ( r.getUsers().contains( u ) ) {
                    Iterator<User> i = r.getUsers().iterator();
                    while ( i.hasNext() ) {
                        User usr = i.next();
                        if ( usr.getId() == userId ) {
                            i.remove();
                        }
                    }
                    session.save( r );
                }
            }

            transaction.commit();
        }
        catch ( HibernateException he ) {
            LOGGER.error( "Hibernate exception: " + he.getMessage() );
        }
        finally {
            session.close();
        }
    }

    @SuppressWarnings( "unchecked" )
    @Override
    public void logout( int userId ) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            List<Room> rooms = session.createCriteria( Room.class ).list();
            for ( Room r : rooms ) {
                Iterator<User> i = r.getUsers().iterator();
                while ( i.hasNext() ) {
                    User usr = i.next();
                    if ( usr.getId() == userId ) {
                        i.remove();
                    }
                }
                session.save( r );
            }

            transaction.commit();
        }
        catch ( HibernateException he ) {
            LOGGER.error( "Hibernate exception: " + he.getMessage() );
        }
        finally {
            session.close();
        }
    }

    public void storeChat( ChatMessage message ) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            User u = (User) session.createCriteria( User.class ).add( Restrictions.eq( "id", message.getUserId() ) ).uniqueResult();
            Chat c = new Chat( message, u );

            session.save( c );

            transaction.commit();
        }
        catch ( HibernateException he ) {
            LOGGER.error( "Hibernate exception: " + he.getMessage() );
        }
        finally {
            session.close();
        }
    }

    private List<hr.eon.core.model.Room> remap( List<Room> rooms ) {
        List<hr.eon.core.model.Room> newRooms = new ArrayList<>();

        for ( Room room : rooms ) {
            newRooms.add( new hr.eon.core.model.Room( room.getId(), room.getName(), room.getCapacity(), remapUsers( room.getUsers() ) ) );
        }

        return newRooms;
    }

    private List<ChatUser> remapUsers( List<User> users ) {
        List<ChatUser> newUsers = new ArrayList<>();

        for ( User user : users ) {
            newUsers.add( new ChatUser( user.getId(), user.getUsername() ) );
        }

        return newUsers;
    }
}
