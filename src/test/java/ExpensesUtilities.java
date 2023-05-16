import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ExpensesUtilities {

    private static final String EXPENSES_DATE_FORMAT = "yyyy-mm-dd";
    private static final String EXPENSES_SHEET_NAME_PREFIX = "Expenses ";
    private static final String EXPENSES_INPUT_DIRECTORY_PATH = "/Users/singh264/git/expenses/data/test";
    private static final String EXPENSES_INPUT_PATH_KEY = "EXPENSES_INPUT_PATH";
    private static final String EXPENSES_INPUT_FILE_NAME_FORMAT_STRING = "Expenses, %s %s.pdf";

    private static void createExpensesExcelFile(Object[][][] expensesSheetsData, Properties properties) {
        try {
            Workbook workbook = new XSSFWorkbook();
            CellStyle cellStyle = workbook.createCellStyle();
            CreationHelper creationHelper = workbook.getCreationHelper();
            cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat(EXPENSES_DATE_FORMAT));
            int sheetCount = 0;
            for (Object[][] expensesData : expensesSheetsData) {
                Sheet sheet = workbook.createSheet(EXPENSES_SHEET_NAME_PREFIX + sheetCount);
                int rowCount = 0;
                for (Object[] expense : expensesData) {
                    Row row = sheet.createRow(rowCount);
                    int columnCount = 0;
                    for (Object field : expense) {
                        Cell cell = row.createCell(columnCount);
                        if (rowCount > 0 && columnCount == 0) {
                            cell.setCellValue((Date) field);
                            cell.setCellStyle(cellStyle);
                        }
                        else {
                            if (field instanceof String) {
                                cell.setCellValue((String) field);
                            } else if (field instanceof Integer) {
                                cell.setCellValue((Integer) field);
                            } else if (field instanceof Double) {
                                cell.setCellValue((Double) field);
                            }
                        }
                        columnCount++;
                    }
                    rowCount++;
                }
                sheetCount++;
            }

            File directory = new File(EXPENSES_INPUT_DIRECTORY_PATH);
            if (!directory.exists()){
                directory.mkdir();
            }

            FileOutputStream outputStream = new FileOutputStream(properties.getProperty(EXPENSES_INPUT_PATH_KEY));
            workbook.write(outputStream);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object[][][] createExpensesExcelWithOneSheet(Properties properties) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(new Date());

        Object[][][] expensesSheetsData = {
                {
                        {"Date", "Account number", "Description", "Kind", "Price"},
                        {c.getTime(), "Account 1", "Description 1", "Kind 1", 10.00},
                        {c.getTime(), "Account 1", "Description 2", "Kind 1", 10.00},
                        {c.getTime(), "Account 2", "Description 3", "Kind 2", -10.00},
                }
        };
        createExpensesExcelFile(expensesSheetsData, properties);

        return expensesSheetsData;
    }

    public static Object[][][] createExpensesExcelWithMultipleSheet(Properties properties) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(new Date());
        Date date1 = c.getTime();
        c.add(Calendar.MONTH, 1);
        Date date2 = c.getTime();

        Object[][][] expensesSheetsData = {
                {
                        {"Date", "Account number", "Description", "Kind", "Price"},
                        {date1, "Account 1", "Description 1", "Kind 1", 10.00},
                        {date1, "Account 1", "Description 2", "Kind 1", 10.00},
                        {date1, "Account 2", "Description 3", "Kind 2", -10.00},
                },
                {
                        {"Date", "Account number", "Description", "Kind", "Price"},
                        {date2, "Account 1", "Description 1", "Kind 1", 15.00},
                        {date2, "Account 1", "Description 2", "Kind 1", 100.00},
                        {date2, "Account 2", "Description 3", "Kind 2", -10.00},
                }
        };
        createExpensesExcelFile(expensesSheetsData, properties);

        return expensesSheetsData;
    }

    public static Object[][][] createExpensesExcelWithNoData(Properties properties) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(new Date());

        Object[][][] expensesSheetsData = {
                {
                        {"Date", "Account number", "Description", "Kind", "Price"}
                }
        };
        createExpensesExcelFile(expensesSheetsData, properties);

        return expensesSheetsData;
    }

    public static Object[][][] createExpensesExcelWithInvalidHeader1(Properties properties) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(new Date());

        Object[][][] expensesSheetsData = {
                {
                        {"InvalidHeader", "Account number", "Description", "Kind", "Price"},
                }
        };
        createExpensesExcelFile(expensesSheetsData, properties);

        return expensesSheetsData;
    }

    public static Object[][][] createExpensesExcelWithInvalidHeader2(Properties properties) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(new Date());

        Object[][][] expensesSheetsData = {
                {
                        {"Date", 123, "Description", "Kind", "Price"},
                }
        };
        createExpensesExcelFile(expensesSheetsData, properties);

        return expensesSheetsData;
    }

    public static Map<Integer, String> getHeader(Object[] data) {
        Map<Integer, String> header = new HashMap<>();
        int attributeCount = 0;
        for (Object headerElement : data) {
            header.put(attributeCount, (String)headerElement);
            attributeCount++;
        }

        return header;
    }

    public static String getExpectedExpensesPdfFileName(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String month = localDate.getMonth().toString().toLowerCase();
        month = month.substring(0, 1).toUpperCase() + month.substring(1);
        int year = localDate.getYear();

        return String.format(EXPENSES_INPUT_FILE_NAME_FORMAT_STRING, month, year);
    }
}
