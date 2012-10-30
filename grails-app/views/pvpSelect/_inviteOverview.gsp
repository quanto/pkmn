<h1>Current invites</h1>
<table>
    <g:each in="${invites}" var="invite">
        <tr>
            <td>
                ${invite.player.username}
            </td>
            <td>
                ${invite.dateCreated}
            </td>
            <td>
                Accept
            </td>
        </tr>
    </g:each>
</table>