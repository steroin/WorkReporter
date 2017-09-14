/**
 * Created by Sergiusz on 19.08.2017.
 */

function initSolutionProjectsManagement($scope, $http) {
    $scope.activeSolutionProjectsContent = function() {
        startLoading();
        $http.get('solution/projects', {params : {'id' : $scope.currentSolution.id}}).then(function(data) {
            $scope.solutionProjects = data.data.response;
            $scope.activeContent('solutionProjects', 'solutionMenuProjects');
            $scope.setUpProjectPagination(1);
            $scope.markedItems = [];
            finishLoading();
        });
    };

    $scope.setUpProjectPagination = function(defaultPage) {
        $scope.initPagination($scope.solutionProjects, 10, 5, 'solutionProjectsCrudPagination', defaultPage);
    };

    $scope.addProjectModalOpen = function() {
        $("#projectAddModalNameError").hide();
        $("#projectAddModalDescError").hide();
        $("#addProjectModalNameInput").val("");
        $("#addProjectModalDescInput").val("");
    };

    $scope.addProjectModalSave = function() {
        var name = $("#addProjectModalNameInput").val();
        var desc = $("#addProjectModalDescInput").val();
        if (name.length === 0) {
            $("#projectAddModalNameError").show();
            return;
        } else $("#projectAddModalNameError").hide();

        if (desc.length ===0) {
            $("#projectAddModalDescError").show();
            return;
        } else $("#projectAddModalDescError").hide();

        $("#addProjectModal").modal("hide");
        startLoading();
        var objectToAdd = {
            'solutionid' : $scope.currentSolution.id,
            'name' : name,
            'description' : desc
        };
        $http.post('solution/projects', objectToAdd).then(function(data) {
            $scope.solutionProjects.push(data.data.response);
            $scope.setUpProjectPagination($scope.totalPages);
            finishLoading();
        });
    };

    $scope.editProjectModalOpen = function() {
        $("#projectEditModalNameError").hide();
        $("#projectEditModalDescError").hide();
        $("#editProjectModalNameInput").val($scope.currentProject.name);
        $("#editProjectModalDescInput").val($scope.currentProject.description);
    };

    $scope.editProjectModalSave = function() {
        var name = $("#editProjectModalNameInput").val();
        var desc = $("#editProjectModalDescInput").val();
        if (name.length === 0) {
            $("#projectEditModalNameError").show();
            return;
        } else $("#projectEditModalNameError").hide();

        if (desc.length ===0) {
            $("#projectEditModalDescError").show();
            return;
        } else $("#projectEditModalDescError").hide();

        $("#editProjectModal").modal("hide");
        startLoading();
        var objectToAdd = {
            'solutionid' : $scope.currentSolution.id,
            'name' : name,
            'description' : desc
        };
        $http.patch('solution/projects/'+$scope.currentProject.id, objectToAdd).then(function(data) {
            var response = data.data.response;
            $scope.solutionProjects = $scope.solutionProjects.map(function(obj) {return obj.id == response.id ? response : obj});
            $scope.setUpProjectPagination($scope.currentPageId);
            finishLoading();
        });
    };

    $scope.setCurrentProject = function(id) {
        for (var i = 0; i < $scope.solutionProjects.length; i++) {
            if ($scope.solutionProjects[i].id == id) {
                $scope.currentProject = $scope.solutionProjects[i];
                return;
            }
        }
    };

    $scope.deleteProject = function() {
        $("#deleteProjectModal").modal("hide");
        startLoading();
        $http.delete('solution/projects/'+$scope.currentProject.id, {params: {'solutionid' : $scope.currentSolution.id}}).then(function(data) {
            $scope.solutionProjects = $scope.solutionProjects.filter(function(obj) {
                return obj['id'] != $scope.currentProject.id;
            });
            $scope.setUpProjectPagination($scope.currentPageId);
            finishLoading();
        });
    };

    $scope.deleteSelectedProjectsModalOpen = function() {
        if ($scope.markedItems.length > 0) {
            $("#deleteSelectedProjectsModal").modal("show");
        }
    };

    $scope.deleteSelectedProjects = function() {
        $("#deleteSelectedProjectsModal").modal("hide");
        startLoading();
        $http.delete('solution/projects', {params: {
            'solutionid' : $scope.currentSolution.id,
            'projects' : $scope.markedItems
        }}).then(function(data) {
            $scope.solutionProjects = $scope.solutionProjects.filter(function(obj) {
                return $scope.markedItems.indexOf(obj['id']) == -1;
            });
            $scope.markedItems = [];
            $scope.setUpProjectPagination($scope.currentPageId);
            finishLoading();
        });
    };



    $scope.projectTeamsModalOpen = function() {
        startLoading();
        $http.get('solution/teams', {params: {'projectid' : $scope.currentProject.id}}).then(function(data) {
            $scope.currentProjectTeams = data.data;
            return $http.get('solution/teams', {params : {'id' : $scope.currentSolution.id}});
        }).then(function(data) {
            var currentProjectTeamIds = $scope.currentProjectTeams.map(function(obj) { return parseInt(obj.id); });
            $scope.currentProjectAllTeams = data.data.response;
            $scope.currentProjectAvailableTeams = $scope.currentProjectAllTeams.filter(function(obj) { return currentProjectTeamIds.indexOf(obj.id) == -1 });
            $("#projectTeamsModal").modal("show");
            $scope.teamsToAdd = [];
            $scope.teamsToRemove = [];
            finishLoading();
        });
    };

    $scope.addTeamToProject = function() {
        var id = $("#projectTeamsModalTeamInput").val();
        if (id == null || id.length == 0) return;

        if ($scope.teamsToRemove.indexOf(id) > -1) {
            $scope.teamsToRemove.splice($scope.teamsToRemove.indexOf(id), 1);
        } else {
            $scope.teamsToAdd.push(id);
        }

        var team = $scope.currentProjectAvailableTeams.filter(function(obj){return obj.id == id;})[0];
        $scope.currentProjectTeams.push({'id' : id, 'name' : team.name});
        var index = $scope.currentProjectAvailableTeams.indexOf(team);
        if (index > -1) {
            $scope.currentProjectAvailableTeams.splice(index, 1);
        }
    };

    $scope.removeTeamFromProject = function(id) {
        if ($scope.teamsToAdd.indexOf(id) > -1) {
            $scope.teamsToAdd.splice($scope.teamsToAdd.indexOf(id), 1);
        } else {
            $scope.teamsToRemove.push(id);
        }
        $scope.currentProjectTeams = $scope.currentProjectTeams.filter(function(obj) { return obj.id != id; });
        $scope.currentProjectAvailableTeams.push($scope.currentProjectAllTeams.filter(function(obj) { return obj.id == id; })[0]);
    };

    $scope.projectTeamsModalSave = function() {
        startLoading();
        $("#projectTeamsModal").modal("hide");
        $http.patch('solution/projectsteams/'+$scope.currentProject.id, {
            'teamsToAdd': $scope.teamsToAdd,
            'teamsToRemove': $scope.teamsToRemove
        }).then(function(data) {
            finishLoading();
        });
    };
}