import java.util.Date;

public class Expense {
    private Date date;
    private String accountNumber;
    private String description;
    private String kind;
    private double price;

    public Expense() {
    }

    public Date getDate() {
        return date;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getDescription() {
        return description;
    }

    public String getKind() {
        return kind;
    }

    public double getPrice() {
        return price;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "date=" + date +
                ", accountNumber='" + accountNumber + '\'' +
                ", description='" + description + '\'' +
                ", kind='" + kind + '\'' +
                ", price=" + price +
                '}';
    }
}
