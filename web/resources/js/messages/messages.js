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
            $scope.receivedMessages.sort(function(a,b) { return compareDates(a.sendDate, b.sendDate)});
            return $http.get('messages/sent', {params : {'userid' : $scope.authentication.principal.userId}});
        }).then(function(data) {
            $scope.sentMessages = data.data;
            $scope.sentMessages.sort(function(a,b) { return compareDates(a.sendDate, b.sendDate)});
            finishLoading();
            $("#messagesMainContainer").show();
            $scope.activeReceivedMessagesContent();
            $scope.setUpMessagesPagination(1);
        });
    };
    $scope.loadReceivedMessages = function() {
        $http.get('messages/received', {params : {'userid' : $scope.authentication.principal.userId}}).then(function(data) {
            $scope.receivedMessages = data.data;
            $scope.receivedMessages.sort(function(a,b) { return compareDates(a.sendDate, b.sendDate)});
            $scope.currentMessages = $scope.receivedMessages;
        });
    };

    $scope.loadSentMessages = function() {
        $http.get('messages/sent', {params : {'userid' : $scope.authentication.principal.userId}}).then(function(data) {
            $scope.sentMessages = data.data;
            $scope.currentMessages =  $scope.sentMessages.sort(function(a,b) { return compareDates(a.sendDate, b.sendDate)});
        });
    };

    $scope.activeSentMessagesContent = function() {
        $scope.currentMessages = $scope.sentMessages;
        $("#receivedMessages").hide();
        $("#sentMessages").show();
        $("#receivedMessagesTab").removeClass("active");
        $("#sentMessagesTab").addClass("active");
        $scope.setUpMessagesPagination(1);
    };

    $scope.activeReceivedMessagesContent = function() {
        $scope.currentMessages = $scope.receivedMessages;
        $("#sentMessages").hide();
        $("#receivedMessages").show();
        $("#sentMessagesTab").removeClass("active");
        $("#receivedMessagesTab").addClass("active");
        $scope.setUpMessagesPagination(1);
    };


    $scope.newMessageModalOpen = function() {
        startLoading();
        $("#messageTitle").val("");
        $("#messageContent").val("");
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
        $("#messageContent").val("");
        $scope.receiversToAdd = [];
        $scope.currentReceivers = [];
        var receivedMessage = $scope.receivedMessages.filter(function(obj) {return obj.message.id == id})[0];
        $scope.responseTitle = "RE: "+receivedMessage.message.title;
        $scope.receiversToAdd.push(receivedMessage.message.sender.id);
        $http.get('solution/employees', {params: {'id' : $scope.authentication.principal.solutionId}}).then(function(data) {
            $scope.allReceivers = data.data;
            $scope.currentAvailableReceivers = $scope.allReceivers.filter(function(obj) {return obj.id != $scope.authentication.principal.userId && obj.id != receivedMessage.message.sender.id;});
            $scope.currentReceivers = $scope.allReceivers.filter(function(obj) { return obj.id == receivedMessage.message.sender.id});
            $("#newMessageModal").modal("show");
            finishLoading();
        });
    };

    $scope.addReceiver = function() {
        var id = $("#newMessageModalReceiversInput").val();
        if (id == null || id.length == 0) return;


        $scope.receiversToAdd.push(parseInt(id));
        var receiver = $scope.currentAvailableReceivers.filter(function(obj){return obj.id == id;})[0];
        $scope.currentReceivers.push(receiver);
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
        var messageWrapper = {
            'message' : {
                'sender' : {
                    'id' : $scope.authentication.principal.userId
                },
                'title' : title,
                'content' : content
            },
            'receivers' : $scope.allReceivers.filter(function(obj) { return $scope.receiversToAdd.indexOf(obj.id) > -1; })
        };
        $("#newMessageModal").modal("hide");

        $http.post('messages', messageWrapper).then(function(data) {
            $scope.sentMessages.push(data.data);
            $scope.sentMessages.sort(function(a,b) { return compareDates(a.sendDate, b.sendDate)});
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

    $scope.setUpMessagesPagination = function(defaultPage) {
        $scope.initPagination($scope.currentMessages, 8, 5, 'messagesCrudPagination', defaultPage);
    };

    $scope.parseDateTimestamp = parseDateTimestamp;
    $scope.init();
});