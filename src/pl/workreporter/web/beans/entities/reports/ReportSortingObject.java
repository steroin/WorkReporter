package pl.workreporter.web.beans.entities.reports;

import javafx.geometry.Pos;

/**
 * Created by Sergiusz on 27.10.2017.
 */
public enum ReportSortingObject {
    EMPLOYEE_SURNAME(0),
    EMPLOYEE_FIRSTNAME(1),
    EMPLOYEE_LOGIN(2),
    EMPLOYEE_EMAIL(3),
    EMPLOYEE_TEAM(4),
    EMPLOYEE_POSITION(5),
    EMPLOYEE_LOGGED_HOURS(6),
    PROJECT_NAME(0),
    PROJECT_LOGGED_HOURS(1),
    POSITION_NAME(0),
    POSITION_LOGGED_HOURS(1),
    TEAM_NAME(0),
    TEAM_LEADER(1),
    TEAM_LOGGED_HOURS(2);

    private final int value;
    ReportSortingObject(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    public static ReportSortingObject fromTypeAndValue(ReportObject type, int value) {
        ReportSortingObject ret = null;

        switch (type) {
        case EMPLOYEE:
            switch (value) {
            case 0:
                ret = EMPLOYEE_SURNAME;
                break;
            case 1:
                ret = EMPLOYEE_FIRSTNAME;
                break;
            case 2:
                ret = EMPLOYEE_LOGIN;
                break;
            case 3:
                ret = EMPLOYEE_EMAIL;
                break;
            case 4:
                ret = EMPLOYEE_TEAM;
                break;
            case 5:
                ret = EMPLOYEE_POSITION;
                break;
            case 6:
                ret = EMPLOYEE_LOGGED_HOURS;
                break;
            }
            break;
        case PROJECT:
            switch (value) {
            case 0:
                ret = PROJECT_NAME;
                break;
            case 1:
                ret = PROJECT_LOGGED_HOURS;
                break;
            }
            break;
        case POSITION:
            switch (value) {
            case 0:
                ret = POSITION_NAME;
                break;
            case 1:
                ret = POSITION_LOGGED_HOURS;
                break;
            }
            break;
        case TEAM:
            switch (value) {
            case 0:
                ret = TEAM_NAME;
                break;
            case 1:
                ret = TEAM_LEADER;
                break;
            case 2:
                ret = TEAM_LOGGED_HOURS;
                break;
            }
            break;
        }
        return ret;
    }
}
