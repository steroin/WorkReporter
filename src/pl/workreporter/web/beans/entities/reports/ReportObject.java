package pl.workreporter.web.beans.entities.reports;

/**
 * Created by Sergiusz on 27.10.2017.
 */
public enum ReportObject {
    EMPLOYEE(0),
    PROJECT(1),
    POSITION(2),
    TEAM(3);

    private final int value;
    ReportObject(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ReportObject fromValue(int value) {
        ReportObject ret = ReportObject.EMPLOYEE;

        switch (value) {
            case 0:
                ret = ReportObject.EMPLOYEE;
                break;
            case 1:
                ret = ReportObject.PROJECT;
                break;
            case 2:
                ret = ReportObject.POSITION;
                break;
            case 3:
                ret = ReportObject.TEAM;
                break;
        }
        return ret;
    }
}
