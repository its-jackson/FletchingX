package scripts.api.nodes;

import dax.api_lib.models.RunescapeBank;
import org.tribot.script.sdk.GrandExchange;
import org.tribot.script.sdk.Inventory;
import org.tribot.script.sdk.MyPlayer;
import org.tribot.script.sdk.cache.BankCache;
import org.tribot.script.sdk.pricing.Pricing;
import scripts.FletchingXVariables;
import scripts.api.interfaces.Workable;
import scripts.api.works.Cutting;
import scripts.api.works.Alchemy;
import scripts.api.works.Stringing;
import scripts.api.works.Work;

/**
 * Purpose of class: Control the player's walking
 */

public class Walking extends Node implements Workable {

    private final FletchingXVariables variables = FletchingXVariables.get();

    private boolean walkToBankInitialize;

    private boolean walkToBankAndCut;
    private boolean walkToLumbridgeBasement;

    private boolean walkToBankAndString;

    private boolean walkToBankAndAlch;

    private boolean walkToGrandExchange;

    public Walking(Work work) {
        super(work);
    }

    public Walking() {
        super();
    }

    @Override
    public synchronized void execute() {
        if (isWalkToBankInitialize()) {
            log("Walking to bank initialization");
            getVariables().setState("Walking to bank initialization");
            boolean walkResult = walkToBank(getWork().getBankLocation());
            if (walkResult) {
                log("Walked to bank successfully");
                getVariables().setState("Walked to bank successfully");
            }
            setWalkToBankInitialize(false);
        }
        if (getWork() instanceof Cutting) {
            // do cutting
            if (isWalkToBankAndCut()) {
                log("Walking to bank cutting");
                getVariables().setState("Walking to bank cutting");
                boolean walkResult = walkToBank(getWork().getBankLocation());
                if (walkResult) {
                    log("Walked to bank successfully");
                    getVariables().setState("Walked to bank successfully");
                }
                setWalkToBankAndCut(false);
            } else if (isWalkToLumbridgeBasement()) {
                log("Walking to lumbridge basement for a knife");
                getVariables().setState("Walking to lumbridge basement for a knife");
                boolean walkResult = walkToBank(RunescapeBank.LUMBRIDGE_BASEMENT);
                if (walkResult) {
                    log("Walked to lumbridge basement successfully");
                    getVariables().setState("Walked to lumbridge basement successfully");
                }
                setWalkToLumbridgeBasement(false);
            } else if (isWalkToGrandExchange()) {
                log("Walking to grand exchange");
                getVariables().setState("Walking to grand exchange");
                boolean walkResult = walkToBank(RunescapeBank.GRAND_EXCHANGE);
                if (walkResult) {
                    log("Walked to grand exchange successfully");
                    getVariables().setState("Walked to grand exchange successfully");
                }
                setWalkToGrandExchange(false);
            }
        } else if (getWork() instanceof Stringing) {
            // do stringing
            if (isWalkToBankAndString()) {
                log("Walking to bank stringing");
                getVariables().setState("Walking to bank stringing");
                boolean walkResult = walkToBank(getWork().getBankLocation());
                if (walkResult) {
                    log("Walked to bank successfully");
                    getVariables().setState("Walked to bank successfully");
                }
            }
            setWalkToBankAndString(false);
        } else if (getWork() instanceof Alchemy) {
            // do magic
            if (isWalkToBankAndAlch()) {
                log("Walking to bank alchemy");
                getVariables().setState("Walking to bank alchemy");
                boolean walkResult = walkToBank(getWork().getBankLocation());
                if (walkResult) {
                    log("Walked to bank successfully");
                    getVariables().setState("Walked to bank successfully");
                }
            }
            setWalkToBankAndAlch(false);
        }
    }

    @Override
    public synchronized boolean validate() {
        if (getWork() instanceof Cutting) {
            return shouldWalkToBankAndCut(getWork()) ||
                    shouldWalkToLumbridgeBasement(getWork()) ||
//                    shouldWalkToGrandExchange(getWork()) ||
                    shouldWalkToBankInitialize(getWork());
        } else if (getWork() instanceof Stringing) {
            return shouldWalkToBankAndString(getWork()) ||
                    shouldWalkToBankInitialize(getWork());
        } else if (getWork() instanceof Alchemy) {
            return shouldWalkToBankAndHighLevelAlchemy(getWork()) ||
                    shouldWalkToBankInitialize(getWork());
        }

        return false;
    }

    @Override
    public String name() {
        return "[Walking Control]";
    }

    private boolean shouldWalkToBankInitialize(Work work) {
        // is not at bank
        // bank cache not initialized
        if (!isAtBank(work.getBankLocation()) && !BankCache.isInitialized()) {
            setWalkToBankInitialize(true);
            return isWalkToBankInitialize();
        }

        return false;
    }

