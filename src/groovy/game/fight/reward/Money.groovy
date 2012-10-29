package game.fight.reward

import game.fight.log.MessageLog
import game.context.Fight
import game.Player
import game.OwnerPokemon
import game.context.BattleType

class Money {

    public static void giveMoney(Fight fight, int money)
    {
        if (money > 0)
        {
            fight.roundResult.battleActions.add(new MessageLog("You gained ${money} money."))

            Player player = fight.fightPlayer1.owner
            player.money += money
        }
    }


    public static int calculateMoney(Fight fight, OwnerPokemon ownerPokemon){
        int money = ownerPokemon.pokemon.baseEXP * Math.ceil(ownerPokemon.level / 2) / 15
        if (fight.battleType == BattleType.PVE){
            money = money / 3
        }

        money = Math.floor(money)
        return money
    }

}
