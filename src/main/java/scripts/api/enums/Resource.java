package scripts.api.enums;

/**
 * Purpose: Cache the resource name and resource ids .
 * (int[] initialized empty, first search for resource by name then set the resource ids in-game)
 */
public enum Resource {

    // unstrung bows
    SHORTBOW_UNSTRUNG( "Shortbow (u)", new int[]{}),
    LONGBOW_UNSTRUNG("Longbow (u)", new int[]{}),
    OAK_SHORTBOW_UNSTRUNG("Oak shortbow (u)", new int[]{}),
    OAK_LONGBOW_UNSTRUNG("Oak longbow (u)", new int[]{}),
    WILLOW_SHORTBOW_UNSTRUNG("Willow shortbow (u)", new int[]{}),
    WILLOW_LONGBOW_UNSTRUNG("Willow longbow (u)", new int[]{}),
    MAPLE_SHORTBOW_UNSTRUNG("Maple shortbow (u)", new int[]{}),
    MAPLE_LONGBOW_UNSTRUNG("Maple longbow (u)", new int[]{}),
    YEW_SHORTBOW_UNSTRUNG("Yew shortbow (u)", new int[]{}),
    YEW_LONGBOW_UNSTRUNG("Yew longbow (u)", new int[]{}),
    MAGIC_SHORTBOW_UNSTRUNG("Magic shortbow (u)", new int[]{}),
    MAGIC_LONGBOW_UNSTRUNG("Magic longbow (u)", new int[]{}),

    // strung bows
    SHORTBOW("Shortbow", new int[]{}),
    LONGBOW("Longbow", new int[]{}),
    OAK_SHORTBOW("Oak shortbow", new int[]{}),
    OAK_LONGBOW("Oak longbow", new int[]{}),
    WILLOW_SHORTBOW("Willow shortbow", new int[]{}),
    WILLOW_LONGBOW("Willow longbow", new int[]{}),
    MAPLE_SHORTBOW("Maple shortbow", new int[]{}),
    MAPLE_LONGBOW("Maple longbow", new int[]{}),
    YEW_SHORTBOW("Yew shortbow", new int[]{}),
    YEW_LONGBOW("Yew longbow", new int[]{}),
    MAGIC_SHORTBOW("Magic shortbow",new int[]{}),
    MAGIC_LONGBOW("Magic longbow", new int[]{}),

    // shields
    OAK_SHIELD("Oak shield", new int[]{}),
    WILLOW_SHIELD("Willow shield", new int[]{}),
    MAPLE_SHIELD("Maple shield", new int[]{}),
    YEW_SHIELD("Yew shield", new int[]{}),
    MAGIC_SHIELD("Magic shield", new int[]{}),
    REDWOOD_SHIELD("Redwood shield", new int[]{}),

    // shafts
    SHAFTS("15 arrow shafts", new int[]{}),
    OAK_SHAFTS("30 arrow shafts", new int[]{}),
    WILLOW_SHAFTS("45 arrow shafts", new int[]{}),
    MAPLE_SHAFTS("60 arrow shafts", new int[]{}),
    YEW_SHAFTS("75 arrow shafts", new int[]{}),
    MAGIC_SHAFTS("90 arrow shafts", new int[]{}),
    REDWOOD_SHAFTS("105 arrow shafts", new int[]{}),

    // stocks
    STOCK("Stock", new int[]{}),
    OAK_STOCK("Oak stock", new int[]{}),
    WILLOW_STOCK("Willow stock", new int[]{}),
    TEAK_STOCK("Teak stock", new int[]{}),
    MAPLE_STOCK("Maple stock", new int[]{}),
    MAHOGANY_STOCK("Mahogany stock", new int[]{}),
    YEW_STOCK("Yew stock", new int[]{}),
    MAGIC_STOCK("Magic stock", new int[]{})
    ;

    private final String resource_name;
    private int[] resourceIDS;

    Resource(String resourceName, int[] resourceIDS) {
        this.resource_name = resourceName;
        this.resourceIDS = resourceIDS;
    }

    public String getResourceName() {
        return resource_name;
    }

    public int[] getResourceIDS() {
        return resourceIDS;
    }

    public void setResourceIDS(int[] resourceIDS) {
        this.resourceIDS = resourceIDS;
    }
}
