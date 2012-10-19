<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
    <meta name='layout' content='main'/>
</head>
<body>

    <g:link action="editor">New Map</g:link> | <g:link action="worldMap">World Map</g:link>
    <br/>
    <table>

        <g:each in="${maps}" var="map">
            <tr>
                <td>
                    <g:link action="editor" id="${map.id}">${map.id}</g:link>
                </td>
                <td>
                    ${map.name}
                </td>
                <td>
                    ${map.active?'Active':''}
                </td>
                <td>
                    <g:link action="editor" id="${map.id}">Map</g:link>
                </td>
                <td>
                    <g:link controller="pokemonEditor" action="edit" id="${map.id}">Pokemon</g:link>
                </td>
                <td>
                    <g:link controller="actionEditor" action="actions" id="${map.id}">Actions</g:link>
                </td>
                <td>
                    <g:link action="export" id="${map.id}">Export</g:link>
                </td>

            </tr>
        </g:each>

    </table>

</body>
</html>