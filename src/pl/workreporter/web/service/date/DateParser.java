package pl.workreporter.web.service.date;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

/**
 * Created by Sergiusz on 20.08.2017.
 */
@Service
public class DateParser {
    public String parseToDatabaseTimestamp(String readableFormatDate) {
        if (readableFormatDate == null || readableFormatDate.isEmpty()) {
            return "null";
        }
        String[] split = readableFormatDate.split(" ");
        String[] dateSplit = split[0].split("-");
        String[] timeSplit = split[1].split(":");
        String dateFormat = "YYYY-MM-DD HH24:MI:SS.FF";
        String timestamp = "to_timestamp('"+dateFormat+"', '"+dateSplit[2]+"-"+dateSplit[1]+"-"+dateSplit[0]+" "+
                timeSplit[0]+":"+timeSplit[1]+":"+timeSplit[2]+".0')";
        return timestamp;
    }

    public String parseToReadableDate(String databaseTimestamp) {
        if (databaseTimestamp == null || databaseTimestamp.isEmpty()) {
            return "";
        }
        String[] split = databaseTimestamp.split(" ");
        String[] dateSplit = split[0].split("-");
        String[] timeSplit = split[1].split(":");
        String[] secondsSplit = timeSplit[2].split("\\.");
        String timestamp = dateSplit[2]+"-"+dateSplit[1]+"-"+dateSplit[2]+" "+timeSplit[0]+":"+timeSplit[1]+":"+secondsSplit[0];
        return timestamp;
    }
}
