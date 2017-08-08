<%@ taglib prefix="c" uri="http://www.springframework.org/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: Sergiusz
  Date: 06.08.2017
  Time: 18:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="resources/css/ext/bootstrap.min.css">
    <link rel="stylesheet" href="resources/css/login.css">

    <!-- jQuery library -->
    <script src="resources/js/ext/jquery.min.js"></script>

    <!-- Latest compiled JavaScript -->
    <script src="resources/js/ext/bootstrap.min.js"></script>
    <meta name="viewport" content="width=device-width, initial-scale=1">
</head>

<body>
<div class="container">
    <div class="row">
        <div class="col-xs-4">
        </div>
        <div class="col-xs-4">
            <div class="panel panel-default login-main-panel">
                <div class="panel-heading">
                    Zaloguj sie
                </div>
                <div class="panel-body overflow-hidden">
                    <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSImlSQwrMdPCV3XPxZZv_m6oVz2O_4if8VuqGCPGb7JGDGs2QcE7gg_q4" class="image-rounded welcome-logo"/>
                    <!--
                    <div class="info-container">
                        <p class="lead info">Witamy w ajjsa djaskjds kadksasl sdas. Zaloguj sie aby ask daskdka sjdkassk a</p>
                    </div>
                    -->
                    <form name="loginForm" action="<c:url value='login' />" method="post" class="login-form">
                        <div class="form-group">
                            <div class="row">
                                <div class="col-xs-2"></div>
                                <div class="col-xs-8">
                                    <input id="login" type="text" class="form-control" placeholder="Login lub email">
                                </div>
                                <div class="col-xs-2"></div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="row">
                                <div class="col-xs-2"></div>
                                <div class="col-xs-8">
                                    <input id="password" type="password" class="form-control" placeholder="Hasło">
                                </div>
                                <div class="col-xs-2"></div>
                            </div>
                        </div>
                        <div class="login-button-container">
                            <button type="submit" class="btn btn-default welcome-login-button">Zaloguj</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div class="col-xs-4">
        </div>
    </div>
</div>
</body>

</html>