package game

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
            if (!ownerPokemonList.find{ it.partyPosition == i} ){
                return i
            }
        }

        return 0
    }

    public static OwnerPokemon getFirstAlivePokemon(Owner owner){
        List<OwnerPokemon> ownerPokemonList = OwnerPokemon.findAllByOwnerAndPartyPositionGreaterThan(owner,0)
        return ownerPokemonList.sort{ it.partyPosition * -1 }.last()
    }

}
