<%@ page import="game.Pokemon" contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <meta name='layout' content='game'/>
    </head>
    <body>

        Map: ${map.name}

        <table>
            <tr>
                <td>
                    Pokemon
                </td>
                <td>
                    Lvl. range
                </td>
                <td>
                    Chance
                </td>
            </tr>
            <g:each in="${mapPokemonList}" var="mapPokemon">
                <tr>
                    <td>
                        ${mapPokemon.pokemon}
                    </td>
                    <td>
                        ${mapPokemon.fromLevel} - ${mapPokemon.toLevel}
                    </td>
                    <td>
                        ${mapPokemon.chance}
                    </td>
                    <td>
                        <g:link action="remove" params="${[id:map.id,mapPokemon:mapPokemon.id]}">Remove</g:link>
                    </td>
                </tr>
            </g:each>
        </table>
        <g:form action="add" method="post" id="${map.id}">
            <g:hiddenField name="map.id" value="${map.id}" />
            <table>
                <tr>
                    <td>Pokemon:</td>
                    <td>
                        <g:select name="pokemon.id" from="${Pokemon.list()}" optionKey="id" />
                    </td>
                </tr>
                <tr>
                    <td>Level from:</td>
                    <td>
                        <g:textField name="fromLevel" />
                    </td>

                </tr>
                <tr>
                    <td>Level to:</td>
                    <td>
                        <g:textField name="toLevel" />
                    </td>
                </tr>
                <tr>
                    <td>Chance:</td>
                    <td>
                        <g:textField name="chance" />
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td><g:submitButton name="add" /></td>
                </tr>
            </table>
        </g:form>
    </body>
</html>