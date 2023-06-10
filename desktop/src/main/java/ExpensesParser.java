import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExpensesParser {

    private final String EXPENSES_INPUT_PATH_KEY = "EXPENSES_INPUT_PATH";

    private String expensesFilePath;

    public ExpensesParser(Properties properties) {
        this.expensesFilePath = properties.getProperty(EXPENSES_INPUT_PATH_KEY);
    }

    private List<String> getTheHeader(Row row) throws IllegalArgumentException {
        List<String> header = new ArrayList<>();
        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            switch (cell.getCellType()) {
                case STRING:
                    header.add(cell.getStringCellValue());
                    break;
                case BLANK:
                    break;
                default:
                    throw new IllegalArgumentException("Header is not a string");
            }
        }

        return header;
    }

    private void validateHeader(List<String> header) throws IllegalArgumentException {
        ExpenseHeader[] expenseHeaderValues = ExpenseHeader.values();
        for (ExpenseHeader expenseHeaderValue : expenseHeaderValues) {
            String expenseHeaderValueString = expenseHeaderValue.toString();
            if (!header.contains(expenseHeaderValueString)) {
                throw new IllegalArgumentException("Header does not include " + expenseHeaderValueString);
            }
        }
    }

    private Object getCellValue(Cell cell) {
        Object cellValue = null;
        switch (cell.getCellType()) {
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            case NUMERIC:
                cellValue = DateUtil.isCellDateFormatted(cell) ? cell.getDateCellValue() : cell.getNumericCellValue();
                break;
            default:
        }

        return cellValue;
    }

    private boolean isColumnEmpty(Object columnValue) {
        return columnValue == null ||
                (columnValue instanceof String && ((String) columnValue).trim().isEmpty());
    }

    private String getDataKey(Expense expense) {
        Date date = expense.getDate();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String month = localDate.getMonth().toString().toLowerCase();
        month = month.substring(0, 1).toUpperCase() + month.substring(1);
        int year = localDate.getYear();

        return String.format("%s %s", month, year);
    }

    private List<Expense> getTransactions(Sheet sheet) {
        List<Expense> expenses = new ArrayList<>();
        Iterator<Row> rowIterator = sheet.iterator();
        List<String> header = null;
        if (rowIterator.hasNext()) {
            header = getTheHeader(rowIterator.next());
            validateHeader(header);
        }
        while (rowIterator.hasNext()) {
            Expense expense = null;
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            int columnIndex = 0;
            while (cellIterator.hasNext() && columnIndex <= header.size() - 1) {
                String columnName = header.get(columnIndex);
                Cell cell = cellIterator.next();
                Object cellValue = getCellValue(cell);
                if (columnName.equalsIgnoreCase(ExpenseHeader.DATE.toString()) && isColumnEmpty(cellValue)) {
                    break;
                }
                if (expense == null) {
                    expense = new Expense();
                }
                if (columnName.equalsIgnoreCase(ExpenseHeader.DATE.toString())) {
                    expense.setDate((Date) cellValue);
                }
                else if (columnName.equalsIgnoreCase(ExpenseHeader.ACCOUNT_NUMBER.toString())) {
                    expense.setAccountNumber((String) cellValue);
                }
                else if (columnName.equalsIgnoreCase(ExpenseHeader.DESCRIPTION.toString())) {
                    expense.setDescription((String) cellValue);
                }
                else if (columnName.equalsIgnoreCase(ExpenseHeader.KIND.toString())) {
                    expense.setKind((String) cellValue);
                }
                else if (columnName.equalsIgnoreCase(ExpenseHeader.PRICE.toString())) {
                    expense.setPrice((Double) cellValue);
                }
                columnIndex++;
            }
            if (expense != null) {
                expenses.add(expense);
            }
        }
        return expenses;
    }

    public Map<String, List<Expense>> getData() throws Exception {
        Map<String, List<Expense>> data = new HashMap<>();
        File file = new File(expensesFilePath);
        FileInputStream fileInputStream = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        Iterator<Sheet> sheetIterator = workbook.iterator();
        while (sheetIterator.hasNext()) {
            Sheet sheet = sheetIterator.next();
            List<Expense> expenses = getTransactions(sheet);
            if (expenses.iterator().hasNext()) {
                String dataKey = getDataKey(expenses.iterator().next());
                data.put(dataKey, expenses);
            }
        }

        return data;
    }

    public static void main(String[] args)
    {
        try {
            String configFilePath = "src/main/java/config.properties";
            FileInputStream configFileInputStream = new FileInputStream(configFilePath);
            Properties properties = new Properties();
            properties.load(configFileInputStream);

            ExpensesParser expensesParser = new ExpensesParser(properties);
            ExpensesPdfGenerator expensesPdfGenerator = new ExpensesPdfGenerator(properties, expensesParser.getData());
            expensesPdfGenerator.generateExpensesPdfs();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}  