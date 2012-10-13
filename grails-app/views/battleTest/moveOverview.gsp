<%@ page import="game.Pokemon" contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title></title>
        <meta name='layout' content='main'/>
        <style>

            #pokemonList {
                font-size:12px;
            }

        </style>
    </head>
    <body>
        <table id="pokemonList">
            <g:each in="${Pokemon.list()}" var="pokemon">
                <tr>
                    <td style="background-color: #eee;">
                        ${pokemon.name}
                    </td>
                </tr>

                <g:each in="${game.LearnableMove.findAllByPokemon(pokemon)}" var="learnableMove">
                    <tr>
                        <td>
                            ${learnableMove.move.name}
                        </td>
                        <td style="width:40px;">
                            ${learnableMove.learnLevel}
                        </td>
                        <td style="width:40px;background-color: ${learnableMove.move.implemented==1?'green':'red'};">
                            ${learnableMove.move.implemented}
                        </td>
                    </tr>

                </g:each>

            </g:each>
        </table>
    </body>
</html>