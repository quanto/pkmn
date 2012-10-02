<%--
  Created by IntelliJ IDEA.
  User: kevinverhoef
  Date: 29-09-12
  Time: 12:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="game.Fight" contentType="text/html;charset=UTF-8" %>
<%
    Fight fight = fight
%>
<html>
    <head>
        <title>Battle</title>
        <style>

        body
        {
            font-family:verdana;
            font-size:12px;
        }

        table
        {
            font-size:11px;
        }

        table td
        {
            width:100px;
        }

        a:link, a:visited
        {
            color:black;
        }

        </style>
        <script language="javascript" type="text/javascript" src="${resource(uri:'')}/js/mootools-1.2.1-core-yc.js"></script>
        <script language="javascript" type="text/javascript" src="${resource(uri:'')}/js/ajax.js"></script>
    <script type="text/javascript">

        var player1healthbar;
        var player2healthbar;

        // Player variables
        var player1maxHealth = ${fight.fightPlayer1.maxHp};
        var player1health = ${fight.fightPlayer1.hp}; // Health after actions
        var player2maxHealth = ${fight.fightPlayer2.maxHp};
        var player2health = ${fight.fightPlayer2.hp};
        var player1name = "Kevin";
        var player2name = "Fabian";
        var player1pokemonName = "${fight.fightPlayer1.ownerPokemon.pokemon.name}";
        var player2pokemonName = "${fight.fightPlayer2.ownerPokemon.pokemon.name}";
        var player1pokemonLevel = ${fight.fightPlayer1.level};
        var player2pokemonLevel = ${fight.fightPlayer2.level};

        //a:1:10;m:Doing 10dmg again;a:1:10;m:bla fainted;
        var string = "";

        //	var string = "m:Pidhey does a Thunderstrike.;a:1:10;m:Pidgey did 10dmg to mew.;m:Pidhey does a Thunderstrike.;a:1:10;m:Pidgey did 10dmg to mew.;m:Fabian has won the battle.;";
        var combatValues = Array();
        var totalActions = 0;

        var currentAction = 0;

        // Ajax
        var ajax = new Array();

        window.addEvent('domready', function()
        {
            player1healthbar = new Fx.Morph($('player1health'));
            player2healthbar = new Fx.Morph($('player2health'));

            // Get actions
            getMenu("",true);
            //getLog();
            //prepareActions();
            //combatActions();
        });

        function doAction(url)
        {
            var index = ajax.length;
            ajax[index] = new sack();

            ajax[index].requestFile = url;
            ajax[index].onCompletion = function()
            {
                //alert (ajax[index].response);
                getMenu("",true);
            };
            ajax[index].runAJAX();

            return false;
        }

        function getMenu(params, requestLog)
        {

            var index = ajax.length;
            ajax[index] = new sack();

            ajax[index].requestFile = "${createLink(action:'menuRequest')}?menuRequest" + params;
            ajax[index].onCompletion = function()
            {

                $('menu').innerHTML = ajax[index].response;
                if (requestLog)
                {
                    getLog();
                    $("menu").style.display = "none";
                }

            };
            ajax[index].runAJAX();

            return false;
        }

        function getLog()
        {

            var index = ajax.length;
            ajax[index] = new sack();

            ajax[index].requestFile = "${createLink(action:'logRequest')}";
            ajax[index].onCompletion = function()
            {
                //alert (ajax[index].response)
                eval(ajax[index].response);
            };
            ajax[index].runAJAX();
        }

        function combatActions()
        {

            for (var i=currentAction;i<combatValues.length;i++)
            {

                currentAction = i;

                var action = combatValues[i].substring(0,2);
                var value = combatValues[i].substring(2,combatValues[i].length);
                if (action == "a:")
                {
                    var player = value.substring(0,1);
                    var hpValue = value.substring(2,value.length);

                    slideHP(hpValue,player);

                    currentAction += 1;
                }
                else if (action == "m:")
                {
                    displayMessage(value,true);
                    currentAction += 1;
                    break;
                }
                else if (action == "s:")
                {
                    var player = value.substring(0,1);
                    var imageLink = value.substring(2,value.length);
                    switchPokemon(player,imageLink);
                    currentAction += 1;
                }
                else if (action == "n:")
                {
                    var player = value.substring(0,1);
                    var pokemonname = value.substring(2,value.length);

                    if (player == 1)
                        player1pokemonName = pokemonname;
                    else
                        player2pokemonName = pokemonname;

                    updateUI();

                    currentAction += 1;
                }
                else
                {
                    currentAction += 1;
                    break;
                }

            }

            if (currentAction > totalActions - 1)
            {
                // Toon menu
                $("menu").style.display = "block";
                $("log").innerHTML = "";
            }

            // Format:
            // a: damage action
            // m: message
            // n: setpokemonname
            // s: switchpokemonimage
        }

        function updateUI()
        {
            // set the pokemon
            $("player1pokemonName").innerHTML = player1pokemonName + "<br>lv. " + player1pokemonLevel;
            $("player2pokemonName").innerHTML = player2pokemonName + "<br>lv. " + player2pokemonLevel;
        }

        function prepareActions()
        {

            // Split the actions
            combatValues = string.split(";");
            totalActions = combatValues.length;

            currentAction = 0;

            updateUI();

            for (var i=0;i<combatValues.length - 1;i++)
            {

                var action = combatValues[i].substring(0,2);
                var value = combatValues[i].substring(2,combatValues[i].length);
                if (action == "a:")
                {
                    var player = value.substring(0,1);
                    var hpValue = value.substring(2,value.length);

                    eval("player" + player + "health += parseInt(hpValue);");
                }
            }

            if (player2health > player2maxHealth)
                player2health = player2maxHealth;
            if (player1health > player1maxHealth)
                player1health = player1maxHealth;

            // set hp before fight
            setHP();
        }

        function switchPokemon(player, imgLink)
        {
            if (player == 1)
            {
                $('player1image').src = "images/pkmn/back" + imgLink;
            }
        }

        function slideHP(HPvalue,player)
        {
            if (player == 1)
            {
                player1health = parseInt(player1health) - parseInt(HPvalue);

                var barLength = calcBarLength(player1maxHealth, player1health);

                player1healthbar.start({
                    width: barLength + 'px',
                    backgroundColor: getColor(barLength)
                });
            }
            else
            {
                player2health = parseInt(player2health) - parseInt(HPvalue);

                var barLength = calcBarLength(player2maxHealth, player2health);

                player2healthbar.start({
                    width: barLength + 'px',
                    backgroundColor: getColor(barLength)
                });
            }
        }

        function getColor(healthScale)
        {
            var color = "green";
            if (healthScale <= 50)
                color = "yellow";
            return color;
        }

        function setHP()
        {
            var player1scale = calcBarLength(player1maxHealth, player1health);
            var player2scale = calcBarLength(player2maxHealth, player2health);
            $('player1health').style.width = player1scale + 'px';
            $('player1health').style.backgroundColor = getColor(player1scale);
            $('player2health').style.width = player2scale + 'px';
            $('player2health').style.backgroundColor = getColor(player2scale);
        }

        function calcBarLength(maxvalue, value)
        {
            var l = 100 / parseInt(parseInt(maxvalue)) * parseInt(parseInt(value));
            if (l < 0)
                return 0;
            else
                return l;
        }

        function displayMessage(message)
        {
            $('log').innerHTML = "<a href='javascript:combatActions()'>X</a> " + message;
        }

    </script>
    </head>
<body>

    <table>
        <tr>
            <td>
                <img src='${resource(uri:'')}/images/pkmn/back${fight.fightPlayer1.ownerPokemon.pokemon.threeValueNumber()}.gif2'>   ${fight.fightPlayer1.hp}
            </td>
            <td>
                <img src='${resource(uri:'')}/images/pkmn/front${fight.fightPlayer2.ownerPokemon.pokemon.threeValueNumber()}.gif2'>  ${fight.fightPlayer2.hp}
            </td>
        </tr>
        <tr>
            <td>
                <div id="player1pokemonName"></div>
                <div style="width:100px;border:1px solid #444444;height:5px;">
                    <div id="player1health" style="width:100px;background-color:white;height:5px;"></div>
                </div>
            </td>
            <td>
                <div id="player2pokemonName"></div>
                <div style="width:100px;border:1px solid #444444;height:5px;">
                    <div id="player2health" style="width:100px;background-color:white;height:5px;"></div>
                </div>
            </td>
        </tr>

    </table>

    <p>
    <div id="log"></div>
    <p>
    <div id="menu" style="display:none;">

    </div>

</body>
</html>