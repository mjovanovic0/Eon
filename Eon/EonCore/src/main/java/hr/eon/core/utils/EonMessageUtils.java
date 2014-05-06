package hr.eon.core.utils;

import hr.eon.core.EonMessage;
import java.util.List;

public final class EonMessageUtils {

    private EonMessageUtils() {}

    @SuppressWarnings( "unchecked" )
    public static <C> C getMessageFromList( Class<C> wanted, List<EonMessage> messageList ) {
        for ( EonMessage message : messageList ) {
            if ( wanted.getSimpleName().equals( message.getName() ) ) {
                return (C) message;
            }
        }
        return null;
    }

}
