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
import game.PvpSelectAction
import game.BoulderAction
import game.BushAction
import game.FindItemAction

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
${map.owner?.username?:'kevin'}
</mapData>
${getPokemonData(map)}
${getMaptransitions(map)}
${getComputerActions(map)}
${getRecoverActions(map)}
${getMessageActions(map)}
${getNpcActions(map)}
${getMarketActions(map)}
${getPvpSelectActions(map)}
${getBoulderActions(map)}
${getBushActions(map)}
${getFindItemActions(map)}
"""
    }

    public static String getBaseActionProperties(Action action){
        """${action.positionX}
${action.positionY}
${action.identifier}
${action.condition?:''}
${action.conditionMetMessage?:''}
${action.conditionNotMetMessage?:''}
${action.triggerOnActionButton}
${action.triggerBeforeStep}
${action.conditionalStep}
${action.placeOneTimeActionLock}"""
    }

    public static String getBoulderActions(Map map){
        String data = ""
        map.actions.sort { it.positionX + "" + it.positionY } .each { Action action ->
            if(action in BoulderAction){
                data += """<boulderAction>
${getBaseActionProperties(action)}
</boulderAction>
"""
            }
        }
        return data
    }

    public static String getFindItemActions(Map map){
        String data = ""
        map.actions.sort { it.positionX + "" + it.positionY } .each { Action action ->
            if(action in FindItemAction){
                data += """<findItemAction>
${getBaseActionProperties(action)}
</findItemAction>
"""
            }
        }
        return data
    }

    public static String getBushActions(Map map){
        String data = ""
        map.actions.sort { it.positionX + "" + it.positionY } .each { Action action ->
            if(action in BushAction){
                data += """<bushAction>
${getBaseActionProperties(action)}
</bushAction>
"""
            }
        }
        return data
    }

    public static String getMarketActions(Map map){
        String data = ""
        map.actions.sort { it.positionX + "" + it.positionY } .each { Action action ->
            if(action in MarketAction){
                data += """<marketAction>
${getBaseActionProperties(action)}
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
${getBaseActionProperties(action)}
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
${getBaseActionProperties(action)}
${action.message}
</mapMessage>
"""
            }
        }
        return data
    }

    public static String getPvpSelectActions(Map map){
        String data = ""
        map.actions.sort { it.positionX + "" + it.positionY } .each { Action action ->
            if(action in PvpSelectAction){
                data += """<pvpSelectAction>
${getBaseActionProperties(action)}
</pvpSelectAction>
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
${getBaseActionProperties(action)}
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
${getBaseActionProperties(action)}
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
${getBaseActionProperties(action)}
${action.jumpTo?.identifier}
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
