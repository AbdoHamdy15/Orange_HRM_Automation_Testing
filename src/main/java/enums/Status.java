package enums;

public enum Status {
    ENABLED("Enabled"),
    DISABLED("Disabled");

    private final String label;

    Status(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
