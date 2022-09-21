package eu.themetacloudservice.terminal;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.storage.SetupStorage;
import eu.themetacloudservice.terminal.enums.Color;
import eu.themetacloudservice.terminal.enums.Type;
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
    public SetupStorage setupStorage;
    private final LineReader lineReader;
    private Thread consoleThread;

    @SneakyThrows
    public TerminalDriver() {

        /*
        * @FUNCTION: Terminal and reader with tapping in Jline3
        * @Coder: RauchigesEtwas (Robin B.)
        * */
        this.setupStorage = new SetupStorage();

        this.mainScreenStorage = new LinkedList<>();
        this.inputs = new LinkedList<>();
        this.isInSetup = false;
        this.terminal = TerminalBuilder.builder()
                .system(true)
                .streams(System.in, System.out)
                .encoding(StandardCharsets.UTF_8)
                .dumb(true)
                .build();

        this.lineReader = LineReaderBuilder.builder()
                .terminal(this.terminal)
                .option(LineReader.Option.AUTO_REMOVE_SLASH, false)
                .option(LineReader.Option.INSERT_TAB, false)
                .option(LineReader.Option.DISABLE_EVENT_EXPANSION, true)
                .build();

        this.consoleThread = new Thread(() -> {

            while (!this.consoleThread.isInterrupted()) {

                try {
                    final var line = this.lineReader.readLine(getColoredString("§eMetaCloud§6@§f" + Driver.getInstance().getMessageStorage().version + " §7> "));
                    if (line != null && !line.isEmpty()) {
                        final var input = this.getInputs().poll();

                        if(this.isInSetup){
                            if (line.equalsIgnoreCase("leave")){
                                leaveSetup();
                            }
                            if (!new File("./service.json").exists()){
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

    public void joinSetup(){
        this.isInSetup = true;
        clearScreen();
        if (Driver.getInstance().getMessageStorage().language.equalsIgnoreCase("DE")){
            log(Type.EMPTY, "               _______ _______ _______ _     _  _____ \n" +
                    "               |______ |______    |    |     | |_____]\n" +
                    "               ______| |______    |    |_____| |      \n" +
                    "       ________________________________________________________\n" +
                    "             §eMODERN§7 MINECRAFT §eCLOUDSYSTEM§7 FOR §eEVERYONE§7                                                        \n" +
                    "\n" +
                    "     <§e!§7> die Einrichtung ist §astarted§7, bitte beantworten Sie alle meine §eFragen §r\n" +
                    "     <§e!§7> Sie können die Einrichtung jederzeit mit \"§eleave§7\" verlassen\n");
            if (!new File("./service.json").exists()){
                Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Welche Sprache möchten Sie haben?");
                Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Mögliche Antworten: §eDE §7/ §eEN");
            }
        }else {
            log(Type.EMPTY, "               _______ _______ _______ _     _  _____ \n" +
                    "               |______ |______    |    |     | |_____]\n" +
                    "               ______| |______    |    |_____| |      \n" +
                    "       ________________________________________________________\n" +
                    "             §eMODERN§7 MINECRAFT §eCLOUDSYSTEM§7 FOR §eEVERYONE§7                                                        \n" +
                    "\n" +
                    "     <§e!§7> the setup has been §astarted§7, please answer all my §equestions §r\n" +
                    "     <§e!§7> you can leave the setup at any time with \"§eleave§7\"\n");
            if (!new File("./service.json").exists()){

                Driver.getInstance().getTerminalDriver().log(Type.SETUP, "What language would you like to have?");
                Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Possible answers: §eDE §7/ §eEN");
            }
        }
    }

    public void leaveSetup(){
        clearScreen();
        this.isInSetup = false;

        this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
        for (int i = 0; i != this.mainScreenStorage.size(); i++) {
                TerminalStorage storage = this.mainScreenStorage.get(i);

            if (storage.getType() == Type.EMPTY){
                this.terminal.writer().println("\r" + getColoredString(storage.getMessage() + Color.RESET.getAnsiCode()));
            }else {
                this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("dd.MM HH:mm:ss").format(System.currentTimeMillis()) + "§7] §e"+storage.getType().toString().toUpperCase()+"§7: §r" + storage.getMessage() + Color.RESET.getAnsiCode()));
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
                this.lineReader.getTerminal().writer().flush();
                if (!this.lineReader.isReading()) return;
                this.lineReader.callWidget(LineReader.REDRAW_LINE);
                this.lineReader.callWidget(LineReader.REDISPLAY);

            }else {

                this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
                this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("dd.MM HH:mm:ss").format(System.currentTimeMillis()) + "§7] §e"+type.toString().toUpperCase()+"§7: §r" + message + Color.RESET.getAnsiCode()));
                this.lineReader.getTerminal().writer().flush();
                if (!this.lineReader.isReading()) return;
                this.lineReader.callWidget(LineReader.REDRAW_LINE);
                this.lineReader.callWidget(LineReader.REDISPLAY);
            }

        }else {
            if (type == Type.SETUP){
                this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
                this.terminal.writer().println("\r" + getColoredString("§7[§f"  + new SimpleDateFormat("dd.MM HH:mm:ss").format(System.currentTimeMillis()) + "§7] §eSETUP§7: §r" + message +Color.RESET.getAnsiCode()));
                this.lineReader.getTerminal().writer().flush();
                this.lineReader.callWidget(LineReader.REDRAW_LINE);
                this.lineReader.callWidget(LineReader.REDISPLAY);

            } else if (type == Type.EMPTY){

                this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
                this.terminal.writer().println("\r" + getColoredString(message + Color.RESET.getAnsiCode()));
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
}
