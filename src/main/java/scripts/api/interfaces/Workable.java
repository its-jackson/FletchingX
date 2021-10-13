package scripts.api.interfaces;

import dax.api_lib.models.RunescapeBank;
import org.tribot.api2007.Banking;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.sdk.MyPlayer;
import org.tribot.script.sdk.cache.BankCache;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.InventoryItem;
import org.tribot.script.sdk.types.WorldTile;
import org.tribot.script.sdk.walking.LocalWalking;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;

public interface Workable {

    enum Logs {

        LOG("Logs", -1),
        OAK_LOG("Oak logs", -1),
        WILLOW_LOG("Willow logs", -1),
        ARTCTIC_PINE_LOG("Arctic pine logs", -1),
        YEW_LOG("Yew logs", 1515),
        ;

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

    default boolean inventoryContainsKnife() {
        return findKnife().isPresent();
    }

    default Optional<InventoryItem> findKnife() {
        return Query.inventory()
                .nameContains(Knives.KNIFE.getKnifeName())
                .findFirst();
    }

    default boolean combineItem(InventoryItem combine, List<InventoryItem> items, String action) {
        if (combine == null || items.isEmpty()) {
            return false;
        }

        // click the combining item
        boolean clickResult = combine.click(action);

        if (clickResult) {
            items.stream()
                    .findAny()
                    .ifPresent(inventoryItem -> inventoryItem.click(action));
            return true;
        }

        return false;
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
        return getBankCacheItemIDS().contains(itemID);
    }

    // static methods below

    static List<Integer> getBankCacheItemIDS() {
        return BankCache.entries()
                .stream()
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    static OptionalInt getBankCacheItemCount(int itemID) {
        return BankCache.entries()
                .stream()
                .filter(integerIntegerEntry -> integerIntegerEntry.getKey() == itemID)
                .mapToInt(Map.Entry::getValue)
                .findFirst();
    }

}
