/*
 * this class is by RauchigesEtwas
 */

/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.config.impli;

import java.util.ArrayList;

public class ItemLayout {

    public String material;
    public String displayName;
    ArrayList<String> lore;


    public ItemLayout() {
    }

    public ItemLayout(String material, String displayName, ArrayList<String> lore) {
        this.material = material;

        this.displayName = displayName;
        this.lore = lore;
    }

    public String getMaterial() {
        return material;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ArrayList<String> getLore() {
        return lore;
    }
}
