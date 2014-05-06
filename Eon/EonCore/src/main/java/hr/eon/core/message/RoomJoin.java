package hr.eon.core.message;

import hr.eon.core.Constants.Messages;
import hr.eon.core.EonMessage;

public class RoomJoin extends EonMessage {

    private static final long serialVersionUID = 6271698189945217284L;

    private int roomId;

    public RoomJoin( int roomId, int userId ) {
        super( Messages.JOINROOM, RoomJoin.class, userId );
        this.roomId = roomId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId( int roomId ) {
        this.roomId = roomId;
    }

}
