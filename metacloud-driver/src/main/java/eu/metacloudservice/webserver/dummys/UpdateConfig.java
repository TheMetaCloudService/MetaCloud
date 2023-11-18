/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.webserver.dummys;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;

public class UpdateConfig implements IConfigAdapter {

    String data;

    public UpdateConfig(String data) {
        this.data = data;
    }

    public UpdateConfig() {
    }

    public String getData() {
        return data;
    }
}
