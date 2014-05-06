package hr.eon.client.window.model;

import hr.eon.client.EonClient;
import hr.eon.core.model.ChatUser;
import hr.eon.core.model.Room;
import org.apache.pivot.wtk.content.TreeBranch;
import org.apache.pivot.wtk.content.TreeNode;

public class GuiRoom {

    private Room room;

    public GuiRoom( Room room ) {
        this.room = room;
    }

    public TreeNode getGuiComponents() {
        if ( this.room.isEmpty() ) {
            TreeNode tn = new TreeNode( this.room.getName() + getFillCounter() );
            tn.setIcon( "/hr/eon/client/window/icons/room_green.png" );
            return tn;
        } else {
            TreeBranch tb = new TreeBranch( this.room.getName() + getFillCounter() );
            tb.setIcon( "/hr/eon/client/window/icons/room_" + ( this.room.isFull() ? "red.png" : "green.png" ) );

            for ( ChatUser user : this.room.getUsers() ) {
                TreeNode tn = new TreeNode( user.getUsername() );
                boolean isMe = EonClient.me.getName().equals( user.getUsername() );

                tn.setIcon( "/hr/eon/client/window/icons/room_" + ( isMe ? "me.png" : "user.png" ) );
                tb.add( tn );
            }

            return tb;
        }
    }

    private String getFillCounter() {
        // @formatter:off
        return new StringBuffer().append( "  (" )
            .append( this.room.getUsers().size() ).append( "/" )
            .append( this.room.getCapacity() ).append( ")" )
            .toString();
        // @formatter:on
    }

}
