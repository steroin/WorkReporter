/**
 * Created by Sergiusz on 19.08.2017.
 */
function initSolutionPositionsManagement($scope, $http) {
    $scope.activeSolutionPositionsContent = function() {
        startLoading();
        $http.get('solution/positions', {params : {'id' : $scope.currentSolution.id}}).then(function(data) {
            $scope.solutionPositions = data.data;
            $scope.activeContent('solutionPositions', 'solutionMenuPositions');
            $scope.setUpPositionPagination(1);
            $scope.markedItems = [];
            finishLoading();
        });
    };

    $scope.setUpPositionPagination = function(defaultPage) {
        $scope.initPagination($scope.solutionPositions, 10, 5, 'solutionPositionsCrudPagination', defaultPage);
    };

    $scope.addPositionModalOpen = function() {
        $("#positionAddModalNameError").hide();
        $("#addPositionModalNameInput").val("");
    };

    $scope.addPositionModalSave = function() {
        var name = $("#addPositionModalNameInput").val();
        if (name.length === 0) {
            $("#positionAddModalNameError").show();
            return;
        } else $("#positionAddModalNameError").hide();

        $("#addPositionModal").modal("hide");
        startLoading();
        var objectToAdd = {
            'solutionid' : $scope.currentSolution.id,
            'name' : name
        };
        $http.post('solution/positions', objectToAdd).then(function(data) {
            $scope.solutionPositions.push(data.data);
            $scope.setUpPositionPagination($scope.totalPages);
            finishLoading();
        });
    };

    $scope.editPositionModalOpen = function() {
        $("#positionEditModalNameError").hide();
        $("#editPositionModalNameInput").val($scope.currentPosition.name);
    };

    $scope.editPositionModalSave = function() {
        var name = $("#editPositionModalNameInput").val();
        if (name.length === 0) {
            $("#projectEditModalNameError").show();
            return;
        } else $("#projectEditModalNameError").hide();

        $("#editPositionModal").modal("hide");
        startLoading();
        $scope.currentPosition.name = name;
        $http.patch('solution/positions/'+$scope.currentPosition.id, $scope.currentPosition).then(function(data) {
            return $http.get('currentdate');
        }).then(function(data) {
            $scope.currentPosition.lastEditionDate = data.data;
            finishLoading();
        });
    };

    $scope.setCurrentPosition = function(id) {
        for (var i = 0; i < $scope.solutionPositions.length; i++) {
            if ($scope.solutionPositions[i].id == id) {
                $scope.currentPosition = $scope.solutionPositions[i];
                return;
            }
        }
    };

    $scope.deletePosition = function() {
        $("#deletePositionModal").modal("hide");
        startLoading();
        $http.delete('solution/positions/'+$scope.currentPosition.id, {params: {'solutionid' : $scope.currentSolution.id}}).then(function(data) {
            $scope.solutionPositions = $scope.solutionPositions.filter(function(obj) {
                return obj['id'] != $scope.currentPosition.id;
            });
            $scope.setUpPositionPagination($scope.currentPageId);
            finishLoading();
        });
    };

    $scope.deleteSelectedPositionsModalOpen = function() {
        if ($scope.markedItems.length > 0) {
            $("#deleteSelectedPositionsModal").modal("show");
        }
    };

    $scope.deleteSelectedPositions = function() {
        $("#deleteSelectedPositionsModal").modal("hide");
        startLoading();
        $http.delete('solution/positions', {params: {
            'solutionid' : $scope.currentSolution.id,
            'positions' : $scope.markedItems
        }}).then(function(data) {
            $scope.solutionPositions = $scope.solutionPositions.filter(function(obj) {
                return $scope.markedItems.indexOf(obj['id']) == -1;
            });
            $scope.markedItems = [];
            $scope.setUpPositionPagination($scope.currentPageId);
            finishLoading();
        });
    };
}
