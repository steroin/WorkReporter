/**
 * Created by Sergiusz on 21.08.2017.
 */
function initSolutionTeamsManagement($scope, $http) {
    $scope.activeSolutionTeamsContent = function () {
        startLoading();
        $http.get('solution/teams', {params: {'id': $scope.currentSolution.id}}).then(function (data) {
            $scope.solutionTeams = data.data.response;
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
            $scope.solutionTeams.push(data.data.response);
            $scope.setUpTeamPagination($scope.totalPages);
            finishLoading();
        });
    };

    $scope.editTeamModalOpen = function() {
        $("#teamEditModalNameError").hide();
        $("#editTeamModalNameInput").val($scope.currentTeam.name);
        $http.get('solution/employees', {params : {'teamid' : $scope.currentTeam.id}}).then(function(data) {
            $scope.currentTeamMembers = data.data.response;
            return $http.get('empty');
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

        var objectToAdd = {
            'solutionId' : $scope.currentSolution.id,
            'name' : name,
            'leaderId' : leader
        };
        $http.patch('solution/teams/'+$scope.currentTeam.id, objectToAdd).then(function(data) {
            var response = data.data.response;
            $scope.solutionTeams = $scope.solutionTeams.map(function(obj) {return obj.id == response.id ? response : obj});
            $scope.setUpTeamPagination($scope.currentPageId);
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

    $scope.teamProjectsModalOpen = function() {
        startLoading();
        $http.get('solution/projects', {params: {'teamid' : $scope.currentTeam.id}}).then(function(data) {
            $scope.currentTeamProjects = data.data.response === undefined ? [] : data.data.response;
            return $http.get('solution/projects', {params : {'id' : $scope.currentSolution.id}});
        }).then(function(data) {
            var currentTeamProjectIds = $scope.currentTeamProjects.map(function(obj) { return parseInt(obj.id); });
            $scope.currentTeamAllProjects = data.data.response;
            $scope.currentTeamAvailableProjects = $scope.currentTeamAllProjects.filter(function(obj) { return currentTeamProjectIds.indexOf(obj.id) == -1 });
            $("#teamProjectsModal").modal("show");
            $scope.projectsToAdd = [];
            $scope.projectsToRemove = [];
            finishLoading();
        });
    };

    $scope.addProjectToTeam = function() {
        var id = $("#teamProjectsModalProjectInput").val();
        if (id == null || id.length == 0) return;

        if ($scope.projectsToRemove.indexOf(id) > -1) {
            $scope.projectsToRemove.splice($scope.projectsToRemove.indexOf(id), 1);
        } else {
            $scope.projectsToAdd.push(id);
        }

        var project = $scope.currentTeamAvailableProjects.filter(function(obj){return obj.id == id;})[0];
        $scope.currentTeamProjects.push({'id' : id, 'name' : project.name});
        var index = $scope.currentTeamAvailableProjects.indexOf(project);
        if (index > -1) {
            $scope.currentTeamAvailableProjects.splice(index, 1);
        }
    };

    $scope.removeProjectFromTeam = function(id) {
        if ($scope.projectsToAdd.indexOf(id) > -1) {
            $scope.projectsToAdd.splice($scope.projectsToAdd.indexOf(id), 1);
        } else {
            $scope.projectsToRemove.push(id);
        }
        $scope.currentTeamProjects = $scope.currentTeamProjects.filter(function(obj) { return obj.id != id; });
        $scope.currentTeamAvailableProjects.push($scope.currentTeamAllProjects.filter(function(obj) { return obj.id == id; })[0]);
    };

    $scope.teamProjectsModalSave = function() {
        startLoading();
        $("#teamProjectsModal").modal("hide");
        $http.patch('solution/teamsprojects/'+$scope.currentTeam.id, {
            'projectsToAdd': $scope.projectsToAdd,
            'projectsToRemove': $scope.projectsToRemove
        }).then(function(data) {
            finishLoading();
        });
    };
}