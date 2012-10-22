package game

import map.View

class Player extends Owner{

    transient springSecurityService

    String username
    String password
    boolean enabled = true
    boolean accountExpired = false
    boolean accountLocked = false
    boolean passwordExpired = false
    String email
    String recoverPasswordSecret
    String ip

    int positionX = 1
    int positionY = 1
    Map map
    boolean online
    int adminlvl
    Date registerDate = new Date()
    Date lastLogin
    int money = 100
    View view = View.ChoosePokemon
    // Stored to send the player back to the last recover location when he loses
    RecoverAction lastRecoverAction
    // view

    int pveBattlesWon = 0
    int pveBattlesLost = 0
    int pvnBattlesWon = 0
    int pvnBattlesLost = 0
    Integer fightNr

    static constraints = {
        email nullable: true
        ip nullable: true
        lastLogin nullable: true
        map nullable: true
        username blank: false, unique: true
        password blank: false
        fightNr nullable :true
        recoverPasswordSecret nullable: true
    }

    static mapping = {
        password column: '`password`'
    }

    Set<Role> getAuthorities() {
        PlayerRole.findAllByPlayer(this).collect { it.role } as Set
    }

    def beforeInsert() {
        encodePassword()
    }

    def beforeUpdate() {
        if (isDirty('password')) {
            println "encode"
            println password
            encodePassword()
        }
    }

    protected void encodePassword() {
        password = springSecurityService.encodePassword(password)
    }

}
