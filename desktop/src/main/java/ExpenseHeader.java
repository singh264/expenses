public enum ExpenseHeader {
    DATE("Date"),
    ACCOUNT_NUMBER("Account number"),
    DESCRIPTION("Description"),
    KIND("Kind"),
    PRICE("Price");

    private String header;

    ExpenseHeader(String header) {
        this.header = header;
    }

    @Override
    public String toString() {
        return header;
    }
}
