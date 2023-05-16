import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class TestExpense {
    private Date date = new Date();
    private String accountNumber = "accountNumber";
    private String description = "description";
    private String kind = "kind";
    private double price = 1.0;

    private Expense exec() {
        return new Expense();
    }

    @Test
    void testExpenseReturnsValidDate() {
        Expense expense = exec();
        expense.setDate(date);

        Assertions.assertEquals(date, expense.getDate());
    }

    @Test
    void testExpenseReturnsAccountNumber() {
        Expense expense = exec();
        expense.setAccountNumber(accountNumber);

        Assertions.assertEquals(accountNumber, expense.getAccountNumber());
    }

    @Test
    void testExpenseReturnsValidDescription() {
        Expense expense = exec();
        expense.setDescription(description);

        Assertions.assertEquals(description, expense.getDescription());
    }

    @Test
    void testExpenseReturnsValidKind() {
        Expense expense = exec();
        expense.setKind(kind);

        Assertions.assertEquals(kind, expense.getKind());
    }

    @Test
    void testExpenseReturnsValidPrice() {
        Expense expense = exec();
        expense.setPrice(price);

        Assertions.assertEquals(price, expense.getPrice());
    }

    @Test
    void testExpenseReturnsValidToString() {
        Expense expense = exec();
        expense.setDate(date);
        expense.setAccountNumber(accountNumber);
        expense.setDescription(description);
        expense.setKind(kind);
        expense.setPrice(price);

        String expectedExpenseToString = "Transaction{" +
                "date=" + date +
                ", accountNumber='" + accountNumber + '\'' +
                ", description='" + description + '\'' +
                ", kind='" + kind + '\'' +
                ", price=" + price +
                '}';

        Assertions.assertEquals(expectedExpenseToString, expense.toString());
    }
}
