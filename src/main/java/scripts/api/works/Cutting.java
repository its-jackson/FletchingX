package scripts.api.works;

import dax.api_lib.models.RunescapeBank;
import org.tribot.script.sdk.cache.BankCache;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.InventoryItem;
import scripts.api.interfaces.Workable;
import scripts.api.time.TimeElapse;
import scripts.api.enums.Resource;
import scripts.api.enums.ResourceLocation;
import scripts.api.enums.ResourceOption;

import java.util.List;

/**
 * Cutting work; handles every type of log to be cut into another InventoryItem.
 */
public class Cutting extends Work {

    // the log required to make the supplies
    private Workable.Logs logRequired;

    public Cutting(Resource resource, ResourceOption resourceOption, RunescapeBank bankLocation, TimeElapse time) {
        super(resource, resourceOption, bankLocation, time);
    }

    public Cutting(Resource resource, ResourceOption resourceOption, RunescapeBank bankLocation, TimeElapse time, long suppliesToMake) {
        super(resource, resourceOption, bankLocation, time, suppliesToMake);
    }

    public Cutting(Resource resource, ResourceOption resourceOption, RunescapeBank bankLocation, int level, long suppliesToMake) {
        super(resource, resourceOption, bankLocation, level, suppliesToMake);
    }

    public Cutting(Resource resource, ResourceOption resourceOption, RunescapeBank bankLocation, long suppliesAmount) {
        super(resource, resourceOption, bankLocation, suppliesAmount);
    }

    public Cutting(Resource resource, ResourceOption resourceOption, int level, TimeElapse time, long suppliesAmount) {
        super(resource, resourceOption, level, time, suppliesAmount);
    }

    public Cutting(Resource resource, ResourceOption resourceOption, RunescapeBank bankLocation, int level) {
        super(resource, resourceOption, bankLocation, level);
    }

    public Cutting(Resource resource, ResourceOption resourceOption, RunescapeBank bankLocation) {
        super(resource, resourceOption, bankLocation);
    }

    public Cutting(Resource resource, ResourceLocation resourceLocation, ResourceOption resourceOption, int level, TimeElapse time) {
        super(resource, resourceLocation, resourceOption, level, time);
    }

    public Cutting(Resource resource, ResourceLocation resourceLocation, ResourceOption resourceOption, TimeElapse time) {
        super(resource, resourceLocation, resourceOption, time);
    }

    public Cutting(Resource resource, ResourceLocation resourceLocation, ResourceOption resourceOption) {
        super(resource, resourceLocation, resourceOption);
    }

    public Cutting() {
        super();
    }


    @Override
    public boolean validate() {
        return super.validate() || logsFullyDepleted() || cannotFletchAnymoreShields();
    }

    @Override
    public void completeState(Resource resource) {
        switch (resource) {
            case SHORTBOW_UNSTRUNG:
            case LONGBOW_UNSTRUNG:
            case SHAFTS:
            case STOCK:
                setLogRequired(Workable.Logs.LOG);
                break;
            case OAK_SHORTBOW_UNSTRUNG:
            case OAK_LONGBOW_UNSTRUNG:
            case OAK_SHAFTS:
            case OAK_STOCK:
            case OAK_SHIELD:
                setLogRequired(Workable.Logs.OAK_LOG);
                break;
            case WILLOW_SHORTBOW_UNSTRUNG:
            case WILLOW_LONGBOW_UNSTRUNG:
            case WILLOW_SHAFTS:
            case WILLOW_STOCK:
            case WILLOW_SHIELD:
                setLogRequired(Workable.Logs.WILLOW_LOG);
                break;
            case MAPLE_SHORTBOW_UNSTRUNG:
            case MAPLE_LONGBOW_UNSTRUNG:
            case MAPLE_SHAFTS:
            case MAPLE_STOCK:
            case MAPLE_SHIELD:
                setLogRequired(Workable.Logs.MAPLE_LOG);
                break;
            case YEW_SHORTBOW_UNSTRUNG:
            case YEW_LONGBOW_UNSTRUNG:
            case YEW_SHAFTS:
            case YEW_STOCK:
            case YEW_SHIELD:
                setLogRequired(Workable.Logs.YEW_LOG);
                break;
            case MAGIC_SHORTBOW_UNSTRUNG:
            case MAGIC_LONGBOW_UNSTRUNG:
            case MAGIC_SHAFTS:
            case MAGIC_STOCK:
            case MAGIC_SHIELD:
                setLogRequired(Workable.Logs.MAGIC_LOG);
                break;
            case REDWOOD_SHAFTS:
            case REDWOOD_SHIELD:
                setLogRequired(Workable.Logs.REDWOOD_LOG);
                break;
            case TEAK_STOCK:
                setLogRequired(Workable.Logs.TEAK_LOG);
                break;
            case MAHOGANY_STOCK:
                setLogRequired(Workable.Logs.MAHOGANY_LOG);
        }
    }

    @Override
    public void completeState(ResourceLocation resourceLocation) {

    }

    public boolean logsFullyDepleted() {
        if (BankCache.isInitialized()) {
            // no more logs available to cut
            // bank does not contain enough gold to buy remainder logs / supplies to make
            return (BankCache.getStack(getLogRequired().getLogID()) == 0 && !inventoryContainsRequiredLogs());
        }

        return false;
    }

    public boolean cannotFletchAnymoreShields() {
        if (getResource().getResourceName().contains("shield")) {
            // cannot make shields if logs less than two
            // if supplies to make is less == 1
            // and the logs available inside the inventory are less than 2
            return getSuppliesToMake() == 1;
        }

        return false;
    }

    public boolean inventoryContainsRequiredLogs() {
        return !findRequiredLogs().isEmpty();
    }

    public List<InventoryItem> findRequiredLogs() {
        return Query.inventory()
                .nameEquals(getLogRequired().getLogName())
                .toList();
    }

    public Workable.Logs getLogRequired() {
        return logRequired;
    }

    public void setLogRequired(Workable.Logs logRequired) {
        this.logRequired = logRequired;
    }

    @Override
    public String toString() {
        return super.toString() +
                "Cutting{" +
                "logRequired=" + logRequired +
                '}';
    }
}
