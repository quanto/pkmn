package game

/**
 * Created with IntelliJ IDEA.
 * User: kevinverhoef
 * Date: 02-10-12
 * Time: 11:09
 * To change this template use File | Settings | File Templates.
 */
class Moves {

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
