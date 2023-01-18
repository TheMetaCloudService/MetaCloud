package eu.themetacloudservice.terminal.completer;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.themetacloudservice.configuration.dummys.managerconfig.ManagerConfigNodes;
import lombok.var;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TerminalCompleter  implements Completer {

    private boolean exeists;

    @Override
    public void complete(LineReader lineReader, ParsedLine parsedLine, List<Candidate> list) {
        final var input = parsedLine.line();
        List<String> suggestions = null;

        if (Driver.getInstance().getTerminalDriver().isInSetup()){
            final var result = new LinkedList<String>();

            if (Driver.getInstance().getMessageStorage().setuptype.equalsIgnoreCase("GROUP")){

                if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 1){
                    result.add("LOBBY");
                    result.add("PROXY");
                    result.add("GAME");
                }if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 3){
                    result.add("y");
                    result.add("n");
                }if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 10){
                    ArrayList<String> rawtemplates = Driver.getInstance().getTemplateDriver().get();
                    result.addAll(rawtemplates);
                    result.add("CREATE");
                }if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 11){
                    ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
                    ArrayList<ManagerConfigNodes> configNodes = config.getNodes();
                    configNodes.forEach(managerConfigNodes -> result.add(managerConfigNodes.getName()));
                }


                suggestions = result;
                suggestions.stream().map(Candidate::new).forEach(list::add);
            }else if (Driver.getInstance().getMessageStorage().setuptype.equalsIgnoreCase("MAINSETUP")){
                if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 0){
                    if (!input.contains(" ")){
                        result.add("DE");
                        result.add("EN");
                    }
                }
                if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 1){
                    if (!input.contains(" ")){
                        result.add("MANAGER");
                        result.add("NODE");
                    }
                }
                if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 2){
                    if (!input.contains(" ")){
                        try {
                            String ip = new BufferedReader(new InputStreamReader(new URL("https://checkip.amazonaws.com").openConnection().getInputStream())).readLine();
                            result.add("" + ip);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        result.add("127.0.0.1");
                    }

                }

                if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 4){
                    if ( Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString().equalsIgnoreCase("MANAGER")){
                        if (!input.contains(" ")){
                            result.add("WATERFALL");
                            result.add("BUNGEECORD");
                        }
                    }
                }
                if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 5){
                    if ( Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString().equalsIgnoreCase("MANAGER")){
                        if (!input.contains(" ")){
                            result.add("PAPER-1.19.3");
                            result.add("PAPER-1.19.2");
                            result.add("PAPER-1.18.2");
                            result.add("PAPER-1.17.1");
                            result.add("PAPER-1.16.5");
                            result.add("SPIGOT-1.19.3");
                            result.add("SPIGOT-1.19.2");
                            result.add("SPIGOT-1.18.2");
                            result.add("SPIGOT-1.17.1");
                            result.add("SPIGOT-1.16.5");

                        }
                    }
                }
                if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 6){
                    if ( Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString().equalsIgnoreCase("NODE")){
                        if (!input.contains(" ")){
                            result.add("WATERFALL");
                            result.add("BUNGEECORD");
                        }
                    }
                }
                if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 7){
                    if ( Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString().equalsIgnoreCase("NODE")){
                        if (!input.contains(" ")){
                            result.add("PAPER-1.19.3");
                            result.add("PAPER-1.19.2");
                            result.add("PAPER-1.18.2");
                            result.add("PAPER-1.17.1");
                            result.add("PAPER-1.16.5");
                            result.add("SPIGOT-1.19.3");
                            result.add("SPIGOT-1.19.2");
                            result.add("SPIGOT-1.18.2");
                            result.add("SPIGOT-1.17.1");
                            result.add("SPIGOT-1.16.5");
                        }
                    }
                }

                if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 5){
                    if ( Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString().equalsIgnoreCase("NODE")){
                        if (!input.contains(" ")){
                            try {
                                String ip = new BufferedReader(new InputStreamReader(new URL("https://checkip.amazonaws.com").openConnection().getInputStream())).readLine();
                                result.add("" + ip);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            result.add("127.0.0.1");
                        }
                    }

                }



                suggestions = result;
                suggestions.stream().map(Candidate::new).forEach(list::add);
            }
        }else {
            if (input.isEmpty()){
                final var result = new LinkedList<String>();
                Driver.getInstance().getTerminalDriver().getCommandDriver().getCommands().forEach(command -> {
                    result.add(command.getCommand());

                    result.addAll(command.getAliases());
                });
                suggestions = result;
                suggestions.stream().map(Candidate::new).forEach(list::add);
            }else if (!canBeFinde(input)){
                final var result = new LinkedList<String>();
                Driver.getInstance().getTerminalDriver().getCommandDriver().getCommands().forEach(command -> {
                    result.add(command.getCommand());

                    result.addAll(command.getAliases());
                });
                suggestions = result;
                suggestions.stream().map(Candidate::new).forEach(list::add);
            }else {
                var arguments = input.split(" ");
                final var consoleInput = Driver.getInstance().getTerminalDriver().getInputs().peek();
                if (input.indexOf(' ') == -1) {
                    if (consoleInput == null) {
                        final var result = new LinkedList<String>();
                        final var toTest = arguments[arguments.length - 1];

                        Driver.getInstance().getTerminalDriver().getCommandDriver().getCommands().forEach(command -> {
                            if (command.getCommand() != null && (toTest.trim().isEmpty() || command.getCommand().toLowerCase().contains(toTest.toLowerCase()))) {

                                result.add(command.getCommand());
                            }

                            command.getAliases().forEach(s -> {
                                if (s.toLowerCase().contains(toTest.toLowerCase())){
                                    result.add(s);
                                }
                            });
                        });

                        if (result.isEmpty() && !Driver.getInstance().getTerminalDriver().getCommandDriver().getCommands().isEmpty()) {
                            Driver.getInstance().getTerminalDriver().getCommandDriver().getCommands().forEach(command -> {
                                result.add(command.getCommand());

                                result.addAll(command.getAliases());
                            });


                        }

                        suggestions = result;

                    }else{
                        suggestions = consoleInput.tabCompletes();
                    }
                }else{
                    if (consoleInput != null) return;
                    final var command = Driver.getInstance().getTerminalDriver().getCommandDriver().getCommand(arguments[0]);
                    final var result = new LinkedList<String>();

                    if(command == null){
                        Driver.getInstance().getTerminalDriver().getCommandDriver().getCommands().forEach(command1 -> {
                            result.add(command1.getCommand());

                            result.addAll(command1.getAliases());
                        });
                    }else{
                        if (command.tabComplete(consoleInput, arguments) != null){
                            result.addAll(command.tabComplete(consoleInput, Driver.getInstance().getMessageStorage().dropFirstString(arguments)));
                            suggestions = result;
                        }
                    }
                    if (suggestions == null || suggestions.isEmpty()) return;


                }
                suggestions.stream().map(Candidate::new).forEach(list::add);

            }
        }

    }



    private boolean canBeFinde(String line){
        ArrayList<String> commandsAndAliases = new ArrayList<>();
        exeists = false;

        if (line.contains(" ")){
            return true;
        }

        Driver.getInstance().getTerminalDriver().getCommandDriver().getCommands().forEach(command -> {
            commandsAndAliases.add(command.getCommand());

            commandsAndAliases.addAll(command.getAliases());
        });

        commandsAndAliases.forEach(command -> {
            if (command.startsWith(line)){
                exeists = true;
            }
        });


        return exeists;
    }
}
