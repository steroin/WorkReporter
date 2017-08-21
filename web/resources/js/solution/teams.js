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

    $scope.editTeamModalOpen = function() {
        $("#teamEditModalNameError").hide();
        $("#editTeamModalNameInput").val($scope.currentTeam.name);
        $http.get('solution/employees', {params : {'teamid' : $scope.currentTeam.id}}).then(function(data) {
            $scope.currentTeamMembers = data.data;
            return $http.get('solution/teams_empty');
        }).then(function(data) {
            $("#editTeamModalLeaderInput").val($scope.currentTeam.leaderId);
            $("#editTeamModal").modal("show");
            finishLoading();
        });
    };

    $scope.editTeamModalSave = function() {
        var name = $("#editTeamModalNameInput").val();
        if (name.length === 0) {
            $("#teamEditModalNameError").show();
            return;
        } else $("#teamEditModalNameError").hide();
        var leader = $("#editTeamModalLeaderInput").val();

        $("#editTeamModal").modal("hide");
        startLoading();
        $scope.currentTeam.name = name;
        $scope.currentTeam.leaderId = leader;
        var leaderObject = $scope.currentTeamMembers.filter(function(item) {return item.id == leader})[0];
        $scope.currentTeam.leaderName = leader.length == 0 ? "" : leaderObject.firstName+" "+leaderObject.lastName+" ("+leaderObject.login+")";
        $http.patch('solution/teams/'+$scope.currentTeam.id, $scope.currentTeam).then(function(data) {
            finishLoading();
        });
    };

    $scope.deleteTeam = function() {
        $("#deleteTeamModal").modal("hide");
        startLoading();
        $http.delete('solution/teams/'+$scope.currentTeam.id, {params: {
            'solutionid' : $scope.currentSolution.id
        }}).then(function(data) {
            $scope.solutionTeams = $scope.solutionTeams.filter(function(obj) {
                return obj['id'] != $scope.currentTeam.id;
            });
            $scope.setUpTeamPagination($scope.currentPageId);
            finishLoading();
        });
    };

    $scope.deleteSelectedTeamsModalOpen = function() {
        if ($scope.markedItems.length > 0) {
            $("#deleteSelectedTeamsModal").modal("show");
        }
    };

    $scope.deleteSelectedTeams = function() {
        $("#deleteSelectedTeamsModal").modal("hide");
        startLoading();
        $http.delete('solution/teams', {params: {
            'solutionid' : $scope.currentSolution.id,
            'teams' : $scope.markedItems
        }}).then(function(data) {
            $scope.solutionTeams = $scope.solutionTeams.filter(function(obj) {
                return $scope.markedItems.indexOf(obj['id']) == -1;
            });
            $scope.markedItems = [];
            $scope.setUpTeamPagination($scope.currentPageId);
            finishLoading();
        });
    };
}