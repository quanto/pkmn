<%@ page import="game.Player" %>
<g:render template="/layouts/tiles/messages" />

<table>
    <tr>
        <td>Player</td>
        <td>Map</td>
        <td>Online?</td>
    </tr>
    <g:each in="${friends}" var="player">
        <g:if test="${ownPlayer != player || 1==1}">
            <tr>
                <td id='row'>${player.username}</td>
                <td id='row'>${player.map.name}</td>
                <g:if test="${onlinePlayers.find{ it.username == player.username} }">
                    <td class='online'>Online</td>
                </g:if>
                <g:else>
                    <td class='offline'>Offline</td>
                </g:else>
            <tr>
        </g:if>
    </g:each>

</table>
<br />
<g:if test="${friendRequests}">
    <table>
        <tr>
            <td>Invite</td>
            <td>
            </td>
        </tr>
        <g:each in="${friendRequests}" var="friendRequest">
            <tr>
                <td>${friendRequest.player.username}</td>
                <td>
                    <g:link action="acceptInvite" controller="online" id="${friendRequest.id}">Accept</g:link> / <g:link action="declineInvite" controller="online" id="${friendRequest.id}">Decline</g:link>
                </td>
            </tr>
        </g:each>
    </table>
</g:if>

<g:form controller="online" action="invite">
    Invite: <g:textField name="playerName"/>
    <g:submitButton name="invite" value="invite" />
</g:form>