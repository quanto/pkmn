package game

class Map {

    String name
    String dataForeground
    String dataBackground
    String pokemon
    boolean active

    static mapping = {
        dataForeground type:"text"
        dataBackground type:"text"
    }

    static constraints = {
        id display: true
    //    dataForeground display:false
    //    dataBackground display:false
    //    pokemon display:false
        active display:true
    }
}
