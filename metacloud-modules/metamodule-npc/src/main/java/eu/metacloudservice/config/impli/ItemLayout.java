/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.config;

import java.util.ArrayList;

public class ItemLayout {

    public String material;
    public Integer subID;
    public String displayName;
    ArrayList<String> lore;

    public ItemLayout(String material, Integer subID, String displayName, ArrayList<String> lore) {
        this.material = material;
        this.subID = subID;
        this.displayName = displayName;
        this.lore = lore;
    }

    public String getMaterial() {
        return material;
    }

    public Integer getSubID() {
        return subID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ArrayList<String> getLore() {
        return lore;
    }
}
