package game

import game.item.UsableItem
import grails.converters.JSON
import map.View

import game.fight.log.MessageLog
import game.context.FightPlayer
import game.context.PlayerData
import game.context.Fight
import game.fight.Battle
import game.fight.FightMove
import game.fight.UseItem
import game.fight.Run
import game.fight.action.MoveAction
import game.fight.action.SwitchAction
import game.context.BattleType
import game.fight.action.NoAction
import game.fight.ContinueMove
import game.context.FightPokemon

class BattleController {

    FightFactoryService fightFactoryService
    BattleService battleService

    def exit(){
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        battleService.exitBattle(player)

        def model = [
                updateView: true
        ]

        render model as JSON
    }

    def logRequest = {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()
        //owner = owner.merge()
        Fight fight = fightFactoryService.getFight(player.fightNr)

        FightPlayer myFightPlayer = fight.myPlayer(player)

        def model = [
                roundResult: fight.roundResult.toBattleString(myFightPlayer)
        ]

        render model as JSON
    }

    def doMove = {

        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        Fight fight = fightFactoryService.getFight(player.fightNr)
        FightPlayer myFightPlayer = fight.myPlayer(player)

        if (!fight.switchRound){

            int ownerPokemonMoveId = Integer.parseInt(params.id)

			Moves.setPlayerMove(fight, myFightPlayer, ownerPokemonMoveId)

            def model = [
                    roundResult: fight.roundResult.toBattleString(myFightPlayer)
            ]

            render model as JSON

        }
    }

    def menuRequest = {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        render battleService.menuRequest(player, params) as JSON
    }


    def noSwitch = {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        Fight fight = fightFactoryService.getFight(player.fightNr)
        FightPlayer myFightPlayer = fight.myPlayer(player)

        if (fight.switchRound && myFightPlayer.fightPokemon.hp > 0){
            Moves.setMove(fight,myFightPlayer, new NoAction())
        }

        def model = [
                roundResult: fight.roundResult.toBattleString(myFightPlayer)
        ]

        render model as JSON

    }

    def switchPokemon = {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        Fight fight = fightFactoryService.getFight(player.fightNr)
        FightPlayer myFightPlayer = fight.myPlayer(player)

        int partyPosition = Integer.parseInt(params.id)
        FightPokemon fightPokemon = myFightPlayer.party.find() { it.hp > 0 && it.partyPosition == partyPosition}

        if (fightPokemon){
            Moves.setMove(fight,myFightPlayer, new SwitchAction( fightPokemon: fightPokemon ))
        }

        def model = [
                roundResult: fight.roundResult.toBattleString(myFightPlayer)
        ]

        render model as JSON
    }

    def run = {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()


        Fight fight = fightFactoryService.getFight(player.fightNr)
        if (!fight.switchRound){
            Run.run(fight)
        }
        FightPlayer myFightPlayer = fight.myPlayer(player)

        def model = [
                roundResult: fight.roundResult.toBattleString(myFightPlayer)
        ]

        render model as JSON
    }

    def useItem = {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        Fight fight = fightFactoryService.getFight(player.fightNr)
        FightPlayer myFightPlayer = fight.myPlayer(player)

        if (!fight.switchRound){

            OwnerItem ownerItem = player.ownerItems.find() { it.id == Integer.parseInt(params.id) }

            if (ownerItem){
                UseItem.setItemAction(fight, myFightPlayer, ownerItem)
            }

        }

        def model = [
                roundResult: fight.roundResult.toBattleString(myFightPlayer)
        ]

        render model as JSON
    }

}
