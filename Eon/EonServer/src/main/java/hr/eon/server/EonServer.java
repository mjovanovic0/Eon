package hr.eon.server;

/**
 * Main class for Server.
 * It start EonPluginManager and EonContext with Spring backend.
 * 
 * @author mjovanovic
 * @since 0.0.1
 */
public final class EonServer {

    private EonServer() {}

    public static EonContext eonContext;

    public static void main( String[] args ) throws InterruptedException {
        eonContext = new EonContext();
    }

}
