package eu.metacloudservice.config;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;
import lombok.Getter;

import java.util.HashMap;

public class IconBase implements IConfigAdapter {

    @Getter
    private HashMap<String, String> icons;

    public IconBase() {
    }

    public IconBase(HashMap<String, String> icons) {
        this.icons = icons;
    }
}
