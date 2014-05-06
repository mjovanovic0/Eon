package hr.eon.core.message;

import hr.eon.core.Constants.Messages;
import hr.eon.core.EonMessage;

public class RoomJoinResponse extends EonMessage {

    private static final long serialVersionUID = 190626121345810884L;

    public RoomJoinResponse( Integer userId ) {
        super( Messages.JOINROOM_RESPONSE, RoomJoinResponse.class, userId );
    }

}
