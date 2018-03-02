package chv.has.utils.UserInterface;

import chv.has.HAS;
import chv.has.model.interfaces.MessageInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Christopher Anciaux
 */
public class MessageInterfaceManager extends AbstractUserInterfaceManager {
    private Map<MessageInterface, chv.has.utils.UserInterface.MessageInterface> handledMessages;

    public MessageInterfaceManager(HAS has) {
        super(has);
        this.handledMessages = new HashMap<>();
    }

    public void displayMessage(MessageInterface message) {
        this.handledMessages.put(message, new chv.has.utils.UserInterface.MessageInterface(this, message));
    }

    public void forgetMessage(MessageInterface message) {
        this.handledMessages.remove(message);
    }
}
