<g:each in="${ownerMoves}" var="ownerMove">
    <a href="" onclick="return getMenu('&replaceMoveId=${ownerMove.id}');">${ownerMove.move.name}</a>
    <br>
</g:each>

<a href='' onclick="return getMenu('');">I changed my mind</a>