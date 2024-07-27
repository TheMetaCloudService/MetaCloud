package eu.metacloudservice.cloudplayer.codec.title;

import lombok.Getter;

public class Title {
    @Getter
    private final  String title;
    @Getter
    private final String subtitle;
    @Getter
    private final int fadeIn;
    @Getter
    private final int stay;
    @Getter
    private final int fadeOut;

    public Title(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        this.title = title;
        this.subtitle = subtitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }
}
