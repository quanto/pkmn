package data

import game.OwnerPokemon
import game.Npc

class NpcImport {

    /**
     * NPC's are imported in the mapImport.
     * @param npc
     */
    public static void importNpc(Npc npc){

        println "Import npc ${npc.identifier}"

        File file = new File('import/npcs/' + npc.identifier + '.txt')

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
            if (node){
                parts.add(line)
            }

            if (line.contains("<ownerPokemon>")){
                node = "ownerPokemon"
            }
            else if (line.contains("<ownerMove>")){
                node = "ownerMove"
            }
        }


    }

}
