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
            finishLoading();
            $("#messagesPane").show();
        });
    };
    $scope.loadReceivedMessage = function() {
        $http.get('messages/received', {params : {'userid' : $scope.authentication.principal.userId}}).then(function(data) {
            $scope.receivedMessages = data.data;
        });
    };

    $scope.parseDateTimestamp = parseDateTimestamp;
    $scope.init();
});