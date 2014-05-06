package hr.eon.client.window.model;

import hr.eon.core.model.ChatUser;
import org.apache.pivot.wtk.content.TreeNode;

public class GuiChatUser {

    private ChatUser chatUser;

    public GuiChatUser( ChatUser chatUser ) {
        this.chatUser = chatUser;
    }

    public TreeNode getElemenet() {
        return new TreeNode( this.chatUser.getUsername() );
    }

}
