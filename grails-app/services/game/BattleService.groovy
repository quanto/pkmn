package game

import game.context.BattleType
import game.context.Fight
import game.context.FightPlayer
import game.context.FightPokemon
import game.context.PlayerData
import game.fight.Battle
import game.fight.ContinueMove
import game.fight.action.NoAction
import game.fight.action.SwitchAction
import game.item.UsableItem
import grails.converters.JSON
import map.View

class BattleService {

    FightFactoryService fightFactoryService

    static transactional = false

    def getBattleModel(Player player) {

        Fight fight

        if (player.fightNr != null){
            fight = fightFactoryService.getFight(player.fightNr)
        }

        if (player && fight){
            FightPlayer myFightPlayer = fight.myPlayer(player)

            def model = [
                    myFightPlayer: [
                            playerNr: myFightPlayer.playerNr
                    ],
                    fight: [
                            fightplayers: [
                                    [
                                            playerNr: fight.fightPlayer1.playerNr,
                                            name: fight.fightPlayer1.name,
                                            fightPokemon: [
                                                    level: fight.fightPlayer1.fightPokemon.level,
                                                    name: fight.fightPlayer1.fightPokemon.name,
                                                    hp: fight.fightPlayer1.fightPokemon.hp,
                                                    maxHp: fight.fightPlayer1.fightPokemon.maxHp,
                                            ],
                                    ],
                                    [
                                            playerNr: fight.fightPlayer2.playerNr,
                                            name: fight.fightPlayer2.name,
                                            fightPokemon: [
                                                    level: fight.fightPlayer2.fightPokemon.level,
                                                    name: fight.fightPlayer2.fightPokemon.name,
                                                    hp: fight.fightPlayer2.fightPokemon.hp,
                                                    maxHp: fight.fightPlayer2.fightPokemon.maxHp,

                                            ],
                                    ],
                            ]
                    ],
                    roundResult: fight.roundResult.toBattleString(myFightPlayer)
            ]
            return model
        }

    }

    def menuRequest(Player player, def params){

        Fight fight = fightFactoryService.getFight(player.fightNr)
        FightPlayer myFightPlayer = fight.myPlayer(player)

        // forget learn move
        if (myFightPlayer.learnMoves && params.forgetMove != null)
        {
            removeLearnMove(myFightPlayer)
        }
        // forget old move
        if (params.replaceMoveId != null){
            replaceMove(myFightPlayer,Integer.parseInt(params.replaceMoveId))
        }
        // continue normal flow and show the menu

        // We should set the switch action of the computer before setting our own action
        if (fight.switchRound && fight.battleType != BattleType.PVP){
            if (fight.fightPlayer2.mustSwitch){
                FightPokemon fightPokemon = myFightPlayer.opponentFightPlayer().party.find{ it.hp > 0 }
                myFightPlayer.opponentFightPlayer().battleAction = new SwitchAction(fightPokemon:fightPokemon)
            }
            else {
                myFightPlayer.opponentFightPlayer().battleAction = new NoAction()
            }
        }

        // Replace old move
        if (myFightPlayer.learnMoves && params.replaceMove != null)
        {
            return [
                    menuType: 'replaceMoveList',
                    ownerMoves: myFightPlayer.fightPokemon.ownerPokemon.ownerMoves.collect { OwnerMove it -> [
                            name: it.move.name,
                            id: it.id,
                    ]}
            ]
        }
        // leer moves
        else if (myFightPlayer.learnMoves){
            // haal move op
            Move move = myFightPlayer.learnMoves[0]

            return [
                    menuType: 'chooseLearnMove',
                    move: [
                            nr: move.nr,
                            name: move.name,
                    ],
                    fightPokemon: [
                            name: myFightPlayer.fightPokemon.name
                    ]
            ]
        }
        else if (fight.battleOver){
            return [
                    menuType: 'exitMenu'
            ]
        }
        else if (myFightPlayer.waitOnOpponentMove){
            return [
                    menuType: 'waitOnOpponentMove',
                    waiting: true,
            ]

        }
        else if (fight.switchRound && myFightPlayer.mustSwitch){
            def model = getPokmonListModel(myFightPlayer)
            model.mustChoose = true

            return model
        }
        // We should wait until the other switches
        else if ((fight.switchRound && !myFightPlayer.opponentFightPlayer().battleAction && params.pkmn == null)){
            return [
                    menuType: 'waitOnOpponentSwitch',
                    waiting: true,
            ]
        }
        // Ask to switch
        else if (fight.switchRound && params.pkmn == null){
            return [
                    menuType: 'chooseSwitchPokemon',
                    playerName: myFightPlayer.opponentFightPlayer().name,
                    pokemonName: myFightPlayer.opponentFightPlayer().battleAction.fightPokemon.name
            ]

        }
        // Switch pokemon list
        else if (params.pkmn != null && !fight.battleOver){
            def model = getPokmonListModel(myFightPlayer)
            model.mustChoose = false

            return model

        }
        else if (params.items != null){

            return [
                    menuType: 'itemList',
                    ownerItems: player.ownerItems.findAll{ it.item in UsableItem }.collect { OwnerItem it -> [

                            image: it.item.image,
                            name: it.item.name,
                            quantity: it.quantity,
                            ownerItemId: it.id,
                    ]}
            ]

        }
        else if (params.fight != null && !fight.battleOver){

            Battle.beforeChosingMove(fight, myFightPlayer, player);

            return [
                    menuType: 'movesList',
                    fightMoves: myFightPlayer.fightPokemon.fightMoves.collect {[
                            ppLeft: it.ppLeft,
                            moveName: it.move.name,
                            ownerMoveId: it.ownerMove.id,
                            pp: it.move.pp,
                    ]}
            ]

        }
        else {
            if (ContinueMove.continueMove(fight,myFightPlayer)){
                return [
                        update: true
                ]
            }
            else {
                return [
                        menuType: 'actionList'
                ]
            }
        }
    }

