/**
 * Created by Sergiusz on 19.08.2017.
 */
function disableAllAssets() {
    $(".pageContent").addClass('contentDisabled');
}

function enableAllAssets() {
    $(".pageContent").removeClass('contentDisabled');
}

function hideLoader() {
    $(".pageLoader").hide();
}

function showLoader() {
    $(".pageLoader").show();
}

function startLoading() {
    showLoader();
    disableAllAssets();
}

function finishLoading() {
    hideLoader();
    enableAllAssets();
}

function parseDateTimestamp(date) {
    if (date == null || typeof date == 'undefined' || date.length == 0) {
        return "";
    }
    return date.split(" ")[0];
}

function stringifyMonth(month) {
    if (month == 1) return "styczeń";
    else if (month == 2) return "luty";
    else if (month == 3) return "marzec";
    else if (month == 4) return "kwiecień";
    else if (month == 5) return "maj";
    else if (month == 6) return "czerwiec";
    else if (month == 7) return "lipiec";
    else if (month == 8) return "sierpień";
    else if (month == 9) return "wrzesień";
    else if (month == 10) return "październik";
    else if (month == 11) return "listopad";
    else if (month == 12) return "grudzień";
    else return "";
}