package data

import game.Player
import game.Role
import map.View

class PlayerBackup {


    public static void exportPlayers(){
        println "Backup player data " + new Date().toString()

        Player.list().each{ Player player ->

            exportPlayer(player)

        }
    }

    public static void exportPlayer(Player player){
        try{
            File file = new File("import/players/" + player.username + ".txt")

            String roleData = ""
            player.getAuthorities().each { Role role ->
                roleData += """<role>
${role.authority}
</role>
"""
            }


            String playerData =
                """<playerData>
${player.username}
${player.name}
${player.password}
${player.enabled}
${player.email}
${player.ip}
${player.registerDate.format("dd-MM-yyyy HH:mm:ss")}
${player.map.name}
${player.lastRecoverAction.map.name}
${player.lastRecoverAction.positionX}
${player.lastRecoverAction.positionY}
${player.positionX}
${player.positionY}
${player.money}
${player.view == View.Battle?View.ShowMap:player.view}
${player.pveBattlesWon}
${player.pveBattlesLost}
${player.pvnBattlesWon}
${player.pvnBattlesLost}
${player.pvpBattlesWon}
${player.pvpBattlesLost}
${player.characterImage}
</playerData>
${roleData}
${ItemBackup.getItemBackupData(player)}
${NpcLockBackup.getNpcLockData(player)}
${getFriends(player)}
"""

            playerData += OwnerBackup.getOwnerPokemonBackupData(player)

            FileWriter fstream = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fstream);

            out.write(playerData)

            out.close();
        }catch (Exception e){
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static String getFriends(Player player){
        String friends = player.friends.collect{ it.username }.join('\n')

        return """<friends>
${friends}
</friends>"""
    }

}
