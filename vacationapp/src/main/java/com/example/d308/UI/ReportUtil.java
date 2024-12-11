package com.example.d308.util;

import android.content.Context;
import com.example.d308.database.VacationDatabaseBuilder;
import com.example.d308.entity.Vacation;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReportUtil {

    public static void generateVacationReport(final Context context) {
        // Run on a background thread to avoid blocking the UI
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Get the database instance using the custom builder
                VacationDatabaseBuilder db = VacationDatabaseBuilder.getDatabase(context);

                // Fetch all vacation data from the database
                List<Vacation> vacations = db.vacationDao().getAllVacations();

                // Generate the report content based on the fetched data
                String reportContent = generateReportContent(vacations);

                // Save the generated report to a file
                saveReportToFile(context, reportContent);
            }
        }).start();
    }

    // Helper method to generate report content from vacation data
    private static String generateReportContent(List<Vacation> vacations) {
        StringBuilder reportBuilder = new StringBuilder();

        // Add a timestamp to the report header
        String timestamp = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
        reportBuilder.append("Report generated on: ").append(timestamp).append("\n\n");

        // Add the column headers for the report
        reportBuilder.append(String.format("%-10s %-20s %-20s %-15s %-15s\n",
                "ID", "Title", "Place", "Start Date", "End Date"));
        reportBuilder.append("-------------------------------------------------------------\n");

        // Loop through each vacation and add its details to the report
        for (Vacation vacation : vacations) {
            reportBuilder.append(String.format("%-10d %-20s %-20s %-15s %-15s\n",
                    vacation.getVacationID(), vacation.getTitle(), vacation.getPlace(),
                    vacation.getStartDate(), vacation.getEndDate()));
        }

        return reportBuilder.toString();
    }

    // Save the report content to a file on internal storage
    private static void saveReportToFile(Context context, String reportContent) {
        // Create a timestamped filename to avoid overwriting
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = "vacation_report_" + timestamp + ".txt";

        // Write the report content to a file in the app's internal storage
        File reportFile = new File(context.getFilesDir(), filename);

        try (FileOutputStream fos = new FileOutputStream(reportFile);
             OutputStreamWriter writer = new OutputStreamWriter(fos)) {
            writer.write(reportContent);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the error if file writing fails
        }
    }
}
