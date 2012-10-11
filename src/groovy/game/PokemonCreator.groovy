package game

class PokemonCreator {

    /**
     * Creates an Owner Pokemon with it's base moves,
     * partyPosition and Exp.
     * @param pokemon
     * @param level
     * @param owner
     */
    public static void addOwnerPokemonToOwner(Pokemon pokemon, int level, Owner owner)
    {
        OwnerPokemon ownerPokemon = PokemonCreator.getOwnerPokemon(pokemon,level)
        ownerPokemon.owner = owner

        ownerPokemon.partyPosition = Party.getOpenPartyPosition(owner)
        ownerPokemon.xp = EXP.getExp(ownerPokemon.level,pokemon.levelRate)

        Moves.setBaseMoves(ownerPokemon)

        ownerPokemon.save()
    }

    /**
     * Creates an empty ownerPokemon. This does
     * NOT create the moves.
     * @param pokemon
     * @param level
     * @return
     */
    public static OwnerPokemon getOwnerPokemon(Pokemon pokemon, int level){
        Random random = new Random()

        OwnerPokemon ownerPokemon = new OwnerPokemon(
                isNpc : false,
                pokemon: pokemon,
                hpIV:random.nextInt(32),
                attackIV:random.nextInt(32),
                defenseIV:random.nextInt(32),
                spAttackIV:random.nextInt(32),
                spDefenseIV:random.nextInt(32),
                speedIV:random.nextInt(32),
                hp:40,
                partyPosition: 0,
                xp:0,
                level:level
        )
        ownerPokemon.hp = ownerPokemon.calculateHP();

        // Bepaal gender
        if (pokemon.maleRate <= 0 && pokemon.femaleRate <= 0)
        {
            ownerPokemon.gender = Gender.None;
        }
        else
        {
            if (random.nextInt(100) <= pokemon.maleRate){
                ownerPokemon.gender = Gender.Male
            }
            else
            {
                ownerPokemon.gender = Gender.Female
            }
        }

        return ownerPokemon
    }


}
