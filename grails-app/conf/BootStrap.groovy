import game.*
import data.MoveImport
import data.PokemonImport
import data.EvolutionImport
import data.LearnableMovesImport
import data.EffectivenessImport
import data.MapImport

class BootStrap {

    def init = { servletContext ->

        Player owner = new Player(name: "Kevin", password: "12345", money: 1000, registerDate : new Date())
        owner.save()

        PokemonImport.importPokemon()

        Pokemon pokemon = Pokemon.get(1)

        MoveImport.importMoves()

        EvolutionImport.importEvolution()

        EffectivenessImport.importEffectiveness()

        MapImport.importMaps()

        Move move = Move.findByName("Growl")
        Move moveTackle = Move.findByName("Tackle")

        LearnableMovesImport.importLearnableMoves()

        OwnerPokemon ownerPokemon = new OwnerPokemon(
                owner : owner,
                isNpc : false,
                pokemon: pokemon,
                hpIV :29,
                attackIV:16,
                defenseIV:22,
                spAttackIV:2,
                spDefenseIV:13,
                speedIV:20,
                hp:12,
                gender: Gender.Male,
                partyPosition: 1,
                xp:0,
                level:5
        )


        OwnerMove ownerMove1 = new OwnerMove(
                ownerPokemon : ownerPokemon,
                move : moveTackle,
                ppLeft : 40
        )

        OwnerMove ownerMove2 = new OwnerMove(
                ownerPokemon : ownerPokemon,
                move : move,
                ppLeft : 30
        )


        ownerPokemon.addToOwnerMoves(ownerMove1)
        ownerPokemon.addToOwnerMoves(ownerMove2)

        ownerPokemon.save()

    }

    def destroy = {
    }
}
