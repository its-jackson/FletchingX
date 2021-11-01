package scripts.api.nodes;

import org.tribot.script.sdk.*;
import org.tribot.script.sdk.antiban.PlayerPreferences;
import org.tribot.script.sdk.cache.BankCache;
import scripts.FletchingXVariables;
import scripts.api.interfaces.Workable;
import scripts.api.works.Cutting;
import scripts.api.works.Alchemy;
import scripts.api.works.Stringing;
import scripts.api.works.Work;

/**
 * Purpose of class: Control the player's banking
 */

public class Banking extends Node implements Workable {

    private final FletchingXVariables variables = FletchingXVariables.get();

    // bank cache initialization variable
    private boolean initializeBankCache;

    // cutting variables
    private boolean depositCutting;
    private boolean withdrawLogs;
    private boolean withdrawKnife;

    // stringing variables
    private boolean depositStringing;
    private boolean withdrawUnstrungBows;
    private boolean withdrawBowStrings;

    // magic variables
    private boolean withdrawStaffOfFire;
    private boolean withdrawNatureRunes;
    private boolean withdrawHighLevelAlchemyResource;

    // grand exchange coins
    private boolean withdrawCoinsKnife;
    private boolean withdrawCoinsLogs;

    public Banking(Work work) {
        super(work);
    }

    public Banking() {
        super();
    }

