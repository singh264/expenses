import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.GridData;
import com.google.api.services.sheets.v4.model.RowData;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;

import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.collections.FXCollections;

public class SheetsQuickstart extends Application {
    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String KIND_OF_EXPENSE_COLUMN_NAME = "Kind";
    private static final String PRICE_IN_OF_EXPENSE_COLUMN_NAME = "Price in";
    private static final String PRICE_OUT_OF_EXPENSE_COLUMN_NAME = "Price out";
    private static final double VALUE_OF_THE_EXPENSE = 0D;

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES =
            Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/Users/singh264/git/expenses/credentials.json";

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // Load client secrets.
//        InputStream in = SheetsQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        InputStream in = new FileInputStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static void findTheDataWithTheStrikethroughInTheSpreadsheet(Spreadsheet spreadsheet) {
        Path fileName = Path.of("/Users/singh264/git/expenses/dataWithTheStrikethroughInTheSpreadsheet.txt");
        try {
            Files.deleteIfExists(fileName);
            Files.createFile(fileName);
            Files.writeString(fileName, "rowNumber, columnNumber, data\n", StandardOpenOption.WRITE);
            Sheet sheet = spreadsheet.getSheets().get(0);
            GridData gridData = sheet.getData().get(0);
            List<RowData> rows = gridData.getRowData();
            int rowNumber = 1;
            for (RowData row : rows) {
                List<CellData> rowData = row.getValues();
                if (rowData != null) {
                    int columnNumber = 1;
                    for (CellData cellData : rowData) {
                        if (cellData.getEffectiveFormat() != null && cellData.getEffectiveFormat().getTextFormat().getStrikethrough()) {
                            Files.writeString(fileName, String.format("%d, %d, %s\n", rowNumber, columnNumber, cellData.getFormattedValue()), StandardOpenOption.APPEND);
                        }
                        columnNumber++;
                    }
                }
                rowNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void displayThePieChart() {
        launch();
    }

    public static double obtainTheNumericValueOfTheCell(CellData cell) {
        return Double.valueOf(cell.getFormattedValue().replace(",", ""));
    }

    public static List<RowData> obtainTheRowsOfTheSpreadsheet(Spreadsheet spreadsheet) {
        Sheet sheet = spreadsheet.getSheets().get(0);
        GridData gridData = sheet.getData().get(0);
        return gridData.getRowData();
    }

    public static boolean isTheDataInTheCell(CellData cell) {
        return !cell.isEmpty() &&
               !isTheCellStruckthrough(cell) &&
               cell.getFormattedValue() != null &&
               !cell.getFormattedValue().isEmpty();
    }

    public static boolean isTheCellStruckthrough(CellData cell) {
        return cell.getEffectiveFormat() != null &&
               cell.getEffectiveFormat().getTextFormat().getStrikethrough();
    }

    public static boolean isTheRowStruckthrough(List<CellData> cells) {
        boolean isTheRowStruckthrough = true;
        for (CellData cell : cells) {
            if (!isTheCellStruckthrough(cell)) {
                isTheRowStruckthrough = false;
                break;
            }
        }
        return isTheRowStruckthrough;
    }

    public static List<String> obtainTheNameOfTheColumns(Spreadsheet spreadsheet) {
        List<String> nameOfTheColumns = new ArrayList<>();
        List<RowData> rows = obtainTheRowsOfTheSpreadsheet(spreadsheet);
        for (RowData row : rows) {
            List<CellData> cells = row.getValues();
            if (cells != null) {
                if (!isTheRowStruckthrough(cells)) {
                    for (CellData cell : cells) {
                        nameOfTheColumns.add(cell.getFormattedValue());
                    }
                    break;
                }
            }
        }
        return nameOfTheColumns;
    }

    public static Set<String> findTheKindsOfTheExpense(Spreadsheet spreadsheet) {
        Set<String> kindsOfTheExpense = new HashSet<>();
        List<String> nameOfTheColumns = obtainTheNameOfTheColumns(spreadsheet);
        int kindOfExpenseColumnIndex = nameOfTheColumns.indexOf(KIND_OF_EXPENSE_COLUMN_NAME);
        List<RowData> rows = obtainTheRowsOfTheSpreadsheet(spreadsheet);;
        boolean foundTheFirstRow = false;
        for (RowData row : rows) {
            List<CellData> cells = row.getValues();
            if (cells != null) {
                if (!foundTheFirstRow) {
                    if (!isTheRowStruckthrough(cells)) {
                        foundTheFirstRow = true;
                        continue;
                    }
                }
                if (cells.size() > kindOfExpenseColumnIndex) {
                    CellData cell = cells.get(kindOfExpenseColumnIndex);
                    if (!isTheCellStruckthrough(cell)) {
                        kindsOfTheExpense.add(cell.getFormattedValue());
                    }
                }
            }
        }
        return kindsOfTheExpense;
    }

    public static Map<String, Double> findTheValuesOfThePieChart(Spreadsheet spreadsheet) {
        Map<String, Double> valuesOfThePieChart = new HashMap<>();
        Set<String> kindsOfTheExpense = findTheKindsOfTheExpense(spreadsheet);
        for (String kindOfTheExpense : kindsOfTheExpense) {
            valuesOfThePieChart.put(kindOfTheExpense, VALUE_OF_THE_EXPENSE);
        }
        List<String> nameOfTheColumns = obtainTheNameOfTheColumns(spreadsheet);
        int kindOfTheExpenseColumnIndex = nameOfTheColumns.indexOf(KIND_OF_EXPENSE_COLUMN_NAME);
        int priceInOfTheExpenseColumnIndex = nameOfTheColumns.indexOf(PRICE_IN_OF_EXPENSE_COLUMN_NAME);
        int priceOutOfTheExpenseColumnIndex = nameOfTheColumns.indexOf(PRICE_OUT_OF_EXPENSE_COLUMN_NAME);
        List<RowData> rows = obtainTheRowsOfTheSpreadsheet(spreadsheet);;
        boolean foundTheFirstRow = false;
        for (RowData row : rows) {
            List<CellData> cells = row.getValues();
            if (cells != null) {
                if (!foundTheFirstRow) {
                    if (!isTheRowStruckthrough(cells)) {
                        foundTheFirstRow = true;
                        continue;
                    }
                }
                CellData cell = cells.get(kindOfTheExpenseColumnIndex);
                if (isTheDataInTheCell(cell)) {
                    String kindOfTheExpense = cell.getFormattedValue();
                    Double value = valuesOfThePieChart.get(kindOfTheExpense);
                    cell = cells.get(priceInOfTheExpenseColumnIndex);
                    if (isTheDataInTheCell(cell)) {
                        value += obtainTheNumericValueOfTheCell(cell);
                    }
                    cell = cells.get(priceOutOfTheExpenseColumnIndex);
                    if (isTheDataInTheCell(cell)) {
                        value += obtainTheNumericValueOfTheCell(cell);
                    }
                    valuesOfThePieChart.put(kindOfTheExpense, value);
                }
            }
        }
        return valuesOfThePieChart;
    }

    public static int findTheNumberOfSheetsInTheSpreadsheet(String spreadsheetId) {
        int numberOfSheetsInTheSpreadsheet = 0;
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Sheets service =
                    new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                            .setApplicationName(APPLICATION_NAME)
                            .build();
            int sheetNumber = 1;
            while (true) {
                final String range = String.format("Sheet%d!A:G", sheetNumber);
                service.spreadsheets()
                        .get(spreadsheetId)
                        .setRanges(List.of(range))
                        .setIncludeGridData(true)
                        .execute();
                sheetNumber++;
                numberOfSheetsInTheSpreadsheet++;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return numberOfSheetsInTheSpreadsheet;
    }

    /**
     * Prints the names and majors of students in a sample spreadsheet:
     * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
     */
    public static void main(String... args) {
        displayThePieChart();
    }

    @Override
    public void start(Stage stage) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1yfUiM2l0mJLdDUY3T1meGf9G7GHVjcKhzqCp2MRPVFk";
        Sheets service =
                new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                        .setApplicationName(APPLICATION_NAME)
                        .build();
        List<PieChart> pieCharts = new ArrayList<>();
        int numberOfSheets = findTheNumberOfSheetsInTheSpreadsheet(spreadsheetId);
        for (int sheetNumber = 1; sheetNumber <= numberOfSheets; sheetNumber++) {
            final String range = String.format("Sheet%d!A:G", sheetNumber);
            Spreadsheet spreadsheet = service.spreadsheets()
                    .get(spreadsheetId)
                    .setRanges(List.of(range))
                    .setIncludeGridData(true)
                    .execute();
            Map<String, Double> valuesOfThePieChart = findTheValuesOfThePieChart(spreadsheet);
            PieChart.Data pieChartData [] = new PieChart.Data[valuesOfThePieChart.size()];
            int index = 0;
            for (String key : valuesOfThePieChart.keySet()) {
                double value = valuesOfThePieChart.get(key);
                pieChartData[index] = new PieChart.Data(key, value);
                index++;
            }
            pieCharts.add(new PieChart(FXCollections.observableArrayList(pieChartData)));
        }
        VBox vbox = new VBox();
        for (PieChart pieChart: pieCharts){
            vbox.getChildren().add(pieChart);
        }
        stage.setTitle("Expenses");
        stage.setScene(new Scene(vbox, 5000, 3000));
        stage.show();
    }
}
