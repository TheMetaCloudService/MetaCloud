package eu.metacloudservice.module.config;

import lombok.Getter;

public class ModuleConfiguration {

    @Getter
    private String name, author, main, copy, version;

    public ModuleConfiguration(String name, String author, String main, String copy, String version) {
        this.name = name;
        this.author = author;
        this.main = main;
        this.copy = copy;
        this.version = version;
    }
}
