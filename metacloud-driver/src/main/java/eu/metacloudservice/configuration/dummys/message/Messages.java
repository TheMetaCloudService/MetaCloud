package eu.metacloudservice.configuration.dummys.message;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;

import java.util.HashMap;

public class Messages implements IConfigAdapter {


    public HashMap<String, String> messages;


    public Messages(HashMap<String, String> messages) {
        this.messages = messages;
    }

    public Messages() {
    }

    public HashMap<String, String> getMessages() {
        return messages;
    }
}
