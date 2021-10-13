package scripts.api.nodes;

import org.tribot.script.sdk.Bank;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.cache.BankCache;
import scripts.api.interfaces.Workable;
import scripts.api.works.Cutting;
import scripts.api.works.Work;

import java.util.Locale;
import java.util.OptionalInt;

public class Banking extends Node implements Workable {

    private boolean shouldInitializeBankCache;
    private boolean shouldWithdrawKnife;

    private boolean shouldDepositCutting;
    private boolean shouldWithdrawCutting;

    public Banking(Work work) {
        super(work);
    }

    public Banking() {
        super();
    }

    @Override
    public synchronized void execute() {
        if (getWork() instanceof Cutting) {
            // open bank
            if (!Bank.isOpen()) {
                Bank.open();
            }
            if (Bank.isOpen()) {
                // initialize bank cache
                if (isShouldInitializeBankCache()) {
                    log("Bank cache initialized");
                    setShouldInitializeBankCache(false);
                }
                // deposit all except knife
                if (isShouldDepositCutting()) {
                    int depositCount = depositAllExceptKnife();
                    boolean depositAllExceptResult = Waiting.waitUntil(3000, () -> !getWork().inventoryContainsRequiredResources());
                    if (depositAllExceptResult) {
                        log("Deposited all except knife");
                    }
                    setShouldDepositCutting(false);
                }
                // withdraw logs
                if (isShouldWithdrawCutting()) {
                    boolean withdrawLogsResult = withdrawLogs(getWork());
                    if (withdrawLogsResult) {
                        log("Withdrew " + ((Cutting) getWork()).getLogRequired().getLogName().toLowerCase(Locale.ROOT));
                        boolean closeBankResult = Bank.close();
                        if (closeBankResult) {
                            log("Closed bank");
                        }
                    }
                    setShouldWithdrawCutting(false);
                }
                // withdraw knife
                if (isShouldWithdrawKnife()) {
                    boolean withdrawKnifeResult = withdrawKnife();
                    if (withdrawKnifeResult) {
                        log("Withdrew knife");
                    }
                    setShouldWithdrawKnife(false);
                }
            }

        } else {
            // do stringing work
        }
    }

    @Override
    public synchronized boolean validate() {
        if (!getWork().validate()) {
            if (getWork() instanceof Cutting) {
                // do cutting
                return shouldInitalizeBankCache(getWork()) ||
                        shouldDepositCutting(getWork()) ||
                        shouldWithdrawCutting(getWork()) ||
                        shouldWithdrawKnife(getWork())
                        ;

            } else {
                // do stringing
            }
        }

        return false;
    }

    @Override
    public String name() {
        return "[Banking Control]";
    }

    // bank cache not initialized
    // is at bank
    private boolean shouldInitalizeBankCache(Work work) {
        if (isAtBank(work.getBankLocation())) {
            if (!BankCache.isInitialized()) {
                setShouldInitializeBankCache(true);
                return isShouldInitializeBankCache();
            }
        }
        return false;
    }

    // is at bank
    // bank cache is initialized
    // inventory contains knife
    // inventory contains resource made
    // inventory does not contain logs
    private boolean shouldDepositCutting(Work work) {
        if (isAtBank(work.getBankLocation())) {
            if (BankCache.isInitialized()) {
                if (inventoryContainsKnife()) {
                    if (work.inventoryContainsRequiredResources()) {
                        if (!((Cutting)work).inventoryContainsRequiredLogs()) {
                            setShouldDepositCutting(true);
                            return isShouldDepositCutting();
                        }
                    }
                }
            }
        }
        return false;
    }

    // is at bank
    // bank cache is initialized
    // inventory contains knife
    // inventory does not contain logs
    // inventory does not contain resources made
    private boolean shouldWithdrawCutting(Work work) {
        Cutting cuttingWork = (Cutting) work;

        if (isAtBank(work.getBankLocation())) {
            if (BankCache.isInitialized()) {
                // check if bankcache has required logs
                OptionalInt optionalIntLogCount = Workable.getBankCacheItemCount(cuttingWork.getLogRequired().getLogID());
                if (optionalIntLogCount.isPresent()) {
                    if (optionalIntLogCount.getAsInt() > 0) {
                        if (inventoryContainsKnife()) {
                            if (!cuttingWork.inventoryContainsRequiredLogs()) {
                                if (!work.inventoryContainsRequiredResources()) {
                                    setShouldWithdrawCutting(true);
                                    return isShouldWithdrawCutting();
                                }
                            }
                        }
                    }
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
        if (isAtBank(work.getBankLocation())) {
            if (BankCache.isInitialized()) {
                if (!inventoryContainsKnife()) {
                    if (bankCacheContainsKnife()) {
                        setShouldWithdrawKnife(true);
                        return isShouldWithdrawKnife();
                    }
                }
            }
        }

        return false;
    }

    private boolean withdrawKnife() {
        Bank.depositInventory();

        boolean withdrawKnifeResult = Bank.withdraw(Knives.KNIFE.getKnifeName(), 1);
        boolean waitResult = Waiting.waitUntil(3000, this::inventoryContainsKnife);

        return waitResult || withdrawKnifeResult;
    }

    private boolean withdrawLogs(Work work) {
        Cutting cuttingWork = (Cutting) work;

        String logName = cuttingWork.getLogRequired().getLogName();
        int logCount = Bank.getCount(logName);
        int depositCount = depositAllExceptKnife();

        if (logCount > 0) {
            boolean withdrawResult = Bank.withdrawAll(logName);
            boolean waitResult = Waiting.waitUntil(cuttingWork::inventoryContainsRequiredLogs);
            return waitResult || withdrawResult;
        }

        return false;
    }

    public boolean isShouldInitializeBankCache() {
        return shouldInitializeBankCache;
    }

    public void setShouldInitializeBankCache(boolean shouldInitializeBankCache) {
        this.shouldInitializeBankCache = shouldInitializeBankCache;
    }

    public boolean isShouldWithdrawKnife() {
        return shouldWithdrawKnife;
    }

    public void setShouldWithdrawKnife(boolean shouldWithdrawKnife) {
        this.shouldWithdrawKnife = shouldWithdrawKnife;
    }

    public boolean isShouldDepositCutting() {
        return shouldDepositCutting;
    }

    public void setShouldDepositCutting(boolean shouldDepositCutting) {
        this.shouldDepositCutting = shouldDepositCutting;
    }

    public boolean isShouldWithdrawCutting() {
        return shouldWithdrawCutting;
    }

    public void setShouldWithdrawCutting(boolean shouldWithdrawCutting) {
        this.shouldWithdrawCutting = shouldWithdrawCutting;
    }
}
