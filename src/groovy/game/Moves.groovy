package game

import game.fight.NPCHelper

class Moves {

    /**
     * Player sets a move
     */
    public static void setMove(Fight fight, FightPlayer fightPlayer, Move move, boolean clearLog = true)
    {

        // reset log
        if (clearLog){
            fight.log = "";
        }

        if (move){
            fightPlayer.move = move
        }
        else {
            fightPlayer.doNoMove = true
        }

        // Kijk of tegen de computer wordt gespeelt, dan wordt er een move gekozen
        if (fightPlayer.playerType == PlayerType.user && (fight.battleType == BattleType.PVE || fight.battleType == BattleType.PVN))
        {
            if (!fightPlayer.doNoMove || fightPlayer.playerType == PlayerType.user){

                Battle.beforeChosingMove(fight, fightPlayer.opponentFightPlayer(), fightPlayer.opponentFightPlayer().owner)

                if (fight.battleType == BattleType.PVE)
                {
                    // reset escape attempts
                    if (move){
                        fightPlayer.fight.escapeAttempts = 0
                    }
                    // kies random wild move
                    fightPlayer.opponentFightPlayer().move = WildMove.choseWildMove(fightPlayer.opponentFightPlayer())
                }
                else
                {
                    // kies random move van npc
                    fightPlayer.opponentFightPlayer().move = NPCHelper.choseNpcMove(fightPlayer.opponentFightPlayer().ownerPokemon);
                }
            }
        }

        // Kijk of ronde gedaan kan worden
        if ((fight.fightPlayer1.move != null || fight.fightPlayer1.doNoMove) && (fight.fightPlayer2.move != null  || fight.fightPlayer1.doNoMove))
        {
            Battle.battle(fight)
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

            fight.log += "m:" + fightPlayer.ownerPokemon.pokemon.name + "learned " + move.name + ".;";
        }
        else
        {
            // zet learnMoves om te kunnen kiezen
            fightPlayer.learnMoves += move.id + ";";
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
