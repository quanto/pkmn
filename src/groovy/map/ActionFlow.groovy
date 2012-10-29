package map

import game.Action
import game.Condition
import game.MapTransition
import game.MapMessage
import game.RecoverAction
import game.fight.status.Recover
import game.ComputerAction
import game.MarketAction
import game.NpcAction
import game.NpcLock
import game.context.Fight
import game.context.BattleType
import game.Player
import game.FightFactoryService

class ActionFlow {

    public static ActionResult decideAction(Player player, ActionTrigger actionTrigger, FightFactoryService fightFactoryService){
        Action action = Action.findByMapAndPositionXAndPositionY(player.map,player.positionX,player.positionY)

        if (action){
            // Check if we even should trigger the action
            if (!action.triggerBeforeStep && actionTrigger == ActionTrigger.Move){
                return null
            }
            else if (!action.triggerOnActionButton && actionTrigger == ActionTrigger.ActionBtn){
                return null
            }

            ActionResult actionResult = new ActionResult()

            // Support condition on actions
            boolean conditionEval = true
            if (action.condition){

                conditionEval = Condition.conditionEval(player,action.condition)
                if (conditionEval && action.conditionMetMessage){
                    actionResult.evalMessage += "setMessage('${action.conditionMetMessage?.encodeAsHTML()}');"
                }
            }

            // Action flow
            if (action.condition && !conditionEval){
                if (action.conditionNotMetMessage){
                    actionResult.evalMessage += "setMessage('${action.conditionNotMetMessage?.encodeAsHTML()}');"

                    // Prevent the step
                    if (action.conditionalStep){
                        actionResult.allowStep =  false
                    }
                }
                else {
                    actionResult.evalMessage += "setMessage('Condition not met for condition: ${action.condition?.encodeAsHTML()}!');"

                    // Prevent the step
                    if (action.conditionalStep){
                        actionResult.allowStep =  false
                    }
                }
            }
            else if (action in MapTransition){
                MapTransition mapTransition = (MapTransition)action

                // Action is allowed
                player.positionX = mapTransition.jumpTo.positionX
                player.positionY = mapTransition.jumpTo.positionY
                player.setMap mapTransition.jumpTo.map

                actionResult.evalMessage += "getView()"
            }
            else if (action in MapMessage){
                MapMessage mapMessage = (MapMessage)action
                actionResult.evalMessage += "setMessage('${mapMessage.message?.encodeAsHTML()}');"
            }
            else if (action in RecoverAction){
                Recover.recoverParty(player)
                // Set last recover position
                player.lastRecoverAction = (RecoverAction)action

                actionResult.evalMessage += "setMessage('Your pokemon have been recovered!');"
            }
            else if (action in ComputerAction){
                player.view = View.ShowComputer
                player.save(flush:true)
                actionResult.evalMessage += "getView()"
            }
            else if (action in MarketAction){
                player.view = View.ShowMarket
                player.save(flush:true)
                actionResult.evalMessage += "getView()"
            }
            else if (action in NpcAction){

                NpcAction npcAction = action

                // Check locks
                NpcLock npcLock = NpcLock.findByPlayerAndNpc(player,npcAction.owner)

                if (npcLock){
                    if (npcAction.owner.npcLockedMessage){
                        actionResult.evalMessage += "setMessage('${npcAction.owner.npcLockedMessage?.encodeAsHTML()}');"
                    }
                    else if (npcLock.permanent){
                        actionResult.evalMessage += "setMessage('You already defeated ${npcAction.owner.name?.encodeAsHTML()}.');"
                    }
                    else {
                        actionResult.evalMessage += "setMessage('You already defeated ${npcAction.owner.name?.encodeAsHTML()} today. ${npcAction.owner.name?.encodeAsHTML()} is still recovering, come back later.');"
                    }
                }
                else {
                    Fight fight = fightFactoryService.startFight(BattleType.PVN,player,npcAction.owner,null,null)

                    // koppel gevecht aan speler
                    player.fightNr = fight.nr
                    player.view = View.Battle

                    player.save(flush:true)
                    actionResult.evalMessage += "getView()"
                }
            }
            else {
                // Should not be reachable, unknown action
                assert false
            }

            return actionResult
        }
        return null
    }

}