    @Override
    public synchronized void execute() {
        // 40 mean because we want the average to be around that
        // 80 standard deviation we want the preferences to be spread out
        final int player_pref =
                PlayerPreferences.preference("org.tribot.script.sdk.Bank", g -> g.normal(
                                1,
                                100,
                                40,
                                80
                        )
                );

        //log("Player preference = " + player_pref);

        // open bank
        if (!Bank.isOpen()) {
            //Widgets.closeAll();
            if (openBank(player_pref)) {
                log("Opened bank");
                getVariables().setState("Opened bank");
            }
        }

        if (Bank.isOpen()) {
            // initialize bank cache
            if (isInitializeBankCache()) {
                log("Bank cache initialized");
                getVariables().setState("Bank cache initialized");
                if (Bank.depositInventory()) {
                    BankCache.update();
                    log("Cleaned inventory");
                    getVariables().setState("Cleaned inventory");
                }
                setInitializeBankCache(false);
            } else {
                if (getWork() instanceof Cutting) {
                    if (isDepositCutting()) {
                        // deposit all except knife
                        int depositCount = depositAllExceptKnife();
                        boolean depositAllExceptResult = Waiting.waitUntil(3000, () -> !getWork().inventoryContainsResources());
                        if (depositAllExceptResult && depositCount > 0) {
                            log("Deposited all except knife");
                            getVariables().setState("Deposited all except knife");
                        }
                        setDepositCutting(false);
                    } else if (isWithdrawLogs()) {
                        // withdraw logs
                        boolean withdrawLogsResult = withdrawLogs(getWork());
                        if (withdrawLogsResult) {
                            log("Withdrew " + ((Cutting) getWork()).getLogRequired().getLogName());
                            getVariables().setState("Withdrew " + ((Cutting) getWork()).getLogRequired().getLogName());
                            boolean closeBankResult = closeBank(player_pref);
                            if (closeBankResult) {
                                log("Closed bank");
                                getVariables().setState("Closed bank");
                            }
                        }
                        setWithdrawLogs(false);
                    } else if (isWithdrawKnife()) {
                        // withdraw knife
                        boolean withdrawKnifeResult = withdrawKnife();
                        if (withdrawKnifeResult) {
                            log("Withdrew knife");
                            getVariables().setState("Withdrew knife");
                        }
                        setWithdrawKnife(false);
                    } else if (isWithdrawCoinsKnife()) {
                        log("Withdrawing coins");
                        getVariables().setState("Withdrawing coins");
                        boolean withdrawCoinsResult = Bank.withdrawAll("Coins");
                        if (withdrawCoinsResult) {
                            log("Withdrew all coins");
                            getVariables().setState("Withdrew all coins");
                        }
                        boolean waitResult = Waiting.waitUntil(3000, this::inventoryContainsCoins);
                        if (waitResult) {
                            log("Inventory contains coins");
                            getVariables().setState("Inventory contains coins");
                        }
                        setWithdrawCoinsKnife(false);
                    }
                } else if (getWork() instanceof Stringing) {
                    // do stringing work
                    // initialize bank cache
                    if (isDepositStringing()) {
                        // deposit all stringing
                        boolean depositResult = Bank.depositInventory();
                        boolean waitResult = Waiting.waitUntil(3000, Inventory::isEmpty);
                        if (depositResult && waitResult) {
                            log("Deposited inventory");
                            getVariables().setState("Deposited inventory");
                        }
                        setDepositStringing(false);
                    } else if (isWithdrawUnstrungBows()) {
                        // withdraw unstrung bows
                        boolean withdrawResult = withdrawUnstrungBows(getWork());
                        if (withdrawResult) {
                            log("Withdrew " + ((Stringing) getWork()).getUnstrungBow().getResourceName());
                            getVariables().setState("Withdrew " + ((Stringing) getWork()).getUnstrungBow().getResourceName());
                        }
                        setWithdrawUnstrungBows(false);
                    } else if (isWithdrawBowStrings()) {
                        // withdraw bow strings
                        boolean withdrawResult = withdrawBowStrings(getWork());
                        if (withdrawResult) {
                            log("Withdrew " + ((Stringing) getWork()).getBowString().getStringName());
                            getVariables().setState("Withdrew " + ((Stringing) getWork()).getBowString().getStringName());
                            boolean closeBankResult = closeBank(player_pref);
                            if (closeBankResult) {
                                log("Closed bank");
                                getVariables().setState("Closed bank");
                            }
                        }
                        setWithdrawBowStrings(false);
                    }
                } else if (getWork() instanceof Alchemy) {
                    // do magic
                    if (isWithdrawStaffOfFire()) {
                        boolean withdrawStaffOfFireResult = withdrawStaffOfFire(getWork());
                        if (withdrawStaffOfFireResult) {
                            log("Withdrew " + Staffs.FIRE_STAFF.getStaffName());
                            getVariables().setState("Withdrew " + Staffs.FIRE_STAFF.getStaffName());
                        }
                        if (Equipment.equip(Staffs.FIRE_STAFF.getStaffID())) {
                            log("Equipped " + Staffs.FIRE_STAFF.getStaffName());
                            getVariables().setState("Equipped " + Staffs.FIRE_STAFF.getStaffName());
                        }
                        setWithdrawStaffOfFire(false);
                    } else if (isWithdrawNatureRunes()) {
                        boolean withdrawNatureRunesResult = withdrawNatureRunes(getWork());
                        if (withdrawNatureRunesResult) {
                            log("Withdrew " + Runes.NATURE_RUNE.getRuneName());
                            getVariables().setState("Withdrew " + Runes.NATURE_RUNE.getRuneName());
                        }
                        setWithdrawNatureRunes(false);
                    } else if (isWithdrawHighLevelAlchemyResource()) {
                        boolean withdrawAlchemyResourceResult = withdrawHighLevelAlchemyResources(getWork());
                        if (withdrawAlchemyResourceResult) {
                            log("Withdrew " + getWork().getResource().getResourceName());
                            getVariables().setState("Withdrew " + getWork().getResource().getResourceName());
                        }
                        setWithdrawHighLevelAlchemyResource(false);
                    }
                }
            }
        }
    }

