package game

class Money {

    public static void giveMoney(Fight fight, int money)
    {
        if (money > 0)
        {
            fight.log += "m:You gained ${money} money.;"
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
