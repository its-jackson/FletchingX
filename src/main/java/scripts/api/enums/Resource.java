package scripts.api.enums;

/**
 * Purpose: Cache the resource name and resource ids.
 * (index 0 = not noted, index 1 = noted)
 */
public enum Resource {

    // unstrung bows
    SHORTBOW_UNSTRUNG( "Shortbow (u)", new int[]{50, 51}),
    LONGBOW_UNSTRUNG("Longbow (u)", new int[]{48, 49}),
    OAK_SHORTBOW_UNSTRUNG("Oak shortbow (u)", new int[]{54, 55}),
    OAK_LONGBOW_UNSTRUNG("Oak longbow (u)", new int[]{56, 57}),
    WILLOW_SHORTBOW_UNSTRUNG("Willow shortbow (u)", new int[]{60, 61}),
    WILLOW_LONGBOW_UNSTRUNG("Willow longbow (u)", new int[]{58, 59}),
    MAPLE_SHORTBOW_UNSTRUNG("Maple shortbow (u)", new int[]{64, 65}),
    MAPLE_LONGBOW_UNSTRUNG("Maple longbow (u)", new int[]{62, 63}),
    YEW_SHORTBOW_UNSTRUNG("Yew shortbow (u)", new int[]{68, 69}),
    YEW_LONGBOW_UNSTRUNG("Yew longbow (u)", new int[]{66, 67}),
    MAGIC_SHORTBOW_UNSTRUNG("Magic shortbow (u)", new int[]{72, 73}),
    MAGIC_LONGBOW_UNSTRUNG("Magic longbow (u)", new int[]{70, 71}),

    // strung bows
    SHORTBOW("Shortbow", new int[]{841, 842}),
    LONGBOW("Longbow", new int[]{839, 840}),
    OAK_SHORTBOW("Oak shortbow", new int[]{843, 844}),
    OAK_LONGBOW("Oak longbow", new int[]{845, 846}),
    WILLOW_SHORTBOW("Willow shortbow", new int[]{849, 850}),
    WILLOW_LONGBOW("Willow longbow", new int[]{847, 848}),
    MAPLE_SHORTBOW("Maple shortbow", new int[]{853, 854}),
    MAPLE_LONGBOW("Maple longbow", new int[]{851, 852}),
    YEW_SHORTBOW("Yew shortbow", new int[]{857, 858}),
    YEW_LONGBOW("Yew longbow", new int[]{855, 856}),
    MAGIC_SHORTBOW("Magic shortbow",new int[]{861, 862}),
    MAGIC_LONGBOW("Magic longbow", new int[]{859, 860}),

    // shields
    OAK_SHIELD("Oak shield", new int[]{22251, 22252}),
    WILLOW_SHIELD("Willow shield", new int[]{22254, 22255}),
    MAPLE_SHIELD("Maple shield", new int[]{22257, 22258}),
    YEW_SHIELD("Yew shield", new int[]{22260, 22261}),
    MAGIC_SHIELD("Magic shield", new int[]{22263, 22264}),
    REDWOOD_SHIELD("Redwood shield", new int[]{22266, 22267}),

    // shafts
    SHAFTS("15 arrow shafts", new int[]{52}),
    OAK_SHAFTS("30 arrow shafts", new int[]{52}),
    WILLOW_SHAFTS("45 arrow shafts", new int[]{52}),
    MAPLE_SHAFTS("60 arrow shafts", new int[]{52}),
    YEW_SHAFTS("75 arrow shafts", new int[]{52}),
    MAGIC_SHAFTS("90 arrow shafts", new int[]{52}),
    REDWOOD_SHAFTS("105 arrow shafts", new int[]{52}),

    // stocks
    STOCK("Wooden stock", new int[]{9440, 9441}),
    OAK_STOCK("Oak stock", new int[]{9442, 9443}),
    WILLOW_STOCK("Willow stock", new int[]{9444, 9445}),
    TEAK_STOCK("Teak stock", new int[]{9446, 9447}),
    MAPLE_STOCK("Maple stock", new int[]{9448, 9449}),
    MAHOGANY_STOCK("Mahogany stock", new int[]{9450, 9451}),
    YEW_STOCK("Yew stock", new int[]{9452, 9453}),
    MAGIC_STOCK("Magic stock", new int[]{21952, 21953})
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
