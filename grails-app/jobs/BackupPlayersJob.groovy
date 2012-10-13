import game.Player
import game.OwnerPokemon
import game.OwnerMove
import game.Role

class BackupPlayersJob {

    static triggers = {
            simple name:'backupPlayersJob', startDelay:30 * 1000, repeatInterval: 60 * 1000
    }

    def concurrent = false

    def execute() {
        println "Backup player data " + new Date().toString()

        Player.list().each{ Player player ->

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
${player.mail}
${player.ip}
${player.registerDate}
${player.map.name}
${player.lastRecoverAction.map.name}
${player.lastRecoverAction.positionX}
${player.lastRecoverAction.positionY}
${player.positionX}
${player.positionY}
${player.money}
${player.view}
${player.pveBattlesWon}
${player.pveBattlesLost}
${player.pvnBattlesWon}
${player.pvnBattlesLost}
</playerData>
${roleData}
"""



                List<OwnerPokemon> ownerPokemonList = OwnerPokemon.findAllByOwner(player)

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

                playerData += ownerPokemonData

                FileWriter fstream = new FileWriter(file);
                BufferedWriter out = new BufferedWriter(fstream);

                out.write(playerData)

                out.close();
            }catch (Exception e){
                System.err.println("Error: " + e.getMessage());
            }

        }

    }
}