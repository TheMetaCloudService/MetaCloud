package eu.metacloudservice.pool.player.components;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;
import eu.metacloudservice.pool.player.components.actions.ClickEventAction;
import eu.metacloudservice.pool.player.components.actions.HoverEventAction;
import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;

public class CloudComponent {

    @Getter
    private String component;

    @Getter
    private String clickEventAction;

    @Getter
    private String clickResul;

    @Getter
    private String hoverEventAction;

    @Getter
    private String hoverResul;

    @Getter
    private ArrayList<String> extras;



    public CloudComponent(String component) {
        this.component = component;
        extras = new ArrayList<>();

    }

    public CloudComponent setClickEvent(@NonNull ClickEventAction action, @NonNull String object){
        this.clickEventAction =action.toString();
        this.clickResul = object;
        return this;
    }

    public CloudComponent setHoverEvent(@NonNull HoverEventAction action, @NonNull String object){
        this.hoverEventAction =action.toString();
        this.hoverResul = object;
        return this;
    }
    public CloudComponent addExtra(@NonNull String message){
        extras.add(message);
        return this;
    }

}
