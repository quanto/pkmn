
<table style="background-image: url('${resource(uri:'')}/images/backgrounds/bg2.png');background-repeat: no-repeat;">
    <tr>
        <td colspan="2">
            <div style="height:69px;"></div>
            <img id="player1image" height="160" ng-src=''>
        </td>
        <td colspan="2" valign="top">
            <div style="height:15px;"></div>
            <img id="player2image" height="160" ng-src=''>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <div id="player1pokemonName">{{model.fight.fightPlayers[0].name}}</div>
        </td>
        <td colspan="2">
            <div id="player2pokemonName">{{model.fight.fightPlayers[1].name}}</div>
        </td>
    </tr>
    <tr>
        <td>

            <div style="width:100px;border:1px solid #444444;height:5px;">
                <div id="player1healthbar" style="width:100px;background-color:white;height:5px;"></div>
            </div>
        </td>
        <td>
            <span id="player1hp"></span> / <span id="player1maxhp" />
        </td>
        <td>

            <div style="width:100px;border:1px solid #444444;height:5px;">
                <div id="player2healthbar" style="width:100px;background-color:white;height:5px;"></div>
            </div>

        </td>
        <td>
            <span id="player2hp"></span> / <span id="player2maxhp" />
        </td>
    </tr>

</table>

<p>
<div id="log" ng-show="message">
    <a href='#' ng-click='combatActions();'>X {{message}}</a>
</div>
<p>

<div id="menu">

    <div ng-show="menuModel.menuType == 'actionList'">
        <p>Actions<br>
        <table>
            <tr>
                <td><a href="#" ng-click="getMenu('&fight');">Fight</a></td>
            </tr>
            <tr>
                <td><a href="#" ng-click="doAction('${createLink(controller:'battle', action:'run')}');">Run</a></td>
            </tr>
            <tr>
                <td><a href="#" ng-click="getMenu('&pkmn');">PKMN</a></td>
            </tr>
            <tr>
                <td><a href="#" ng-click="getMenu('&items');">Item</a></td>
            </tr>
        </table>
    </div>

    <div ng-show="menuModel.menuType == 'movesList'">
        Moves<br>

        <span ng-repeat="fightMove in menuModel.fightMoves">
            <a ng-click="doMoveAction('${createLink(controller:'battle', action:'doMove')}', fightMove.ownerMoveId);" href="#">{{fightMove.moveName}} {{fightMove.ppLeft}}/{{fightMove.pp}}</a>
            <br>
        </span>
        <a href='#' ng-click="getMenu('');">Back</a>
    </div>

    <div ng-show="menuModel.menuType == 'itemList'">

        <p>Item<br>
        <table>

                <tr ng-repeat="ownerItem in menuModel.ownerItems">
                    <td><img ng-src="${resource(uri:'')}/images/items/{{ownerItem.image}}"></td>
                    <td>{{ownerItem.name}}</td>
                    <td>{{ownerItem.quantity}}</td>
                    <td><a href="#" ng-click="doAction('${createLink(controller:'battle', action:'useItem') + '/'}' + ownerItem.ownerItemId);">use</a></td>
                </tr>
        </table>

        <p>

        <a href='#' ng-click="getMenu('');">Back</a>

    </div>

    <div ng-show="menuModel.menuType == 'pokemonList'">

        <table>
            <tr>
                <td></td><td>name</td><td>hp</td><td>lvl</td><td>gender</td>
            </tr>

            <tr ng-repeat="fightPokemon in menuModel.fightPokemons">
                <td><img ng-src='${resource(uri:'')}/images/pkmn/{{fightPokemon.threeValueNumber}}ani.gif'></td>
                <td>{{fightPokemon.name}}</td>
                <td>{{fightPokemon.hp}}/{{fightPokemon.maxHp}}</td>
                <td>{{fightPokemon.level}}</td>
                <td>{{fightPokemon.gender}}</td>

                <td>
                    <a ng-show="fightPokemon.hp > 0" href='#' ng-click="switchPokemonAction('${createLink(controller:'battle', action:'switchPokemon')}',fightPokemon.partyPosition);">switch</a>
                </td>
            </tr>
        </table>

        <div ng-show="!menuModel.mustChoose">
            <a href='#' ng-click="getMenu('');">Back</a>
        </div>

    </div>

    <div ng-show="menuModel.menuType == 'exitMenu'">

        <p>Actions<br>
        <table>
            <tr>
                <td>
                    <a href='#' ng-click="doAction('${createLink(controller: 'battle', action:'exit')}')">Exit</a>
                </td>
            </tr>
        </table>

    </div>
    <div ng-show="menuModel.menuType == 'chooseLearnMove'">

        {{menuModel.fightPokemon.name}} is trying to learn {{menuModel.move.name}}! Do you wish to forget a move to make room for {{menuModel.move.name}}?<p>

        <a href='#' ng-click="getMenu('&replaceMove');">Yes</a><br>
        <a href='#' ng-click="getMenu('&forgetMove');">No</a><br>
    </div>

    <div ng-show="menuModel.menuType == 'replaceMoveList'">

        Choose a move that should be forgotten.

        <table>
            <tr ng-repeat="ownerMove in menuModel.ownerMoves">
                <td>
                    <a href="#" ng-click="getMenu('&replaceMoveId='+ownerMove.id);">{{ownerMove.name}}</a>
                </td>
            </tr>
        </table>

        <a href='#' ng-click="getMenu('');">I changed my mind</a>
    </div>

    <div ng-show="menuModel.menuType == 'chooseSwitchPokemon'">
        <p>
            {{menuModel.playerName}} is about to send out {{menuModel.pokemonName}}. Would you like to switch pokemon?
        </p>
        <a href='#' ng-click="getMenu('&pkmn');">Yes</a><br>
        <a href='#' ng-click="doAction('${createLink(controller:'battle', action:'noSwitch')}');">No</a><br>

    </div>

    <div ng-show="menuModel.menuType == 'waitOnOpponentMove'">
        Waiting on opponent move...
    </div>

    <div ng-show="menuModel.menuType == 'waitOnOpponentSwitch'">
        Waiting on opponent move...
    </div>

</div>