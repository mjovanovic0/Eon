package hr.eon.core.message;

import hr.eon.core.Constants.Messages;
import hr.eon.core.EonMessage;

/**
 * Login message which is used to transport user credentials from Client to
 * Server application.
 * Password is in hashed type.
 * 
 * @author mjovanovic
 * @since 0.0.1
 */
public class Login extends EonMessage {

    private static final long serialVersionUID = 6153434408197814274L;

    private String username;
    private String password;

    public Login( String username, String password ) {
        super( Messages.LOGIN, Login.class, null );
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
