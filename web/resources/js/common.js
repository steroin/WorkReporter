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

function getStatusName(status) {
    if (status == 1) return 'Oczekujące';
    else if (status == 2) return 'Zatwierdzone';
    else if (status == 3) return 'Odrzucone';
    else return "";
}

function getStatusClass(status) {
    if (status == 1) return 'label label-primary';
    else if (status == 2) return 'label label-success';
    else if (status == 3) return 'label label-danger';
    else return "";
}

function glowInputWrong(obj) {
    obj.addClass("inputWrong");
    obj.keypress(function() {
        if (obj.hasClass("inputWrong")) {
            $(this).removeClass("inputWrong");
        }
    });
}

function splitDate(date) {
    var split = date.split(" ");
    var dateSplit = split[0].split("-");
    var timeSplit = split.length > 1 ? split[1].split(":") : ['00', '00', '00'];
    return [dateSplit[2], dateSplit[1], dateSplit[0], timeSplit[0], timeSplit[1], timeSplit[2]];
}

function compareDates(dateA, dateB) {
    var dateASplit = splitDate(dateA);
    var dateBSplit = splitDate(dateB);

    for (var i = 0; i < 6; i++) {
        if (dateASplit[i] > dateBSplit[i]) return -1;
        else if (dateASplit[i] < dateBSplit[i]) return 1;
    }
    return 0;
}