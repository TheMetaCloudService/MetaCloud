/*
 * this class is by RauchigesEtwas
 */

/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.terminal;

import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.packet.packets.in.node.PacketInSendConsole;
import eu.metacloudservice.networking.packet.packets.in.node.PacketInSendConsoleFromNode;
import eu.metacloudservice.terminal.commands.CommandDriver;
import eu.metacloudservice.terminal.completer.TerminalCompleter;
import eu.metacloudservice.terminal.enums.Color;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.terminal.logging.SimpleLatestLog;
import eu.metacloudservice.terminal.setup.SetupDriver;
import eu.metacloudservice.terminal.setup.setups.main.GeneralSetup;
import eu.metacloudservice.terminal.setup.setups.group.GroupSetup;
import eu.metacloudservice.terminal.streams.LoggerOutputStream;
import eu.metacloudservice.terminal.utils.TerminalStorage;
import eu.metacloudservice.terminal.utils.TerminalStorageLine;
import eu.metacloudservice.webserver.entry.RouteEntry;
import lombok.SneakyThrows;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;
import java.io.File;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.Queue;

public final class TerminalDriver {


    private boolean isInSetup;
    private  LinkedList<TerminalStorage> mainScreenStorage;
    private  Queue<TerminalStorageLine> inputs;

    private  SetupDriver setupDriver;

    private  Terminal terminal;
    private  LineReader lineReader;
    private  SimpleLatestLog simpleLatestLog;
    private  CommandDriver commandDriver;

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
        this.commandDriver = new CommandDriver();
        setupDriver = new SetupDriver();
        this.mainScreenStorage = new LinkedList<>();
        this.inputs = new LinkedList<>();
        this.simpleLatestLog = new SimpleLatestLog();
        this.isInSetup = false;
        this.terminal = TerminalBuilder.builder()
                .system(true)
                .encoding(StandardCharsets.UTF_8)
                .name("META-CONSOLE")
                .build();

        this.lineReader = LineReaderBuilder.builder()
                .terminal(this.terminal)
                .completer(new TerminalCompleter())
                .appName("META-READER")
                .option(LineReader.Option.AUTO_REMOVE_SLASH, true)
                .option(LineReader.Option.HISTORY_IGNORE_SPACE, true)
                .option(LineReader.Option.HISTORY_REDUCE_BLANKS, true)
                .option(LineReader.Option.HISTORY_IGNORE_DUPS, true)
                .option(LineReader.Option.EMPTY_WORD_OPTIONS, false)
                .option(LineReader.Option.HISTORY_TIMESTAMPED, false)
                .option(LineReader.Option.DISABLE_EVENT_EXPANSION, true)
                .option(LineReader.Option.INSERT_TAB, false)
                .variable(LineReader.HISTORY_SIZE, 500)
                .parser(new DefaultParser().eofOnUnclosedQuote(true))
                .build();

