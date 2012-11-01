package game

class Pokemon {

    int nr
    String name
    String type1
    String type2
    int baseHp
    int baseAttack
    int baseDefense
    int baseSpAttack
    int baseSpDefense
    int baseSpeed
    double maleRate
    double femaleRate
    int catchRate
    int baseEXP
    String levelRate
    String height
    String weight

    String threeValueNumber()
    {
        return String.format("%3s", nr.toString()).replaceAll(' ','0')
    }

    @Override
    public String toString(){
        return "#${nr} ${name}"
    }

    boolean hasType(String type){
        return type1 == type || type2 == type
    }

}
