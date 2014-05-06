package hr.eon.server.message.handler;

import hr.eon.core.Constants.Status;
import hr.eon.core.EonMessage;
import hr.eon.core.message.Login;
import hr.eon.core.message.LoginResponse;
import hr.eon.core.message.MessageList;
import hr.eon.core.message.Rooms;
import hr.eon.core.message.handler.EonHandler;
import hr.eon.core.model.Room;
import hr.eon.server.EonServer;
import hr.eon.server.dao.DatabaseDao;
import hr.eon.server.dao.model.User;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Eon message handler for handling Login request message.
 * it check provided user credentials in database via DAO layer and return
 * status of check and if credentials are valid it also return session token
 * which validate user integrity.
 * 
 * @author mjovanovic
 * @since 0.0.1
 */
@Component
public class LoginHandler implements EonHandler {

    private static final Logger LOGGER = Logger.getLogger( LoginHandler.class );

    @Autowired
    private DatabaseDao databaseDao;

    @Override
    public EonMessage handle( EonMessage message, List<EonMessage> listMessage ) {
        Login login = (Login) message;

        LOGGER.info( "Login request received" );

        User user = databaseDao.doLogin( login.getUsername(), login.getPassword() );

        if ( user != null && EonServer.eonContext.getUserById( user.getId() ) == null ) {
            EonServer.eonContext.addUser( user );
            LOGGER.info( "Login is OK" );

            List<Room> rooms = databaseDao.getRooms();
            MessageList ml = new MessageList( user.getId() );
            ml.addMessage( new LoginResponse( Status.OK, user.getId(), user.getUsername(), user.getSession() ) );
            ml.addMessage( new Rooms( rooms, user.getId() ) );

            return ml;
        } else {
            LOGGER.info( "Login is FAIL" );
            return new LoginResponse( Status.FAIL, null, null, null );
        }
    }
}
