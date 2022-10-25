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
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
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

    /**
     * Prints the names and majors of students in a sample spreadsheet:
     * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
     */
    public static void main(String... args) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1yfUiM2l0mJLdDUY3T1meGf9G7GHVjcKhzqCp2MRPVFk";
        final String range = "A:G";
        Sheets service =
                new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                        .setApplicationName(APPLICATION_NAME)
                        .build();
        Spreadsheet spreadsheet = service.spreadsheets()
                .get(spreadsheetId)
                .setRanges(List.of(range))
                .setIncludeGridData(true)
                .execute();
        Sheet sheet = spreadsheet.getSheets().get(0);
        GridData gridData = sheet.getData().get(0);
        List<RowData> rows = gridData.getRowData();
        for (RowData row : rows) {
            List<CellData> rowData = row.getValues();
            if (rowData != null && rowData.get(0).getFormattedValue() != null) {
                System.out.println(rowData.get(0).getFormattedValue());
            }
        }
        findTheDataWithTheStrikethroughInTheSpreadsheet(spreadsheet);
        List<String> nameOfTheColumns = obtainTheNameOfTheColumns(spreadsheet);
        System.out.println(nameOfTheColumns);
        displayThePieChart();
    }

    @Override
    public void start(Stage stage) {
        // set title for the stage
        stage.setTitle("Creating Pie Chart");

        // piechart data
        PieChart.Data data[] = new PieChart.Data[5];

        // string and integer data
        String status[] = {"Correct Answer", "Wrong Answer",
                "Compilation Error", "Runtime Error",
                "Others" };

        int values[] = {20, 30, 10, 4, 2};

        for (int i = 0; i < 5; i++) {
            data[i] = new PieChart.Data(status[i], values[i]);
        }

        // create a pie chart
        PieChart pie_chart = new
                PieChart(FXCollections.observableArrayList(data));

        // create a Group
        Group group = new Group(pie_chart);

        // create a scene
        Scene scene = new Scene(group, 500, 300);

        // set the scene
        stage.setScene(scene);

        stage.show();
    }
}
