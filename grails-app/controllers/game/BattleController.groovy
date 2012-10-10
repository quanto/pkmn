package game

import map.View

class BattleController {

    FightFactoryService fightFactoryService

    def exit(){
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        Fight fight = fightFactoryService.getFight(player.fightNr)

        if (fight.battleOver){
            player.view = View.ShowMap
            player.save()
        }

        render text: g.render(template: 'getView')
    }

    def index() {

        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        Fight fight = fightFactoryService.getFight(player.fightNr)

        if (player && fight){
            /*
            // active player en openent player
            if (fight.player1Id == $owner->id)
            {
                $mp = 1;
                $op = 2;
            }
            else
            {
                $mp = 2;
                $op = 1;
            }
            */
            int player1protect = 0
            int player2protect = 0

            render view: 'index', model: [fight:fight]
        }
        else {
            render text:"No fight"
        }
    }

    def logRequest = {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()
        //owner = owner.merge()
        Fight fight = fightFactoryService.getFight(player.fightNr)

        render text: g.render(template: 'log',model: [fight:fight])
    }

    def doMove = {

        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        Fight fight = fightFactoryService.getFight(player.fightNr)

        int ownerPokemonMoveId = Integer.parseInt(params.id)

        OwnerMove ownerMove = OwnerMove.findByIdAndOwnerPokemon(ownerPokemonMoveId,fight.fightPlayer1.ownerPokemon)
        int moveId = ownerMove.move.id

        if (moveId == -1 || moveId == 394) // strangle || geen move
        {
            Moves.setMove(fight,fight.fightPlayer1, ownerMove.move)
        }
        else
        {

//            // Controlleer of de user klopt
//            if (ownerMove.ownerPokemon.owner != owner)
//            {
//                println "4"
//                render text:"Move bestaat niet voor pokemon"
//                return
//            }
//            else
//            {
                // :TODO implement
//                // controlleer pp
//                if (ownerPokemonMove->ppLeft != 0)
//                {
//                    if (isset(_SESSION["takePP"]) && _SESSION["takePP"] == false)
//                    {
//
//                        _SESSION["takePP"] = true;
//                    }
//                    else
//                    {
//                        ownerPokemonMove->ppLeft -= 1;
//                    }
//                    ownerPokemonMove->update();
                Moves.setMove(fight,fight.fightPlayer1,ownerMove.move)
//                }
//                else
//                {
//                    exit();
//                    //header("Location: index.php?action=fight");
//                }

                // After displaying last message we can destroy the fight
//                if (fight.battleOver){
//                    fightFactoryService.endFight(fight)
//                }

                render template: "log", model : [fight:fight]
                //redirect action: "index"
//            }
        }
    }

    def menuRequest = {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        Fight fight = fightFactoryService.getFight(player.fightNr)
        FightPlayer myFightPlayer = fight.myPlayer(player)

        // wil move leren
//			if (fight.{"player" . $mp . "learnMoves"} != "" && isset($_GET["forgetMove"]) && $_GET["forgetMove"] == "yes")
//			{
//				forgetMoveList();
//			}
//			// leer moves
//			else if (fight.{"player" . $mp . "learnMoves"} != "")
//			{
//				chooseLearnMove();
//			}
//			else if (fight.battleOver == 1)
//			{
//				//displayBattle();
//				/*
//				$quests = new quests();
//				$quests->qd1 = fight.player2PokemonId;
//				$quests->playerid = fight.player1Id;
//
//				$quests->checkQuest();
//				*/
//                 exitMenu();
//            }
//            else if (fight.{"player" . $mp . "Hp"} == 0 && fight.{"player" . $op . "Hp"} != 0)
//            {
//                pokemonList(true);
//            }
//            else if (isset($_GET["item"]) && fight.battleOver == 0)
//            {
//                //displayBattle();
//                itemList();
//            }
//            else if (isset($_GET["fight"]) && fight.battleOver == 0)
//            {
//                beforeChosingMove($mp);
//                moveList();
//            }
//            // Switch pokemon list
//            else if (isset($_GET["action"]) && $_GET["action"] == "pkmn" && fight.battleOver == 0)
//            {
//                pokemonList(false);
//            }
//            else
//            {
//                actionList();
//            }
//            exit();

        // moveList
        if (fight.battleOver){
            render text: g.render(template: 'exit')
        }
        else if (params.fight != null && !fight.battleOver)
        {
            Battle.beforeChosingMove(fight, myFightPlayer, player);

            render text: g.render(template: 'moveList', model: [ownerMoveList:myFightPlayer.ownerPokemon.ownerMoves])
        }
        // Switch pokemon list
        else if (params.pkmn != null && !fight.battleOver)
        {
            List<OwnerPokemon> ownerPokemonList = OwnerPokemon.findAllByOwnerAndPartyPositionGreaterThan(player,0)
            FightPlayer fightPlayer = fight.fightPlayer1
            render text: g.render(template: 'pokemonList',model: [mustChoose:false,ownerPokemonList:ownerPokemonList,fightPlayer:fightPlayer])
        }
        else {
            render text: g.render(template: 'actionList')
        }
    }

    def switchPokemon = {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        Fight fight = fightFactoryService.getFight(player.fightNr)

        OwnerPokemon ownerPokemon = OwnerPokemon.findByOwnerAndPartyPositionAndHpGreaterThan(player,params.id,0)
        if (ownerPokemon){
            fight.log = ""
            fight.fightPlayer1 = Stats.setBaseStats(fight,ownerPokemon, PlayerType.user, 1)
            Moves.setMove(fight,fight.fightPlayer1, null, false)
        }


        render template: "log", model : [fight:fight]
    }

    def run = {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        Fight fight = fightFactoryService.getFight(player.fightNr)

        println "run"
        Run.run(fight)

        render template: "log", model : [fight:fight]
    }



}