    @Override
    public synchronized boolean validate() {
        if (isAtBank(getWork().getBankLocation()) && BankCache.isInitialized()) {
            if (getWork() instanceof Cutting) {
                // do cutting
                return shouldDepositResourcesCutting(getWork()) ||
                        shouldWithdrawLogs(getWork()) ||
                        shouldWithdrawKnife(getWork());
//                        shouldWithdrawCoinsKnife(getWork());
            } else if (getWork() instanceof Stringing) {
                // do stringing
                return shouldDepositResourcesStringing(getWork()) ||
                        shouldWithdrawUnstrungBows(getWork()) ||
                        shouldWithdrawBowStrings(getWork());
            } else if (getWork() instanceof Alchemy) {
                // do magic
                return shouldWithdrawStaffOfFire(getWork()) ||
                        shouldWithdrawNatureRunes(getWork()) ||
                        shouldWithdrawHighLevelAlchemyResource(getWork());
            }
        }

        // not at bank or bank cache not initialized - return should initialize bank cache
        return shouldInitalizeBankCache(getWork());
    }

    @Override
    public String name() {
        return "[Banking Control]";
    }

    // is at bank
    // bank cache not initialized
    private boolean shouldInitalizeBankCache(Work work) {
        if (isAtBank(work.getBankLocation())) {
            if (!BankCache.isInitialized()) {
                setInitializeBankCache(true);
                return isInitializeBankCache();
            }
        }

        return false;
    }

    // is at bank
    // bank cache is initialized
    // inventory contains knife
    // inventory contains resource made
    // inventory does not contain logs
    private boolean shouldDepositResourcesCutting(Work work) {
        Cutting cuttingWork = (Cutting) work;

        boolean isShield = cuttingWork
                .getResource()
                .getResourceName()
                .contains("shield");

        if (inventoryContainsKnife()) {
            if (work.inventoryContainsResources()) {
                if (isShield) {
                    int inventoryCount = cuttingWork
                            .findRequiredLogs()
                            .size();
                    if (inventoryCount < 2) {
                        setDepositCutting(true);
                        return isDepositCutting();
                    }
                } else {
                    if (!((Cutting) work).inventoryContainsRequiredLogs()) {
                        setDepositCutting(true);
                        return isDepositCutting();
                    }
                }
            }
        }

        return false;
    }

    // is at bank
    // bank cache is initialized
    // inventory contains resource made
    // inventory does not contain unstrung bows
    // inventory does not contain bow strings
    private boolean shouldDepositResourcesStringing(Work work) {
        if (work.inventoryContainsResources()) {
            if (!((Stringing) work).inventoryContainsUnstrungBow()) {
                if (!((Stringing) work).inventoryContainsBowStrings()) {
                    setDepositStringing(true);
                    return isDepositStringing();
                }
            } else {
                // inventory contains resource made
                // inventory contains unstrung bows
                // inventory does not contain bow strings
                if (!((Stringing) work).inventoryContainsBowStrings()) {
                    setDepositStringing(true);
                    return isDepositStringing();
                }
            }
        }


        return false;
    }

    // is at bank
    // bank cache is initialized
    // bank cache has required logs
    // inventory contains knife
    // inventory does not contain logs
    // inventory does not contain resources made
    private boolean shouldWithdrawLogs(Work work) {
        Cutting cuttingWork = (Cutting) work;

        if (bankCacheContains(cuttingWork.getLogRequired().getLogID())) {
            if (inventoryContainsKnife()) {
                if (!cuttingWork.inventoryContainsRequiredLogs()) {
                    if (!work.inventoryContainsResources()) {
                        setWithdrawLogs(true);
                        return isWithdrawLogs();
                    }
                }
            }
        }

        return false;
    }

    // is at bank
    // bank cache is initialized
    // bank cache has required unstrung bows
    // inventory does not contain unstrung bows
    // inventory does not contain resources made
    private boolean shouldWithdrawUnstrungBows(Work work) {
        Stringing stringingWork = (Stringing) work;

        if (bankCacheContains(stringingWork.getUnstrungBow().getResourceIDS()[0])) {
            if (!stringingWork.inventoryContainsUnstrungBow()) {
                if (!work.inventoryContainsResources()) {
                    setWithdrawUnstrungBows(true);
                    return isWithdrawUnstrungBows();
                }
            }
        }

        return false;
    }

