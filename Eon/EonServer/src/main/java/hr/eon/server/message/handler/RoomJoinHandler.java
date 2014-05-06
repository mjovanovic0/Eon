package hr.eon.server.message.handler;

import hr.eon.core.EonMessage;
import hr.eon.core.message.MessageList;
import hr.eon.core.message.RoomJoin;
import hr.eon.core.message.RoomJoinResponse;
import hr.eon.core.message.Rooms;
import hr.eon.core.message.handler.EonHandler;
import hr.eon.core.model.Room;
import hr.eon.server.dao.DatabaseDao;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoomJoinHandler implements EonHandler {

    private static final Logger LOGGER = Logger.getLogger( RoomJoinHandler.class );

    @Autowired
    private DatabaseDao databaseDao;

    @Override
    public EonMessage handle( EonMessage message, List<EonMessage> listMessage ) {

        RoomJoin roomJoin = (RoomJoin) message;

        LOGGER.info( "Adding user " + roomJoin.getUserId() + " to room " + roomJoin.getRoomId() );

        databaseDao.joinRoom( roomJoin.getRoomId(), roomJoin.getUserId() );
        List<Room> rooms = databaseDao.getRooms();
        Rooms newRooms = new Rooms( rooms, roomJoin.getUserId() );

        MessageList ml = new MessageList( -1 );
        ml.markBroadcast();
        ml.addMessage( new RoomJoinResponse( -1 ) );
        ml.addMessage( newRooms );

        // Notify all users with new roomList status

        return ml;
    }

}
