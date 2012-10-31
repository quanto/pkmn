package game.fight

import game.context.FightPlayer
import game.fight.log.MessageLog

class Hp {

    /**
     * Return the real amount of damage done
     */
    static int doDamage(FightPlayer fightPlayer, int damage){
        damage = doStatusDamage(fightPlayer, damage)

        // Endure leaves the user with 1 hp
        if (fightPlayer.endure && fightPlayer.hp <= 0){
            damage = damage - 1
            fightPlayer.hp = 1
            fightPlayer.fight.roundResult.battleActions.add(new MessageLog("${fightPlayer.ownerPokemon.pokemon.name} endured the hit."))
        }
        return damage
    }

    static int doStatusDamage(FightPlayer fightPlayer, int damage){
        fightPlayer.hp = fightPlayer.hp - damage
        if (fightPlayer.hp < 0){
            damage = damage - Math.abs(fightPlayer.hp)
            fightPlayer.hp = 0
        }
        return damage
    }

    static int doRecover(FightPlayer fightPlayer, int recoverAmount){
        fightPlayer.hp = fightPlayer.hp + recoverAmount

        if (fightPlayer.hp > fightPlayer.maxHp){
            recoverAmount = recoverAmount - (fightPlayer.hp - fightPlayer.maxHp)
            fightPlayer.hp = fightPlayer.maxHp
        }
        return recoverAmount
    }

}
