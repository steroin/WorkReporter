package pl.workreporter.web.beans.entities.reports;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Sergiusz on 27.10.2017.
 */
public class Report implements Serializable {

    private List<String> columns;
    private List<List<String>> content;

    public Report(String... cols) {
        columns = new ArrayList<>();

        for (String column : cols) {
            columns.add(column);
        }
        content = new ArrayList<>();
    }

    public void addRow(String... row) {
        if (row.length != columns.size()) {
            throw new IllegalArgumentException("Row size doesn't match reports columns number.");
        }

        List<String> rowList = new ArrayList<>();
        Collections.addAll(rowList, row);
        content.add(rowList);
    }

    public List<String> getColumns() {
        return columns;
    }

    public List<List<String>> getContent() {
        return content;
    }
}
