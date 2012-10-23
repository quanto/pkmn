package data

import game.OwnerPokemon
import game.Npc

class NpcImport {

    /**
     * NPC's are imported in the mapImport.
     * @param npc
     */
    public static Npc importNpc(String identifier){

        println "Import npc ${identifier}"

        Npc npc = new Npc(
                identifier: identifier
        )

        File file = new File('import/npcs/' + identifier + '.txt')

        String node = ""

        def parts = []

        OwnerPokemon ownerPokemon

        file.eachLine { line ->

            if (line.contains("</ownerPokemon>")){
                node = ""
                ownerPokemon = PlayerImport.createOwnerPokemon(npc, parts)
                ownerPokemon.save()
                parts = []
            }
            else if (line.contains("</ownerMove>")){
                node = ""
                PlayerImport.createOwnerMove(ownerPokemon, parts)
                parts = []
            }
            else if (line.contains("</npcData>")){
                node = ""
                updateNpcData(npc, parts)
                parts = []
            }
            else if (line.contains("</ownerItem>")){
                node = ""
                PlayerImport.createOwnerItem(npc, parts)
                parts = []
            }

            if (node){
                parts.add(line)
            }

            if (line.contains("<ownerPokemon>")){
                node = "ownerPokemon"
            }
            else if (line.contains("<ownerMove>")){
                node = "ownerMove"
            }
            else if (line.contains("<npcData>")){
                node = "npcData"
            }
            else if (line.contains("<ownerItem>")){
                node = "ownerItem"
            }

        }

        npc.save()

        return npc
    }

    public static void updateNpcData(Npc npc, def parts){
        npc.name = parts[1]
        npc.permanentLock = new Boolean(parts[2])

        npc.save()
    }

}
