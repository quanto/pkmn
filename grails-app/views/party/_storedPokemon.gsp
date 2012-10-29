<%@ page import="game.context.Gender" %>
<div  style='height:246px;overflow:auto;'>
    <table class='computerList'>
        <tr>
            <td></td>
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
        </tr>

        <g:each in="${computerList}" var="ownerPokemon" status="i">
            <tr>
                <td></td>
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
                <td><a href='battleEngine/pokedex.php?nr=" . $row["nr"] . "'>${ownerPokemon.pokemon.name}</a></td>
                <td>${ownerPokemon.hp}/${ownerPokemon.calculateHP()}</td>
                <td style='text-align:center;'>${ownerPokemon.level}</td>
                <td style='text-align:center;width:70px;white-space:nowrap;'>
                <g:render template="hpBar" model="${[hp:ownerPokemon.hp,maxHp:ownerPokemon.calculateHP()]}" />
                <br><g:render template="expBar" model="${[level:ownerPokemon.level, levelRate: ownerPokemon.pokemon.levelRate, xp:ownerPokemon.xp]}" />

                <td><a href='${createLink(action:'add',id:ownerPokemon.id)}'>withdraw</a></td>
                <td><a href='${createLink(action:'release',id:ownerPokemon.id)}' onclick="return confirm('Are you sure you want to release this pokemon?');">release</a></td>
            </tr>
        </g:each>
    </table>
</div>