package game

import org.apache.commons.lang.RandomStringUtils

class Market {

    String identifier = RandomStringUtils.random(15, true, true)

    static hasMany = [marketItems: MarketItem]

    static constraints = {
    }
}
