package hr.eon.client.window;

import hr.eon.client.EonClient;
import hr.eon.core.Constants.MessageDirection;
import hr.eon.core.message.ChatMessage;
import hr.eon.core.message.RoomJoin;
import hr.eon.core.model.Room;
import java.net.URL;
import org.apache.log4j.Logger;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.ComponentKeyListener;
import org.apache.pivot.wtk.ComponentMouseButtonListener;
import org.apache.pivot.wtk.Keyboard.KeyLocation;
import org.apache.pivot.wtk.MessageType;
import org.apache.pivot.wtk.Mouse.Button;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.TextArea;
import org.apache.pivot.wtk.TextInput;
import org.apache.pivot.wtk.TreeView;
import org.apache.pivot.wtk.Window;
import org.apache.pivot.wtk.content.TreeNode;

/**
 * GUI window for Main window.
 * 
 * @author mjovanovic
 * @since 0.0.2
 */
public class MainWindow extends Window implements Bindable {

    private static final Logger LOGGER = Logger.getLogger( MainWindow.class );

    private TextArea txtChat = null;
    private TextInput txtChatSay = null;
    private PushButton btnChatSay = null;
    private TreeView roomList;

    @Override
    public void initialize( Map<String, Object> namespace, URL location, Resources resources ) {

        txtChat = (TextArea) namespace.get( "txtChat" );
        roomList = (TreeView) namespace.get( "roomList" );
        txtChatSay = (TextInput) namespace.get( "txtChatSay" );
        btnChatSay = (PushButton) namespace.get( "btnChatSay" );

        roomList.getComponentMouseButtonListeners().add( new SelectionListener() );
        btnChatSay.getButtonPressListeners().add( new ButtonListener() );
        txtChatSay.getComponentKeyListeners().add( new InputListener() );

        EonClient.eonGui.setChatArea( txtChat );
        EonClient.eonGui.setRoomList( roomList );
        EonClient.eonGui.refreshRoomList();
    }

    class InputListener implements ComponentKeyListener {

        @Override
        public boolean keyTyped( Component component, char character ) {
            return false;
        }

        @Override
        public boolean keyPressed( Component component, int keyCode, KeyLocation keyLocation ) {
            return false;
        }

        @Override
        public boolean keyReleased( Component component, int keyCode, KeyLocation keyLocation ) {
            if ( keyCode == 10 ) {
                btnChatSay.press();
            }
            return false;
        }

    }

    class ButtonListener implements ButtonPressListener {

        @Override
        public void buttonPressed( org.apache.pivot.wtk.Button button ) {
            String txtSay = txtChatSay.getText();

            if ( EonClient.eonGui.getCurrentRoom() == -1 ) {
                EonClient.eonGui.prompt( MessageType.ERROR, "Prvo se pridruzite sobi za razgovor." );
                return;
            }

            if ( txtSay.length() == 0 ) {
                EonClient.eonGui.prompt( MessageType.ERROR, "Unesite poruku za slanje." );
                return;
            }

            ChatMessage cm = new ChatMessage( EonClient.me.getName(), EonClient.me.getId(), EonClient.eonGui.getCurrentRoom(), txtSay );
            cm.markBroadcast();
            cm.setFrom( MessageDirection.SERVER );

            EonClient.eonContext.sendMessage( cm );
            txtChatSay.setText( "" );
        }

    }

    class SelectionListener implements ComponentMouseButtonListener {

        @Override
        public boolean mouseDown( Component component, Button button, int x, int y ) {
            return false;
        }

        @Override
        public boolean mouseUp( Component component, Button button, int x, int y ) {
            return false;
        }

        @Override
        public boolean mouseClick( Component component, Button button, int x, int y, int count ) {
            if ( count == 2 ) {
                TreeNode selected = (TreeNode) roomList.getSelectedNode();
                if ( selected == null || !selected.getText().contains( "(" ) ) {
                    return false;
                }

                String roomName = selected.getText().substring( 0, selected.getText().lastIndexOf( '(' ) - 2 );
                Room room = EonClient.eonGui.getRoomByName( roomName );
                int roomId = room.getId();
                int currentRoom = EonClient.eonGui.getCurrentRoom();

                if ( room.isFull() ) {
                    EonClient.eonGui.prompt( MessageType.INFO, "Soba je puna!" );
                }

                if ( roomId > 0 && roomId != currentRoom && !room.isFull() ) {
                    EonClient.eonContext.sendMessage( new RoomJoin( roomId, EonClient.me.getId() ) );
                }
            }
            return false;
        }
    }
}
