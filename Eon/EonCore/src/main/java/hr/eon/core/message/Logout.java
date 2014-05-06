package hr.eon.core.message;

import hr.eon.core.Constants.Messages;
import hr.eon.core.EonMessage;

public class Logout extends EonMessage {

    private static final long serialVersionUID = -3487062228215970873L;

    public Logout( int id ) {
        super( Messages.LOGOUT, Logout.class, id );
    }

}
