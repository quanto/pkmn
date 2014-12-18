package game

import game.fight.NPCHelper
import game.fight.action.NoAction
import game.fight.log.MessageLog
import game.context.FightPlayer
import game.context.PlayerType
import game.context.BattleType
import game.context.Fight
import game.fight.Battle
import game.fight.FightMove
import game.fight.WildMove
import game.fight.action.BattleAction
import game.fight.action.SwitchAction
import game.fight.action.MoveAction
import game.fight.action.FailAction
import game.context.FightPokemon

class Moves {
	
	public static void setPlayerMove(Fight fight, FightPlayer myFightPlayer, int ownerPokemonMoveId) {
		
		FightMove fightMove = myFightPlayer.fightPokemon.fightMoves.find {  FightMove fightMove ->  fightMove.ownerMove.id == ownerPokemonMoveId }
		
		assert fightMove
		Move move = fightMove.move

		if (move == null || move.name == "Struggle") { // Struggle || geen move
		
			// Dit is geen eigen move, pp hoeft er niet af
			Moves.setMove(fight, myFightPlayer, new MoveAction(move: move,ownerMoveForPP: null))
		}
		else
		{
			if (fightMove.ppLeft <= 0){
				fight.roundResult.personalActions.add(new MessageLog("No pp left for move ${move.name}."))
			}
			else {
				Moves.setMove(fight,myFightPlayer, new MoveAction(move:move,ownerMoveForPP:fightMove))
			}
		}
	}

    /**
     * Set a move
     */
    public static void setMove(Fight fight, FightPlayer fightPlayer, BattleAction battleAction)
    {
        assert battleAction
        if (battleAction){
            fightPlayer.battleAction = battleAction
        }

        // Mirror Move
        if (battleAction in MoveAction && battleAction.move.name == "Mirror Move"){
            if (fightPlayer.opponentFightPlayer().lastBattleAction in MoveAction && fightPlayer.opponentFightPlayer().lastBattleAction.move.name != "Mirror Move"){
                fightPlayer.battleAction = new MoveAction( move: fightPlayer.opponentFightPlayer().lastBattleAction.move,ownerMoveForPP: battleAction.ownerMoveForPP)
            }
            else {
                fightPlayer.battleAction = new FailAction()
            }
        }

        // Kijk of tegen de computer wordt gespeelt, dan wordt er een move gekozen
        if (fightPlayer.playerType == PlayerType.user && (fight.battleType == BattleType.PVE || fight.battleType == BattleType.PVN))
        {
            if (!(fightPlayer.battleAction in NoAction) || fightPlayer.playerType == PlayerType.user){

                if (fight.switchRound){
					// Tegenstander hoeft niet te wisselen
					if (fightPlayer.opponentFightPlayer().fightPokemon.hp > 0){
						fightPlayer.opponentFightPlayer().battleAction = new NoAction()
					}
                }
                else {
                    Battle.beforeChosingMove(fight, fightPlayer.opponentFightPlayer(), fightPlayer.opponentFightPlayer().owner)

                    if (fight.battleType == BattleType.PVE)
                    {
                        // reset escape attempts
                        if (battleAction){
                            fightPlayer.fight.escapeAttempts = 0
                        }
                        // kies random wild move
                        fightPlayer.opponentFightPlayer().battleAction = WildMove.choseWildMove(fightPlayer.opponentFightPlayer())
                    }
                    else
                    {
                        // kies random move van npc
                        fightPlayer.opponentFightPlayer().battleAction = NPCHelper.choseNpcMove(fightPlayer.opponentFightPlayer().fightPokemon.ownerPokemon);
                    }
                }
            }
        }

        // Kijk of ronde gedaan kan worden
        if (fight.fightPlayer1.battleAction != null && fight.fightPlayer2.battleAction != null)
        {
            Battle.battle(fight)
        }
        else if (fight.battleType == BattleType.PVP){
            // We should wait on the other
            fightPlayer.waitOnOpponentMove = true
        }
        else {
            // Computer kan hierin terecht komen door de beforeChosingMove van hierboven
           // assert false
        }
    }

    public static void learnMove(Fight fight, FightPlayer fightPlayer, Move move)
    {
        // kijk of er nog een move geleerd kan worden
        // Criteria

        int totalMoves = fightPlayer.fightPokemon.ownerPokemon.ownerMoves.size()

        if (totalMoves < 4){

            OwnerMove ownerMove = new OwnerMove(
                    ownerPokemon : fightPlayer.fightPokemon.ownerPokemon,
                    move : move,
                    ppLeft : move.pp
            )
            fightPlayer.fightPokemon.ownerPokemon.addToOwnerMoves(ownerMove)
            fightPlayer.fightPokemon.ownerPokemon.save()

            fight.roundResult.battleActions.add(new MessageLog(fightPlayer.fightPokemon.name + "learned " + move.name + "."))

        }
        else{
            // zet learnMoves om te kunnen kiezen
            fightPlayer.learnMoves.add(move)
        }
    }

    public static void setBaseMoves(OwnerPokemon ownerPokemon){
        List<LearnableMove> learnableMoveList = LearnableMove.findAllByPokemonAndLearnLevelLessThanEquals(ownerPokemon.pokemon,ownerPokemon.level).findAll{ it.move.implemented }


        if (learnableMoveList){
            Collections.shuffle(learnableMoveList)

            int moves = learnableMoveList.size() > 4?4:learnableMoveList.size()

            (0..moves-1).each{ int i ->

                Move move = learnableMoveList.get(i).move
                OwnerMove ownerMove = new OwnerMove(
                        ownerPokemon : ownerPokemon,
                        move : move,
                        ppLeft : move.pp
                )
                ownerPokemon.addToOwnerMoves(ownerMove)
                ownerPokemon.save()
            }
        }
        else {
            println "No learnable moves for ${ownerPokemon.pokemon} lvl. ${ownerPokemon.level}"
        }

    }

}
