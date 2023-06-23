package eu.metacloudservice.codec.title;

import lombok.Getter;

public class Title {
    @Getter
    private String title;
    @Getter
    private String subtitle;
    @Getter
    private int fadeIn;
    @Getter
    private int stay;
    @Getter
    private int fadeOut;

    public Title(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        this.title = title;
        this.subtitle = subtitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }
}