    // is at bank
    // bank cache is initialized
    // bank cache has bow strings
    // inventory contains unstrung bows
    // inventory does not contain resources made
    private boolean shouldWithdrawBowStrings(Work work) {
        Stringing stringingWork = (Stringing) work;

        if (bankCacheContains(stringingWork.getBowString().getStringID())) {
            if (stringingWork.inventoryContainsUnstrungBow()) {
                if (!work.inventoryContainsResources()) {
                    setWithdrawBowStrings(true);
                    return isWithdrawBowStrings();
                }
            }
        }

        return false;
    }

    // is at the bank
    // bank is initialized
    // inventory has no knife
    // bank cache contains knife
    private boolean shouldWithdrawKnife(Work work) {
        if (!inventoryContainsKnife()) {
            if (bankCacheContainsKnife()) {
                setWithdrawKnife(true);
                return isWithdrawKnife();
            }
        }

        return false;
    }

    private boolean shouldWithdrawCoinsKnife(Work work) {
        if (!inventoryContainsKnife()) {
            if (!bankCacheContainsKnife()) {
                if (!inventoryContainsCoins()) {
                    setWithdrawCoinsKnife(true);
                    return isWithdrawCoinsKnife();
                }
            }
        }

        return false;
    }

    private boolean shouldWithdrawCoinsLogs(Work work) {
        if (!inventoryContainsKnife()) {
            if (!bankCacheContainsKnife()) {
                if (!inventoryContainsCoins()) {
                    setWithdrawCoinsKnife(true);
                    return isWithdrawCoinsKnife();
                }
            }
        }

        return false;
    }

    // is at the bank
    // bank is initialized
    // equipment has no staff of fire
    // inventory has no staff of fire
    // bank cache contains staff of fire
    private boolean shouldWithdrawStaffOfFire(Work work) {
        Alchemy alchemyWork = (Alchemy) getWork();

        if (!alchemyWork.equipmentContainsStaffOfFire() && !alchemyWork.inventoryContainsStaffOfFire()) {
            if (bankCacheContains(Staffs.FIRE_STAFF.getStaffID())) {
                setWithdrawStaffOfFire(true);
                return isWithdrawStaffOfFire();
            }
        }

        return false;
    }

    // is at bank
    // bank initialized
    // inventory contains no nature runes
    // bank cache contains nature runes
    private boolean shouldWithdrawNatureRunes(Work work) {
        Alchemy alchemyWork = (Alchemy) getWork();

        if (!alchemyWork.inventoryContainsNatureRunes()) {
            if (bankCacheContains(Runes.NATURE_RUNE.getRuneID())) {
                setWithdrawNatureRunes(true);
                return isWithdrawNatureRunes();
            }
        }

        return false;
    }

    // is at bank
    // bank initialized
    // inventory contains no alchemy resource
    // bank cache contains alchemy resource
    private boolean shouldWithdrawHighLevelAlchemyResource(Work work) {
        Alchemy alchemyWork = (Alchemy) work;

        if (!alchemyWork.inventoryContainsResources()) {
            // index 0 = resource id not noted
            if (bankCacheContains(alchemyWork.getResource().getResourceIDS()[0])) {
                setWithdrawHighLevelAlchemyResource(true);
                return isWithdrawHighLevelAlchemyResource();
            }
        }

        return false;
    }

    private boolean withdrawKnife() {
        boolean depositResult = Bank.depositInventory();
        boolean withdrawKnifeResult = Bank.withdraw(Knives.KNIFE.getKnifeName(), 1);
        boolean waitResult = Waiting.waitUntil(3000, this::inventoryContainsKnife);

        return waitResult || withdrawKnifeResult;
    }

