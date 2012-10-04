package game

class Map {

    String name
    String dataForeground
    String databackground
    String pokemon
    boolean active

    static mapping = {
        dataForeground type:"text"
        databackground type:"text"
    }

    static constraints = {
        id display: true
        dataForeground display:false
        databackground display:false
        pokemon display:false
        active display:true
    }
}
