<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <meta name="layout" content="game">
    </head>
    <body>
        <g:each in="${maps}" var="map">
            <g:render template="mapLinks" model="[map:map]" />
            <g:render template="/game/layerMap" model="${[map:map]}" />
        </g:each>
    </body>
</html>