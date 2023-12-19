package com.example.crfid.common;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

public class CommonFunctions {
    public static String convertDate(String dateString) {
        String[] parts = dateString.split("\\(");
        String millisecondsStr = parts[1].replaceAll("[^\\d]", "");
        long milliseconds = Long.parseLong(millisecondsStr);

        Date date = new Date(milliseconds);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(date);
    }

    public static String convertDuration(String durationString) {
        // Extracting hours, minutes, and seconds from the string
        int hours = 0, minutes = 0, seconds = 0;

        if (durationString.contains("H")) {
            String[] hoursSplit = durationString.split("H");
            hours = Integer.parseInt(hoursSplit[0].replace("PT", ""));
            durationString = hoursSplit[1];
        }

        if (durationString.contains("M")) {
            String[] minutesSplit = durationString.split("M");
            minutes = Integer.parseInt(minutesSplit[0]);
            durationString = minutesSplit[1];
        }

        if (durationString.contains("S")) {
            String[] secondsSplit = durationString.split("S");
            seconds = Integer.parseInt(secondsSplit[0]);
        }

        // Constructing the readable format
        String formattedDuration = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        return formattedDuration;
    }

    public static void showAlert( Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    
}
