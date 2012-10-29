<%@ page import="game.context.Gender" %>
<table class='computerList'>
    <tr>
        <td style='width:16px;text-align:center;'>#</td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td style='width:150px;'>Name</td>
        <td>HP</td>
        <td style='width:30px;text-align:center;'>Lv.</td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
    </tr>
    <g:each in="${ownerPokemonList.sort{it.partyPosition}}" var="ownerPokemon" status="i">

        <tr>
            <td style='text-align:center;'>${ownerPokemon.partyPosition}</td>
            <td>
                <img src='${resource(uri:'')}/images/pkmn/${ownerPokemon.pokemon.threeValueNumber()}ani.gif'>
            </td>
            <td>
                <g:if test="${ownerPokemon.pokemon.type1}">
                    <img src='${resource(uri:'')}/images/types/${ownerPokemon.pokemon.type1}.png'>
                </g:if>
            </td>
            <td>
                <g:if test="${ownerPokemon.pokemon.type2}">
                    <img src='${resource(uri:'')}/images/types/${ownerPokemon.pokemon.type2}.png'>
                </g:if>
            </td>
            <td>
                <g:if test="${ownerPokemon.gender == Gender.Male}">
                    <img src='${resource(uri:'')}/images/gender/male.png'>
                </g:if>
                <g:else>
                    <img src='${resource(uri:'')}/images/gender/female.png'>
                </g:else>
            </td>
            <td><a href='${createLink(action:'pokemon',id:ownerPokemon.pokemon.id)}' onclick="return getPokemonData(this)">${ownerPokemon.pokemon.name}</a></td>
            <td>${ownerPokemon.hp}/${ownerPokemon.calculateHP()}</td>
            <td style='text-align:center;'>${ownerPokemon.level}</td>
            <td style='text-align:center;width:70px;white-space:nowrap;'>
                <g:render template="hpBar" model="${[hp:ownerPokemon.hp,maxHp:ownerPokemon.calculateHP()]}" />
                <br><g:render template="expBar" model="${[level:ownerPokemon.level, levelRate: ownerPokemon.pokemon.levelRate, xp:ownerPokemon.xp]}" />
            <g:if test="${i != 0}">
                <td><a href='${createLink(action:'moveUp',id:ownerPokemon.id)}'>up</a></td>
            </g:if>
            <g:else>
                <td></td>
            </g:else>
            <g:if test="${i != ownerPokemonList.size()-1}">
                <td><a href='${createLink(action:'moveDown',id:ownerPokemon.id)}'>down</a></td>
            </g:if>
            <g:else>
                <td></td>
            </g:else>
            <td>
                <g:if test="${computerView}">
                    <a href='${createLink(action:'deposit',id:ownerPokemon.id)}'>deposit</a>
                </g:if>
            </td>

        </tr>
    </g:each>
    <g:each in="${(ownerPokemonList?.size()+1..6)}" var="i">
        <tr>
            <td style='text-align:center;'>${i}</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
    </g:each>
</table>