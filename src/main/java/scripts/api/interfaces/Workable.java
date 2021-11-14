package scripts.api.interfaces;

import dax.api_lib.models.RunescapeBank;
import org.tribot.api2007.Banking;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.sdk.*;
import org.tribot.script.sdk.cache.BankCache;
import org.tribot.script.sdk.input.Keyboard;
import org.tribot.script.sdk.interfaces.Actionable;
import org.tribot.script.sdk.interfaces.Interactable;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.*;
import org.tribot.script.sdk.walking.GlobalWalking;
import org.tribot.script.sdk.walking.LocalWalking;
import org.tribot.script.sdk.walking.WalkState;
import scripts.api.antiban.AntiBan;

import java.util.List;
import java.util.Optional;

public interface Workable {

    // standalone item id's
    int COIN_ID = 995;

    // var bit
    int ESC_VARBIT = 4681;

    enum Logs {

        LOG("Logs", 1511),
        OAK_LOG("Oak logs", 1521),
        WILLOW_LOG("Willow logs", 1519),
        MAPLE_LOG("Maple logs", 1517),
        TEAK_LOG("Teak logs", 6333),
        ARTCTIC_PINE_LOG("Arctic pine logs", 10810),
        MAHOGANY_LOG("Mahogany logs", 6332),
        YEW_LOG("Yew logs", 1515),
        MAGIC_LOG("Magic logs", 1513),
        REDWOOD_LOG("Redwood logs", 19669);

        private String logName;
        private int logID;

        Logs(String logs, int logID) {
            this.logName = logs;
            this.logID = logID;
        }

        public String getLogName() {
            return logName;
        }

        public void setLogName(String logName) {
            this.logName = logName;
        }

        public int getLogID() {
            return logID;
        }

        public void setLogID(int logID) {
            this.logID = logID;
        }
    }

    enum Knives {

        KNIFE("Knife", 946);

        private String knifeName;
        private int knifeID;

        Knives(String knife, int knifeID) {
            this.knifeName = knife;
            this.knifeID = knifeID;
        }

        public String getKnifeName() {
            return knifeName;
        }

        public void setKnifeName(String knifeName) {
            this.knifeName = knifeName;
        }

        public int getKnifeID() {
            return knifeID;
        }

        public void setKnifeID(int knifeID) {
            this.knifeID = knifeID;
        }
    }

    enum Strings {

        BOW_STRING("Bow string", 1777),
        CROSS_BOW_STRING("Cross bow string", -1);

        private String stringName;
        private int stringID;

        Strings(String stringName, int stringID) {
            this.stringName = stringName;
            this.stringID = stringID;
        }

        public String getStringName() {
            return stringName;
        }

        public void setStringName(String stringName) {
            this.stringName = stringName;
        }

        public int getStringID() {
            return stringID;
        }

        public void setStringID(int stringID) {
            this.stringID = stringID;
        }
    }

    enum Staffs {

        FIRE_STAFF("Staff of fire", 1387);

        private String staffName;
        private int staffID;

        Staffs(String staffName, int staffID) {
            this.staffName = staffName;
            this.staffID = staffID;
        }

        public String getStaffName() {
            return staffName;
        }

        public void setStaffName(String staffName) {
            this.staffName = staffName;
        }

        public int getStaffID() {
            return staffID;
        }

        public void setStaffID(int staffID) {
            this.staffID = staffID;
        }
    }

    enum Runes {

        NATURE_RUNE("Nature runes", 561);

        private String runeName;
        private int runeID;

        Runes(String runeName, int runeID) {
            this.runeName = runeName;
            this.runeID = runeID;
        }

        public String getRuneName() {
            return runeName;
        }

        public void setRuneName(String runeName) {
            this.runeName = runeName;
        }

        public int getRuneID() {
            return runeID;
        }

        public void setRuneID(int runeID) {
            this.runeID = runeID;
        }
    }

    default boolean walkToTile(WorldTile tile) {
        AntiBan.activateRun();
        return GlobalWalking.walkTo(tile, () -> {
            AntiBan.activateRun();
            if (MyPlayer.getPosition().distanceTo(tile) < 7) {
                return WalkState.SUCCESS;
            } else {
                return WalkState.CONTINUE;
            }
        });
    }

    /**
     * Walks to the desired RunescapeBank using DaxWalker
     *
     * @param bank
     * @return
     */
    default boolean walkToBank(RunescapeBank bank) {
        // old RSTile
        RSTile oldRSTile = bank.getPosition();
        int x = oldRSTile.getX();
        int y = oldRSTile.getY();
        int plane = oldRSTile.getPlane();
        // construct the WorldTile
        WorldTile bankTile = new WorldTile(x, y, plane);
        // walk to bank tile
        AntiBan.activateRun();
        return GlobalWalking.walkTo(bankTile, () -> {
            AntiBan.activateRun();
            if (MyPlayer.getPosition().distanceTo(bankTile) < 7) {
                return WalkState.SUCCESS;
            } else {
                return WalkState.CONTINUE;
            }
        });
    }

    /**
     * @param bank
     * @return True if the worker distance to bank tile is less than 7; false otherwise
     */
    default boolean isAtBank(RunescapeBank bank) {
        if (bank == null) {
            return false;
        }
        // get old RSTile
        RSTile oldRSTile = bank.getPosition();
        // construct new WorldTile
        Optional<WorldTile> bankTileOptional = convertRSTileToWorldTile(oldRSTile);
        if (bankTileOptional.isPresent()) {
            WorldTile bankTile = bankTileOptional.get();
            // worker distance to bank less than 7
            return MyPlayer.getPosition().distanceTo(bankTile) < 7 && LocalWalking.createMap().canReach(bankTile);
        } else {
            return false;
        }
    }

