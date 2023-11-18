/*
 * this class is by RauchigesEtwas
 */

/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.terminal.animation;

import eu.metacloudservice.Driver;
import lombok.SneakyThrows;

import java.util.LinkedList;
import java.util.List;

public class AnimationDriver {

    private final List<String> layout;

    public AnimationDriver() {
        this.layout = new LinkedList<>();
        this.layout.add("[██████████████                                                                         ] (-) | 10%/100% ");
        this.layout.add("[██████████████████                                                                     ] (X) | 25%/100% ");
        this.layout.add("[████████████████████████                                                               ] (|) | 30%/100% ");
        this.layout.add("[████████████████████████████████                                                       ] (X) | 40%/100% ");
        this.layout.add("[█████████████████████████████████████                                                  ] (-) | 50%/100% ");
        this.layout.add("[██████████████████████████████████████████                                             ] (X) | 60%/100% ");
        this.layout.add("[███████████████████████████████████████████████████                                    ] (|) | 65%/100% ");
        this.layout.add("[█████████████████████████████████████████████████████████                              ] (X) | 70%/100% ");
        this.layout.add("[████████████████████████████████████████████████████████████                           ] (-) | 75%/100% ");
        this.layout.add("[██████████████████████████████████████████████████████████████                         ] (X) | 78%/100% ");
        this.layout.add("[█████████████████████████████████████████████████████████████████                      ] (|) | 80%/100% ");
        this.layout.add("[████████████████████████████████████████████████████████████████████                   ] (X) | 84%/100% ");
        this.layout.add("[███████████████████████████████████████████████████████████████████████                ] (-) | 87%/100% ");
        this.layout.add("[██████████████████████████████████████████████████████████████████████████             ] (X) | 90%/100% ");
        this.layout.add("[█████████████████████████████████████████████████████████████████████████████          ] (|) | 95%/100% ");
        this.layout.add("[██████████████████████████████████████████████████████████████████████████████████     ] (X) | 99%/100% ");
        this.layout.add("[███████████████████████████████████████████████████████████████████████████████████████] (X) | 100%/100% ");
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
