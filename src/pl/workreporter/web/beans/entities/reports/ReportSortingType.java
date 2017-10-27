package pl.workreporter.web.beans.entities.reports;

/**
 * Created by Sergiusz on 27.10.2017.
 */
public enum ReportSortingType {
    ASCENDING(0),
    DESCENDING(1);

    private final int value;
    ReportSortingType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    public static ReportSortingType fromValue(int value) {
        ReportSortingType ret = ReportSortingType.ASCENDING;

        switch (value) {
            case 0:
                ret = ReportSortingType.ASCENDING;
                break;
            case 1:
                ret = ReportSortingType.DESCENDING;
                break;
        }
        return ret;
    }
}