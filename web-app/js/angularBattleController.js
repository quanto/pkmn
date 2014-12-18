app.controller('BattleController', function ($scope, $http, $timeout) {

    $scope.pokemon = new Array();
    $scope.combatValues = new Array();
    $scope.totalActions = 0;
    $scope.message;
    $scope.currentAction = 0;
    $scope.myPlayerNr;
    $scope.menuModel;

    $scope.$on('initBattle',function(event, data) {
        $scope.init(data);
    });

    $scope.init = function(data){
        $scope.model = data;

        // init pokemon

        $scope.myPlayerNr = data.myFightPlayer.playerNr;

        for (var playerNr=1;playerNr<=2;playerNr++){

            var i = playerNr-1;

            $scope.pokemon[playerNr] = {
                playerNr: data.fight.fightplayers[i].playerNr,
                name: data.fight.fightplayers[i].fightPokemon.name,
                level: data.fight.fightplayers[i].fightPokemon.level,
                maxHealth: data.fight.fightplayers[i].fightPokemon.maxHp,
                health: data.fight.fightplayers[i].fightPokemon.hp
            };

        }

        // init log
        $scope.prepareActions(data.roundResult);

    };

    $scope.switchMyPlayer = function(playerNr){
        if ($scope.myPlayerNr == 2 && playerNr == 1){
            return 2
        }
        else if ($scope.myPlayerNr == 2 && playerNr == 2){
            return 1
        }
        else {
            return playerNr
        }
    }

    $scope.combatActions = function(){

        for (var i= $scope.currentAction;i < $scope.combatValues.length;i++){

            $scope.currentAction = i;

            var action = $scope.combatValues[i].substring(0,2);
            var value = $scope.combatValues[i].substring(2,$scope.combatValues[i].length);

            if (action == "a:"){
                var player = value.substring(0,1);
                var hpValue = value.substring(2,value.length);

                $scope.slideHP(hpValue,player);

                $scope.currentAction += 1;
                break;
            }
            else if (action == "m:"){
                $scope.displayMessage(value,true);
                $scope.currentAction += 1;
                break;
            }
            else if (action == "s:"){ // set pkmn image

                var player = value.substring(0,1);
                var imageLink = value.substring(2,value.length);
                $scope.switchPokemon($scope.switchMyPlayer(player),imageLink);
                $scope.currentAction += 1;
            }
            else if (action == "h:"){ // set hp

                var player = value.substring(0,1);
                var hp = parseInt(value.substring(2,value.length));
                $scope.pokemon[player].health = hp

                // Ui update after level

                $scope.currentAction += 1;
            }
            else if (action == "x:"){ // set max hp

                var player = value.substring(0,1);
                var hp = parseInt(value.substring(2,value.length));
                $scope.pokemon[player].maxHealth = hp

                // Ui update after level

                $scope.currentAction += 1;
            }
            else if (action == "n:"){ // set name

                var player = value.substring(0,1);
                var pokemonname = value.substring(2,value.length);

                $scope.pokemon[player].name = pokemonname;


                // Ui update after level

                $scope.currentAction += 1;
            }
            else if (action == "l:"){ // set level

                var player = value.substring(0,1);
                var level = value.substring(2,value.length);

                $scope.pokemon[player].level = level;

                $scope.setHP();
                $scope.updateUI();

                $scope.currentAction += 1;
            }
            else{
                $scope.currentAction += 1;
                break;
            }

        }

        if ($scope.currentAction > $scope.totalActions - 1)
        {
            // Toon menu
            $scope.getMenu("");
            $scope.message = null;
        }
        // Format:
        // a: damage action
        // m: message
        // n: setpokemonname
        // s: switchpokemonimage
    }

    $scope.switchPokemonAction = function(url, partyPosition){
        $scope.doAction(url + "/" + partyPosition);
    }

    $scope.doMoveAction = function(url, ownerMoveId){
        $scope.doAction(url + "/" + ownerMoveId);
    }

    $scope.doAction = function(url){

        $.ajax({
            async: false,
            type: "POST",
            url: url,
            success: function(data) {

                if (data.roundResult){
                    $scope.prepareActions(data.roundResult);
                }
                if (data.updateView){
                    $scope.updateView();
                }

            },
            error: function() {
            }
        });

        return false;
    }

    $scope.getMenu = function(params){

        $.ajax({
            async: false,
            type: "POST",
            url: serverUrl + "/battle/menuRequest?menuRequest" + params,
            success: function(data) {
                $scope.menuModel = data;

                if ($scope.menuModel.waiting){
                    $timeout(function(){
                        $scope.updateView();
                    },2000);
                }

                if ($scope.menuModel.update){
                    $scope.updateView();
                }
            },
            error: function(){
                alert("error: getMenu");
            }
        });

        return false;
    }

    $scope.switchPokemon = function(player, imgLink)
    {
        if (player == 1)
        {
            $('#player1image').attr("src", serverUrl + "/images/pkmn/back" + imgLink);
        }
        else {
            $('#player2image').attr("src", serverUrl + "/images/pkmn/front" + imgLink);
        }
    }

    $scope.prepareActions = function(string){
        $scope.menuModel = null;

        // Split the actions
        $scope.combatValues = string.split(";");
        $scope.totalActions = $scope.combatValues.length;

        $scope.currentAction = 0;

        // set hp before fight
        $scope.updateUI();
        $scope.setHP();

        // Start the actions
        $scope.combatActions();
    }

    $scope.updateUI = function(){
        for (var playerNr=1;playerNr<=2;playerNr++){
            $("#player" + $scope.switchMyPlayer(playerNr) + "pokemonName").html($scope.pokemon[playerNr].name + "<br>lv. " + $scope.pokemon[playerNr].level);
            $("#player" + $scope.switchMyPlayer(playerNr) + "maxhp").text($scope.pokemon[playerNr].maxHealth)
            $("#player" + $scope.switchMyPlayer(playerNr) + "hp").text($scope.pokemon[playerNr].health)
        }
        // set the pokemon
    }

    $scope.slideHP = function(HPvalue,playerNr){

        $scope.pokemon[playerNr].health = parseInt(HPvalue);

        var barLength = $scope.calcBarLength($scope.pokemon[playerNr].maxHealth, $scope.pokemon[playerNr].health);


        $('#player' + $scope.switchMyPlayer(playerNr) + 'healthbar').animate({
            width: barLength + 'px'
        },{
            duration: 1000,
            step: function( currentLeft ){
                $("#player" + $scope.switchMyPlayer(playerNr) + "hp").text($scope.calcHpFromBar($scope.pokemon[playerNr].maxHealth,currentLeft))
                $('#player' + $scope.switchMyPlayer(playerNr) + 'healthbar').css("background-color", $scope.getColor(currentLeft));

            },
            complete: function() {
                $scope.combatActions();
            }
        });
    }

    $scope.getColor = function(healthScale){
        var color = "green";
        if (healthScale <= 50)
            color = "yellow";
        if (healthScale <= 15)
            color = "red";
        return color;
    }

    $scope.setHP = function(){
        for (var playerNr=1;playerNr<=2;playerNr++){
            var playerScale = $scope.calcBarLength($scope.pokemon[$scope.switchMyPlayer(playerNr)].maxHealth, $scope.pokemon[$scope.switchMyPlayer(playerNr)].health);

            $('#player' + $scope.switchMyPlayer(playerNr) + 'healthbar').css("width",playerScale + 'px');
            $('#player' + $scope.switchMyPlayer(playerNr) + 'healthbar').css("background-color", $scope.getColor(playerScale));
        }
    }

    $scope.calcBarLength = function(maxvalue, value){
        var l = 100 / parseInt(parseInt(maxvalue)) * parseInt(parseInt(value));
        if (l < 0)
            return 0;
        else
            return l;
    }

    $scope.calcHpFromBar = function(maxvalue, value){
        var l = maxvalue / 100 * parseInt(parseInt(value));

        if (l < 0)
            return 0;
        else
            return Math.round(l);
    }

    $scope.displayMessage = function(message){
        $scope.message = message;
    }

});