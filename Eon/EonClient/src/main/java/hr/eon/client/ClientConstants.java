package hr.eon.client;

/**
 * Client related constants that should not be available to Server.
 * 
 * @author mjovanovic
 * @since 0.0.2
 */
public class ClientConstants {

    private ClientConstants() {}

    public enum ConnectionStatus {
        // @formatter:off
        INITIALIZING( 0 ), 
        CONNECTED( 1 ), 
        MASTER_OFFLINE( 2 ), 
        DISCONNECTED( 3 ),
        AUTHORIZED( 4 );
        // @formatter:on

        private int code;

        ConnectionStatus( int code ) {
            this.code = code;
        }

        public int getCode() {
            return this.code;
        }

        ConnectionStatus getConnectionStatusByCode( int code ) {
            for ( ConnectionStatus status : values() ) {
                if ( status.getCode() == code ) {
                    return status;
                }
            }
            return null;
        }
    }

}
