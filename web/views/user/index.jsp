<%--
  Created by IntelliJ IDEA.
  User: Sergiusz
  Date: 23.08.2017
  Time: 13:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html ng-app="user">
<head>
    <title>Title</title>
    <link rel="stylesheet" href="resources/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="resources/bootstrap/css/bootstrap-grid.min.css">
    <link rel="stylesheet" href="resources/bootstrap/css/bootstrap-reboot.min.css">
    <link rel="stylesheet" href="resources/font-awesome-4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="resources/css/global.css">
    <link rel="stylesheet" href="resources/css/user/user.css">

    <script type=" text/javascript" src="resources/jquery/jquery-3.2.1.min.js"></script>
    <script type=" text/javascript" src="resources/bootstrap/js/bootstrap.min.js"></script>
    <script type=" text/javascript" src="resources/angularjs/angular.min.js"></script>
    <script type=" text/javascript" src="resources/js/common.js"></script>
    <script type=" text/javascript" src="resources/js/user/user.js"></script>
</head>
<body ng-controller="userController" ng-cloak>

<%@ include file="../assets/navbar.html"%>
<div class="container">
    <div class="row">
        <div class="loaderContainer pageLoader">
            <div class="wrLoader"></div>
        </div>
    </div>
    <%@ include file="../assets/user/userData.html"%>
</div>

</body>
</html>
