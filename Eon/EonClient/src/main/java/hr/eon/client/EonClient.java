package hr.eon.client;

import hr.eon.client.model.User;
import hr.eon.client.window.LoginWindow;

/**
 * Main class for Client.
 * It start EonPluginManager and EonContext with Spring backend and raise login
 * GUI
 * 
 * @author mjovanovic
 * @since 0.0.1
 */
public final class EonClient {

    public static User me;
    public static EonContext eonContext;
    public static EonGui eonGui;

    public static void main( String[] args ) {
        me = new User();
        eonGui = new EonGui( args );
        eonContext = new EonContext();
        eonGui.invokeSwitchWindow( LoginWindow.class );
    }

    public static void shutdown() {
        eonContext.shutdown();
    }
}
