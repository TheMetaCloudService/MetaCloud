package eu.metacloudservice.manager.commands;

import eu.metacloudservice.Driver;
import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.terminal.commands.CommandAdapter;
import eu.metacloudservice.terminal.commands.CommandInfo;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.terminal.utils.TerminalStorageLine;
import eu.metacloudservice.webserver.PasteDriver;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

@CommandInfo(command = "paste", description = "command-paste-description", aliases = {"support", "haste"})
public class PasteCommand extends CommandAdapter {
    public void performCommand(CommandAdapter command, String[] args) {
        if (args.length == 0) {
            Driver.getInstance().getTerminalDriver().log(Type.COMMAND,
                    Driver.getInstance().getLanguageDriver().getLang().getMessage("command-past-help-1"),
                    Driver.getInstance().getLanguageDriver().getLang().getMessage("command-past-help-2")
            );
        } else if (args[0].equalsIgnoreCase("lastestlog")) {
            String filePath = "./local/logs/latest.log";
            if ((new File(filePath)).exists()) {
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(filePath));
                    try {
                        StringWriter stringWriter = new StringWriter();
                        String line;
                        while ((line = reader.readLine()) != null)
                            stringWriter.write(line + "\n");
                        String fileContent = stringWriter.toString();
                        String url = (new PasteDriver()).paste(fileContent);
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-paste-post-latest"));
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, " >>" + url);
                        reader.close();
                    } catch (Throwable throwable) {
                        try {
                            reader.close();
                        } catch (Throwable throwable1) {
                            throwable.addSuppressed(throwable1);
                        }
                        throw throwable;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-paste-not-found-latest"));

            }
        } else if (args[0].equalsIgnoreCase("service") && args.length == 2) {
            String service = args[1];
            String filePath = "./live/" + CloudManager.serviceDriver.getService(service).getEntry().getGroupName() + "/" + service + "/logs/latest.log";
            if ((new File(filePath)).exists()) {
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(filePath));
                    try {
                        StringWriter stringWriter = new StringWriter();
                        String line;
                        while ((line = reader.readLine()) != null)
                            stringWriter.write(line + "\n");
                        String fileContent = stringWriter.toString();
                        String url = (new PasteDriver()).paste(fileContent);
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-paste-post-service")
                                .replace("%service%", service));
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, " >>" + url, " >>" + url);
                        reader.close();
                    } catch (Throwable throwable) {
                        try {
                            reader.close();
                        } catch (Throwable throwable1) {
                            throwable.addSuppressed(throwable1);
                        }
                        throw throwable;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-paste-not-found-service")
                        .replace("%service%", service));
            }
        } else {
            Driver.getInstance().getTerminalDriver().log(Type.COMMAND,
                    Driver.getInstance().getLanguageDriver().getLang().getMessage("command-past-help-1"),
                    Driver.getInstance().getLanguageDriver().getLang().getMessage("command-past-help-2")
            );
        }
    }

    public ArrayList<String> tabComplete(TerminalStorageLine consoleInput, String[] args) {
        ArrayList<String> returns = new ArrayList<>();
        if (args.length == 0) {
            returns.add("lastestlog");
            returns.add("service");
        } else if (args.length == 1) {
            CloudManager.serviceDriver.getServices().forEach(taskedService -> returns.add(taskedService.getEntry().getServiceName()));
        }
        return returns;
    }
}
