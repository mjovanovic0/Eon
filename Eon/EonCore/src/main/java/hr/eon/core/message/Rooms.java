package hr.eon.core.message;

import hr.eon.core.Constants.Messages;
import hr.eon.core.EonMessage;
import hr.eon.core.model.Room;
import java.util.List;

public class Rooms extends EonMessage {

    private static final long serialVersionUID = 7627346406831994028L;

    private List<Room> rooms;

    public Rooms( List<Room> rooms, int userId ) {
        super( Messages.ROOMS, Rooms.class, userId );
        this.rooms = rooms;
    }

    public List<Room> getRooms() {
        return this.rooms;
    }

    public void setRooms( List<Room> rooms, boolean append ) {
        if ( append ) {
            rooms.addAll( rooms );
        } else {
            this.rooms = rooms;
        }
    }

}
