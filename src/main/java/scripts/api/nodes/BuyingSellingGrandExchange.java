package scripts.api.nodes;

import org.tribot.script.sdk.*;
import org.tribot.script.sdk.cache.BankCache;
import org.tribot.script.sdk.pricing.Pricing;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.GrandExchangeOffer;
import scripts.FletchingXVariables;
import scripts.api.interfaces.Workable;
import scripts.api.works.Cutting;
import scripts.api.works.Work;

import java.util.Optional;

public class BuyingSellingGrandExchange extends Node implements Workable {

    private final FletchingXVariables variables = FletchingXVariables.get();

    // cutting variables
    private boolean buyKnife;
    private boolean buyLogs;

    public BuyingSellingGrandExchange(Work work) {
        super(work);
    }

    public BuyingSellingGrandExchange() {
        super();
    }

    @Override
    public synchronized void execute() {

        if (!GrandExchange.isOpen()) {
            Widgets.closeAll();
            GrandExchange.open();
        }

        if (GrandExchange.isOpen()) {
            if (getWork() instanceof Cutting) {
                // cutting work
                if (isBuyKnife()) {
                    // buy knife from ge if offer not already placed
                    placeOfferKnife();
                    setBuyKnife(false);
                } else if (isBuyLogs()) {
                    // buy logs from ge if offer not already placed
                    placeOfferLogs(getWork());
                    setBuyLogs(false);
                }
            } else {
                // stringing work
            }
        }
    }

    @Override
    public synchronized boolean validate() {
        // only valid if player is not an iron man of any sort
        if (!MyPlayer.getAccountType().equals(MyPlayer.AccountType.NORMAL)) {
            return false;
        }

        if (BankCache.isInitialized()) {
            if (getWork() instanceof Cutting) {
                return shouldBuyKnife(getWork()) ||
                        shouldBuyLogs(getWork());
            } else {

            }
        }

        return false;
    }

    @Override
    public String name() {
        return "[Grand Exchange Control]";
    }

    private boolean shouldBuyKnife(Work work) {
        // bank does not contain knife
        // inventory does not contain knife
        // inventory contains enough gold
        // is at the grand exchange

        int knifePrice = Pricing
                .lookupPrice(Knives.KNIFE.getKnifeID())
                .orElse(0);

        if (!bankCacheContainsKnife()) {
            if (!inventoryContainsKnife()) {
                if (Inventory.getCount(COIN_ID) >= knifePrice) {
                    if (GrandExchange.isNearby()) {
                        setBuyKnife(true);
                        return isBuyKnife();
                    }
                }
            }
        }

        return false;
    }

    private boolean shouldBuyLogs(Work work) {
        // bank does not contain logs
        // inventory does not contain logs
        // inventory contains enough gold
        // is at the grand exchange
        Cutting cuttingWork = (Cutting) work;

        if (!bankCacheContains(cuttingWork.getLogRequired().getLogID())) {
            if (!cuttingWork.inventoryContainsRequiredLogs()) {
                if (inventoryContainsCoins()) {
                    if (GrandExchange.isNearby()) {
                        setBuyLogs(true);
                        return isBuyLogs();
                    }
                }
            }
        }

        return false;
    }

    private boolean shouldBuyUnstrungBows(Work work) {

        return false;
    }

    private boolean shouldBuyBowStrings(Work work) {

        return false;
    }

    private void placeOfferKnife() {
        // buy knife from ge if offer not already placed
        Optional<GrandExchangeOffer> knifeOffer = Query.grandExchangeOffers()
                .itemNameEquals(Knives.KNIFE.getKnifeName())
                .typeEquals(GrandExchangeOffer.Type.BUY)
                .findFirst();

        if (knifeOffer.isPresent()) {
            GrandExchangeOffer knifeOfferItem = knifeOffer.get();

            // wait until the item is status is completed
            boolean isComplete = knifeOfferItem.getStatus()
                    .equals(GrandExchangeOffer.Status.COMPLETED);

            if (isComplete) {
                boolean isCollected = GrandExchange.collectAll(GrandExchange.CollectMethod.INVENTORY);
                if (isCollected) {
                    log("Collected knife");
                    getVariables().setState("Collected knife");
                } else {
                    log("Didn't collect knife");
                    getVariables().setState("Didn't collect knife");
                }
            } else {
                log("Waiting for knife");
                getVariables().setState("Waiting for knife");
            }
        } else {
            // no current offer to buy knife, then place an offer
            log("Buying knife");
            getVariables().setState("Buying knife");
            GrandExchange.CreateOfferConfig offer = GrandExchange.CreateOfferConfig.builder()
                    .itemId(Knives.KNIFE.getKnifeID())
                    .quantity(1)
                    .type(GrandExchangeOffer.Type.BUY)
                    .priceAdjustment(5)
                    .build();

            boolean isPlaceOffer = GrandExchange.placeOffer(offer);

            if (isPlaceOffer) {
                log("Offer has been placed: " + Knives.KNIFE.getKnifeName());
                getVariables().setState("Offer has been placed: " + Knives.KNIFE.getKnifeName());
            }
        }
    }

    private void placeOfferLogs(Work work) {
        Cutting cuttingWork = (Cutting) work;

        String logsToBuyName = cuttingWork.getLogRequired().getLogName();
        int logsToBuyID = cuttingWork.getLogRequired().getLogID();
        long amountToBuy = cuttingWork.getSuppliesToMake();

        // buy knife from ge if offer not already placed
        Optional<GrandExchangeOffer> logOffer = Query.grandExchangeOffers()
                .itemNameEquals(logsToBuyName)
                .typeEquals(GrandExchangeOffer.Type.BUY)
                .findFirst();

        if (logOffer.isPresent()) {
            // current offer already exists, wait until complete.
            GrandExchangeOffer logOfferItem = logOffer.get();

            // wait until the item is status is completed
            boolean isComplete = logOfferItem.getStatus()
                    .equals(GrandExchangeOffer.Status.COMPLETED);

            if (isComplete) {
                boolean isCollected = GrandExchange.collectAll(GrandExchange.CollectMethod.INVENTORY);
                if (isCollected) {
                    log("Collected " + logsToBuyName);
                } else {
                    log("Didn't " + logsToBuyName);
                }
            } else {
                log("Waiting for " + logsToBuyName);
            }
        } else {
            // no current offer to buy logs, then place an offer
            log("Buying " + logsToBuyName);

            GrandExchange.CreateOfferConfig offer = GrandExchange.CreateOfferConfig.builder()
                    .itemId(logsToBuyID)
                    .quantity((int) amountToBuy)
                    .type(GrandExchangeOffer.Type.BUY)
                    .priceAdjustment(5)
                    .build();

            boolean isPlaceOffer = GrandExchange.placeOffer(offer);

            if (isPlaceOffer) {
                log("Offer has been placed: " + logsToBuyName);
            }
        }
    }

    public FletchingXVariables getVariables() {
        return variables;
    }

    public boolean isBuyKnife() {
        return buyKnife;
    }

    public void setBuyKnife(boolean buyKnife) {
        this.buyKnife = buyKnife;
    }

    public boolean isBuyLogs() {
        return buyLogs;
    }

    public void setBuyLogs(boolean buyLogs) {
        this.buyLogs = buyLogs;
    }
}
