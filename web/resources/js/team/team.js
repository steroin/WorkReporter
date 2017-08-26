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
        $http.get('auth').then(function(data){
            $scope.authentication = data.data;
            return $http.get("teams", {params : {'userid' : $scope.authentication.principal.userId}});
        }).then(function(data) {
            $scope.allTeams = data.data;
            $scope.currentTeam = $scope.allTeams[0];
            return $http.get('teams/'+$scope.currentTeam.id+'/employees');
        }).then(function(data) {
            $scope.currentEmployees = data.data;
            $scope.currentEmployee = $scope.currentEmployees[0];
            $scope.currentPeriod = 0;

            return $http.get('teams/'+$scope.currentTeam.id+'/employees/'+$scope.currentEmployee.id, {params : {'period' : 7}});
        }).then(function(data) {
            $scope.currentLogEntries = data.data;
            $(".pageContent").show();
            finishLoading();
        });
    };
    $scope.setCurrentTeam = function(id) {
        startLoading();
        $scope.currentTeam = $scope.allTeams.filter(function(obj) {return obj.id == id})[0];
        $http.get('teams/'+id+'/employees').then(function(data) {
            $scope.currentEmployees = data.data;

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
            $scope.currentLogEntries = data.data;
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
            $scope.currentLogEntries = data.data;
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
    $scope.getStatusName = getStatusName;
    $scope.getStatusClass = getStatusClass;
    $scope.init();
});