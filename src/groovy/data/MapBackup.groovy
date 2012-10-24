package data

import game.Map
import game.Action
import game.MapTransition
import game.ComputerAction
import game.RecoverAction
import game.MapMessage
import game.NpcAction
import game.MarketAction
import game.MapPokemon

class MapBackup {

    public static String exportMap(Map map){

        String data = ""

        try{
            File file = new File("import/maps/" + map.name + ".txt")

            FileWriter fstream = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fstream);

            out.write(getMapData(map))

            out.close();
        }catch (Exception e){
            System.err.println("Error: " + e.getMessage());
        }

        return data
    }

    public static String getMapData(Map map){

        return """<mapData>
${map.name}
${map.dataBackground}
${map.dataForeground}
${map.active}
${map.worldX}
${map.worldY}
</mapData>
${getPokemonData(map)}
${getMaptransitions(map)}
${getComputerActions(map)}
${getRecoverActions(map)}
${getMessageActions(map)}
${getNpcActions(map)}
${getMarketActions(map)}
"""
    }

    public static String getMarketActions(Map map){
        String data = ""
        map.actions.sort { it.positionX + "" + it.positionY } .each { Action action ->
            if(action in MarketAction){
                data += """<marketAction>
${action.positionX}
${action.positionY}
${action.condition?:''}
${action.conditionMetMessage?:''}
${action.conditionNotMetMessage?:''}
${action.market.identifier}
</marketAction>
"""
            }
        }
        return data
    }

    public static String getNpcActions(Map map){
        String data = ""
        map.actions.sort { it.positionX + "" + it.positionY } .each { Action action ->
            if(action in NpcAction){
                data += """<npcAction>
${action.positionX}
${action.positionY}
${action.condition?:''}
${action.conditionMetMessage?:''}
${action.conditionNotMetMessage?:''}
${action.owner.identifier}
</npcAction>
"""
                OwnerBackup.saveNpc(action.owner)
            }
        }
        return data
    }

    public static String getMessageActions(Map map){
        String data = ""
        map.actions.sort { it.positionX + "" + it.positionY } .each { Action action ->
            if(action in MapMessage){
                data += """<mapMessage>
${action.positionX}
${action.positionY}
${action.condition?:''}
${action.conditionMetMessage?:''}
${action.conditionNotMetMessage?:''}
${action.message}
</mapMessage>
"""
            }
        }
        return data
    }

    public static String getRecoverActions(Map map){
        String data = ""
        map.actions.sort { it.positionX + "" + it.positionY } .each { Action action ->
            if(action in RecoverAction){
                data += """<recoverAction>
${action.positionX}
${action.positionY}
${action.condition?:''}
${action.conditionMetMessage?:''}
${action.conditionNotMetMessage?:''}
</recoverAction>
"""
            }
        }
        return data
    }

    public static String getComputerActions(Map map){
        String data = ""
        map.actions.sort { it.positionX + "" + it.positionY } .each { Action action ->
            if(action in ComputerAction){
                data += """<computerAction>
${action.positionX}
${action.positionY}
${action.condition?:''}
${action.conditionMetMessage?:''}
${action.conditionNotMetMessage?:''}
</computerAction>
"""
            }
        }
        return data
    }

    public static String getMaptransitions(Map map){
        String data = ""
        map.actions.sort { it.positionX + "" + it.positionY } .each { Action action ->
            if(action in MapTransition){
                if (action.jumpTo){
                    data += """<mapTransition>
${action.positionX}
${action.positionY}
${action.condition?:''}
${action.conditionMetMessage?:''}
${action.conditionNotMetMessage?:''}
${action.jumpTo?.map?.name}
${action.jumpTo?.positionX}
${action.jumpTo?.positionY}
</mapTransition>
"""
                }
            }
        }
        return data
    }

    public static String getPokemonData(Map map){
        String pokemonData = ""
        map.mapPokemonList.sort{ it.pokemon.nr + "-" + it.fromLevel + "-" + it.toLevel }.each { MapPokemon mapPokemon ->
            pokemonData += """<pokemon>
${mapPokemon.pokemon.id}
${mapPokemon.chance}
${mapPokemon.fromLevel}
${mapPokemon.toLevel}
</pokemon>
"""
        }
        return pokemonData
    }


}