    public def exitBattle(Player player) {
        Fight fight = fightFactoryService.getFight(player.fightNr)

        if (fight.battleOver){
            player.view = View.ShowMap
            player.save(flush: true)
        }
    }

    private def getPokmonListModel(FightPlayer myFightPlayer){
        return [
                menuType: 'pokemonList',
                fightPokemons: myFightPlayer.party.findAll{ myFightPlayer.fightPokemon.ownerPokemonId != it.ownerPokemonId }.collect { FightPokemon fightPokemon -> [
                        level: fightPokemon.level,
                        name: fightPokemon.name,
                        hp: fightPokemon.hp,
                        maxHp: fightPokemon.maxHp,
                        threeValueNumber: fightPokemon.threeValueNumber(),
                        gender: fightPokemon.gender?.toString(),
                        partyPosition: fightPokemon.partyPosition,
                ]}
        ]
    }

    private void removeLearnMove(FightPlayer fightPlayer){
        fightPlayer.learnMoves.remove(fightPlayer.learnMoves[0])
    }

    private void replaceMove(FightPlayer fightPlayer, int forgetMoveId){
        OwnerMove oldOwnerMove = fightPlayer.fightPokemon.ownerPokemon.ownerMoves.find { it.id == forgetMoveId }

        if (oldOwnerMove){
            Move learnMove = fightPlayer.learnMoves[0]
            if (learnMove){
                fightPlayer.fightPokemon.ownerPokemon.removeFromOwnerMoves(oldOwnerMove)
                oldOwnerMove.delete()
                OwnerMove newOwnerMove = new OwnerMove(
                        ownerPokemon:fightPlayer.fightPokemon.ownerPokemon,
                        move: learnMove,
                        ppLeft: learnMove.pp
                )

                fightPlayer.fightPokemon.ownerPokemon.addToOwnerMoves(
                        newOwnerMove
                )
                fightPlayer.fightPokemon.ownerPokemon.save()
                removeLearnMove(fightPlayer)
            }
        }
    }


}
