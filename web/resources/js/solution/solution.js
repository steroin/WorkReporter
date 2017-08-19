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

    $scope.activeSolutionProjectsContent = function() {
        startLoading();
        $http.get('solution/projects', {params : {'id' : $scope.currentSolution.id}}).then(function(data) {
            $scope.solutionProjects = data.data;
            $scope.activeContent('solutionProjects', 'solutionMenuProjects');
            $scope.setUpProjectPagination(1);
            $scope.markedItems = [];
            finishLoading();
        });
    };

    $scope.setUpProjectPagination = function(defaultPage) {
        $scope.initPagination($scope.solutionProjects, 10, 5, 'solutionProjectsCrudPagination', defaultPage);
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
                $("#prevPage").addClass("disabled");
            } else {
                $("#prevPage").removeClass("disabled");
            }
            if (i == $scope.totalPages) {
                $("#nextPage").addClass("disabled");
            } else {
                $("#nextPage").removeClass("disabled");
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
            $("#page"+i).addClass("active");
        };
        $scope.nextPage = function() {
            $scope.setPage($scope.currentPageId + 1);
        };
        $scope.prevPage = function() {
            $scope.setPage($scope.currentPageId - 1);
        };
        $scope.setPage(defaultPageId);
        $(document).ready(function() {$("#page"+defaultPageId).addClass("active");});
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
            $scope.solutionProjects.push(data.data);
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
        $scope.currentProject.name = name;
        $scope.currentProject.description = desc;
        $http.patch('solution/projects/'+$scope.currentProject.id, $scope.currentProject).then(function(data) {
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
