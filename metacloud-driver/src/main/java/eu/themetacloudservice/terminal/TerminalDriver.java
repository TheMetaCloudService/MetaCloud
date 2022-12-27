package eu.themetacloudservice.terminal;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.storage.SetupStorage;
import eu.themetacloudservice.terminal.commands.CommandDriver;
import eu.themetacloudservice.terminal.completer.TerminalCompleter;
import eu.themetacloudservice.terminal.enums.Color;
import eu.themetacloudservice.terminal.enums.Type;
import eu.themetacloudservice.terminal.logging.SimpleLatestLog;
import eu.themetacloudservice.terminal.screens.GroupSetup;
import eu.themetacloudservice.terminal.screens.MainSetup;
import eu.themetacloudservice.terminal.utils.TerminalStorage;
import eu.themetacloudservice.terminal.utils.TerminalStorageLine;
import lombok.SneakyThrows;
import lombok.var;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

public class TerminalDriver {


    private boolean isInSetup;
    private LinkedList<TerminalStorage> mainScreenStorage;
    private Queue<TerminalStorageLine> inputs;
    private final Terminal terminal;
    private SetupStorage setupStorage;
    private final LineReader lineReader;
    private Thread consoleThread;
    private SimpleLatestLog simpleLatestLog;
    private CommandDriver commandDriver;

    public boolean isInSetup(){
        return isInSetup;
    }

    @SneakyThrows
    public TerminalDriver() {

        /*
        * @FUNCTION: Terminal and reader with tapping in Jline3
        * @Coder: RauchigesEtwas (Robin B.)
        * */
        this.setupStorage = new SetupStorage();
        this.commandDriver = new CommandDriver();
        this.mainScreenStorage = new LinkedList<>();
        this.inputs = new LinkedList<>();
        this.simpleLatestLog = new SimpleLatestLog();


        this.isInSetup = false;
        this.terminal = TerminalBuilder.builder()
                .system(true)
                .streams(System.in, System.out)
                .encoding(StandardCharsets.UTF_8)
                .dumb(true)
                .build();

        this.lineReader = LineReaderBuilder.builder()
                .terminal(this.terminal)
                .completer(new TerminalCompleter())
                .option(LineReader.Option.AUTO_REMOVE_SLASH, false)
                .option(LineReader.Option.INSERT_TAB, false)
                .option(LineReader.Option.DISABLE_EVENT_EXPANSION, true)
                .build();

        this.consoleThread = new Thread(() -> {

            while (!this.consoleThread.isInterrupted()) {

                try {
                    final var line = this.lineReader.readLine(getColoredString("§bMetaCloud§f@"+System.getProperty("user.name") +" §7=> "));
                    if (line != null && !line.isEmpty()) {
                        final var input = this.getInputs().poll();

                        if(this.isInSetup){
                            if (line.equalsIgnoreCase("leave")){
                                leaveSetup();
                            }else if (!new File("./service.json").exists()){
                                new MainSetup(line);
                            }else {
                                new GroupSetup(line);
                            }
                        } else if (input != null) {
                            input.inputs().accept(line);
                        } else {

                        }
                    }
                }catch (Exception e){ }
            }
        });
        this.consoleThread.setPriority(Thread.MAX_PRIORITY);
        this.consoleThread.setName("METACLOUD_CONSOLE");
        this.consoleThread.start();
    }

    public SetupStorage getSetupStorage() {
        return setupStorage;
    }


    public void joinSetup(){
        this.isInSetup = true;
        clearScreen();
        this.setupStorage = new SetupStorage();

        log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
        if (Driver.getInstance().getMessageStorage().language.equalsIgnoreCase("DE")){
            if (!new File("./service.json").exists()){
                Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Welche Sprache möchten Sie haben?");
                Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Mögliche Antworten: §bDE, §bEN");
            }
        }else {

                Driver.getInstance().getTerminalDriver().log(Type.SETUP, "What language would you like to have?");
                Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Possible answers: §bDE, §bEN");
            }
        }

