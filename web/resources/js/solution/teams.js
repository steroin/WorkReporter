/**
 * Created by Sergiusz on 21.08.2017.
 */
function initSolutionTeamsManagement($scope, $http) {
    $scope.activeSolutionTeamsContent = function () {
        startLoading();
        $http.get('solution/teams', {params: {'id': $scope.currentSolution.id}}).then(function (data) {
            $scope.solutionTeams = data.data;
            $scope.activeContent('solutionTeams', 'solutionMenuTeams');
            $scope.setUpTeamPagination(1);
            $scope.markedItems = [];
            finishLoading();
        });
    };

    $scope.setUpTeamPagination = function(defaultPage) {
        $scope.initPagination($scope.solutionTeams, 10, 5, 'solutionTeamsCrudPagination', defaultPage);
    };

    $scope.setCurrentTeam = function(id) {
        for (var i = 0; i < $scope.solutionTeams.length; i++) {
            if ($scope.solutionTeams[i].id == id) {
                $scope.currentTeam = $scope.solutionTeams[i];
                return;
            }
        }
    };

    $scope.addTeamModalOpen = function() {
        $("#teamAddModalNameError").hide();
        $("#addTeamModalNameInput").val("");
    };

    $scope.addTeamModalSave = function() {
        var name = $("#addTeamModalNameInput").val();
        if (name.length === 0) {
            $("#teamAddModalNameError").show();
            return;
        } else $("#teamAddModalNameError").hide();

        $("#addTeamModal").modal("hide");
        startLoading();
        var objectToAdd = {
            'solutionId' : $scope.currentSolution.id,
            'name' : name,
            'leaderId' : 'null'
        };
        $http.post('solution/teams', objectToAdd).then(function(data) {
            $scope.solutionTeams.push(data.data);
            $scope.setUpTeamPagination($scope.totalPages);
            finishLoading();
        });
    };
}