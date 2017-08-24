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
        $scope.responseTitle = "";
        $http.get('solution/employees', {params: {'id' : $scope.authentication.principal.solutionId}}).then(function(data) {
            $scope.allReceivers = data.data;
            $scope.currentAvailableReceivers = $scope.allReceivers.filter(function(obj) {return obj.id != $scope.authentication.principal.userId;});
            $("#newMessageModal").modal("show");
            finishLoading();
        });
    };

    $scope.responseModalOpen = function(id) {
        startLoading();
        $scope.receiversToAdd = [];
        $scope.currentReceivers = [];
        var receivedMessage = $scope.receivedMessages.filter(function(obj) {return obj.id == id})[0];
        $scope.responseTitle = "RE: "+receivedMessage.title;
        $http.get('solution/employees', {params: {'id' : $scope.authentication.principal.solutionId}}).then(function(data) {
            $scope.allReceivers = data.data;
            $scope.currentAvailableReceivers = $scope.allReceivers.filter(function(obj) {return obj.id != $scope.authentication.principal.userId && obj.id != receivedMessage.sender.id;});
            $scope.currentReceivers = $scope.allReceivers.filter(function(obj) { return obj.id == receivedMessage.sender.id});
            $("#newMessageModal").modal("show");
            finishLoading();
        });
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

    $scope.sendMessage = function() {
        var title = $("#messageTitle").val();
        var content = $("#messageContent").val();

        if (title.length === 0) {
            $("#newMessageModalInvalidTitleError").show();
            return;
        } else $("#newMessageModalInvalidTitleError").hide();

        if (content.length === 0) {
            $("#newMessageModalInvalidContentError").show();
            return;
        } else $("#newMessageModalInvalidContentError").hide();

        if ($scope.receiversToAdd.length < 1) {
            $("#newMessageModalNoReceiversError").show();
            return;
        } else $("#newMessageModalNoReceiversError").hide();

        startLoading();
        var message = {
            'sender' : {
                'id' : $scope.authentication.principal.userId,
                'firstName' : $scope.authentication.principal.firstName,
                'lastName' : $scope.authentication.principal.lastName,
                'email' : $scope.authentication.principal.email
            },
            'receivers' : $scope.allReceivers.filter(function(obj) { return $scope.receiversToAdd.indexOf(obj.id+"") > -1; }),
            'title' : title,
            'content' : content
        };
        $("#newMessageModal").modal("hide");

        $http.post('messages', message).then(function(data) {
            $scope.sentMessages.push(data.data);
            finishLoading();
        });
    };

    $scope.parseDateTimestamp = parseDateTimestamp;
    $scope.init();
});