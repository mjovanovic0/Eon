package hr.eon.client.window;

import hr.eon.client.EonClient;
import hr.eon.client.window.validation.ValidationUtils;
import hr.eon.core.message.Login;
import java.net.URL;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.FlowPane;
import org.apache.pivot.wtk.Form;
import org.apache.pivot.wtk.Label;
import org.apache.pivot.wtk.MessageType;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.TextInput;
import org.apache.pivot.wtk.Window;

/**
 * GUI window for Login window.
 * 
 * @author mjovanovic
 * @since 0.0.2
 */
public class LoginWindow extends Window implements Bindable {

    private FlowPane flgUsername = null;
    private FlowPane flgPassword = null;
    private TextInput txtUsername = null;
    private TextInput txtPassword = null;
    private Label errorLabel = null;

    @Override
    public void initialize( Map<String, Object> namespace, URL location, Resources resources ) {
        flgUsername = (FlowPane) namespace.get( "flgUsername" );
        flgPassword = (FlowPane) namespace.get( "flgPassword" );
        txtUsername = (TextInput) namespace.get( "txtUsername" );
        txtPassword = (TextInput) namespace.get( "txtPassword" );
        errorLabel = (Label) namespace.get( "errorLabel" );

        PushButton submitButton = (PushButton) namespace.get( "submitButton" );
        submitButton.getButtonPressListeners().add( new ButtonListener() );

        if ( !EonClient.eonContext.isServerAlive() ) {
            errorLabel.setText( "Cannot conect to master server." );
        }
    }

    class ButtonListener implements ButtonPressListener {

        @Override
        public void buttonPressed( Button arg0 ) {
            String strUsername = txtUsername.getText();
            String strPassword = txtPassword.getText();

            Form.Flag flagUsername = null;
            Form.Flag flagPassword = null;

            if ( strUsername.length() == 0 ) {
                flagUsername = new Form.Flag( MessageType.ERROR, "Unesite username." );
            } else if ( !ValidationUtils.assertInRange( strUsername, ValidationUtils.LOGIN_USERNAME_MIN_LENGTH,
                                                        ValidationUtils.LOGIN_USERNAME_MAX_LENGTH ) ) {
                if ( strUsername.length() < ValidationUtils.LOGIN_USERNAME_MIN_LENGTH ) {
                    flagUsername = new Form.Flag( MessageType.ERROR, "Minimalno 4 znakova." );
                } else if ( strUsername.length() > ValidationUtils.LOGIN_USERNAME_MAX_LENGTH ) {
                    flagUsername = new Form.Flag( MessageType.ERROR, "Maksimalno 20 znakova." );
                }
            }

            if ( strPassword.length() == 0 ) {
                flagPassword = new Form.Flag( MessageType.ERROR, "Unesite password." );
            } else if ( !ValidationUtils.assertInRange( strPassword, ValidationUtils.LOGIN_PASSWORD_MIN_LENGTH,
                                                        ValidationUtils.LOGIN_PASSWORD_MAX_LENGTH ) ) {
                if ( strPassword.length() < ValidationUtils.LOGIN_PASSWORD_MIN_LENGTH ) {
                    flagPassword = new Form.Flag( MessageType.ERROR, "Minimalno 4 znakova." );
                } else if ( strPassword.length() > ValidationUtils.LOGIN_PASSWORD_MAX_LENGTH ) {
                    flagPassword = new Form.Flag( MessageType.ERROR, "Maksimalno 20 znakova." );
                }
            }

            Form.setFlag( flgUsername, flagUsername );
            Form.setFlag( flgPassword, flagPassword );

            if ( flagUsername == null && flagPassword == null ) {
                errorLabel.setText( null );
                Login msgLogin = new Login( strUsername, strPassword );
                EonClient.eonContext.sendMessage( msgLogin );
            } else {
                errorLabel.setText( "Provjerite upisane podatke" );
            }
        }

    }
}
