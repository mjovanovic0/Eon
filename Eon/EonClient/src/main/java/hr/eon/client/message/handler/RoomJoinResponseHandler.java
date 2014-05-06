package hr.eon.client.message.handler;

import hr.eon.client.EonClient;
import hr.eon.core.EonMessage;
import hr.eon.core.message.Rooms;
import hr.eon.core.message.handler.EonHandler;
import hr.eon.core.utils.EonMessageUtils;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class RoomJoinResponseHandler implements EonHandler {

    @Override
    public EonMessage handle( EonMessage message, List<EonMessage> listMessage ) {
        Rooms rooms = EonMessageUtils.getMessageFromList( Rooms.class, listMessage );

        EonClient.eonGui.setRooms( rooms.getRooms() );
        EonClient.eonGui.invokeRefreshRoomList();

        return null;
    }

}
