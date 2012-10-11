<%@ page import="game.Player" %>
<table>
    <tr>
        <th>Player</th>
        <th>Map</th>
        <th>Online?</th>
    </tr>
    <g:each in="${players}" var="player">
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