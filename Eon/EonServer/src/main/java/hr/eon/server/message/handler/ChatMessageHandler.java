package hr.eon.server.message.handler;

import hr.eon.core.EonMessage;
import hr.eon.core.message.ChatMessage;
import hr.eon.core.message.handler.EonHandler;
import hr.eon.server.dao.DatabaseDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageHandler implements EonHandler {

    @Autowired
    private DatabaseDao databaseDao;

    @Override
    public EonMessage handle( EonMessage message, List<EonMessage> listMessage ) {
        ChatMessage chatMessage = (ChatMessage) message;

        databaseDao.storeChat( chatMessage );

        return null;
    }
}
