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
        String stringDate = readableFormatDate.toString();
        if (stringDate.isEmpty()) {
            return "null";
        }

        String[] split = stringDate.split(" ");
        String[] dateSplit = split[0].split("-");
        String[] timeSplit = split[1].split(":");
        String dateFormat = "YYYY-MM-DD HH24:MI:SS.FF";
        String timestamp = "to_timestamp('"+dateFormat+"', '"+dateSplit[2]+"-"+dateSplit[1]+"-"+dateSplit[0]+" "+
                timeSplit[0]+":"+timeSplit[1]+":"+timeSplit[2]+".0')";
        return timestamp;
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
}
