package scripts.api.nodes;

import org.tribot.script.sdk.*;
import org.tribot.script.sdk.antiban.PlayerPreferences;
import org.tribot.script.sdk.cache.BankCache;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.InventoryItem;
import scripts.FletchingXVariables;
import scripts.api.antiban.AntiBan;
import scripts.api.interfaces.Workable;
import scripts.api.works.Cutting;
import scripts.api.works.Stringing;
import scripts.api.works.Work;

import java.security.SecureRandom;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Purpose of class: Fletch (make) the desired resource that is in the current work object
 */

public class Fletching extends Node implements Workable {

    private final FletchingXVariables variables = FletchingXVariables.get();

    private Node worldHopNode;

    public Fletching(Work work) {
        super(work);
    }

    public Fletching() {
        super();
    }

    @Override
    public synchronized void execute() {
        final long startTime = System.currentTimeMillis();

        closeAllOpenWidgets();

        final int player_pref_make_screen =
                PlayerPreferences.preference("org.tribot.script.sdk.MakeScreen.makeAll", g -> g.normal(
                                1,
                                100,
                                30,
                                60
                        )
                );
        final int player_pref_use_on =
                PlayerPreferences.preference("org.tribot.script.sdk.types.InventoryItem.useOn", g -> g.normal(
                                1,
                                100,
                                50,
                                70
                        )
                );

        if (getWork() instanceof Cutting) {
            // perform cutting work
            log("Utilizing " + Knives.KNIFE.getKnifeName());
            getVariables().setState("Utilizing " + Knives.KNIFE.getKnifeName());

            // find the logs
            List<InventoryItem> logs = ((Cutting) getWork()).findRequiredLogs();

            // find knife
            Optional<InventoryItem> knife = findInventoryKnife();

            // combine knife and logs
            knife.ifPresent(k -> combineItem(k, logs, player_pref_use_on));
        } else {
            // perform stringing work
            log("Utilizing " + Strings.BOW_STRING.getStringName());
            getVariables().setState("Utilizing " + Strings.BOW_STRING.getStringName());

            // find the unstrung bows
            List<InventoryItem> bows = ((Stringing) getWork()).findUnstrungBows();

            // find the bow string
            List<InventoryItem> bowStrings = ((Stringing) getWork()).findBowStrings();

            // combine bowstring and bows
            bowStrings.stream()
                    .findAny()
                    .ifPresent(s -> combineItem(s, bows, player_pref_use_on));
        }

        // wait until make interface is on screen
        boolean isMakeScreenOpen = Waiting.waitUntil(3000, MakeScreen::isOpen);

        // make the desired resource
        if (isMakeScreenOpen) {
            log("Make screen is open");
            getVariables().setState("Make screen is open");

            boolean makeResult = make(player_pref_make_screen, getWork());

            if (makeResult) {
                // wait until we are animating
                Waiting.waitUntil(3000, MyPlayer::isAnimating);
                // complete the fletching task
                completeFletchingTask(getWork());
            }
        }

        // Generate the trackers
        AntiBan.generateTrackers((int) (System.currentTimeMillis() - startTime), false);
    }

    @Override
    public synchronized boolean validate() {
        if (!BankCache.isInitialized()) {
            return false;
        }

        if (isAtBank(getWork().getBankLocation())) {
            if (getWork() instanceof Cutting) {
                return shouldCutLog(getWork());
            } else {
                return shouldStringBow(getWork());
            }
        }

        return false;
    }

    @Override
    public String name() {
        return "[Fletching Control]";
    }

    private boolean make(int preference, Work work) {
        if (preference > 50) {
            MakeScreen.setSelectPreference(MakeScreen.SelectPreference.MOUSE);
        } else {
            MakeScreen.setSelectPreference(MakeScreen.SelectPreference.KEYS);
        }

        return MakeScreen.makeAll(item -> {
            String itemAvailable = item
                    .getName()
                    .toLowerCase(Locale.ROOT);

            String itemToMake = work
                    .getResource()
                    .getResourceName()
                    .toLowerCase(Locale.ROOT);

//            log("Item available to make: " + itemAvailable);
//            log("Item to make: " + itemToMake);

            return itemToMake.contains(itemAvailable);
        });
    }

    /**
     * Complete the fletching task; catch the loop after the player clicks "make"
     * Keep track of the resources actually made.
     *
     * @param work The work that is generated to be manipulated
     */
    private void completeFletchingTask(Work work) {
        String resourceName = work
                .getResource()
                .getResourceName();

        // set world hop node
        getVariables()
                .getNodes()
                .stream()
                .filter(node -> node instanceof WorldHop)
                .findFirst()
                .ifPresent(this::setWorldHopNode);

        if (work instanceof Cutting) {
            while (MyPlayer.isAnimating()) {
                miniWork(resourceName, work, getWorldHopNode());
            }
        } else {
            // downcast
            Stringing stringingWork = (Stringing) work;

            // time out in case of infinite loop
            final long time_out = System.currentTimeMillis() + new SecureRandom()
                    .longs(15000, 20000)
                    .findAny()
                    .orElse(15000);

            while (stringingWork.inventoryContainsUnstrungBow() && System.currentTimeMillis() < time_out) {
                miniWork(resourceName, work, getWorldHopNode());
            }
        }
    }

    private void miniWork(String resourceName, Work work, Node node) {
        log("Making " + resourceName);
        getVariables().setState("Making " + resourceName);
        log(work.getResource().getResourceName() + " remaining: " + work.getSuppliesToMake());

        if (node != null) {
            // downcast for fun
            WorldHop worldHop = (WorldHop) node;
            if (worldHop.validate()) {
                worldHop.execute();
            }
        }

        AntiBan.checkAntiBanTask(Query.gameObjects()
                .findRandom()
                .orElse(null));

        Waiting.waitUniform(1000, 1500);
    }

    /**
     * Determine when the player should cut the logs.
     *
     * @param work The work generated by the player to be manipulated upon.
     * @return True if the player contains the required logs, knife, and is at bank; false otherwise.
     */
    private boolean shouldCutLog(Work work) {
        Cutting cuttingWork = (Cutting) work;

        boolean isShield = cuttingWork
                .getResource()
                .getResourceName()
                .contains("shield")
                ;

        if (cuttingWork.inventoryContainsRequiredLogs()) {
            if (inventoryContainsKnife()) {
                if (isShield) {
                    int inventoryCount = cuttingWork
                            .findRequiredLogs()
                            .size()
                            ;
                    return inventoryCount > 1;
                }
                return true;
            }
        }

        return false;
    }

    /**
     * Determine when the player should string the bows.
     *
     * @param work The work generated by the player to be manipulated upon.
     * @return True if the player contains the required unstrung bows, bow strings, and is at the bank; false otherwise.
     */
    private boolean shouldStringBow(Work work) {
        if (((Stringing) work).inventoryContainsUnstrungBow()) {
            return ((Stringing) work).inventoryContainsBowStrings();
        }

        return false;
    }

    public FletchingXVariables getVariables() {
        return variables;
    }

    public Node getWorldHopNode() {
        return worldHopNode;
    }

    public void setWorldHopNode(Node worldHopNode) {
        this.worldHopNode = worldHopNode;
    }
}
