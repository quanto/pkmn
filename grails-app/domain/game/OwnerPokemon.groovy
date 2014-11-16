package game

import game.fight.reward.EXP
import game.context.Gender

/**
 * Created with IntelliJ IDEA.
 * User: kevinverhoef
 * Date: 29-09-12
 * Time: 08:13
 * To change this template use File | Settings | File Templates.
 */
class OwnerPokemon {

    static belongsTo = [owner: Owner]
    static hasMany = [ownerMoves: OwnerMove]
    boolean isNpc
    Pokemon pokemon
    int hpIV
    int attackIV
    int defenseIV
    int spAttackIV
    int spDefenseIV
    int speedIV
    int hp
    Gender gender
    int partyPosition
    int xp
    int level
    int burn = 0
    int freeze = 0
    int paralysis = 0
    int poison = 0
    int badlyPoisond = 0
    int sleep = 0
    int confusion = 0
    int curse = 0

    static fetchMode = [pokemon: 'eager']

    static constraints = {
        owner nullable:true
    }

    static transients = ['baseExp', 'expPercentage', 'hpPercentage']

    int calculateHP()
    {
        int ivHP = hpIV
        int baseHP = pokemon.baseHp
        int ev = 0; // EV wordt (nog) niet gebruikt
        int hp = ((ivHP + 2 * baseHP + (ev/4)) * level/100) + 10 + level;
        //$hp =  floor( floor( $baseHP * 2 + $ivHP + floor( $ev / 4 ) ) * $level / 100 + 10 + $level );

        return Math.floor(hp);
    }

    public int getHpPercentage(){
        Math.round(100 / calculateHP() * hp)
    }

    public int getBaseExp(){
        EXP.getExp(level,pokemon.levelRate)
    }

    public int getExpPercentage(){
        int percentage = EXP.getExpPercentage(level, pokemon.levelRate, xp)
        return percentage>0?percentage:0
    }


}
