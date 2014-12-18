import game.*
import data.MoveImport
import data.PokemonImport
import data.EvolutionImport
import data.LearnableMovesImport
import data.EffectivenessImport
import data.MapImport
import data.ItemImport
import game.action.Action
import game.action.MapTransition
import game.action.MarketAction
import game.action.NpcAction
import game.lock.OneTimeActionLock
import map.View
import data.PlayerImport

class BootStrap {

    def grailsApplication

    def init = { servletContext ->

        boolean doImport = !Player.list().size() // Simple check to see if there's anything at all in the database.

//        println "--- Find duplicate actions"
//        Action.list().each { Action action ->
//            try {
//            List<Action> foundActions
//            if (action.altMap){
//                foundActions = Action.findAllByPositionXAndPositionYAndAltMap(action.positionX,action.positionY,action.altMap)
//            }
//            else {
//                foundActions = Action.findAllByPositionXAndPositionYAndAltMapIsNull(action.positionX,action.positionY)
//            }
//            foundActions.eachWithIndex { Action entry, int i ->
//                if (i>0){
//
//                    OneTimeActionLock.findAllByAction(entry).each { it.delete() }
//
//                    // Remove childs
//                    if (entry in NpcAction){
//                        entry.owner.delete()
//                    }
//                    else if (entry in MarketAction){
//                        entry.market.delete()
//                    }
//                    else if (entry in MapTransition){
//                        entry.jumpTo.delete()
//                    }
//
//                    entry.delete(flush:true)
//                }
//            }
//            }
//            catch (Exception e){
//                println e
//            }
//        }
//        println "--- Find duplicate actions done"


        // Reset Players in a fight
        Player.findAllByView(View.Battle).each {
            it.view = View.ShowMap
            it.save(flush: true)
        }

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

            new NewsItem(
                    message: """
                        Started working on the game again.
                    """,
                    player: Player.findByUsername("kevin"),
                    date: new Date().parse("dd-MM-yyyy HH:mm:ss","05-07-2013 12:00:00")
            ).save()

            new NewsItem(
                    message: """
                        Made a client side refactor.
                    """,
                    player: Player.findByUsername("kevin"),
                    date: new Date().parse("dd-MM-yyyy HH:mm:ss","05-07-2013 12:00:00")
            ).save()

            new NewsItem(
                    message: """
                        Chat has been improved. Messages are now scoped. Also a simple friend system was introduced.
                    """,
                    player: Player.findByUsername("kevin"),
                    date: new Date().parse("dd-MM-yyyy HH:mm:ss","05-07-2013 12:00:00")
            ).save()

            new NewsItem(
                    message: """
                        I'm trying to get the map actions ready so i can start to create a story line in the game.
                        The second area has now been filled with pokemon. It's now possible to show moving pokemon
                        to the map.
                    """,
                    player: Player.findByUsername("kevin"),
                    date: new Date().parse("dd-MM-yyyy HH:mm:ss","22-10-2013 12:00:00")
            ).save()

            new NewsItem(
                    message: """
                        Large map update.
                    """,
                    player: Player.findByUsername("kevin"),
                    date: new Date().parse("dd-MM-yyyy HH:mm:ss","11-10-2014 12:00:00")
            ).save()

            new NewsItem(
                    message: """
                        Started to JSONify everything. Chat, news and partylist done.
                    """,
                    player: Player.findByUsername("kevin"),
                    date: new Date().parse("dd-MM-yyyy HH:mm:ss","16-10-2014 12:00:00")
            ).save()
        }

        // Players can not be in a battle. When stopping the instance the battles are destroyed.
        Player.findAllByView(View.Battle){
            it.view = View.ShowMap
            it.save()
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
