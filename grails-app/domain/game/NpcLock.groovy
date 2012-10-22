package game

/**
 * If you win a battle against an NPC
 * this lock object will be created.
 *
 * The release will be done by the ReleaseNpcLocksJob.
 *
 *  This prevents users to battle the same
 *  npc over and over.
 */
class NpcLock {

    Player player
    Npc npc
    Date dateCreated

    // Permanent locks are never released. Gym leaders for example.
    boolean permanent

    static constraints = {
    }
}
