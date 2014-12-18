<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name='layout' content='game'/>
    <script type="text/javascript" src="${resource(uri:'')}/js/external/angular.min.js"></script>

    <script type="text/javascript" src="${resource(uri:'')}/js/angularGameController.js"></script>
    <script type="text/javascript" src="${resource(uri:'')}/js/angularDirectives.js"></script>
    <script type="text/javascript" src="${resource(uri:'')}/js/angularMapController.js"></script>
    <script type="text/javascript" src="${resource(uri:'')}/js/angularPartyController.js"></script>
    <script type="text/javascript" src="${resource(uri:'')}/js/angularBattleController.js"></script>
    <script type="text/javascript" src="${resource(uri:'')}/js/angularOnlineController.js"></script>
    <script type="text/javascript" src="${resource(uri:'')}/js/angularMarketController.js"></script>
    <script type="text/javascript" src="${resource(uri:'')}/js/angularPvpSelectController.js"></script>
    <script type="text/javascript" src="${resource(uri:'')}/js/angularChoosePokemonController.js"></script>
    <script type="text/javascript" src="${resource(uri:'')}/js/angularComputerController.js"></script>
    <script type="text/javascript" src="${resource(uri:'')}/js/angularNewsController.js"></script>
    <script type="text/javascript" src="${resource(uri:'')}/js/angularStatsController.js"></script>
    <script type="text/javascript" src="${resource(uri:'')}/js/angularBagController.js"></script>
    <script type="text/javascript" src="${resource(uri:'')}/js/angularChatController.js"></script>
    <script type="text/javascript" src="${resource(uri:'')}/js/main.js"></script>
    <script type="text/javascript" src="${resource(uri:'')}/js/chat.js"></script>
    <script type="text/javascript" src="${resource(uri:'')}/js/uiFunctions.js"></script>
    <script type="text/javascript" src="${resource(uri:'')}/js/jquery_json.js"></script>
    <script type="text/javascript" src="${resource(uri:'')}/js/jquery-ui-1.9.0.custom.min.js"></script>

    <link type="text/css" href="${resource(uri:'')}/css/jqueryui.css" rel="stylesheet">
    <parameter name="showRightContent" value="${true}" />
    <parameter name="showBottomContent" value="${true}" />

</head>

<body>

    <center>
        <h3 id="firebug" style="color:#FF0000;"></h3>

        <div id="theMap" ng-controller="GameController">

            <div ng-show="view == 'ShowMap'" ng-controller="MapController">
                <div style="position:relative;height:{{model.map.rows * 16}}px;width: {{model.map.columns * 16}}px;">
                    <img style="position:absolute;top:0px;left:0px;" ng-src="${resource(uri:'')}{{model.map.foregroundImage}}" />

                    <div id="player" class="player spritely actionObject" correctionTop="-16" correctionLeft="0"></div>

                    <div id="objectContainer">

                    </div>
                    <img style="position:absolute;top:0px;left:0px;" ng-src="${resource(uri:'')}{{model.map.backgroundImage}}" />
                </div>
            </div>

            <div ng-show="view == 'Battle'" ng-controller="BattleController">

                <g:render template="/battle/battle" />

            </div>

            <div ng-show="view == 'ShowMarket'" ng-controller="MarketController" style="text-align:left;padding-left:20px;">

                <g:render template="/market/market" />

            </div>

            <div ng-show="view == 'ShowComputer'" ng-controller="ComputerController">

                <g:render template="/party/computer" />

            </div>

            <div ng-show="view == 'ChoosePokemon'" ng-controller="ChoosePokemonController">

                <g:render template="/choosePokemon/choosePokemon" />

            </div>

            <div ng-show="view == 'ShowPvpSelect'" ng-controller="PvpSelectController">

                <g:render template="/pvpSelect/pvpSelect" />

            </div>

        </div>
        <div id="textBox">
        </div>
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
