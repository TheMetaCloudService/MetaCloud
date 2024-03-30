/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.language.entry;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;

import java.util.HashMap;

public class LanguagePacket {

    private HashMap<String, String> data;

    public LanguagePacket() {
        data = new HashMap<>();
    }

    public String getMessage(String path){
        return data.get(path);
    }

    public void update(HashMap<String, String> map ){
        data.clear();
        data.putAll(map);
    }

    public HashMap<String, String> getMessages(){
        return data;
    }
    public void add(String path, String message){
        data.put(path, message);
    }
}
