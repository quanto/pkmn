package game.items

import game.Fight
import game.Owner
import game.Item
import game.FightPlayer
import game.Party
import game.Moves

class PokeBall {

    public static Double throwBall(Fight fight, Owner itemOwner, Item item, FightPlayer attackingFightPlayer, FightPlayer defendingFightPlayer){
        Integer catchFactor = null

        // Cherish Ball , normal ball
        if (item.name == "Cherish Ball" || item.name == "Poke Ball" || item.name == "Premier Ball")
        {
            catchFactor = 1
        }
        else if (item.name == "Master Ball")
        {
            catchFactor = 999
        }
        else if (item.name == "Net Ball")
        {
            if (defendingFightPlayer.ownerPokemon.pokemon.type1 == "bug" || defendingFightPlayer.ownerPokemon.pokemon.type1 == "water" || defendingFightPlayer.ownerPokemon.pokemon.type2 == "bug" || defendingFightPlayer.ownerPokemon.pokemon.type2 == "water")
            {
                catchFactor = 3
            }
            else
            {
                catchFactor = 1
            }
        }
        else if (item.name == "Safari Ball")
        {
            catchFactor = 1.5;
        }
        else if (item.name == "Ultra Ball")
        {
            catchFactor = 2
        }
        return catchFactor
    }

    public static void catchSuccess(Fight fight, Owner itemOwner, Item item, FightPlayer attackingFightPlayer, FightPlayer defendingFightPlayer, Double catchFactor){
        // http://www.dragonflycave.com/capture.aspx


        // Haal pokemon op voor catchRate
        int catchRate = defendingFightPlayer.ownerPokemon.pokemon.catchRate * catchFactor
        int M = defendingFightPlayer.ownerPokemon.calculateHP()
        int H = defendingFightPlayer.ownerPokemon.hp
        int B = 1;
        int S = 1
        int X = (((3 * M - 2 * H) * (catchRate * B)) / (3 * M)) * S

        if (1==1)//if (throwPokeball(catchRate))
        {
            fight.log += "m:You catcht ${defendingFightPlayer.ownerPokemon.pokemon.name}!;"
            fight.battleOver = true

            // Add the caught pokemon
            defendingFightPlayer.ownerPokemon.owner =  attackingFightPlayer.owner
            defendingFightPlayer.ownerPokemon.partyPosition = Party.getOpenPartyPosition(attackingFightPlayer.owner)
            defendingFightPlayer.ownerPokemon.save()

            // Add moves
            Moves.setBaseMoves(defendingFightPlayer.ownerPokemon)
        }
        else
        {
            fight.log = "m:You fail to catch ${defendingFightPlayer.ownerPokemon.pokemon.name};"
        }
    }

    //    int calc_chances(x, c, b, s, p) {
//
//        var num_x = x.length;
//        for (int i = 0; i < num_x; i++) {
//            if (x[i] >= 255) {
//                // auto-catch
//                chance.success += 1 / num_x;
//                chance.comp_val = 65536;
//            }
//            else {
//                // Using the full formula for Y
//                var y = Math.floor(1048560 / Math.floor(Math.sqrt(Math.floor(Math.sqrt(16711680 / x[i])))));
//                chance.comp_val = y;
//                var y_chance = y / 65536;
//                if (y_chance > 1) {
//                    // A chance greater than 100% is nonsensical, so bump it back down
//                    y_chance = 1;
//                }
//                var y_compl = 1 - y_chance;
//                var success_chance = Math.pow(y_chance, 4);
//                chance.success += success_chance / num_x;
//                chance.wobble3 += (Math.pow(y_chance, 3) - success_chance) / num_x;
//                chance.wobble2 += Math.pow(y_chance, 2) * y_compl / num_x;
//                chance.wobble1 += y_chance * y_compl / num_x;
//                chance.wobble0 += y_compl / num_x;
//            }
//        }
//
//        return chance;
//    }

}
