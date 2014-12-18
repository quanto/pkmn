package game

class PartyService {

    static transactional = false

    public def getPartyModel(Player player){
        def ownerPokemonList = OwnerPokemon.findAllByPartyPositionGreaterThanAndOwner(0,player)
        return getPokemonModel(ownerPokemonList)
    }

    public def getComputerModel(Player player){
        def ownerPokemonList = OwnerPokemon.findAllByOwner(player)
        def partyList = ownerPokemonList?.findAll() { it.partyPosition > 0 }
        def computerList = ownerPokemonList?.findAll() { it.partyPosition == 0 }

        def model = [
                party: getPokemonModel(partyList),
                stored: getPokemonModel(computerList),
        ]
        return model
    }

    public void correctPartyPositions(Player player){
        List<OwnerPokemon> ownerPokemonList = OwnerPokemon.findAllByPartyPositionGreaterThanAndOwner(0,player)
        ownerPokemonList.sort{ it.partyPosition }.eachWithIndex{ OwnerPokemon ownerPokemon, int i ->
            ownerPokemon.partyPosition = (i+1)
            ownerPokemon.save()
        }
    }

    private def getPokemonModel(ownerPokemonList){
        ownerPokemonList.collect {
            [
                    id: it.id,
                    partyPosition: it.partyPosition,
                    gender: it.gender.toString(),
                    hp: it.hp,
                    totalHp: it.calculateHP(),
                    level: it.level,
                    hpPercentage: it.getHpPercentage(),
                    expPercentage: it.getExpPercentage(),
                    pokemon: [
                            id: it.pokemon.id,
                            name: it.pokemon.name,
                            threeValueNumber: it.pokemon.threeValueNumber(),
                            height: it.pokemon.height,
                            weight: it.pokemon.weight
                    ]
            ]
        }.sort { it.partyPosition }
    }

}
