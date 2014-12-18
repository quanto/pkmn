package game.items

import game.OwnerPokemon
import game.context.Fight
import game.Owner
import game.item.Item
import game.context.FightPlayer
import game.Party
import game.Moves

import game.fight.log.MessageLog

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
            if (defendingFightPlayer.fightPokemon.hasType("bug") || defendingFightPlayer.fightPokemon.hasType("water"))
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

        // :TODO implement catch formula
        // Haal pokemon op voor catchRate
        int catchRate = defendingFightPlayer.fightPokemon.catchRate * catchFactor

        int M = defendingFightPlayer.fightPokemon.maxHp
        int H = defendingFightPlayer.fightPokemon.hp
        int B = 1
        int S = 1
        int X = (((3 * M - 2 * H) * (catchRate * B)) / (3 * M)) * S

        if (1==1)//if (throwPokeball(catchRate))
        {

            fight.roundResult.battleActions.add(new MessageLog("You catcht ${defendingFightPlayer.fightPokemon.name}!"))
            fight.battleOver = true

            // Add the caught pokemon

            OwnerPokemon ownerPokemon = defendingFightPlayer.fightPokemon.ownerPokemon

            ownerPokemon.owner =  attackingFightPlayer.owner
            ownerPokemon.partyPosition = Party.getOpenPartyPosition(attackingFightPlayer.owner)
            ownerPokemon.save()

            // Add moves
            Moves.setBaseMoves(ownerPokemon)
        }
        else
        {
            fight.roundResult.battleActions.add(new MessageLog("You fail to catch ${defendingFightPlayer.fightPokemon.name}"))
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
