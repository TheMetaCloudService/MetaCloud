package eu.metacloudservice.terminal;

import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.storage.SetupStorage;
import eu.metacloudservice.terminal.commands.CommandDriver;
import eu.metacloudservice.terminal.completer.TerminalCompleter;
import eu.metacloudservice.terminal.enums.Color;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.terminal.logging.SimpleLatestLog;
import eu.metacloudservice.terminal.streams.LoggerOutputStream;
import eu.metacloudservice.terminal.utils.TerminalStorage;
import eu.metacloudservice.terminal.utils.TerminalStorageLine;
import eu.metacloudservice.webserver.entry.RouteEntry;
import lombok.SneakyThrows;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;
import java.io.File;
import java.io.PrintStream;
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


        System.setOut(new PrintStream(new LoggerOutputStream(Type.INFO), true));
        System.setErr(new PrintStream(new LoggerOutputStream(Type.ERROR), true));
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
                .name("META-CONSOLE")
                .build();

        this.lineReader = LineReaderBuilder.builder()
                .terminal(this.terminal)
                .completer(new TerminalCompleter())
                .option(LineReader.Option.DISABLE_EVENT_EXPANSION, true)
                .option(LineReader.Option.AUTO_REMOVE_SLASH, false)
                .option(LineReader.Option.INSERT_TAB, false)
                .appName("META-READER")
                .build();


        Thread consoleReadingThread = new TerminalReader(this);
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

        if (Driver.getInstance().getMessageStorage().openServiceScreen){
            Driver.getInstance().getMessageStorage().openServiceScreen = false;
        }
        this.isInSetup = false;
        if (Driver.getInstance().getMessageStorage().setuptype.equalsIgnoreCase("GROUP")){
            Driver.getInstance().getGroupDriver().getAll().forEach(group -> {
                if (Driver.getInstance().getWebServer().getRoute("/"+group.getGroup()) == null){
                    Driver.getInstance().getWebServer().addRoute(new RouteEntry("/" + group.getGroup(), new ConfigDriver().convert(group)));
                }else {
                    Driver.getInstance().getWebServer().updateRoute("/" + group.getGroup(), new ConfigDriver().convert(group));
                }
            });
        }
        this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
        for (int i = 0; i != this.mainScreenStorage.size(); i++) {
                TerminalStorage storage = this.mainScreenStorage.get(i);

            if (storage.getType() == Type.EMPTY){
                String msg = storage.getMessage();
                this.terminal.writer().println("\r" + getColoredString(msg + Color.RESET.getAnsiCode()));
                simpleLatestLog.log(getClearSting(msg));
                simpleLatestLog.saveLogs();
            }else {
                this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §b"+storage.getType().toString().toUpperCase()+"§7: §r" + storage.getMessage() + Color.RESET.getAnsiCode()));
                simpleLatestLog.log(getClearSting("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §b"+storage.getType().toString().toUpperCase()+"§7: §r" + storage.getMessage())));
                simpleLatestLog.saveLogs();
            }
        }
        this.lineReader.getTerminal().flush();
        if (!this.lineReader.isReading()) return;
        this.lineReader.callWidget(LineReader.REDRAW_LINE);
        this.lineReader.callWidget(LineReader.REDISPLAY);

    }

    public void clearScreen(){
        if (!this.isInSetup && !Driver.getInstance().getMessageStorage().openServiceScreen){
            this.mainScreenStorage.clear();
        }
        this.terminal.puts(InfoCmp.Capability.clear_screen);
        this.terminal.flush();
        this.redraw();
    }

    public void log(Type type, String[] de, String[] en){
        if (Driver.getInstance().getMessageStorage().language.equalsIgnoreCase("DE")){

            this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
            for (int i = 0; i != de.length; i++){
                this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §b"+type.toString().toUpperCase().replace("INFORMATION", "§bINFORMATION")
                        .replace("ERROR", "§cERROR").replace("WARNING", "§eWARN")+"§7: §r" + de[i] +Color.RESET.getAnsiCode()));
                this.lineReader.getTerminal().flush();
            }
        }else {
            this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
            for (int i = 0; i != en.length; i++){
                this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §b"+type.toString().toUpperCase().replace("INFORMATION", "§bINFORMATION")
                        .replace("ERROR", "§cERROR").replace("WARNING", "§eWARN")+"§7: §r" + en[i] +Color.RESET.getAnsiCode()));

                this.lineReader.getTerminal().flush();
            }
        }
    }

    public void log(String service, String[] de, String[] en){
        if (Driver.getInstance().getMessageStorage().language.equalsIgnoreCase("DE")){

            this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
            for (int i = 0; i != de.length; i++){
                this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §b"+service
                      +"§7: §r" + de[i] +Color.RESET.getAnsiCode()));
                this.lineReader.getTerminal().flush();
                simpleLatestLog.log(getClearSting("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §c"+service+"§7: §r" + de[i]));
                simpleLatestLog.saveLogs();
                this.lineReader.getTerminal().flush();
                if (!this.lineReader.isReading()) return;
                this.lineReader.callWidget(LineReader.REDRAW_LINE);
                this.lineReader.callWidget(LineReader.REDISPLAY);
            }
        }else {
            this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
            for (int i = 0; i != en.length; i++){
                this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §b"+
                        service +"§7: §r" + en[i] +Color.RESET.getAnsiCode()));
                this.lineReader.getTerminal().flush();
                simpleLatestLog.log("["  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "] "+service+": " + getClearSting(en[i]) );
                simpleLatestLog.saveLogs();
                this.lineReader.getTerminal().flush();
                if (!this.lineReader.isReading()) return;
                this.lineReader.callWidget(LineReader.REDRAW_LINE);
                this.lineReader.callWidget(LineReader.REDISPLAY);
            }
        }
    }


    public void log(String service, String messages){

        this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
        this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §b"+
                service +"§7: §r" + messages +Color.RESET.getAnsiCode()));
        this.lineReader.getTerminal().flush();
        simpleLatestLog.log(getColoredString(getClearSting("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §c"+service+"§7: §r" + messages )));
        simpleLatestLog.saveLogs();
        this.lineReader.getTerminal().flush();
        if (!this.lineReader.isReading()) return;
        this.lineReader.callWidget(LineReader.REDRAW_LINE);
        this.lineReader.callWidget(LineReader.REDISPLAY);
    }

    public void log(Type type, String... messages){

        this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
        for (int i = 0; i != messages.length ; i++) {
            this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §b"+type.toString().toUpperCase().replace("INFORMATION", "§bINFORMATION")
                    .replace("ERROR", "§cERROR").replace("WARNING", "§eWARN")+"§7: §r" + messages[i] +Color.RESET.getAnsiCode()));
            simpleLatestLog.log(getClearSting("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §b"+type.toString().toUpperCase()+"§7: §r" + messages[i]));
            simpleLatestLog.saveLogs();
            this.lineReader.getTerminal().flush();

        }
    }

    public void log(Type type, String message){
        /*
         * @FUNCTION: Log output in console
         * @ARGUMENTS: Type type, String raw
         * @Coder: RauchigesEtwas (Robin B.)
         * */

        if (Driver.getInstance().getMessageStorage().openServiceScreen){

            this.mainScreenStorage.add(new TerminalStorage(type, message));

        }else if (!this.isInSetup){

            this.mainScreenStorage.add(new TerminalStorage(type, message));

            if (type == Type.EMPTY){
                this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
                this.terminal.writer().println("\r" + getColoredString(message + Color.RESET.getAnsiCode()));
                simpleLatestLog.log(getClearSting("\r" + getColoredString(message + Color.RESET.getAnsiCode())));
                simpleLatestLog.saveLogs();
                this.lineReader.getTerminal().flush();
                if (!this.lineReader.isReading()) return;
                this.lineReader.callWidget(LineReader.REDRAW_LINE);
                this.lineReader.callWidget(LineReader.REDISPLAY);

            }else if (type == Type.ERROR){

                this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
                this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §c"+type.toString().toUpperCase()+"§7: §r" + message + Color.RESET.getAnsiCode()));
                simpleLatestLog.log(getClearSting("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §c"+type.toString().toUpperCase()+"§7: §r" + message +Color.RESET.getAnsiCode()));
                simpleLatestLog.saveLogs();
                this.lineReader.getTerminal().flush();
                if (!this.lineReader.isReading()) return;
                this.lineReader.callWidget(LineReader.REDRAW_LINE);
                this.lineReader.callWidget(LineReader.REDISPLAY);
            }else if (type == Type.INFO){

                this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
                this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §b"+type.toString().toUpperCase()+"§7: §r" + message + Color.RESET.getAnsiCode()));
                simpleLatestLog.log(getClearSting("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §b"+type.toString().toUpperCase()+"§7: §r" + message +Color.RESET.getAnsiCode()));
                simpleLatestLog.saveLogs();
                this.lineReader.getTerminal().flush();
                if (!this.lineReader.isReading()) return;
                this.lineReader.callWidget(LineReader.REDRAW_LINE);
                this.lineReader.callWidget(LineReader.REDISPLAY);
            }else if (type == Type.WARN){

                this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
                this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §b"+type.toString().toUpperCase()+"§7: §r" + message + Color.RESET.getAnsiCode()));
                simpleLatestLog.log(getClearSting("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §e"+type.toString().toUpperCase()+"§7: §r" + message +Color.RESET.getAnsiCode()));
                simpleLatestLog.saveLogs();
                this.lineReader.getTerminal().flush();
                if (!this.lineReader.isReading()) return;
                this.lineReader.callWidget(LineReader.REDRAW_LINE);
                this.lineReader.callWidget(LineReader.REDISPLAY);
            }else{
                this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
                this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §b"+type.toString().toUpperCase()+"§7: §r" + message + Color.RESET.getAnsiCode()));
                simpleLatestLog.log(getClearSting("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §b"+type.toString().toUpperCase()+"§7: §r" + message +Color.RESET.getAnsiCode()));
                simpleLatestLog.saveLogs();
                this.lineReader.getTerminal().flush();
                if (!this.lineReader.isReading()) return;
                this.lineReader.callWidget(LineReader.REDRAW_LINE);
                this.lineReader.callWidget(LineReader.REDISPLAY);
            }

        }else {
            if (type == Type.SETUP){
                this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
                this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §bSETUP§7: §r" + message +Color.RESET.getAnsiCode()));
                simpleLatestLog.log(getClearSting("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §bSETUP§7: §r" + message +Color.RESET.getAnsiCode()));
                simpleLatestLog.saveLogs();
                this.lineReader.getTerminal().flush();

                if (!this.lineReader.isReading()) return;
                this.lineReader.callWidget(LineReader.REDRAW_LINE);
                this.lineReader.callWidget(LineReader.REDISPLAY);

            } else if (type == Type.SETUP_ERROR){

                this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
                this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §bINCORRECT§7: §r" + message +Color.RESET.getAnsiCode()));
                simpleLatestLog.log(getClearSting("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §bINCORRECT§7: §r" + message +Color.RESET.getAnsiCode()));
                simpleLatestLog.saveLogs();
               this.lineReader.getTerminal().flush();

                if (!this.lineReader.isReading()) return;
                this.lineReader.callWidget(LineReader.REDRAW_LINE);
                this.lineReader.callWidget(LineReader.REDISPLAY);
            }else if (type == Type.EMPTY){

                this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
                this.terminal.writer().println("\r" + getColoredString(message + Color.RESET.getAnsiCode()));
                simpleLatestLog.log(getClearSting("\r" + getColoredString(message + Color.RESET.getAnsiCode())));
                simpleLatestLog.saveLogs();
                this.lineReader.getTerminal().flush();
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

    public String getClearSting(String text){

        text = text.replace("§r", "")
                .replace("§f", "")
                .replace("§0", "")
                .replace("§c", "")
                .replace("§e", "")
                .replace("§9", "")
                .replace("§a", "")
                .replace("§5", "")
                .replace("§6", "")
                .replace("§7", "")
                .replace("§4", "")
                .replace("§8", "")
                .replace("§1", "")
                .replace("§2", "")
                .replace("§b", "")
                .replace("§3", "");


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