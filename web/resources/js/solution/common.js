/**
 * Created by Sergiusz on 19.08.2017.
 */
function disableAllAssets() {
    $(".solutionContent").addClass('contentDisabled');
}

function enableAllAssets() {
    $(".solutionContent").removeClass('contentDisabled');
}

function hideLoader() {
    $("#solutionChooserLoader").hide();
}

function showLoader() {
    $("#solutionChooserLoader").show();
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