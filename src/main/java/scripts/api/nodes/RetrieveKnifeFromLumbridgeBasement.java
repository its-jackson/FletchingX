package scripts.api.nodes;

import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.cache.BankCache;
import org.tribot.script.sdk.pricing.Pricing;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.GroundItem;
import scripts.FletchingXVariables;
import scripts.api.interfaces.Workable;
import scripts.api.works.Work;

import java.util.Optional;

/**
 * Purpose of class: Will actually retrieve a knife if the player does not have one.
 */

public class RetrieveKnifeFromLumbridgeBasement extends Node implements Workable {

    private final FletchingXVariables variables = FletchingXVariables.get();

    public RetrieveKnifeFromLumbridgeBasement(Work work) {
        super(work);
    }

    public RetrieveKnifeFromLumbridgeBasement() {
    }

    @Override
    public synchronized void execute() {
        log("Taking the knife off from table");
        getVariables().setState("Taking the knife off from table");

        // find the ground item knife
        Optional<GroundItem> knifeOptional = Query.groundItems()
                .idEquals(Knives.KNIFE.getKnifeID())
                .findFirst();

        if (knifeOptional.isPresent()) {
            GroundItem actualKnife = knifeOptional.get();

            boolean clickResult = interact(actualKnife, actualKnife.getActions().get(0));
            if (clickResult) {
                log("Clicked " + actualKnife.getName());
                getVariables().setState("Clicked " + actualKnife.getName());

            }

            boolean waitResult = Waiting.waitUntil(3000, this::inventoryContainsKnife);
            if (clickResult && waitResult) {
                log("Inventory contains " + actualKnife.getName());
                getVariables().setState("Inventory contains " + actualKnife.getName());
            }
        }
    }

    @Override
    public synchronized boolean validate() {
        if (!BankCache.isInitialized()) {
            return false;
        }

        return shouldGetKnifeLumbridgeBasement();
    }

    @Override
    public String name() {
        return "[Mission Control]";
    }

    private boolean shouldGetKnifeLumbridgeBasement() {
        int knifePrice = Pricing
                .lookupPrice(Knives.KNIFE.getKnifeID())
                .orElse(0);

        // is iron man or is member
        // bank is initialized
        // bank has no knife
        // bank has no coins or not enough coins to buy a knife
        // inventory has no knife
        // in lumbridge basement
        if (!bankCacheContainsKnife()) {
            if (!inventoryContainsKnife()) {
                return isAtLumbridgeBasement();
//                if (BankCache.getStack(COIN_ID) < knifePrice) {
//
//                }
            }
        }

        return false;
    }

    public FletchingXVariables getVariables() {
        return variables;
    }
}
