package game

import game.fight.NPCHelper

import game.fight.log.MessageLog
import game.context.FightPlayer
import game.context.PlayerType
import game.context.BattleType
import game.context.Fight
import game.fight.Battle
import game.fight.WildMove
import game.fight.action.BattleAction
import game.fight.action.SwitchAction

class Moves {

    /**
     * Player sets a move
     */
    public static void setMove(Fight fight, FightPlayer fightPlayer, BattleAction battleAction, boolean clearLog = true)
    {

        if (battleAction){
            fightPlayer.battleAction = battleAction
        }
        else {
            fightPlayer.doNoMove = true // :TODO Get rid of this. Should use NoAction if needed
        }

        // Kijk of tegen de computer wordt gespeelt, dan wordt er een move gekozen
        if (fightPlayer.playerType == PlayerType.user && (fight.battleType == BattleType.PVE || fight.battleType == BattleType.PVN))
        {
            if (!fightPlayer.doNoMove || fightPlayer.playerType == PlayerType.user){

                if (fight.switchRound){
                    // Bring out the next pvn pokemon
                    OwnerPokemon nextOwnerPokemon = OwnerPokemon.findByOwnerAndPartyPosition(fight.fightPlayer2.owner,fight.fightPlayer2.ownerPokemon.partyPosition + 1)

                    fightPlayer.opponentFightPlayer().battleAction = new SwitchAction(ownerPokemon:nextOwnerPokemon)
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
                        fightPlayer.opponentFightPlayer().battleAction = NPCHelper.choseNpcMove(fightPlayer.opponentFightPlayer().ownerPokemon);
                    }
                }
            }
        }

        // Kijk of ronde gedaan kan worden
        if ((fight.fightPlayer1.battleAction != null || fight.fightPlayer1.doNoMove) && (fight.fightPlayer2.battleAction != null  || fight.fightPlayer1.doNoMove))
        {
            Battle.battle(fight)
        }
        else if (fight.battleType == BattleType.PVP){
            // We should wait on the other
            fightPlayer.waitOnOpponentMove = true
        }
        else {
            assert false
        }
    }

    public static void learnMove(Fight fight, FightPlayer fightPlayer, Move move)
    {
        // kijk of er nog een move geleerd kan worden
        // Criteria

        int totalMoves = fightPlayer.ownerPokemon.ownerMoves.size()

        if (totalMoves < 4)
        {

            OwnerMove ownerMove = new OwnerMove(
                    ownerPokemon : fightPlayer.ownerPokemon,
                    move : move,
                    ppLeft : move.pp
            )
            fightPlayer.ownerPokemon.addToOwnerMoves(ownerMove)
            fightPlayer.ownerPokemon.save()

            fight.roundResult.battleActions.add(new MessageLog(fightPlayer.ownerPokemon.pokemon.name + "learned " + move.name + "."))

        }
        else
        {
            // zet learnMoves om te kunnen kiezen
            fightPlayer.learnMoves.add(move)
        }
    }

    public static void setBaseMoves(OwnerPokemon ownerPokemon)
    {
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
