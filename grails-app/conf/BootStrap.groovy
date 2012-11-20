import game.*
import data.MoveImport
import data.PokemonImport
import data.EvolutionImport
import data.LearnableMovesImport
import data.EffectivenessImport
import data.MapImport
import data.ItemImport
import map.View
import data.PlayerImport
import org.apache.commons.lang.RandomStringUtils

class BootStrap {

    def grailsApplication

    def init = { servletContext ->

        boolean doImport = !Player.list().size() // Simple check to see if there's anything at all in the database.

        if (doImport){

            // init Roles
            Role adminRole = new Role(
                    authority:"ROLE_ADMIN"
            )
            adminRole.save()
            Role editorRole = new Role(
                    authority:"ROLE_MAP_EDITOR"
            )
            editorRole.save()


            PokemonImport.importPokemon()

            MoveImport.importMoves()

            EvolutionImport.importEvolution()

            EffectivenessImport.importEffectiveness()

            ItemImport.importItems()

            MapImport.importMaps()

            LearnableMovesImport.importLearnableMoves()

            PlayerImport.importPlayers()

            MapImport.setMapOwners()




            /*
            Pokemon pokemon = Pokemon.get(1)

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

            Player player = new Player(
                    username: "kevin",
                    name: "Kevin Verhoef",
                    password: "12345",
                    money: 1000,
                    registerDate : new Date(),
                    map: Map.findByName("Glooming forest -2x0"),
                    positionX : 12,
                    positionY : 12,
                    lastRecoverAction:RecoverAction.list().last(),
                    view: View.ShowMap
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
            PokemonCreator.addOwnerPokemonToOwner(Pokemon.get(4), 5, player2)

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
            */
            new NewsItem(
                    message: """
                    The last weeks we've been working hard to transfer our
                    existing php code to Grails to bring the game back to live.
                    The game is almost back at it's original state.
                    Where even implementing new functionalities now.
                    Some new feature are the iPad move support, a new layout and an improved map editor
                    that allowes us to rapidly draw maps.
                    """,
                    player: Player.findByUsername("kevin"),
                    date: new Date().parse("dd-MM-yyyy HH:mm:ss","14-10-2012 12:00:00")
            ).save()

            new NewsItem(
                    message: """
                        I'm refactoring the battle system at the moment. This may introduce
                        some new issues but eventualy it should resolve battle flow issues.
                    """,
                    player: Player.findByUsername("kevin"),
                    date: new Date().parse("dd-MM-yyyy HH:mm:ss","29-10-2012 12:00:00")
            ).save()

            new NewsItem(
                    message: """
                        Yeah, pvp is finaly working correct!
                    """,
                    player: Player.findByUsername("kevin"),
                    date: new Date().parse("dd-MM-yyyy HH:mm:ss","02-11-2012 12:00:00")
            ).save()

            new NewsItem(
                    message: """
                        Frontend became much smarter. Animations have been added and server calls reduced.
                    """,
                    player: Player.findByUsername("kevin"),
                    date: new Date().parse("dd-MM-yyyy HH:mm:ss","16-11-2012 12:00:00")
            ).save()


        }

        // Set identifiers
//        Action.list().each{
//            it.identifier = RandomStringUtils.random(15, true, true)
//            it.save()
//        }
    }

    def destroy = {
    }
}
