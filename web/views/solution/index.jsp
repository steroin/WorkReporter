<%--
  Created by IntelliJ IDEA.
  User: Sergiusz
  Date: 11.08.2017
  Time: 17:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html ng-app="solution">
<head>
    <title>Title</title>
    <link rel="stylesheet" href="resources/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="resources/bootstrap/css/bootstrap-grid.min.css">
    <link rel="stylesheet" href="resources/bootstrap/css/bootstrap-reboot.min.css">
    <link rel="stylesheet" href="resources/font-awesome-4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="resources/css/global.css">
    <link rel="stylesheet" href="resources/css/solution/solutionMenu.css">
    <script type=" text/javascript" src="resources/jquery/jquery-3.2.1.min.js"></script>
    <script type=" text/javascript" src="resources/bootstrap/js/bootstrap.min.js"></script>
    <script type=" text/javascript" src="resources/angularjs/angular.min.js"></script>
    <script type=" text/javascript" src="resources/js/common.js"></script>
    <script type=" text/javascript" src="resources/js/solution/projects.js"></script>
    <script type=" text/javascript" src="resources/js/solution/positions.js"></script>
    <script type=" text/javascript" src="resources/js/solution/teams.js"></script>
    <script type=" text/javascript" src="resources/js/solution/employees.js"></script>
    <script type=" text/javascript" src="resources/js/solution/reports.js"></script>
    <script type=" text/javascript" src="resources/js/solution/main.js"></script>
    <meta charset="UTF-8"/>
</head>
<body ng-controller="solutionController" ng-cloak>
    <%@ include file="../assets/navbar.html"%>
    <div class="container">
        <div class="row">
            <div class="col-xs-4"></div>
            <div class="col-xs-4">
                <div class="row">
                    <div class="chooseSolutionContainer">
                        <h2>{{currentSolution.name}}</h2>
                    </div>
                </div>
            </div>
            <div class="col-xs-4"></div>
        </div>
        <div class="row solutionPageLoader">
            <div class="loaderContainer pageLoader">
                <div class="wrLoader"></div>
            </div>
        </div>

        <div class="row">
            <%@ include file="../assets/solution/solutionMenu.html"%>
            <%@ include file="../assets/solution/solutionInfo.html"%>
            <%@ include file="../assets/solution/editSolutionNameModal.html"%>
            <%@ include file="../assets/solution/solutionProjects.html"%>
            <%@ include file="../assets/solution/solutionPositions.html"%>
            <%@ include file="../assets/solution/solutionTeams.html"%>
            <%@ include file="../assets/solution/solutionEmployees.html"%>
            <%@ include file="../assets/solution/project/deleteProjectModal.html"%>
            <%@ include file="../assets/solution/project/deleteSelectedProjectsModal.html"%>
            <%@ include file="../assets/solution/project/editProjectModal.html"%>
            <%@ include file="../assets/solution/project/addProjectModal.html"%>
            <%@ include file="../assets/solution/position/deletePositionModal.html"%>
            <%@ include file="../assets/solution/position/deleteSelectedPositionsModal.html"%>
            <%@ include file="../assets/solution/position/editPositionModal.html"%>
            <%@ include file="../assets/solution/position/addPositionModal.html"%>
            <%@ include file="../assets/solution/employee/addEmployeeModal.html"%>
            <%@ include file="../assets/solution/employee/deleteEmployeeModal.html"%>
            <%@ include file="../assets/solution/employee/editEmployeeModal.html"%>
            <%@ include file="../assets/solution/employee/errorEmployeeModal.html"%>
            <%@ include file="../assets/solution/employee/deleteSelectedEmployeesModal.html"%>
            <%@ include file="../assets/solution/team/addTeamModal.html"%>
            <%@ include file="../assets/solution/team/editTeamModal.html"%>
            <%@ include file="../assets/solution/team/deleteTeamModal.html"%>
            <%@ include file="../assets/solution/team/deleteSelectedTeamsModal.html"%>
            <%@ include file="../assets/solution/team/teamProjectsModal.html"%>
            <%@ include file="../assets/solution/project/projectTeamsModal.html"%>
            <%@ include file="../assets/solution/solutionReports.html"%>
            <%@ include file="../assets/solution/reports/showReportModal.html"%>
        </div>
    </div>
</body>
</html>
