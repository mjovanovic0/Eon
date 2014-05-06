package hr.eon.core.message.handler;

import hr.eon.core.EonMessage;
import java.util.List;

/**
 * Interface that all handlers implements to become Eon message handler.
 * 
 * @author mjovanovic
 * @since 0.0.1
 */
public interface EonHandler {

    EonMessage handle( EonMessage message, List<EonMessage> listMessage );

}