        Thread consoleReadingThread = new TerminalReader(this);
        consoleReadingThread.setName("CONSOLE");
        consoleReadingThread.setPriority(Thread.MAX_PRIORITY);
        consoleReadingThread.start();

    }



    public Terminal getTerminal() {
        return terminal;
    }

    public LineReader getLineReader() {
        return lineReader;
    }


    public void joinSetup(){

        this.isInSetup = true;

        if (!new File("./service.json").exists() && !new File("./nodeservice.json").exists()){
            String joinedLanguages = String.join(", ", Driver.getInstance().getLanguageDriver().getSupportedLanguages());
            clearScreen();
            log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
            Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-1"));
            Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-possible-answers")
                    .replace("%possible_answers%", joinedLanguages));

            setupDriver.setSetup(new GeneralSetup());
        }else {
            clearScreen();
            log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
            Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-group-question-1"));
            setupDriver.setSetup(new GroupSetup());
        }
    }

    public void leaveSetup(){
        clearScreen();
        if (Driver.getInstance().getMessageStorage().openServiceScreen){
            Driver.getInstance().getMessageStorage().openServiceScreen = false;
            Driver.getInstance().getMessageStorage().screenForm = "";
        }
        this.isInSetup = false;
        if (setupDriver.getSetup() instanceof GroupSetup){
            Driver.getInstance().getGroupDriver().getAll().forEach(group -> {
                if (group == null) return;
                if (Driver.getInstance().getWebServer().getRoute("/"+group.getGroup()) == null){
                    Driver.getInstance().getWebServer().addRoute(new RouteEntry("/" + group.getGroup(), new ConfigDriver().convert(group)));
                }else {
                    Driver.getInstance().getWebServer().updateRoute("/" + group.getGroup(), new ConfigDriver().convert(group));
                }
            });
        }
        setupDriver.setSetup(null);
        this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
        if ( this.mainScreenStorage.size() > 200){
            for (int i =  this.mainScreenStorage.size()-200; i != this.mainScreenStorage.size(); i++) {
                TerminalStorage storage = this.mainScreenStorage.get(i);

                if (storage.getType() == Type.EMPTY){
                    String msg = storage.getMessage();
                    this.terminal.writer().println("\r" + getColoredString(msg + Color.RESET.getAnsiCode()));
                }else {
                    this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §b"+storage.getType().toString().toUpperCase()+"§7: §r" + storage.getMessage() + Color.RESET.getAnsiCode()));
                }
            }
        }else {
            for (int i =  this.mainScreenStorage.size() > 60 ? this.mainScreenStorage.size()-30 : 0; i != this.mainScreenStorage.size(); i++) {
                TerminalStorage storage = this.mainScreenStorage.get(i);
                if (storage.getType() == Type.EMPTY){
                    String msg = storage.getMessage();
                    this.terminal.writer().println("\r" + getColoredString(msg + Color.RESET.getAnsiCode()));
                }else {
                    this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §b"+storage.getType().toString().toUpperCase()+"§7: §r" + storage.getMessage() + Color.RESET.getAnsiCode()));
                }
            }
        }

        this.lineReader.getTerminal().flush();
        if (!this.lineReader.isReading()) return;
        this.lineReader.callWidget(LineReader.REDRAW_LINE);
        this.lineReader.callWidget(LineReader.REDISPLAY);

    }

    public void redirect(){
        this.terminal.puts(InfoCmp.Capability.clear_screen);
        this.terminal.flush();
        this.redraw();
        this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
        if ( this.mainScreenStorage.size() > 200){
            for (int i =  this.mainScreenStorage.size()-200; i != this.mainScreenStorage.size(); i++) {
                TerminalStorage storage = this.mainScreenStorage.get(i);

                if (storage.getType() == Type.EMPTY){
                    String msg = storage.getMessage();
                    this.terminal.writer().println("\r" + getColoredString(msg + Color.RESET.getAnsiCode()));
                }else {
                    this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §b"+storage.getType().toString().toUpperCase()+"§7: §r" + storage.getMessage() + Color.RESET.getAnsiCode()));
                }
            }
        }else {
            for (int i =  this.mainScreenStorage.size() > 60 ? this.mainScreenStorage.size()-30 : 0; i != this.mainScreenStorage.size(); i++) {
                TerminalStorage storage = this.mainScreenStorage.get(i);
                if (storage.getType() == Type.EMPTY){
                    String msg = storage.getMessage();
                    this.terminal.writer().println("\r" + getColoredString(msg + Color.RESET.getAnsiCode()));
                }else {
                    this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §b"+storage.getType().toString().toUpperCase()+"§7: §r" + storage.getMessage() + Color.RESET.getAnsiCode()));
                }
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

    @SneakyThrows
    public void log(String service, String messages){

        this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
        this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §b"+
                service +"§7: §r" + messages +Color.RESET.getAnsiCode()));
        this.lineReader.getTerminal().flush();
        simpleLatestLog.log(getColoredString(getClearSting("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §c"+service+"§7: §r" + messages )));
        
        this.lineReader.getTerminal().flush();
        if (!this.lineReader.isReading()) return;
        this.lineReader.callWidget(LineReader.REDRAW_LINE);
        this.lineReader.callWidget(LineReader.REDISPLAY);
    }

    @SneakyThrows
    public void log(Type type, String... messages){

        this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
        for (int i = 0; i != messages.length ; i++) {
            if (Driver.getInstance().getMessageStorage().sendConsoleToManager && (Driver.getInstance().getMessageStorage().sendConsoleToManagerName == null || Driver.getInstance().getMessageStorage().sendConsoleToManagerName.equalsIgnoreCase(""))){
                NettyDriver.getInstance().nettyClient.sendPacketSynchronized(new PacketInSendConsoleFromNode(messages[i]));
            }

            this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §b"+type.toString().toUpperCase().replace("INFORMATION", "§bINFORMATION")
                    .replace("ERROR", "§cERROR").replace("WARNING", "§eWARN")+"§7: §r" + messages[i] +Color.RESET.getAnsiCode()));
            simpleLatestLog.log(getClearSting("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §b"+type.toString().toUpperCase()+"§7: §r" + messages[i]));


        }
        this.lineReader.getTerminal().flush();
        if (!this.lineReader.isReading()) return;
        this.lineReader.callWidget(LineReader.REDRAW_LINE);
        this.lineReader.callWidget(LineReader.REDISPLAY);
    }

    @SneakyThrows
    public void log(Type type, String message){
        /*
         * @FUNCTION: Log output in console
         * @ARGUMENTS: Type type, String raw
         * @Coder: RauchigesEtwas (Robin B.)
         * */

        if (Driver.getInstance().getMessageStorage().sendConsoleToManager && (Driver.getInstance().getMessageStorage().sendConsoleToManagerName == null || Driver.getInstance().getMessageStorage().sendConsoleToManagerName.equalsIgnoreCase(""))){
            NettyDriver.getInstance().nettyClient.sendPacketSynchronized(new PacketInSendConsoleFromNode(message));
        }

        if (Driver.getInstance().getMessageStorage().openServiceScreen){

            this.mainScreenStorage.add(new TerminalStorage(type, message));

        }else if (!this.isInSetup){

            this.mainScreenStorage.add(new TerminalStorage(type, message));

            if (type == Type.EMPTY){
                this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
                this.terminal.writer().println("\r" + getColoredString(message + Color.RESET.getAnsiCode()));
                this.lineReader.getTerminal().flush();
                simpleLatestLog.log(getClearSting(getClearSting(message)));
                
                if (!this.lineReader.isReading()) return;
                this.lineReader.callWidget(LineReader.REDRAW_LINE);
                this.lineReader.callWidget(LineReader.REDISPLAY);

            }else if (type == Type.ERROR){

                this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
                this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §c"+type.toString().toUpperCase()+"§7: §r" + message + Color.RESET.getAnsiCode()));
                simpleLatestLog.log(getClearSting("["  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "] §c"+type.toString().toUpperCase()+"§7: §r" + message ));
                
                this.lineReader.getTerminal().flush();
                if (!this.lineReader.isReading()) return;
                this.lineReader.callWidget(LineReader.REDRAW_LINE);
                this.lineReader.callWidget(LineReader.REDISPLAY);
            }else if (type == Type.INFO){

                this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
                this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §b"+type.toString().toUpperCase()+"§7: §r" + message + Color.RESET.getAnsiCode()));
                simpleLatestLog.log(getClearSting("["  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "] §b"+type.toString().toUpperCase()+"§7: §r" + message));
                
                this.lineReader.getTerminal().flush();
                if (!this.lineReader.isReading()) return;
                this.lineReader.callWidget(LineReader.REDRAW_LINE);
                this.lineReader.callWidget(LineReader.REDISPLAY);
            }else if (type == Type.WARN){

                this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
                this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §b"+type.toString().toUpperCase()+"§7: §r" + message + Color.RESET.getAnsiCode()));
                simpleLatestLog.log(getClearSting("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §e"+type.toString().toUpperCase()+"§7: §r" + message));
                
                this.lineReader.getTerminal().flush();
                if (!this.lineReader.isReading()) return;
                this.lineReader.callWidget(LineReader.REDRAW_LINE);
                this.lineReader.callWidget(LineReader.REDISPLAY);
            }else{
                this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
                this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §b"+type.toString().toUpperCase()+"§7: §r" + message + Color.RESET.getAnsiCode()));
                simpleLatestLog.log(getClearSting("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §b"+type.toString().toUpperCase()+"§7: §r" + message ));
                
                this.lineReader.getTerminal().flush();
                if (!this.lineReader.isReading()) return;
                this.lineReader.callWidget(LineReader.REDRAW_LINE);
                this.lineReader.callWidget(LineReader.REDISPLAY);
            }

        }else {
            if (type == Type.INSTALLATION){
                this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
                this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §bINSTALLATION§7: §r" + message +Color.RESET.getAnsiCode()));
                simpleLatestLog.log(getClearSting("["  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "] SETUP: " + message ));
                
                this.lineReader.getTerminal().flush();

                if (!this.lineReader.isReading()) return;
                this.lineReader.callWidget(LineReader.REDRAW_LINE);
                this.lineReader.callWidget(LineReader.REDISPLAY);

            } else if (type == Type.SETUP_ERROR){

                this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
                this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "§7] §bINCORRECT§7: §r" + message +Color.RESET.getAnsiCode()));
                simpleLatestLog.log(getClearSting("["  + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "] INCORRECT: " + message ));
                
               this.lineReader.getTerminal().flush();

                if (!this.lineReader.isReading()) return;
                this.lineReader.callWidget(LineReader.REDRAW_LINE);
                this.lineReader.callWidget(LineReader.REDISPLAY);
            }else if (type == Type.EMPTY){

                this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
                this.terminal.writer().println("\r" + getColoredString(message + Color.RESET.getAnsiCode()));
                simpleLatestLog.log(getClearSting("\r" + getClearSting(message )));
                
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
     *
     * @param text the text
     * @return the string
     */
    public String getColoredString(String text) {
        if (Driver.getInstance().getMessageStorage().sendConsoleToManager){
            NettyDriver.getInstance().nettyClient.sendPacketSynchronized(new PacketInSendConsole(Driver.getInstance().getMessageStorage().sendConsoleToManagerName, text));
        }
        for (Color consoleColour : Color.values()) {
            text = text.replace('§' + String.valueOf(consoleColour.getIndex()), consoleColour.getAnsiCode());
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

    public SetupDriver getSetupDriver() {
        return setupDriver;
    }

    public CommandDriver getCommandDriver() {
        return commandDriver;
    }
}
