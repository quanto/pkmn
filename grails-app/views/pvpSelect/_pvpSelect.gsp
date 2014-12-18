<div ng-show="!model.waiting">
    <h1>Current invites</h1>
    <table>
        <tr ng-repeat="invite in model.invites">
            <td>
                {{invite.inviteNr}}
            </td>
            <td>
                {{invite.player.username}}
            </td>
            <td>
                {{invite.dateCreated}}
            </td>
            <td>
                <input type="button" value="Accept" ng-click="acceptInvite(invite.inviteNr)" />
            </td>
        </tr>
    </table>


    <table>
        <tr>
            <td>
                <input type="button" value="Refresh" ng-click="reload()" />
            </td>
            <td>
                <input type="button" value="Create Invite" ng-click="createInvite()" />
            </td>
        </tr>

    </table>


</div>
<div ng-show="model.waiting">
    <h1>Waiting for someone to accept</h1>

    <input type="button" value="Cancel Invite" ng-click="cancelInvite()" />

</div>

<p>
    <a href='#' ng-click="exit()">Exit</a>
</p>