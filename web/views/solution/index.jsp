<%--
  Created by IntelliJ IDEA.
  User: Sergiusz
  Date: 11.08.2017
  Time: 18:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="resources/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="resources/bootstrap/css/bootstrap-grid.min.css">
    <link rel="stylesheet" href="resources/bootstrap/css/bootstrap-reboot.min.css">
    <script src="resources/jquery/jquery-3.2.1.min.js"></script>
    <script src="resources/bootstrap/js/bootstrap.min.js"></script>
</head>
<body>
<%@ include file="../html/navbar.html"%>

</body>
</html>


<!DOCTYPE html>
<html>

<head>

    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">

    <!-- jQuery library -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

    <!-- Latest compiled JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="style.css" />
    <script src="script.js"></script>
</head>

<body>
<!--
NAVBAR
-->
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">WorkReporter</a>
        </div>
        <ul class="nav navbar-nav">
            <li><a href="#">Zarzadzanie firma</a></li>
            <li><a href="#">Zarzadzanie zespolem</a></li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            <li><a href="#"><span class="glyphicon glyphicon-envelope"></span></a></li>
            <li><a href="#"><span class="glyphicon glyphicon-user"></span> Sergiusz Pogorzala</a></li>
            <li><a href="#"><span class="glyphicon glyphicon-log-out"></span> Wyloguj</a></li>
        </ul>
    </div>
</nav>

<div class="container">
    <div class="row solutionChooserContainer">
        <div class="col-xs-4"></div>
        <div class="col-xs-4">
            <div class="row">
                <div class="chooseSolutionContainer">
                    <h2>Solution</h2>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-3"></div>
                <div class="col-xs-6">
                    <div class="dropdown chooseSolutionSelect">
                        <button class="btn btn-primary dropdown-toggle chooseSolutionButton" type="button" data-toggle="dropdown">Wybierz firme
                            <span class="caret"></span></button>
                        <ul class="dropdown-menu">
                            <li><a href="#">HTML</a></li>
                            <li><a href="#">CSS</a></li>
                            <li><a href="#">JavaScript</a></li>
                        </ul>
                    </div>
                </div>
                <div class="col-xs-3"></div>
            </div>
        </div>
        <div class="col-xs-4"></div>
    </div>
    <div class="row">
        <div class="loaderConainer">
            <div class="wrLoader"></div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-3">
            <div class="panel panel-default">
                <ul class="list-group solutionMenu">
                    <a href="#"><li class="list-group-item solutionMenuItemActive"><h5 class="solutionMenuItemName">Informacje</h5></li></a>
                    <a href="#"><li class="list-group-item"><span class="solutionMenuItemName">Zespoly</span></li></a>
                    <a href="#"><li class="list-group-item"><span class="solutionMenuItemName">Pracownicy</span></li></a>
                    <a href="#"><li class="list-group-item"><span class="solutionMenuItemName">Statystyki</span></li></a>
                </ul>
            </div>
        </div>
        <div class="col-sm-6">
            <div class="panel panel-default solutionContent">
                <table class="table table-striped">
                    <tr>
                        <td>Nazwa:</td>
                        <td>Solution</td>
                        <td><a href="#"><span class="glyphicon glyphicon-edit"></span></a></td>
                    </tr>
                    <tr>
                        <td>Liczba zespolow:</td>
                        <td>N</td>
                        <td></td>
                    </tr>
                    <tr>
                        <td>Liczba pracownikow:</td>
                        <td>N</td>
                        <td></td>
                    </tr>
                </table>
            </div>
        </div>
        <div class="col-sm-3"></div>
    </div>
    <div class="row">
        <div class="col-sm-3"></div>
        <div class="col-sm-6">
            <div class="panel panel-default solutionContent">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <td>Zespol</td>
                        <td></td>
                    </tr>
                    </thead>
                    <tr>
                        <td>Zespol 1</td>
                        <td>
                            <a href="#"><span class="glyphicon glyphicon-edit"></span></a>
                            <a href="#"><span class="glyphicon glyphicon-remove"></span></a>
                        </td>
                    </tr>
                    <tr>
                        <td>Zespol 2</td>
                        <td>
                            <a href="#"><span class="glyphicon glyphicon-edit"></span></a>
                            <a href="#"><span class="glyphicon glyphicon-remove"></span></a>
                        </td>
                    </tr>
                    <tr>
                        <td>Zespol 3</td>
                        <td>
                            <a href="#"><span class="glyphicon glyphicon-edit"></span></a>
                            <a href="#"><span class="glyphicon glyphicon-remove"></span></a>
                        </td>
                    </tr>
                    <tr>
                        <td>Zespol 4</td>
                        <td>
                            <a href="#"><span class="glyphicon glyphicon-edit"></span></a>
                            <a href="#"><span class="glyphicon glyphicon-remove"></span></a>
                        </td>
                    </tr>
                    <tr>
                        <td>Zespol 5</td>
                        <td>
                            <a href="#"><span class="glyphicon glyphicon-edit"></span></a>
                            <a href="#"><span class="glyphicon glyphicon-remove"></span></a>
                        </td>
                    </tr>
                    <tr>
                        <td>Zespol 6</td>
                        <td>
                            <a href="#"><span class="glyphicon glyphicon-edit"></span></a>
                            <a href="#"><span class="glyphicon glyphicon-remove"></span></a>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
        <div class="col-sm-3"></div>
    </div>
</div>

</body>

</html>


