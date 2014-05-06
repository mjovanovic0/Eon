package hr.eon.server.message.handler;

import hr.eon.core.EonMessage;
import hr.eon.core.message.Logout;
import hr.eon.core.message.MessageList;
import hr.eon.core.message.RoomJoinResponse;
import hr.eon.core.message.Rooms;
import hr.eon.core.message.handler.EonHandler;
import hr.eon.core.model.Room;
import hr.eon.server.EonServer;
import hr.eon.server.dao.DatabaseDao;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LogoutHandler implements EonHandler {

    private static final Logger LOGGER = Logger.getLogger( LogoutHandler.class );

    @Autowired
    private DatabaseDao databaseDao;

    @Override
    public EonMessage handle( EonMessage message, List<EonMessage> listMessage ) {
        Logout logout = (Logout) message;

        LOGGER.info( "Logout request received" );

        EonServer.eonContext.removeUser( logout.getUserId() );

        databaseDao.logout( logout.getUserId() );
        List<Room> rooms = databaseDao.getRooms();
        Rooms newRooms = new Rooms( rooms, -1 );

        MessageList ml = new MessageList( -1 );
        ml.markBroadcast();
        ml.addMessage( new RoomJoinResponse( -1 ) );
        ml.addMessage( newRooms );

        LOGGER.debug( "Logout msg " + ml );

        return ml;
    }
}
