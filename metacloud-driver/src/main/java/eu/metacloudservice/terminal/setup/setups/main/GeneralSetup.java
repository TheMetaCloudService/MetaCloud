/*
 * this class is by RauchigesEtwas
 */

/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.terminal.setup.setups.main;


import eu.metacloudservice.Driver;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.terminal.setup.classes.SetupClass;
import eu.metacloudservice.timebaser.TimerBase;
import eu.metacloudservice.timebaser.utils.TimeUtil;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class GeneralSetup extends SetupClass {

    @SneakyThrows
    @Override
    public void call(String line) {
        if (getStep() == 0){
            if (Driver.getInstance().getLanguageDriver().getSupportedLanguages().stream().anyMatch(s -> s.equalsIgnoreCase(line))){
                Driver.getInstance().getMessageStorage().language = line.toUpperCase();
                Driver.getInstance().getLanguageDriver().reload();
                getAnswers().put("language", line.toUpperCase());
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());

                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-2"));
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-possible-answers")
                        .replace("%possible_answers%", "yes, no"));

                addStep();


            }else {
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-failed"));
                new TimerBase().scheduleAsync(new TimerTask() {
                    @Override
                    public void run() {
                        String joinedLanguages = String.join(", ", Driver.getInstance().getLanguageDriver().getSupportedLanguages());
                        Driver.getInstance().getTerminalDriver().clearScreen();
                        Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                        Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-1"));
                        Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-possible-answers")
                                .replace("%possible_answers%", joinedLanguages));

                    }
                }, 2, TimeUtil.SECONDS);
            }
        }else if (getStep() == 1){
            if (line.equalsIgnoreCase("yes") || line.equalsIgnoreCase("y") || line.equalsIgnoreCase("no") || line.equalsIgnoreCase("n")){
                addStep();
                getAnswers().put("updater", line.equalsIgnoreCase("yes") || line.equalsIgnoreCase("y"));
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-3"));
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-possible-answers")
                        .replace("%possible_answers%", "Manager, Node"));
            }else {
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-failed"));
                new TimerBase().scheduleAsync(new TimerTask() {
                    @Override
                    public void run() {
                        Driver.getInstance().getTerminalDriver().clearScreen();
                        Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                        Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-2"));
                        Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-possible-answers")
                                .replace("%possible_answers%", "yes, no"));

                    }
                }, 2, TimeUtil.SECONDS);
            }
        }else if (getStep() == 2){
            if (line.equalsIgnoreCase("manager") || line.equalsIgnoreCase("node") ){
                if (line.equalsIgnoreCase("manager")){
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-manager-question-1"));
                    Driver.getInstance().getTerminalDriver().getSetupDriver().setSetup(new ManagerSetup());
                    Driver.getInstance().getTerminalDriver().getSetupDriver().getSetup().getAnswers().putAll(getAnswers());
                }else {
                    String ip = new BufferedReader(new InputStreamReader(new URL("https://checkip.amazonaws.com").openConnection().getInputStream())).readLine();
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                   Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-node-question-1")
                            .replace("%address%", ip).split("\n"));
                    Driver.getInstance().getTerminalDriver().getSetupDriver().setSetup(new NodeSetup());
                    Driver.getInstance().getTerminalDriver().getSetupDriver().getSetup().getAnswers().putAll(getAnswers());

                }
            }else {
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-failed"));
                new TimerBase().scheduleAsync(new TimerTask() {
                    @Override
                    public void run() {
                        Driver.getInstance().getTerminalDriver().clearScreen();
                        Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                        Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-3"));
                        Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-possible-answers")
                                .replace("%possible_answers%", "Manager, Node"));

                    }
                }, 2, TimeUtil.SECONDS);
            }
        }
    }

    @Override
    public List<String> tabComplete() {
        List<String> complete = new ArrayList<>();
        if (getStep() == 0) {
            complete.addAll(Driver.getInstance().getLanguageDriver().getSupportedLanguages());
        }else if (getStep() == 1) {
           complete.add("yes");
           complete.add("no");
        }else if (getStep() == 2) {
            complete.add("Manager");
            complete.add("Node");
        }
        return complete;
    }




}
