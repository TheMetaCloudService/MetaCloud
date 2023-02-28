package eu.metacloudservice.cloudplayer.components;

import eu.metacloudservice.cloudplayer.components.actions.ClickEventAction;
import eu.metacloudservice.cloudplayer.components.actions.HoverEventAction;

import java.util.ArrayList;

public class CloudComponent {

    private String component;
    private String clickEventAction;
    private String clickResul;
    private String hoverEventAction;
    private String hoverResul;
    private ArrayList<String> extras;

    public CloudComponent(String text) {
        this.component = text;
        extras = new ArrayList<>();

    }

    public CloudComponent setClickEvent(ClickEventAction action, String object){
        this.clickEventAction =action.toString();
        this.clickResul = object;
        return this;
    }

    public CloudComponent setHoverEvent(HoverEventAction action, String object){
        this.hoverEventAction =action.toString();
        this.hoverResul = object;
        return this;
    }
    public CloudComponent addExtra(String message){
        extras.add(message);
        return this;
    }


    public String getComponent() {
        return component;
    }

    public String getClickEventAction() {
        return clickEventAction;
    }

    public String getClickResul() {
        return clickResul;
    }

    public String getHoverEventAction() {
        return hoverEventAction;
    }

    public String getHoverResul() {
        return hoverResul;
    }

    public ArrayList<String> getExtras() {
        return extras;
    }
}
