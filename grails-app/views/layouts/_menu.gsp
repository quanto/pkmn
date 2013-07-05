
<span style="color:red;">Version ${grailsApplication.metadata.'app.version'}</span> |

<sec:ifLoggedIn>
    Ingelogd als: <sec:loggedInUserInfo field="username"/>
    | <a href="${createLink(controller: 'logout')}">Uitloggen</a>
    | <a href="${createLink(controller: 'game')}">Game</a>
</sec:ifLoggedIn>

<sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MAP_EDITOR">
| <a href="${createLink(controller: 'mapEditor')}">Map editor</a>
</sec:ifAnyGranted>
<sec:ifAnyGranted roles="ROLE_ADMIN">
    | <a href="${createLink(controller: 'main')}">Main</a>
    | <a href="${createLink(controller: 'mapEditor', action:'worldMap')}">World map</a>
    | <a href="${createLink(controller: 'battleTest')}">Battle test</a>
</sec:ifAnyGranted>