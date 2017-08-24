/**
 * Created by Sergiusz on 23.08.2017.
 */
var module = angular.module('messages', []);
module.config(['$httpProvider', function($httpProvider) {
    $httpProvider.defaults.headers.patch = {
        'Content-Type': 'application/json;charset=utf-8'
    }
}]);

module.controller('messagesController', function($scope, $http) {
    $scope.init = function() {
        startLoading();
        $http.get('auth').then(function(data){
            $scope.authentication = data.data;
            return $http.get('messages/received', {params : {'userid' : $scope.authentication.principal.userId}});
        }).then(function(data) {
            $scope.receivedMessages = data.data;
            return $http.get('messages/sent', {params : {'userid' : $scope.authentication.principal.userId}});
        }).then(function(data) {
            $scope.sentMessages = data.data;
            finishLoading();
            $("#messagesMainContainer").show();
            $scope.activeReceivedMessagesContent();
        });
    };
    $scope.loadReceivedMessages = function() {
        $http.get('messages/received', {params : {'userid' : $scope.authentication.principal.userId}}).then(function(data) {
            $scope.receivedMessages = data.data;
        });
    };

    $scope.loadSentMessages = function() {
        $http.get('messages/sent', {params : {'userid' : $scope.authentication.principal.userId}}).then(function(data) {
            $scope.sentMessages = data.data;
        });
    };

    $scope.activeSentMessagesContent = function() {
        $("#receivedMessages").hide();
        $("#sentMessages").show();
        $("#receivedMessagesTab").removeClass("active");
        $("#sentMessagesTab").addClass("active");
    };

    $scope.activeReceivedMessagesContent = function() {
        $("#sentMessages").hide();
        $("#receivedMessages").show();
        $("#sentMessagesTab").removeClass("active");
        $("#receivedMessagesTab").addClass("active");
    };


    $scope.parseDateTimestamp = parseDateTimestamp;
    $scope.init();
});