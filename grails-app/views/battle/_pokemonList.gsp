
<table>
    <tr><td></td><td>name</td><td>hp</td><td>lvl</td><td>gender</td></tr>

    <g:each in="${fightPokemonList}" var="fightPokemon">
        <tr>
            <td><img src='${resource(uri:'')}/images/pkmn/${fightPokemon.threeValueNumber()}ani.gif'></td>
            <td>${fightPokemon.name}</td>
            <td>${fightPokemon.hp}/${fightPokemon.maxHp}</td>
            <td>${fightPokemon.level}</td>
            <td>${fightPokemon.gender}</td>
            <g:if test="${fightPokemon.hp > 0 && fightPlayer.fightPokemon.ownerPokemonId != fightPokemon.ownerPokemonId}">
                <td><a href='' onclick="return doAction('${createLink(action:'switchPokemon',id:fightPokemon.partyPosition)}');">switch</a></td>
            </g:if>
        </tr>
    </g:each>
</table>

<g:if test="${!mustChoose}">
    <a href='' onclick="return getMenu('')">Back</a>
</g:if>