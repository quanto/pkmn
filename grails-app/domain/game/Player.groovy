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
    String mail
    String ip

    int positionX = 1
    int positionY = 1
    Map map
    boolean online
    int adminlvl
    Date registerDate
    Date lastLogin
    int money
    View view = View.ChoosePokemon
    // view

    static constraints = {
        mail nullable: true
        ip nullable: true
        lastLogin nullable: true
        map nullable: true
        username blank: false, unique: true
        password blank: false
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
            encodePassword()
        }
    }

    protected void encodePassword() {
        password = springSecurityService.encodePassword(password)
    }

}
