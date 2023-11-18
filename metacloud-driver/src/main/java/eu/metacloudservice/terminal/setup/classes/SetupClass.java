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

package eu.metacloudservice.terminal.setup.classes;

import java.util.HashMap;
import java.util.List;

public abstract class SetupClass {

    private Integer step = 0;
    private final HashMap<String, Object> answers = new HashMap<>();

    public abstract void call(String line);

    public abstract List<String> tabComplete();

    public HashMap<String, Object> getAnswers() {
        return answers;
    }

    public Integer getStep() {
        return step;
    }

    public void addStep(){
        step++;
    }
}
