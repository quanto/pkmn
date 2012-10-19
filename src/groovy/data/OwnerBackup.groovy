package data

import game.OwnerPokemon
import game.OwnerMove
import game.Owner
import map.View
import game.Npc

class OwnerBackup {

    public static void npcExport(){
        Npc.list().each{ Npc npc ->
            saveNpc(npc)
        }
    }

    public static void saveNpc(Npc npc){
        try{
            File file = new File("import/npcs/" + npc.identifier + ".txt")

            FileWriter fstream = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fstream);

            out.write(getNpcData(npc))

            out.close();
        }catch (Exception e){
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static String getNpcData(Npc npc){
        String npcData = """<playerData>
${npc.identifier}
${npc.name}
</playerData>
${getOwnerPokemonBackupData(npc)}
"""
        return npcData
    }

    public static String getOwnerPokemonBackupData(Owner owner){
        List<OwnerPokemon> ownerPokemonList = OwnerPokemon.findAllByOwner(owner)

        String ownerPokemonData = ""

        ownerPokemonList.each { OwnerPokemon op ->
            String ownerMoves = ""
            op.ownerMoves.each { OwnerMove om ->
                ownerMoves +=
                    """<ownerMove>
${om.move.name}
${om.ppLeft}
</ownerMove>
"""
            }

            ownerPokemonData += """<ownerPokemon>
${op.isNpc}
${op.pokemon.nr}
${op.hpIV}
${op.attackIV}
${op.defenseIV}
${op.spAttackIV}
${op.spDefenseIV}
${op.speedIV}
${op.hp}
${op.gender}
${op.partyPosition}
${op.xp}
${op.level}
</ownerPokemon>
${ownerMoves}
"""
        }

        return ownerPokemonData
    }

}