    private boolean shouldWalkToBankAndCut(Work work) {
        // is not at bank
        // bank cache is initialized
        // bank cache contains knife
        // bank cache contains logs
        Cutting cuttingWork = (Cutting) work;

        if (!isAtBank(work.getBankLocation())) {
            if (BankCache.isInitialized()) {
                if (bankCacheContains(cuttingWork.getLogRequired().getLogID())) {
                    if (bankCacheContainsKnife() || inventoryContainsKnife()) {
                        setWalkToBankAndCut(true);
                        return isWalkToBankAndCut();
                    }
                }
            }
        }

        return false;
    }

    private boolean shouldWalkToBankAndString(Work work) {
        // is not at bank
        // bank cache is initialized
        // bank cache contains bow strings
        // bank cache contains unstrung bows
        Stringing stringingWork = (Stringing) work;

        if (!isAtBank(work.getBankLocation())) {
            if (BankCache.isInitialized()) {
                if (bankCacheContains(stringingWork.getBowString().getStringID())) {
                    if (bankCacheContains(stringingWork.getUnstrungBow().getResourceIDS()[0])) {
                        setWalkToBankAndString(true);
                        return isWalkToBankAndString();
                    }
                }
            }
        }

        return false;
    }

    private boolean shouldWalkToBankAndHighLevelAlchemy(Work work) {
        // is not at bank
        // bank cache initialzied
        // bank cache contains nature runes
        // bank cache contains fire staff
        // bank cache contains resource
        Alchemy alchemyWork = (Alchemy) work;

        if (!isAtBank(work.getBankLocation())) {
            if (BankCache.isInitialized()) {
                if (bankCacheContains(alchemyWork.getNatureRunes().getRuneID())) {
                    if (bankCacheContains(alchemyWork.getStaffOfFire().getStaffID())) {
                        if (bankCacheContains(alchemyWork.getResource().getResourceIDS()[0])) {
                            setWalkToBankAndAlch(true);
                            return isWalkToBankAndAlch();
                        }
                    }
                }
            }
        }

        return false;
    }

    private boolean shouldWalkToGrandExchange(Work work) {
        // is not iron man
        // bank initialized
        // bank has no knife
        // inventory has no knife
        // bank has enough gp to buy knife
        // not in grand exchange
        if (!MyPlayer.getAccountType().equals(MyPlayer.AccountType.NORMAL)) {
            return false;
        }

        int knifePrice = Pricing
                .lookupPrice(Knives.KNIFE.getKnifeID())
                .orElse(0);

        if (BankCache.isInitialized()) {
            if (!bankCacheContainsKnife()) {
                if (!inventoryContainsKnife()) {
                    if (Inventory.getCount(COIN_ID) > knifePrice) {
                        if (!GrandExchange.isNearby()) {
                            setWalkToGrandExchange(true);
                            return isWalkToGrandExchange();
                        }
                    }
                }
            }
        }

        return false;
    }

    private boolean shouldWalkToLumbridgeBasement(Work work) {
        int knifePrice = Pricing
                .lookupPrice(Knives.KNIFE.getKnifeID())
                .orElse(0);

        // is iron man or is member
        // bank is initialized
        // bank has no knife
        // bank has no coins or not enough coins to buy a knife
        // inventory has no knife
        // not in lumbridge basement
        if (BankCache.isInitialized()) {
            if (!bankCacheContainsKnife()) {
                if (!inventoryContainsKnife()) {
                    if (!isAtLumbridgeBasement()) {
                        setWalkToLumbridgeBasement(true);
                        return isWalkToLumbridgeBasement();
                    }
//                    if (BankCache.getStack(COIN_ID) < knifePrice) {
//
//                    }
                }
            }
        }

        return false;
    }

    public FletchingXVariables getVariables() {
        return variables;
    }

    public boolean isWalkToBankInitialize() {
        return walkToBankInitialize;
    }

    public void setWalkToBankInitialize(boolean walkToBankInitialize) {
        this.walkToBankInitialize = walkToBankInitialize;
    }

    public boolean isWalkToBankAndCut() {
        return walkToBankAndCut;
    }

    public void setWalkToBankAndCut(boolean walkToBankAndCut) {
        this.walkToBankAndCut = walkToBankAndCut;
    }

    public boolean isWalkToLumbridgeBasement() {
        return walkToLumbridgeBasement;
    }

    public void setWalkToLumbridgeBasement(boolean walkToLumbridgeBasement) {
        this.walkToLumbridgeBasement = walkToLumbridgeBasement;
    }

    public boolean isWalkToBankAndString() {
        return walkToBankAndString;
    }

    public void setWalkToBankAndString(boolean walkToBankAndString) {
        this.walkToBankAndString = walkToBankAndString;
    }

    public boolean isWalkToBankAndAlch() {
        return walkToBankAndAlch;
    }

    public void setWalkToBankAndAlch(boolean walkToBankAndAlch) {
        this.walkToBankAndAlch = walkToBankAndAlch;
    }

    public boolean isWalkToGrandExchange() {
        return walkToGrandExchange;
    }

    public void setWalkToGrandExchange(boolean walkToGrandExchange) {
        this.walkToGrandExchange = walkToGrandExchange;
    }
}
