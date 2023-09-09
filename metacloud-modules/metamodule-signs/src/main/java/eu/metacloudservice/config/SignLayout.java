package eu.metacloudservice.config;

public class SignLayout {

    String[] lines;
    String itemID;
    boolean glowText;
    String color;

    public SignLayout() {}

    public String[] getLines() {
        return lines;
    }

    public String getItemID() {
        return itemID;
    }


    public boolean isGlowText() {
        return glowText;
    }

    public String getColor() {
        return color;
    }

    public SignLayout(String[] lines, String itemID, boolean glowText, String color) {
        this.lines = lines;
        this.itemID = itemID;
        this.glowText = glowText;
        this.color = color;
    }
}
