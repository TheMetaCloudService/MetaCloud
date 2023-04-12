package eu.metacloudservice.config;

public class SignLayout {

    String[] lines;
    String itemID;
    String subID;

    public SignLayout() {}

    public String[] getLines() {
        return lines;
    }

    public String getItemID() {
        return itemID;
    }

    public String getSubID() {
        return subID;
    }

    public SignLayout(String[] lines, String itemID, String subID) {
        this.lines = lines;
        this.itemID = itemID;
        this.subID = subID;
    }
}
