package scripts.api.nodes;

import scripts.api.works.Work;

public class GrandExchange {

    private boolean shouldBuyKnife(Work work) {
        // bank does not contain knife
        // inventory does not contain knife
        // bank contains enough gold || inventory contains enough gold
        // is at the grand exchange
        return false;
    }
}
