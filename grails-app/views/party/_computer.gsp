<%-- Party list --%>
<table class='computerList' ng-show="!selectedPokemon">

    <tr>
        <td style='width:16px;text-align:center;'>#</td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td style='width:150px;'>Name</td>
        <td>HP</td>
        <td style='width:30px;text-align:center;'>Lv.</td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
    </tr>

    <tr ng-repeat="ownerPokemon in model.party">
        <td style='text-align:center;'>{{ownerPokemon.partyPosition}}</td>
        <td>
            <img ng-src='${resource(uri:'')}/images/pkmn/{{ownerPokemon.pokemon.threeValueNumber}}ani.gif'>
        </td>
        <td>
            <span ng-if="ownerPokemon.pokemon.type1">
                <img ng-src='${resource(uri:'')}/images/types/{{ownerPokemon.pokemon.type1}}.png'>
            </span>
        </td>
        <td>
            <span ng-if="ownerPokemon.pokemon.type2">
                <img ng-src='${resource(uri:'')}/images/types/{{ownerPokemon.pokemon.type2}}.png'>
            </span>
        </td>
        <td>
            <span ng-show="ownerPokemon.gender == 'Male'"><img src='${resource(uri:'')}/images/gender/male.png'></span>
            <span ng-show="ownerPokemon.gender == 'Female'"><img src='${resource(uri:'')}/images/gender/female.png'></span>
        </td>
        <td><a href='#' ng-click="selectPokemon(ownerPokemon.pokemon)">{{ownerPokemon.pokemon.name}}</a></td>
        <td>{{ownerPokemon.hp}}/{{ownerPokemon.totalHp}}</td>
        <td style='text-align:center;'>{{ownerPokemon.level}}</td>
        <td style='text-align:center;width:70px;white-space:nowrap;'>
            <g:render template="/party/hpBar" /><br />
            <g:render template="/party/expBar" />
        </td>
        <td>
            <a ng-show="ownerPokemon != model.party[0]" ng-click="moveUp(ownerPokemon.id)" href='#'>up</a>
        </td>
        <td>
            <a ng-show="ownerPokemon != model.party[model.party.length-1]" ng-click="moveDown(ownerPokemon.id)" href='#'>down</a>
        </td>
        <td>
            <a ng-show="model.party.length > 1" ng-click="deposit(ownerPokemon.id)" href='#'>deposit</a>
        </td>
    </tr>
</table>

<%-- Stored list --%>
<table class='computerList' ng-show="!selectedPokemon">

    <tr>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td style='width:150px;'>Name</td>
        <td>HP</td>
        <td style='width:30px;text-align:center;'>Lv.</td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
    </tr>

    <tr ng-repeat="ownerPokemon in model.stored">
        <td>
            <img ng-src='${resource(uri:'')}/images/pkmn/{{ownerPokemon.pokemon.threeValueNumber}}ani.gif'>
        </td>
        <td>
            <span ng-if="ownerPokemon.pokemon.type1">
                <img ng-src='${resource(uri:'')}/images/types/{{ownerPokemon.pokemon.type1}}.png'>
            </span>
        </td>
        <td>
            <span ng-if="ownerPokemon.pokemon.type2">
                <img ng-src='${resource(uri:'')}/images/types/{{ownerPokemon.pokemon.type2}}.png'>
            </span>
        </td>
        <td>
            <span ng-show="ownerPokemon.gender == 'Male'"><img src='${resource(uri:'')}/images/gender/male.png'></span>
            <span ng-show="ownerPokemon.gender == 'Female'"><img src='${resource(uri:'')}/images/gender/female.png'></span>
        </td>
        <td><a href='#' ng-click="selectPokemon(ownerPokemon.pokemon)">{{ownerPokemon.pokemon.name}}</a></td>
        <td>{{ownerPokemon.hp}}/{{ownerPokemon.totalHp}}</td>
        <td style='text-align:center;'>{{ownerPokemon.level}}</td>
        <td style='text-align:center;width:70px;white-space:nowrap;'>
            <g:render template="/party/hpBar" /><br />
            <g:render template="/party/expBar" />
        </td>
        <td>
            <a ng-show="model.party.length < 6" ng-click="withdraw(ownerPokemon.id)" href='#'>withdraw</a>
        </td>
        <%--
        <td><a href='${createLink(action:'release',id:ownerPokemon.id)}' onclick="return confirm('Are you sure you want to release this pokemon?');">release</a></td>
        --%>
    </tr>
</table>

<%-- selected pokemon --%>

<div ng-show="selectedPokemon">
    {{selectedPokemon.name}}<br>

    <table>
        <tr>
            <td>Height:</td>
            <td>{{selectedPokemon.height}}</td>
        </tr>
        <tr>
            <td>Weight:</td>
            <td>{{selectedPokemon.weight}}</td>
        </tr>
    </table>

    <a ng-click="deselectPokemon()" href="#">Back</a>
</div>

<p>
    <a href='#' ng-click="exit()">Exit</a>
</p>
