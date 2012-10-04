package game

import map.View

class Player extends Owner{

    String password
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
    View view = View.ShowMap
    // view

    static constraints = {
        mail nullable: true
        ip nullable: true
        lastLogin nullable: true
        map nullable: true
    }

}
