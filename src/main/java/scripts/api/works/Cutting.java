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

    // the amount of supplies to create
    // -1 by default for endless
    private long suppliesAmount = -1;

    // the log required to make the supplies
    private Workable.Logs logRequired;

    public Cutting(Resource resource, ResourceOption resourceOption, RunescapeBank bankLocation) {
        super(resource, resourceOption, bankLocation);
    }

    public Cutting(Resource resource, ResourceOption resourceOption, RunescapeBank bankLocation, long suppliesAmount) {
        super(resource, resourceOption, bankLocation);
        this.suppliesAmount = suppliesAmount;
    }

    public Cutting(Resource resource, ResourceLocation resourceLocation, ResourceOption resourceOption, int level, TimeElapse time, long suppliesAmount) {
        super(resource, resourceLocation, resourceOption, level, time);
        this.suppliesAmount = suppliesAmount;
    }

    public Cutting(Resource resource, ResourceLocation resourceLocation, ResourceOption resourceOption, long suppliesAmount) {
        super(resource, resourceLocation, resourceOption);
        this.suppliesAmount = suppliesAmount;
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
        return super.validate() || reachedSupplies();
    }

    @Override
    public void completeState(Resource resource) {
        switch (resource) {
            case YEW_STOCK:
            case YEW_SHIELD:
            case YEW_SHAFTS:
            case YEW_SHORTBOW_UNSTRUNG:
            case YEW_LONGBOW_UNSTRUNG: setLogRequired(Workable.Logs.YEW_LOG);
            break;
        }
    }

    @Override
    public void completeState(ResourceLocation resourceLocation) {

    }

    /**
     * Determine if the amount of supplies met or if all required logs are depleted.
     *
     * @return True if amount of supplies or all supplies depleted otherwise; false.
     */
    private boolean reachedSupplies() {
        if (BankCache.isInitialized()) {
            // no more logs available to cut
            boolean logsFullyDepleted = (BankCache.getStack(getLogRequired().getLogID()) == 0 && !inventoryContainsRequiredLogs());
            // fletched the total supplies to make
            return logsFullyDepleted || getSuppliesAmount() == 0;
        }

        return false;
    }

    public boolean inventoryContainsRequiredLogs() {
        return !findRequiredLogs().isEmpty();
    }

    public List<InventoryItem> findRequiredLogs() {
        return Query.inventory()
                .nameContains(getLogRequired().getLogName())
                .toList();
    }

    public long getSuppliesAmount() {
        return suppliesAmount;
    }

    public void setSuppliesAmount(long suppliesAmount) {
        if (this.suppliesAmount > 0) {
            this.suppliesAmount = suppliesAmount;
        }
    }

    public Workable.Logs getLogRequired() {
        return logRequired;
    }

    public void setLogRequired(Workable.Logs logRequired) {
        this.logRequired = logRequired;
    }
}
