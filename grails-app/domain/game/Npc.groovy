package game

import org.apache.commons.lang.RandomStringUtils

class Npc extends Owner {

    String identifier = RandomStringUtils.random(15, true, true)

    static constraints = {

    }

}
