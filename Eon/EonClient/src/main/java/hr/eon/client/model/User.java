package hr.eon.client.model;

import hr.eon.core.Constants.Status;
import hr.eon.core.message.LoginResponse;

/**
 * Truncated alias class for EonServer User domain model.
 * It contains user-related data which is essential for EonClient application.
 * 
 * @author mjovanovic
 * @since 0.0.1
 */
public class User {

    private int id;
    private String name;
    private String sessionToken;
    private Status loginStatus;
    private String jmsId;

    public int getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }

    public Status getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus( Status loginStatus ) {
        this.loginStatus = loginStatus;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken( String sesionToken ) {
        this.sessionToken = sesionToken;
    }

    public void populateWithLoginResponse( LoginResponse response ) {
        this.loginStatus = Status.OK;
        this.id = response.getUserId();
        this.name = response.getUsername();
        this.sessionToken = response.getSessionToken();
    }

    public String getJmsId() {
        return jmsId;
    }

    public void setJmsId( String jmsId ) {
        this.jmsId = jmsId;
    }

}
