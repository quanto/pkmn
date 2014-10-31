<%@ page import="game.OwnerMove" %>
Moves<br>

<g:each in="${fightMovesList?.sort{ it.move.name } }" var="fightMove">
    <a onclick="return doAction('${createLink(action:'doMove',id:fightMove.ownerMove.id)}');" href="">${fightMove.move.name} ${fightMove.ppLeft}/${fightMove.move.pp}</a>
    <br>
</g:each>
<a href='' onclick="return getMenu('');">Back</a>