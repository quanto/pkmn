<%@ page import="map.CharacterImage" contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <meta name='layout' content='game'/>
    </head>
    <body>
        <g:each in="${CharacterImage.values()}" var="character">
            <img src="/game/images/chars/${character}.png"/>
            ${character}<br />
        </g:each>
    </body>
</html>