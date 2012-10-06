<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title></title>
</head>
<body>

    <g:link action="editor">New Map</g:link>
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
                    <g:link controller="map" action="edit" id="${map.id}">Pokemon</g:link>
                </td>

            </tr>
        </g:each>

    </table>

</body>
</html>