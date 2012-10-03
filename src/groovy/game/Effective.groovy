package game

/**
 * Created with IntelliJ IDEA.
 * User: kevinverhoef
 * Date: 03-10-12
 * Time: 14:29
 * To change this template use File | Settings | File Templates.
 */
class Effective {

    public static double effectiveness(String attackType, String pokemonType1, String pokemonType2)
    {
        Effectiveness effectiveness = Effectiveness.findByType1AndType2AndAttackType(pokemonType1,pokemonType2,attackType)

        // Probeer type andersom voor resultaat
        if (pokemonType2 != "" && effectiveness != null){
            effectiveness = Effectiveness.findByType1AndType2AndAttackType(pokemonType2,pokemonType1,attackType)
        }

        if (effectiveness != null){
            return effectiveness.effect
        }
        else {
            return 1.0
        }
    }

}
