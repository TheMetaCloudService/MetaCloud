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

package eu.metacloudservice.terminal.setup.setups.main;

import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.nodeconfig.NodeConfig;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.terminal.setup.classes.SetupClass;
import eu.metacloudservice.timebaser.TimerBase;
import eu.metacloudservice.timebaser.utils.TimeUtil;
import eu.metacloudservice.webserver.RestDriver;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class NodeSetup extends SetupClass {


    @Override
    public void call(String line) {
        if (getStep() == 0){
            if (line.contains("/setup/")){
                NodeConfig config = (NodeConfig) new ConfigDriver().convert(new RestDriver().getWithoutAuth(line), NodeConfig.class);
                new ConfigDriver("nodeservice.json").save(config);
                Driver.getInstance().getTerminalDriver().leaveSetup();
            }else {
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-general-question-failed"));
                new TimerBase().scheduleAsync(new TimerTask() {
                    @SneakyThrows
                    @Override
                    public void run() {

                        String ip = new BufferedReader(new InputStreamReader(new URL("https://checkip.amazonaws.com").openConnection().getInputStream())).readLine();
                        Driver.getInstance().getTerminalDriver().clearScreen();
                        Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                        Driver.getInstance().getTerminalDriver().log(Type.INSTALLATION, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-node-question-1")
                                .replace("%address%", ip).split("\n"));
                    }
                }, 2, TimeUtil.SECONDS);
            }
        }

    }

    @Override
    public List<String> tabComplete() {
        List<String> complete = new ArrayList<>();
        return complete;
    }


}
