package scripts;

import org.apache.commons.lang3.time.StopWatch;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.script.sdk.*;
import org.tribot.script.sdk.painting.Painting;
import org.tribot.script.sdk.pricing.Pricing;
import org.tribot.script.sdk.script.ScriptConfig;
import org.tribot.script.sdk.script.TribotScript;
import org.tribot.script.sdk.script.TribotScriptManifest;
import org.tribot.script.sdk.util.ScriptSettings;
import org.tribot.script.sdk.walking.GlobalWalking;
import org.tribot.script.sdk.walking.adapter.DaxWalkerAdapter;
import scripts.api.PolymorphicMousePaint;
import scripts.api.antiban.AntiBan;
import scripts.api.exceptions.ScriptCompleteException;
import scripts.api.gui.GUIFX;
import scripts.api.nodes.*;
import scripts.api.time.TimeElapse;
import scripts.api.utilities.PolymorphicScriptSettings;
import scripts.api.works.Cutting;
import scripts.api.works.Alchemy;
import scripts.api.works.Stringing;
import scripts.api.works.Work;
import scripts.api.works.Worker;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * To do:
 * 1) Fix profit gained / profit hr (Subtract the bow string or the log price)
 * 2) Add custom mouse paint
 * 3) Finish the GUI
 * 4) Add preference for depositing all
 */

@TribotScriptManifest(
        name = "Fletching X",
        author = "Polymorphic",
        category = "Fletching",
        description = "Fletching X - Written By Polymorphic")

public class FletchingX implements TribotScript {

    private final static StopWatch stop_watch = new StopWatch();

    private final FletchingXVariables variables = FletchingXVariables.get();
    private final Worker worker = Worker.get();

    @Override
    public void configure(ScriptConfig config) {
        config.setBreakHandlerEnabled(true);
        config.setRandomsAndLoginHandlerEnabled(true);
    }

    @Override
    public void execute(String s) {
        if (parseArgument(s)) {
            // parse args
            getVariables().setStart(true);
        } else {
            // load gui, cannot parse args
            loadGraphicalUserInterface();
        }

        // start stop watch
        if (stop_watch.isStopped()) {
            stop_watch.start();
        }

        // execute work
        handleWork(getVariables().isStart());
    }

