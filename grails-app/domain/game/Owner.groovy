package game

class Owner {

    String name

    static hasMany = [ownerItems: OwnerItem]

    static constraints = {

    }
}
