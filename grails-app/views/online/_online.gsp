
<table>
    <tr>
        <td>Player</td>
        <td>Map</td>
        <td></td>
    </tr>

    <tr ng-repeat="friend in model.friends">
        <td><a href="#" ng-click="setPrivateMessage(friend.username)">{{friend.username}}</a></td>
        <td>{{friend.map}}</td>
        <td ng-show="friend.online" class='online'>Online</td>
        <td ng-show="!friend.online" class='offline'>Offline</td>
    <tr>

</table>
<br />

<table ng-show="model.friendRequests.length>0">
    <tr>
        <td>Invite</td>
        <td>
        </td>
    </tr>

    <tr ng-repeat="friendRequest in model.friendRequests">
        <td>{{friendRequest.username}}</td>
        <td>
            <button ng-click="acceptInvite(friendRequest.id)">Accept</button>
            <button ng-click="declineInvite(friendRequest.id)">Decline</button>
        </td>
    </tr>

</table>


Invite: <g:textField name="playerName" ng-model="inviteUsername" />
<button ng-click="invitePlayer(inviteUsername)">invite</button>
<br/>
{{inviteFeedback}}
