import org.junit.jupiter.api.*;

import java.io.FileOutputStream;
import java.util.*;

public class TestExpensesParser {

    private static Properties properties = new Properties();
    private static final String CONFIG_FILE_PATH = "src/test/java/config.properties";
    private static final String EXPENSES_INPUT_PATH_KEY = "EXPENSES_INPUT_PATH";
    private static final String EXPENSES_INPUT_PATH = "/Users/singh264/git/expenses/data/test/Expenses.xlsx";
    private static final String OUTPUT_DIRECTORY_PATH_KEY = "OUTPUT_DIRECTORY_PATH";
    private static final String OUTPUT_DIRECTORY_PATH = "/Users/singh264/git/expenses/output/test";
    private static final String PROPERTIES_COMMENTS = "";

    @BeforeAll
    private static void createConfigFile() {
        try {
            String configFilePath = CONFIG_FILE_PATH;
            properties.put(EXPENSES_INPUT_PATH_KEY, EXPENSES_INPUT_PATH);
            properties.put(OUTPUT_DIRECTORY_PATH_KEY, OUTPUT_DIRECTORY_PATH);
            FileOutputStream outputStream = new FileOutputStream(configFilePath);
            properties.store(outputStream, PROPERTIES_COMMENTS);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void validateExpensesData(Object[][] expectedExpensesData, List<Expense> actualExpensesData) {
        Assertions.assertEquals(expectedExpensesData.length - 1, actualExpensesData.size());

        Map<Integer, String> expectedExpensesDataHeader = ExpensesUtilities.getHeader(expectedExpensesData[0]);
        for (int i = 1; i < expectedExpensesData.length; i++) {
            Object[] expectedExpense = expectedExpensesData[i];
            Expense actualExpense = actualExpensesData.get(i - 1);
            for (Object expectedExpenseAttribute : expectedExpense) {
                if (expectedExpensesDataHeader.get(i).equalsIgnoreCase(ExpenseHeader.DATE.toString())) {
                    Assertions.assertEquals(expectedExpenseAttribute, actualExpense.getDate());
                }
                else if (expectedExpensesDataHeader.get(i).equalsIgnoreCase(ExpenseHeader.ACCOUNT_NUMBER.toString())) {
                    Assertions.assertEquals(expectedExpense[i], actualExpense.getAccountNumber());
                }
                else if (expectedExpensesDataHeader.get(i).equalsIgnoreCase(ExpenseHeader.DESCRIPTION.toString())) {
                    Assertions.assertEquals(expectedExpense[i], actualExpense.getDescription());
                }
                else if (expectedExpensesDataHeader.get(i).equalsIgnoreCase(ExpenseHeader.KIND.toString())) {
                    Assertions.assertEquals(expectedExpense[i], actualExpense.getKind());
                }
                else if (expectedExpensesDataHeader.get(i).equalsIgnoreCase(ExpenseHeader.PRICE.toString())) {
                    Assertions.assertEquals(expectedExpense[i], actualExpense.getPrice());
                }
            }
        }
    }

    private void validateExpensesSheetData(Object[][][] expectedExpensesSheetData, Map<String, List<Expense>> actualExpensesSheetData) {
        Assertions.assertEquals(expectedExpensesSheetData.length, actualExpensesSheetData.size());

        Iterator<String> actualExpenseDataKeyIterator = actualExpensesSheetData.keySet().iterator();
        for (Object[][] expectedExpensesData : expectedExpensesSheetData) {
            List<Expense> actualExpensesData = actualExpensesSheetData.get(actualExpenseDataKeyIterator.next());
            validateExpensesData(expectedExpensesData, actualExpensesData);
        }
    }

    @Test
    void testExpensesParserWithExpensesExcelWithNoData() {
        Object[][][] expectedExpensesSheetData = ExpensesUtilities.createExpensesExcelWithNoData(properties);
        ExpensesParser expensesParser = new ExpensesParser(properties);
        Map<String, List<Expense>> actualExpensesSheetData = null;
        try {
            actualExpensesSheetData = expensesParser.getData();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(0, actualExpensesSheetData.size());
    }

    @Test
    void testExpensesParserWithExpensesExcelWithOneSheet() {
        Object[][][] expectedExpensesSheetData = ExpensesUtilities.createExpensesExcelWithOneSheet(properties);
        ExpensesParser expensesParser = new ExpensesParser(properties);
        Map<String, List<Expense>> actualExpensesSheetData = null;
        try {
            actualExpensesSheetData = expensesParser.getData();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        validateExpensesSheetData(expectedExpensesSheetData, actualExpensesSheetData);
    }

    @Test
    void testExpensesParserWithExpensesExcelWithMultipleSheet() {
        Object[][][] expectedExpensesSheetData = ExpensesUtilities.createExpensesExcelWithMultipleSheet(properties);
        ExpensesParser expensesParser = new ExpensesParser(properties);
        Map<String, List<Expense>> actualExpensesSheetData = null;
        try {
            actualExpensesSheetData = expensesParser.getData();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        validateExpensesSheetData(expectedExpensesSheetData, actualExpensesSheetData);
    }

    @Test
    void testExpensesParserWithExpensesExcelWithInvalidHeader1() {
        Object[][][] expectedExpensesSheetData = ExpensesUtilities.createExpensesExcelWithInvalidHeader1(properties);
        ExpensesParser expensesParser = new ExpensesParser(properties);
        Exception exception = null;
        try {
            Map<String, List<Expense>> actualExpensesSheetData = expensesParser.getData();
        }
        catch (Exception e) {
            exception = e;
        }
        Assertions.assertNotNull(exception);
    }

    @Test
    void testExpensesParserWithExpensesExcelWithInvalidHeader2() {
        Object[][][] expectedExpensesSheetData = ExpensesUtilities.createExpensesExcelWithInvalidHeader2(properties);
        ExpensesParser expensesParser = new ExpensesParser(properties);
        Exception exception = null;
        try {
            Map<String, List<Expense>> actualExpensesSheetData = expensesParser.getData();
        }
        catch (Exception e) {
            exception = e;
        }
        Assertions.assertNotNull(exception);
    }
}
