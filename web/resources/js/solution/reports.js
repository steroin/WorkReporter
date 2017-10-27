/**
 * Created by Sergiusz on 26.10.2017.
 */
function initSolutionReportsManagement($scope, $http) {

    $scope.activeSolutionReportsContent = function() {
        startLoading();
        $scope.activeContent('solutionReports', 'solutionMenuReports');
        $scope.setUpSortingObjects(OBJECT_EMPLOYEE);
        $("#reportObject").change(function() {
            $scope.setUpSortingObjects(parseInt($("#reportObject").val()));
            $scope.$apply();
        });
        finishLoading();
    };

    $scope.setUpSortingObjects = function(object) {
        if (object === OBJECT_EMPLOYEE) $scope.sortingObjects = employeeSortingObjects;
        else if (object === OBJECT_PROJECT) $scope.sortingObjects = projectSortingObjects;
        else if (object === OBJECT_POSITION) $scope.sortingObjects = positionSortingObjects;
        else if (object === OBJECT_TEAM) $scope.sortingObjects = teamSortingObjects;
    };

    $scope.generateReport = function() {
        var reportObject = $("#reportObject").val();
        var sortingObject = $("#reportSortingObject").val();
        var sortingType = $("#reportSortingType").val();
        var dateFrom = $("#reportPeriodFrom").val();
        var dateTo = $("#reportPeriodTo").val();

        startLoading();
        $http.get('solution/reports/'+reportObject+'/'+sortingObject+'/'+sortingType+'/'+dateFrom+'/'+dateTo).then(function(data) {
            $scope.currentReport = data.data.response;
            $('#showReportModal').modal('show');
            finishLoading();
        });
    }
}

function validateDate(date) {

};

var OBJECT_EMPLOYEE = 0;
var OBJECT_PROJECT = 1;
var OBJECT_POSITION = 2;
var OBJECT_TEAM = 3;

var employeeSortingObjects = [
    {'value' : 0, 'name' : "Nazwisko"},
    {'value' : 1, 'name' : "Imię"},
    {'value' : 2, 'name' : "Login"},
    {'value' : 3, 'name' : "Adres email"},
    {'value' : 4, 'name' : "Zespół"},
    {'value' : 5, 'name' : "Stanowisko"},
    {'value' : 6, 'name' : "Przepracowane godziny"}
];
var projectSortingObjects = [
    {'value' : 0, 'name' : "Nazwa projektu"},
    {'value' : 1, 'name' : "Przepracowane godziny"}
];
var positionSortingObjects = [
    {'value' : 0, 'name' : "Nazwa stanowiska"},
    {'value' : 1, 'name' : "Przepracowane godziny"}
];
var teamSortingObjects = [
    {'value' : 0, 'name' : "Nazwa zespołu"},
    {'value' : 1, 'name' : "Lider zespołu"},
    {'value' : 2, 'name' : "Przepracowane godziny"},
];