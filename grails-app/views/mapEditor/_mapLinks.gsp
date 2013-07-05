<%@ page import="game.Action; game.MapTransition; game.MapPokemon" %>
<g:link action="editor" target="_blank" id="${map.id}">${map?.name}</g:link>
<g:link controller="pokemonEditor" target="_blank" action="edit" id="${map.id}">Pokemon ${MapPokemon.countByMap(map)}</g:link>
<g:link controller="actionEditor" target="_blank" action="actions" id="${map.id}">Actions ${Action.countByMap(map)}</g:link>
<g:link controller="mapEditor" target="_blank" action="transitions" id="${map.id}">Transitions: ${MapTransition.countByMap(map)}</g:link>