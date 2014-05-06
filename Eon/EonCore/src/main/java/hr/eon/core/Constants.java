package hr.eon.core;

/**
 * Constants that are used through many aspects of all Eon applications.
 * 
 * @author mjovanovic
 * @since 0.0.1
 */
public class Constants {

    public enum LogLevels {
        TRACE, INFO, DEBUG, WARN, ERROR
    }

    public enum Messages {


        // @formatter:off
        MESSAGE_LIST( 1 ),
        LOGIN( 1000 ),
        LOGIN_RESPONSE( 1001 ),
        LOGOUT( 1002 ),
        ROOMS( 1100 ),
        JOINROOM( 1101 ),
        JOINROOM_RESPONSE( 1102 ),
        CHATMESSAGE( 1103 );
        // @formatter:on

        private int id;

        Messages( int id ) {
            this.id = id;
        }

        public int getCode() {
            return id;
        }
    }

    public enum Status {
        OK, FAIL
    }

    public enum MessageDirection {
        SERVER, CLIENT
    }

}
