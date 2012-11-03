
import data.PlayerBackup

class BackupPlayersJob {

    static triggers = {
            simple name:'backupPlayersJob', startDelay:30 * 1000, repeatInterval: 60 * 1000
    }

    def concurrent = false

    def execute() {
        PlayerBackup.exportPlayers()
    }
}