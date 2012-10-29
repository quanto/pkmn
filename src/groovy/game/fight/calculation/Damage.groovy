package game.fight.calculation

class Damage {

    public static int calcDmg(int level, int attackStat, int attackPower, int defenseStat, double effectiveness)
    {
        Random random = new Random()
        int stab = 1; // 1 of 1.5, ??? :TODO
        int randomNumber = random.nextInt(15)+86
        int dmg = ((((2 * level / 5 + 2) * attackStat * attackPower / defenseStat) / 50) + 2) * stab * effectiveness * randomNumber / 100;
        return Math.round(dmg);
    }

}
