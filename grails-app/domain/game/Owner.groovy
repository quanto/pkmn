package game

class Owner {

    String name
    int pveBattlesWon = 0
    int pveBattlesLost = 0
    int pvnBattlesWon = 0
    int pvnBattlesLost = 0
    Fight fight

    static constraints = {
        fight nullable :true
    }
}
