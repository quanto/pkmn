package game

import game.context.PlayerData
import game.social.FriendRequest
import grails.converters.JSON

class OnlineController {

    def sessionRegistry

    def index() {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        def friendRequests = FriendRequest.findByInvitedPlayer(player)

        def onlinePlayers = sessionRegistry.getAllPrincipals()

        def model = [
                friendRequests: friendRequests.collect { FriendRequest request -> [
                    id: request.id,
                    username: request.player.username,
                ]},

                friends: player.friends.collect {[
                        username: it.username,
                        online: onlinePlayers.contains{ it.username == player.username},
                        map: it.map.name,
                ]}
        ]
        render model as JSON
    }

    def invite(String playerName){

        Player invitedPlayer = Player.findByUsername(playerName)
        if (!invitedPlayer){
            render text: "Username not found"
            return
        }

        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        if (invitedPlayer.username == player.username){
            render text: "Cannot invite self"
            return
        }

        if (player.friends.find{ it.username == invitedPlayer.username }){
            render text: "User already in friends list"
            return
        }

        if (FriendRequest.findByPlayerAndInvitedPlayer(player,invitedPlayer)){
            render text: "Friend request already send"
            return
        }

        FriendRequest friendRequest = new FriendRequest(
                player: player,
                invitedPlayer: invitedPlayer
        )

        friendRequest.save()

        render text: "Friend request created"
    }

    def acceptInvite(long id){
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        FriendRequest friendRequest = FriendRequest.findByIdAndInvitedPlayer(id, player)

        if (friendRequest){
            FriendRequest.withSession {
                friendRequest.player.addToFriends(player)
                player.addToFriends(friendRequest.player)
                friendRequest.delete(flush:true)
                render text: "Friend added"
                return
            }
        }
        render text: ""
    }

    def declineInvite(long id){
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        FriendRequest friendRequest = FriendRequest.findByIdAndInvitedPlayer(id, player)

        if (friendRequest){
            FriendRequest.withSession {
                friendRequest.delete(flush:true)
                render text: "Friend request declined"
                return
            }
        }
        render text: ""
    }

}
