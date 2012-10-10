package game

/**
 * Created with IntelliJ IDEA.
 * User: kevinverhoef
 * Date: 02-10-12
 * Time: 20:54
 * To change this template use File | Settings | File Templates.
 */
class Party {

    public static int getOpenPartyPosition(Owner owner)
    {
        List<OwnerPokemon> ownerPokemonList = OwnerPokemon.findAllByPartyPositionGreaterThanAndOwner(0,owner)?.sort{ it.partyPosition }

        if (ownerPokemonList.size() == 6)
        {
            return 0;
        }

        for (int i=1;i<7;i++)
        {
            if (ownerPokemonList.get(i).partyPosition != i)
            {
                return i
                break
            }
        }

        return 0
    }

}
