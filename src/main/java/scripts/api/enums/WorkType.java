package scripts.api.enums;

public enum WorkType {

    CUTTING("Cutting"),
    STRINGING("Stringing"),
    ALCHEMY("Alchemy")
    ;

    private final String workType;

    WorkType(String workType) {
        this.workType = workType;
    }

    public String getWorkType() {
        return workType;
    }
}
