<%@ page import="game.MapTransition; game.MapPokemon" %>
<g:link action="editor" id="${map.id}">${map?.name}</g:link>
<g:link controller="pokemonEditor" action="edit" id="${map.id}">Pokemon ${MapPokemon.countByMap(map)}</g:link>
<g:link controller="actionEditor" action="actions" id="${map.id}">Actions</g:link>
<g:link controller="mapEditor" action="transitions" id="${map.id}">Transitions: ${MapTransition.countByMap(map)}</g:link>