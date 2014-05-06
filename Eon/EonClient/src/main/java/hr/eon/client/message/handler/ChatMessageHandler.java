package hr.eon.client.message.handler;

import hr.eon.client.EonClient;
import hr.eon.client.window.MainWindow;
import hr.eon.core.EonMessage;
import hr.eon.core.message.ChatMessage;
import hr.eon.core.message.handler.EonHandler;
import java.util.List;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageHandler implements EonHandler {

    private static final Logger LOGGER = Logger.getLogger( ChatMessageHandler.class );

    @Override
    public EonMessage handle( EonMessage message, List<EonMessage> listMessage ) {
        ChatMessage chatMessage = (ChatMessage) message;

        int myRoom = EonClient.eonGui.getCurrentRoom();

        if ( chatMessage.getRoomId() == myRoom ) {
            EonClient.eonGui.invokeAppendNewMessage( chatMessage );

            if ( chatMessage.getUserId() != EonClient.me.getId() ) {
                playSound( "notification.wav" );
            }
        }

        return null;
    }

    public synchronized void playSound( final String url ) {
        new Thread( new Runnable() {

            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream( MainWindow.class
                        .getResourceAsStream( "/hr/eon/client/window/sounds/" + url ) );
                    clip.open( inputStream );
                    clip.start();
                }
                catch ( Exception e ) {
                    LOGGER.error( "Error occured while playing notification sound.", e );
                }
            }
        } ).start();
    }
}
