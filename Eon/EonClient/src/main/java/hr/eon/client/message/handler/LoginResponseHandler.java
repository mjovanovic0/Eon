package hr.eon.client.message.handler;

import hr.eon.client.ClientConstants.ConnectionStatus;
import hr.eon.client.EonClient;
import hr.eon.client.window.MainWindow;
import hr.eon.core.Constants.Status;
import hr.eon.core.EonMessage;
import hr.eon.core.message.LoginResponse;
import hr.eon.core.message.Rooms;
import hr.eon.core.message.handler.EonHandler;
import hr.eon.core.utils.EonMessageUtils;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.pivot.wtk.MessageType;
import org.springframework.stereotype.Component;

/**
 * Eon message handler for handling LoginResponse response message.
 * It notify visually user if login is unsuccessfull or load main application
 * window on successfull login.
 * 
 * @author mjovanovic
 * @since 0.0.1
 */
@Component
public class LoginResponseHandler implements EonHandler {

    private static final Logger LOGGER = Logger.getLogger( LoginResponseHandler.class );

    @Override
    public EonMessage handle( EonMessage message, List<EonMessage> listMessages ) {
        LoginResponse loginResponse = (LoginResponse) message;

        LOGGER.info( "Login response received" );

        if ( Status.OK.equals( loginResponse.getStatus() ) ) {
            EonClient.me.populateWithLoginResponse( loginResponse );
            EonClient.eonContext.setConnectionStatus( ConnectionStatus.AUTHORIZED );
            LOGGER.info( "Login is OK" );

            if ( listMessages.size() == 0 ) {
                LOGGER.warn( "Room list not received!" );
            } else {
                Rooms rooms = EonMessageUtils.getMessageFromList( Rooms.class, listMessages );
                EonClient.eonGui.setRooms( rooms.getRooms() );
            }

            EonClient.eonGui.invokeSwitchWindow( MainWindow.class );
        } else {
            EonClient.me.setLoginStatus( Status.FAIL );
            LOGGER.info( "Login is FAIL" );

            EonClient.eonGui.invokePrompt( MessageType.ERROR, "Neispravni autorizacijski podaci." );
        }

        return null;
    }
}
