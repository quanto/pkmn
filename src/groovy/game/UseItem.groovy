package game

import game.items.PokeBall

class UseItem {

    public static void useItem(Fight fight, Owner itemOwner, OwnerItem ownerItem, FightPlayer attackingFightPlayer, FightPlayer defendingFightPlayer){

        Item item = ownerItem.item
        if (!item.implemented){
            fight.log = "m:You can not use item ${item.name} in battle.;";
        }
        else {

            fight.log = "m:${itemOwner.name} uses ${item.name}.;";

            // Try a PokeBall
            Double catchFactor = PokeBall.throwBall(fight, itemOwner, item, attackingFightPlayer, defendingFightPlayer)
            boolean throwBall = catchFactor > 0

            if (throwBall){
                PokeBall.catchSuccess(fight, itemOwner, item, attackingFightPlayer, defendingFightPlayer, catchFactor)
            }

            // Set an empty move so round can be done
            Moves.setMove(fight,attackingFightPlayer,null,false);

            if (ownerItem.quantity <= 1){
                itemOwner.removeFromOwnerItems(ownerItem)
                ownerItem.delete()
            }
            else {
                ownerItem.quantity -= 1
                ownerItem.save()
            }
        }
            /*
        // Potion
        else if (ownerItem.itemId == 40)
        {
            recover(20,ap);
        }
        //Soda Pop
        else if (ownerItem.itemId == 44)
        {
            recover(60,ap);
        }
        // Super potion
        else if (ownerItem.itemId == 45)
        {
            recover(500,ap);
        }
        // Lemonade
        else if (ownerItem.itemId == 32)
        {
            recover(80,ap);
        }
        // Hyper Potion
        else if (ownerItem.itemId == 29)
        {
            recover(200,ap);
        }
        // Fresh Water
        else if (ownerItem.itemId == 25 || ownerItem.itemId == 23)
        {
            recover(50,ap);
        }
        // MooMoo Milk
        else if (ownerItem.itemId == 37)
        {
            recover(100,ap);
        }
        // Old Gateau Lava Cookie Heal Powder Full Heal
        else if (ownerItem.itemId == 38 || ownerItem.itemId == 31 || ownerItem.itemId == 28 || ownerItem.itemId == 26)
        {
            removeAllStatusAfflictions(ap);
            fight.log .= "m:" . "All status effect have been cured.;";
        }
        // Full Restore
        else if (ownerItem.itemId == 27)
        {
            recover(fight.{"player" . ap . "MaxHp"},ap);
            removeAllStatusAfflictions(ap);
            fight.log .= "m:" . "All status effect have been cured.;";
        }
        // Max Potion
        else if (ownerItem.itemId == 35)
        {
            recover(fight.{"player" . ap . "MaxHp"},ap);
        }
        // Awakening
        else if (ownerItem.itemId == 19)
        {
            if (fight.{"player" . ap . "sleep"} > 0)
            {
                fight.{"player" . ap . "sleep"} = 0;
                fight.log .= "m:" . {"pokemonap"}.name . " wakes up.;";
            }
        }
        // Antidote
        else if (ownerItem.itemId == 18)
        {
            if (fight.{"player" . ap . "poison"} > 0 || fight.{"player" . ap . "badlypoisond"} > 0)
            {
                fight.{"player" . ap . "poison"} = 0;
                fight.{"player" . ap . "badlypoisond"} = 0;
                fight.log .= "m:" . {"pokemonap"}.name . " is no longer poisond.;";
            }
        }
        // Burn Heal
        else if (ownerItem.itemId == 20)
        {
            if (fight.{"player" . ap . "burn"} > 0)
            {
                fight.{"player" . ap . "burn"} = 0;
                fight.log .= "m:" . {"pokemonap"}.name . " is no longer burned.;";
            }
        }
        // Paralyze Heal
        else if (ownerItem.itemId == 39)
        {
            if (fight.{"player" . ap . "paralysis"} > 0)
            {
                fight.{"player" . ap . "paralysis"} = 0;
                fight.log .= "m:" . {"pokemonap"}.name . " is no longer paralyzed.;";
            }
        }
        // Ice Heal
        else if (ownerItem.itemId == 30)
        {
            if (fight.{"player" . ap . "freeze"} > 0)
            {
                fight.{"player" . ap . "freeze"} = 0;
                fight.log .= "m:" . {"pokemonap"}.name . " is no longer frozen.;";
            }
        }
        */

    }

}
