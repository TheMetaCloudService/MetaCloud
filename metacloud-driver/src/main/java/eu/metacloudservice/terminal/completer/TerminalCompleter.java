package eu.metacloudservice.terminal.completer;

import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.metacloudservice.configuration.dummys.managerconfig.ManagerConfigNodes;
import eu.metacloudservice.storage.PacketLoader;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public final class TerminalCompleter  implements Completer {

    private boolean exists;

    @Override
    public void complete(LineReader lineReader, ParsedLine parsedLine, List<Candidate> list) {
        final var input = parsedLine.line();
        List<String> suggestions = null;


        if (Driver.getInstance().getMessageStorage().openServiceScreen){
            final var result = new LinkedList<String>();
            result.add("leave");
            suggestions = result;
            suggestions.stream().map(Candidate::new).forEach(list::add);
        }else if (Driver.getInstance().getTerminalDriver().isInSetup()){
            final var result = new LinkedList<String>();

            if (Driver.getInstance().getMessageStorage().setupType.equalsIgnoreCase("GROUP")){

                if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 1){
                    result.add("LOBBY");
                    result.add("PROXY");
                    result.add("GAME");
                }
                if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 2){
                    result.add("256");
                    result.add("512");
                    result.add("1024");
                    result.add("2048");
                    result.add("4096");
                    result.add("8192");
                }
                if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 3){
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
            }else if (Driver.getInstance().getMessageStorage().setupType.equalsIgnoreCase("MAINSETUP")){
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
                            result.add( ip);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

                if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 4){
                    if ( Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString().equalsIgnoreCase("MANAGER")){
                        if (!input.contains(" ")){
                            result.addAll(new PacketLoader().availableBungeecords());

                        }
                    }
                }
                if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 5){
                    if ( Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString().equalsIgnoreCase("MANAGER")){
                        if (!input.contains(" ")){
                            if ( Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("chosespigot").toString().equalsIgnoreCase("none")){
                                List<String> spigots = new PacketLoader().availableSpigots();
                                List<String> mainSpigots = new ArrayList<>();
                                spigots.forEach(s -> {
                                    if (!mainSpigots.contains(s.split("-")[0])) {
                                        mainSpigots.add(s.split("-")[0]);
                                    }
                                });

                                result.addAll(mainSpigots);
                            }else {

                                List<String> spigots = new PacketLoader().availableSpigots().stream().filter(s -> s.startsWith( Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("chosespigot").toString())).collect(Collectors.toList());
                                result.addAll(spigots);
                                result.add("BACKTOMAIN");
                             }
                        }
                    }
                }
                if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 6){
                    if ( Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString().equalsIgnoreCase("MANAGER")){
                        if (!input.contains(" ")){
                            result.add("Y");
                            result.add("N");
                        }
                    }else
                    if ( Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString().equalsIgnoreCase("NODE")){
                        if (!input.contains(" ")){
                            result.addAll(new PacketLoader().availableBungeecords());
                        }
                    }
                }

                if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 8){
                    if ( Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString().equalsIgnoreCase("MANAGER")){
                        if (!input.contains(" ")){
                            result.add("Y");
                            result.add("N");
                        }
                    }
                }

                if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 7){
                    if ( Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString().equalsIgnoreCase("MANAGER")){
                        if (!input.contains(" ")){
                            result.add("Y");
                            result.add("N");
                        }
                    }else
                    if ( Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString().equalsIgnoreCase("NODE")){
                        if (!input.contains(" ")){
                            if ( Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("chosespigot").toString().equalsIgnoreCase("none")){
                                List<String> spigots = new PacketLoader().availableSpigots();
                                List<String> mainSpigots = new ArrayList<>();
                                spigots.forEach(s -> {
                                    if (!mainSpigots.contains(s.split("-")[0])) {
                                        mainSpigots.add(s.split("-")[0]);
                                    }
                                });

                                result.addAll(mainSpigots);
                            }else {
                                List<String> spigots = new PacketLoader().availableSpigots().stream().filter(s -> s.startsWith( Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("chosespigot").toString())).collect(Collectors.toList());
                                result.addAll(spigots);
                                result.add("BACKTOMAIN");
                            }
                        }
                    }
                }

                if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 5){
                    if ( Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString().equalsIgnoreCase("NODE")){
                        if (!input.contains(" ")){
                            try {
                                String ip = new BufferedReader(new InputStreamReader(new URL("https://checkip.amazonaws.com").openConnection().getInputStream())).readLine();
                                result.add(ip);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
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
        exists = false;

        if (line.contains(" ")){
            return true;
        }

        Driver.getInstance().getTerminalDriver().getCommandDriver().getCommands().forEach(command -> {
            commandsAndAliases.add(command.getCommand());

            commandsAndAliases.addAll(command.getAliases());
        });

        commandsAndAliases.forEach(command -> {
            if (command.startsWith(line)){
                exists = true;
            }
        });


        return exists;
    }
}