    private boolean withdrawLogs(Work work) {
        Cutting cuttingWork = (Cutting) work;

        String logName = cuttingWork.getLogRequired().getLogName();
        int logCount = Bank.getCount(logName);
        int depositCount = depositAllExceptKnife();

        // find appropriate amount to withdraw
        // -1 infinite, until all logs fully depleted
        long amountToWithdraw = cuttingWork.getSuppliesToMake() == -1 ? 27 : cuttingWork.getSuppliesToMake();
        amountToWithdraw = Math.min(27, amountToWithdraw);

        if (logCount > 0) {
            boolean withdrawResult;

            if (amountToWithdraw == 27) {
                withdrawResult = Bank.withdrawAll(logName);
            } else {
                withdrawResult = Bank.withdraw(logName, (int) amountToWithdraw);
            }

            boolean waitResult = Waiting.waitUntil(cuttingWork::inventoryContainsRequiredLogs);

            return waitResult && withdrawResult;
        }

        return false;
    }

    private boolean withdrawUnstrungBows(Work work) {
        Stringing stringingWork = (Stringing) work;

        String unstrungBowName = stringingWork.getUnstrungBow().getResourceName();
        int unstrungBowID = stringingWork.getUnstrungBow().getResourceIDS()[0];
        int unstrungBowCount = Bank.getCount(unstrungBowName);

        boolean depositResult = Bank.depositInventory();

        // find appropriate amount to withdraw
        // -1 infinite, until all unstrung bows fully depleted
        long amountToWithdraw = stringingWork.getSuppliesToMake() == -1 ? 14 : stringingWork.getSuppliesToMake();
        amountToWithdraw = Math.min(14, amountToWithdraw);

        if (unstrungBowCount > 0) {
            boolean withdrawResult;
            boolean waitResult;

            withdrawResult = Bank.withdraw(unstrungBowName, (int) amountToWithdraw);
            waitResult = Waiting.waitUntil(3000, stringingWork::inventoryContainsUnstrungBow);

            return waitResult && withdrawResult;
        }

        return false;
    }

    private boolean withdrawBowStrings(Work work) {
        Stringing stringingWork = (Stringing) work;

        String bowStringName = stringingWork
                .getBowString()
                .getStringName();

        // find appropriate amount to withdraw
        // -1 infinite, until all unstrung bows fully depleted
        long amountToWithdraw = stringingWork.getSuppliesToMake() == -1 ? 14 : stringingWork.getSuppliesToMake();
        amountToWithdraw = Math.min(14, amountToWithdraw);

        int bowStringCount = Bank.getCount(bowStringName);
        int depositCount = depositAllExcept(stringingWork.getUnstrungBow().getResourceName());

        if (bowStringCount > 0) {
            boolean withdrawResult;
            boolean waitResult;

            if (amountToWithdraw == 14) {
                withdrawResult = Bank.withdrawAll(bowStringName);
            } else {
                withdrawResult = Bank.withdraw(bowStringName, (int) amountToWithdraw);
            }

            waitResult = Waiting.waitUntil(3000, stringingWork::inventoryContainsBowStrings);

            return waitResult && withdrawResult;
        }

        return false;
    }

    private boolean withdrawStaffOfFire(Work work) {
        Alchemy alchemyWork = (Alchemy) getWork();

        boolean depositResult = Bank.depositInventory();
        boolean withdrawStaffResult = Bank.withdraw(Staffs.FIRE_STAFF.getStaffID(), 1);
        boolean waitResult = Waiting.waitUntil(3000, alchemyWork::inventoryContainsStaffOfFire);

        return waitResult || withdrawStaffResult;
    }

    private boolean withdrawNatureRunes(Work work) {
        Alchemy alchemyWork = (Alchemy) getWork();

        int depositResult = depositAllExcept(Staffs.FIRE_STAFF.getStaffID());
        boolean withdrawNatureRunesResult = Bank.withdrawAll(Runes.NATURE_RUNE.getRuneID());
        boolean waitResult = Waiting.waitUntil(3000, alchemyWork::inventoryContainsNatureRunes);

        return waitResult || withdrawNatureRunesResult;
    }

