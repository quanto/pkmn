<%@ page import="game.OwnerMove" %>
Moves<br>

<g:each in="${ownerMoveList?.sort{ it.move.name } }" var="ownerMove">
    <a onclick="return doAction('${createLink(action:'doMove',id:ownerMove.id)}');" href="">${ownerMove.move.name} ${ownerMove.ppLeft}/${ownerMove.move.pp}</a>
    <br>
</g:each>
<a href='' onclick="return getMenu('');">Back</a>