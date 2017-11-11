/**
 * Created by Sergiusz on 25.08.2017.
 */
var module = angular.module('team', []);
module.config(['$httpProvider', function($httpProvider) {
    $httpProvider.defaults.headers.patch = {
        'Content-Type': 'application/json;charset=utf-8'
    }
}]);
module.controller('teamManagementController', function($scope, $http) {
    $scope.init = function() {
        startLoading();
        $(".teamChooserContainer").hide();
        $("#noTeamsError").hide();
        $http.get('auth').then(function(data){
            $scope.authentication = data.data.response;
            return $http.get("teams", {params : {'userid' : $scope.authentication.principal.userId}});
        }).then(function(data) {
            if (data.data.response.length === 0) {
                finishLoading();
                $("#noTeamsError").show();
                return null;
            }
            $(".teamChooserContainer").show();
            $scope.allTeams = data.data.response;
            $scope.currentTeam = $scope.allTeams[0];
            return $http.get('teams/'+$scope.currentTeam.id+'/employees');
        }).then(function(data) {
            if (data === null) return null;
            $scope.currentEmployees = data.data.response;
            $scope.currentEmployee = $scope.currentEmployees[0];
            $scope.currentPeriod = 0;

            return $http.get('teams/'+$scope.currentTeam.id+'/employees/'+$scope.currentEmployee.id, {params : {'period' : 7}});
        }).then(function(data) {
            if (data === null) return;
            if (typeof(data) === 'undefined') $scope.currentLogEntries = [];
            else $scope.currentLogEntries = data.data.response;
            $scope.currentLogEntries.sort(function(a,b) { return compareDates(a.logStart, b.logStart)});
            $(".pageContent").show();
            $scope.setUpTeamManagementPagination(1);
            finishLoading();
        });
    };

    $scope.initPagination = function(content, itemsPerPage, maxVisiblePages, pagesContainerId, defaultPageId) {
        $scope.totalPages = Math.ceil(content.length / itemsPerPage);
        if ($scope.totalPages < 2) {
            $("#"+pagesContainerId).hide();
        } else {
            $("#"+pagesContainerId).show();
        }
        if (maxVisiblePages > $scope.totalPages) {
            maxVisiblePages = $scope.totalPages;
        }
        $scope.setPage = function(i) {
            if (i < 1 || i > $scope.totalPages) return;
            if (i == 1) {
                $("#"+pagesContainerId+" #prevPage").addClass("disabled");
            } else {
                $("#"+pagesContainerId+" #prevPage").removeClass("disabled");
            }
            if (i == $scope.totalPages) {
                $("#"+pagesContainerId+" #nextPage").addClass("disabled");
            } else {
                $("#"+pagesContainerId+" #nextPage").removeClass("disabled");
            }
            $scope.currentPageId = i;
            $scope.currentPage = content.slice(($scope.currentPageId - 1) * itemsPerPage, $scope.currentPageId * itemsPerPage);
            var currentPages = [];
            var start = $scope.currentPageId - Math.floor(maxVisiblePages  / 2) + (maxVisiblePages + 1) % 2;
            var end = $scope.currentPageId + Math.floor(maxVisiblePages / 2);
            if (start < 1) {
                end = end - start + 1;
                start = 1;
            }
            if (end > $scope.totalPages) {
                start = start - end + $scope.totalPages;
                end = $scope.totalPages;
            }
            for (var n = start; n <= end; n++) {
                currentPages.push(n);
            }
            $scope.pagination = currentPages;
            $("#"+pagesContainerId+" .active").removeClass("active");
            $("#"+pagesContainerId+" #page"+i).addClass("active");
        };
        $scope.nextPage = function() {
            $scope.setPage($scope.currentPageId + 1);
        };
        $scope.prevPage = function() {
            $scope.setPage($scope.currentPageId - 1);
        };
        if (defaultPageId < 1) defaultPageId = 1;
        else if (defaultPageId > $scope.totalPages) defaultPageId = $scope.totalPages;
        $scope.setPage(defaultPageId);
        $(document).ready(function() {$("#"+pagesContainerId+" #page"+defaultPageId).addClass("active");});
        if (content.length == 0) {
            $scope.currentPage = [];
        }
    };

    $scope.setUpTeamManagementPagination = function(defaultPage) {
        $scope.initPagination($scope.currentLogEntries, 10, 5, 'teamManagementCrudPagination', defaultPage);
    };

    $scope.setCurrentTeam = function(id) {
        startLoading();
        $scope.currentTeam = $scope.allTeams.filter(function(obj) {return obj.id == id})[0];
        $http.get('teams/'+id+'/employees').then(function(data) {
            $scope.currentEmployees = data.data.response;

            if ($scope.currentEmployees.length == 0) {
                $scope.currentEmployee = {};
                $scope.currentLogEntries = [];
                finishLoading();
                return;
            }

            $scope.currentEmployee = $scope.currentEmployees[0];
            var period = $("#periodPicker").val();
            var days = 7;

            if (period == 1) days = 30;
            else if (period == 2) days = 365;
            return $http.get('teams/'+$scope.currentTeam.id+'/employees/'+$scope.currentEmployee.id, {params : {'period' : days}});
        }).then(function(data) {
            if (typeof(data) == 'undefined') $scope.currentLogEntries = [];
            else $scope.currentLogEntries = data.data.response;
            $scope.currentLogEntries.sort(function(a,b) { return compareDates(a.logStart, b.logStart)});
            $scope.setUpTeamManagementPagination(1);
            finishLoading();
        });
    };

    $scope.setCurrentEmployee = function(id) {
        if ($scope.currentEmployees.length == 0) return;
        startLoading();
        var period = $("#periodPicker").val();
        var days = 7;

        if (period == 1) days = 30;
        else if (period == 2) days = 365;

        $scope.currentEmployee = $scope.currentEmployees.filter(function(obj) {return obj.id == id})[0];
        $http.get('teams/'+$scope.currentTeam.id+'/employees/'+id, {params : {'period' : days}}).then(function(data) {
            if (typeof(data) == 'undefined') $scope.currentLogEntries = [];
            else $scope.currentLogEntries = data.data.response;
            $scope.currentLogEntries.sort(function(a,b) { return compareDates(a.logStart, b.logStart)});
            $scope.setUpTeamManagementPagination(1);
            finishLoading();
        });
    };

    $scope.setCurrentPeriod = function(period) {
        if ($scope.currentEmployees.length == 0) return;
        $("#periodPicker").val(period);
        $scope.currentPeriod = period;
        $scope.setCurrentEmployee($scope.currentEmployee.id);
    };

    $scope.getPeriodName = function(val) {
        if (val == 0) {
            return "Ostatnie 7 dni";
        }  else if (val == 1) {
            return "Ostatni miesiąc";
        } else if (val == 2) {
            return "Ostatni rok";
        }
    };

    $scope.changeEntryStatus = function(id, status) {
        var entry = $scope.currentLogEntries.filter(function(obj) {return obj.id == id;})[0];
        if (status == entry.status) return;
        startLoading();
        $http.patch('teams/'+$scope.currentTeam.id+'/employees/'+$scope.currentEmployee.id+'/entries/'+id, status).then(function(data) {
            entry.status = status;
            finishLoading();
        });
    };

    $scope.changeEntryStatusToWaiting = function(id) {
        $scope.changeEntryStatus(id, 1);
    };

    $scope.changeEntryStatusToAccepted = function(id) {
        $scope.changeEntryStatus(id, 2);
    };

    $scope.changeEntryStatusToRejected = function(id) {
        $scope.changeEntryStatus(id, 3);
    };

    $scope.getDateDate = function(date) {
        var split = splitDate(date);
        return split[0]+"-"+split[1]+"-"+split[2];
    };

    $scope.getDateTime = function(date) {
        var split = splitDate(date);
        return split[3]+":"+split[4];
    };
    $scope.hasRole = function(role) {
        return typeof $scope.authentication.authorities !== 'undefined' && $scope.authentication.authorities.filter(function(obj) { return obj.authority == role;}).length > 0;
    };

    $scope.getStatusName = getStatusName;
    $scope.getStatusClass = getStatusClass;
    $scope.init();
});