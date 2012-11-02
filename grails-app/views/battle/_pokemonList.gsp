
<table>
    <tr><td></td><td>name</td><td>hp</td><td>lvl</td><td>gender</td></tr>

    <g:each in="${ownerPokemonList}" var="ownerPokemon">
        <tr>
            <td><img src='${resource(uri:'')}/images/pkmn/${ownerPokemon.pokemon.threeValueNumber()}ani.gif'></td>
            <td>${ownerPokemon.pokemon.name}</td>
            <td>${ownerPokemon.hp}/${ownerPokemon.calculateHP()}</td>
            <td>${ownerPokemon.level}</td>
            <td>${ownerPokemon.gender}</td>
            <g:if test="${ownerPokemon.hp > 0 && fightPlayer.ownerPokemon.id != ownerPokemon.id}">
                <td><a href='' onclick="return doAction('${createLink(action:'switchPokemon',id:ownerPokemon.partyPosition)}');">switch</a></td>
            </g:if>
        </tr>
    </g:each>
</table>

<g:if test="${!mustChoose}">
    <a href='' onclick="return getMenu('')">Back</a>
</g:if>