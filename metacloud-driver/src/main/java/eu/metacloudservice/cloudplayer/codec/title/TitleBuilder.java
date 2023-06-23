package eu.metacloudservice.cloudplayer.codec.title;

public class TitleBuilder {
    private String title;
    private String subtitle;
    private int fadeIn;
    private int stay;
    private int fadeOut;

    public TitleBuilder() {
        // Set default values if needed
        this.title = "";
        this.subtitle = "";
        this.fadeIn = 0;
        this.stay = 0;
        this.fadeOut = 0;
    }

    public TitleBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public TitleBuilder withSubtitle(String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public TitleBuilder withFadeIn(int fadeIn) {
        this.fadeIn = fadeIn;
        return this;
    }

    public TitleBuilder withStay(int stay) {
        this.stay = stay;
        return this;
    }

    public TitleBuilder withFadeOut(int fadeOut) {
        this.fadeOut = fadeOut;
        return this;
    }

    public Title build() {
        return new Title(title, subtitle, fadeIn, stay, fadeOut);
    }
}

