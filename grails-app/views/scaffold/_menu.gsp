<li><a class="home" href="${createLink(controller: 'main')}"><g:message code="default.home.label"/></a></li>
<li><a class="players" href="${createLink(controller: 'player')}">Players</a></li>
<li><a class="maps" href="${createLink(controller: 'map')}">Maps</a></li>
<li><a class="pkmn" href="${createLink(controller: 'pokemon')}">PKMN</a></li>
<li><a class="items" href="${createLink(controller: 'item')}">Items</a></li>
<li><a class="list" href="${createLink(controller: 'newsItem')}">News</a></li>
<g:if test="${controllerName == 'map'}">
    <li><g:link controller="mapEditor" action="editor">New Map</g:link></li>
    <sec:ifAnyGranted roles="ROLE_ADMIN">
        <li><g:link controller="mapEditor" action="worldMap">World Map</g:link></li>
        <li><g:link controller="mapEditor" action="exportMaps">Export Maps</g:link></li>
    </sec:ifAnyGranted>
</g:if>