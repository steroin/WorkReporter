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
        $("#addEmployeeModalBirthDayInput").val("");
        $("#addEmployeeModalPhoneInput").val("");
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

    $scope.editEmployeeModalOpen = function() {
        startLoading();
        $("#employeeEditModalFirstNameError").hide();
        $("#employeeEditModalLastNameError").hide();
        $("#employeeEditModalEmailError").hide();
        $("#employeeEditModalLoginError").hide();
        $("#employeeEditModalPositionError").hide();
        $("#employeeEditModalWorkingTimeError").hide();
        $("#editEmployeeModalFirstNameInput").val($scope.currentEmployee.personalData.firstName);
        $("#editEmployeeModalLastNameInput").val($scope.currentEmployee.personalData.lastName);
        $("#editEmployeeModalEmailInput").val($scope.currentEmployee.account.email);
        $("#editEmployeeModalLoginInput").val($scope.currentEmployee.account.login);
        $("#editEmployeeModalWorkingTimeInput").val($scope.currentEmployee.workingTime);
        $("#editEmployeeModalBirthDayInput").val($scope.currentEmployee.personalData.birthday == null ? "" : parseDateTimestamp($scope.currentEmployee.personalData.birthday));
        $("#editEmployeeModalPhoneInput").val($scope.currentEmployee.personalData.phone == null ? "" : parseDateTimestamp($scope.currentEmployee.personalData.phone));

        $http.get('solution/positions', {params : {'id' : $scope.currentSolution.id}}).then(function(data) {
            $scope.solutionPositions = data.data;
            return $http.get('solution/teams', {params : {'id' : $scope.currentSolution.id}});
        }).then(function(data) {
            $scope.solutionTeams = data.data;
            return $http.get('empty')
        }).then(function(data) {
            $("#editEmployeeModalPositionInput").val($scope.currentEmployee.position.id);
            $("#editEmployeeModalTeamInput").val($scope.currentEmployee.team == null ? "" : $scope.currentEmployee.team.id);
            $("#editEmployeeModal").modal("show");
            finishLoading();
        });
    };

    $scope.editEmployeeModalSave = function() {
        var firstName = $("#editEmployeeModalFirstNameInput").val();
        if (firstName.length === 0) {
            $("#employeeEditModalFirstNameError").show();
            return;
        } else $("#employeeEditModalFirstNameError").hide();

        var lastName = $("#editEmployeeModalLastNameInput").val();
        if (lastName.length === 0) {
            $("#employeeEditModalLastNameError").show();
            return;
        } else $("#employeeEditModalLastNameError").hide();

        var email = $("#editEmployeeModalEmailInput").val();
        if (email.length === 0) {
            $("#employeeEditModalEmailError").show();
            return;
        } else $("#employeeEditModalEmailError").hide();

        var login = $("#editEmployeeModalLoginInput").val();
        if (login.length === 0) {
            $("#employeeEditModalLoginError").show();
            return;
        } else $("#employeeEditModalLoginError").hide();

        var position = $("#editEmployeeModalPositionInput").val();
        if (position.length === 0) {
            $("#employeeEditModalPositionError").show();
            return;
        } else $("#employeeEditModalPositionError").hide();

        var workingTime = $("#editEmployeeModalWorkingTimeInput").val();
        if (workingTime.length === 0) {
            $("#employeeEditModalWorkingTimeError").show();
            return;
        } else $("#employeeEditModalWorkingTimeError").hide();

        var team = $("#editEmployeeModalTeamInput").val();
        var birthday = $("#editEmployeeModalBirthDayInput").val();
        var phone = $("#editEmployeeModalPhoneInput").val();
        $("#editEmployeeModal").modal("hide");
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

        $http.patch('solution/employees/'+$scope.currentEmployee.id, objectToAdd).then(function(data) {
            $scope.solutionEmployees = $scope.solutionEmployees.map(function(obj) {return obj.id == data.data.id ? data.data : obj});
            $scope.setUpEmployeesPagination($scope.currentPageId);
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

    $scope.deleteEmployeeModalOpen = function() {/*
        if ($scope.currentSolution.administrators.indexOf($scope.currentEmployee.id) > -1) {
            $("#errorEmployeeModal").modal("show");
            $("#employeeErrorModalDeleteSolutionAdmin").show();
        } else {*/
            $("#deleteEmployeeModal").modal("show");
        //}
    };

    $scope.deleteEmployee = function() {
        $("#deleteEmployeeModal").modal("hide");
        startLoading();
        $http.delete('solution/employees/'+$scope.currentEmployee.id, {params: {
            'solutionid' : $scope.currentSolution.id
        }}).then(function(data) {
            $scope.solutionEmployees = $scope.solutionEmployees.filter(function(obj) {
                return obj['id'] != $scope.currentEmployee.id;
            });
            $scope.setUpEmployeesPagination($scope.currentPageId);
            finishLoading();
        });
    };

    $scope.deleteSelectedEmployeesModalOpen = function() {
        if ($scope.markedItems.length > 0) {
            $("#deleteSelectedEmployeesModal").modal("show");
        }
    };

    $scope.deleteSelectedEmployees = function() {
        $("#deleteSelectedEmployeesModal").modal("hide");
        startLoading();
        $http.delete('solution/employees', {params: {
            'solutionid' : $scope.currentSolution.id,
            'employees' : $scope.markedItems
        }}).then(function(data) {
            $scope.solutionEmployees = $scope.solutionEmployees.filter(function(obj) {
                return $scope.markedItems.indexOf(obj['id']) == -1;
            });
            $scope.markedItems = [];
            $scope.setUpEmployeesPagination($scope.currentPageId);
            finishLoading();
        });
    };
}