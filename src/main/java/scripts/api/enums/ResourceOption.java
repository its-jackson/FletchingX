package scripts.api.enums;

public enum ResourceOption {

    SELL_TO_GRAND_EXCHANGE("Sell To Grand Exchange"),
    FLETCH_THEN_ALCH("Fletch Then Alch")
    ;

    private final String option;

    ResourceOption(String option) {
        this.option = option;
    }

    public String getOption() {
        return option;
    }
}
