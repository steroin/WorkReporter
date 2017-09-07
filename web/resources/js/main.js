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
            $scope.currentEntries.sort(function(a,b) { return compareDates(b.logStart, a.logStart)});
            $scope.markedItems = [];
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
        $("#addLogEntryStartHour").val("");
        $("#addLogEntryLoggedHours").val("");
        $("#logEntryAddStartHourError").hide();
        $("#logEntryAddStartLoggedHours").hide();
        $("#logEntryAddEntryTypeError").hide();
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
            'start' : $scope.currentDay+"-"+$scope.currentMonth+"-"+$scope.currentYear+" "+startHour,
            'loggedhours' : loggedHours,
            'logtypeid' : logType,
            'projectid' : project
        };
        $http.post('entries', objectToAdd).then(function(data) {
            $scope.currentEntries.push(data.data);
            $scope.currentEntries.sort(function(a,b ) { return a.startHour > b.startHour});
            finishLoading();
        });

    };

    $scope.setCurrentLogEntry = function(id) {
        for (var i = 0; i < $scope.currentEntries.length; i++) {
            if ($scope.currentEntries[i].id == id) {
                $scope.currentEntry = $scope.currentEntries[i];
                return;
            }
        }
    };

    $scope.editLogEntryModalOpen = function() {
        if ($scope.currentEntry.status != 1) return;
        startLoading();
        $("#editLogEntryStartHour").val($scope.getDateTime($scope.currentEntry.logStart));
        $("#editLogEntryLoggedHours").val($scope.currentEntry.loggedHours);
        $("#logEntryEditStartHourError").hide();
        $("#logEntryEditStartLoggedHours").hide();
        $("#logEntryEditEntryTypeError").hide();
        $http.get('entrytypes').then(function(data) {
            $scope.logEntryTypes = data.data;
            return $http.get('entries/projects', {params: {'userid' : $scope.authentication.principal.userId}});
        }).then(function(data) {
            $scope.userAvailableProjects = data.data;
            $("#editLogEntryEntryType").val($scope.currentEntry.logType.id);
            return $http.get('empty');
        }).then(function() {
            $("#editLogEntryProject").val($scope.currentEntry.project === null ? "" : $scope.currentEntry.project.id);
            finishLoading();
            $("#editLogEntryModal").modal("show");
        });
    };

    $scope.editLogEntryModalSave = function() {
        if ($scope.currentEntry.status != 1) return;
        var startHour = $("#editLogEntryStartHour").val();
        if (startHour.length === 0) {
            $(".logEntryEditStartHourError").show();
            return;
        } else $(".logEntryEditStartHourError").hide();

        var loggedHours = $("#editLogEntryLoggedHours").val();
        if (loggedHours.length === 0) {
            $(".logEntryEditStartLoggedHours").show();
            return;
        } else $(".logEntryEditStartLoggedHours").hide();

        var logType = $("#editLogEntryEntryType").val();
        if (logType.length === 0) {
            $(".logEntryEditEntryTypeError").show();
            return;
        } else $(".logEntryEditEntryTypeError").hide();
        var project = $("#editLogEntryProject").val();

        $("#editLogEntryModal").modal("hide");
        startLoading();

        // TO DO:
        $scope.currentEntry.startHour = startHour;
        $scope.currentEntry.loggedHours = loggedHours;
        $scope.currentEntry.logTypeId = logType;
        $scope.currentEntry.projectId = project;
        $scope.currentEntry.logTypeName = $scope.logEntryTypes.filter(function(obj) { return obj.id == logType;})[0].name;
        $scope.currentEntries.sort(function(a,b ) { return a.startHour > b.startHour});
        var newName = $scope.userAvailableProjects.filter(function(obj) {return obj.id == project});
        $scope.currentEntry.projectName = newName.length == 0 ? "" : newName[0].name;
        $http.patch('entries/'+$scope.currentEntry.id, $scope.currentEntry).then(function() {
            finishLoading();
        });
    };

    $scope.deleteLogEntryModalOpen = function() {
        if ($scope.currentEntry.status != 1) return;
        $("#deleteLogEntryModal").modal("show");
    };

    $scope.deleteLogEntry = function() {
        if ($scope.currentEntry.status != 1) return;
        startLoading();
        $("#deleteLogEntryModal").modal("hide");
        $http.delete('entries/'+$scope.currentEntry.id).then(function(data) {
            $scope.currentEntries = $scope.currentEntries.filter(function(obj) {return obj.id != $scope.currentEntry.id});
            finishLoading();
        });
    };

    $scope.deleteSelectedLogEntriesModalOpen = function() {
        if ($scope.markedItems.length < 1 || $scope.currentEntries.filter(function(obj) {
            return obj.status != 1 && $scope.markedItems.indexOf(obj.id) > -1;
        }).length > 0) return;

        $("#deleteSelectedLogEntriesModal").modal("show");
    };

    $scope.deleteSelectedLogEntries = function() {
        $("#deleteSelectedLogEntriesModal").modal("hide");
        startLoading();
        $http.delete('entries', {params : {'entries' : $scope.markedItems}}).then(function(data) {
            $scope.currentEntries = $scope.currentEntries.filter(function(obj) {
                return $scope.markedItems.indexOf(obj['id']) == -1;
            });
            finishLoading();
            $scope.markedItems = [];
        });
    };

    $scope.isMarked = function(id) { return $scope.markedItems.indexOf(id) > -1 };

    $scope.markItem = function(id) {
        if (!$scope.markedItems.indexOf(id) > -1) {
            $scope.markedItems.push(id);
        }
    };
    $scope.unmarkItem = function(id) {
        if ($scope.markedItems.indexOf(id) > -1) {
            $scope.markedItems.splice($scope.markedItems.indexOf(id), 1);
        }
    };

    $scope.crudItemCheckboxAction = function(id) {
        if ($scope.isMarked(id)) {
            $scope.unmarkItem(id);
        } else {
            $scope.markItem(id);
        }
    };
    $scope.getDateDate = function(date) {
        var split = splitDate(date);
        return split[0]+"-"+split[1]+"-"+split[2];
    };

    $scope.getDateTime = function(date) {
        var split = splitDate(date);
        return split[3]+":"+split[4];
    };
    $scope.getStatusName = getStatusName;
    $scope.getStatusClass = getStatusClass;
    $scope.parseDateTimestamp = parseDateTimestamp;
    $scope.stringifyMonth = stringifyMonth;
    $scope.init();
});