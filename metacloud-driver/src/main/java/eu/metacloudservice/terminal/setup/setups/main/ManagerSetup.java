/*
 * this class is by RauchigesEtwas
 */

/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.terminal.setup.setups.main;

import eu.metacloudservice.Driver;
import eu.metacloudservice.storage.PacketLoader;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.terminal.setup.classes.SetupClass;
import eu.metacloudservice.timebaser.TimerBase;
import eu.metacloudservice.timebaser.utils.TimeUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class ManagerSetup extends SetupClass {

    @Override
    public void call(String line) {
        if (getStep() == 0){
            if (line.contains(".") && line.matches("[0-9.]+")){
                getAnswers().put("address", line);
                addStep();
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-manager-question-2"));

            }else {
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-failed"));
                new TimerBase().scheduleAsync(new TimerTask() {
                    @Override
                    public void run() {
                        Driver.getInstance().getTerminalDriver().clearScreen();
                        Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                        Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-manager-question-1"));

                    }
                }, 2, TimeUtil.SECONDS);
            }
        }else if (getStep() == 1){
            if (line.matches("[0-9]+")){
                getAnswers().put("memory", line);
                addStep();

                List<String> bungees = new PacketLoader().availableBungeecords();
                StringBuilder available = new StringBuilder();
                if (bungees.size() == 1){
                    available = new StringBuilder(bungees.get(0));
                }else {

                    for (int i = 0; i != bungees.size(); i++){
                        if (i == 0){
                            available = new StringBuilder(bungees.get(0));
                        }else {
                            available.append(", ").append(bungees.get(i));
                        }
                    }
                }

                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-manager-question-3"));
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-possible-answers")
                        .replace("%possible_answers%", available));
            }else {
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-failed"));
                new TimerBase().scheduleAsync(new TimerTask() {
                    @Override
                    public void run() {
                        Driver.getInstance().getTerminalDriver().clearScreen();
                        Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                        Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-manager-question-2"));

                    }
                }, 2, TimeUtil.SECONDS);
            }
        }else if (getStep() == 2){
            if (new PacketLoader().availableBungeecords().contains(line)){
                getAnswers().put("bungee", line.toUpperCase());
                addStep();
                List<String> spigots = new PacketLoader().availableSpigots();
                List<String> mainSpigots = new ArrayList<>();
                spigots.forEach(s -> {
                    if (!mainSpigots.contains(s.split("-")[0])){
                        mainSpigots.add(s.split("-")[0]);
                    }
                });

                StringBuilder available = new StringBuilder();
                if (mainSpigots.size() == 1){
                    available = new StringBuilder(mainSpigots.get(0));
                }else {

                    for (int i = 0; i != mainSpigots.size(); i++){
                        if (i == 0){
                            available = new StringBuilder(mainSpigots.get(0));
                        }else {
                            available.append(", ").append(mainSpigots.get(i));
                        }
                    }
                }

                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-manager-question-4"));
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-possible-answers")
                        .replace("%possible_answers%", available));
            }else {
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-failed"));
                new TimerBase().scheduleAsync(new TimerTask() {
                    @Override
                    public void run() {
                        List<String> bungees = new PacketLoader().availableBungeecords();
                        StringBuilder available = new StringBuilder();
                        if (bungees.size() == 1){
                            available = new StringBuilder(bungees.get(0));
                        }else {

                            for (int i = 0; i != bungees.size(); i++){
                                if (i == 0){
                                    available = new StringBuilder(bungees.get(0));
                                }else {
                                    available.append(", ").append(bungees.get(i));
                                }
                            }
                        }

                        Driver.getInstance().getTerminalDriver().clearScreen();
                        Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                        Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-manager-question-3"));
                        Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-possible-answers")
                                .replace("%possible_answers%", available));
                    }
                }, 2, TimeUtil.SECONDS);
            }
        }else if (getStep() == 3){
            List<String> spigots = new PacketLoader().availableSpigots();
            List<String> mainSpigots = new ArrayList<>();
            spigots.forEach(s -> {
                if (!mainSpigots.contains(s.split("-")[0])){
                    mainSpigots.add(s.split("-")[0]);
                }
            });
            if (mainSpigots.contains(line)){
                addStep();
                List<String> mainSpigots2 = new ArrayList<>();
                spigots.forEach(s -> {
                    if (s.startsWith(line)){
                        mainSpigots2.add(s);
                    }
                });

                StringBuilder available = new StringBuilder();
                if (mainSpigots2.size() == 1){
                    available = new StringBuilder(mainSpigots2.get(0));
                }else {

                    for (int i = 0; i != mainSpigots2.size(); i++){
                        if (i == 0){
                            available = new StringBuilder(mainSpigots2.get(0));
                        }else {
                            available.append(", ").append(mainSpigots2.get(i));
                        }
                    }
                }


                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-manager-question-5"));
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-possible-answers")
                        .replace("%possible_answers%", available));
            }else {
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-failed"));
                new TimerBase().scheduleAsync(new TimerTask() {
                    @Override
                    public void run() {
                        List<String> spigots = new PacketLoader().availableSpigots();
                        List<String> mainSpigots = new ArrayList<>();
                        spigots.forEach(s -> {
                            if (!mainSpigots.contains(s.split("-")[0])){
                                mainSpigots.add(s.split("-")[0]);
                            }
                        });

                        StringBuilder available = new StringBuilder();
                        if (mainSpigots.size() == 1){
                            available = new StringBuilder(mainSpigots.get(0));
                        }else {

                            for (int i = 0; i != mainSpigots.size(); i++){
                                if (i == 0){
                                    available = new StringBuilder(mainSpigots.get(0));
                                }else {
                                    available.append(", ").append(mainSpigots.get(i));
                                }
                            }
                        }

                        Driver.getInstance().getTerminalDriver().clearScreen();
                        Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                        Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-manager-question-4"));
                        Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-possible-answers")
                                .replace("%possible_answers%", available));
                    }
                }, 2, TimeUtil.SECONDS);
            }
        }

    }

    @Override
    public List<String> tabComplete() {
        List<String> complete = new ArrayList<>();
        if (getStep() == 0){
            try {
                String ip = new BufferedReader(new InputStreamReader(new URL("https://checkip.amazonaws.com").openConnection().getInputStream())).readLine();
                complete.add("127.0.0.1");
                complete.add(ip);
            } catch (IOException ignored) {}

        }else if (getStep() == 1){
            complete.add("1024");
            complete.add("" + 1024 *2);
            complete.add("" + 1024 *3);
            complete.add("" + 1024 *4);
            complete.add("" + 1024 *5);
            complete.add("" + 1024 *10);
            complete.add("" + 1024 *15);

        }else if (getStep() == 2){
            return new PacketLoader().availableBungeecords();
        }else if (getStep() == 3){
            List<String> spigots = new PacketLoader().availableSpigots();
            List<String> mainSpigots = new ArrayList<>();
            spigots.forEach(s -> {
                if (!mainSpigots.contains(s.split("-")[0])){
                    mainSpigots.add(s.split("-")[0]);
                }
            });
            return mainSpigots;
        }
        return complete;
    }
}
