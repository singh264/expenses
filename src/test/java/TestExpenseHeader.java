import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestExpenseHeader {
    @Test
    void testExpenseHeaderReturnsValidDateString() {
        Assertions.assertEquals("Date", ExpenseHeader.DATE.toString());
    }

    @Test
    void testExpenseHeaderReturnsValidAccountNumberString() {
        Assertions.assertEquals("Account number", ExpenseHeader.ACCOUNT_NUMBER.toString());
    }

    @Test
    void testExpenseHeaderReturnsValidDescriptionString() {
        Assertions.assertEquals("Description", ExpenseHeader.DESCRIPTION.toString());
    }

    @Test
    void testExpenseHeaderReturnsValidKindString() {
        Assertions.assertEquals("Kind", ExpenseHeader.KIND.toString());
    }

    @Test
    void testExpenseHeaderReturnsValidPriceString() {
        Assertions.assertEquals("Price", ExpenseHeader.PRICE.toString());
    }
}
