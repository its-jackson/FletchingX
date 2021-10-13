package scripts.api.nodes;

import org.tribot.script.sdk.MakeScreen;
import org.tribot.script.sdk.MyPlayer;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.types.InventoryItem;
import scripts.api.interfaces.Workable;
import scripts.api.works.Cutting;
import scripts.api.works.Work;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Cuts the log into the desired resource
 */
public class CutLog extends Node implements Workable {

    public CutLog(Work work) {
        super(work);
    }

    public CutLog() {
        super();
    }

    @Override
    public synchronized void execute() {
        log("Utilizing knife");

        // find the logs
        List<InventoryItem> logs = ((Cutting) getWork()).findRequiredLogs();

        // find knife
        Optional<InventoryItem> knife = findKnife();

        // combine knife and logs
        knife.ifPresent(k -> combineItem(k, logs, "Use"));

        // wait until make interface is on screen
        boolean isMakeScreenOpen = Waiting.waitUntil(3000, MakeScreen::isOpen);

        // make the desired resource
        if (isMakeScreenOpen) {
            log("Make screen is open");
            //MakeScreen.setSelectPreference(MakeScreen.SelectPreference.KEYS);
            boolean makeResult = MakeScreen.makeAll(item -> {
                log("Item available to make: " + item.getName());
                log("Item to make: " + getWork().getResource().getResourceName());
                return getWork().getResource()
                        .getResourceName()
                        .toLowerCase(Locale.ROOT)
                        .contains(item.getName().toLowerCase(Locale.ROOT));
            });
            if (makeResult) {
                // wait until we are animating
                Waiting.waitUntil(3000, MyPlayer::isAnimating);
                // complete the fletching task
                completeFletchingTask(getWork());
            }
        }
    }

    @Override
    public synchronized boolean validate() {
        // has inventory logs
        // has inventory knife
        // at required the bank
        // contains the specified supplies or ran out of supplies or time surpassed or reached level
        return shouldCutLog(getWork()) && !getWork().validate();
    }

    @Override
    public String name() {
        return "[Cutting Control]";
    }

    private void completeFletchingTask(Work work) {
        String resourceName = work.getResource().getResourceName();

        while (MyPlayer.isAnimating()) {
            log("Making " + resourceName);
            Waiting.waitUniform(500, 1000);
        }
    }

    private boolean shouldCutLog(Work work) {
        if (((Cutting)work).inventoryContainsRequiredLogs()) {
            if (inventoryContainsKnife()) {
                return isAtBank(work.getBankLocation());
            }
        }
        return false;
    }
}
