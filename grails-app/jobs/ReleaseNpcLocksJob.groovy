import game.NpcLock

class ReleaseNpcLocksJob {

    static triggers = {
        simple name:'releaseNpcLocksJob', startDelay:30 * 1000, repeatInterval: 5 * 60 * 1000
    }

    def concurrent = false

    def execute() {
        NpcLock.executeUpdate("delete NpcLock where dateCreated < :yesterday and permanent = :false",['yesterday':new Date()-1,'false':false])
    }

}
