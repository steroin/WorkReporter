package pl.workreporter.web.service.date;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

/**
 * Created by Sergiusz on 20.08.2017.
 */
@Service
public class DateParser {
    public String parseToDatabaseTimestamp(Object readableFormatDate) {
        if (readableFormatDate == null) {
            return "null";
        }
        String stringDate = readableFormatDate.toString().trim();
        if (stringDate.isEmpty()) {
            return "null";
        }

        String[] split = stringDate.split(" ");
        String[] dateSplit = split[0].split("-");
        String time = "00:00:00.0";
        if (split.length > 1) {
            String[] timeSplit = split[1].split(":");
            time = timeSplit[0]+":"+timeSplit[1]+":"+timeSplit[2]+".0";
        }
        String dateFormat = "YYYY-MM-DD HH24:MI:SS.FF";

        String date = dateSplit[2]+"-"+dateSplit[1]+"-"+dateSplit[0];
        return "to_timestamp('"+date+" "+time+"', '"+dateFormat+"')";
    }

    public String parseToReadableDate(Object databaseTimestamp) {
        if (databaseTimestamp == null) {
            return "";
        }
        String stringDate = databaseTimestamp.toString();
        if (stringDate.isEmpty()) {
            return "";
        }
        String[] split = stringDate.split(" ");
        String[] dateSplit = split[0].split("-");
        String[] timeSplit = split[1].split(":");
        String[] secondsSplit = timeSplit[2].split("\\.");
        String timestamp = dateSplit[2]+"-"+dateSplit[1]+"-"+dateSplit[0]+" "+timeSplit[0]+":"+timeSplit[1]+":"+secondsSplit[0];
        return timestamp;
    }

    public String makeParsableDate(String date) {
        if (date == null || date.isEmpty()) return "";
        String[] split = date.split(" ");

        if (split.length == 0) {
            return "";
        }

        String[] dateSplit = split[0].split("-");
        if (dateSplit[0].length() < 2) dateSplit[0] = "0"+dateSplit[0];
        if (dateSplit[1].length() < 2) dateSplit[1] = "0"+dateSplit[1];

        String[] timeSplit = {"00", "00", "00"};
        if (split.length > 1) {
            String[] givenTime = split[1].split(":");
            timeSplit = new String[] { givenTime[0], givenTime[1], "00" };

            if (givenTime[0].length() < 2) timeSplit[0] = "0"+givenTime[0];
            if (givenTime[1].length() < 2) timeSplit[1] = "0"+givenTime[1];
            if (givenTime.length > 2) {
                if (givenTime[2].length() < 2) timeSplit[2] = "0"+givenTime[2];
            } else {
                timeSplit[2] = "00";
            }
        }
        return dateSplit[0] + "-" + dateSplit[1] + "-" + dateSplit[2] + " " + timeSplit[0] + ":" + timeSplit[1] + ":" + timeSplit[2];
    }
}
