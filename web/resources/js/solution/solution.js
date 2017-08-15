var module = angular.module('solution', []);

module.controller('solutionController', function($scope, $http) {
    $scope.contents = ["solutionInfo", "solutionProjects", "solutionTeams", "solutionEmployees"];
    $scope.get

    $scope.init = function() {
        startLoading();
        $http.get("solution/solution").then(function(data) {
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
            $scope.activeSolutionInfoContent();
            finishLoading();
        });
    };

    $scope.activeSolutionInfoContent = function() {
        startLoading();
        $scope.activeContent('solutionInfo', 'solutionMenuInfo');
        finishLoading();
    };

    $scope.activeSolutionProjectsContent = function() {
        startLoading();
        $http.get('solution/projects', {params : {'id' : $scope.currentSolution.id}}).then(function(data) {
            $scope.solutionProjects = data.data;
            $scope.activeContent('solutionProjects', 'solutionMenuProjects');
            finishLoading();
        });
    };

    $scope.activeSolutionPositionsContent = function() {
        startLoading();
        $scope.activeContent('solutionPositions', 'solutionMenuPositions');
        finishLoading();
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
        return $http.get('solution/solution', {params : {'id' : id}});
    };

    $scope.activeContent = function(contentId, menuItemId) {
        $(".solutionContent").hide();
        $(".solutionMenuItemActive").removeClass("solutionMenuItemActive");
        $("#"+menuItemId).addClass("solutionMenuItemActive");
        $("#"+contentId).show();
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
            $http.get().then(function(data) {
                finishLoading();
            });
        }
    };
    $scope.init();
});

function startLoading() {
    showLoader();
    disableAllAssets();
}

function finishLoading() {
    hideLoader();
    enableAllAssets();
}

function disableAllAssets() {
    $(".solutionContent").addClass('contentDisabled');
}

function enableAllAssets() {
    $(".solutionContent").removeClass('contentDisabled');
}

function hideLoader() {
    $("#solutionChooserLoader").hide();
}

function showLoader() {
    $("#solutionChooserLoader").show();
}