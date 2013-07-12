package game

import game.action.Action

/**
 * We do not always want to show the same map, for this problem we have the AltMap.
 * The alt map is an alternative map that triggers on an condition.
 * We can replace the background, foreground or actions of the map.
 */
class AltMap {

    Condition condition
    int priority // Which alt map should take priority 0 = first

    boolean newDataForeground
    boolean newDataBackground
    boolean newActions

    String dataForeground
    String dataBackground

    static hasMany = [actions:Action]
    static belongsTo = [map: Map]

    static constraints = {
        priority unique: ['map']
        dataForeground nullable: true
        dataBackground nullable: true
    }

    static mapping = {
        condition column: "`condition`" // Condition is a reserved keyword in mysql
        dataForeground type:"text"
        dataBackground type:"text"
    }

    static AltMap getAltMap(Map map, Player player){
        return map.altMaps.sort{ it.priority }.find { Condition.conditionEval(player, it.condition) }
    }
}
