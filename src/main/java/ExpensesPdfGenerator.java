import java.io.*;

import java.awt.Graphics2D;
import java.io.FileOutputStream;
import java.util.*;

import com.itextpdf.text.Paragraph;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class ExpensesPdfGenerator {

    private final String PIE_CHART_TITLE_PREFIX = "Expenses, ";
    private final boolean ENABLE_PIE_CHART_LEGEND = false;
    private final boolean ENABLE_PIE_CHART_TOOLTIPS = false;
    private final boolean ENABLE_PIE_CHART_URLS = false;
    private final String OUTPUT_DIRECTORY_PATH_KEY = "OUTPUT_DIRECTORY_PATH";
    private final String EXPENSE_PDF_PATH_PREFIX = "%s/Expenses, %s.pdf";
    private final int PIE_CHART_WIDTH = 500;
    private final int PIE_CHART_HEIGHT = 350;
    private final int PIE_CHART_UPPER_LEFT_X_COORDINATE = 0;
    private final int PIE_CHART_UPPER_LEFT_Y_COORDINATE = 0;
    private final int PDF_TEMPLATE_X_COORDINATE = 40;
    private final int PDF_TEMPLATE_Y_COORDINATE = 450;
    private final int EMPTY_LINES_BEFORE_EXPENSES_SUMMARY = 20;

    private String outputDirectoryPath;
    private Map<String, List<Expense>> data;

    public ExpensesPdfGenerator(Properties properties, Map<String, List<Expense>> data) {
        this.outputDirectoryPath = properties.getProperty(OUTPUT_DIRECTORY_PATH_KEY);
        this.data = data;
    }

    private JFreeChart createPieChart(String dataKey) {
        DefaultPieDataset pieChartData = new DefaultPieDataset();
        List<Expense> expenses = data.get(dataKey);
        Set<String> expenseKinds = new HashSet<>();
        double totalExpensePrice = 0;
        Map<String, Double> expenseKindPrices = new HashMap<>();
        for (Expense expense : expenses) {
            String expenseKind = expense.getKind();
            double expensePrice = Math.abs(expense.getPrice());
            expenseKinds.add(expenseKind);
            totalExpensePrice += expensePrice;
            if (expenseKindPrices.containsKey(expenseKind)) {
                expenseKindPrices.put(expenseKind, expenseKindPrices.get(expenseKind) + expensePrice);
            }
            else {
                expenseKindPrices.put(expenseKind, expensePrice);
            }
        }
        for (String expenseKind : expenseKindPrices.keySet()) {
            double expenseKindPrice = expenseKindPrices.get(expenseKind);
            double expenseKindPricePercentage = (expenseKindPrice / totalExpensePrice) * 100;
            pieChartData.setValue(expenseKind, expenseKindPricePercentage);
        }

        return ChartFactory.createPieChart(
                getPieChartTitle(dataKey),
                pieChartData,
                ENABLE_PIE_CHART_LEGEND,
                ENABLE_PIE_CHART_TOOLTIPS,
                ENABLE_PIE_CHART_URLS);
    }

    private String getPieChartTitle(String dataKey) {
        return PIE_CHART_TITLE_PREFIX + dataKey;
    }

    private void createOutputDirectory() {
        File directory = new File(outputDirectoryPath);
        if (!directory.exists()){
            directory.mkdir();
        }
    }

    private List<Paragraph> getExpensesSummary(String dataKey) {
        List<Paragraph> paragraphs = new ArrayList<>();
        List<Expense> expenses = data.get(dataKey);
        Set<String> expenseKinds = new HashSet<>();
        Map<String, Double> expenseKindPrices = new HashMap<>();
        double totalExpense = 0;
        double totalIncome = 0;
        for (Expense expense : expenses) {
            String expenseKind = expense.getKind();
            double expensePrice = expense.getPrice();
            expenseKinds.add(expenseKind);
            if (expenseKindPrices.containsKey(expenseKind)) {
                expenseKindPrices.put(expenseKind, expenseKindPrices.get(expenseKind) + expensePrice);
            }
            else {
                expenseKindPrices.put(expenseKind, expensePrice);
            }
            if (expensePrice <= 0) {
                totalExpense += Math.abs(expensePrice);
            }
            else {
                totalIncome += Math.abs(expensePrice);
            }
        }
        for (String expenseKind : expenseKindPrices.keySet()) {
            double expenseKindPrice = expenseKindPrices.get(expenseKind);
            paragraphs.add(new Paragraph(String.format("%s: $%.2f", expenseKind, expenseKindPrice)));
        }
        paragraphs.add(new Paragraph(" "));
        paragraphs.add(new Paragraph(String.format("The net income is $%.2f = $%.2f - $%.2f.", (totalIncome - totalExpense), totalIncome, totalExpense)));

        return paragraphs;
    }

    private void createExpensePdf(String dataKey) {
        try {
            createOutputDirectory();
            String pdfFilePath = String.format(EXPENSE_PDF_PATH_PREFIX, outputDirectoryPath, dataKey);
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));

            document.open();

            PdfContentByte pdfContentByte = writer.getDirectContent();
            PdfTemplate pdfTemplate = pdfContentByte.createTemplate(PIE_CHART_WIDTH, PIE_CHART_HEIGHT);
            Graphics2D graphics2d = pdfTemplate.createGraphics(PIE_CHART_WIDTH, PIE_CHART_HEIGHT, new DefaultFontMapper());
            java.awt.geom.Rectangle2D rectangle2d = new java.awt.geom.Rectangle2D.Double(
                    PIE_CHART_UPPER_LEFT_X_COORDINATE,
                    PIE_CHART_UPPER_LEFT_Y_COORDINATE,
                    PIE_CHART_WIDTH,
                    PIE_CHART_HEIGHT);
            JFreeChart pieChart = createPieChart(dataKey);
            pieChart.draw(graphics2d, rectangle2d);
            graphics2d.dispose();
            pdfContentByte.addTemplate(pdfTemplate, PDF_TEMPLATE_X_COORDINATE, PDF_TEMPLATE_Y_COORDINATE);
            for (int i = 0; i < EMPTY_LINES_BEFORE_EXPENSES_SUMMARY; i++) {
                document.add(new com.itextpdf.text.Paragraph(" "));
            }
            for (Paragraph paragraph : getExpensesSummary(dataKey)) {
                document.add(paragraph);
            }

            document.close();
            System.out.println("Expenses PDF created in: " + pdfFilePath);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void generateExpensesPdfs() {
        Iterator<String> dataKeyIterator = data.keySet().iterator();
        while (dataKeyIterator.hasNext()) {
            createExpensePdf(dataKeyIterator.next());
        }
    }
}
