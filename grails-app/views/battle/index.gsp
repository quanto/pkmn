<%@ page import="game.context.Fight" %>
<%
    Fight fight = fight
%>
<html>
    <head>
        <style>

            body
            {
                font-family:verdana;
                font-size:16px;
            }

            table
            {
                font-size:16px;
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
        <script language="javascript" type="text/javascript" src="${resource(uri:'')}/js/jquery-1.10.2.min.js"></script>
    <script type="text/javascript">

        // Player variables
        var pokemon = new Array()
        <g:each in="${[fight.fightPlayer1,fight.fightPlayer2]}" var="fightPlayer">
            pokemon[${fightPlayer.playerNr}] ={playerNr:${fightPlayer.playerNr},name:"${fightPlayer.fightPokemon.name}",level:${fightPlayer.fightPokemon.level},maxHealth:${fightPlayer.fightPokemon.maxHp},health:${fightPlayer.fightPokemon.hp}};
        </g:each>

        //a:1:10;m:Doing 10dmg again;a:1:10;m:bla fainted;
        var string = "";

        //	var string = "m:Pidhey does a Thunderstrike.;a:1:10;m:Pidgey did 10dmg to mew.;m:Pidhey does a Thunderstrike.;a:1:10;m:Pidgey did 10dmg to mew.;m:Fabian has won the battle.;";
        var combatValues = Array();
        var totalActions = 0;

        var currentAction = 0;
        var myPlayerNr = ${myFightPlayer.playerNr}

        $(document).ready(function() {

            // Get actions
            //getMenu("");
            //getLog();
            //prepareActions();
            //combatActions();
            <g:render template="log" model="[fight:fight,myFightPlayer:myFightPlayer]" />
        });

        function switchMyPlayer(playerNr){
            if (myPlayerNr == 2 && playerNr == 1){
                return 2
            }
            else if (myPlayerNr == 2 && playerNr == 2){
                return 1
            }
            else {
                return playerNr
            }
        }

        function doAction(url)
        {

            $.ajax({
                async: false,
                type: "POST",
                url: url,
                success: function(data) {
                    eval(data);
                },
                error: function() {
                }
            });

            return false;
        }

        function getMenu(params)
        {
            $.ajax({
                async: false,
                type: "POST",
                url: "${createLink(action:'menuRequest')}?menuRequest" + params,
                success: function(data) {
                    $('#menu').html(data);
                },
                error: function() {
                }
            });

            return false;
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
                    break;
                }
                else if (action == "m:")
                {
                    displayMessage(value,true);
                    currentAction += 1;
                    break;
                }
                else if (action == "s:") // set pkmn image
                {
                    var player = value.substring(0,1);
                    var imageLink = value.substring(2,value.length);
                    switchPokemon(switchMyPlayer(player),imageLink);
                    currentAction += 1;
                }
                else if (action == "h:") // set hp
                {
                    var player = value.substring(0,1);
                    var hp = parseInt(value.substring(2,value.length));
                    pokemon[player].health = hp

                    // updateUI(); // Ui update after level

                    currentAction += 1;
                }
                else if (action == "x:") // set max hp
                {
                    var player = value.substring(0,1);
                    var hp = parseInt(value.substring(2,value.length));
                    pokemon[player].maxHealth = hp

                    // updateUI(); // Ui update after level

                    currentAction += 1;
                }
                else if (action == "n:") // set name
                {
                    var player = value.substring(0,1);
                    var pokemonname = value.substring(2,value.length);

                    pokemon[player].name = pokemonname;


                    // updateUI(); // Ui update after level

                    currentAction += 1;
                }
                else if (action == "l:") // set level
                {
                    var player = value.substring(0,1);
                    var level = value.substring(2,value.length);

                    pokemon[player].level = level;

                    setHP();
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
                getMenu("");
                $("#log").html("");
            }
            // Format:
            // a: damage action
            // m: message
            // n: setpokemonname
            // s: switchpokemonimage
        }

        function updateUI()
        {
            for (var playerNr=1;playerNr<=2;playerNr++){
                $("#player" + switchMyPlayer(playerNr) + "pokemonName").html(pokemon[playerNr].name + "<br>lv. " + pokemon[playerNr].level);
                $("#player" + switchMyPlayer(playerNr) + "maxhp").text(pokemon[playerNr].maxHealth)
                $("#player" + switchMyPlayer(playerNr) + "hp").text(pokemon[playerNr].health)
            }
            // set the pokemon
        }

        function prepareActions()
        {

            // Split the actions
            combatValues = string.split(";");
            totalActions = combatValues.length;

            currentAction = 0;


//            if (health[1] > maxHealth[1])
//                health[1] = maxHealth[1];
//            if (health[2] > maxHealth[2])
//                health[2] = maxHealth[2];

            // set hp before fight
            updateUI();
            setHP();
        }

        function switchPokemon(player, imgLink)
        {
            if (player == 1)
            {
                $('#player1image').attr("src","${resource(uri:'')}/images/pkmn/back" + imgLink);
            }
            else {
                $('#player2image').attr("src","${resource(uri:'')}/images/pkmn/front" + imgLink);
            }
        }

        function slideHP(HPvalue,playerNr)
        {

            pokemon[playerNr].health = parseInt(HPvalue);

            var barLength = calcBarLength(pokemon[playerNr].maxHealth, pokemon[playerNr].health);


            $('#player' + switchMyPlayer(playerNr) + 'healthbar').animate({
                width: barLength + 'px'
            },{
                duration: 1000,
                step: function( currentLeft ){
                    $("#player" + switchMyPlayer(playerNr) + "hp").text(calcHpFromBar(pokemon[playerNr].maxHealth,currentLeft))
                    $('#player' + switchMyPlayer(playerNr) + 'healthbar').css("background-color",getColor(currentLeft));

                },
                complete: function() {
                    combatActions();
                }
            });
        }

        function getColor(healthScale)
        {
            var color = "green";
            if (healthScale <= 50)
                color = "yellow";
            if (healthScale <= 15)
                color = "red";
            return color;
        }

        function setHP()
        {
            for (var playerNr=1;playerNr<=2;playerNr++){
                var playerScale = calcBarLength(pokemon[switchMyPlayer(playerNr)].maxHealth, pokemon[switchMyPlayer(playerNr)].health);

                $('#player' + switchMyPlayer(playerNr) + 'healthbar').css("width",playerScale + 'px');
                $('#player' + switchMyPlayer(playerNr) + 'healthbar').css("background-color",getColor(playerScale));
            }
        }

        function calcBarLength(maxvalue, value)
        {
            var l = 100 / parseInt(parseInt(maxvalue)) * parseInt(parseInt(value));
            if (l < 0)
                return 0;
            else
                return l;
        }

        function calcHpFromBar(maxvalue, value)
        {
            var l = maxvalue / 100 * parseInt(parseInt(value));

            if (l < 0)
                return 0;
            else
                return Math.round(l);
        }

        function displayMessage(message)
        {
            $('#log').html("<a href='javascript:combatActions()'>X</a> " + message);
        }

    </script>
    </head>
<body>

    <table style="background-image: url('${resource(uri:'')}/images/backgrounds/bg2.png');background-repeat: no-repeat;">
        <tr>
            <td colspan="2">
                <div style="height:69px;"></div>
                <img id="player1image" height="160" src=''>
            </td>
            <td colspan="2" valign="top">
                <div style="height:15px;"></div>
                <img id="player2image" height="160" src=''>
            </td>
        </tr>
        <tr>
             <td colspan="2">
                 <div id="player1pokemonName"></div>
             </td>
            <td colspan="2">

                 <div id="player2pokemonName"></div>
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
    <div id="log"></div>
    <p>
    <div id="menu">

    </div>

</body>
</html>