package eu.themetacloudservice.terminal.completer;

import eu.themetacloudservice.Driver;
import lombok.var;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
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

            if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 4){
                if ( Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString().equalsIgnoreCase("MANAGER")){
                    if (!input.contains(" ")){
                        result.add("VELOCITY");
                        result.add("WATERFALL");
                    }
                }
            }


            suggestions = result;
            suggestions.stream().map(Candidate::new).forEach(list::add);
        }else {
            if (input.isEmpty() || input.equalsIgnoreCase("")){
                final var result = new LinkedList<String>();
                Driver.getInstance().getTerminalDriver().getCommandDriver().getCommands().forEach(command -> {
                    result.add(command.getCommand());

                    command.getAliases().forEach(s -> {
                        result.add(s);
                    });
                });
                suggestions = result;
                suggestions.stream().map(Candidate::new).forEach(list::add);
            }else if (!canBeFinde(input)){
                final var result = new LinkedList<String>();
                Driver.getInstance().getTerminalDriver().getCommandDriver().getCommands().forEach(command -> {
                    result.add(command.getCommand());

                    command.getAliases().forEach(s -> {
                        result.add(s);
                    });
                });
                suggestions = result;
                suggestions.stream().map(Candidate::new).forEach(list::add);
            }else {
                var arguments = input.split(" ");
                final var consoleInput = Driver.getInstance().getTerminalDriver().getInputs().peek();
                if (input.isEmpty() || input.indexOf(' ') == -1) {
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

                                command.getAliases().forEach(s -> {
                                    result.add(s);
                                });
                            });


                        }

                        suggestions = result;

                    }else{
                        suggestions = consoleInput.tabCompletes();
                    }
                }else{
                    if (consoleInput != null) return;
                    final var command =  Driver.getInstance().getTerminalDriver().getCommandDriver().getCommand(arguments[0]);
                    final var result = new LinkedList<String>();

                    if(command == null){
                        Driver.getInstance().getTerminalDriver().getCommandDriver().getCommands().forEach(command1 -> {
                            result.add(command1.getCommand());

                            command1.getAliases().forEach(s -> {
                                result.add(s);
                            });
                        });
                    }else{
                        if (command.tabComplete(consoleInput, arguments) != null){
                            command.tabComplete(consoleInput, Driver.getInstance().getMessageStorage().dropFirstString(arguments)).forEach(s -> {
                                result.add(s);
                            });
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

            command.getAliases().forEach(s -> {
                commandsAndAliases.add(s);
            });
        });

        commandsAndAliases.forEach(command -> {
            if (command.startsWith(line) || command.equals(line)){
                exeists = true;
            }
        });


        return exeists;
    }
}
