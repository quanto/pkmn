package game.items

import game.context.Fight
import game.item.Item
import game.fight.status.Recover
import game.context.FightPlayer
import game.fight.log.MessageLog

class RecoverItem {

    public static boolean useRecoverItem(Fight fight, Item item, FightPlayer attackingFightPlayer, FightPlayer defendingFightPlayer){

        // Potion
        if (item.name == "Potion")
        {
            Recover.recover(fight, 20,attackingFightPlayer)
        }
        //Soda Pop
        else if (item.name == "Soda Pop")
        {
            Recover.recover(fight, 60,attackingFightPlayer)
        }
        // Super potion
        else if (item.name == "Super Potion")
        {
            Recover.recover(fight, 500,attackingFightPlayer)
        }
        // Lemonade
        else if (item.name == "Lemonade")
        {
            Recover.recover(fight, 80,attackingFightPlayer)
        }
        // Hyper Potion
        else if (item.name == "Hyper Potion")
        {
            Recover.recover(fight, 200,attackingFightPlayer)
        }
        // Fresh Water
        else if (item.name == "Fresh Water")
        {
            Recover.recover(fight, 50,attackingFightPlayer)
        }
        // MooMoo Milk
        else if (item.name == "MooMoo Milk")
        {
            Recover.recover(fight, 100,attackingFightPlayer)
        }
        // Old Gateau Lava Cookie Heal Powder Full Heal
        else if (item.name == "Old Gateau" || item.name == "Lava Cookie" || item.name == "Full Heal")
        {
            Recover.removeAllStatusAfflictions(attackingFightPlayer)
            fight.roundResult.battleActions.add(new MessageLog("All status effect have been cured."))
        }
        // Full Restore
        else if (item.name == "Full Restore")
        {
            Recover.recover(fight, attackingFightPlayer.ownerPokemon.calculateHP() ,attackingFightPlayer)
            Recover.removeAllStatusAfflictions(attackingFightPlayer)
            fight.roundResult.battleActions.add(new MessageLog("All status effect have been cured."))
        }
        // Max Potion
        else if (item.name == "Max Potion")
        {
            Recover.recover(fight, attackingFightPlayer.ownerPokemon.calculateHP(), attackingFightPlayer)
        }
        // Awakening
        else if (item.name == "Awakening")
        {
            if (attackingFightPlayer.sleep > 0)
            {
                attackingFightPlayer.sleep = 0
                fight.roundResult.battleActions.add(new MessageLog("${attackingFightPlayer.ownerPokemon.pokemon.name} wakes up."))
            }
        }
        // Antidote
        else if (item.name == "Antidote")
        {
            if (attackingFightPlayer.poison > 0 || attackingFightPlayer.badlypoisond > 0)
            {
                attackingFightPlayer.poison = 0
                attackingFightPlayer.badlypoisond = 0

                fight.roundResult.battleActions.add(new MessageLog("${attackingFightPlayer.ownerPokemon.pokemon.name} is no longer poisond."))
            }
        }
        // Burn Heal
        else if (item.name == "Burn Heal")
        {
            if (attackingFightPlayer.burn > 0)
            {
                attackingFightPlayer.burn = 0
                fight.roundResult.battleActions.add(new MessageLog("${attackingFightPlayer.ownerPokemon.pokemon.name} is no longer burned."))
            }
        }
        // Paralyze Heal
        else if (item.name == "Paralyze Heal")
        {
            if (attackingFightPlayer.paralysis > 0)
            {
                attackingFightPlayer.paralysis = 0

                fight.roundResult.battleActions.add(new MessageLog("${attackingFightPlayer.ownerPokemon.pokemon.name} is no longer paralyzed."))
            }
        }
        // Ice Heal
        else if (item.name == "Ice Heal")
        {
            if (attackingFightPlayer.freeze > 0)
            {
                attackingFightPlayer.freeze = 0

                fight.roundResult.battleActions.add(new MessageLog("${attackingFightPlayer.ownerPokemon.pokemon.name} is no longer frozen."))
            }
        }
        else {
            return false
        }

        return true
    }

}
