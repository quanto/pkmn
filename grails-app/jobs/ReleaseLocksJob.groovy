import game.lock.Lock

class ReleaseLocksJob {

    static triggers = {
        simple name:'releaseLocksJob', startDelay:30 * 1000, repeatInterval: 5 * 60 * 1000
    }

    def concurrent = false

    def execute() {
        Lock.executeUpdate("delete Lock where dateCreated < :yesterday and permanent = :false",['yesterday':new Date()-1,'false':false])
    }

}
