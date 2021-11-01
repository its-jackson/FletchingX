package scripts.api.nodes;

import org.tribot.script.sdk.*;
import org.tribot.script.sdk.cache.BankCache;
import org.tribot.script.sdk.pricing.Pricing;
import org.tribot.script.sdk.query.Query;
import scripts.FletchingXVariables;
import scripts.api.antiban.AntiBan;
import scripts.api.interfaces.Workable;
import scripts.api.works.Alchemy;
import scripts.api.works.Work;
import scripts.api.works.Worker;

public class HighLevelAlchemy extends Node implements Workable {

    private final long start_time = System.currentTimeMillis();

    private final FletchingXVariables variables = FletchingXVariables.get();
    private final Worker worker = Worker.get();

    private static final String[] SPELL_NAMES = {"High Level Alchemy"};

    public HighLevelAlchemy(Work work) {
        super(work);
    }

    public HighLevelAlchemy() {
        super();
    }

    @Override
    public synchronized void execute() {
        AntiBan.checkAntiBanTask(Query.gameObjects()
                .findRandom()
                .orElse(null));

        // downcast work
        Alchemy alchemyWork = (Alchemy) getWork();

        log(String.format("Performing %s with %s", SPELL_NAMES[0], getWork().getResource().getResourceName()));
        getVariables().setState(String.format("Performing %s with %s", SPELL_NAMES[0], getWork().getResource().getResourceName()));

        if (alchemyWork.inventoryContainsStaffOfFire() && !alchemyWork.equipmentContainsStaffOfFire()) {
            if (Equipment.equip(Staffs.FIRE_STAFF.getStaffID())) {
                log("Equipped " + Staffs.FIRE_STAFF.getStaffName());
                getVariables().setState("Equipped " + Staffs.FIRE_STAFF.getStaffName());
            }
        }

        alchemyWork.findResources()
                .stream()
                .findFirst()
                .ifPresent(inventoryItem -> {
                    boolean isSpellSelected = Magic.ensureSpellSelected(SPELL_NAMES[0]);

                    if (isSpellSelected || Magic.isSpellSelected(SPELL_NAMES[0])) {
                        Magic.getSelectedSpellName().ifPresent(spell -> {
                            if (spell.equals(SPELL_NAMES[0])) {
                                log(String.format("%s spell is selected", SPELL_NAMES[0]));
                                getVariables().setState(String.format("%s spell is selected", SPELL_NAMES[0]));
                                boolean clickResult = inventoryItem.click("Cast");
                                if (clickResult) {
                                    log(String.format("Clicked %s", alchemyWork.getResource().getResourceName()));
                                    getVariables().setState(String.format("Clicked %s", alchemyWork.getResource().getResourceName()));
                                    alchemyWork.decrementSuppliesToMake();
                                    getWorker().incrementResourceCount();
                                    log(String.format("%s remaining: %d", alchemyWork.getResource().getResourceName(), alchemyWork.getSuppliesToMake()));
                                    getVariables().setState(String.format("%s remaining: %d", alchemyWork.getResource().getResourceName(), alchemyWork.getSuppliesToMake()));
                                    boolean alchemyComplete = Waiting.waitUntil(5000, () -> !MyPlayer.isAnimating() && GameTab.MAGIC.isOpen());
                                    if (alchemyComplete) {
                                        // profit = subtract nature runes && resource price
                                        int natureRunePrice = Pricing
                                                .lookupPrice(Runes.NATURE_RUNE.getRuneID())
                                                .orElse(0);

                                        int resourcePrice = Pricing
                                                .lookupPrice(alchemyWork.getResource().getResourceIDS()[0])
                                                .orElse(0);

                                        int goldCollectedAfter = Inventory.getCount(COIN_ID) - natureRunePrice - resourcePrice;

                                        getWorker().setTotalProfit(goldCollectedAfter);

                                        log("Total profit: " + getWorker().getTotalProfit());
                                    }
                                }
                            }
                        });
                    }
                });

        // Generate the trackers
        AntiBan.generateTrackers((int) (System.currentTimeMillis() - this.start_time), false);
    }

    @Override
    public synchronized boolean validate() {
        // validate if bank cache initialized and is at bank define by work
        if (!(BankCache.isInitialized() && isAtBank(getWork().getBankLocation()))) {
            return false;
        }

        return shouldHighLevelAlchemy(getWork());
    }

    @Override
    public String name() {
        return "[Alchemy Control]";
    }

    private boolean shouldHighLevelAlchemy(Work work) {
        // inventory contains nature runes
        // inventory contains resource
        // equipment contains staff of fire or inventory contains staff of fire
        Alchemy alchemyWork = (Alchemy) work;

        if (alchemyWork.inventoryContainsNatureRunes()) {
            if (alchemyWork.inventoryContainsResources()) {
                return alchemyWork.equipmentContainsStaffOfFire() || alchemyWork.inventoryContainsStaffOfFire();
            }
        }

        return false;
    }

    private boolean shouldLowLevelAlchemy(Work work) {
        return false;
    }

    public FletchingXVariables getVariables() {
        return variables;
    }

    public Worker getWorker() {
        return worker;
    }
}
