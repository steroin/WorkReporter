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
<body ng-app="solution">
<%@ include file="../assets/navbar.html"%>
<div class="container">
    <div ng-controller="solutionChooser" ng-cloak class="ng-cloak row solutionChooserContainer">
        <div class="col-xs-4"></div>
        <div class="col-xs-4">
            <div class="row">
                <div class="chooseSolutionContainer">
                    <h2>{{solutionChooserData.managedSolutions[solutionChooserData.currentSolution]}}</h2>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-3"></div>
                <div class="col-xs-6">
                    <div class="dropdown chooseSolutionSelect">
                        <button class="btn btn-primary dropdown-toggle chooseSolutionButton" type="button" data-toggle="dropdown">Wybierz firme
                            <span class="caret"></span></button>
                        <ul class="dropdown-menu">
                            <li ng-repeat="(id, name) in solutionChooserData.managedSolutions">
                                <a id="solution{{id}}" href="#">{{name}}</a>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="col-xs-3"></div>
            </div>
        </div>
        <div class="col-xs-4"></div>
    </div>
</div>
</body>
</html>
