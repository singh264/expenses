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

    public static List<String> obtainTheNameOfTheColumns(Spreadsheet spreadsheet) {
        List<String> nameOfTheColumns = new ArrayList<>();
        Sheet sheet = spreadsheet.getSheets().get(0);
        GridData gridData = sheet.getData().get(0);
        List<RowData> rows = gridData.getRowData();
        for (RowData row : rows) {
            List<CellData> rowData = row.getValues();
            if (rowData != null) {
                boolean isTheRowStruckthrough = true;
                for (CellData cellData : rowData) {
                    if (cellData.getEffectiveFormat() != null && !cellData.getEffectiveFormat().getTextFormat().getStrikethrough()) {
                        isTheRowStruckthrough = false;
                        break;
                    }
                }
                if (!isTheRowStruckthrough) {
                    for (CellData cellData : rowData) {
                        if (cellData.getEffectiveFormat() != null && !cellData.getEffectiveFormat().getTextFormat().getStrikethrough()) {
                            nameOfTheColumns.add(cellData.getFormattedValue());
                        }
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
        int kindOfExpenseColumnIndex = nameOfTheColumns.indexOf("Kind");
        Sheet sheet = spreadsheet.getSheets().get(0);
        GridData gridData = sheet.getData().get(0);
        List<RowData> rows = gridData.getRowData();
        boolean foundTheFirstRow = false;
        for (RowData row : rows) {
            List<CellData> rowData = row.getValues();
            if (rowData != null) {
                if (!foundTheFirstRow) {
                    boolean isTheRowStruckthrough = true;
                    for (CellData cellData : rowData) {
                        if (cellData.getEffectiveFormat() != null && !cellData.getEffectiveFormat().getTextFormat().getStrikethrough()) {
                            isTheRowStruckthrough = false;
                            break;
                        }
                    }
                    if (!isTheRowStruckthrough) {
                        foundTheFirstRow = true;
                        continue;
                    }
                }
                if (rowData.size() > kindOfExpenseColumnIndex) {
                    CellData cellData = rowData.get(kindOfExpenseColumnIndex);
                    if (cellData.getEffectiveFormat() != null && !cellData.getEffectiveFormat().getTextFormat().getStrikethrough()) {
                        String kindOfTheExpense = cellData.getFormattedValue();
                        kindsOfTheExpense.add(kindOfTheExpense);
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
            valuesOfThePieChart.put(kindOfTheExpense, 0D);
        }
        List<String> nameOfTheColumns = obtainTheNameOfTheColumns(spreadsheet);
        int kindOfTheExpenseColumnIndex = nameOfTheColumns.indexOf("Kind");
        int priceInOfTheExpenseColumnIndex = nameOfTheColumns.indexOf("Price in");
        int priceOutOfTheExpenseColumnIndex = nameOfTheColumns.indexOf("Price out");
        Sheet sheet = spreadsheet.getSheets().get(0);
        GridData gridData = sheet.getData().get(0);
        List<RowData> rows = gridData.getRowData();
        boolean foundTheFirstRow = false;
        for (RowData row : rows) {
            List<CellData> rowData = row.getValues();
            if (rowData != null) {
                if (!foundTheFirstRow) {
                    boolean isTheRowStruckthrough = true;
                    for (CellData cellData : rowData) {
                        if (cellData.getEffectiveFormat() != null && !cellData.getEffectiveFormat().getTextFormat().getStrikethrough()) {
                            isTheRowStruckthrough = false;
                            break;
                        }
                    }
                    if (!isTheRowStruckthrough) {
                        foundTheFirstRow = true;
                        continue;
                    }
                }
                if (rowData.size() > kindOfTheExpenseColumnIndex) {
                    CellData cellData = rowData.get(kindOfTheExpenseColumnIndex);
                    if (cellData.getEffectiveFormat() != null && !cellData.getEffectiveFormat().getTextFormat().getStrikethrough()) {
                        String kindOfTheExpense = cellData.getFormattedValue();
                        Double value = valuesOfThePieChart.get(kindOfTheExpense);
                        cellData = rowData.get(priceInOfTheExpenseColumnIndex);
                        if (cellData.getEffectiveFormat() != null && !cellData.getEffectiveFormat().getTextFormat().getStrikethrough() && cellData.getFormattedValue() != null && !cellData.getFormattedValue().isEmpty()) {
                            Double priceInOfTheExpense = Double.valueOf(cellData.getFormattedValue().replace(",", ""));
                            value += priceInOfTheExpense;
                        }
                        cellData = rowData.get(priceOutOfTheExpenseColumnIndex);
                        if (cellData.getEffectiveFormat() != null && !cellData.getEffectiveFormat().getTextFormat().getStrikethrough() && cellData.getFormattedValue() != null && !cellData.getFormattedValue().isEmpty()) {
                            Double priceOutOfTheExpense = Double.valueOf(cellData.getFormattedValue().replace(",", ""));
                            value += priceOutOfTheExpense;
                        }
                        valuesOfThePieChart.put(kindOfTheExpense, value);
                    }
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
