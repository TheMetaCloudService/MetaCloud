/*
 * this class is by RauchigesEtwas
 */

/*
 * this class is by RauchigesEtwas
 */

/*
 * this class is by RauchigesEtwas
 */

/*
 * this class is by RauchigesEtwas
 */

/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.terminal.setup.setups.group;

import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.metacloudservice.configuration.dummys.managerconfig.ManagerConfigNodes;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.groups.dummy.GroupStorage;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.terminal.setup.classes.SetupClass;
import eu.metacloudservice.timebaser.TimerBase;
import eu.metacloudservice.timebaser.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GroupSetup extends SetupClass {

    @Override
    public void call(String line) {
        if (getStep() == 0){
            Driver.getInstance().getTerminalDriver().clearScreen();
            addStep();
            getAnswers().put("group", line);
            Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
            Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-group-question-2"));
            Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-possible-answers")
                    .replace("%possible_answers%", "PROXY, LOBBY, GAME"));

            return;
        } if (getStep() == 1){
            if (line.equalsIgnoreCase("PROXY") || line.equalsIgnoreCase("PROXY ")){
                Driver.getInstance().getTerminalDriver().clearScreen();
                addStep();
                getAnswers().put("groupType", "PROXY");
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-group-question-3"));


                return;
            }else if (line.equalsIgnoreCase("LOBBY") || line.equalsIgnoreCase("LOBBY ")){
                Driver.getInstance().getTerminalDriver().clearScreen();
                addStep();
                getAnswers().put("groupType", "LOBBY");
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-group-question-3"));



                return;
            }else if (line.equalsIgnoreCase("GAME") || line.equalsIgnoreCase("GAME ")){
                Driver.getInstance().getTerminalDriver().clearScreen();
                addStep();
                getAnswers().put("groupType", "GAME");
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-group-question-3"));



                return;
            }else {
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-failed"));
                new TimerBase().scheduleAsync(new TimerTask() {
                    @Override
                    public void run() {
                        Driver.getInstance().getTerminalDriver().clearScreen();
                        Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                        Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-group-question-2"));
                        Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-possible-answers")
                                .replace("%possible_answers%", "PROXY, LOBBY, GAME"));
                    }
                }, 2, TimeUtil.SECONDS);


                return;
            }
        }if (getStep() == 2){
            if(line.matches("[0-9]+")){
                Driver.getInstance().getTerminalDriver().clearScreen();
                addStep();
                getAnswers().put("memory", line.replace(" ", ""));
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-group-question-4"));
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-possible-answers")
                        .replace("%possible_answers%", "yes, no"));

            }else {
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-failed"));
                new TimerBase().scheduleAsync(new TimerTask() {
                    @Override
                    public void run() {
                        Driver.getInstance().getTerminalDriver().clearScreen();
                        Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                        Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-group-question-3"));

                    }
                }, 2, TimeUtil.SECONDS);
            }
            return;
        }if (getStep() == 3){
            if (line.equalsIgnoreCase("yes") || line.equalsIgnoreCase("y")){
                Driver.getInstance().getTerminalDriver().clearScreen();
                addStep();
                getAnswers().put("static", true);
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-group-question-5"));

                return;

            }else if (line.equalsIgnoreCase("No") || line.equalsIgnoreCase("N")){
                Driver.getInstance().getTerminalDriver().clearScreen();
                addStep();
                getAnswers().put("static", false);
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-group-question-5"));


                return;
            }else {
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-failed"));
                new TimerBase().scheduleAsync(new TimerTask() {
                    @Override
                    public void run() {
                        Driver.getInstance().getTerminalDriver().clearScreen();
                        Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                        Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-group-question-4"));
                        Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-possible-answers")
                                .replace("%possible_answers%", "yes, no"));
                    }
                }, 2, TimeUtil.SECONDS);

                return;
            }

        }if (getStep() == 4){
            if(line.matches("[0-9]+")){
                Driver.getInstance().getTerminalDriver().clearScreen();
                addStep();
                getAnswers().put("players", line);
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-group-question-6"));


            }else {
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-failed"));
                new TimerBase().scheduleAsync(new TimerTask() {
                    @Override
                    public void run() {
                        Driver.getInstance().getTerminalDriver().clearScreen();
                        Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                        Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-group-question-5"));

                    }
                }, 2, TimeUtil.SECONDS);

            }
            return;
        }if (getStep() == 5){
            if(line.matches("[0-9]+")){
                Driver.getInstance().getTerminalDriver().clearScreen();
                addStep();
                getAnswers().put("minonline", line);
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-group-question-7"));

            }else {
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-failed"));
                new TimerBase().scheduleAsync(new TimerTask() {
                    @Override
                    public void run() {
                        Driver.getInstance().getTerminalDriver().clearScreen();
                        Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                        Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-group-question-6"));

                    }
                }, 2, TimeUtil.SECONDS);

            }
            return;
        }if (getStep() == 6){
            if(line.matches("[0-9]+") || line.equalsIgnoreCase("-1")){
                Driver.getInstance().getTerminalDriver().clearScreen();
                addStep();
                getAnswers().put("maxoneline", line);
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-group-question-8"));


            }else {
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-failed"));
                new TimerBase().scheduleAsync(new TimerTask() {
                    @Override
                    public void run() {
                        Driver.getInstance().getTerminalDriver().clearScreen();
                        Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                        Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-group-question-7"));

                    }
                }, 2, TimeUtil.SECONDS);


            }
            return;
        }if (getStep() == 7){
            if(line.matches("[0-9]+") && Integer.parseInt(line) <= 100){
                Driver.getInstance().getTerminalDriver().clearScreen();
                addStep();
                getAnswers().put("startnew", Integer.valueOf(line));
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-group-question-9"));


            }else {
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-failed"));
                new TimerBase().scheduleAsync(new TimerTask() {
                    @Override
                    public void run() {
                        Driver.getInstance().getTerminalDriver().clearScreen();
                        Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                        Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-group-question-8"));

                    }
                }, 2, TimeUtil.SECONDS);

            }
            return;

        }if (getStep() == 8){
            if(line.matches("[0-9]+")){
                Driver.getInstance().getTerminalDriver().clearScreen();
                addStep();
                getAnswers().put("group100", Integer.valueOf(line));
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-group-question-10"));


            }else {
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-failed"));
                new TimerBase().scheduleAsync(new TimerTask() {
                    @Override
                    public void run() {
                        Driver.getInstance().getTerminalDriver().clearScreen();
                        Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                        Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-group-question-9"));

                    }
                }, 2, TimeUtil.SECONDS);

            }
            return;
        }if (getStep() == 9){
            if(line.matches("[0-9]+")){
                Driver.getInstance().getTerminalDriver().clearScreen();
                addStep();
                getAnswers().put("network100", Integer.valueOf(line));


                ArrayList<String> templates = Driver.getInstance().getTemplateDriver().get();

                String templateList;
                if (templates.isEmpty()){
                    templateList = "CREATE";
                }else {
                    StringBuilder templateListBuilder = new StringBuilder();
                    for (int i = 0; i != templates.size() ; i++) {
                        String temp = templates.get(i);
                        templateListBuilder.append(temp.replace("null", "")).append(", ");
                    }
                    templateList = templateListBuilder.toString();
                    templateList = templateList + "CREATE";
                }



                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-group-question-11"));
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-possible-answers")
                        .replace("%possible_answers%", templateList));

            }else {
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-failed"));
                new TimerBase().scheduleAsync(new TimerTask() {
                    @Override
                    public void run() {
                        Driver.getInstance().getTerminalDriver().clearScreen();
                        Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                        Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-group-question-10"));

                    }
                }, 2, TimeUtil.SECONDS);

            }
            return;
        }if (getStep() == 10){
            ArrayList<String> rawtemplates = Driver.getInstance().getTemplateDriver().get();
            ArrayList<String> templates = new ArrayList<>();
            rawtemplates.forEach(s -> {
                templates.add(s);
                templates.add(s+ " ");
            });
            if (templates.contains(line) || line.equalsIgnoreCase("CREATE") || line.equals("CREATE ")){
                Driver.getInstance().getTerminalDriver().clearScreen();
                addStep();
                if (line.equalsIgnoreCase("CREATE") || line.equals("CREATE ")){
                    getAnswers().put("template", getAnswers().get("group").toString());
                }else {
                    getAnswers().put("template", line.replace(" ", ""));
                }
                ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
                ArrayList<ManagerConfigNodes> configNodes = config.getNodes();
                StringBuilder templateListBuilder = new StringBuilder();
                for (int i = 0; i != configNodes.size() ; i++) {
                    String temp = configNodes.get(i).getName();
                    if (i == configNodes.size()-1){
                        templateListBuilder.append(temp);
                    }else {
                        templateListBuilder.append(temp).append(", ");
                    }

                }

                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-group-question-12"));

                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-possible-answers")
                        .replace("%possible_answers%", templateListBuilder));
            }else {
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-failed"));
                new TimerBase().scheduleAsync(new TimerTask() {
                    @Override
                    public void run() {
                        Driver.getInstance().getTerminalDriver().clearScreen();
                        ArrayList<String> templates = Driver.getInstance().getTemplateDriver().get();

                        String templateList;
                        if (templates.isEmpty()){
                            templateList = "CREATE";
                        }else {
                            StringBuilder templateListBuilder = new StringBuilder();
                            for (int i = 0; i != templates.size() ; i++) {
                                String temp = templates.get(i);
                                templateListBuilder.append(temp.replace("null", "")).append(", ");
                            }
                            templateList = templateListBuilder.toString();
                            templateList = templateList + "CREATE";
                        }



                        Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                        Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-group-question-11"));
                        Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-possible-answers")
                                .replace("%possible_answers%", templateList));
                    }
                }, 2, TimeUtil.SECONDS);

            }
            return;
        }if (getStep() == 11){
            Driver.getInstance().getTerminalDriver().clearScreen();

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {

                    Driver.getInstance().getGroupDriver().create(new Group(getAnswers().get("group").toString(),
                            getAnswers().get("groupType").toString(),
                            Integer.valueOf( getAnswers().get("memory").toString()),
                            true,
                            Boolean.parseBoolean(getAnswers().get("static").toString()),
                            0,
                            "",
                            Integer.valueOf(getAnswers().get("players").toString()),
                            Integer.valueOf(getAnswers().get("minonline").toString()),
                            Integer.valueOf(getAnswers().get("maxoneline").toString()),
                            Integer.valueOf(getAnswers().get("startnew").toString()),
                            Integer.valueOf(getAnswers().get("group100").toString()),
                            Integer.valueOf(getAnswers().get("network100").toString()),
                            new GroupStorage(  getAnswers().get("template").toString(),
                                    getAnswers().get("node").toString() , "", ""), 0
                    ));


                    Driver.getInstance().getTerminalDriver().leaveSetup();
                }
            }, 1000);

            Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
            getAnswers().put("node", line);
            Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-group-finish"));


        }
    }

    @Override
    public List<String> tabComplete() {
        List<String> complete = new ArrayList<>();
        if (getStep() == 1){
            complete.add("PROXY");
            complete.add("GAME");
            complete.add("LOBBY");
        }else  if (getStep() == 2){
            complete.add("512");
            complete.add("1024");
            complete.add("2048");
        }else  if (getStep() == 3){
            complete.add("yes");
            complete.add("no");
        }else  if (getStep() == 6){
            complete.add("-1");
        }else  if (getStep() == 7){
            complete.add("25");
            complete.add("50");
            complete.add("75");
            complete.add("100");
        }else  if (getStep() == 10){
            complete.addAll(Driver.getInstance().getTemplateDriver().get());
            complete.add("create");
        }else  if (getStep() == 11){
            ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
            config.getNodes().forEach(managerConfigNodes -> complete.add(managerConfigNodes.getName()));
        }


        return complete;
    }


}
