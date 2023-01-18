package eu.themetacloudservice.terminal;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.storage.SetupStorage;
import eu.themetacloudservice.terminal.commands.CommandDriver;
import eu.themetacloudservice.terminal.completer.TerminalCompleter;
import eu.themetacloudservice.terminal.enums.Color;
import eu.themetacloudservice.terminal.enums.Type;
import eu.themetacloudservice.terminal.logging.SimpleLatestLog;
import eu.themetacloudservice.terminal.utils.TerminalStorage;
import eu.themetacloudservice.terminal.utils.TerminalStorageLine;
import lombok.SneakyThrows;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.Queue;

public class TerminalDriver {


    private boolean isInSetup;
    private final LinkedList<TerminalStorage> mainScreenStorage;
    private final Queue<TerminalStorageLine> inputs;
    private final Terminal terminal;
    private SetupStorage setupStorage;
    private final LineReader lineReader;
    private final SimpleLatestLog simpleLatestLog;
    private final CommandDriver commandDriver;

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


        Thread consoleReadingThread = new TerminalReader(this);
        consoleReadingThread.setUncaughtExceptionHandler((t, e) -> e.printStackTrace());
        consoleReadingThread.start();

    }

    public SetupStorage getSetupStorage() {
        return setupStorage;
    }


    public LineReader getLineReader() {
        return lineReader;
    }

    public void logSpeed(Type type, String detext, String entext){
      if (Driver.getInstance().getMessageStorage().language.equalsIgnoreCase("DE")){
          log(type, detext);
      }else {
          log(type, entext);
      }
    }

    public void joinSetup(){
        this.isInSetup = true;
        clearScreen();
        this.setupStorage = new SetupStorage();
        log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
        if (!new File("./service.json").exists() && !new File("./nodeservice.json").exists()){
            Driver.getInstance().getTerminalDriver().logSpeed(Type.SETUP, "Welche Sprache möchten Sie haben?", "What language would you like to have?");
            Driver.getInstance().getTerminalDriver().logSpeed(Type.SETUP, "Mögliche Antworten: §fDE, §fEN", "Possible answers: §fDE, §fEN");
        }else if (Driver.getInstance().getMessageStorage().setuptype.equalsIgnoreCase("GROUP")){
            Driver.getInstance().getTerminalDriver().logSpeed(Type.SETUP, "Wie soll die Gruppe heißen?", "What should the group be called?");
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

    public void log(Type type, String[] de, String[] en){
        if (Driver.getInstance().getMessageStorage().language.equalsIgnoreCase("DE")){

            this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
            for (int i = 0; i != en.length; i++){
                this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §b"+type.toString().toUpperCase().replace("INFORMATION", "§aINFORMATION")
                        .replace("ERROR", "§cERROR").replace("WARNING", "§eWARNING")+"§7: §r" + de[i] +Color.RESET.getAnsiCode()));
                this.lineReader.getTerminal().writer().flush();
            }
        }else {
            this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
            for (int i = 0; i != en.length; i++){
                this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §b"+type.toString().toUpperCase().replace("INFORMATION", "§aINFORMATION")
                        .replace("ERROR", "§cERROR").replace("WARNING", "§eWARNING")+"§7: §r" + en[i] +Color.RESET.getAnsiCode()));

                this.lineReader.getTerminal().writer().flush();
            }
        }
    }

    public void log(Type type, String... messages){

        this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
        for (int i = 0; i != messages.length ; i++) {
            this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §b"+type.toString().toUpperCase().replace("INFORMATION", "§aINFORMATION")
                    .replace("ERROR", "§cERROR").replace("WARNING", "§eWARNING")+"§7: §r" + messages[i] +Color.RESET.getAnsiCode()));
            simpleLatestLog.log(getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §b"+type.toString().toUpperCase()+"§7: §r" + messages[i] +Color.RESET.getAnsiCode()));
            simpleLatestLog.saveLogs();
            this.lineReader.getTerminal().writer().flush();

        }
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
                this.lineReader.isReading();

            }else if (type == Type.ERROR){

                this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
                this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §C"+type.toString().toUpperCase()+"§7: §r" + message + Color.RESET.getAnsiCode()));
                simpleLatestLog.log(getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §C"+type.toString().toUpperCase()+"§7: §r" + message +Color.RESET.getAnsiCode()));
                simpleLatestLog.saveLogs();
                this.lineReader.getTerminal().writer().flush();
                if (!this.lineReader.isReading()) return;
                this.lineReader.callWidget(LineReader.REDRAW_LINE);
                this.lineReader.callWidget(LineReader.REDISPLAY);
            }else if (type == Type.INFORMATION){

                this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
                this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §a"+type.toString().toUpperCase()+"§7: §r" + message + Color.RESET.getAnsiCode()));
                simpleLatestLog.log(getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §a"+type.toString().toUpperCase()+"§7: §r" + message +Color.RESET.getAnsiCode()));
                simpleLatestLog.saveLogs();
                this.lineReader.getTerminal().writer().flush();
                if (!this.lineReader.isReading()) return;
                this.lineReader.callWidget(LineReader.REDRAW_LINE);
                this.lineReader.callWidget(LineReader.REDISPLAY);
            }else if (type == Type.WARNING){

                this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
                this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §b"+type.toString().toUpperCase()+"§7: §r" + message + Color.RESET.getAnsiCode()));
                simpleLatestLog.log(getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §e"+type.toString().toUpperCase()+"§7: §r" + message +Color.RESET.getAnsiCode()));
                simpleLatestLog.saveLogs();
                this.lineReader.getTerminal().writer().flush();
                if (!this.lineReader.isReading()) return;
                this.lineReader.callWidget(LineReader.REDRAW_LINE);
                this.lineReader.callWidget(LineReader.REDISPLAY);
            }else{
                this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
                this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §b"+type.toString().toUpperCase()+"§7: §r" + message + Color.RESET.getAnsiCode()));
                simpleLatestLog.log(getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §b"+type.toString().toUpperCase()+"§7: §r" + message +Color.RESET.getAnsiCode()));
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

                if (!this.lineReader.isReading()) return;
                this.lineReader.callWidget(LineReader.REDRAW_LINE);
                this.lineReader.callWidget(LineReader.REDISPLAY);

            } else if (type == Type.SETUP_ERROR){

                this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
                this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §bINCORRECT§7: §r" + message +Color.RESET.getAnsiCode()));
                simpleLatestLog.log(getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §bINCORRECT§7: §r" + message +Color.RESET.getAnsiCode()));
                simpleLatestLog.saveLogs();
               this.lineReader.getTerminal().writer().flush();

                if (!this.lineReader.isReading()) return;
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

    public Queue<TerminalStorageLine> getInputs() {
        return this.inputs;
    }

    public CommandDriver getCommandDriver() {
        return commandDriver;
    }
}
