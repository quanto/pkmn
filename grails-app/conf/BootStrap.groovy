import game.*
import data.MoveImport
import data.PokemonImport
import data.EvolutionImport
import data.LearnableMovesImport
import data.EffectivenessImport
import data.MapImport
import data.TilesImport
import data.ItemImport

class BootStrap {

    def grailsApplication

    def init = { servletContext ->

        // init Roles
        Role adminRole = new Role(
                authority:"ROLE_ADMIN"
        )
        adminRole.save()

        PokemonImport.importPokemon()

        Pokemon pokemon = Pokemon.get(1)

        MoveImport.importMoves()

        EvolutionImport.importEvolution()

        EffectivenessImport.importEffectiveness()

        MapImport.importMaps()

        ItemImport.importItems()

        Map map = Map.findByName("Blossom_town")

        boolean newImport = true

        if (!newImport){
            MapPokemon mapPokemon = new MapPokemon(
                    map: map,
                    pokemon:pokemon,
                    chance : 50,
                    fromLevel:1,
                    toLevel:1
            )
            map.addToMapPokemonList(mapPokemon)

            Map map2 = Map.findByName("Blossom_town3")

            MapMessage mapMessage = new MapMessage(
                map:map2,
                positionX:12,
                positionY:13,
                message: "Hello there :)"
            )
            map2.addToActions(mapMessage)

            MapTransition mapTransition1 = new MapTransition(
                    positionX : 7,
                    positionY : 0,
                    map : map
            )
            mapTransition1.save()
            MapTransition mapTransition2 = new MapTransition(
                    positionX : 7,
                    positionY : 19,
                    map : map2,
                    jumpTo: mapTransition1
            )

            mapTransition1.jumpTo = mapTransition2


            map.addToActions(mapTransition1)
            map2.addToActions(mapTransition2)

            RecoverAction recoverAction = new RecoverAction(
                    positionX : 12,
                    positionY : 10,
                    map : map
            )
            map.addToActions(recoverAction)

            ComputerAction computerAction = new ComputerAction(
                    positionX : 13,
                    positionY : 10,
                    map : map
            )
            map.addToActions(computerAction)
        }

        TilesImport.importTiles()

        Player player = new Player(
                username: "kevin",
                name: "Kevin Verhoef",
                password: "12345",
                money: 1000,
                registerDate : new Date(),
                map:map,
                lastRecoverAction:RecoverAction.list().last()
        )
        player.save()

        new OwnerItem(
            item: Item.findByName("Poke Ball"),
            quantity: 100,
            owner: player
        ).save()

        new OwnerItem(
                item: Item.findByName("Premier Ball"),
                quantity: 50,
                owner: player
        ).save()

        Player player2 = new Player(
                username: "player2",
                name: "Player Two",
                password: "12345",
                money: 1000,
                registerDate : new Date(),
                map:map,
                lastRecoverAction:RecoverAction.list().last()
        )
        player2.save()
        PokemonCreator.addOwnerPokemonToOwner(Pokemon.get(16), 5, player)

        Owner npc = new Owner(
                name: "Npc1"
        )
        npc.save()

        new ChatMessage(
                message: "Hello World!",
                player: player
        ).save()

        Move move = Move.findByName("Growl")
        Move moveTackle = Move.findByName("Tackle")

        LearnableMovesImport.importLearnableMoves()

        // Balbasaur
        OwnerPokemon ownerPokemon = new OwnerPokemon(
                owner : player,
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
                level:5
        )
        ownerPokemon.xp = ownerPokemon.getBaseExp()


        OwnerMove ownerMove1 = new OwnerMove(
                ownerPokemon : ownerPokemon,
                move : moveTackle,
                ppLeft : moveTackle.pp
        )

        OwnerMove ownerMove2 = new OwnerMove(
                ownerPokemon : ownerPokemon,
                move : move,
                ppLeft : move.pp
        )


        ownerPokemon.addToOwnerMoves(ownerMove1)
        ownerPokemon.addToOwnerMoves(ownerMove2)

        ownerPokemon.save()

        // Charmander
        OwnerPokemon ownerPokemon2 = new OwnerPokemon(
                owner : player,
                isNpc : false,
                pokemon: Pokemon.get(4),
                hpIV :29,
                attackIV:16,
                defenseIV:22,
                spAttackIV:2,
                spDefenseIV:13,
                speedIV:20,
                hp:12,
                gender: Gender.Female,
                partyPosition: 2,
                level:3
        )
        ownerPokemon2.xp = ownerPokemon2.getBaseExp()


        OwnerMove ownerMove3 = new OwnerMove(
                ownerPokemon : ownerPokemon,
                move : moveTackle,
                ppLeft : moveTackle.pp
        )


        ownerPokemon2.addToOwnerMoves(ownerMove3)

        ownerPokemon2.save()

        // Squirtle - stored pokemon
        OwnerPokemon ownerPokemon3 = new OwnerPokemon(
                owner : player,
                isNpc : false,
                pokemon: Pokemon.get(7),
                hpIV :29,
                attackIV:16,
                defenseIV:22,
                spAttackIV:2,
                spDefenseIV:13,
                speedIV:20,
                hp:12,
                gender: Gender.Female,
                partyPosition: 0,
                level:3
        )
        ownerPokemon3.xp = ownerPokemon2.getBaseExp()


        OwnerMove ownerMove4 = new OwnerMove(
                ownerPokemon : ownerPokemon,
                move : moveTackle,
                ppLeft : moveTackle.pp
        )


        ownerPokemon3.addToOwnerMoves(ownerMove4)

        ownerPokemon3.save()



        // NPC
        OwnerPokemon ownerPokemonNPC = new OwnerPokemon(
                owner : npc,
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
                level:5
        )
        ownerPokemonNPC.xp = ownerPokemonNPC.getBaseExp()


        OwnerMove ownerMoveNPC1 = new OwnerMove(
                ownerPokemon : ownerPokemonNPC,
                move : moveTackle,
                ppLeft : moveTackle.pp
        )

        OwnerMove ownerMoveNPC2 = new OwnerMove(
                ownerPokemon : ownerPokemonNPC,
                move : move,
                ppLeft : move.pp
        )


        ownerPokemonNPC.addToOwnerMoves(ownerMoveNPC1)
        ownerPokemonNPC.addToOwnerMoves(ownerMoveNPC2)

        ownerPokemonNPC.save()

        OwnerPokemon ownerPokemonNPC2 = new OwnerPokemon(
                owner : npc,
                isNpc : false,
                pokemon: Pokemon.get(15),
                hpIV :29,
                attackIV:16,
                defenseIV:22,
                spAttackIV:2,
                spDefenseIV:13,
                speedIV:20,
                hp:20,
                gender: Gender.Female,
                partyPosition: 2,
                level:5
        )
        ownerPokemonNPC2.xp = ownerPokemonNPC2.getBaseExp()
        ownerPokemonNPC2.save()

        // Player 1 admin role
        new PlayerRole(
                player:player,
                role:adminRole
        ).save()

    }

    def destroy = {
    }
}
