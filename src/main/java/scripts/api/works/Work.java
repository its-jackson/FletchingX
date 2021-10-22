package scripts.api.works;

import dax.api_lib.models.RunescapeBank;
import org.tribot.script.sdk.Skill;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.InventoryItem;
import org.tribot.script.sdk.types.WorldTile;
import scripts.api.time.TimeElapse;
import scripts.api.enums.Resource;
import scripts.api.enums.ResourceLocation;
import scripts.api.enums.ResourceOption;
import scripts.api.interfaces.Validatable;

import java.io.Serializable;
import java.util.List;

/**
 * Purpose of class: Superclass for all work instantiated. Used for defining the behaviour of the nodes and
 *                      which nodes to be utilized. The work also validates when to stop the script or when to continue.
 */
public abstract class Work implements Validatable, Serializable {

    private Resource resource;
    private ResourceLocation resourceLocation;
    private ResourceOption resourceOption;

    private int level;
    private TimeElapse time;
    private RunescapeBank bankLocation;
    private WorldTile alternateBankLocation;

    // the amount of supplies to create
    // default -1 means until all supplies (logs / bow string / unstrung bows) fully depleted
    private long suppliesToMake = -1;

    public Work(Resource resource, ResourceOption resourceOption, RunescapeBank bankLocation, TimeElapse time) {
        this.resource = resource;
        this.resourceOption = resourceOption;
        this.bankLocation = bankLocation;
        this.time = time;
        completeState(this.resource);
    }


    public Work(Resource resource, ResourceOption resourceOption, RunescapeBank bankLocation, TimeElapse time, long suppliesToMake) {
        this.resource = resource;
        this.resourceOption = resourceOption;
        this.bankLocation = bankLocation;
        this.time = time;
        this.suppliesToMake = suppliesToMake;
        completeState(this.resource);
    }

    public Work(Resource resource, ResourceOption resourceOption, RunescapeBank bankLocation, int level, long suppliesToMake) {
        this.resource = resource;
        this.resourceOption = resourceOption;
        this.bankLocation = bankLocation;
        this.level = level;
        this.suppliesToMake = suppliesToMake;
        completeState(this.resource);
    }

    public Work(Resource resource, ResourceOption resourceOption, RunescapeBank bankLocation, long suppliesToMake) {
        this.resource = resource;
        this.resourceOption = resourceOption;
        this.bankLocation = bankLocation;
        this.suppliesToMake = suppliesToMake;
        completeState(this.resource);
    }

    public Work(Resource resource, ResourceOption resourceOption, RunescapeBank bankLocation, int level) {
        this.resource = resource;
        this.resourceOption = resourceOption;
        this.bankLocation = bankLocation;
        this.level = level;
        completeState(this.resource);
    }

    public Work(Resource resource, ResourceOption resourceOption, int level, TimeElapse time, long suppliesToMake) {
        this.resource = resource;
        this.resourceOption = resourceOption;
        this.level = level;
        this.time = time;
        this.suppliesToMake = suppliesToMake;
        completeState(this.resource);
    }

    public Work(Resource resource, ResourceOption resourceOption, RunescapeBank bankLocation) {
        this.resource = resource;
        this.resourceOption = resourceOption;
        this.bankLocation = bankLocation;
        completeState(this.resource);
    }

    public Work(Resource resource, ResourceLocation resourceLocation, ResourceOption resourceOption, int level, TimeElapse time) {
        this.resource = resource;
        this.resourceLocation = resourceLocation;
        this.resourceOption = resourceOption;
        this.level = level;
        this.time = time;
        completeState(this.resourceLocation);
        completeState(this.resource);
    }

    public Work(Resource resource, ResourceLocation resourceLocation, ResourceOption resourceOption, int level) {
        this.resource = resource;
        this.resourceLocation = resourceLocation;
        this.resourceOption = resourceOption;
        this.level = level;
        completeState(this.resourceLocation);
        completeState(this.resource);
    }

