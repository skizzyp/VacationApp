package com.example.d308.util;

import android.content.Context;
import android.util.Log;
import com.example.d308.database.VacationDatabaseBuilder;
import com.example.d308.database.VacationDatabaseBuilder;
import com.example.d308.entity.Vacation;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class ReportUtil {

    public static void generateAndSaveReport(Context context) {
        // Step 1: Query the data from the database
        List<Vacation> vacations = getVacationsFromDatabase(context);

        // Step 2: Format the data as a text string
        String textData = formatVacationsAsText(vacations);

        // Step 3: Save the formatted text data to a file
        saveToFile(context, textData);
    }

    // Step 1: Query the data from the database
    private static List<Vacation> getVacationsFromDatabase(Context context) {
        VacationDatabaseBuilder db = VacationDatabaseBuilder.getDatabase(context);
        return db.vacationDao().getAllVacations(); // Fetch vacations from database
    }

    // Step 2: Format data as a text report
    private static String formatVacationsAsText(List<Vacation> vacations) {
        StringBuilder textBuilder = new StringBuilder();

        // Add a header for the report
        textBuilder.append("Vacation Report\n\n");

        // Loop through each vacation entry and append it to the text
        for (Vacation vacation : vacations) {
            textBuilder.append("Vacation ID: ").append(vacation.getVacationID()).append("\n");
            textBuilder.append("Title: ").append(vacation.getTitle()).append("\n");
            textBuilder.append("Place: ").append(vacation.getPlace()).append("\n");
            textBuilder.append("Start Date: ").append(vacation.getStartDate()).append("\n");
            textBuilder.append("End Date: ").append(vacation.getEndDate()).append("\n");
            textBuilder.append("---------------------------------\n");
        }

        return textBuilder.toString();
    }

    // Step 3: Save the report data to a text file
    private static void saveToFile(Context context, String data) {
        String filename = "vacations_report.txt";
        File file = new File(context.getFilesDir(), filename);

        try (FileOutputStream fos = new FileOutputStream(file);
             OutputStreamWriter writer = new OutputStreamWriter(fos)) {
            writer.write(data);
            writer.flush();
            Log.d("Report", "File saved successfully at: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Report", "Error saving file", e);
        }
    }
}
