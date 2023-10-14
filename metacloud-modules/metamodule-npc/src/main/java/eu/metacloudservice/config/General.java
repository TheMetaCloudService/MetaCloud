package eu.metacloudservice.config;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;

public class General implements IConfigAdapter {

    private String modulename;
    private String moduleversion;
    private String moduleauthor;

    public General(String modulename, String moduleversion, String moduleauthor) {
        this.modulename = modulename;
        this.moduleversion = moduleversion;
        this.moduleauthor = moduleauthor;
    }

    public General() {
    }

    public String getModulename() {
        return modulename;
    }

    public String getModuleversion() {
        return moduleversion;
    }

    public String getModuleauthor() {
        return moduleauthor;
    }
}
