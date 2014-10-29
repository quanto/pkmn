package game

class Effectiveness {

    String attackType
    String type1
    String type2
    double effect

    static constraints = {
        type2 nullable: true
    }
}
