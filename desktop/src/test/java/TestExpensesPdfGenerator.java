import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

public class TestExpensesPdfGenerator {

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

    @AfterEach
    private void cleanup() {
        File directory = new File(OUTPUT_DIRECTORY_PATH);
        for(File file: directory.listFiles())
            if (!file.isDirectory())
                file.delete();
    }

    private void validateExpensesPdf(Object[][][] expectedExpensesSheetData) {
        int expectedExpensesPdfCount = expectedExpensesSheetData.length;
        int actualExpensesPdfCount = new File(OUTPUT_DIRECTORY_PATH).listFiles().length;
        Assertions.assertEquals(expectedExpensesPdfCount, actualExpensesPdfCount);

        for (Object[][] expectedExpensesData : expectedExpensesSheetData) {
            String expectedExpensesPdfFileName = ExpensesUtilities.getExpectedExpensesPdfFileName((Date) expectedExpensesData[1][0]);
            Assertions.assertTrue(new File(OUTPUT_DIRECTORY_PATH, expectedExpensesPdfFileName).exists());
        }
    }

    @Test
    void testExpensesPdfGeneratorWithExpensesExcelWithNoData() {
        Object[][][] expectedExpensesSheetData = ExpensesUtilities.createExpensesExcelWithNoData(properties);
        ExpensesParser expensesParser = new ExpensesParser(properties);
        Map<String, List<Expense>> actualExpensesSheetData = null;
        try {
            actualExpensesSheetData = expensesParser.getData();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        ExpensesPdfGenerator expensesPdfGenerator = new ExpensesPdfGenerator(properties, actualExpensesSheetData);
        expensesPdfGenerator.generateExpensesPdfs();
        Assertions.assertEquals(0, new File(OUTPUT_DIRECTORY_PATH).listFiles().length);
    }

    @Test
    void testExpensesPdfGeneratorWithExpensesExcelWithOneSheet() {
        Object[][][] expectedExpensesSheetData = ExpensesUtilities.createExpensesExcelWithOneSheet(properties);
        ExpensesParser expensesParser = new ExpensesParser(properties);
        Map<String, List<Expense>> actualExpensesSheetData = null;
        try {
            actualExpensesSheetData = expensesParser.getData();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        ExpensesPdfGenerator expensesPdfGenerator = new ExpensesPdfGenerator(properties, actualExpensesSheetData);
        expensesPdfGenerator.generateExpensesPdfs();
        validateExpensesPdf(expectedExpensesSheetData);
    }

    @Test
    void testExpensesPdfGeneratorWithExpensesExcelWithMultipleSheet() {
        Object[][][] expectedExpensesSheetData = ExpensesUtilities.createExpensesExcelWithMultipleSheet(properties);
        ExpensesParser expensesParser = new ExpensesParser(properties);
        Map<String, List<Expense>> actualExpensesSheetData = null;
        try {
            actualExpensesSheetData = expensesParser.getData();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        ExpensesPdfGenerator expensesPdfGenerator = new ExpensesPdfGenerator(properties, actualExpensesSheetData);
        expensesPdfGenerator.generateExpensesPdfs();
        validateExpensesPdf(expectedExpensesSheetData);
    }
}
