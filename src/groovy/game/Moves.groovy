package game

/**
 * Created with IntelliJ IDEA.
 * User: kevinverhoef
 * Date: 02-10-12
 * Time: 11:09
 * To change this template use File | Settings | File Templates.
 */
class Moves {

    /**
     * Player sets a move
     */
    public static void setMove(Fight fight, FightPlayer fightPlayer, int moveId, boolean clearLog = true)
    {

        // reset log
        if (clearLog){
            fight.log = "";
        }

        // :TODO not safe
        fightPlayer.move = Move.get(moveId)

        // Kijk of tegen de computer wordt gespeelt, dan wordt er een move gekozen
        if (fightPlayer.playerType == PlayerType.user && (fight.battleType == BattleType.PVE || fight.battleType == BattleType.PVN))
        {
            if (fight.battleType == BattleType.PVE)
            {
                // reset escape attempts
                if (moveId != -1){
                    fightPlayer.fight.escapeAttempts = 0
                }
                // kies random wild move

                fightPlayer.opponentFightPlayer().move = WildMove.choseWildMove(fightPlayer.opponentFightPlayer());
            }
            else
            {
                // :TODO implement
//                // kies random move van npc
//                fightPlayer.opponentFightPlayer().move = choseNpcMove(); // Testmove gezet
            }
            // overide move door status

            // freeze
            Freeze.checkFreeze(fight, fightPlayer);
            // :TODO implement
//            // paralyses
//            checkParalyses(2);
//            // confusion
//            checkConfusion(2);
//            // sleep
//            checkSleep(2);
        }

        // Kijk of ronde gedaan kan worden
        if (fight.fightPlayer1.move != null && fight.fightPlayer2.move != null)
        {
            Battle.battle(fight)
        }
    }

    public static void learnMove(Fight fight, FightPlayer fightPlayer, Move move)
    {
        // kijk of er nog een move geleerd kan worden
        // Criteria
        def c = OwnerMove.createCriteria()

        int totalMoves = OwnerMove.countByOwnerPokemon(fightPlayer.ownerPokemon)

//        sql = "select * from ownerpokemonmove where ownerId = '" . fight->{"player" . player . "Id"} . "' and ownerPokemonId = '" . fight->{"player" . player . "OwnerPokemonId"} . "'";

        if (totalMoves < 4)
        {

            OwnerMove ownerMove = new OwnerMove(
                    ownerPokemon : fightPlayer.ownerPokemon,
                    move : move,
                    ppLeft : move.pp
            )
            ownerMove.save()
            fight.log += "m:" + fightPlayer.ownerPokemon.pokemon.name + "learned " + move.name + ".;";
        }
        else
        {
            // zet learnMoves om te kunnen kiezen
            fightPlayer.learnMoves += move.id + ";";
        }

    }

}
