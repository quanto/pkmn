package game

class BattleController {

    FightFactoryService fightFactoryService

    def index() {
        boolean debug = true

        Owner owner = session.owner
        //owner = owner.merge()

        Fight fight = fightFactoryService.getFight(owner.fightNr)

        if (owner && fight){
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
    }

    def logRequest = {
        Owner owner = session.owner
        //owner = owner.merge()
        Fight fight = fightFactoryService.getFight(owner.fightNr)

        render text: g.render(template: 'log',model: [fight:fight])
    }

    def doMove = {

        Owner owner = session.owner
        //owner = owner.merge()
        Fight fight = fightFactoryService.getFight(owner.fightNr)
        //fight = fight.refresh()


        int ownerPokemonMoveId = Integer.parseInt(params.id)
        // :TODO make safe

        OwnerMove ownerMove = OwnerMove.get(ownerPokemonMoveId)
        int moveId = ownerMove.move.id

        if (moveId == -1 || moveId == 394) // strangle || geen move
        {
            Moves.setMove(fight,fight.fightPlayer1, moveId)
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
                Moves.setMove(fight,fight.fightPlayer1,moveId);
//                }
//                else
//                {
//                    exit();
//                    //header("Location: index.php?action=fight");
//                }
                redirect action: "index"
//            }
        }
    }

    def menuRequest = {
        Owner owner = session.owner
        //owner = owner.merge()
        Fight fight = fightFactoryService.getFight(owner.fightNr)
        FightPlayer myFightPlayer = fight.myPlayer(session.owner)

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
        if (params.fight != null && !fight.battleOver)
        {
            Battle.beforeChosingMove(fight, myFightPlayer, owner);

            myFightPlayer.ownerPokemon = myFightPlayer.ownerPokemon.refresh()

            render text: g.render(template: 'moveList', model: [ownerMoveList:myFightPlayer.ownerPokemon.ownerMoves])
        }
        // Switch pokemon list
        else if (params.pkmn != null && !fight.battleOver)
        {
            List<OwnerPokemon> ownerPokemonList = OwnerPokemon.findAllByOwnerAndPartyPositionGreaterThan(owner,0)
            FightPlayer fightPlayer = fight.fightPlayer1
            render text: g.render(template: 'pokemonList',model: [mustChoose:false,ownerPokemonList:ownerPokemonList,fightPlayer:fightPlayer])
        }
        else {
            render text: g.render(template: 'actionList')
        }
    }

    def run = {
        Owner owner = session.owner
        //owner = owner.merge()
        Fight fight = fightFactoryService.getFight(owner.fightNr)

        Run.run(fight)

        render text: ""
    }



}
