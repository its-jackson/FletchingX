package scripts.api.enums;

public enum WorkType {

    BOW("Bow"),
    UNSTRUNG_BOW("Unstrung Bow"),
    SHIELD("Shield"),
    SHAFT("Shaft"),
    STOCK("Stock")
    ;

    private final String workType;

    WorkType(String workType) {
        this.workType = workType;
    }

    public String getWorkType() {
        return workType;
    }
}
