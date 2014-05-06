package hr.eon.core.message;

import hr.eon.core.Constants.Messages;
import hr.eon.core.Constants.Status;
import hr.eon.core.EonMessage;

/**
 * Response message which is sent after check mechanism which process received
 * Login message finish.
 * Message transport login check status and assigned session token if login was
 * successfull.
 * 
 * @author mjovanovic
 * @since 0.0.1
 */
public class LoginResponse extends EonMessage {

    private static final long serialVersionUID = 6153434408197814274L;

    private Integer id;
    private String username;
    private Status status;
    private String sessionToken;

    public LoginResponse( Status status, Integer id, String username, String sessionToken ) {
        super( Messages.LOGIN_RESPONSE, LoginResponse.class, id );
        this.status = status;
        this.id = id;
        this.username = username;
        this.sessionToken = sessionToken;
    }

    public Status getStatus() {
        return status;
    }

    public Integer getUserId() {
        return id;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public String getUsername() {
        return username;
    }

}
