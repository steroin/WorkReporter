var module = angular.module('solution', []);
module.config(['$httpProvider', function($httpProvider) {
    $httpProvider.defaults.headers.patch = {
        'Content-Type': 'application/json;charset=utf-8'
    }
}]);
module.controller('solutionController', function($scope, $http) {
    $scope.contents = ["solutionInfo", "solutionProjects", "solutionTeams", "solutionEmployees"];

    $scope.init = function() {
        startLoading();
        initSolutionProjectsManagement($scope, $http);
        initSolutionPositionsManagement($scope, $http);
        initSolutionTeamsManagement($scope, $http);
        initSolutionEmployeesManagement($scope, $http);
        $http.get("solution/solutions").then(function(data) {
            $scope.solutionChooserData = data.data;
            return $scope.getSolutionRequest($scope.solutionChooserData.firstSolutionId);
        }).then(function(data) {
            $scope.currentSolution = data.data;
            $scope.activeContent('solutionInfo', 'solutionMenuInfo');
            finishLoading();
        });
    };
    
    $scope.setCurrentSolution = function(id) {
        startLoading();
        $scope.getSolutionRequest(id).then(function(data) {
            $scope.currentSolution = data.data;
            $scope.currentPage = {};
            $scope.activeSolutionInfoContent();
            finishLoading();
        });
    };

    $scope.activeSolutionInfoContent = function() {
        startLoading();
        $scope.activeContent('solutionInfo', 'solutionMenuInfo');
        finishLoading();
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
    };



    $scope.activeSolutionTeamsContent = function() {
        startLoading();
        $scope.activeContent('solutionTeams', 'solutionMenuTeams');
        finishLoading();
    };

    $scope.activeSolutionEmployeesContent = function() {
        startLoading();
        $scope.activeContent('solutionEmployees', 'solutionMenuEmployees');
        finishLoading();
    };

    $scope.getSolutionRequest = function(id) {
        return $http.get('solution/solutions/'+id);
    };

    $scope.activeContent = function(contentId, menuItemId) {
        $(".solutionContent").hide();
        $(".solutionMenuItemActive").removeClass("solutionMenuItemActive");
        $("#"+menuItemId).addClass("solutionMenuItemActive");
        $("#"+contentId).show();
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

    $scope.editSolutionNameModalOpen = function() {
        $("#solutionNameEditModalError").hide();
        $("#solutionNameEditModalInput").val($scope.currentSolution.name);
    };

    $scope.editSolutionNameModalSave = function() {
        var name = $("#solutionNameEditModalInput").val();
        if (name.length === 0) {
            $("#solutionNameEditModalError").show();
        } else {
            $("#editSolutionNameModal").modal("hide");
            startLoading();
            $scope.currentSolution.name = name;
            $http.patch('solution/solutions/'+$scope.currentSolution.id, $scope.currentSolution).then(function(data) {
                finishLoading();
            });
        }
    };
    $scope.parseDateTimestamp = parseDateTimestamp;
    $scope.init();
});



