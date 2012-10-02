package game

/**
 * Created with IntelliJ IDEA.
 * User: kevinverhoef
 * Date: 29-09-12
 * Time: 08:13
 * To change this template use File | Settings | File Templates.
 */
class OwnerPokemon {

    Owner owner
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
    int attackStage = 0
    int defenseStage = 0
    int spDefenseStage = 0
    int spAttackStage = 0
    int speedStage = 0
    int accuracyStage = 0
    int criticalStage = 0
    int evasionStage = 0

    static constraints = {
        owner nullable:true
    }

    int calculateHP()
    {
        int ivHP = hpIV
        int baseHP = pokemon.baseHp
        int ev = 0; // EV wordt (nog) niet gebruikt
        int hp = ((ivHP + 2 * baseHP + (ev/4)) * level/100) + 10 + level;
        //$hp =  floor( floor( $baseHP * 2 + $ivHP + floor( $ev / 4 ) ) * $level / 100 + 10 + $level );

        return Math.floor(hp);
    }

}
