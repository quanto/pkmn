package data

import game.Player
import game.Map
import game.RecoverAction
import map.View
import game.OwnerPokemon
import game.Pokemon
import game.context.Gender
import game.OwnerMove
import game.Move
import game.PlayerRole
import game.Role
import game.Owner
import game.OwnerItem
import game.Item
import game.NpcLock
import game.Npc

class PlayerImport {

    public static void importPlayers(){

        println "Import player lines"

        new File('import/players/').listFiles().each { File file ->

            String node = ""

            def parts = []

            Player player
            OwnerPokemon ownerPokemon

            file.eachLine { line ->

                if (line.contains("</playerData>")){
                    node = ""
                    player = createPlayer(parts)
                    parts = []
                }
                else if (line.contains("</ownerPokemon>")){
                    node = ""
                    ownerPokemon = createOwnerPokemon(player, parts)
                    ownerPokemon.save()
                    parts = []
                }
                else if (line.contains("</ownerMove>")){
                    node = ""
                    createOwnerMove(ownerPokemon, parts)
                    parts = []
                }
                else if (line.contains("</role>")){
                    node = ""
                    createRole(player, parts)
                    parts = []
                }
                else if (line.contains("</ownerItem>")){
                    node = ""
                    createOwnerItem(player, parts)
                    parts = []
                }
                else if (line.contains("</npcLock>")){
                    node = ""
                    createNpcLock(player, parts)
                    parts = []
                }

                if (node){
                    parts.add(line)
                }

                if (line.contains("<playerData>")){
                    node = "playerData"
                }
                else if (line.contains("<ownerPokemon>")){
                    node = "ownerPokemon"
                }
                else if (line.contains("<ownerMove>")){
                    node = "ownerMove"
                }
                else if (line.contains("<role>")){
                    node = "role"
                }
                else if (line.contains("<ownerItem>")){
                    node = "ownerItem"
                }
                else if (line.contains("<npcLock>")){
                    node = "npcLock"
                }
            }

        }
    }

    public static void createNpcLock(Player player,def parts){
        new NpcLock(
                player: player,
                npc: Npc.findByIdentifier(parts[0]),
                dateCreated: new Date().parse("dd-MM-yyyy HH:mm:ss",parts[1]),
                permanent: new Boolean(parts[2])
        ).save()
    }

    public static void createOwnerItem(Owner owner,def parts){
        OwnerItem ownerItem = new OwnerItem(
                owner: owner,
                item: Item.findByName(parts[0]),
                quantity: Integer.parseInt(parts[1])
        )
        owner.addToOwnerItems(ownerItem)
        owner.save()
    }

    public static void createOwnerMove(OwnerPokemon ownerPokemon, def parts){
        OwnerMove ownerMove = new OwnerMove(
                move:Move.findByName(parts[0]),
                ppLeft: Integer.parseInt(parts[1])
        )
        ownerPokemon.addToOwnerMoves(ownerMove)
        ownerMove.ownerPokemon = ownerPokemon
    }

    public static void createRole(Player player, def parts){
        PlayerRole playerRole = new PlayerRole(
                player: player,
                role: Role.findByAuthority(parts[0])
        )
        playerRole.save()
    }

    public static OwnerPokemon createOwnerPokemon(Owner player, def parts){
        OwnerPokemon ownerPokemon = new OwnerPokemon(
                isNpc:new Boolean(parts[0]),
                pokemon:Pokemon.findByNr(Integer.parseInt(parts[1])),
                hpIV: Integer.parseInt(parts[2]),
                attackIV: Integer.parseInt(parts[3]),
                defenseIV: Integer.parseInt(parts[4]),
                spAttackIV: Integer.parseInt(parts[5]),
                spDefenseIV: Integer.parseInt(parts[6]),
                speedIV: Integer.parseInt(parts[7]),
                hp: Integer.parseInt(parts[8]),
                gender: Gender.values().find{ it.toString() == parts[9] },
                partyPosition: Integer.parseInt(parts[10]),
                xp: Integer.parseInt(parts[11]),
                level: Integer.parseInt(parts[12])
        )
        ownerPokemon.owner = player
        return ownerPokemon
    }

    public static Player createPlayer(def parts){
        Player player = new Player(
                username: parts[0],
                name:parts[1],
                password:parts[2],
                enabled:new Boolean(parts[3]),
                email:parts[4]!='null'?parts[4]:null,
                ip:parts[5]!='null'?parts[5]:null,
                registerDate:new Date().parse("dd-MM-yyyy HH:mm:ss",parts[6]),
                map: Map.findByName(parts[7]),
                lastRecoverAction: RecoverAction.findByMapAndPositionXAndPositionY(Map.findByName(parts[8]),Integer.parseInt(parts[9]),Integer.parseInt(parts[10])),
                positionX: Integer.parseInt(parts[11]),
                positionY: Integer.parseInt(parts[12]),
                money: Integer.parseInt(parts[13]),
                view: View.values().find { it.toString() == parts[14] },
                pveBattlesWon: Integer.parseInt(parts[15]),
                pveBattlesLost: Integer.parseInt(parts[16]),
                pvnBattlesWon: Integer.parseInt(parts[17]),
                pvnBattlesLost: Integer.parseInt(parts[18]),
                pvpBattlesWon: Integer.parseInt(parts[18]),
                pvpBattlesLost: Integer.parseInt(parts[20])
        )
        player.save()
        Player.executeUpdate("update Player set password = :password where username = :username",[username:parts[0],password: parts[2]])
        return player
    }
}
