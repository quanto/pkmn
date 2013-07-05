<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name='layout' content='game'/>
    <script type="text/javascript" src="${resource(uri:'')}/js/main.js"></script>
    <script type="text/javascript" src="${resource(uri:'')}/js/chat.js"></script>
    <script type="text/javascript" src="${resource(uri:'')}/js/uiFunctions.js"></script>
    <script type="text/javascript" src="${resource(uri:'')}/js/choosePokemon.js"></script>
    <script type="text/javascript" src="${resource(uri:'')}/js/jquery_json.js"></script>
    <script type="text/javascript" src="${resource(uri:'')}/js/jquery-ui-1.9.0.custom.min.js"></script>

    <link type="text/css" href="${resource(uri:'')}/css/jqueryui.css" rel="stylesheet">
    <parameter name="showRightContent" value="${true}" />
    <parameter name="showBottomContent" value="${true}" />

</head>

<body>
    <center>
        <h3 id="firebug" style="color:#FF0000;"></h3>

        <div id="theMap"></div>

        <div id="textBox"></div>
    </center>


    <div id="tableNavigation" style="position:absolute;bottom:-5px;right:0px;">
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
        
</body>
</html>