    private void handleWork(boolean start) {
        if (start) {
            setup();
            waitUntilInGame();
            setWorker();
            do {
                // perform the work
                for (Work work : getVariables().getSettings().getWork()) {
                    Log.log(work);

                    // set current work
                    getVariables().setCurrentWork(work);

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
                        nodes.add(new Fletching(work));
                        nodes.add(new Banking(work));
                        nodes.add(new Walking(work));
                        //nodes.add(new BuyingSellingGrandExchange(work));
                        nodes.add(new RetrieveKnifeFromLumbridgeBasement(work));
                        nodes.add(new WorldHop(work));
                    } else if (work instanceof Stringing) {
                        // stringing
                        nodes.add(new Fletching(work));
                        nodes.add(new Banking(work));
                        nodes.add(new Walking(work));
                        nodes.add(new WorldHop(work));
                    } else if (work instanceof Alchemy) {
                        // alchemy
                        nodes.add(new HighLevelAlchemy(work));
                        nodes.add(new Banking(work));
                        nodes.add(new Walking(work));
                        nodes.add(new WorldHop(work));
                    }

                    // loop while the work is validated
                    while (!work.validate()) {
                        // sleep
                        Log.log(String.format("Reaction time: %s milliseconds", AntiBan.sleep(getVariables().getWaitTimes())));

                        // execute the valid node within the given work
                        nodes.stream()
                                .filter(Node::validate)
                                .findFirst()
                                .ifPresent(Node::execute);
                    }

                    Log.log("Work complete, please remain patient " + Worker.getUserName());
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

    private void loadGraphicalUserInterface() {
        // load fxml
        try {
            getVariables().setFxml(new URL("https://jacksonjohnson.ca/fletchingx/fletchingx.fxml"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // set gui
        getVariables().setGui(new GUIFX(getVariables().getFxml()));
        getVariables().getGui().show();

        while (getVariables().getGui().isOpen()) {
            Waiting.wait(500);
        }
    }

    private void setup() {
        // set anti-ban variables
        AntiBan.create();
        AntiBan.setMicroSleep(getVariables().getSettings().isMicroSleep());
        AntiBan.setHumanFatigue(getVariables().getSettings().isFatigue());
        AntiBan.setPrintDebug(true);
        General.useAntiBanCompliance(true);

//        Log.log("ABC2: " + General.useAntiBanCompliance());
//        Log.log("Micro sleep: " + AntiBan.getMicroSleep());
//        Log.log("Fatigue: " + AntiBan.getHumanFatigue());
//        Log.log("Print Debug: " + AntiBan.getPrintDebug());

        // set global walking engine
        GlobalWalking.setEngine(new DaxWalkerAdapter("sub_JK3knXqxVGZtGR", "74aa47de-1cb1-4ee1-a8c9-5bae53c70b22"));

        // on end listener
        ScriptListening.addEndingListener(this::end);

        // server message listener
        MessageListening.addServerMessageListener(message -> {
            message = message.toLowerCase(Locale.ROOT);

            if (message.contains("you carefully cut")) {
                int resourcePrice = Pricing
                        .lookupPrice(getVariables().getCurrentWork().getResource().getResourceIDS()[0])
                        .orElse(0);
                int logPrice = Pricing
                        .lookupPrice(((Cutting) getVariables().getCurrentWork()).getLogRequired().getLogID())
                        .orElse(0);
                // increment resource count
                getWorker().incrementResourceCount();
                // decrement supplies to make
                getVariables().getCurrentWork().decrementSuppliesToMake();
                // update total profit
                getWorker().setTotalProfit((int) (getWorker().getResourceCount() * resourcePrice - logPrice));
            }

            if (message.contains("you add a string")) {
                int resourcePrice = Pricing
                        .lookupPrice(getVariables().getCurrentWork().getResource().getResourceIDS()[0])
                        .orElse(0);
                int bowStringPrice = Pricing
                        .lookupPrice(((Stringing) getVariables().getCurrentWork()).getBowString().getStringID())
                        .orElse(0);
                int unStrungBowPrice = Pricing
                        .lookupPrice(((Stringing) getVariables().getCurrentWork()).getUnstrungBow().getResourceIDS()[0])
                        .orElse(0);
                // increment resource count
                getWorker().incrementResourceCount();
                // decrement supplies to make
                getVariables().getCurrentWork().decrementSuppliesToMake();
                // update total profit
                getWorker().setTotalProfit((int) (getWorker().getResourceCount() * resourcePrice - unStrungBowPrice - bowStringPrice));
            }

            if (message.contains("congratulations")) {
                getWorker().incrementLevelCount();
            }
        });

        // mouse paint
        Painting.setMousePaint(new PolymorphicMousePaint(Color.RED, Color.RED, 24));

        // main script paint
        Painting.addPaint(g -> {
            g.setRenderingHints(getVariables().getAa());

            int factor = 3600000;

            int actualFletchingLevel = Skill.FLETCHING.getActualLevel();
            int actualMagicLevel = Skill.MAGIC.getActualLevel();

            int levelsGained = getWorker().getLevelCount();

            long resourceGained = getWorker().getResourceCount();
            long resourcesPerHour = (long) (resourceGained * (factor / (double) stop_watch.getTime()));

            int fletchingXPGained = getWorker().calculateFletchingExperienceGained();
            long fletchingXPPerHour = (long) (fletchingXPGained * (factor / (double) stop_watch.getTime()));
            int magicXPGained = getWorker().calculateMagicExperienceGained();
            long magicXPPerHour = (long) (magicXPGained * (factor / (double) stop_watch.getTime()));

            long profitGained = getWorker().getTotalProfit();
            long profitPerHour = (long) (profitGained * (factor / (double) stop_watch.getTime()));

            int percentToNextLevelFletching = Skill.FLETCHING.getXpPercentToNextLevel();
            int percentToNextLevelMagic = Skill.MAGIC.getXpPercentToNextLevel();

            String state = getVariables().getState();

            g.drawImage(getVariables().getImg(), -1, 313, null);
            g.setColor(getVariables().getPaint_main_colour());
            g.setFont(getVariables().getFont());
            g.drawString(String.format("Time Running: %s", Timing.msToString(stop_watch.getTime())), 15, 360); // runtime

            if (getVariables().getCurrentWork() instanceof Cutting || getVariables().getCurrentWork() instanceof Stringing) {
                g.drawString(String.format("XP Gained: %,d XP", fletchingXPGained), 15, 375); // gained xp
                g.drawString(String.format("XP/Hour: %,d XP/HR", fletchingXPPerHour), 15, 390); // gained xp
            } else {
                g.drawString(String.format("XP Gained: %,d XP", magicXPGained), 15, 375); // gained xp
                g.drawString(String.format("XP/Hour: %,d XP/HR", magicXPPerHour), 15, 390); // gained xp
            }

            g.drawString(String.format("Status: %s", state), 15, 405); // script state
            g.drawString(String.format("Profit Gained: %,d GP", profitGained), 105, 420); // profit
            g.drawString(String.format("Profit/Hour: %,d GP/HR", profitPerHour), 105, 435); // profit hr
            g.drawString(String.format("Resources Gained: %,d Resources", resourceGained), 105, 450); // log count
            g.drawString(String.format("Resources/Hour: %,d Resources/HR", resourcesPerHour), 105, 465); // logs hr
            g.setFont(getVariables().getMain_font_secondary());

            if (getVariables().getCurrentWork() instanceof Cutting || getVariables().getCurrentWork() instanceof Stringing) {
                g.drawString(String.format("Currently level %d, %d Level's Gained.", actualFletchingLevel, levelsGained), 215, 390);
            } else {
                g.drawString(String.format("Currently level %d, %d Level's Gained.", actualMagicLevel, levelsGained), 215, 390);
            }

            // progress bar
            g.setColor(Color.BLACK);
            g.drawRect(20, 290, 475, 12);
            g.setColor(new Color(150, 0, 0)); // red
            g.fillRect(20, 290, 475, 12);
            g.setColor(new Color(0, 110, 40)); // green

            if (getVariables().getCurrentWork() instanceof Cutting || getVariables().getCurrentWork() instanceof Stringing) {
                g.fillRect(20, 290, percentToNextLevelFletching * 475 / 100, 12);
                g.setColor(getVariables().getPaint_main_colour());
                g.drawString(String.format("%d%% TO NEXT LEVEL", percentToNextLevelFletching), 210, 300);
            } else {
                g.fillRect(20, 290, percentToNextLevelMagic * 475 / 100, 12);
                g.setColor(getVariables().getPaint_main_colour());
                g.drawString(String.format("%d%% TO NEXT LEVEL", percentToNextLevelMagic), 210, 300);
            }

            // fatigue bar
            if (AntiBan.getHumanFatigue()) {
                int percentFatigueLevel = (int) (AntiBan.getFatigueMultiple() * 100);
                g.setColor(Color.BLACK);
                g.drawRect(20, 275, 475, 12);
                g.setColor(new Color(150, 0, 0)); // red
                g.fillRect(20, 275, 475, 12);
                g.setColor(new Color(0, 0, 148)); // blue
                g.fillRect(20, 275, percentFatigueLevel * 475 / 100, 12);
                g.setColor(getVariables().getPaint_main_colour());
                g.drawString(String.format("%d%% FATIGUE LEVEL", percentFatigueLevel), 210, 284);
            }
        });
    }

    private void end() {
        Log.log(String.format("End of script statistics for: %s", Worker.getUserName()));
        Log.log(String.format("Total Time Ran: %s", Timing.msToString(stop_watch.getTime())));
        Log.log(String.format("Total Resources Made: %s", getWorker().getResourceCount()));
        Log.log(String.format("Total Profit Made: %,d GP", getWorker().getTotalProfit()));
        Log.log(String.format("Total Fletching Experience Gained: %,d XP", getWorker().calculateFletchingExperienceGained()));
        Log.log(String.format("Total Magic Experience Gained: %,d XP", getWorker().calculateMagicExperienceGained()));
        Log.log(String.format("Total Level's Gained: %d Levels", getWorker().getLevelCount()));
        Log.log(String.format("Until next time %s, take care.", Worker.getUserName()));
        throw new ScriptCompleteException("", new Error(), false, false);
    }

    private boolean parseArgument(String arg) {
        if (arg.isBlank() || arg.isEmpty()) {
            return false;
        }

        ScriptSettings settingsHandler = new PolymorphicScriptSettings()
                .getSettings();

        Optional<FletchingXSettings> settingsOptional = settingsHandler.load(arg, FletchingXSettings.class);

        if (settingsOptional.isPresent()) {
            Log.log(String.format("Loaded file: %s", arg));
            getVariables().setSettings(settingsOptional.get());
            Log.log(String.format("Loaded settings successfully: %s", getVariables().getSettings()));
            return true;
        } else {
            Log.log("Incorrect argument parsed. You must create settings via GUI first.");
            return false;
        }
    }

    private void waitUntilInGame() {
        while (!GameState.getState().equals(GameState.State.LOGGED_IN)) {
            Waiting.wait(500);
        }
    }

    private void setWorker() {
        if (GameState.getState().equals(GameState.State.LOGGED_IN)) {
            getWorker().setFletchingStartExperience(Skill.FLETCHING.getXp());
            getWorker().setMagicStartExperience(Skill.MAGIC.getXp());
        }
    }

    public FletchingXVariables getVariables() {
        return variables;
    }

    public Worker getWorker() {
        return worker;
    }
}
