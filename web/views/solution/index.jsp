<%--
  Created by IntelliJ IDEA.
  User: Sergiusz
  Date: 11.08.2017
  Time: 17:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="resources/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="resources/bootstrap/css/bootstrap-grid.min.css">
    <link rel="stylesheet" href="resources/bootstrap/css/bootstrap-reboot.min.css">
    <link rel="stylesheet" href="resources/css/global.css">
    <link rel="stylesheet" href="resources/css/solution/solutionMenu.css">
    <script type=" text/javascript" src="resources/jquery/jquery-3.2.1.min.js"></script>
    <script type=" text/javascript" src="resources/bootstrap/js/bootstrap.min.js"></script>
    <script type=" text/javascript" src="resources/angularjs/angular.min.js"></script>
    <script type=" text/javascript" src="resources/js/solution/solution.js"></script>
</head>
<body ng-app="solution" ng-controller="solutionController" ng-cloak>
    <%@ include file="../assets/navbar.html"%>
    <div class="container">
        <%@ include file="../assets/solution/solutionChooser.html"%>
        <div class="row">
            <div id="solutionChooserLoader" class="loaderContainer">
                <div class="wrLoader"></div>
            </div>
        </div>


        <div class="row">
            <%@ include file="../assets/solution/solutionMenu.html"%>
            <%@ include file="../assets/solution/solutionInfo.html"%>
            <%@ include file="../assets/solution/solutionProjects.html"%>
            <%@ include file="../assets/solution/solutionTeams.html"%>
            <%@ include file="../assets/solution/solutionEmployees.html"%>
        </div>
    </div>
</body>
</html>