    public Work(Resource resource, ResourceLocation resourceLocation, ResourceOption resourceOption, TimeElapse time) {
        this.resource = resource;
        this.resourceLocation = resourceLocation;
        this.resourceOption = resourceOption;
        this.time = time;
        completeState(this.resourceLocation);
        completeState(this.resource);
    }

    public Work(Resource resource, ResourceLocation resourceLocation, ResourceOption resourceOption) {
        this.resource = resource;
        this.resourceLocation = resourceLocation;
        this.resourceOption = resourceOption;
        completeState(this.resourceLocation);
        completeState(this.resource);
    }

    public Work() {}

    /**
     * Critical method for setting complete state for constructor
     */
    public abstract void completeState(ResourceLocation resourceLocation);

    /**
     * Critical method for setting complete state for constructor
     */
    public abstract void completeState(Resource resource);

    /**
     * Validate the work given - if the worker's level hasn't been reached
     * Or if the worker's timer hasn't elapsed then the method will return false; otherwise
     * the method will return true if not reached;
     *
     * @return True when the work has been reached or surpassed.
     */
    @Override
    public boolean validate() {
        // reached specific level
        // or reached the time elapsed
        return reachedLevel() || reachedTime() || reachedSupplies();
    }

    public boolean reachedLevel() {
        // return if worker mining level is greater than the desired level
        if (getLevel() <= 0 || getLevel() > 99) {
            return false;
        }

        if (this instanceof Cutting || this instanceof Stringing) {
            return Skill.FLETCHING.getActualLevel() >= getLevel();
        } else if (this instanceof Alchemy) {
            return Skill.MAGIC.getActualLevel() >= getLevel();
        }

        return false;
    }

    public boolean reachedTime() {
        // return true if worker timer has surpassed desired time
        return getTime() != null && getTime().validate();
    }

    /**
     * Determine if the amount of supplies met
     *
     * @return True if amount of supplies or all supplies depleted otherwise; false.
     */
    public boolean reachedSupplies() {
        return getSuppliesToMake() == 0;
    }

    public boolean inventoryContainsResources() {
        return !findResources().isEmpty();
    }

    public List<InventoryItem> findResources() {
        return Query.inventory()
                .nameEquals(getResource().getResourceName())
                .toList();
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public ResourceLocation getResourceLocation() {
        return resourceLocation;
    }

    public void setResourceLocation(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    public ResourceOption getResourceOption() {
        return resourceOption;
    }

    public void setResourceOption(ResourceOption resourceOption) {
        this.resourceOption = resourceOption;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public TimeElapse getTime() {
        return time;
    }

    public void setTime(TimeElapse time) {
        this.time = time;
    }

    public RunescapeBank getBankLocation() {
        return bankLocation;
    }

    public void setBankLocation(RunescapeBank bankLocation) {
        this.bankLocation = bankLocation;
    }

    public WorldTile getAlternateBankLocation() {
        return alternateBankLocation;
    }

    public void setAlternateBankLocation(WorldTile alternateBankLocation) {
        this.alternateBankLocation = alternateBankLocation;
    }

    public long getSuppliesToMake() {
        return suppliesToMake;
    }

    public void setSuppliesToMake(long suppliesToMake) {
        this.suppliesToMake = suppliesToMake;
    }

    public void decrementSuppliesToMake() {
        if (this.suppliesToMake != -1) {
            this.suppliesToMake--;
        }
    }

    @Override
    public String toString() {
        return "Work{" +
                "resource=" + resource +
                ", resourceLocation=" + resourceLocation +
                ", resourceOption=" + resourceOption +
                ", level=" + level +
                ", time=" + time +
                ", bankLocation=" + bankLocation +
                ", alternateBankLocation=" + alternateBankLocation +
                ", suppliesToMake=" + suppliesToMake +
                '}';
    }
}
