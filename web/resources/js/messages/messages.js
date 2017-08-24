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


    $scope.newMessageModalOpen = function() {
        startLoading();
        $scope.receiversToAdd = [];
        $scope.currentReceivers = [];
        $http.get('solution/employees', {params: {'id' : $scope.authentication.principal.solutionId}}).then(function(data) {
            $scope.allReceivers = data.data;
            $scope.currentAvailableReceivers = $scope.allReceivers.filter(function(obj) {return obj.id != $scope.authentication.principal.userId;});
            $("#newMessageModal").modal("show");
            finishLoading();
        })
    };
    $scope.addReceiver = function() {
        var id = $("#newMessageModalReceiversInput").val();
        if (id == null || id.length == 0) return;


        $scope.receiversToAdd.push(id);
        var receiver = $scope.currentAvailableReceivers.filter(function(obj){return obj.id == id;})[0];
        $scope.currentReceivers.push({'id' : id, 'firstname' : receiver.firstName, 'lastname' : receiver.lastName, 'email' : receiver.email});
        var index = $scope.currentAvailableReceivers.indexOf(receiver);
        if (index > -1) {
            $scope.currentAvailableReceivers.splice(index, 1);
        }
    };
    $scope.removeReceiver = function(id) {
        $scope.receiversToAdd.splice($scope.receiversToAdd.indexOf(id), 1);
        $scope.currentReceivers = $scope.currentReceivers.filter(function(obj) { return obj.id != id; });
        $scope.currentAvailableReceivers.push($scope.allReceivers.filter(function(obj) { return obj.id == id; })[0]);
    };

    $scope.parseDateTimestamp = parseDateTimestamp;
    $scope.init();
});