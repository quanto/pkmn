<a href="http://kevinverhoef.nl"></a>
<a href="http://fabianwouters.nl"></a>
<sec:ifLoggedIn>
    Ingelogd als: <sec:loggedInUserInfo field="username"/>
    | <a href="${createLink(controller: 'logout')}">Uitloggen</a>
    | <a href="${createLink(controller: 'game')}">Game</a>
</sec:ifLoggedIn>
<sec:ifAnyGranted roles="ROLE_ADMIN">
    | <a href="${createLink(controller: 'main')}">Main</a>
    | <a href="${createLink(controller: 'mapEditor')}">Map editor</a>
    | <a href="${createLink(controller: 'mapEditor', action:'worldMap')}">World map</a>

    | <a href="${createLink(controller: 'admin')}">Admin panel</a>
    | <a href="${createLink(controller: 'battleTest')}">Battle test</a>
</sec:ifAnyGranted>