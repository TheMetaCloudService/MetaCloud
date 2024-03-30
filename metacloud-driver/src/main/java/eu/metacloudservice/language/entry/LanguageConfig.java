package eu.metacloudservice.language.entry;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;


@Getter
@AllArgsConstructor
public class LanguageConfig implements IConfigAdapter {

    private HashMap<String, String> messages;

    public LanguageConfig() {}
}
