package eu.metacloudservice.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
public class Motd {

    private String protocol;
    private String firstline;
    private String secondline;
    private ArrayList<String> playerinfos;
    private String icon;

    public Motd() {}
}
