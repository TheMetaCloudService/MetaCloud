/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.cloudflare;

import org.json.JSONObject;

import java.util.Collections;
import java.util.Map;

public class Json {

    private final JSONObject object;

    public Json() {
        this.object = new JSONObject();
    }

    public Json append(String key, String string){
        this.object.put(key, string);
        return this;
    }

    public Json append(String key, int ints){
        this.object.put(key, ints);
        return this;
    }

    public Json append(String key, Map map){
        this.object.put(key, map);
        return this;
    }

    public Json append(String key, Long longs){
        this.object.put(key, longs);
        return this;
    }

    public Json append(String key, Double doubles){
        this.object.put(key, doubles);
        return this;
    }


    public Json append(String key, Object obj){
        this.object.put(key, obj);
        return this;
    }

    public Json append(String key, Boolean bool){
        this.object.put(key, bool);
        return this;
    }

    public Json append(String key, Collections collections){
        this.object.put(key, collections);
        return this;
    }

    public String build(){
        return object.toString();
    }

}
