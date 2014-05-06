package hr.eon.server.dao;

import hr.eon.core.message.ChatMessage;
import hr.eon.core.model.Room;
import hr.eon.server.dao.model.User;
import java.util.List;

/**
 * DAO layer of Server application.
 * 
 * @author mjovanovic
 * @since 0.0.1
 */
public interface DatabaseDao {

    /**
     * Check user credentials and return sessionToken if credentials was good.
     * Otherwise return null
     */
    User doLogin( String username, String password );

    /**
     * Get user by it's id.
     * Otherwise return null
     */
    User getUserById( int id );

    List<Room> getRooms();

    void joinRoom( int roomId, int userId );

    void logout( int userId );

    void storeChat( ChatMessage message );

}
