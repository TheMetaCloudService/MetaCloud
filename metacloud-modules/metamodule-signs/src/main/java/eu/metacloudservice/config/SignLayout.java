package eu.metacloudservice.config;

public class SignLayout {

    String[] lines;
    String itemID;
    String glowColor;

    public SignLayout() {}

    public String[] getLines() {
        return lines;
    }

    public String getItemID() {
        return itemID;
    }


    public String getGlowColor() {
        return glowColor;
    }

    public SignLayout(String[] lines, String itemID, String glowcolor) {
        this.lines = lines;
        this.itemID = itemID;
        this.glowColor = glowcolor;
    }
}
