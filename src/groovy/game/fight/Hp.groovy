package game.fight

import game.context.FightPlayer
import game.fight.log.MessageLog

class Hp {

    /**
     * Return the real amount of damage done
     */
    static int doDamage(FightPlayer fightPlayer, int damage){
        if (fightPlayer.protect){
            damage = 0
            fightPlayer.fight.roundResult.battleActions.add(new MessageLog("${fightPlayer.fightPokemon.name} protected itself."))
        }

        damage = doStatusDamage(fightPlayer, damage)

        // Endure leaves the user with 1 hp
        if (fightPlayer.endure && fightPlayer.fightPokemon.hp <= 0){
            damage = damage - 1
            fightPlayer.fightPokemon.hp = 1
            fightPlayer.fight.roundResult.battleActions.add(new MessageLog("${fightPlayer.fightPokemon.name} endured the hit."))
        }
        return damage
    }

    static int doStatusDamage(FightPlayer fightPlayer, int damage){
        fightPlayer.fightPokemon.hp = fightPlayer.fightPokemon.hp - damage
        if (fightPlayer.fightPokemon.hp < 0){
            damage = damage - Math.abs(fightPlayer.fightPokemon.hp)
            fightPlayer.fightPokemon.hp = 0
        }
        return damage
    }

    static int doRecover(FightPlayer fightPlayer, int recoverAmount){
        fightPlayer.fightPokemon.hp = fightPlayer.fightPokemon.hp + recoverAmount

        if (fightPlayer.fightPokemon.hp > fightPlayer.fightPokemon.maxHp){
            recoverAmount = recoverAmount - (fightPlayer.fightPokemon.hp - fightPlayer.fightPokemon.maxHp)
            fightPlayer.fightPokemon.hp = fightPlayer.fightPokemon.maxHp
        }
        return recoverAmount
    }

}
