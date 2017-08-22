/**
 * Created by Sergiusz on 21.08.2017.
 */
var module = angular.module('main', []);
module.config(['$httpProvider', function($httpProvider) {
    $httpProvider.defaults.headers.patch = {
        'Content-Type': 'application/json;charset=utf-8'
    }
}]);

module.controller('mainController', function($scope, $http) {
    $scope.init = function() {
        startLoading();
        var today = new Date();
        $scope.initialYear = 2016;
        $scope.initialMonth = 1;
        $scope.initialDay = 1;

        $(".pageContent").show();
        $http.get('auth').then(function(data){
            $scope.authentication = data.data;
            $scope.setCurrentDate(today.getFullYear(), today.getMonth()+1, today.getDate());
        });
    };
    $scope.reloadCalendar = function() {
        var today = new Date();
        $scope.years = [];
        for (var i = today.getFullYear(); i >= $scope.initialYear ; i--) {
            $scope.years.push(i);
        }

        $scope.months = [];
        var maxMonth = $scope.currentYear >= today.getFullYear() ? today.getMonth()+1 : 12;
        var minMonth = $scope.currentYear <= $scope.initialYear ? $scope.initialMonth : 1;
        for (var i = minMonth; i <= maxMonth ; i++) {
            $scope.months.push(i);
        }

        $scope.days = [];
        var maxDays = $scope.currentYear >= today.getFullYear() && $scope.currentMonth >= today.getMonth()+1 ?
            today.getDate() : new Date($scope.currentYear, $scope.currentMonth, 0).getDate();
        var minDays = $scope.currentYear <= $scope.initialYear && $scope.currentMonth <= $scope.initialMonth ?
            $scope.initialDay : 1;
        for (var i = minDays; i <= maxDays ; i++) {
            $scope.days.push(i);
        }
    };
    $scope.setCurrentDate = function(year, month, day) {
        startLoading();
        disableDateChanges();
        $scope.currentYear = year;
        $scope.currentMonth = month;
        $scope.currentDay = day;
        $scope.validateDate();
        $scope.reloadCalendar();
        $http.get('entries', {params : {
            'userid' : $scope.authentication.principal.userId,
            'year' : $scope.currentYear,
            'month' : $scope.currentMonth,
            'day' : $scope.currentDay
        }}).then(function(data) {
            $scope.currentEntries = data.data;
            enableDateChange();
            finishLoading();
        });
    };
    var disableDateChanges = function() {
        $scope.dataChangeDisabled = true;
    };
    var enableDateChange = function() {
        $scope.dataChangeDisabled = false;
    };

    $scope.validateDate = function() {
        var today = new Date();
        if ($scope.currentYear < $scope.initialYear ||
            ($scope.currentYear == $scope.initialYear && $scope.currentMonth < $scope.initialMonth) ||
            ($scope.currentYear == $scope.initialYear && $scope.currentMonth == $scope.initialMonth && $scope.currentDay < $scope.initialDay)) {
            $scope.currentYear = $scope.initialYear;
            $scope.currentMonth = $scope.initialMonth;
            $scope.currentDay = $scope.initialDay;
        } else if ($scope.currentYear > today.getFullYear() ||
            ($scope.currentYear == today.getFullYear() && $scope.currentMonth > today.getMonth()+1) ||
            ($scope.currentYear == today.getFullYear() && $scope.currentMonth == today.getMonth()+1 && $scope.currentDay > today.getDate())) {
            $scope.currentYear = today.getFullYear();
            $scope.currentMonth = today.getMonth()+1;
            $scope.currentDay = today.getDate();
        } else if ($scope.currentDay > new Date($scope.currentYear, $scope.currentMonth, 0).getDate()) {
            $scope.currentDay = new Date($scope.currentYear, $scope.currentMonth, 0).getDate();
        }
    };

    $scope.prevDay = function() {
        if ($scope.dataChangeDisabled) return;
        var date = new Date($scope.currentYear, $scope.currentMonth-1, $scope.currentDay);
        date.setDate(date.getDate() - 1);
        $scope.setCurrentDate(date.getFullYear(), date.getMonth()+1, date.getDate());
    };
    $scope.nextDay = function() {
        if ($scope.dataChangeDisabled) return;
        var date = new Date($scope.currentYear, $scope.currentMonth-1, $scope.currentDay);
        date.setDate(date.getDate() + 1);
        if (date > new Date()) return;
        $scope.setCurrentDate(date.getFullYear(), date.getMonth()+1, date.getDate());
    };

    $scope.addLogEntryModalOpen = function() {
        startLoading();
        $http.get('entrytypes').then(function(data) {
            $scope.logEntryTypes = data.data;
            return $http.get('entries/projects', {params: {'userid' : $scope.authentication.principal.userId}});
        }).then(function(data) {
            $scope.userAvailableProjects = data.data;
            finishLoading();
            $("#addLogEntryModal").modal("show");
        });
    };

    $scope.addLogEntryModalSave = function() {
        var startHour = $("#addLogEntryStartHour").val();
        if (startHour.length === 0) {
            $(".logEntryAddStartHourError").show();
            return;
        } else $(".logEntryAddStartHourError").hide();

        var loggedHours = $("#addLogEntryLoggedHours").val();
        if (loggedHours.length === 0) {
            $(".logEntryAddStartLoggedHours").show();
            return;
        } else $(".logEntryAddStartLoggedHours").hide();

        var logType = $("#addLogEntryEntryType").val();
        if (logType.length === 0) {
            $(".logEntryAddEntryTypeError").show();
            return;
        } else $(".logEntryAddEntryTypeError").hide();
        var project = $("#addLogEntryProject").val();

        $("#addLogEntryModal").modal("hide");
        startLoading();
        var objectToAdd = {
            'userid' : $scope.authentication.principal.userId,
            'day' : $scope.currentDay+"-"+$scope.currentMonth+"-"+$scope.currentYear,
            'starthour' : startHour,
            'loggedhours' : loggedHours,
            'logtypeid' : logType,
            'projectid' : project
        };
        $http.post('entries', objectToAdd).then(function(data) {
            $scope.currentEntries.push(data.data);
            finishLoading();
        });

    };

    $scope.getStatusName = getStatusName;
    $scope.getStatusClass = getStatusClass;
    $scope.parseDateTimestamp = parseDateTimestamp;
    $scope.stringifyMonth = stringifyMonth;
    $scope.init();
});