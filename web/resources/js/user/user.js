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
            $scope.userData = data.data.response.user;
            $scope.currentSolution = data.data.response.solution;
            $("#userData").show();
            $scope.activePersonalData();
            finishLoading();
        });
    };

    $scope.activePersonalData = function() {
        if ($scope.currentUserData == 0) return;
        $(".buttonsContainer").show();
        $(".userData").hide();
        $("#userPersonalData").show();
        $(".dataTab").removeClass("active");
        $("#personalDataTab").addClass("active");
        $scope.currentUserData = 0;
        $(".modalError").hide();
    };
    $scope.activeAccountData = function() {
        if ($scope.currentUserData == 1) return;
        $(".buttonsContainer").show();
        $(".userData").hide();
        $("#userAccountData").show();
        $(".dataTab").removeClass("active");
        $("#accountDataTab").addClass("active");
        $scope.currentUserData = 1;
        $(".modalError").hide();
    };
    $scope.activeEmploymentData = function() {
        if ($scope.currentUserData == 2) return;
        $(".buttonsContainer").hide();
        $(".userData").hide();
        $("#userEmploymentData").show();
        $(".dataTab").removeClass("active");
        $("#employmentDataTab").addClass("active");
        $scope.currentUserData = 2;
        $(".modalError").hide();
    };

    $scope.saveUserData = function() {
        if ($scope.currentUserData == 0) {
            var birthday = $("#userDataBirthday").val();
            var phone = $("#userDataPhone").val();

            if (birthday === parseDateTimestamp($scope.userData.personalData.birthday) && phone === $scope.userData.personalData.phone) return;

            startLoading();
            $(".modalError").hide();

            var objectToAdd = {
                'solutionid' : $scope.currentSolution.id,
                'birthday' : birthday,
                'phone' : phone
            };

            $http.patch('users/'+$scope.userData.id, objectToAdd).then(function(data) {
                $scope.userData = data.data.response;
                finishLoading();
            });
        } else if ($scope.currentUserData == 1) {
            var existingPassword = $("#userDataPassword").val();
            var repeatPassword = $("#userDataNewPassword").val();
            var newPassword = $("#userDataRepeatPassword").val();
            if (existingPassword.length == 0 && repeatPassword.length == 0 && newPassword.length == 0) return;

            if (newPassword !== repeatPassword) {
                $("#userAccountDataRepeatPasswordError").show();
                glowInputWrong($("#userDataRepeatPassword"));
                glowInputWrong($("#userDataNewPassword"));
                return;
            }

            startLoading();
            $(".modalError").hide();
            $http.patch('pwd/'+$scope.userData.id, {
                'password' : existingPassword,
                'newPassword' : newPassword,
                'passwordRepeat' : repeatPassword
            }).then(function(data) {
                if (data.data == 0) {
                    $("#userAccountDataSuccess").show();
                    $("#userDataPassword").val("");
                    $("#userDataNewPassword").val("");
                    $("#userDataRepeatPassword").val("");
                } else if (data.data == 1) {
                    $("#userAccountDataRepeatPasswordError").show();
                    glowInputWrong($("#userDataRepeatPassword"));
                    glowInputWrong($("#userDataNewPassword"));
                } else if (data.data == 2) {
                    $("#userAccountDataExistingPasswordError").show();
                    glowInputWrong($("#userDataPassword"));
                } else if (data.data == 3) {
                    glowInputWrong($("#userDataRepeatPassword"));
                    glowInputWrong($("#userDataNewPassword"));
                }  else if (data.data == 4) {
                    $("#userAccountDataUnknownError").show();
                }
                finishLoading();
            });
        }
    };
    $scope.hasRole = function(role) {
        return typeof $scope.authentication.authorities !== 'undefined' && $scope.authentication.authorities.filter(function(obj) { return obj.authority == role;}).length > 0;
    };
    $scope.parseDateTimestamp = parseDateTimestamp;
    $scope.init();
});