<!doctype html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title><g:layoutTitle default="Grails"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">
    <link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
    <link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">
    <script type="text/javascript" src="${resource(uri:'')}/js/jquery-1.7.min.js"></script>
    <script type="text/javascript" src="${resource(uri:'')}/js/tablet/move.js"></script>
    <script>

        $(document).ready(function() {
            $('#right').attr('height',($(window).height() - $("#top").height()) + "px");
        });

        $(window).resize(function() {
            $('#right').attr('height',($(window).height() - $("#top").height()) + "px");
        });

    </script>
    <style>

    body {
        margin:0;
        font-size:13px;
        font-family:Arial, Helvetica, sans-serif;
    }

    #top {
        padding-right: 20px;
        text-align:right;
        height:40px;
        background-color: #CCC;
        border-bottom:1px solid #444;
    }

    #layout {
        width:100%;
        height:100%;
    }

    #left {
        text-align: center;

    }

    #right{
        border-left:1px solid #444;
        width:300px;
        background-color: #EEE;
    }

    #bottom {
        hight:100px;
    }

    .chatBox
    {
        width:100%;
        height:150px;
        overflow:auto;
        background-color: white;
        border-bottom:1px solid #444;
        border-top:1px solid #444;
        font-size:12px;
    }

    .chatTime
    {
        font-size:10px;
        font-family:"Times New Roman", Times, serif;
    }

    #chatMessage{
        height: 30px;
        border:0;
        width:100%;
    }

    #right table {
        width:100%;
        border-collapse:collapse;
    }

    #right table td {
        padding:5px 5px 5px 5px;
        border-top:1px solid #ccc;
        border-bottom:1px solid #ccc;
    }

    h1 {
        font-size:16px;
        padding-left:5px;
    }

    .online
    {
        border: #00FF00 solid 1px;
        background-color:#C6FFC6;
    }
    .offline
    {
        border:1px #FF0000 solid;
        background-color: #FF9999;
    }

    img {
        -webkit-user-select: none;
    }

    /* Special tablet navigation */
    #tableNavigation {
        display:none;
    }

    </style>
    <g:layoutHead/>
    <r:layoutResources />

</head>
<body>

<table id="layout" cellspacing="0" rowspacing="0">
    <tr>
        <td colspan="2" id="top">
            <sec:ifLoggedIn>
                Ingelogd als: <sec:loggedInUserInfo field="username"/>
                | <a href="${createLink(controller: 'logout')}">Uitloggen</a>
                | <a href="${createLink(controller: 'game')}">Game</a>
            </sec:ifLoggedIn>
            <sec:ifAnyGranted roles="ROLE_ADMIN">
                | <a href="${createLink(controller: 'mapEditor')}">Map editor</a>

                | <a href="${createLink(controller: 'admin')}">Admin panel</a>
                | <a href="${createLink(controller: 'battleTest')}">Battle test</a>
            </sec:ifAnyGranted>
        </td>
    </tr>
    <tr>

        <td valign="top" id="left">
            <center>
                <g:layoutBody/>
            </center>
        </td>
        <td valign="top" id="right" rowspan="2">
            <h1>Map</h1>
            <div class="userData">
                <div id="mapName"></div>
                <div id="location"></div>
            </div>
            <h1>Online</h1>
            <div id="online">

            </div>
            <h1>Party</h1>
            <div id="party">

            </div>

        </td>

    </tr>
    <tr>
        <td id="bottom" valign="bottom">
            <div class="chatBox" id="chatBox">
            </div>
            <div>
                <input type="text" id="chatMessage" />
            </div>
        </td>
    </tr>

</table>

<div id="tableNavigation" style="position:absolute;bottom:15px;right:15px;">
    <table style="width:0;" cellpadding="0" cellspacing="0">
        <tr>
            <td></td>
            <td><img id="moveUp" src="${resource(uri:'')}/images/move/button.png" style="position: relative;top:30px;" onclick="" /></td>
            <td></td>
        </tr>
        <tr>
            <td><img id="moveLeft" src="${resource(uri:'')}/images/move/button.png" style="position: relative;left:30px;" /></td>
            <td></td>
            <td><img id="moveRight" src="${resource(uri:'')}/images/move/button.png" style="position: relative;right:30px;" /></td>
        </tr>
        <tr>
            <td></td>
            <td><img id="moveDown" src="${resource(uri:'')}/images/move/button.png" style="position: relative;bottom:30px;" /></td>
            <td></td>
        </tr>
    </table>
</div>

<g:javascript library="application"/>
<r:layoutResources />
</body>
</html>