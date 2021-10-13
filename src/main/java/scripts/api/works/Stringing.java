package scripts.api.works;

import dax.api_lib.models.RunescapeBank;
import scripts.api.enums.Resource;
import scripts.api.enums.ResourceLocation;
import scripts.api.enums.ResourceOption;
import scripts.api.time.TimeElapse;

public class Stringing extends Work {

    private long suppliesAmount;
    private long stringAmount;

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
    public void completeState(ResourceLocation resourceLocation) {

    }

    @Override
    public void completeState(Resource resource) {

    }

    @Override
    public boolean validate() {
        return super.validate();
    }

    public long getSuppliesAmount() {
        return suppliesAmount;
    }

    public void setSuppliesAmount(long suppliesAmount) {
        this.suppliesAmount = suppliesAmount;
    }

    public long getStringAmount() {
        return stringAmount;
    }

    public void setStringAmount(long stringAmount) {
        this.stringAmount = stringAmount;
    }
}
