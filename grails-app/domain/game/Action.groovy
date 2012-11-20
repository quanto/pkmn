package game

import org.apache.commons.lang.RandomStringUtils

class Action {

    String identifier
    Map map
    int positionX
    int positionY

    static constraints = {
        identifier nullable:true
    }

    def beforeInsert() {
        if (!identifier){
            identifier = RandomStringUtils.random(15, true, true)
        }
    }

    def beforeUpdate() {
        if (!identifier){
            identifier = RandomStringUtils.random(15, true, true)
        }
    }

}
