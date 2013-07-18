package data

import game.AltMap
import game.Map
import game.action.*
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
${map.owner?.username?:'kevin'}
</mapData>
${getPokemonData(map)}
${getActionData(map.actions)}
${getAltMapsData(map)}
"""
    }

    public static getActionData(def actions){
        """${getMaptransitions(actions)}
${getComputerActions(actions)}
${getRecoverActions(actions)}
${getMessageActions(actions)}
${getNpcActions(actions)}
${getMarketActions(actions)}
${getPvpSelectActions(actions)}
${getBoulderActions(actions)}
${getBushActions(actions)}
${getFindItemActions(actions)}
${getPersonActions(actions)}
${getMessagePersonActions(actions)}
"""
    }

    public static String getAltMapsData(Map map){
        String altMapData = map.altMaps.collect { getAltMapData(it) }.join('\n')?:''
        return altMapData
    }

    public static String getAltMapData(AltMap altMap){
        return """<altMapData>
${altMap.newDataBackground}
${altMap.newDataForeground}
${altMap.newActions}
${altMap.dataBackground?:''}
${altMap.dataForeground?:''}
${altMap.condition}
${altMap.priority}
</altMapData>
${getActionData(altMap.actions)}
"""
    }

    public static String getBaseActionProperties(Action action){
        """${action.positionX}
${action.positionY}
${action.identifier}
${action.condition?.name()?:''}
${action.conditionMetMessage?:''}
${action.conditionNotMetMessage?:''}
${action.triggerOnActionButton}
${action.triggerBeforeStep}
${action.conditionalStep}
${action.placeOneTimeActionLock}
${action.image?:''}
${action.correctionLeft?:''}
${action.correctionTop?:''}
${action.cssClass?:''}"""
    }

    public static String getPersonActionProperties(Action action){
        """${getBaseActionProperties(action)}
${action.characterImage.name()}
${action.macro}"""
    }

    public static String getPersonActions(def actions){
        String data = ""
        actions.sort { it.positionX + "" + it.positionY } .each { Action action ->
            if(action in PersonAction){
                data += """<personAction>
${getPersonActionProperties(action)}
</personAction>
"""
            }
        }
        return data
    }

    public static String getMessagePersonActions(def actions){
        String data = ""
        actions.sort { it.positionX + "" + it.positionY } .each { Action action ->
            if(action in PersonAction){
                data += """<messagePersonAction>
${getPersonActionProperties(action)}
${action.message}
</messagePersonAction>
"""
            }
        }
        return data
    }

    public static String getBoulderActions(def actions){
        String data = ""
        actions.sort { it.positionX + "" + it.positionY } .each { Action action ->
            if(action in BoulderAction){
                data += """<boulderAction>
${getBaseActionProperties(action)}
</boulderAction>
"""
            }
        }
        return data
    }

    public static String getFindItemActions(def actions){
        String data = ""
        actions.sort { it.positionX + "" + it.positionY } .each { Action action ->
            if(action in FindItemAction){
                data += """<findItemAction>
${getBaseActionProperties(action)}
</findItemAction>
"""
            }
        }
        return data
    }

    public static String getBushActions(def actions){
        String data = ""
        actions.sort { it.positionX + "" + it.positionY } .each { Action action ->
            if(action in BushAction){
                data += """<bushAction>
${getBaseActionProperties(action)}
</bushAction>
"""
            }
        }
        return data
    }

    public static String getMarketActions(def actions){
        String data = ""
        actions.sort { it.positionX + "" + it.positionY } .each { Action action ->
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

    public static String getNpcActions(def actions){
        String data = ""
        actions.sort { it.positionX + "" + it.positionY } .each { Action action ->
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

    public static String getMessageActions(def actions){
        String data = ""
        actions.sort { it.positionX + "" + it.positionY } .each { Action action ->
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

    public static String getPvpSelectActions(def actions){
        String data = ""
        actions.sort { it.positionX + "" + it.positionY } .each { Action action ->
            if(action in PvpSelectAction){
                data += """<pvpSelectAction>
${getBaseActionProperties(action)}
</pvpSelectAction>
"""
            }
        }
        return data
    }

    public static String getRecoverActions(def actions){
        String data = ""
        actions.sort { it.positionX + "" + it.positionY } .each { Action action ->
            if(action in RecoverAction){
                data += """<recoverAction>
${getBaseActionProperties(action)}
</recoverAction>
"""
            }
        }
        return data
    }

    public static String getComputerActions(def actions){
        String data = ""
        actions.sort { it.positionX + "" + it.positionY } .each { Action action ->
            if(action in ComputerAction){
                data += """<computerAction>
${getBaseActionProperties(action)}
</computerAction>
"""
            }
        }
        return data
    }

    public static String getMaptransitions(def actions){
        String data = ""
        actions.sort { it.positionX + "" + it.positionY } .each { Action action ->
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
