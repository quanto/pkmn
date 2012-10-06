package game

import org.apache.commons.lang.builder.HashCodeBuilder

class PlayerRole implements Serializable {

	Player player
	Role role

	boolean equals(other) {
		if (!(other instanceof PlayerRole)) {
			return false
		}

		other.player?.id == player?.id &&
			other.role?.id == role?.id
	}

	int hashCode() {
		def builder = new HashCodeBuilder()
		if (player) builder.append(player.id)
		if (role) builder.append(role.id)
		builder.toHashCode()
	}

	static PlayerRole get(long playerId, long roleId) {
		find 'from PlayerRole where player.id=:playerId and role.id=:roleId',
			[playerId: playerId, roleId: roleId]
	}

	static PlayerRole create(Player player, Role role, boolean flush = false) {
		new PlayerRole(player: player, role: role).save(flush: flush, insert: true)
	}

	static boolean remove(Player player, Role role, boolean flush = false) {
		PlayerRole instance = PlayerRole.findByPlayerAndRole(player, role)
		if (!instance) {
			return false
		}

		instance.delete(flush: flush)
		true
	}

	static void removeAll(Player player) {
		executeUpdate 'DELETE FROM PlayerRole WHERE player=:player', [player: player]
	}

	static void removeAll(Role role) {
		executeUpdate 'DELETE FROM PlayerRole WHERE role=:role', [role: role]
	}

	static mapping = {
		id composite: ['role', 'player']
		version false
	}
}
