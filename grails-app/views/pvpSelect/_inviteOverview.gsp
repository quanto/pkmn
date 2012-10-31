<h1>Current invites</h1>
<table>
    <g:each in="${invites}" var="invite">
        <g:if test="${invite.player.id != player.id}">
            <tr>
                <td>
                    ${invite.inviteNr}
                </td>
                <td>
                    ${invite.player.username}
                </td>
                <td>
                    ${invite.dateCreated}
                </td>
                <td>
                    <g:link action="acceptInvite" id="${invite.inviteNr}">
                        Accept
                    </g:link>
                </td>
            </tr>
        </g:if>
    </g:each>
</table>

    <g:form action="createInvite" method="post">
        <table>
            <tr>
                <td></td>
                <td><g:submitButton name="createInvite" value="Create Invite" /></td>
            </tr>

        </table>
    </g:form>

</table>