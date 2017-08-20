/**
 * Created by Sergiusz on 19.08.2017.
 */
function initSolutionEmployeesManagement($scope, $http) {
    $scope.activeSolutionEmployeesContent = function() {
        startLoading();
        $http.get('solution/employees', {params : {'id' : $scope.currentSolution.id}}).then(function(data) {
            $scope.solutionEmployees = data.data;
            $scope.activeContent('solutionEmployees', 'solutionMenuEmployees');
            $scope.setUpEmployeesPagination(1);
            $scope.markedItems = [];
            finishLoading();
        });
    };

    $scope.setUpEmployeesPagination = function(defaultPage) {
        $scope.initPagination($scope.solutionEmployees, 10, 5, 'solutionEmployeesCrudPagination', defaultPage);
    };

    $scope.addEmployeeModalOpen = function() {
        startLoading();
        $("#employeeAddModalFirstNameError").hide();
        $("#employeeAddModalLastNameError").hide();
        $("#employeeAddModalEmailError").hide();
        $("#employeeAddModalLoginError").hide();
        $("#employeeAddModalPositionError").hide();
        $("#employeeAddModalWorkingTimeError").hide();
        $("#addEmployeeModalFirstNameInput").val("");
        $("#addEmployeeModalLastNameInput").val("");
        $("#addEmployeeModalEmailInput").val("");
        $("#addEmployeeModalLoginInput").val("");
        $("#addEmployeeModalTeamInput").val("");
        $("#addEmployeeModalPositionInput").val($("#addEmployeeModalPositionInput option:first").val());
        $("#addEmployeeModalWorkingTimeInput").val("");
        $http.get('solution/positions', {params : {'id' : $scope.currentSolution.id}}).then(function(data) {
            $scope.solutionPositions = data.data;
            return $http.get('solution/teams', {params : {'id' : $scope.currentSolution.id}});
        }).then(function(data) {
            $scope.solutionTeams = data.data;
            $("#addEmployeeModal").modal("show");
            finishLoading();
        });
    };

    $scope.addEmployeeModalSave = function() {
        var firstName = $("#addEmployeeModalFirstNameInput").val();
        if (firstName.length === 0) {
            $("#employeeAddModalFirstNameError").show();
            return;
        } else $("#employeeAddModalFirstNameError").hide();

        var lastName = $("#addEmployeeModalLastNameInput").val();
        if (lastName.length === 0) {
            $("#employeeAddModalLastNameError").show();
            return;
        } else $("#employeeAddModalLastNameError").hide();

        var email = $("#addEmployeeModalEmailInput").val();
        if (email.length === 0) {
            $("#employeeAddModalEmailError").show();
            return;
        } else $("#employeeAddModalEmailError").hide();

        var login = $("#addEmployeeModalLoginInput").val();
        if (login.length === 0) {
            $("#employeeAddModalLoginError").show();
            return;
        } else $("#employeeAddModalLoginError").hide();

        var position = $("#addEmployeeModalPositionInput").val();
        if (position.length === 0) {
            $("#employeeAddModalPositionError").show();
            return;
        } else $("#employeeAddModalPositionError").hide();

        var workingTime = $("#addEmployeeModalWorkingTimeInput").val();
        if (workingTime.length === 0) {
            $("#employeeAddModalWorkingTimeError").show();
            return;
        } else $("#employeeAddModalWorkingTimeError").hide();

        var team = $("#addEmployeeModalTeamInput").val();
        var birthday = $("#addEmployeeModalBirthDayInput").val();
        var phone = $("#addEmployeeModalPhoneInput").val();
        $("#addEmployeeModal").modal("hide");
        startLoading();
        var objectToAdd = {
            'solutionid' : $scope.currentSolution.id,
            'teamid' : team,
            'positionid' : position,
            'workingtime' : workingTime,
            'firstname' : firstName,
            'lastname' : lastName,
            'birthday' : birthday,
            'phone' : phone,
            'login' : login,
            'email' : email
        };

        $http.post('solution/employees', objectToAdd).then(function(data) {
            $scope.solutionEmployees.push(data.data);
            $scope.setUpEmployeesPagination($scope.totalPages);
            finishLoading();
        });
    };

    $scope.setCurrentEmployee = function(id) {
        for (var i = 0; i < $scope.solutionEmployees.length; i++) {
            if ($scope.solutionEmployees[i].id == id) {
                $scope.currentEmployee = $scope.solutionEmployees[i];
                return;
            }
        }
    };

    $scope.deleteEmployeeModalOpen = function() {
        if ($scope.currentSolution.administrators.indexOf($scope.currentEmployee.id) > -1) {
            $("#errorEmployeeModal").modal("show");
            $("#employeeErrorModalDeleteSolutionAdmin").show();
        } else {
            $("#deleteEmployeeModal").modal("show");
        }
    };

    $scope.deleteEmployee = function() {
        $("#deleteEmployeeModal").modal("hide");
        startLoading();
        $http.delete('solution/employees/'+$scope.currentEmployee.id, {params: {
            'solutionid' : $scope.currentSolution.id,
            'personaldataid' : $scope.currentEmployee.personalDataId,
            'accountid' : $scope.currentEmployee.accountId
        }}).then(function(data) {
            $scope.solutionEmployees = $scope.solutionEmployees.filter(function(obj) {
                return obj['id'] != $scope.currentEmployee.id;
            });
            $scope.setUpEmployeesPagination($scope.currentPageId);
            finishLoading();
        });
    };

   /* $scope.deleteSelectedPositionsModalOpen = function() {
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
    };*/
}