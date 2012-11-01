package game.fight

import game.fight.calculation.Effective
import game.context.MoveCategory
import game.fight.status.Stat
import game.fight.calculation.Damage
import game.fight.log.MessageLog
import game.fight.calculation.CriticalHit
import game.fight.status.Recover
import game.context.MoveInfo
import game.context.Fight
import game.context.FightPlayer
import game.OwnerPokemon
import game.Move

class PhysicalDamage {

    public static void doDamage(Fight fight, MoveInfo moveInfo, FightPlayer attackFightPlayer, FightPlayer defendingFightPlayer, Move attackMove){

        OwnerPokemon attackOwnerPokemon = attackFightPlayer.ownerPokemon
        OwnerPokemon defendingOwnerPokemon = defendingFightPlayer.ownerPokemon

        for (int i=0;i<moveInfo.loop;i++)
        {

            if (moveInfo.calculateDamage && !moveInfo.oneHitKO)
            {

                // effectieviteit
                moveInfo.effectiveness = Effective.effectiveness(attackMove.type,attackOwnerPokemon.pokemon.type1,attackOwnerPokemon.pokemon.type2)

                if (attackMove.category == MoveCategory.SpecialMove)
                {
                    // bereken nieuwe stats
                    int attackStat = Stat.getStat(attackFightPlayer.spAttack, attackFightPlayer.spAttackStage);
                    int defenseStat = Stat.getStat(defendingFightPlayer.spDefense, defendingFightPlayer.spDefenseStage);
                    // bereken schade
                    moveInfo.damage = Damage.calcDmg(attackFightPlayer.level,attackStat,moveInfo.attackPower,defenseStat,moveInfo.effectiveness);
                }
                else
                {
                    // bereken nieuwe stats
                    int attackStat = Stat.getStat(attackFightPlayer.attack, attackFightPlayer.attackStage);
                    int defenseStat = Stat.getStat(defendingFightPlayer.defense, defendingFightPlayer.defenseStage);
                    // bereken schade
                    moveInfo.damage = Damage.calcDmg(attackFightPlayer.level,attackStat,moveInfo.attackPower,defenseStat,moveInfo.effectiveness);
                }


                // User recovers half of the damage inflicted on opponent.
                if (attackMove.name == "Drain Punch" || attackMove.name == "Absorb" || attackMove.name == "Giga Drain" || attackMove.name == "Leech Life" || attackMove.name == "Mega Drain" || attackMove.name == "Dream Eater")
                {
                    moveInfo.recoverAction = true;
                    moveInfo.effectAction = true;
                    moveInfo.recover = moveInfo.damage / 2;
                    if (moveInfo.recover < 1)
                        moveInfo.recover = 1;
                }

            }
            else if (moveInfo.oneHitKO)
            {
                defendingFightPlayer.hp = 0

                fight.roundResult.battleActions.add(new MessageLog("It's a one hit KO!"))
            }

            if (!moveInfo.oneHitKO)
            {
                // Critcal hits
                if (CriticalHit.tryCriticalHit(moveInfo.criticalHitStage))
                {
                    moveInfo.damage = moveInfo.damage * 2;
                    fight.roundResult.battleActions.add(new MessageLog("Critical hit!"))
                }

                moveInfo.damage = Math.floor(moveInfo.damage);

                // acties na schade berekening

                // Always leaves opponent with at least 1 HP.
                if (attackMove.name == "False Swipe")
                {
                    if (defendingFightPlayer.hp - moveInfo.damage < 0)
                    {
                        moveInfo.damage = defendingFightPlayer.hp - 1;
                    }
                }
                // 2x damage against an opponent using Fly.
                if (attackMove.name == "Twister" && defendingOwnerPokemon.pokemon.hasType("flying"))
                {
                    moveInfo.damage = moveInfo.damage * 2;
                }

                if (i == 0)
                {
                    // bericht effectiveness
                    if (moveInfo.effectiveness == 0)
                        fight.roundResult.battleActions.add(new MessageLog("It has no effect."))
                    else if (moveInfo.effectiveness == 0.25 || moveInfo.effectiveness == 0.5)
                        fight.roundResult.battleActions.add(new MessageLog("It`s not very effective."))
                    else if (moveInfo.effectiveness == 2 || moveInfo.effectiveness == 4)
                        fight.roundResult.battleActions.add(new MessageLog("It`s super effective."))
                }

                // Doe schade
                moveInfo.damage = Hp.doDamage(defendingFightPlayer,moveInfo.damage)

                // If there's no damage the user protected itself
                if (moveInfo.damage > 0){
                    fight.roundResult.battleActions.add(new MessageLog(attackOwnerPokemon.pokemon.name + " hits " + defendingOwnerPokemon.pokemon.name + " with " + attackMove.name + " ${moveInfo.damage} dmg."))
                    Recover.healthSlideLogAction(fight, defendingFightPlayer,moveInfo.damage);
                }

                if (moveInfo.recoil)
                {
                    int recoilDamage = Math.floor(moveInfo.damage / 100 * moveInfo.recoil)
                    Hp.doStatusDamage(attackFightPlayer,recoilDamage)

                    fight.roundResult.battleActions.add(new MessageLog(attackOwnerPokemon.pokemon.name + " hurts from recoil damage. ${moveInfo.damage} dmg."))
                    Recover.healthSlideLogAction(fight, attackFightPlayer,moveInfo.damage);
                    fight.roundResult.battleActions.add(new MessageLog("Recoil did ${moveInfo.damage} dmg."))
                }

                // Bericht bij loop
                if (moveInfo.loop > 1 && moveInfo.loop == (i + 1))
                {
                    fight.roundResult.battleActions.add(new MessageLog("Hits " + (i + 1) + " times"))
                }

            }



        }
    }

}
