package scripts.api.works;

import dax.api_lib.models.RunescapeBank;
import org.tribot.script.sdk.Equipment;
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
 * Purpose of class: Subclass for controlling the Alchemy work
 */

public class Alchemy extends Work {

    // required staff
    private final Workable.Staffs staff_of_fire = Workable.Staffs.FIRE_STAFF;

    // required rune
    private final Workable.Runes nature_runes = Workable.Runes.NATURE_RUNE;

    public Alchemy(Resource resource, ResourceOption resourceOption, RunescapeBank bankLocation, TimeElapse time) {
        super(resource, resourceOption, bankLocation, time);
    }

    public Alchemy(Resource resource, ResourceOption resourceOption, RunescapeBank bankLocation, TimeElapse time, long suppliesToMake) {
        super(resource, resourceOption, bankLocation, time, suppliesToMake);
    }

    public Alchemy(Resource resource, ResourceOption resourceOption, RunescapeBank bankLocation, int level, long suppliesToMake) {
        super(resource, resourceOption, bankLocation, level, suppliesToMake);
    }

    public Alchemy(Resource resource, ResourceOption resourceOption, RunescapeBank bankLocation, long suppliesToMake) {
        super(resource, resourceOption, bankLocation, suppliesToMake);
    }

    public Alchemy(Resource resource, ResourceOption resourceOption, int level, TimeElapse time, long suppliesToMake) {
        super(resource, resourceOption, level, time, suppliesToMake);
    }

    public Alchemy(Resource resource, ResourceOption resourceOption, RunescapeBank bankLocation, int level) {
        super(resource, resourceOption, bankLocation, level);
    }

    public Alchemy(Resource resource, ResourceOption resourceOption, RunescapeBank bankLocation) {
        super(resource, resourceOption, bankLocation);
    }

    public Alchemy(Resource resource, ResourceLocation resourceLocation, ResourceOption resourceOption, int level, TimeElapse time) {
        super(resource, resourceLocation, resourceOption, level, time);
    }

    public Alchemy(Resource resource, ResourceLocation resourceLocation, ResourceOption resourceOption, int level) {
        super(resource, resourceLocation, resourceOption, level);
    }

    public Alchemy(Resource resource, ResourceLocation resourceLocation, ResourceOption resourceOption, TimeElapse time) {
        super(resource, resourceLocation, resourceOption, time);
    }

    public Alchemy(Resource resource, ResourceLocation resourceLocation, ResourceOption resourceOption) {
        super(resource, resourceLocation, resourceOption);
    }

    public Alchemy() {
    }

    @Override
    public boolean validate() {
        return super.validate() || natureRunesFullyDepleted();
    }

    @Override
    public void completeState(ResourceLocation resourceLocation) {

    }

    @Override
    public void completeState(Resource resource) {

    }

    @Override
    public boolean inventoryContainsResources() {
        return !findResources().isEmpty();
    }

    @Override
    public List<InventoryItem> findResources() {
        // 0 = not noted, 1 == noted
        return Query.inventory()
                .idEquals(getResource().getResourceIDS()[1])
                .toList();
    }

    public boolean natureRunesFullyDepleted() {
        if (BankCache.isInitialized()) {
            return (BankCache.getStack(getNatureRunes().getRuneID()) == 0) && !inventoryContainsNatureRunes();
        }

        return false;
    }

    public boolean inventoryContainsNatureRunes() {
        return !findNatureRunes().isEmpty();
    }

    public List<InventoryItem> findNatureRunes() {
        return Query.inventory()
                .idEquals(getNatureRunes().getRuneID())
                .toList();
    }

    public boolean equipmentContainsStaffOfFire() {
        return Equipment.contains(getStaffOfFire().getStaffID());
    }

    public boolean inventoryContainsStaffOfFire() {
        return !findStaffOfFire().isEmpty();
    }

    public List<InventoryItem> findStaffOfFire() {
        return Query.inventory()
                .idEquals(getStaffOfFire().getStaffID())
                .toList();
    }

    public Workable.Staffs getStaffOfFire() {
        return staff_of_fire;
    }

    public Workable.Runes getNatureRunes() {
        return nature_runes;
    }
}
