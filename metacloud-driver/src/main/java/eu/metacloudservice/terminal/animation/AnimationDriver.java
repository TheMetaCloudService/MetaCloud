package eu.metacloudservice.terminal.animation;

import eu.metacloudservice.Driver;
import lombok.SneakyThrows;

import java.util.LinkedList;
import java.util.List;

public class AnimationDriver {

    private final List<String> layout;

    public AnimationDriver() {
        this.layout = new LinkedList<>();
        this.layout.add("[§a██████████████                                                                         §7] (-) | §a10%/100% ");
        this.layout.add("[§a██████████████████                                                                     §7] (X) | §a25%/100% ");
        this.layout.add("[§a████████████████████████                                                               §7] (|) | §a30%/100% ");
        this.layout.add("[§a████████████████████████████████                                                       §7] (X) | §a40%/100% ");
        this.layout.add("[§a█████████████████████████████████████                                                  §7] (-) | §a50%/100% ");
        this.layout.add("[§a██████████████████████████████████████████                                             §7] (X) | §a60%/100% ");
        this.layout.add("[§a███████████████████████████████████████████████████                                    §7] (|) | §a65%/100% ");
        this.layout.add("[§a█████████████████████████████████████████████████████████                              §7] (X) | §a70%/100% ");
        this.layout.add("[§a████████████████████████████████████████████████████████████                           §7] (-) | §a75%/100% ");
        this.layout.add("[§a██████████████████████████████████████████████████████████████                         §7] (X) | §a78%/100% ");
        this.layout.add("[§a█████████████████████████████████████████████████████████████████                      §7] (|) | §a80%/100% ");
        this.layout.add("[§a████████████████████████████████████████████████████████████████████                   §7] (X) | §a84%/100% ");
        this.layout.add("[§a███████████████████████████████████████████████████████████████████████                §7] (-) | §a87%/100% ");
        this.layout.add("[§a██████████████████████████████████████████████████████████████████████████             §7] (X) | §a90%/100% ");
        this.layout.add("[§a█████████████████████████████████████████████████████████████████████████████          §7] (|) | §a95%/100% ");
        this.layout.add("[§a██████████████████████████████████████████████████████████████████████████████████     §7] (X) | §a99%/100% ");
        this.layout.add("[§a███████████████████████████████████████████████████████████████████████████████████████§7] (X) | §a100%/100% ");

    }


    @SneakyThrows
    public void play(){
        for (int i = 0; i != 18 ; i++) {
            if (i == 17) {
                Driver.getInstance().getTerminalDriver().redirect();
            } else {
                String layout = this.layout.get(i);
                Driver.getInstance().getTerminalDriver().getTerminal().writer().print("\r" + layout);
                Driver.getInstance().getTerminalDriver().getTerminal().writer().flush();
                Thread.sleep(250);
            }
        }
    }

}
