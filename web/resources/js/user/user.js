/**
 * Created by Sergiusz on 23.08.2017.
 */
var module = angular.module('user', []);
module.config(['$httpProvider', function($httpProvider) {
    $httpProvider.defaults.headers.patch = {
        'Content-Type': 'application/json;charset=utf-8'
    }
}]);

module.controller('userController', function($scope, $http) {
    $scope.init = function() {
        startLoading();
        $http.get('users/me').then(function(data) {
            $scope.userData = data.data;
            $("#userData").show();
            $scope.activePersonalData();
            finishLoading();
        });
    };

    $scope.activePersonalData = function() {
        $(".userData").hide();
        $("#userPersonalData").show();
        $(".dataTab").removeClass("active");
        $("#personalDataTab").addClass("active");
        $scope.currentUserData = 0;
    };
    $scope.activeAccountData = function() {
        $(".userData").hide();
        $("#userAccountData").show();
        $(".dataTab").removeClass("active");
        $("#accountDataTab").addClass("active");
        $scope.currentUserData = 1;
    };
    $scope.activeEmploymentData = function() {
        $(".userData").hide();
        $("#userEmploymentData").show();
        $(".dataTab").removeClass("active");
        $("#employmentDataTab").addClass("active");
        $scope.currentUserData = 2;
    };

    $scope.saveUserData = function() {

    };
    $scope.parseDateTimestamp = parseDateTimestamp;
    $scope.init();
});