    public void leaveSetup(){
        clearScreen();
        this.isInSetup = false;

        this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
        for (int i = 0; i != this.mainScreenStorage.size(); i++) {
                TerminalStorage storage = this.mainScreenStorage.get(i);

            if (storage.getType() == Type.EMPTY){
                String msg = storage.getMessage();
                this.terminal.writer().println("\r" + getColoredString(msg + Color.RESET.getAnsiCode()));
                simpleLatestLog.log(getColoredString(msg + Color.RESET.getAnsiCode()));
                simpleLatestLog.saveLogs();
            }else {
                this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §b"+storage.getType().toString().toUpperCase()+"§7: §r" + storage.getMessage() + Color.RESET.getAnsiCode()));
                simpleLatestLog.log(getColoredString("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §b"+storage.getType().toString().toUpperCase()+"§7: §r" + storage.getMessage() + Color.RESET.getAnsiCode())));
                simpleLatestLog.saveLogs();
            }
        }
        this.lineReader.getTerminal().writer().flush();
        if (!this.lineReader.isReading()) return;
        this.lineReader.callWidget(LineReader.REDRAW_LINE);
        this.lineReader.callWidget(LineReader.REDISPLAY);

    }

    public void clearScreen(){
        if (!this.isInSetup){
            this.mainScreenStorage.clear();
        }
        this.terminal.puts(InfoCmp.Capability.clear_screen);
        this.terminal.flush();
        this.redraw();
    }

    public void log(Type type, String... messages){

        this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
        for (int i = 0; i != messages.length ; i++) {
            this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §b"+type.toString().toUpperCase()+"§7: §r" + messages[i] +Color.RESET.getAnsiCode()));
            simpleLatestLog.log(getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §v"+type.toString().toUpperCase()+"§7: §r" + messages[i] +Color.RESET.getAnsiCode()));
            simpleLatestLog.saveLogs();
        }
        this.lineReader.getTerminal().writer().flush();
        this.lineReader.callWidget(LineReader.REDRAW_LINE);
        this.lineReader.callWidget(LineReader.REDISPLAY);
    }

    public void log(Type type, String message){
        /*
         * @FUNCTION: Log output in console
         * @ARGUMENTS: Type type, String raw
         * @Coder: RauchigesEtwas (Robin B.)
         * */

        if (!this.isInSetup){

            this.mainScreenStorage.add(new TerminalStorage(type, message));

            if (type == Type.EMPTY){
                this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
                this.terminal.writer().println("\r" + getColoredString(message + Color.RESET.getAnsiCode()));
                simpleLatestLog.log(getColoredString("\r" + getColoredString(message + Color.RESET.getAnsiCode())));
                simpleLatestLog.saveLogs();
                this.lineReader.getTerminal().writer().flush();
                if (!this.lineReader.isReading()) return;
                this.lineReader.callWidget(LineReader.REDRAW_LINE);
                this.lineReader.callWidget(LineReader.REDISPLAY);

            }else {

                this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
                this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §b"+type.toString().toUpperCase()+"§7: §r" + message + Color.RESET.getAnsiCode()));
                simpleLatestLog.log(getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §v"+type.toString().toUpperCase()+"§7: §r" + message +Color.RESET.getAnsiCode()));
                simpleLatestLog.saveLogs();
                this.lineReader.getTerminal().writer().flush();
                if (!this.lineReader.isReading()) return;
                this.lineReader.callWidget(LineReader.REDRAW_LINE);
                this.lineReader.callWidget(LineReader.REDISPLAY);
            }

        }else {
            if (type == Type.SETUP){
                this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
                this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §bSETUP§7: §r" + message +Color.RESET.getAnsiCode()));
                simpleLatestLog.log(getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §bSETUP§7: §r" + message +Color.RESET.getAnsiCode()));
                simpleLatestLog.saveLogs();
                this.lineReader.getTerminal().writer().flush();
                this.lineReader.callWidget(LineReader.REDRAW_LINE);
                this.lineReader.callWidget(LineReader.REDISPLAY);

            } else if (type == Type.SETUP_ERROR){

                this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
                this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §bINCORRECT§7: §r" + message +Color.RESET.getAnsiCode()));
                simpleLatestLog.log(getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §bINCORRECT§7: §r" + message +Color.RESET.getAnsiCode()));
                simpleLatestLog.saveLogs();
               this.lineReader.getTerminal().writer().flush();
                this.lineReader.callWidget(LineReader.REDRAW_LINE);
                this.lineReader.callWidget(LineReader.REDISPLAY);
            }else if (type == Type.EMPTY){

                this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
                this.terminal.writer().println("\r" + getColoredString(message + Color.RESET.getAnsiCode()));
                simpleLatestLog.log(getColoredString("\r" + getColoredString(message + Color.RESET.getAnsiCode())));
                simpleLatestLog.saveLogs();
                this.lineReader.getTerminal().writer().flush();
                if (!this.lineReader.isReading()) return;
                this.lineReader.callWidget(LineReader.REDRAW_LINE);
                this.lineReader.callWidget(LineReader.REDISPLAY);

            } else {
                this.mainScreenStorage.add(new TerminalStorage(type, message));
            }

        }
    }


    /**
     * Color string string.
     *
     * @param text the text
     * @return the string
     */
    public String getColoredString(String text) {

        for (Color consoleColour : Color.values()) {
            text = text.replace('§' + "" + consoleColour.getIndex(), consoleColour.getAnsiCode());
        }

        return text;
    }

    public void redraw() {
        if (this.lineReader.isReading()) {
            this.lineReader.callWidget(LineReader.REDRAW_LINE);
            this.lineReader.callWidget(LineReader.REDISPLAY);
        }
    }

    public void addInput(final Consumer<String> input, final List<String> tabCompletions) {
        this.inputs.add(new TerminalStorageLine(input, tabCompletions));
    }

    public Queue<TerminalStorageLine> getInputs() {
        return this.inputs;
    }

    public CommandDriver getCommandDriver() {
        return commandDriver;
    }
}