    private boolean withdrawHighLevelAlchemyResources(Work work) {
        Alchemy alchemyWork = (Alchemy) work;

        int depositResult = depositAllExcept(Staffs.FIRE_STAFF.getStaffID(), Runes.NATURE_RUNE.getRuneID());
        boolean setNoteEnabled = org.tribot.api2007.Banking.setNoteEnabled(true);
        boolean isNoteResult = Waiting.waitUntil(2000, org.tribot.api2007.Banking::isNoteEnabled);

        if (isNoteResult) {
            boolean withdrawResult = Bank.withdraw(alchemyWork.getResource().getResourceName(), (int) alchemyWork.getSuppliesToMake());
            boolean waitResult = Waiting.waitUntil(2000, alchemyWork::inventoryContainsResources);
            return withdrawResult || waitResult;
        }

        return false;
    }

    public FletchingXVariables getVariables() {
        return variables;
    }

    public boolean isInitializeBankCache() {
        return initializeBankCache;
    }

    public void setInitializeBankCache(boolean initializeBankCache) {
        this.initializeBankCache = initializeBankCache;
    }

    public boolean isWithdrawKnife() {
        return withdrawKnife;
    }

    public void setWithdrawKnife(boolean withdrawKnife) {
        this.withdrawKnife = withdrawKnife;
    }

    public boolean isDepositCutting() {
        return depositCutting;
    }

    public void setDepositCutting(boolean depositCutting) {
        this.depositCutting = depositCutting;
    }

    public boolean isWithdrawLogs() {
        return withdrawLogs;
    }

    public void setWithdrawLogs(boolean withdrawLogs) {
        this.withdrawLogs = withdrawLogs;
    }

    public boolean isDepositStringing() {
        return depositStringing;
    }

    public void setDepositStringing(boolean depositStringing) {
        this.depositStringing = depositStringing;
    }

    public boolean isWithdrawUnstrungBows() {
        return withdrawUnstrungBows;
    }

    public void setWithdrawUnstrungBows(boolean withdrawUnstrungBows) {
        this.withdrawUnstrungBows = withdrawUnstrungBows;
    }

    public boolean isWithdrawBowStrings() {
        return withdrawBowStrings;
    }

    public void setWithdrawBowStrings(boolean withdrawBowStrings) {
        this.withdrawBowStrings = withdrawBowStrings;
    }

    public boolean isWithdrawStaffOfFire() {
        return withdrawStaffOfFire;
    }

    public void setWithdrawStaffOfFire(boolean withdrawStaffOfFire) {
        this.withdrawStaffOfFire = withdrawStaffOfFire;
    }

    public boolean isWithdrawNatureRunes() {
        return withdrawNatureRunes;
    }

    public void setWithdrawNatureRunes(boolean withdrawNatureRunes) {
        this.withdrawNatureRunes = withdrawNatureRunes;
    }

    public boolean isWithdrawHighLevelAlchemyResource() {
        return withdrawHighLevelAlchemyResource;
    }

    public void setWithdrawHighLevelAlchemyResource(boolean withdrawHighLevelAlchemyResource) {
        this.withdrawHighLevelAlchemyResource = withdrawHighLevelAlchemyResource;
    }

    public boolean isWithdrawCoinsKnife() {
        return withdrawCoinsKnife;
    }

    public void setWithdrawCoinsKnife(boolean withdrawCoinsKnife) {
        this.withdrawCoinsKnife = withdrawCoinsKnife;
    }

    public boolean isWithdrawCoinsLogs() {
        return withdrawCoinsLogs;
    }

    public void setWithdrawCoinsLogs(boolean withdrawCoinsLogs) {
        this.withdrawCoinsLogs = withdrawCoinsLogs;
    }
}
