package game

class MarketAction extends Action {

    static hasMany = [marketItems: MarketItem]

    static constraints = {
    }

}
