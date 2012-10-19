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
    Owner npc
    Date dateCreated

    static constraints = {
    }
}
