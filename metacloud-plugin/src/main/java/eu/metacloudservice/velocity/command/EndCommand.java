package eu.metacloudservice.velocity.command;

import com.velocitypowered.api.command.SimpleCommand;
import eu.metacloudservice.CloudAPI;

public class EndCommand implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        CloudAPI.getInstance().getThisService().shutdown();
    }
}
