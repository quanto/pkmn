import game.FightFactoryService
import game.context.Fight

/**
 * Fights older then x amount of time should be destroyed so the
 * don't remain forever in memory.
 */
class RemoveOldFightsJob {

    FightFactoryService fightFactoryService

    static triggers = {
        simple name:'removeOldFightsJob', startDelay:30 * 1000, repeatInterval: 5 * 60 * 1000
    }

    def concurrent = false

    def execute() {
        FightFactoryService.fights.findAll{ new Date()-1 > it.createDate  }.each { Fight fight ->
            println "Destroy fight ${fight}"
            fightFactoryService.endFight(fight)
        }
    }

}
