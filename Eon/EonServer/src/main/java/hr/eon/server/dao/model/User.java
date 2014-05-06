package hr.eon.server.dao.model;

import hr.eon.server.ServerConstants;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Domain model of user entity.
 * 
 * @author mjovanovic
 * @since 0.0.1
 */
@Entity
@Table( name = "USERS" )
public class User implements Serializable {

    private static final long serialVersionUID = 9034030297284881888L;

    private int id;
    private String username;
    private String password;
    private String email;
    private String session;

    public User() {}

    @Id
    @GeneratedValue
    @Column( name = "USER_ID" )
    public int getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }

    @Column( name = "USER_NAME", nullable = false )
    public String getUsername() {
        return username;
    }

    public void setUsername( String username ) {
        this.username = username;
    }

    @Column( name = "USER_PASSWORD", nullable = false )
    public String getPassword() {
        return password;
    }

    public void setPassword( String password ) {
        this.password = password;
    }

    @Column( name = "USER_EMAIL", nullable = false )
    public String getEmail() {
        return email;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    @Column( name = "USER_SESSION" )
    public String getSession() {
        return session;
    }

    public void setSession( String session ) {
        this.session = session;
    }

    public void generateSessionToken() {
        String sessionToken;
        // @formatter:off
        sessionToken = new StringBuilder()
            .append( ServerConstants.SHA1_SEED )
            .append( new Date().getTime() )
            .append( this.getUsername() )
            .append( this.getId() )
            .toString();
        // @formatter:on
        sessionToken = "S" + DigestUtils.shaHex( sessionToken ).toUpperCase();
        this.setSession( sessionToken );
    }
}
