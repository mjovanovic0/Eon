package hr.eon.client;

import hr.eon.client.window.BlankWindow;
import hr.eon.client.window.model.GuiRoom;
import hr.eon.core.message.ChatMessage;
import hr.eon.core.model.ChatUser;
import hr.eon.core.model.Room;
import java.awt.EventQueue;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.collections.List;
import org.apache.pivot.collections.Map;
import org.apache.pivot.serialization.SerializationException;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.ApplicationContext;
import org.apache.pivot.wtk.Bounds;
import org.apache.pivot.wtk.DesktopApplicationContext;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.MessageType;
import org.apache.pivot.wtk.Prompt;
import org.apache.pivot.wtk.TextArea;
import org.apache.pivot.wtk.TreeView;
import org.apache.pivot.wtk.Window;

public class EonGui implements Application {

    private static final Logger LOGGER = Logger.getLogger( EonGui.class );
    private static final String NEW_CHAT_TEMPLATE = "%s - %s > %s";

    private BXMLSerializer bxml;
    private static Display display;
    private Window window;

    private TextArea chatArea;
    private TreeView roomList;
    private java.util.List<Room> rooms;

    public EonGui() {}

    public EonGui( String[] args ) {
        DesktopApplicationContext.main( EonGui.class, args );
        bxml = new BXMLSerializer();
        openWindow( BlankWindow.class );
    }

    public void openWindow( Class<?> clazz ) {
        try {
            String bxmlLocation = String.format( "xml/%s.xml", clazz.getSimpleName() );
            window = (Window) bxml.readObject( clazz, bxmlLocation );
            window.open( display );
            display.getHostWindow().setSize( window.getSize().width, window.getSize().height );
        }
        catch ( IOException | SerializationException e ) {
            LOGGER.error( "Error occured while opening window " + clazz.getSimpleName(), e );
        }
    }

    @SuppressWarnings( { "unchecked", "rawtypes" } )
    public void refreshRoomList() {
        List guiRooms = roomList.getTreeData();
        guiRooms.clear();

        for ( Room room : getRooms() ) {
            guiRooms.add( new GuiRoom( room ).getGuiComponents() );
        }
        roomList.expandAll();
    }

    public void setRooms( java.util.List<Room> rooms ) {
        this.rooms = rooms;
    }

    public Room getRoomByName( String roomName ) {
        for ( Room room : rooms ) {
            if ( roomName.equals( room.getName() ) ) {
                return room;
            }
        }
        return null;
    }

    public int getCurrentRoom() {
        for ( Room room : rooms ) {
            for ( ChatUser user : room.getUsers() ) {
                if ( user.getId() == EonClient.me.getId() ) {
                    return room.getId();
                }
            }
        }
        return -1;
    }

    public java.util.List<Room> getRooms() {
        if ( this.rooms == null ) {
            this.rooms = new ArrayList<>();
        }
        return rooms;
    }

    public void invokeRefreshRoomList() {
        LOGGER.trace( "Invoking refreshing roomList" );
        try {
            EventQueue.invokeAndWait( new Runnable() {

                @Override
                public void run() {
                    refreshRoomList();
                }
            } );
        }
        catch ( InvocationTargetException | InterruptedException e ) {
            LOGGER.error( "Error invoking refreshing roomList", e );
        }
    }

    public void invokeSwitchWindow( final Class<?> clazz ) {
        LOGGER.trace( "Invoking switching to window " + clazz.getSimpleName() );
        try {
            EventQueue.invokeAndWait( new Runnable() {

                @Override
                public void run() {
                    openWindow( clazz );
                }
            } );
        }
        catch ( InvocationTargetException | InterruptedException e ) {
            LOGGER.error( "Error invoking switching to window: " + clazz.getSimpleName(), e );
        }
    }

    public void prompt( final MessageType type, final String message ) {
        Prompt.prompt( type, message, window );
    }

    public void invokePrompt( final MessageType type, final String message ) {
        LOGGER.trace( "Invoking prompt event " );
        try {
            EventQueue.invokeAndWait( new Runnable() {

                @Override
                public void run() {
                    Prompt.prompt( type, message, window );
                }
            } );
        }
        catch ( InvocationTargetException | InterruptedException e ) {
            LOGGER.error( "Error prompting user.", e );
        }
    }

    public TextArea getChatArea() {
        return this.chatArea;
    }

    public void setChatArea( TextArea chatArea ) {
        this.chatArea = chatArea;
    }

    public void appendNewMessage( final ChatMessage message ) {
        String oldText = chatArea.getText() + (char) 10;
        final String newText = String.format( NEW_CHAT_TEMPLATE, message.getDate(), message.getSenderName(), message.getText() );
        chatArea.setText( oldText + newText );
        ApplicationContext.queueCallback( new Runnable() {

            @Override
            public void run() {
                Bounds bounds = chatArea.getCharacterBounds( chatArea.getCharacterCount() );
                chatArea.scrollAreaToVisible( bounds );
            }
        } );
    }

    public void invokeAppendNewMessage( final ChatMessage message ) {
        LOGGER.trace( "Invoking appending to chat " );
        try {
            EventQueue.invokeAndWait( new Runnable() {

                @Override
                public void run() {
                    appendNewMessage( message );
                }
            } );
        }
        catch ( InvocationTargetException | InterruptedException e ) {
            LOGGER.error( "Error appending to chat.", e );
        }
    }

    @Override
    public void startup( Display dsp, Map<String, String> properties ) throws Exception {
        display = dsp;
    }

    @Override
    public boolean shutdown( boolean optional ) throws Exception {
        if ( window != null ) {
            window.close();
        }
        EonClient.shutdown();
        return false;
    }

    @Override
    public void suspend() throws Exception {}

    @Override
    public void resume() throws Exception {}

    public TreeView getRoomList() {
        return roomList;
    }

    public void setRoomList( TreeView roomList ) {
        this.roomList = roomList;
    }
}