    default boolean isAtLumbridgeBasement() {
        return isAtBank(RunescapeBank.LUMBRIDGE_BASEMENT);
    }

    /**
     * Convert RSTile to WorldTile.
     *
     * @param rsTile The current RSTile desired.
     * @return The converted WorldTile Optional otherwise; an empty Optional.
     */
    default Optional<WorldTile> convertRSTileToWorldTile(RSTile rsTile) {
        if (rsTile != null) {
            return Optional.of(new WorldTile(rsTile.getX(), rsTile.getY(), rsTile.getPlane()));
        }
        return Optional.empty();
    }

    default boolean inventoryContainsCoins() {
        return findInventoryCoins().isPresent();
    }

    default boolean inventoryContainsKnife() {
        return findInventoryKnife().isPresent();
    }

    default Optional<InventoryItem> findInventoryCoins() {
        return findInventoryItem(COIN_ID);
    }

    default Optional<InventoryItem> findInventoryKnife() {
        return findInventoryItem(Knives.KNIFE.getKnifeName());
    }

    default Optional<InventoryItem> findInventoryItem(int itemName) {
        return Query.inventory()
                .idEquals(itemName)
                .findFirst();
    }

    default Optional<InventoryItem> findInventoryItem(String itemName) {
        return Query.inventory()
                .nameContains(itemName)
                .findFirst();
    }

    default Optional<EquipmentItem> findEquipmentItem (int itemName) {
        return Query.equipment()
                .idEquals(itemName)
                .findFirst();
    }

    default Optional<EquipmentItem> findEquipmentItem (String itemName) {
        return Query.equipment()
                .nameContains(itemName)
                .findFirst();
    }

    // created my own combine method
    default void combineItem(InventoryItem combine, List<InventoryItem> items, int preference) {
        if (combine == null || items.isEmpty()) {
            return;
        }

        // 50 percent of players will use this method
        if (preference > 50) {
            items.stream()
                    .findAny()
                    .ifPresent(combine::useOn);

        } else {

            // 50 percent of players will use this method
            items.stream()
                    .findAny()
                    .ifPresent(inventoryItem -> inventoryItem.useOn(combine));
        }
    }

    default boolean interact(Interactable entity, String action) {
        if (entity == null || action == null) {
            return false;
        }

        List<String> actions = ((Actionable)entity).getActions();

        // no actions exist, return false
        if (actions.size() == 0) {
            return false;
        }

        return entity.interact(action);
    }

    default boolean openBank(int preference) {
        // 50 percent of players will use this method
        if (preference > 50) {
            return Bank.open();
        } else {
            // 50 percent of players will use this method
            Optional<Npc> banker = Query.npcs()
                    .nameContains("Bank", "Banker")
                    .maxDistance(7)
                    .findClosest();

            if (banker.isPresent()) {
                boolean clickResult = interact(banker.get(), "Bank");

                if (clickResult) {
                    boolean waitResult = Waiting.waitUntil(7000, Bank::isOpen);
                    if (!waitResult) {
                        return Bank.open();
                    } else {
                        BankCache.update();
                    }
                }

                return clickResult;

            } else {
                return Bank.open();
            }
        }
    }

    default boolean closeBank(int preference) {
        BankCache.update();
        // 50 percent of players will use this method
        if (preference > 50) {
            return Bank.close();
        } else {
            // 50 percent of players will use this method
            if (isEscapeClose()) {
                Keyboard.pressEscape();
                return Waiting.waitUntil(2000, () -> !Bank.isOpen());
            } else {
                // go into all game settings and turn on escape close
                return enableEscapeClose();
            }
        }
    }

    default boolean enableEscapeClose() {
        Widgets.closeAll();

        if (GameTab.OPTIONS.open()) {
            Optional<Widget> allSettings = Widgets.findWhereAction("All settings");
            allSettings.ifPresent(widget -> widget.click("All settings"));
            Waiting.wait(1000);
            Optional<Widget> searchWidget = Widgets.get(134, 10);
            Waiting.wait(1000);
            searchWidget.ifPresent(widget -> widget.click("Search"));
            Waiting.wait(1000);
            Keyboard.typeString("esc");
            Waiting.wait(1000);
            Optional<Widget> escapeWidget = Widgets.get(134, 18, 1);
            escapeWidget.ifPresent(widget -> widget.click("Toggle"));
            Waiting.wait(1000);
            Widgets.closeAll();
            return isEscapeClose();
        }

        return false;
    }


    default boolean isEscapeClose() {
        return GameState.getVarbit(ESC_VARBIT) > 0;
    }

    default int depositAllExcept(int... item) {
        return Banking.depositAllExcept(item);
    }

    default int depositAllExcept(String... item) {
        return Banking.depositAllExcept(item);
    }

    default int depositAllExceptKnife() {
        return depositAllExcept(Knives.KNIFE.getKnifeName());
    }

    default boolean bankCacheContainsKnife() {
        return bankCacheContains(Knives.KNIFE.getKnifeID());
    }

    default boolean bankCacheContains(int itemID) {
        return BankCache.getStack(itemID) > 0;
    }
}
