package scripts.api.works;

import dax.api_lib.models.RunescapeBank;
import org.tribot.script.sdk.cache.BankCache;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.InventoryItem;
import scripts.api.enums.Resource;
import scripts.api.enums.ResourceLocation;
import scripts.api.enums.ResourceOption;
import scripts.api.interfaces.Workable;
import scripts.api.time.TimeElapse;

import java.util.List;

/**
 * Purpose of class: Subclass for controlling the Stringing work
 */

public class Stringing extends Work {

    // the unstrung bow required for the work
    private Resource unstrungBow;

    // the bow strings needed for the work
    private final Workable.Strings bow_strings = Workable.Strings.BOW_STRING;

    public Stringing(Resource resource, ResourceOption resourceOption, RunescapeBank bankLocation, TimeElapse time) {
        super(resource, resourceOption, bankLocation, time);
    }

    public Stringing(Resource resource, ResourceOption resourceOption, RunescapeBank bankLocation, TimeElapse time, long suppliesToMake) {
        super(resource, resourceOption, bankLocation, time, suppliesToMake);
    }

    public Stringing(Resource resource, ResourceOption resourceOption, RunescapeBank bankLocation, int level, long suppliesToMake) {
        super(resource, resourceOption, bankLocation, level, suppliesToMake);
    }

    public Stringing(Resource resource, ResourceOption resourceOption, RunescapeBank bankLocation, long suppliesAmount) {
        super(resource, resourceOption, bankLocation, suppliesAmount);
    }

    public Stringing(Resource resource, ResourceOption resourceOption, int level, TimeElapse time, long suppliesAmount) {
        super(resource, resourceOption, level, time, suppliesAmount);
    }

    public Stringing(Resource resource, ResourceOption resourceOption, RunescapeBank bankLocation, int level) {
        super(resource, resourceOption, bankLocation, level);
    }

    public Stringing(Resource resource, ResourceOption resourceOption, RunescapeBank bankLocation) {
        super(resource, resourceOption, bankLocation);
    }

    public Stringing(Resource resource, ResourceLocation resourceLocation, ResourceOption resourceOption, int level, TimeElapse time) {
        super(resource, resourceLocation, resourceOption, level, time);
    }

    public Stringing(Resource resource, ResourceLocation resourceLocation, ResourceOption resourceOption, int level) {
        super(resource, resourceLocation, resourceOption, level);
    }

    public Stringing(Resource resource, ResourceLocation resourceLocation, ResourceOption resourceOption, TimeElapse time) {
        super(resource, resourceLocation, resourceOption, time);
    }

    public Stringing(Resource resource, ResourceLocation resourceLocation, ResourceOption resourceOption) {
        super(resource, resourceLocation, resourceOption);
    }

    public Stringing() {
        super();
    }


    @Override
    public boolean validate()  {
        return super.validate() || bowStringFullyDepleted() || unstrungBowFullyDepleted();
    }

    @Override
    public void completeState(ResourceLocation resourceLocation) {

    }

    @Override
    public void completeState(Resource resource) {
        switch (resource) {
            case LONGBOW: setUnstrungBow(Resource.LONGBOW_UNSTRUNG);
            break;
            case SHORTBOW: setUnstrungBow(Resource.SHORTBOW_UNSTRUNG);
            break;
            case OAK_LONGBOW: setUnstrungBow(Resource.OAK_LONGBOW_UNSTRUNG);
            break;
            case OAK_SHORTBOW: setUnstrungBow(Resource.OAK_SHORTBOW_UNSTRUNG);
            break;
            case WILLOW_LONGBOW: setUnstrungBow(Resource.WILLOW_LONGBOW_UNSTRUNG);
            break;
            case WILLOW_SHORTBOW: setUnstrungBow(Resource.WILLOW_SHORTBOW_UNSTRUNG);
            break;
            case MAPLE_LONGBOW: setUnstrungBow(Resource.MAPLE_LONGBOW_UNSTRUNG);
            break;
            case MAPLE_SHORTBOW: setUnstrungBow(Resource.MAPLE_SHORTBOW_UNSTRUNG);
            break;
            case YEW_LONGBOW: setUnstrungBow(Resource.YEW_LONGBOW_UNSTRUNG);
            break;
            case YEW_SHORTBOW: setUnstrungBow(Resource.YEW_SHORTBOW_UNSTRUNG);
            break;
            case MAGIC_LONGBOW: setUnstrungBow(Resource.MAGIC_LONGBOW_UNSTRUNG);
            break;
            case MAGIC_SHORTBOW: setUnstrungBow(Resource.MAGIC_SHORTBOW_UNSTRUNG);
        }
    }

    public boolean bowStringFullyDepleted() {
        if (BankCache.isInitialized()) {
            // no more logs available to cut
            return (BankCache.getStack(getBowString().getStringID()) == 0) && !inventoryContainsBowStrings();
        }

        return false;
    }

    public boolean unstrungBowFullyDepleted() {
        if (BankCache.isInitialized()) {
            // no more logs available to cut
            return (BankCache.getStack(getUnstrungBow().getResourceIDS()[0]) == 0) && !inventoryContainsUnstrungBow();
        }

        return false;
    }

    public boolean inventoryContainsBowStrings() {
        return !findBowStrings().isEmpty();
    }

    public List<InventoryItem> findBowStrings() {
        return Query.inventory()
                .nameEquals(getBowString().getStringName())
                .toList();
    }

    public boolean inventoryContainsUnstrungBow() {
        return !findUnstrungBows().isEmpty();
    }

    public List<InventoryItem> findUnstrungBows() {
        return Query.inventory()
                .nameEquals(getUnstrungBow().getResourceName())
                .toList();
    }

    public Resource getUnstrungBow() {
        return unstrungBow;
    }

    public void setUnstrungBow(Resource unstrungBow) {
        this.unstrungBow = unstrungBow;
    }

    public Workable.Strings getBowString() {
        return bow_strings;
    }

    @Override
    public String toString() {
        return super.toString() +
                "Stringing{" +
                "unstrungBow=" + unstrungBow +
                ", bow_strings=" + bow_strings +
                '}';
    }
}
