package game

import game.context.PlayerData
import game.social.FriendRequest

class OnlineController {

    def sessionRegistry

    def index() {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        def friendRequests = FriendRequest.findByInvitedPlayer(player)

        render template: 'online', model: [friends: player.friends, ownPlayer: player, friendRequests: friendRequests, onlinePlayers: sessionRegistry.getAllPrincipals()]
    }

    def invite(String playerName){

        Player invitedPlayer = Player.findByUsername(playerName)
        if (!invitedPlayer){
            flash.errorMessage = "Username not found"
            redirect action: 'index'
            return
        }

        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        if (invitedPlayer.username == player.username){
            flash.errorMessage = "Cannot invite self"
            redirect action: 'index'
            return
        }

        if (player.friends.find{ it.username == invitedPlayer.username }){
            flash.errorMessage = "User already in friends list"
            redirect action: 'index'
            return
        }

        if (FriendRequest.findByPlayerAndInvitedPlayer(player,invitedPlayer)){
            flash.errorMessage = "Friend request already send"
            redirect action: 'index'
            return
        }

        FriendRequest friendRequest = new FriendRequest(
                player: player,
                invitedPlayer: invitedPlayer
        )

        friendRequest.save()

        flash.message = "Friend request created"
        redirect action: 'index'
    }

    def acceptInvite(long id){
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        FriendRequest friendRequest = FriendRequest.findByIdAndInvitedPlayer(id, player)

        if (friendRequest){
            FriendRequest.withSession {
                friendRequest.player.addToFriends(player)
                player.addToFriends(friendRequest.player)
                friendRequest.delete()
                flash.message = "Friend added"
            }
        }
        redirect action: 'index'
    }

    def declineInvite(long id){
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        FriendRequest friendRequest = FriendRequest.findByIdAndInvitedPlayer(id, player)

        if (friendRequest){
            FriendRequest.withSession {
                friendRequest.delete()
                flash.message = "Friend request declined"
            }
        }
        redirect action: 'index'
    }

}
