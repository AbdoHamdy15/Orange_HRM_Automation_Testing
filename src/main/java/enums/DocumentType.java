package enums;

public enum DocumentType {
    PASSPORT("Passport"),
    VISA("Visa");

    private final String label;

    DocumentType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
