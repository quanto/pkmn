package game

import org.apache.commons.lang.RandomStringUtils

class Action {

    String identifier = RandomStringUtils.random(15, true, true)
    Map map
    int positionX
    int positionY

}
