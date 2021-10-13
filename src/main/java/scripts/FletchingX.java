package scripts;

import dax.api_lib.models.RunescapeBank;
import org.apache.commons.lang3.time.StopWatch;
import org.tribot.script.sdk.Log;
import org.tribot.script.sdk.Tribot;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.script.ScriptConfig;
import org.tribot.script.sdk.script.TribotScript;
import org.tribot.script.sdk.script.TribotScriptManifest;
import org.tribot.script.sdk.walking.GlobalWalking;
import org.tribot.script.sdk.walking.adapter.DaxWalkerAdapter;
import scripts.api.enums.Resource;
import scripts.api.enums.ResourceOption;
import scripts.api.nodes.Banking;
import scripts.api.nodes.CutLog;
import scripts.api.nodes.Node;
import scripts.api.nodes.StringBow;
import scripts.api.time.TimeElapse;
import scripts.api.works.Cutting;
import scripts.api.works.Work;

import java.util.Collections;
import java.util.List;

@TribotScriptManifest(
        name = "Fletching X",
        author = "Polymorphic",
        category = "Fletching",
        description = "Fletching X - Written By Polymorphic")

public class FletchingX implements TribotScript {

    private final static StopWatch STOP_WATCH = new StopWatch();

    private final FletchingXVariables variables = FletchingXVariables.get();

    @Override
    public void configure(ScriptConfig config) {
        config.setBreakHandlerEnabled(true);
        config.setRandomsAndLoginHandlerEnabled(true);
    }

    @Override
    public void execute(String s) {
        // start stop watch
        if (STOP_WATCH.isStopped()) {
            STOP_WATCH.start();
        }
        // set global walking engine
        GlobalWalking.setEngine(new DaxWalkerAdapter("sub_JK3knXqxVGZtGR", "74aa47de-1cb1-4ee1-a8c9-5bae53c70b22"));
        // create test work
        Work cuttingWork = new Cutting(
                Resource.YEW_LONGBOW_UNSTRUNG,
                ResourceOption.SELL_TO_GRAND_EXCHANGE,
                RunescapeBank.GRAND_EXCHANGE
                 // -1 means until all supplies depleted
        );
        // add test work
        getVariables()
                .getSettings()
                .getWork()
                .add(cuttingWork);
        // set true start
        getVariables().setStart(true);
        // run script
        handleWork(getVariables().isStart());
    }

    private void handleWork(boolean start) {
        if (start) {
            do {
                // perform the work
                for (Work work : getVariables().getSettings().getWork()) {
                    Log.log(work);
                    // get nodes and clear
                    List<Node> nodes = getVariables().getNodes();
                    nodes.clear();
                    // get timer and reset start time
                    TimeElapse timer = work.getTime();
                    if (timer != null) {
                        timer.setStartTime(System.currentTimeMillis());
                    }
                    // initialize the nodes for the type of work polymorphic
                    if (work instanceof Cutting) {
                        // cutting
                        nodes.add(new CutLog(work));
                        nodes.add(new Banking(work));
                    } else {
                        // stringing
                        nodes.add(new StringBow(work));
                    }
                    // loop while the work is validated (reached time or level)
                    while (!work.validate()) {
                        // execute the valid node within the given work
                        nodes.stream()
                                .filter(Node::validate)
                                .findFirst()
                                .ifPresent(Node::execute);
                        Waiting.waitUniform(80, 120);
                    }
                    Log.log("Work complete. Please be patient.");
                }
                if (getVariables().getSettings().isRepeatShuffle()) {
                    Collections.shuffle(getVariables().getSettings().getWork());
                }
            }
            // keep repeating the script if necessary
            while (getVariables().getSettings().isRepeat() || getVariables().getSettings().isRepeatShuffle());
            // end script once all work is completed
            end();
        }
    }

    private void end() {
        getVariables().setStart(false);
        throw new RuntimeException("Script is over! Thanks for playing " + Tribot.getUsername());
    }

    public FletchingXVariables getVariables() {
        return variables;
    }
}
